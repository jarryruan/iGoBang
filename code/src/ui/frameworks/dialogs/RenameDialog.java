package ui.frameworks.dialogs;

import ui.Configure;

import java.io.File;

/**
 * 存档重命名对话框
 */

public class RenameDialog extends NameInputDialog {
    public RenameDialog() {
        getButtonBar().getButton(0).setOnAction(e -> {
            String targetName = textField.getText();
            int index = contentPane.getSaveDialog().getListView().getSelectionModel().getSelectedIndex();
            String name = contentPane.getSaveDialog().getListView().getItems().get(index);
            if (name.equals(targetName)) {
                contentPane.showDialog(contentPane.getSaveDialog());
                return;
            }
            File targetFile = new File("saves/" + targetName + ".game");
            if (validName(targetName) && !targetFile.exists()) {
                File file = new File("saves/" + name + ".game");
                file.renameTo(targetFile);
                contentPane.getSaveDialog().getListView().getItems().set(index, targetName);
                contentPane.showDialog(contentPane.getSaveDialog());
            } else {
                showAlert();
            }
        });
        getButtonBar().getButton(1).setOnAction(e -> {
            contentPane.showDialog(contentPane.getSaveDialog());
        });
    }
}
