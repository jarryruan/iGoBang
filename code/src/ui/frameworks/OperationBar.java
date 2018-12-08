package ui.frameworks;

import ui.nodes.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.naming.TimeLimitExceededException;

/**
 * 棋盘右侧的操作栏，有悔棋保存存档等功能
 */

public class OperationBar extends BorderPane {
    public static final double width = 200;
    StepView top;
    //top是上端具有悔棋功能的面板

    OperationView bottom;
    //bottom是下端的按钮栏

    public OperationBar() {
        getStyleClass().add("operation_bar");
        setPrefWidth(width);
        setPadding(new Insets(5, 5, 40, 5));
        top = new StepView();
        setTop(top);
        setAlignment(top, Pos.CENTER);
        bottom = new OperationView();
        setBottom(bottom);
        setAlignment(bottom, Pos.BOTTOM_CENTER);
    }

    public StepListView getListView() {
        return top.getListView();
    }
    //悔棋列表

    public GameButton[] getStepButtons() {
        return top.getStepButtons();
    }
    //悔棋按钮

    public GameButton[] getOperationButtons() {
        return bottom.getOperationButtons();
    }
    //操作按钮

    public TimeDisplay getTimeDisplay() {
        return top.getTimeDisplay();
    }
    //时间显示面板

    public TitleLabel getBlackCountDisplay() {
        return top.getBlackCountDisplay();
    }
    //黑棋数量面板

    public TitleLabel getWhiteCountDisplay() {
        return top.getWhiteCountDisplay();
    }
    //白棋数量面板

    public GameCheckBox getAISwitch(){
        return bottom.useAI;
    }
    //获取AI开关

    private class StepView extends VBox {
        private StepListView stepList;
        private HBox operatePane;
        private BlueButton backButton;
        private GameButton forwardButton;
        private TimeDisplay timeDisplay;
        private HBox countDisplay;
        private TitleLabel blackCount;
        private TitleLabel whiteCount;

        public StepView() {
            stepList = new StepListView();
            stepList.setPrefHeight(350);
            blackCount = new TitleLabel("0");
            whiteCount = new TitleLabel("0");
            blackCount.setStyle("-fx-text-fill:black;");
            countDisplay = new HBox();
            countDisplay.setSpacing(40);
            countDisplay.setAlignment(Pos.CENTER);
            countDisplay.getChildren().addAll(blackCount, whiteCount);
            setSpacing(10);
            operatePane = new HBox();
            backButton = new BlueButton("向后一步", 15);
            backButton.setDisable(true);
            forwardButton = new GameButton("向前一步", 15);
            forwardButton.setDisable(true);
            operatePane.setAlignment(Pos.CENTER);
            operatePane.getChildren().addAll(backButton, forwardButton);
            timeDisplay = new TimeDisplay();
            timeDisplay.prefWidthProperty().bind(this.widthProperty());
            getChildren().addAll(timeDisplay, stepList, operatePane, countDisplay);
        }

        public StepListView getListView() {
            return this.stepList;
        }

        public GameButton[] getStepButtons() {
            GameButton[] btns = {backButton, forwardButton};
            return btns;
        }

        public TimeDisplay getTimeDisplay() {
            return this.timeDisplay;
        }

        public TitleLabel getBlackCountDisplay() {
            return this.blackCount;
        }

        public TitleLabel getWhiteCountDisplay() {
            return this.whiteCount;
        }

    }

    private class OperationView extends VBox {
        private GameCheckBox useAI;
        private RedButton startGame;
        private BlueButton saveGame;
        private GameButton loadGame;

        public OperationView() {
            useAI = new GameCheckBox("以AI为对手");
            startGame = new RedButton("开始新游戏", 15);
            saveGame = new BlueButton("保存存档", 15);
            loadGame = new GameButton("载入存档", 15);
            setSpacing(10);
            setAlignment(Pos.CENTER);
            getChildren().addAll(useAI,startGame, saveGame, loadGame);
        }

        public GameButton[] getOperationButtons() {
            GameButton[] btns = {startGame, saveGame, loadGame};
            return btns;
        }
    }
}
