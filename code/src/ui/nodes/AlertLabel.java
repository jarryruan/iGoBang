package ui.nodes;

import javafx.scene.control.Label;

/**
 * 警示文本
 */

public class AlertLabel extends Label {
    public AlertLabel(String content){
        super(content);
        getStyleClass().addAll("alert_label");
    }
}
