# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## プロジェクト概要

シンジケートローン管理システム - 複数の金融機関が協調して大規模融資を行うシンジケートローンの管理システムです。Spring Boot + Java 17で構築されたController中心型アーキテクチャを採用しています。

## アーキテクチャ特徴

**Controller中心型アーキテクチャ** - Service層を禁止し、業務ロジックをController内に直接実装することで仕様書とコードの直接対応を重視しています：

- **Service層禁止**: 業務ロジックはController内に直接実装
- **Repository直接呼び出し**: ControllerからRepositoryを直接利用
- **@Transactional適用**: Controller メソッドレベルでトランザクション管理
- **機能横断レイヤー構造**: entity/repository/controller/dto の構成

## 技術スタック

- **Java**: 17
- **Spring Boot**: 3.2.1
- **Spring Data JPA**: データアクセス層
- **H2 Database**: 開発用インメモリDB
- **Maven**: ビルドツール
- **Lombok**: ボイラープレートコード削減
- **SpringDoc OpenAPI**: API文書化

## 開発コマンド

### アプリケーション起動
```bash
mvn spring-boot:run
```

### テスト実行
```bash
# 単体テスト実行
mvn test

# テストカバレッジレポート生成
mvn test jacoco:report

# シナリオテスト実行
./test_scenario.sh
./test_update_scenario.sh
```

### ビルド
```bash
# コンパイルのみ
mvn compile

# パッケージ作成
mvn package

# 依存関係のクリーンインストール
mvn clean install
```

### ヘルスチェック
```bash
curl http://localhost:8080/hello
```

## プロジェクト構成

```
src/main/java/com/example/syndicatelending/
├── common/         # 共通値オブジェクト・例外・インフラストラクチャ
│   ├── application/exception/  # 業務例外クラス
│   └── domain/model/          # 共通値オブジェクト（Money, Percentage）
├── controller/     # REST APIエンドポイント（業務ロジック実装）
├── domain/         # ドメイン固有のビジネスルール・バリデーター
├── dto/            # APIリクエスト/レスポンス用DTO
├── entity/         # JPAエンティティ（全業務領域共通）
└── repository/     # Spring Data JPAリポジトリ
```

## 主要業務機能とAPI

| 機能 | エンドポイント | 主要クラス |
|------|----------------|------------|
| 参加者管理 | `/api/parties/**` | PartyController |
| シンジケート | `/api/v1/syndicates/**` | SyndicateController |
| ファシリティ | `/api/v1/facilities/**` | FacilityController |
| ドローダウン | `/api/loans/drawdowns/**` | DrawdownController |

## 重要な設計原則

### Controller中心実装パターン
- 複雑な業務ロジックをController内に直接実装
- 複数Repositoryの直接利用
- トランザクション境界の明確化（1 HTTPリクエスト = 1 トランザクション）

### エラーハンドリング戦略
```java
try {
    // メイン処理
} catch (BusinessRuleViolationException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
} catch (ResourceNotFoundException ex) {
    return ResponseEntity.status(404).body(ex.getMessage());
} catch (Exception ex) {
    return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
}
```

### Value Object活用
- **Money**: 金額計算の精度保証
- **Percentage**: 持分比率の正確な管理
- **各種Enum**: 列挙型による分類の標準化

## 主要エンティティ関係

- **Company**: 企業情報
- **Borrower**: 借り手
- **Investor**: 投資家
- **Syndicate**: シンジケート団
- **Facility**: 融資枠
- **SharePie**: 投資家出資割合
- **Loan**: ローン
- **Drawdown**: ドローダウン（Transaction継承）
- **FacilityInvestment**: 投資記録

## 重要な業務ロジック

### ファシリティ作成時の投資記録自動生成
```java
// 投資金額 = コミットメント × 投資家の出資割合
investment.setAmount(commitment.multiply(pie.getShare().getValue()));
```

### ドローダウン処理での二重エンティティ作成
1. Loanエンティティ作成（ローン情報保持）
2. Drawdownエンティティ作成（Transaction継承、取引記録）

## データベース設定

- **H2 Console**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (空白)

## OpenAPI文書

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## コード規約

- **@Transactional**: Controller メソッドレベルで必須
- **例外処理**: 業務例外とシステム例外の明確な分離
- **Value Object**: 金額や割合はMoney/Percentageクラスを使用
- **Repository直接呼び出し**: Service層を経由せずController内で直接利用