package ui.frameworks.dialogs;

import kernel.UILinker;
import ui.Configure;
import ui.frameworks.ContentPane;
import ui.nodes.*;
import javafx.scene.image.ImageView;

import java.io.File;

/**
 * 存档管理器对话框
 */

public class SaveDialog extends DialogBase {
    private TitleLabel titleLabel;
    private SaveListView listView;

    public SaveDialog() {
        super(1024, 600, true);
        titleLabel = new TitleLabel("管理存档");

        titleLabel.setGraphic(new ImageView(Configure.getResource("drawable/icon/save.png")));
        titleLabel.setLayoutX(20);
        titleLabel.setLayoutY(10);

        listView = new SaveListView();
        listView.setLayoutY(90);
        listView.setLayoutX(20);
        listView.prefWidthProperty().bind(this.widthProperty().subtract(40));
        listView.prefHeightProperty().bind(this.heightProperty().subtract(200));
        getChildren().addAll(titleLabel, listView);
        refresh();

        getButtonBar().addButton(new GameButton("刷新"), new BlueButton("载入"), new GameButton("重命名"), new RedButton("删除"));
        ContentPane contentPane = ContentPane.getSelf();
        getButtonBar().getButton(1).setDisable(true);
        getButtonBar().getButton(2).setDisable(true);
        getButtonBar().getButton(3).setDisable(true);
        getButtonBar().getButton(0).setOnAction(e -> refresh());
        getButtonBar().getButton(1).setOnAction(e -> {
            String name = listView.getSelectionModel().getSelectedItem();
            UILinker.getSelf().loadGame(name);
            UILinker.getSelf().setCurrentSave(name);
            contentPane.showDialog(contentPane.getGameDisplay());
        });
        getButtonBar().getButton(2).setOnAction(e -> {
            contentPane.getRenameDialog().fadeAlert();
            contentPane.getRenameDialog().getTextField().setText(listView.getSelectionModel().getSelectedItem());
            contentPane.showDialog(contentPane.getRenameDialog());
        });
        getButtonBar().getButton(3).setOnAction(e -> {
            int index = listView.getSelectionModel().getSelectedIndex();
            String name = listView.getItems().get(index);
            File file = new File("saves/" + name + ".game");
            file.delete();
            listView.getItems().remove(index);
        });
        listView.getSelectionModel().selectedIndexProperty().addListener(ov -> {
            boolean disabled = true;
            if (listView.getSelectionModel().getSelectedIndex() >= 0) {
                disabled = false;
            } else {
                disabled = true;
            }
            getButtonBar().getButton(1).setDisable(disabled);
            getButtonBar().getButton(2).setDisable(disabled);
            getButtonBar().getButton(3).setDisable(disabled);
        });


    }

    public SaveListView getListView() {
        return this.listView;
    }
    //取存档列表界面对象

    public void refresh() {
        listView.getItems().clear();
        File workingDir = new File("saves/");
        File[] files = workingDir.listFiles();
        if (files != null && files.length != 0) {
            for (int i = 0; i < files.length; i++) {
                String name = files[i].getName();
                if (name.toLowerCase().matches("^.+\\.game$")) {
                    listView.getItems().add(name.split("\\.")[0]);
                }
            }
        }
    }
    //刷新存档列表

}
