package ui.frameworks.dialogs;


import addons.MusicPlayer;
import javafx.scene.shape.Rectangle;
import ui.Configure;
import ui.nodes.SelectorListView;
import java.io.File;
import java.util.ArrayList;

/**
 * 音乐选择器
 */

public class MusicSelector extends PopupBase {
    public static final double width = 400;
    public static final double height = 200;
    private SelectorListView listView;
    private static final String[] music = {"Winter","Yet So Close",
            "Let's Cross The Rainbow","雨道","Masked Heroes",
            "Autumn Leaf","Letter","Utakata","天空を駆ける風の都"};
    private Rectangle rcBkg;

    public MusicSelector() {
        super(width, height);
        rcBkg = new Rectangle();
        rcBkg.getStyleClass().add("black_pad_background_rectangle");
        rcBkg.setWidth(width);
        rcBkg.setHeight(height);

        listView = new SelectorListView();
        listView.setPrefSize(width, height);

        initMusicList();
        listView.getSelectionModel().selectedItemProperty().addListener(ov -> {
            int index = listView.getSelectionModel().getSelectedIndex();
            if (index >= 1) {
                String name = listView.getItems().get(index);
                MusicPlayer.getSelf().play(Configure.getResource("music/" + name + ".mp3"));
            } else if (index == 0) {
                MusicPlayer.getSelf().stop();
            }
            Configure.setMusicIndex(index);
            Configure.writeConfigure();
        });
        Configure.readConfigure();
        listView.getSelectionModel().select(Configure.getMusicIndex());

        getChildren().addAll(rcBkg, listView);
    }

    private void initMusicList() {
        listView.getItems().add("无音乐");
        for (int i = 0; i < music.length; i++) {
            String name = music[i];
            listView.getItems().add(name);
        }
    }
    //初始化音乐列表

    private class MusicItem {
        private String fileName;
        private String itemName;

        public MusicItem(String fileName, String itemName) {
            this.fileName = fileName;
            this.itemName = itemName;
        }

        public String getResourcePath() {
            return Configure.getResource("music/") + fileName;
        }

        public String getItemName() {
            return this.itemName;
        }

        public String getFileName() {
            return this.fileName;
        }
    }
}

