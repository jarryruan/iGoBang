package ui;

import ui.frameworks.ContentPane;

import java.io.*;

/**
 * 游戏总配置
 */

public class Configure {
    public static final int viewportWidth = 1280;
    public static final int viewportHeight = 800;
    private static int backgroundIndex = 0;
    private static int musicIndex = 0;

    private Configure() {

    }

    public static String getResource(String name) {
        return Configure.class.getResource("/" + name).toString();
    }
    //取指定资源文件路径

    public static void readConfigure() {
        File conf = new File("configure.conf");
        if (conf.exists()) {
            try {
                DataInputStream input = new DataInputStream(new FileInputStream(conf));
                backgroundIndex = input.readInt();
                musicIndex = input.readInt();
                input.close();
            } catch (IOException ex) {
                conf.delete();
            }
        }
    }
    //读取游戏偏好设置（音乐和背景图偏好设置）

    public static void writeConfigure() {
        File conf = new File("configure.conf");
        try {
            DataOutputStream input = new DataOutputStream(new FileOutputStream(conf));
            input.writeInt(backgroundIndex);
            input.writeInt(musicIndex);
            input.close();
        } catch (IOException ex) {
            ContentPane.getSelf().showTips("无法写入配置文件");
        }
    }
    //写入游戏偏好设置（音乐和背景图偏好设置）

    public static void setBackgroundIndex(int index) {
        backgroundIndex = index;
    }
    //记录当前所用的背景图序号

    public static int getBackgroundIndex() {
        return backgroundIndex;
    }
    //取所记录的背景图序号

    public static void setMusicIndex(int index) {
        musicIndex = index;
    }
    //记录当前所用的背景音乐序号

    public static int getMusicIndex() {
        return musicIndex;
    }
    //取所记录的背景音乐序号
}
