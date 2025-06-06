# シンジケートローン管理システム

複数の金融機関が協調して大規模融資を行うシンジケートローンの管理システムです。Spring Boot + Java 17で構築されたController偏重アーキテクチャを採用しています。

## 🎯 システム概要

シンジケートローンは、複数の投資家（金融機関）が協調して単一の借り手に対して融資を行う仕組みです。本システムでは以下の主要機能を提供します：

- **参加者管理**: 企業、借り手、投資家の情報管理
- **シンジケート団組成**: リードバンクを中心とした投資家グループの管理
- **融資枠管理**: ファシリティの作成と投資家間の持分比率管理
- **投資記録管理**: ファシリティ組成時の投資家投資記録自動生成
- **取引処理**: ドローダウン、支払い、取引の記録・分配（一部実装済み）

## ⚡ 技術スタック

- **Java**: 17
- **Spring Boot**: 3.x
- **Spring Data JPA**: データアクセス層
- **H2 Database**: 開発用インメモリDB
- **Maven**: ビルドツール

## 🏗️ アーキテクチャ特徴

**Controller中心型アーキテクチャ**を採用し、仕様書とコードの直接対応を重視しています：

- **Service層禁止**: 業務ロジックはController内に直接実装
- **機能横断レイヤー構造**: entity/repository/controller/dto の構成
- **Repository直接呼び出し**: ControllerからRepositoryを直接利用
- **@Transactional適用**: Controller メソッドレベルでトランザクション管理

## 📁 プロジェクト構成

```
src/main/java/com/example/syndicatelending/
├── common/         # 共通値オブジェクト・例外・インフラストラクチャ
├── controller/     # REST APIエンドポイント（業務ロジック実装）
├── domain/         # ドメイン固有のビジネスルール・バリデーター
├── dto/            # APIリクエスト/レスポンス用DTO
├── entity/         # JPAエンティティ（全業務領域共通）
└── repository/     # Spring Data JPAリポジトリ
```

## 🚀 クイックスタート

### 前提条件
- Java 17
- Maven 3.6+

### アプリケーション起動
```bash
# プロジェクトのクローン
git clone <repository-url>
cd ddd-ai-bbom

# 依存関係のインストールと起動
mvn spring-boot:run
```

### 動作確認
```bash
# ヘルスチェック
curl http://localhost:8080/hello

# サンプルデータでのテスト実行
./test_scenario.sh
```

## 📊 主要API

| 機能 | エンドポイント | 説明 |
|------|----------------|------|
| 参加者管理 | `/api/parties/**` | 企業・借り手・投資家の管理 |
| シンジケート | `/api/v1/syndicates/**` | シンジケート団の組成・管理 |
| ファシリティ | `/api/v1/facilities/**` | 融資枠の作成・管理 |
| ドローダウン | `/api/loans/drawdowns/**` | 資金引き出し処理 |

詳細なAPI仕様は各コントローラーのJavaDocまたは `docs/` フォルダの処理フロー文書を参照してください。

## 🧪 テスト

```bash
# 単体テスト実行
mvn test

# シナリオテスト実行
./test_scenario.sh
./test_update_scenario.sh
```

## 📝 ドキュメント

- `docs/create-facility.md`: ファシリティ作成処理フロー
- `docs/create-drawdown.md`: ドローダウン作成処理フロー
- `.clinerules/`: 開発ガイドライン・アーキテクチャ仕様

## 🎨 設計哲学

本システムは「**ドメイン貧血症という非難を受けようとも、仕様書の通り愚直にControllerに処理を連ねる**」ことを最大の特徴としています。

- **仕様の透明性**: 仕様書とコードの1:1対応
- **実装の単純さ**: 過度な抽象化の排除
- **保守性重視**: 理解しやすく修正しやすい構造
