/*
 * TSG - Tonami's Shooting Game
 *
 * Copyright (C) 2021 Sora Tonami
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
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * アプリケーション
 *
 * @version 1.0.0
 */
public class Main extends Application {
    private static final Map<String, Boolean> flags = Maps.newHashMap();
    private static AnimationTimer gameLoop;
    // 5分
    private static long timeLimit = 300000000000L;
    private Controller controller;
    private long start_ts = 0L;
    
    /**
     * メインメソッド
     *
     * @param args コマンドライン引数
     */
    public static void main(String[] args) {
        if(args.length > 0) timeLimit = Long.parseLong(args[0]);
        flags.put("bullet.1.isFire", false);
        flags.put("bullet.2.isFire", false);
        flags.put("bullet.3.isFire", false);
        flags.put("bullet.4.isFire", false);
        flags.put("bullet.5.isFire", false);
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
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280.0D, 720.0D);
        controller = fxmlLoader.getController();
        stage.setTitle("MCS2021 Shooting");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
        scene.setOnKeyPressed(this::onKeyPressed);
        scene.setOnKeyReleased(this::onKeyReleased);
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(start_ts == 0L) start_ts = now;
                loop(now);
            }
        };
        gameLoop.start();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
    
    /**
     * ゲームループ
     *
     * @param ts フレームのタイムスタンプ(ナノ秒)
     */
    private void loop(long ts) {
        if(ts - start_ts >= timeLimit) {
            gameLoop.stop();
            return;
        }
        if(flags.get("player.isLeft") && controller.getPlayer().getX() > -590.0D) controller.getPlayer().setX(controller.getPlayer().getX() - 10.0D);
        if(flags.get("player.isRight") && controller.getPlayer().getX() < 590.0D) controller.getPlayer().setX(controller.getPlayer().getX() + 10.0D);
        for(int i = 0; i < 5; i++) {
            if(flags.get("bullet." + (i + 1) + ".isFire") && controller.getBullets()[i].getY() > -600.0D) {
                controller.getBullets()[i].setVisible(true);
                controller.getBullets()[i].setY(controller.getBullets()[i].getY() - 15.0D);
            } else {
                flags.put("bullet." + (i + 1) + ".isFire", false);
                controller.getBullets()[i].setVisible(false);
                controller.getBullets()[i].setX(controller.getPlayer().getX());
                controller.getBullets()[i].setY(controller.getPlayer().getY() - 2.0D);
            }
        }
        //controller.getDebug().setText("Pos:" + controller.getPlayer().getX() + "," + controller.getPlayer().getY());
        controller.getDebug().setText("Time: " + (timeLimit - ts + start_ts) + "s / " + timeLimit + "s");
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
                if(!flags.get("bullet.1.isFire")) flags.put("bullet.1.isFire", true);
                else if(!flags.get("bullet.2.isFire")) flags.put("bullet.2.isFire", true);
                else if(!flags.get("bullet.3.isFire")) flags.put("bullet.3.isFire", true);
                else if(!flags.get("bullet.4.isFire")) flags.put("bullet.4.isFire", true);
                else if(!flags.get("bullet.5.isFire")) flags.put("bullet.5.isFire", true);
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
