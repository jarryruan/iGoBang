package ui.nodes;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import ui.Configure;

/**
 * 存档列表框
 */

public class SaveListView extends ListView<String> {
    public SaveListView() {
        super();
        getStylesheets().add(Configure.getResource("stylesheet/savelist.css"));
        setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new MyListCell();
            }
        });

    }

    private class MyListCell extends ListCell<String> {
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            this.setText(item);
            if (!empty) {
                this.setGraphic(new ImageView(Configure.getResource("drawable/icon/item.png")));
            } else {
                this.setGraphic(null);
            }
        }
    }
}
