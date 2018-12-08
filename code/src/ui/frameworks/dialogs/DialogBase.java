package ui.frameworks.dialogs;

import ui.nodes.ButtonBar;

/**
 * 对话框基础类
 */

public class DialogBase extends PopupBase {
    private boolean showButtonBar = false;
    private ButtonBar buttonBar;

    public DialogBase(double preferredWidth, double preferredHeight) {
        super(preferredWidth, preferredHeight);
        getStyleClass().add("dialog_base");
    }

    public DialogBase(double preferredWidth, double preferredHeight, boolean showButtonBar) {
        this(preferredWidth, preferredHeight);
        this.showButtonBar = showButtonBar;
        if (showButtonBar) {
            buttonBar = new ButtonBar();
            buttonBar.setPrefWidth(preferredWidth);
            buttonBar.layoutYProperty().bind(this.heightProperty().subtract(ButtonBar.height));
            buttonBar.layoutXProperty().bind(this.widthProperty().subtract(preferredWidth).divide(2));
            getChildren().add(buttonBar);
        }
    }

    public ButtonBar getButtonBar() {
        return this.buttonBar;
    }
    //取按钮栏
}
