package ui.nodes;

import javafx.beans.InvalidationListener;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import ui.animations.EasingProperty;

public class GameCheckBox extends ToggleButton {
    GameCheckBoxGraph graph;
    public GameCheckBox(String title){
        super(title);
        getStyleClass().add("game_checkbox");
        graph = new GameCheckBoxGraph();
        setGraphic(graph);
        setContentDisplay(ContentDisplay.RIGHT);
        setGraphicTextGap(20);
    }
    private class GameCheckBoxGraph extends Pane{
        private Rectangle strokeRc;
        private Rectangle fillRc;
        private Circle circle;
        private double startCenterX = 10;
        private double endCenterX = 10;
        private EasingProperty centerXEasing;
        private EasingProperty opacityEasing;
        public GameCheckBoxGraph(){
            super();
            strokeRc = new Rectangle();
            strokeRc.setArcWidth(20);
            strokeRc.setArcHeight(20);
            strokeRc.setWidth(60);
            strokeRc.setHeight(20);
            strokeRc.setStroke(Color.color(0.627,0.643,0.741));
            strokeRc.setFill(Color.TRANSPARENT);

            fillRc = new Rectangle();
            fillRc.setArcWidth(20);
            fillRc.setArcHeight(20);
            fillRc.setWidth(60);
            fillRc.setHeight(20);
            fillRc.setStroke(Color.TRANSPARENT);
            fillRc.setFill(Color.color(0,0.431,0.702));
            fillRc.setOpacity(0);
            opacityEasing = new EasingProperty(fillRc.opacityProperty());
            opacityEasing.setEaseIn(true);

            circle = new Circle(7);
            startCenterX = 10;
            endCenterX = 50;
            circle.setFill(Color.WHITE);
            circle.setStroke(Color.LIGHTGRAY);
            circle.setCenterY(startCenterX);
            circle.setCenterX(startCenterX);
            centerXEasing = new EasingProperty(circle.centerXProperty());
            centerXEasing.setEaseIn(true);
            InvalidationListener listener = ov ->{
                if(isSelected()){
                    centerXEasing.setToValue(endCenterX);
                    opacityEasing.setToValue(1);
                }else{
                    centerXEasing.setToValue(startCenterX);
                    opacityEasing.setToValue(0);
                }
            };
            selectedProperty().addListener(listener);
            getChildren().addAll(fillRc,strokeRc,circle);
        }
    }
}
