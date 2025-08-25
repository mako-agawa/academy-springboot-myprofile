### 対象チケット
[【個人開発11】項目追加機能実装
](https://prum.backlog.com/view/PRUM_ACADEMY-5522) 

### 概要
項目追加機能を実装する

### 内容
1. 学習カテゴリーごとに学習項目を追加できるようにした。
2. 要件にかなわないものには各種バリデーションメッセージが表示される。
3. 登録完了のモーダルが表示され、項目一覧画面へ戻る。

### 主要なファイル
**【Controller】**
- LearningDataController.java の更新

**【Service】**
- LearningDataService.java の更新

**【Repository】**
- LearningDataRepository.java の更新
→検索用メソッドを追加

**【Web】**
- SkillForm.javaを作成。※項目追加のデータを受け取るため。

**【resources】**
- /skill/new.htmlを作成。
- add-modal.jsを作成。

============================================

### 対象チケット
[【個人開発12】項目編集機能実装
](https://prum.backlog.com/view/PRUM_ACADEMY-5523) 

### 概要
項目追加編集機能を実装する

### 内容
1. 学習時間を変更し、「学習時間を保存する」ボタンを押下すると学習時間の編集ができようにした。
2. 編集完了のモーダルが表示され、項目一覧画面へ戻る。

### 主要なファイル
**【Controller】**
- LearningDataController.java の更新
→　updateTimeRecordメソッドを追加

**【Service】**
- LearningDataService.java の更新

**【Web】**
- TimeRecordFrom.javaを作成。※項目追加のデータを受け取るため。

**【resources】**
- /skill/index.htmlを作成。
- timeUpdate-modal.jsを作成。


============================================

### 対象チケット
[【個人開発13】項目削除機能実装
](https://prum.backlog.com/view/PRUM_ACADEMY-5524) 

### 概要
項目の削除機能を実装する

### 内容
1. 「削除する」ボタンを押下すると該当の項目が削除されるようにした。
2. その後、削除完了のモーダルが表示され、項目一覧画面へ戻る。

### 主要なファイル
**【Controller】**
- LearningDataController.java の更新
→　deleteSkillメソッドを追加

**【Service】**
- LearningDataService.java の更新　 
→ deleteByIdメソッドを追加

**【Web】**
- TimeRecordFrom.javaを作成。※項目追加のデータを受け取るため。

**【resources】**
- /skill/index.htmlを更新。
- delete-modal.jsを作成。


============================================


### 対象チケット
[【個人開発14】スキルチャート表示実装
](https://prum.backlog.com/view/PRUM_ACADEMY-5525) 

### 概要
スキルチャート表示の実装をする

### 内容
1. ログインユーザーの月毎のデータを取得。
2. 該当月のバックエンド、フロントエンド、インフラのデータごとに学習時間をチャートに表示。

### 主要なファイル
**【Controller】**
- LearningDataController.java の更新
→　

**【Service】**
- LearningDataService.java の更新　 
→ 

**【Web】**
- 

**【resources】**
- /index.htmlを更新。
- learning-chart.jsを作成。


============================================