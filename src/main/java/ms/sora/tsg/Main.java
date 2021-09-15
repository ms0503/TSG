/*
 * TSG - Tonami's Shooting Game
 *
 * Copyright (C) 2021 Sora Tonami. Some rights reserved.
 *
 * This file is part of TSG.
 *
 * TSG is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TSG is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TSG.  If not, see <https://www.gnu.org/licenses/>.
 */

package ms.sora.tsg;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * アプリケーション
 *
 * @version 1.0.0
 */
public class Main extends Application {
    private static final Map<String, Boolean> flags = Maps.newHashMap();
    private static AnimationTimer gameLoop;
    // 初期値：4.0D
    private static double enemyBulletSpeed = 4.0D;
    private static double moveSpeed = 10.0D;
    // 1分(60秒 x ナノ秒変換の為の10億倍)
    private static long timeLimit = 60L * 1000000000L;
    private Controller controller;
    private MediaPlayer mp;
    private Random rand;
    private int score = 0;
    private long timerASecond = 0L;
    private long timerFifteenSeconds = 0L;
    private long start_ts = 0L;
    
    /**
     * メインメソッド
     *
     * @param args コマンドライン引数
     */
    public static void main(String[] args) {
        if(args.length > 0) for(String arg : args) {
            try {
                String[] temp;
                temp = arg.split(",");
                moveSpeed = Double.parseDouble(temp[0]);
                enemyBulletSpeed = Double.parseDouble(temp[1]);
            } catch(NumberFormatException ignored) {}
        }
        flags.put("bullet.00.isFire", false);
        flags.put("bullet.01.isFire", false);
        flags.put("bullet.02.isFire", false);
        flags.put("bullet.03.isFire", false);
        flags.put("bullet.04.isFire", false);
        flags.put("bullet.05.isFire", false);
        flags.put("bullet.06.isFire", false);
        flags.put("bullet.10.isFire", false);
        flags.put("bullet.11.isFire", false);
        flags.put("bullet.12.isFire", false);
        flags.put("bullet.13.isFire", false);
        flags.put("bullet.14.isFire", false);
        flags.put("bullet.15.isFire", false);
        flags.put("bullet.16.isFire", false);
        flags.put("bullet.20.isFire", false);
        flags.put("bullet.21.isFire", false);
        flags.put("bullet.22.isFire", false);
        flags.put("bullet.23.isFire", false);
        flags.put("bullet.24.isFire", false);
        flags.put("bullet.25.isFire", false);
        flags.put("bullet.26.isFire", false);
        flags.put("bullet.1.isFire", false);
        flags.put("bullet.2.isFire", false);
        flags.put("bullet.3.isFire", false);
        flags.put("bullet.4.isFire", false);
        flags.put("bullet.5.isFire", false);
        flags.put("enemy.00.isDead", false);
        flags.put("enemy.01.isDead", false);
        flags.put("enemy.02.isDead", false);
        flags.put("enemy.03.isDead", false);
        flags.put("enemy.04.isDead", false);
        flags.put("enemy.05.isDead", false);
        flags.put("enemy.06.isDead", false);
        flags.put("enemy.10.isDead", false);
        flags.put("enemy.11.isDead", false);
        flags.put("enemy.12.isDead", false);
        flags.put("enemy.13.isDead", false);
        flags.put("enemy.14.isDead", false);
        flags.put("enemy.15.isDead", false);
        flags.put("enemy.16.isDead", false);
        flags.put("enemy.20.isDead", false);
        flags.put("enemy.21.isDead", false);
        flags.put("enemy.22.isDead", false);
        flags.put("enemy.23.isDead", false);
        flags.put("enemy.24.isDead", false);
        flags.put("enemy.25.isDead", false);
        flags.put("enemy.26.isDead", false);
        flags.put("enemy.0.isTurned", false);
        flags.put("enemy.1.isTurned", true);
        flags.put("enemy.2.isTurned", false);
        flags.put("enemy.isAllDead", false);
        flags.put("player.isDead", false);
        flags.put("player.isLeft", false);
        flags.put("player.isRight", false);
        launch(args);
    }
    
