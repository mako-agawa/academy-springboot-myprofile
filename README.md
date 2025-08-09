# My Profile アプリ
## テーブル図
    制作予定
## ER図
![ER図](/public/myProfile_erfigure.png)


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