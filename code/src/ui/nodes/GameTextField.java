package ui.nodes;

import javafx.scene.control.TextField;

/**
 * 输入框
 */

public class GameTextField extends TextField{
    public GameTextField(){
        super();
        this.getStyleClass().add("game_text_field");
    }
}
