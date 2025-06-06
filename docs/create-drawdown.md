# Loan Bounded Context - Drawdown作成処理フロー

## 概要

ドローダウン（資金引き出し）作成処理の流れを説明します。POSTエンドポイント `/api/loans/drawdowns` を起点とした処理フローを詳述します。

**特徴**: 本システムの設計思想に従い、Controllerに業務ロジックを直接実装し、仕様書とコードの対応を明確にしています。

## 処理フロー概要

1. **CreateDrawdownRequest受信** - リクエストデータの受け取り
2. **Loan作成** - ドローダウンに対応するLoanエンティティの作成
3. **Drawdown作成** - Transaction継承したDrawdownエンティティの作成
4. **データ永続化** - Loan、Drawdownの順次保存
5. **レスポンス返却** - 作成されたDrawdownエンティティの返却

## シーケンス図

```mermaid
sequenceDiagram
    participant Client
    participant DrawdownController
    participant LoanRepository
    participant DrawdownRepository
    participant Loan
    participant Drawdown

    Client->>DrawdownController: POST /api/loans/drawdowns
    Note over DrawdownController: CreateDrawdownRequest受信

    DrawdownController->>Loan: new Loan()
    DrawdownController->>Loan: setFacilityId(facilityId)
    DrawdownController->>Loan: setBorrowerId(borrowerId)
    DrawdownController->>Loan: setPrincipalAmount(Money)
    DrawdownController->>Loan: setOutstandingBalance(Money)
    DrawdownController->>Loan: setAnnualInterestRate(Percentage)
    DrawdownController->>Loan: setDrawdownDate(date)
    DrawdownController->>Loan: setRepaymentPeriodMonths(months)
    DrawdownController->>Loan: setRepaymentCycle(cycle)
    DrawdownController->>Loan: setRepaymentMethod(method)
    DrawdownController->>Loan: setCurrency(currency)

    DrawdownController->>LoanRepository: save(loan)
    LoanRepository-->>DrawdownController: savedLoan

    DrawdownController->>Drawdown: new Drawdown()
    DrawdownController->>Drawdown: setLoanId(savedLoan.getId())
    DrawdownController->>Drawdown: setCurrency(currency)
    DrawdownController->>Drawdown: setPurpose(purpose)
    DrawdownController->>Drawdown: setFacilityId(facilityId)
    DrawdownController->>Drawdown: setBorrowerId(borrowerId)
    DrawdownController->>Drawdown: setTransactionDate(drawdownDate)
    DrawdownController->>Drawdown: setAmount(Money)

    DrawdownController->>DrawdownRepository: save(drawdown)
    DrawdownRepository-->>DrawdownController: savedDrawdown

    DrawdownController-->>Client: HTTP 201 + Drawdown entity
```

## 詳細処理フロー

### 1. リクエスト受信
- **エンドポイント**: `POST /api/loans/drawdowns`
- **Content-Type**: `application/json`
- **リクエストボディ**: `CreateDrawdownRequest`

### 2. Loanエンティティ作成・設定

#### 2.1 Loanインスタンス生成
```java
Loan loan = new Loan();
```

#### 2.2 基本情報設定
- **facilityId**: ファシリティID（外部キー）
- **borrowerId**: 借り手ID（外部キー）
- **currency**: 通貨コード（例: JPY, USD）

#### 2.3 金額・金利設定
- **principalAmount**: `Money.of(request.getAmount())` - 元本金額
- **outstandingBalance**: `Money.of(request.getAmount())` - 初期残高（元本と同額）
- **annualInterestRate**: `Percentage.of(request.getAnnualInterestRate())` - 年利率

#### 2.4 返済条件設定
- **drawdownDate**: ドローダウン実行日
- **repaymentPeriodMonths**: 返済期間（月数）
- **repaymentCycle**: 返済サイクル（例: "MONTHLY"）
- **repaymentMethod**: 返済方法（例: "EQUAL_INSTALLMENT"）

### 3. Loanエンティティ永続化
- **Repository**: `LoanRepository.save(loan)`
- **戻り値**: `savedLoan` - 保存されたLoanエンティティ（IDが自動生成済み）

### 4. Drawdownエンティティ作成・設定

#### 4.1 Drawdownインスタンス生成
```java
Drawdown drawdown = new Drawdown();
```
- **継承関係**: `Drawdown extends Transaction`
- **自動設定**: `transactionType = "DRAWDOWN"`（コンストラクタで設定）

#### 4.2 基本情報設定
- **loanId**: 保存されたLoanのID（`savedLoan.getId()`）
- **currency**: 通貨コード
- **purpose**: ドローダウン目的

#### 4.3 Transaction継承フィールド設定
- **facilityId**: ファシリティID
- **borrowerId**: 借り手ID
- **transactionDate**: ドローダウン実行日
- **amount**: `Money.of(request.getAmount())` - 取引金額

