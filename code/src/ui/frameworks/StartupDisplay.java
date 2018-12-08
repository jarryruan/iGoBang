package ui.frameworks;

import addons.Launcher;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import ui.Configure;

import java.io.File;

/**
 * 用于显示启动时的欢迎视频
 */

public class StartupDisplay extends Pane {
    private Media media;
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;

    public StartupDisplay() {
        super();
        this.setPrefWidth(Configure.viewportWidth);
        this.setPrefHeight(Configure.viewportHeight);
        this.getStyleClass().add("startup_display");
        media = new Media(Configure.getResource("video/startup.flv"));
        mediaPlayer = new MediaPlayer(media);
        mediaView = new MediaView(mediaPlayer);
        mediaView.layoutXProperty().bind(this.widthProperty().subtract(media.widthProperty()).divide(2));
        mediaView.layoutYProperty().bind(this.heightProperty().subtract(media.heightProperty()).divide(2));
        getChildren().add(mediaView);
        mediaPlayer.play();
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override

            public void run() {
                Launcher.getSelf().showContentPane();
            }
        });
    }
}
