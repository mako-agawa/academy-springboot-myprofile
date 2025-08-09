# My Profile アプリ
## サンプルデータ

| id | name  | email             | password_digest | biography                   | thumbnail_url                  |
|----|-------|-------------------|-----------------|-----------------------------|---------------------------------|
| 1  | Alice | alice@example.com | pass1234 | I am Alice. I love coding.  | https://example.com/alice.jpg  |
| 2  | Bob   | bob@example.com   | pass1234 | Hi, Bob here. Coffee addict.| https://example.com/bob.jpg    |


## コンテナ上でビルドを実行

1. クローンしたフォルダをvscodeで開き、ターミナルで以下のコマンドを叩き、Javaコンテナを起動します。
```
docker-compose up -d
```

2. コンテナを作成・起動したら、javaコンテナ内に入ります。
```
docker compose exec app bash
```
※ターミナルに以下のような文字列が表示されれば成功です。

`root@569495f96ff8:/app#`

3. ビルドを実行する
```
./gradlew build --continuous
```

## 別のターミナルから再度コンテナ内に入りSpringを起動

1. コンテナ内に入る
```
docker compose exec app bash
```

2. Springを起動
```
./gradlew bootRun
```