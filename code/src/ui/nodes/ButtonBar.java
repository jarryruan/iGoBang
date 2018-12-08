package ui.nodes;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

/**
 * 按钮栏
 */

public class ButtonBar extends Pane {
    public final static double height = 100;
    private ArrayList<GameButton> buttons;
    private HBox buttonContainer;

    public ButtonBar() {
        super();
        getStyleClass().addAll("button_bar");
        buttons = new ArrayList<>();
        setPrefHeight(height);
        buttonContainer = new HBox();
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.prefWidthProperty().bind(this.widthProperty());
        buttonContainer.prefHeightProperty().bind(this.heightProperty());
        buttonContainer.setSpacing(20);
        getChildren().addAll(buttonContainer);
    }

    public void addButton(GameButton... btn) {
        for (int i = 0; i < btn.length; i++) {
            buttons.add(btn[i]);
            buttonContainer.getChildren().add(btn[i]);
        }
    }
    //添加按钮

    public void clear() {
        buttonContainer.getChildren().clear();
        buttons.clear();
    }
    //清除所有按钮

    public GameButton getButton(int index) {
        if (index >= 0 && index < buttons.size()) {
            return buttons.get(index);
        }
        return null;
    }
    //取指定按钮
}
