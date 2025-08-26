
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
- ChartApiController.javaを作成 
　→ /api/chart-dataに学習時間のデータをJSON形式で出力。

**【Service】**
- LearningDataService.java の更新　 
→ 

**【resources】**
- /skill/index.htmlを更新。
- learning-chart.jsを作成。/api/chart-dataからJSONデータを取得。


============================================