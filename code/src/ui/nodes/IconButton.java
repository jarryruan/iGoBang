package ui.nodes;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

/**
 * 图标按钮（仅显示图标，无文字）
 */

public class IconButton extends Button {
    private ImageView img;

    public IconButton(String resURL) {
        super("");
        getStyleClass().add("icon_button");
        try {
            img = new ImageView(resURL);
            setGraphic(img);
            setPrefSize(img.getImage().getWidth() * 1.2, img.getImage().getHeight() * 1.2);
        } catch (Exception ex) {

        }
    }

    public ImageView getImageView() {
        return this.img;
    }
    //取按钮的图标
}
