package ui.nodes;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;

/**
 * 标题文本
 */

public class TitleLabel extends Label {
    public TitleLabel(String title) {
        super(title);
        getStyleClass().add("title_label");
        setContentDisplay(ContentDisplay.LEFT);
        setGraphicTextGap(10);
    }
}
