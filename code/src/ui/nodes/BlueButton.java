package ui.nodes;

/**
 * 蓝色按钮
 */

public class BlueButton extends GameButton {
    public BlueButton(String title) {
        super(title);
        getStyleClass().add("blue_button");
    }

    public BlueButton(String title, int fontSize) {
        super(title, fontSize);
        getStyleClass().add("blue_button");
    }
}
