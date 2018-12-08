package ui.frameworks.dialogs;

import ui.Configure;
import ui.frameworks.ContentPane;
import ui.nodes.ContextLabel;
import ui.nodes.GreenButton;
import ui.nodes.RedButton;
import ui.nodes.TitleLabel;
import javafx.scene.image.ImageView;

/**
 * 消息提示框，用于询问用户是否确认操作
 */

public class MessageBox extends DialogBase {
    public static final double width = 640;
    public static final double height = 350;
    private TitleLabel title;
    private ContextLabel context;

    public MessageBox(String title, String context) {
        super(width, height, true);
        getButtonBar().addButton(new GreenButton("确认"), new RedButton("取消"));
        this.title = new TitleLabel(title);
        this.title.setGraphic(new ImageView(Configure.getResource("drawable/icon/msg.png")));
        this.context = new ContextLabel(context);
        this.title.setLayoutX(20);
        this.title.setLayoutY(10);
        this.context.setLayoutX(20);
        this.context.setLayoutY(90);
        this.context.setPrefWidth(width - 40);
        this.getButtonBar().getButton(1).setOnAction(e -> ContentPane.getSelf().hideDialog());
        getChildren().addAll(this.title, this.context);
    }
}
