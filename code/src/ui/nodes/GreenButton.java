package ui.nodes;

/**
 * 绿色按钮
 */

public class GreenButton extends GameButton {
    public GreenButton(String title) {
        super(title);
        this.getStyleClass().add("green_button");
    }

    public GreenButton(String title, int fontSize) {
        super(title, fontSize);
        this.getStyleClass().add("green_button");
    }
}
