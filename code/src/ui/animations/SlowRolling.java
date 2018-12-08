package ui.animations;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * 属性滚动类，详细原理请参考项目开发文档
 */

public class SlowRolling {
    private double animationRate = 0.0005;
    private Timeline tl;
    private KeyFrame keyFrame;
    private DoubleProperty property;
    private double fromValue;
    private double toValue;

    public SlowRolling(DoubleProperty valueProcessed, double fromValue, double toValue) {
        keyFrame = new KeyFrame(EasingProperty.animationDuration, new KeyFrameHandler());
        tl = new Timeline(keyFrame);
        property = valueProcessed;
        this.fromValue = fromValue;
        property.setValue(fromValue);
        this.toValue = toValue;
        tl.setCycleCount(Animation.INDEFINITE);
    }
    //valueProcessed代表该动画绑定的属性，fromValue和toValue代表来回滚动的两个边界

    public void setRate(double rate) {
        this.animationRate = rate;
    }
    //设置动画速率

    public Timeline getTimeline() {
        return this.tl;
    }
    //取该动画的Timeline对象

    public double getDistance() {
        return (toValue - fromValue);
    }
    //取边界距离

    protected class KeyFrameHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            double currentValue = property.getValue();
            if ((fromValue < toValue && currentValue > toValue) || (fromValue > toValue && currentValue < toValue)) {
                double temp = fromValue;
                fromValue = toValue;
                toValue = temp;
                property.setValue(fromValue);
            }
            double step = getDistance() * animationRate;
            currentValue += step;
            property.setValue(currentValue);
        }
    }
}
