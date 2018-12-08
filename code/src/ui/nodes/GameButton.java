package ui.nodes;

import javafx.scene.control.Button;

/**
 * 普通按钮
 */

public class GameButton extends Button {
    public GameButton(String title) {
        super(title);
        this.getStyleClass().add("game_button");
    }

    public GameButton(String title, int fontSize) {
        this(title);
        this.setStyle("-fx-font-size:" + fontSize + ";");
    }
}