    /**
     * {@inheritDoc}
     *
     * @param stage {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public void start(Stage stage) throws IOException, NullPointerException {
        rand = new Random();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280.0D, 720.0D);
        controller = fxmlLoader.getController();
        stage.setTitle("TSG");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
        scene.setOnKeyPressed(this::onKeyPressed);
        scene.setOnKeyReleased(this::onKeyReleased);
        mp = new MediaPlayer(new Media(Objects.requireNonNull(this.getClass().getResource("shoot.mp3")).toString()));
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(timerASecond == 0L) timerASecond = now;
                if(timerFifteenSeconds == 0L) timerFifteenSeconds = now;
                if(start_ts == 0L) start_ts = now;
                loop(now);
            }
        };
        gameLoop.start();
    }
    
    /**
     * ゲームループ
     *
     * @param ts フレームのタイムスタンプ(ナノ秒)
     */
    private void loop(long ts) {
        /// 裏処理1
        // 終了処理
        if(flags.get("enemy.isAllDead")) {
            controller.getMsg().setText("GAME CLEAR");
            controller.getMsg().setVisible(true);
            score *= timeLimit / 1000000000L - ts / 1000000000L + start_ts / 1000000000L;
            controller.getDebug().setText("Time: " + ((timeLimit / 1000000000L) - (ts / 1000000000L) + (start_ts / 1000000000L)) + "s / " + (timeLimit / 1000000000L) + "s");
            controller.getDebug().setText(controller.getDebug().getText() + "\nScore: " + score);
            controller.getDebug().setText(controller.getDebug().getText() + "\nPos: " + controller.getPlayer().getX() + " , " + controller.getPlayer().getY());
            gameLoop.stop();
            return;
        } else if(flags.get("player.isDead")) {
            controller.getMsg().setText("GAME OVER");
            controller.getMsg().setVisible(true);
            gameLoop.stop();
            return;
        } else if(ts - start_ts >= timeLimit) {
            controller.getMsg().setText("TIME UP");
            controller.getMsg().setVisible(true);
            gameLoop.stop();
            return;
        }
        // タイマー
        boolean isASecondLater = false;
        boolean isFifteenSecondsLater = false;
        if(ts - timerASecond >= 1000000000L) isASecondLater = true;
        if(ts - timerFifteenSeconds >= 15000000000L) isFifteenSecondsLater = true;
        
        /// プレイヤー処理
        // 移動受け付け
        if(flags.get("player.isLeft") && controller.getPlayer().getX() > -2.0D) controller.getPlayer().setX(controller.getPlayer().getX() - moveSpeed);
        if(flags.get("player.isRight") && controller.getPlayer().getX() < 1228.0D) controller.getPlayer().setX(controller.getPlayer().getX() + moveSpeed);
        // 弾管理
        for(int i = 0; i < 5; i++) {
            if(flags.get("bullet." + (i + 1) + ".isFire") && controller.getBullets()[i].getY() > 0.0D) {
                controller.getBullets()[i].setVisible(true);
                controller.getBullets()[i].setY(controller.getBullets()[i].getY() - 15.0D);
                // 敵の当たり判定
                for(int j = 0; j < 7; j++) {
                    if(controller.getBullets()[i].getY() < 98.0D) {
                        if(controller.getEnemies()[0][j].getX() + 6.0D <= controller.getBullets()[i].getX() && controller.getBullets()[i].getX() <= controller.getEnemies()[0][j].getX() + 34.0D) {
                            flags.put("bullet." + i + ".isFire", false);
                            flags.put("enemy.0" + j + ".isDead", true);
                        }
                    } else if(controller.getBullets()[i].getY() < 198.0D) {
                        if(controller.getEnemies()[1][j].getX() + 6.0D <= controller.getBullets()[i].getX() && controller.getBullets()[i].getX() <= controller.getEnemies()[1][j].getX() + 34.0D) {
                            flags.put("bullet." + i + ".isFire", false);
                            flags.put("enemy.1" + j + ".isDead", true);
                        }
                    } else if(controller.getBullets()[i].getY() < 298.0D) {
                        if(controller.getEnemies()[2][j].getX() + 6.0D <= controller.getBullets()[i].getX() && controller.getBullets()[i].getX() <= controller.getEnemies()[2][j].getX() + 34.0D) {
                            flags.put("bullet." + i + ".isFire", false);
                            flags.put("enemy.2" + j + ".isDead", true);
                        }
                    }
                }
            } else {
                flags.put("bullet." + (i + 1) + ".isFire", false);
                controller.getBullets()[i].setVisible(false);
                controller.getBullets()[i].setX(controller.getPlayer().getX() + 20.0D);
                controller.getBullets()[i].setY(controller.getPlayer().getY() - 32.0D);
            }
        }
        
        /// 敵処理
        flags.put("enemy.isAllDead", true);
        score = 0;
        for(int i = 0; i < 3; i++) {
            // 敵の折り返し
            if(isFifteenSecondsLater) flags.put("enemy." + i + ".isTurned", !flags.get("enemy." + i + ".isTurned"));
            for(int j = 0; j < 7; j++) {
                if(!flags.get("enemy." + i + j + ".isDead")) {
                    flags.put("enemy.isAllDead", false);
                    // 敵の移動
                    if(isASecondLater) {
                        if(flags.get("enemy." + i + ".isTurned")) controller.getEnemies()[i][j].setX(controller.getEnemies()[i][j].getX() + 10.0D);
                        else controller.getEnemies()[i][j].setX(controller.getEnemies()[i][j].getX() - 10.0D);
                    }
                    // 敵の弾管理
                    // Y640.0は一番下
                    if(flags.get("bullet." + i + j + ".isFire")) {
                        if(controller.getEnemyBullets()[i][j].getY() < 700.0D) {
                            controller.getEnemyBullets()[i][j].setVisible(true);
                            controller.getEnemyBullets()[i][j].setY(controller.getEnemyBullets()[i][j].getY() + enemyBulletSpeed);
                            // プレイヤーの当たり判定
                            if(controller.getEnemyBullets()[i][j].getY() > 631.0D && controller.getPlayer().getX() + 10.0D <= controller.getEnemyBullets()[i][j].getX() && controller.getPlayer().getX() + 31.0D >= controller.getEnemyBullets()[i][j].getX()) {
                                flags.put("bullet." + i + j + ".isFire", false);
                                flags.put("player.isDead", true);
                            }
                        // 一番下に行った時の処理
                        } else {
                            flags.put("bullet." + i + j + ".isFire", false);
                            controller.getEnemyBullets()[i][j].setVisible(false);
                            controller.getEnemyBullets()[i][j].setX(controller.getEnemies()[i][j].getX());
                            controller.getEnemyBullets()[i][j].setY(controller.getEnemies()[i][j].getY() + 32.0D);
                        }
                    // 乱数による発射調整
                    } else if(rand.nextInt(100) == 0) flags.put("bullet." + i + j + ".isFire", true);
                } else {
                    controller.getEnemies()[i][j].setImage(new Image(Objects.requireNonNull(getClass().getResource("enemyd.png")).toString()));
                    flags.put("bullet." + i + j + ".isFire", false);
                    controller.getEnemyBullets()[i][j].setVisible(false);
                    controller.getEnemyBullets()[i][j].setX(controller.getEnemies()[i][j].getX());
                    controller.getEnemyBullets()[i][j].setY(controller.getEnemies()[i][j].getY() + 32.0D);
                    score += 1000;
                }
            }
        }
        
        /// 裏処理2
        // デバッグ用テキストの更新
        controller.getDebug().setText("Time: " + ((timeLimit / 1000000000L) - (ts / 1000000000L) + (start_ts / 1000000000L)) + "s / " + (timeLimit / 1000000000L) + "s");
        controller.getDebug().setText(controller.getDebug().getText() + "\nScore: " + score);
        controller.getDebug().setText(controller.getDebug().getText() + "\nPos: " + controller.getPlayer().getX() + " , " + controller.getPlayer().getY());
        controller.getDebug().setText(controller.getDebug().getText() + "\nSpeed: " + moveSpeed);
        controller.getDebug().setText(controller.getDebug().getText() + "\nEnemyBulletSpeed: " + enemyBulletSpeed);
        // タイマーのタイムスタンプの更新
        if(isASecondLater) timerASecond = ts;
        if(isFifteenSecondsLater) timerFifteenSeconds = ts;
    }
    
