package addons;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class MusicPlayer {
    private Media media;
    private MediaPlayer mediaPlayer;
    private static MusicPlayer self;

    public MusicPlayer() {
        self = this;
    }

    public MusicPlayer(String source) {
        this();
        play(source);
    }

    public static MusicPlayer getSelf() {
        return self;
    }
    //由于该类只会创建一个对象，故使用一个静态变量来保存唯一的对象，以便于外界访问

    public String getSource() {
        return media.getSource();
    }
    //获取当前音乐资源

    public void play(String source) {
        if (media != null)
            mediaPlayer.stop();
        media = new Media(source);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }
    //播放指定音乐（需指定文件路径）

    public void stop() {
        if (media != null)
            mediaPlayer.stop();
    }
    //停止播放音乐
}
