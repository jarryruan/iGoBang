package ui.frameworks;

import ui.Configure;
import ui.animations.EasingProperty;
import ui.frameworks.dialogs.*;
import ui.nodes.IconButton;
import ui.nodes.MenuBar;
import javafx.scene.layout.Pane;
import ui.nodes.TimeDisplay;

/**
 * 游戏主界面
 */

public class ContentPane extends Pane {
    private BackgroundView bkg;
    private boolean dialogShowed = false;
    private Pane currentDialog;
    private SaveDialog saveDialog;
    private MessageBox exitDialog;
    private GameDisplay gameDisplay;
    private NameInputDialog nameInputDialog;
    private RenameDialog renameDialog;

    private boolean popupShowed = false;
    private PopupBase currentPopup;
    private MenuBar menuBar;
    private IconButton skinButton;
    private IconButton musicButton;
    private SkinSelector skinSelector;
    private MusicSelector musicSelector;
    private TipDisplay tipDisplay;

    private static ContentPane self;

    public ContentPane() {
        self = this;
        getStyleClass().add("content_pane");
        bkg = new BackgroundView();

        tipDisplay = new TipDisplay();
        tipDisplay.setLayoutY(Configure.viewportHeight - MenuBar.height - TipDisplay.height - 10);

        menuBar = new MenuBar();
        menuBar.addButton("主页");
        menuBar.addButton("游戏");
        menuBar.addButton("存档");
        menuBar.addButton("关于");
        menuBar.addButton("退出");
        menuBar.layoutYProperty().bind(this.heightProperty().subtract(menuBar.prefHeightProperty()));
        menuBar.prefWidthProperty().bind(this.widthProperty());

        saveDialog = new SaveDialog();
        exitDialog = new MessageBox("确认", "你确认要退出游戏吗？");
        exitDialog.getButtonBar().getButton(0).setOnAction(e -> System.exit(0));

        gameDisplay = new GameDisplay();
        nameInputDialog = new NameInputDialog();
        renameDialog = new RenameDialog();


        skinSelector = new SkinSelector();
        skinSelector.setLayoutY(20);
        skinButton = new IconButton(Configure.getResource("drawable/icon/skin.png"));
        skinButton.setLayoutX(Configure.viewportWidth - skinButton.getPrefWidth() - 5);
        skinButton.setLayoutY(20);
        skinButton.setOnAction(e -> {
            hidePopup();
            showPopup(skinSelector);
        });


        musicSelector = new MusicSelector();
        musicSelector.setLayoutY(70);
        musicButton = new IconButton(Configure.getResource("drawable/icon/music.png"));
        musicButton.setLayoutX(Configure.viewportWidth - musicButton.getPrefWidth() - 5);
        musicButton.setLayoutY(70);
        musicButton.setOnAction(e -> {
            hidePopup();
            showPopup(musicSelector);
        });


        getChildren().addAll(bkg, menuBar, skinButton, musicButton);

        menuBar.getButton(0).setOnAction(e -> {
            hideDialog();
            hidePopup();
            bkg.getLogoView().unstretch();
        });
        menuBar.getButton(1).setOnAction(e -> {
            showDialog(getGameDisplay());
        });
        menuBar.getButton(2).setOnAction(e -> {
            showDialog(getSaveDialog());
        });
        menuBar.getButton(3).setOnAction(e -> {
            if (bkg.getLogoView().isStretched()) {
                bkg.getLogoView().unstretch();
            } else {
                bkg.getLogoView().stretch();
                hideDialog();
                hidePopup();
            }
        });
        menuBar.getButton(4).setOnAction(e -> {
            showDialog(exitDialog);
        });
        this.setOnMouseClicked(e -> {
            if (popupShowed && currentPopup != tipDisplay)
                hidePopup();
        });
        menuBar.setOnMouseClicked(e -> {
            if (popupShowed && currentPopup != tipDisplay)
                hidePopup();
        });

    }

