package ui.nodes;

import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.shape.Rectangle;
import ui.animations.EasingProperty;

/**
 * 导航栏按钮
 */

public class MenuButton extends Button {
    Rectangle rcBkg;
    EasingProperty opacityAnimation;
    public MenuButton(String title) {
        super(title);
        getStyleClass().add("menu_button");
        rcBkg = new Rectangle(180,60);
        rcBkg.setArcWidth(10);
        rcBkg.setArcHeight(10);
        rcBkg.getStyleClass().add("menu_button_background");
        rcBkg.setOpacity(0.0);
        opacityAnimation = new EasingProperty(rcBkg.opacityProperty());
        setContentDisplay(ContentDisplay.CENTER);
        setGraphic(rcBkg);
        this.setOnMouseEntered(e -> {
            opacityAnimation.setToValue(0.2);
        });
        this.setOnMouseExited(e -> {
            opacityAnimation.setToValue(0.0);
        });
    }
}
