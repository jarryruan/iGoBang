package ui.animations;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.MotionBlur;
import javafx.util.Duration;

/**
 * 属性缓动类，详细原理请参考项目开发文档
 */

public class EasingProperty {
    static final Duration animationDuration = Duration.millis(15);
    private static final double easeInInitialRate = 0.01;
    private static final double minUnit = 0.001;
    private static final double motionBlurStrength = 50;
    private static final double absoluteMinUnit = 1e-10;
    private static double maxRate = 0.15;

    private boolean easeIn = false;
    private double fromValue;
    private double toValue;
    private DoubleProperty property;
    private double currentValue;
    private Timeline tl;
    private GaussianBlur blur;

    public EasingProperty(DoubleProperty valueProcessed) {
        KeyFrame keyFrame = new KeyFrame(animationDuration, new KeyFrameHandler());
        blur = new GaussianBlur(0.0);
        this.property = valueProcessed;
        this.fromValue = property.getValue();
        this.toValue = this.fromValue;
        this.currentValue = this.fromValue;
        tl = new Timeline(keyFrame);
        tl.setCycleCount(Animation.INDEFINITE);
        tl.stop();
    }
    //valueProcessed表示该动画要绑定的属性

    public void setEaseIn(boolean easeIn) {
        this.easeIn = easeIn;
    }
    //设置该动画是否先加速后减速

    public boolean isEaseIn() {
        return this.easeIn;
    }
    //判断该动画是否先加速后减速

    public void setToValue(double value) {
        if (getTimeline().getStatus() == Animation.Status.RUNNING)
            getTimeline().stop();
        if (Math.abs(value - property.getValue()) < absoluteMinUnit) {
            return;
        }
        this.toValue = value;
        this.fromValue = property.getValue();
        this.currentValue = fromValue;
        getTimeline().play();
    }
    //设置动画目标值

    public double getCurrentValue() {
        return this.currentValue;
    }
    //取属性当前值

    public double getFromValue() {
        return this.fromValue;
    }
    //取属性起始值

    public double getTotalDistance() {
        return (toValue - fromValue);
    }
    //取起始值和目标值的差距

    public double getDistance() {
        return (toValue - currentValue);
    }
    //取当前值和目标值的差距

    public void setFromValue(double fromValue) {
        this.fromValue = fromValue;
    }
    //设置起始值

    public void setValueImmediately(double value) {
        if (getTimeline().getStatus() == Animation.Status.RUNNING)
            getTimeline().stop();
        this.fromValue = value;
        this.toValue = value;
        property.setValue(value);
    }
    //立即改变属性值（不进行动画过渡）

    public Timeline getTimeline() {
        return this.tl;
    }
    //取动画的Timeline对象

    public GaussianBlur getBlur() {
        return this.blur;
    }
    //获取与该动画对应的动态模糊特效对象（如果需要的话），速度越快模糊度越高

    protected int compareDouble(double a, double b) {
        if (Math.abs(a - b) < Math.abs(minUnit * getTotalDistance()))
            return 0;
        else if (a > b)
            return 1;
        else
            return -1;
    }
    //比较两个double值的大小关系

    protected double getCurrentSpeed() {
        double distance = getDistance();
        double totalDistance = getTotalDistance();
        double maxSpeed = Math.abs(totalDistance) * maxRate;
        double speed = 0;
        if (easeIn) {
            double half = totalDistance / 2.0;
            double max = (distance < 0) ? (-maxSpeed) : maxSpeed;
            if (Math.abs(distance) > Math.abs(half)) {
                speed = (((distance - half) * (easeInInitialRate - 1.0) * max)) / (totalDistance - half)
                        + max;
            } else {
                speed = (1.0 * maxSpeed / Math.abs(half)) * distance;
            }
        } else {
            speed = (1.0 * maxSpeed / Math.abs(totalDistance)) * distance;
        }
        return speed;
    }
    //获取当前动画速度

    protected class KeyFrameHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            if (compareDouble(currentValue, toValue) == 0) {
                currentValue = toValue;
                property.setValue(toValue);
                getTimeline().stop();
                fromValue = toValue;
                blur.setRadius(0.0);
                return;
            }
            double speed = getCurrentSpeed();
            double maxSpeed = Math.abs(getTotalDistance()) * maxRate;
            blur.setRadius((Math.abs(speed) / maxSpeed) * motionBlurStrength);
            currentValue += getCurrentSpeed();
            property.setValue(currentValue);
        }
    }
}
