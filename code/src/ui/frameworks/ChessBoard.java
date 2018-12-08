package ui.frameworks;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import ui.Configure;

/**
 * 棋盘类，只负责显示棋盘（无棋子）
 */

public class ChessBoard extends Pane {
    ImageView bkg;

    public ChessBoard() {
        super();
        bkg = new ImageView(Configure.getResource("drawable/chessboard.png"));
        bkg.getStyleClass().add("chess_board_background");
        getChildren().add(bkg);
    }

}
