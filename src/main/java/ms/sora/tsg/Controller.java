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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.WindowEvent;

/**
 * GUIコントローラ
 *
 * @version 1.0.0
 */
public class Controller implements Initializable {
    @FXML
    private ImageView bullet1;
    @FXML
    private ImageView bullet2;
    @FXML
    private ImageView bullet3;
    @FXML
    private ImageView bullet4;
    @FXML
    private ImageView bullet5;
    @FXML
    private ImageView enemy00;
    @FXML
    private ImageView enemy01;
    @FXML
    private ImageView enemy02;
    @FXML
    private ImageView enemy03;
    @FXML
    private ImageView enemy04;
    @FXML
    private ImageView enemy05;
    @FXML
    private ImageView enemy06;
    @FXML
    private ImageView enemy10;
    @FXML
    private ImageView enemy11;
    @FXML
    private ImageView enemy12;
    @FXML
    private ImageView enemy13;
    @FXML
    private ImageView enemy14;
    @FXML
    private ImageView enemy15;
    @FXML
    private ImageView enemy16;
    @FXML
    private ImageView enemy20;
    @FXML
    private ImageView enemy21;
    @FXML
    private ImageView enemy22;
    @FXML
    private ImageView enemy23;
    @FXML
    private ImageView enemy24;
    @FXML
    private ImageView enemy25;
    @FXML
    private ImageView enemy26;
    @FXML
    private ImageView player;
    @FXML
    private Label debug;
    
    /**
     * {@inheritDoc}
     *
     * @param loc {@inheritDoc}
     * @param res {@inheritDoc}
     */
    @Override
    public void initialize(URL loc, ResourceBundle res) {
    }
    
    /**
     * コントローラ以外から弾のオブジェクトを操作する為に弾のオブジェクトを取得するメソッド
     *
     * @return 弾のオブジェクト1～5を格納した配列
     */
    public ImageView[] getBullets() {
        return new ImageView[] {bullet1, bullet2, bullet3, bullet4, bullet5};
    }
    
    /**
     * コントローラ以外からデバッグ用のラベルを操作する為にデバッグ用のラベルを取得するメソッド
     *
     * @return デバッグ用のラベル
     */
    public Label getDebug() {
        return debug;
    }
    
    public ImageView[][] getEnemies() {
        return new ImageView[][] {
            {enemy00, enemy01, enemy02, enemy03, enemy04, enemy05, enemy06},
            {enemy10, enemy11, enemy12, enemy13, enemy14, enemy15, enemy16},
            {enemy20, enemy21, enemy22, enemy23, enemy24, enemy25, enemy26}
        };
    }
    
    /**
     * コントローラ以外からプレイヤーのオブジェクトを操作する為にプレイヤーのオブジェクトを取得するメソッド
     *
     * @return プレイヤーのオブジェクト
     */
    public ImageView getPlayer() {
        return player;
    }
    
    /**
     * メニューから終了した時に呼ばれるメソッド
     *
     * @param event イベント
     */
    @FXML
    public void onQuit(ActionEvent event) {
        player.getScene().getWindow().fireEvent(new WindowEvent(player.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}
