package ui.frameworks.dialogs;

import kernel.UILinker;
import ui.Configure;
import ui.animations.EasingProperty;
import ui.frameworks.ContentPane;
import ui.nodes.AlertLabel;

import java.io.File;

/**
 * 存档名称输入对话框
 */

public class NameInputDialog extends InputDialog {
    protected AlertLabel alertLabel;
    protected ContentPane contentPane;
    protected UILinker uiLinker;
    private EasingProperty labelAnimation;

    public NameInputDialog() {
        super("请输入存档名称");
        contentPane = ContentPane.getSelf();
        uiLinker = UILinker.getSelf();
        alertLabel = new AlertLabel("请确保存档名不为空、不包含以下任何字符: /\\:*?\"<>|，并且不与其它存档重名");
        alertLabel.setLayoutX(40);
        alertLabel.setLayoutY(200);
        alertLabel.setOpacity(0.0);
        labelAnimation = new EasingProperty(alertLabel.opacityProperty());
        getChildren().add(alertLabel);
        getButtonBar().getButton(0).setText("保存");
        getButtonBar().getButton(0).setOnAction(e -> {
            String name = textField.getText();
            if (validName(name) && freeToWrite(name)) {
                uiLinker.saveGame(name);
                contentPane.showDialog(contentPane.getSaveDialog());
                contentPane.getSaveDialog().refresh();
                uiLinker.setCurrentSave(name);
            } else {
                showAlert();
            }
        });
        getButtonBar().getButton(1).setOnAction(e -> {
            contentPane.showDialog(contentPane.getGameDisplay());
        });
    }

    public boolean validName(String name) {
        if (name.length() == 0 || name.contains(".") || name.contains("/") ||
                name.contains("\\") || name.contains(":") || name.contains("*") ||
                name.contains("?") || name.contains("\"") || name.contains("<") ||
                name.contains(">") || name.contains("|"))
            return false;

        return true;
    }
    //判断是否非法输入

    public boolean freeToWrite(String name) {
        File file = new File("saves/" + name + ".game");
        if (file.exists() && !name.equals(uiLinker.getCurrentSave())) {
            return false;
        }
        return true;
    }
    //判断存档名是否与其它存档名冲突（防止覆盖）

    public void showAlert() {
        labelAnimation.setToValue(1.0);
    }
    //显示警示信息

    public void fadeAlert() {
        labelAnimation.setToValue(0.0);
    }
    //隐藏警示信息
}
