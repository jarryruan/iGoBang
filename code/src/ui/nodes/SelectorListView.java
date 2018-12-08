package ui.nodes;

import javafx.scene.control.ListView;
import ui.Configure;

/**
 * 选择器列表框（用于选择背景图和背景音乐）
 */

public class SelectorListView extends ListView<String> {
    public SelectorListView() {
        super();
        getStylesheets().add(Configure.getResource("stylesheet/selectorlist.css"));
    }
}
