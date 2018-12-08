package ui.nodes;

import javafx.scene.control.ListView;
import ui.Configure;

/**
 * 悔棋栈列表框（用于实时悔棋）
 */

public class StepListView extends ListView<String> {
    public StepListView() {
        getStylesheets().add(Configure.getResource("stylesheet/steplist.css"));
    }
}