    /**
     * 押されたキーを取得するイベントハンドラ
     *
     * @param event イベント
     */
    private void onKeyPressed(KeyEvent event) {
        switch(event.getCode()) {
            case A, LEFT -> flags.put("player.isLeft", true);
            case D, RIGHT -> flags.put("player.isRight", true);
            case ENTER, SPACE, UP, W -> {
                if(!flags.get("bullet.5.isFire")) {
                    if(!flags.get("bullet.4.isFire")) {
                        if(!flags.get("bullet.3.isFire")) {
                            if(!flags.get("bullet.2.isFire")) {
                                if(!flags.get("bullet.1.isFire")) flags.put("bullet.1.isFire", true);
                                else flags.put("bullet.2.isFire", true);
                            } else flags.put("bullet.3.isFire", true);
                        } else flags.put("bullet.4.isFire", true);
                    } else flags.put("bullet.5.isFire", true);
                    mp.seek(new Duration(0));
                    mp.play();
                }
            }
            default -> {}
        }
    }
    
    /**
     * 離されたキーを取得するイベントハンドラ
     *
     * @param event イベント
     */
    private void onKeyReleased(KeyEvent event) {
        switch(event.getCode()) {
            case A, LEFT -> flags.put("player.isLeft", false);
            case D, RIGHT -> flags.put("player.isRight", false);
            default -> {}
        }
    }
}