### 5. Drawdownエンティティ永続化
- **Repository**: `DrawdownRepository.save(drawdown)`
- **戻り値**: `savedDrawdown` - 保存されたDrawdownエンティティ

### 6. レスポンス返却
- **HTTPステータス**: 201 Created
- **Content-Type**: `application/json`
- **レスポンスボディ**: 作成された`Drawdown`エンティティ（JSON形式）

## リクエスト・レスポンス例

### リクエスト例
```json
POST /api/loans/drawdowns
Content-Type: application/json

{
  "facilityId": 1,
  "borrowerId": 1,
  "amount": 1000000,
  "currency": "JPY",
  "purpose": "設備投資",
  "annualInterestRate": 0.025,
  "drawdownDate": "2025-06-06",
  "repaymentPeriodMonths": 36,
  "repaymentCycle": "MONTHLY",
  "repaymentMethod": "EQUAL_INSTALLMENT"
}
```

### レスポンス例
```json
HTTP/1.1 201 Created
Content-Type: application/json

{
  "id": 1,
  "loanId": 1,
  "currency": "JPY",
  "purpose": "設備投資",
  "facilityId": 1,
  "borrowerId": 1,
  "transactionDate": "2025-06-06",
  "transactionType": "DRAWDOWN",
  "amount": {
    "amount": 1000000,
    "currency": "JPY"
  },
  "createdAt": "2025-06-06T10:30:00",
  "updatedAt": "2025-06-06T10:30:00",
  "version": 0
}
```

## エラーハンドリング

### 現状のエラー処理（改善が必要）
**注意**: 現在のコードは `try-catch` でController内でエラーを処理していますが、これはシステム設計方針に反しており、`GlobalExceptionHandler`への委譲が必要です。

```java
// ❌ 現状のパターン（改善対象）
try {
    // 処理
} catch (Exception ex) {
    return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
}
```

### 想定されるエラーケース

#### ResourceNotFoundException (HTTP 404)
- 指定されたファシリティが存在しない場合
- 指定された借り手が存在しない場合

#### BusinessRuleViolationException (HTTP 400)
- ドローダウン金額がファシリティの利用可能額を超過する場合
- 無効な返済条件が指定された場合
- 既に満期を迎えたファシリティに対するドローダウン要求

#### ValidationException (HTTP 400)
- 必須フィールドの未設定
- 金額が負の値
- 無効な通貨コード
- 無効な日付（過去日等）

## データモデル関係

```mermaid
erDiagram
    Facility ||--o{ Loan : "facility_id"
    Borrower ||--o{ Loan : "borrower_id"
    Loan ||--o{ Drawdown : "loan_id"
    Transaction ||--|{ Drawdown : "extends"
    
    Facility {
        Long id PK
        Long syndicateId FK
        Money commitment
        String currency
        LocalDate maturityDate
    }
    
    Loan {
        Long id PK
        Long facilityId FK
        Long borrowerId FK
        Money principalAmount
        Money outstandingBalance
        Percentage annualInterestRate
        LocalDate drawdownDate
        Integer repaymentPeriodMonths
        String repaymentCycle
        String repaymentMethod
        String currency
        LocalDateTime createdAt
        LocalDateTime updatedAt
        Long version
    }
    
    Transaction {
        Long id PK
        Long facilityId FK
        Long borrowerId FK
        LocalDate transactionDate
        String transactionType
        Money amount
        LocalDateTime createdAt
        LocalDateTime updatedAt
        Long version
    }
    
    Drawdown {
        Long loanId FK
        String currency
        String purpose
    }
```

## 実装特徴

### 1. Controller中心型設計
- **業務ロジック**: DrawdownControllerに直接実装
- **仕様書との対応**: 処理フローが仕様書と1:1対応
- **Service層**: 意図的に排除し、Repository直接呼び出し

### 2. エンティティ設計
- **Loan**: 集約ルート、監査フィールド・バージョン管理付き
- **Drawdown**: Transaction継承、`transactionType="DRAWDOWN"`自動設定
- **値オブジェクト**: Money、Percentageを積極的に使用

### 3. 永続化パターン
- **順次保存**: Loan → Drawdown の順序で永続化
- **外部キー設定**: DrawdownにLoanのIDを設定
- **トランザクション**: Controller層での`@Transactional`適用が必要（現在未実装）

### 4. 改善点
- **例外処理**: GlobalExceptionHandlerへの委譲
- **トランザクション管理**: `@Transactional`の追加
- **バリデーション**: `@Valid`による自動バリデーション
- **ビジネスルール検証**: ドメインバリデーターの追加

---

**このドキュメントは、現状のコード実装を基に作成されており、システムの設計思想である「Controller中心型」アーキテクチャの特徴を反映しています。**