    public void showDialog(DialogBase dialog) {
        if (dialog == currentDialog && dialogShowed) {
            return;
        }
        if (currentDialog != null) {
            getChildren().remove(currentDialog);
            currentDialog = null;
        }
        TimeDisplay timeDisplay = gameDisplay.getOperationBar().getTimeDisplay();
        if(dialog == gameDisplay && !gameDisplay.isClosed() && gameDisplay.getDataCenter().getSteps().size()>0){
            if(!timeDisplay.isStarted()){
                timeDisplay.start();
            }
        }else if(timeDisplay.isStarted()){
            gameDisplay.getOperationBar().getTimeDisplay().stop();
        }
        double width = dialog.getPreferredWidth();
        double height = dialog.getPreferredHeight();
        dialog.setPrefWidth(Configure.viewportWidth);
        dialog.setPrefHeight(Configure.viewportHeight);
        dialog.setOpacity(0.0);
        dialog.layoutXProperty().bind(widthProperty().subtract(dialog.prefWidthProperty()).divide(2));
        dialog.layoutYProperty().bind(heightProperty().subtract(menuBar.prefHeightProperty()).subtract(dialog.prefHeightProperty()).divide(2));
        EasingProperty widthAnimation = new EasingProperty(dialog.prefWidthProperty());
        EasingProperty heightAnimation = new EasingProperty(dialog.prefHeightProperty());
        EasingProperty opacityAnimation = new EasingProperty(dialog.opacityProperty());
        widthAnimation.setToValue(width);
        heightAnimation.setToValue(height);
        opacityAnimation.setToValue(1.0);
        currentDialog = dialog;
        bkg.blur();
        getChildren().add(1, dialog);
        currentDialog.setMouseTransparent(false);
        dialogShowed = true;
    }
    //显示指定对话框

    public void hideDialog() {
        if (currentDialog != null) {
            TimeDisplay timeDisplay = gameDisplay.getOperationBar().getTimeDisplay();
            if(currentDialog == gameDisplay && timeDisplay.isStarted()){
                timeDisplay.stop();
            }
            EasingProperty widthAnimation = new EasingProperty(currentDialog.prefWidthProperty());
            EasingProperty heightAnimation = new EasingProperty(currentDialog.prefHeightProperty());
            EasingProperty opacityAnimation = new EasingProperty(currentDialog.opacityProperty());
            widthAnimation.setToValue(Configure.viewportWidth);
            heightAnimation.setToValue(Configure.viewportHeight);
            opacityAnimation.setToValue(0.0);
            currentDialog.setMouseTransparent(true);
            bkg.unBlur();
            dialogShowed = false;
        }
    }
    //隐藏当前对话框

    public void showPopup(PopupBase popup) {
        if (currentPopup == popup && popupShowed) {
            return;
        }
        if (currentPopup != null) {
            getChildren().remove(currentPopup);
            currentPopup = null;
        }
        popup.setOpacity(0.0);
        double ori = Configure.viewportWidth - popup.getPrefWidth();
        popup.setLayoutX(ori);
        EasingProperty easeX = new EasingProperty(popup.layoutXProperty());
        EasingProperty easeOpacity = new EasingProperty(popup.opacityProperty());
        easeOpacity.setToValue(1.0);
        easeX.setToValue(ori - 100);
        getChildren().add(popup);
        currentPopup = popup;
        currentPopup.setMouseTransparent(false);
        popupShowed = true;

    }
    //显示指定弹窗

    public void hidePopup() {
        if (currentPopup == null) {
            return;
        }
        double toX = Configure.viewportWidth;
        EasingProperty easeX = new EasingProperty(currentPopup.layoutXProperty());
        EasingProperty easeOpacity = new EasingProperty(currentPopup.opacityProperty());
        easeX.setToValue(toX);
        easeOpacity.setToValue(0.0);
        currentPopup.setMouseTransparent(true);
        popupShowed = false;
    }
    //隐藏弹窗

    public void showTips(String context) {
        tipDisplay.setText(context);
        hidePopup();
        showPopup(tipDisplay);
        tipDisplay.startTiming();
    }
    //显示消息提示

    public SaveDialog getSaveDialog() {
        return this.saveDialog;
    }
    //取存档管理对话框

    public NameInputDialog getNameInputDialog() {
        return this.nameInputDialog;
    }
    //取存档名称输入框

    public GameDisplay getGameDisplay() {
        return this.gameDisplay;
    }
    //取棋盘对话框

    public RenameDialog getRenameDialog() {
        return this.renameDialog;
    }
    //取存档重命名对话框

    public static ContentPane getSelf() {
        return self;
    }
    //由于该类只会创建一个对象，故使用一个静态变量来保存唯一的对象，以便于外界访问
}
