# BGS (Background Sound System)

## 概要
`BGS` クラスは、Minecraft のサーバー上で特定の場所に継続的に BGM を再生するためのシステムです。指定した半径内のプレイヤーに対して、距離に応じた音量調整を行いながらサウンドを再生します。

## 機能
- **BGMの再生:** 指定した座標 (`Location`) を中心に、一定の半径 (`radius`) 内にいるプレイヤーにサウンドを再生します。
- **音量調整:** プレイヤーが中心座標から離れるほど音量が小さくなります（ステレオモードが `false` の場合）。
- **ステレオモード:** `true` にすると、音量調整なしで一定の音量で再生されます。
- **クールダウン設定:** `cooldown` により、一定間隔でサウンドが再生されます。
- **BGSの停止:** `cancel()` メソッドで BGS を停止できます。

## 使い方
### インスタンスの作成
```java
BGS bgs = new BGS("MyBGS", "minecraft:music_disc.pigstep", false, 10, 5, someLocation, someSection);
```
- `"MyBGS"`: BGSの名前
- `"minecraft:music_disc.pigstep"`: 再生するサウンドの識別子
- `false`: ステレオモード（`false` の場合は距離による音量調整あり）
- `10`: 半径（この範囲内のプレイヤーに音を再生）
- `5`: クールダウン（5秒ごとにサウンドを再生）
- `someLocation`: サウンドの再生位置
- `someSection`: 設定データの格納場所

### 位置の更新
```java
bgs.setLocation(newLocation);
```
- 再生位置を変更し、設定データを更新します。

### 停止
```java
bgs.cancel();
```
- BGS のサウンド再生を停止します。

## 注意点
- `Bukkit.getScheduler().scheduleSyncRepeatingTask` を使用しており、Bukkit のスケジューラが有効である必要があります。
- `SoundCategory.MASTER` で再生されるため、プレイヤーのマスター音量設定に依存します。

