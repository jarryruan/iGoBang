package ui.nodes;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

/**
 * 导航栏
 */

public class MenuBar extends HBox {
    private ArrayList<MenuButton> buttons;
    public static double height = 75;

    public MenuBar() {
        getStyleClass().add("menu_bar");
        setPrefHeight(height);
        setAlignment(Pos.CENTER);
        buttons = new ArrayList<>();
    }

    public void addButton(String title) {
        MenuButton btn = new MenuButton(title);
        buttons.add(btn);
        getChildren().add(btn);
    }
    //添加导航按钮

    public void clear() {
        getChildren().clear();
        buttons.clear();
    }
    //清除按钮

    public MenuButton getButton(int index) {
        if (index >= 0 && index < buttons.size()) {
            return buttons.get(index);
        }
        return null;
    }
    //取指定按钮
}
