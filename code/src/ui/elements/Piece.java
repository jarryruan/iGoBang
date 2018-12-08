package ui.elements;

import kernel.PieceType;
import ui.Configure;
import ui.animations.EasingProperty;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * 棋子类（界面层）
 */

public class Piece extends Pane {
    public static final double width = 44;
    public static final double height = 44;

    private int currentType = PieceType.EMPTY;
    private ImageView currentImage;
    private FadingCircle circle;
    private EasingProperty currentScaleAnimation;
    private EasingProperty currentOpacityAnimation;

    private ImageView pieceWhite;
    private ImageView pieceBlack;
    private EasingProperty whiteScaleAnimation;
    private EasingProperty whiteOpacityAnimation;
    private EasingProperty blackScaleAnimation;
    private EasingProperty blackOpacityAnimation;

    private ImageView highlightWhite;
    private ImageView highlightBlack;
    private EasingProperty highlightWhiteAnimation;
    private EasingProperty highlightBlackAnimation;

    public Piece() {
        super();
        this.setOpacity(0.8);
        setPrefSize(width, height);
        pieceWhite = new ImageView(Configure.getResource("drawable/piece_white.png"));
        pieceBlack = new ImageView(Configure.getResource("drawable/piece_black.png"));
        highlightWhite = new ImageView(Configure.getResource("drawable/highlight_white.png"));
        highlightBlack = new ImageView(Configure.getResource("drawable/highlight_black.png"));

        pieceWhite.layoutXProperty().bind(this.widthProperty().subtract(pieceWhite.fitWidthProperty()).divide(2));
        pieceWhite.layoutYProperty().bind(this.heightProperty().subtract(pieceWhite.fitHeightProperty()).divide(2));
        pieceBlack.layoutXProperty().bind(this.widthProperty().subtract(pieceBlack.fitWidthProperty()).divide(2));
        pieceBlack.layoutYProperty().bind(this.heightProperty().subtract(pieceBlack.fitHeightProperty()).divide(2));


        whiteScaleAnimation = new EasingProperty(pieceWhite.fitWidthProperty());
        pieceWhite.fitHeightProperty().bind(pieceWhite.fitWidthProperty());
        whiteOpacityAnimation = new EasingProperty(pieceWhite.opacityProperty());
        blackScaleAnimation = new EasingProperty(pieceBlack.fitWidthProperty());
        blackOpacityAnimation = new EasingProperty(pieceBlack.opacityProperty());
        pieceBlack.fitHeightProperty().bind(pieceBlack.fitWidthProperty());
        pieceBlack.setEffect(blackScaleAnimation.getBlur());
        pieceWhite.setEffect(whiteScaleAnimation.getBlur());

        highlightWhiteAnimation = new EasingProperty(highlightWhite.opacityProperty());
        highlightBlackAnimation = new EasingProperty(highlightBlack.opacityProperty());
    }

    public void push(int type) {
        if (currentType == type) {
            return;
        }
        getChildren().clear();
        switch (type) {
            case PieceType.BLACK:
                currentImage = pieceBlack;
                currentScaleAnimation = blackScaleAnimation;
                currentOpacityAnimation = blackOpacityAnimation;
                currentType = PieceType.BLACK;
                break;
            case PieceType.WHITE:
                currentImage = pieceWhite;
                currentScaleAnimation = whiteScaleAnimation;
                currentOpacityAnimation = whiteOpacityAnimation;
                currentType = PieceType.WHITE;
                break;
            default:
                return;
        }
        currentScaleAnimation.setValueImmediately(width * 2.2);
        currentScaleAnimation.setToValue(width);
        currentOpacityAnimation.setValueImmediately(0.0);
        currentOpacityAnimation.setToValue(1.0);
        circle = new FadingCircle(width / 2, height / 2);
        setMouseTransparent(true);
        currentType = type;
        getChildren().addAll(circle, currentImage);
    }
    //显示指定类型的棋子（黑或白）

    public int getCurrentType() {
        return currentType;
    }
    //取当前显示的棋子类型

    public void pop() {
        if (currentType == PieceType.EMPTY) {
            return;
        }
        unhighlight();
        currentScaleAnimation.setToValue(width * 1.5);
        currentOpacityAnimation.setToValue(0.0);
        currentType = PieceType.EMPTY;
        if (circle != null) {
            getChildren().remove(circle);
        }
        setMouseTransparent(false);
    }
    //取消棋子的显示

    public void highlight(double opacity) {
        EasingProperty animation;
        ImageView img;
        if (currentType == PieceType.BLACK) {
            animation = highlightBlackAnimation;
            img = highlightBlack;
        } else if (currentType == PieceType.WHITE) {
            animation = highlightWhiteAnimation;
            img = highlightWhite;
        } else {
            return;
        }
        if (!getChildren().contains(img)) {
            getChildren().add(img);
        }
        animation.setValueImmediately(0);
        animation.setToValue(opacity);
    }
    //高亮显示该棋子（opacity指定透明度）

    public void unhighlight() {
        EasingProperty animation;
        if (currentType == PieceType.BLACK) {
            animation = highlightBlackAnimation;
        } else if (currentType == PieceType.WHITE) {
            animation = highlightWhiteAnimation;
        } else {
            return;
        }
        animation.setToValue(0.0);
    }
    //取消高亮显示
}
