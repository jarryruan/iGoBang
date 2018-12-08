package ui.nodes;

/**
 * 红色按钮
 */

public class RedButton extends GameButton {
    public RedButton(String title) {
        super(title);
        this.getStyleClass().add("red_button");
    }

    public RedButton(String title, int fontSize) {
        super(title, fontSize);
        this.getStyleClass().add("red_button");
    }
}
