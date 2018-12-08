package ui.nodes;

import javafx.scene.control.Label;

/**
 * 正文文本
 */

public class ContextLabel extends Label {
    public ContextLabel(String title) {
        super(title);
        getStyleClass().add("context_label");
    }
}
