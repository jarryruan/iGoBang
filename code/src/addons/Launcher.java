package addons;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ui.Configure;
import ui.frameworks.ContentPane;
import ui.frameworks.StartupDisplay;

public class Launcher extends Application {
    private Scene startupScene;
    private Scene contentScene;
    private StartupDisplay startupDisplay;
    private ContentPane contentPane;
    private static Launcher self;
    private Stage primaryStage;
    private MusicPlayer musicPlayer;

    @Override
    public void start(Stage primaryStage) {
        self = this;
        this.primaryStage = primaryStage;
        startupDisplay = new StartupDisplay();
        startupScene = new Scene(startupDisplay, Configure.viewportWidth, Configure.viewportHeight);
        startupScene.getStylesheets().add(Configure.getResource("stylesheet/startupdisplay.css"));
        primaryStage.setScene(startupScene);
        primaryStage.setTitle("iGoBang");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(Configure.getResource("drawable/icon.png")));
        primaryStage.show();
    }
    //start入口函数

    public void showContentPane() {
        musicPlayer = new MusicPlayer();
        contentPane = new ContentPane();
        contentScene = new Scene(contentPane, Configure.viewportWidth, Configure.viewportHeight);
        contentScene.getStylesheets().add(Configure.getResource("stylesheet/root.css"));
        primaryStage.setScene(contentScene);
    }
    //播放完片头动画后调用此函数来显示游戏主界面

    public static Launcher getSelf() {
        return self;
    }
    //由于该类只会创建一个对象，故使用一个静态变量来保存唯一的对象，以便于外界访问

    public static void main(String[] args) {
        Application.launch(args);
    }
    //main入口函数
}
