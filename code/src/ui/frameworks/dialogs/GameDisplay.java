package ui.frameworks.dialogs;

import kernel.DataCenter;
import kernel.UILinker;
import ui.elements.Piece;
import ui.frameworks.ChessBoard;
import ui.frameworks.ContentPane;
import ui.frameworks.OperationBar;
import ui.nodes.GameButton;
import ui.nodes.StepListView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import javax.xml.crypto.Data;
import java.sql.DataTruncation;

/**
 * 棋盘对话框
 */

public class GameDisplay extends DialogBase {
    public static final double width = 900;
    public static final double height = 700;
    private UILinker uiLinker;
    private DataCenter dataCenter;
    private Piece[][] pieceGroup;
    private ChessBoard chessBoard;
    private OperationBar operationBar;
    private ContentPane contentPane;
    private boolean closed = false;

    public GameDisplay() {
        super(width, height);
        contentPane = ContentPane.getSelf();
        chessBoard = new ChessBoard();
        chessBoard.setLayoutX(25);
        chessBoard.setLayoutY(25);
        getChildren().addAll(chessBoard);
        pieceGroup = new Piece[15][15];
        double offsetX = 53 - Piece.width / 2;
        double offsetY = 50 - Piece.height / 2;
        for (int i = 0; i < pieceGroup.length; i++) {
            for (int j = 0; j < pieceGroup[0].length; j++) {
                pieceGroup[i][j] = new Piece();
                pieceGroup[i][j].setLayoutX(offsetX + j * Piece.width);
                pieceGroup[i][j].setLayoutY(offsetY + i * Piece.height);
                pieceGroup[i][j].setOnMousePressed(new OnMouseClickHandler(i, j));
                getChildren().add(pieceGroup[i][j]);
            }
        }
        operationBar = new OperationBar();
        operationBar.layoutXProperty().bind(this.widthProperty().subtract(operationBar.widthProperty()));
        operationBar.prefHeightProperty().bind(this.heightProperty());
        getChildren().add(operationBar);
        dataCenter = new DataCenter();
        uiLinker = new UILinker(this, dataCenter);

        StepListView list = operationBar.getListView();
        list.getItems().add("[游戏开始]");
        list.getSelectionModel().select(0);
        GameButton backButton = operationBar.getStepButtons()[0];
        GameButton forwardButton = operationBar.getStepButtons()[1];
        list.getSelectionModel().selectedIndexProperty().addListener(ov -> {
            int index = list.getSelectionModel().getSelectedIndex();
            backButton.setDisable((index <= 0) ? (true) : (false));
            forwardButton.setDisable((index >= list.getItems().size() - 1) ? (true) : (false));
            uiLinker.peek(index - 1);
        });
        backButton.setOnAction(e -> {
            list.getSelectionModel().select(list.getSelectionModel().getSelectedIndex() - 1);
        });
        forwardButton.setOnAction(e -> {
            list.getSelectionModel().select(list.getSelectionModel().getSelectedIndex() + 1);
        });
        operationBar.getOperationButtons()[0].setOnAction(e -> uiLinker.restart());
        operationBar.getOperationButtons()[1].setOnAction(e -> {
            contentPane.getNameInputDialog().fadeAlert();
            contentPane.getNameInputDialog().getTextField().setText(uiLinker.getCurrentSave());
            contentPane.showDialog(contentPane.getNameInputDialog());
        });
        operationBar.getOperationButtons()[2].setOnAction(e -> ContentPane.getSelf().showDialog(ContentPane.getSelf().getSaveDialog()));
        operationBar.getAISwitch().selectedProperty().addListener(ov -> {
            if(operationBar.getAISwitch().isSelected()){
                uiLinker.aiPowerOn();
            }else{
                uiLinker.aiPowerOff();
            }
        });

    }

    public Piece getPiece(int i, int j) {
        return pieceGroup[i][j];
    }
    //取位于(i,j)处的棋子对象

    public OperationBar getOperationBar() {
        return this.operationBar;
    }
    //取操作栏对象

    public int[][] getCurrentState() {
        int state[][] = new int[15][15];
        for (int i = 0; i < pieceGroup.length; i++) {
            for (int j = 0; j < pieceGroup[0].length; j++) {
                state[i][j] = pieceGroup[i][j].getCurrentType();
            }
        }
        return state;
    }
    //取当前棋盘

    public boolean isClosed() {
        return this.closed;
    }
    //判断是否禁止下棋（胜利时）

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
    //设置是否禁止下棋

    public DataCenter getDataCenter(){
        return this.dataCenter;
    }
    //取当前游戏数据

    public void setDataCenter(DataCenter dataCenter){
        this.dataCenter = dataCenter;
    }
    //替换DataCenter对象，读取存档时

    private class OnMouseClickHandler implements EventHandler<MouseEvent> {
        private int i;
        private int j;

        public OnMouseClickHandler(int i, int j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public void handle(MouseEvent e) {
            if (!closed) {
                uiLinker.push(i, j);
            }
        }
    }
    //鼠标单击时的事件处理内部类
}
