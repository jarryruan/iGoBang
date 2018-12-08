package ui.frameworks.dialogs;

import ui.Configure;
import ui.frameworks.BackgroundView;
import ui.nodes.SelectorListView;
import javafx.scene.shape.Rectangle;

/**
 * 背景图选择器
 */

public class SkinSelector extends PopupBase {
    public static final double width = 400;
    public static final double height = 200;
    private final static String[] bkgs = {"极地雪峰", "你的名字", "幻境", "优美圣地", "二次元星空", "黑色炫酷", "田园风光","宇宙奇幻","浪潮"};
    private SelectorListView listView;
    private BackgroundView backgroundView;
    private Rectangle rcBkg;

    public SkinSelector() {
        super(width, height);
        rcBkg = new Rectangle();
        rcBkg.getStyleClass().add("black_pad_background_rectangle");
        rcBkg.setWidth(width);
        rcBkg.setHeight(height);

        listView = new SelectorListView();
        listView.setPrefSize(width, height);

        backgroundView = BackgroundView.getSelf();
        initSkinList();
        listView.getSelectionModel().selectedItemProperty().addListener(ov -> {
            int index = listView.getSelectionModel().getSelectedIndex();
            backgroundView.switchBackground(index);
            Configure.setBackgroundIndex(index);
            Configure.writeConfigure();
        });
        Configure.readConfigure();
        listView.getSelectionModel().select(Configure.getBackgroundIndex());
        getChildren().addAll(rcBkg, listView);
    }

    private void initSkinList() {
        for (int i = 0; i < bkgs.length; i++) {
            String name = bkgs[i];
            listView.getItems().add(name);
            backgroundView.addBackgroundChannel(Configure.getResource("drawable/background/" + name + ".jpg"));
        }
    }
    //初始化背景图列表
}

