package ui.frameworks.dialogs;

import javafx.scene.layout.Pane;

/**
 * 弹窗基础类
 */

public class PopupBase extends Pane {
    private double preferredWidth;
    private double preferredHeight;

    public PopupBase(double preferredWidth, double preferredHeight) {
        getStyleClass().add("popup_base");
        this.preferredWidth = preferredWidth;
        this.preferredHeight = preferredHeight;
        this.setPrefSize(preferredWidth, preferredHeight);
    }

    public double getPreferredWidth() {
        return this.preferredWidth;
    }

    public double getPreferredHeight() {
        return this.preferredHeight;
    }
}
