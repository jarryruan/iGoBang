package ui.frameworks.dialogs;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import ui.Configure;
import ui.animations.EasingProperty;
import ui.frameworks.ContentPane;
import ui.nodes.ContextLabel;

/**
 * 消息提示弹窗
 */

public class TipDisplay extends PopupBase {
    public static final double width = 400;
    public static final double height = 100;
    private Rectangle rcBkg;
    private ImageView icon;
    private ContextLabel label;
    private Timeline timer;
    private KeyFrame keyFrame;
    private GaussianBlur blur;
    private EasingProperty blurAnimation;
    private EasingProperty opacityAnimation;
    private static final Duration duration = Duration.millis(8000);
    //停留8秒后自动消失

    public TipDisplay() {
        super(width, height);
        this.setPrefSize(width, height);
        rcBkg = new Rectangle();
        rcBkg.getStyleClass().add("black_pad_background_rectangle");
        rcBkg.setWidth(width);
        rcBkg.setHeight(height);
        icon = new ImageView(Configure.getResource("drawable/icon/msg.png"));
        icon.setLayoutX(20);
        icon.layoutYProperty().bind(this.prefHeightProperty().subtract(icon.getImage().getHeight()).divide(2));
        label = new ContextLabel("测试");
        label.setLayoutX(100);
        label.layoutYProperty().bind(this.heightProperty().subtract(label.heightProperty()).divide(2));
        label.setPrefWidth(width - 100);
        getChildren().addAll(rcBkg, icon, label);
        keyFrame = new KeyFrame(duration, e -> {
            ContentPane.getSelf().hidePopup();
            timer.stop();
        });
        timer = new Timeline(keyFrame);
        blur = new GaussianBlur(0.0);
        rcBkg.setEffect(blur);
        opacityAnimation = new EasingProperty(rcBkg.opacityProperty());
        blurAnimation = new EasingProperty(blur.radiusProperty());
        this.setOnMouseEntered(e -> {
            blurAnimation.setToValue(10.0);
            opacityAnimation.setToValue(0.5);
        });
        this.setOnMouseExited(e -> {
            blurAnimation.setToValue(0.0);
            opacityAnimation.setToValue(1.0);
        });
        this.setOnMouseClicked(e -> {
            ContentPane.getSelf().hidePopup();
            timer.stop();
        });

    }

    public void setText(String text) {
        label.setText(text);
    }
    //设置提示文本

    public void startTiming() {
        timer.stop();
        timer.play();
    }
    //开始倒计时（8秒后自动消失）
}
