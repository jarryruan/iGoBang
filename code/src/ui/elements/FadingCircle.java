package ui.elements;

import ui.animations.EasingProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * 水波反馈圆，主要用于装饰，效果：在鼠标点击后有随机颜色的圆晕泛开
 */

public class FadingCircle extends Circle {
    private EasingProperty scaleAnimation;
    private EasingProperty opacityAnimation;

    public FadingCircle(double x, double y) {
        super(x, y, 0.0);
        getStyleClass().add("fading_circle");
        setFill(Color.color(Math.random(), Math.random(), Math.random()));
        scaleAnimation = new EasingProperty(radiusProperty());
        opacityAnimation = new EasingProperty(opacityProperty());
        scaleAnimation.setToValue(150.0);
        opacityAnimation.setToValue(0.0);
    }
}
