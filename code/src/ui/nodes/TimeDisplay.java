package ui.nodes;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * 游戏用时显示
 */

public class TimeDisplay extends TitleLabel {
    private long seconds = 0;
    private static final Duration duration = new Duration(1000);
    private Timeline timeline;
    private KeyFrame keyFrame;
    private boolean started = false;

    public TimeDisplay() {
        super("00:00:00");
        getStyleClass().add("time_display");
        keyFrame = new KeyFrame(duration, e -> {
            seconds++;
            setText(timeFormat());
        });
        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    public void start() {
        timeline.play();
        started = true;
    }
    //开始计时

    public void stop() {
        timeline.stop();
        started = false;
    }
    //暂停计时

    public void setTime(long time) {
        this.seconds = time;
        setText(timeFormat());
    }
    //设置当前时间

    public boolean isStarted() {
        return this.started;
    }
    //获取计时是否开始

    public void clear() {
        setTime(0);
    }
    //清零

    public long getTime() {
        return this.seconds;
    }
    //获取当前时间

    private String timeFormat() {
        long totalSeconds = seconds;
        long s = totalSeconds % 60;
        long totalMinutes = totalSeconds / 60;
        long m = totalMinutes % 60;
        long h = totalMinutes / 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
    //格式化时间显示
}
