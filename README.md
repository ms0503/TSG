# TSG
TSGは軽く作ったインベーダー系のシューティングゲームです。
## Requirement
* Guava 30.1.1
* Java 16.0.2
* OpenJFX 16
## Installation
### Windows(only 64-bit)
[ここ](https://www.oracle.com/jp/java/technologies/javase-jdk16-downloads.html )から`jdk-16.0.2_windows-x64_bin.exe`をダウンロードし起動。  
画面の指示に従いインストール。  
田+Rで出たウィンドウに`sysdm.cpl`と入れてEnter。  
「詳細設定」→「環境変数」を開く。  
下の方の「新規」を押す。
「変数名」は`JAVA_HOME`、「変数値」は`C:\Program Files\Java\jdk-16.0.2`。  
次に下の方の`Path`をダブルクリック。  
「新規」を押し`%JAVA_HOME%\bin`を入力。  
開いてきたウィンドウをすべて閉じる。  
田+Rで出たウィンドウに`cmd`と入れてEnter。  
```cmd
> cd プロジェクトルート
> gradlew installDist
```
### Debian GNU/Linux・Ubuntu
```bash
$ sudo apt install openjdk-16-jre -y
$ cd プロジェクトルート
$ gradlew installDist
```
### RedHat Enterprise Linux・CentOS
```bash
$ sudo yum install java-16.0.2-openjdk -y
$ cd プロジェクトルート
$ gradlew installDist
```
### Fedora
```bash
$ sudo dnf install java-16.0.2-openjdk -y
$ cd プロジェクトルート
$ gradlew installDist
```
## Usage
### Windows
```cmd
> cd プロジェクトルート
> cd .\build\install\bin
> TSG.bat
```
### Un*x/Linux
```bash
$ cd プロジェクトルート
$ cd ./build/install/bin
$ ./TSG
```
## Author
Sora Tonami<ms0503@outlook.com>
* [Twitter](https://twitter.com/ms0503_/)
* [HomePage](https://www.ngri.jp/)
## License
TSG is under [GNU General Public License 3.0](./COPYING).  
`/src/main/resources/ms/sora/tsg/shoot.mp3`は[小森平様](https://taira-komori.jpn.org/arms01.html )のホームページより頂戴しました。
