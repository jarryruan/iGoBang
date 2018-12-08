package ui.nodes;

import ui.Configure;
import ui.animations.EasingProperty;
import ui.animations.SlowRolling;
import javafx.scene.image.ImageView;

/**
 * 背景图频道（用于保存单个背景）
 */

public class BackgroundChannel extends ImageView {
    private EasingProperty opacityAnimation;
    private SlowRolling rollingAnimation;
    private boolean rollable = true;

    public BackgroundChannel(String url) {
        super(url);
        opacityAnimation = new EasingProperty(this.opacityProperty());
        double width = getImage().getWidth();
        setLayoutX(0);
        setLayoutY(0);
        setOpacity(0);
        rollingAnimation = new SlowRolling(this.layoutXProperty(), 0, Configure.viewportWidth - width + 10);
    }

    public BackgroundChannel(String url, boolean rollable) {
        this(url);
        this.rollable = rollable;
    }

    public void fade() {
        opacityAnimation.setToValue(0.0);
    }
    //淡出

    public void show() {
        opacityAnimation.setToValue(1.0);
    }
    //淡入

    public void startRolling() {
        if (!rollable) return;
        rollingAnimation.getTimeline().play();
    }
    //开始滚动动画

    public void stopRolling() {
        rollingAnimation.getTimeline().stop();
    }
    //停止滚动动画
}
