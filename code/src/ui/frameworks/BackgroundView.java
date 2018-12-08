package ui.frameworks;

import ui.Configure;
import ui.animations.EasingProperty;
import ui.elements.FadingCircle;
import ui.nodes.BackgroundChannel;
import ui.nodes.LogoView;
import ui.nodes.MenuBar;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;

/**
 * 背景图显示器，负责显示和切换背景图、管理背景图动画
 */

public class BackgroundView extends Pane {
    private int currentChannel = -1;
    private GaussianBlur blurEffect;
    private EasingProperty blurAnimation;
    private EasingProperty opacityAnimation;
    private LogoView logoView;
    public static BackgroundView self;

    public BackgroundView() {
        getStyleClass().add("background_view");

        logoView = new LogoView();
        logoView.setLayoutX((Configure.viewportWidth - LogoView.width) / 2);
        logoView.setLayoutY((Configure.viewportHeight - MenuBar.height - LogoView.height) / 2);
        getChildren().addAll(logoView);

        blurEffect = new GaussianBlur(0.0);
        blurAnimation = new EasingProperty(blurEffect.radiusProperty());
        opacityAnimation = new EasingProperty(this.opacityProperty());
        this.setEffect(blurEffect);
        setOnMouseClicked(e -> getChildren().add(new FadingCircle(e.getX(), e.getY())));
        self = this;
    }

    public BackgroundView(String defaultBackground) {
        this();
        addBackgroundChannel(defaultBackground);
        switchBackground(0);
    }

    public void addBackgroundChannel(String url) {
        BackgroundChannel img = new BackgroundChannel(url);
        getChildren().add(getChildren().size() - 1, img);
    }
    //增加背景图

    public void switchBackground(int index) {
        if (index >= 0 && index < getChildren().size() - 1 && index != currentChannel) {
            if (currentChannel >= 0) {
                BackgroundChannel current = (BackgroundChannel) getChildren().get(currentChannel);
                current.fade();
                current.stopRolling();
            }
            BackgroundChannel newChannel = (BackgroundChannel) getChildren().get(index);
            newChannel.show();
            if (newChannel.getImage().getWidth() > Configure.viewportWidth) {
                newChannel.startRolling();
            }
            currentChannel = index;
        }
    }
    //切换背景图

    public static BackgroundView getSelf() {
        return self;
    }
    //由于该类只会创建一个对象，故使用一个静态变量来保存唯一的对象，以便于外界访问

    public LogoView getLogoView() {
        return this.logoView;
    }
    //取背景图上的Logo对象

    public void blur() {
        blurAnimation.setToValue(40.0);
        opacityAnimation.setToValue(0.5);
        setDisable(true);
    }
    //模糊背景图（用于显示对话框）

    public void unBlur() {
        blurAnimation.setToValue(0.0);
        opacityAnimation.setToValue(1.0);
        setDisable(false);
    }
    //取消背景图模糊
}
