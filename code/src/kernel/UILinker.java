package kernel;

import addons.AI;
import ui.frameworks.ContentPane;
import ui.frameworks.dialogs.GameDisplay;
import ui.nodes.StepListView;
import ui.nodes.TimeDisplay;

import java.io.*;
import java.util.ArrayList;

public class UILinker {
    private DataCenter dataCenter;
    private GameDisplay gameDisplay;
    private int peekIndex = 0;
    private ContentPane contentPane;
    private String currentSave = "";
    private AI ai;
    private static UILinker self;

    public UILinker(GameDisplay gameDisplay, DataCenter dataCenter) {
        contentPane = ContentPane.getSelf();
        self = this;
        this.gameDisplay = gameDisplay;
        this.dataCenter = dataCenter;
    }

    public static UILinker getSelf() {
        return self;
    }
    //由于该类只会创建一个对象，故使用一个静态变量来保存唯一的对象，以便于外界访问

    public void push(int i, int j) {
        if (dataCenter.getSteps().empty())
            gameDisplay.getOperationBar().getTimeDisplay().start();
        cutToSelected();
        int currentType = getCurrentType();
        dataCenter.getState()[i][j] = currentType;
        dataCenter.getSteps().push(new Step(i, j));
        StepListView list = gameDisplay.getOperationBar().getListView();
        list.getItems().add("[" + getTypeName(currentType) + "]" + " " + getPieceName(i, j));
        list.getSelectionModel().select(list.getItems().size() - 1);
        gameDisplay.getPiece(i, j).push(currentType);
        markSequence(i, j);
        if(ai!= null && dataCenter.getWinner() == null && getCurrentType() == ai.getType()){
            Step aiPush = ai.push();
            if(aiPush != null){
                push(aiPush.getI(),aiPush.getJ());
            }else{
                contentPane.showTips("您的棋艺高超，AI表示投降！");
            }
        }
    }
    //在指定位置下棋（自动判断黑白棋，且做到界面与数据的同步）

    public void markSequence(int i, int j) {
        for (int k = 0; k < Orientation.orientations.length; k++) {
            PieceSequence ps = new PieceSequence(dataCenter, new Step(i, j), Orientation.orientations[k], dataCenter.getSteps().size() - 1);
            if (ps.matchLiveThree()) {
                dataCenter.getLiveThrees().add(ps);
                highlight(ps.getMatchResult(), 0.3);
                ps.setDisplayed(true);
            }
            if (ps.matchLiveFour()) {
                dataCenter.getLiveFours().add(ps);
                highlight(ps.getMatchResult(), 0.5);
                ps.setDisplayed(true);
            }
            if (ps.matchWinner()) {
                dataCenter.setWinner(ps);
                gameDisplay.setClosed(true);
                contentPane.showTips("恭喜" + getTypeName(dataCenter.getState()[i][j]) + "取得胜利！你可以 [保存存档] 或单击 [开始新游戏] 来重开一局。");
                gameDisplay.getOperationBar().getTimeDisplay().stop();
                highlight(ps.getMatchResult(), 1.0);
                ps.setDisplayed(true);
            }
        }
    }
    /*从指定棋子(i,j)开始判断是否出现活三、活四、胜利，如果满足条件则将棋子序列添加到dataCenter中，同时
      在棋盘上高亮显示
    */

   public void checkSequence() {
        for (int i = 0; i < dataCenter.getLiveThrees().size(); i++) {
            PieceSequence ps = dataCenter.getLiveThrees().get(i);
            ArrayList<Step> p = ps.getMatchResult();
            boolean matched = ps.matchLiveThree();
            if (ps.isDisplayed() && !matched) {
                unhighlight(p);
                ps.setDisplayed(false);
            }
        }
        for (int i = 0; i < dataCenter.getLiveFours().size(); i++) {
            PieceSequence ps = dataCenter.getLiveFours().get(i);
            ArrayList<Step> p = ps.getMatchResult();
            boolean matched = ps.matchLiveFour();
            if (ps.isDisplayed() && !matched) {
                unhighlight(p);
                ps.setDisplayed(false);
            }
        }
        PieceSequence winner = dataCenter.getWinner();
        if (winner != null) {
            ArrayList<Step> p = winner.getMatchResult();
            if (winner.isDisplayed() && !winner.matchWinner()) {
                unhighlight(p);
                winner.setDisplayed(false);
            }
        }
    }
    //当棋盘数据改变时，对以及生成的活三、活四再次检查，如果不满足条件则取消高亮显示

    public void checkSequence(int[][] tempState) {
        int[][] trueState = dataCenter.getState();
        dataCenter.setState(tempState);
        checkSequence();
        for (int i = 0; i < dataCenter.getLiveThrees().size(); i++) {
            PieceSequence ps = dataCenter.getLiveThrees().get(i);
            ArrayList<Step> p = ps.getMatchResult();
            if (!ps.isDisplayed() && ps.matchLiveThree()) {
                highlight(p, 0.3);
                ps.setDisplayed(true);
            }
        }
        for (int i = 0; i < dataCenter.getLiveFours().size(); i++) {
            PieceSequence ps = dataCenter.getLiveFours().get(i);
            ArrayList<Step> p = ps.getMatchResult();
            if (!ps.isDisplayed() && ps.matchLiveFour()) {
                highlight(p, 0.5);
                ps.setDisplayed(true);
            }
        }
        PieceSequence winner = dataCenter.getWinner();
        if (winner != null && !winner.isDisplayed() && winner.matchWinner()) {
            highlight(winner.getMatchResult(), 1.0);
            winner.setDisplayed(true);
        }
        dataCenter.setState(trueState);
    }
    //悔棋时对已经取消高亮显示的活三、活四再次判断，如果成立，则再次高亮显示，tempState指定的是悔棋后所在步骤的棋盘数据

    private void highlight(ArrayList<Step> p, double opacity) {
        for (int i = 0; i < p.size(); i++) {
            gameDisplay.getPiece(p.get(i).getI(), p.get(i).getJ()).highlight(opacity);
        }
    }
    //对指定序列p中的所有棋子高亮显示，opacity指定高亮透明度

    private void unhighlight(ArrayList<Step> p) {
        for (int i = 0; i < p.size(); i++) {
            gameDisplay.getPiece(p.get(i).getI(), p.get(i).getJ()).unhighlight();
        }
    }
    //取消序列p中棋子的高亮显示

    public void restart() {
        gameDisplay.getOperationBar().getListView().getSelectionModel().select(0);
        cutToSelected();
        gameDisplay.getOperationBar().getStepButtons()[1].setDisable(true);
        dataCenter.getLiveFours().clear();
        dataCenter.getLiveFours().clear();
        gameDisplay.setClosed(false);
        gameDisplay.getOperationBar().getTimeDisplay().stop();
        gameDisplay.getOperationBar().getTimeDisplay().clear();
        dataCenter.setWinner(null);
        currentSave = "";
        updateCountDisplay();
    }
    //重新开始游戏，计时归零

    public void cutToSelected() {
        int n = dataCenter.getSteps().size();
        if (peekIndex >= n - 1) {
            return;
        }
        for (int k = n - 1; k > peekIndex; k--) {
            StepListView list = gameDisplay.getOperationBar().getListView();
            if (list.getItems().size() >= k + 2)
                list.getItems().remove(k + 1);
            int i = dataCenter.getSteps().get(k).getI();
            int j = dataCenter.getSteps().get(k).getJ();
            dataCenter.getState()[i][j] = PieceType.EMPTY;
            dataCenter.getSteps().pop();
        }
        for (int i = 0; i < dataCenter.getLiveThrees().size(); i++) {
            PieceSequence ps = dataCenter.getLiveThrees().get(i);
            ArrayList<Step> p = ps.getMatchResult();
            if (ps.getStepIndex() > peekIndex) {
                if (ps.isDisplayed())
                    unhighlight(p);
                dataCenter.getLiveThrees().remove(i);
            }
        }
        for (int i = 0; i < dataCenter.getLiveFours().size(); i++) {
            PieceSequence ps = dataCenter.getLiveFours().get(i);
            ArrayList<Step> p = ps.getMatchResult();
            if (ps.getStepIndex() > peekIndex) {
                if (ps.isDisplayed())
                    unhighlight(p);
                dataCenter.getLiveFours().remove(i);
            }
        }
        if(ai!=null && ai.getType()!=AI.getEnemyType(getCurrentType())){
            ai.setType(AI.getEnemyType(getCurrentType()));
            ai.clear();
        }
    }
    //悔棋后如果用户直接下棋，则将悔棋所在步骤与悔棋前步骤之间的所有棋子删除

    public void peek(int index) {
        int n = dataCenter.getSteps().size();
        updateCountDisplay();
        if (index == peekIndex || index >= n || index < -1) {
            return;
        } else if (index > peekIndex) {
            for (int k = peekIndex + 1; k <= index; k++) {
                int i = dataCenter.getSteps().get(k).getI();
                int j = dataCenter.getSteps().get(k).getJ();
                int type = getType(k);
                gameDisplay.getPiece(i, j).push(type);
            }
        } else if (index < peekIndex) {
            for (int k = peekIndex; k > index; k--) {
                int i = dataCenter.getSteps().get(k).getI();
                int j = dataCenter.getSteps().get(k).getJ();
                gameDisplay.getPiece(i, j).pop();
            }
        }
        peekIndex = index;
        checkSequence(gameDisplay.getCurrentState());
    }
    //将界面数据临时退回到第index步

    public void saveGame(String name) {
        dataCenter.setTime(gameDisplay.getOperationBar().getTimeDisplay().getTime());
        try {
            File dir = new File("saves/");
            if(!dir.exists()){
                System.out.println("Created!");
                dir.mkdir();
            }
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("saves/" + name + ".game"));
            output.writeObject(dataCenter);
            output.close();
        } catch (IOException ex){
            contentPane.showTips("无法保存游戏存档，请检查磁盘设置！");
        }
    }
    //保存存档，name指定存档名（不包含拓展名）

    public void loadGame(String name) {
        restart();
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream("saves/" + name + ".game"));
            dataCenter = (DataCenter) (input.readObject());
            gameDisplay.setDataCenter(dataCenter);
            input.close();
        } catch (FileNotFoundException ex){
            ContentPane.getSelf().showTips("无法找到存档文件 [" + name + ".game] 请尝试刷新列表！");
        } catch (IOException ex) {
            ContentPane.getSelf().showTips("存档文件 [" + name + ".game] 已损坏！无法读取！");
        } catch (ClassNotFoundException ex) {
            ContentPane.getSelf().showTips("出于某些原因，无法读取该存档文件！");
        }
        sync();
        if(ai!=null){
            ai.update(dataCenter,AI.getEnemyType(getCurrentType()));
        }
    }
    //载入存档，name指定存档名（不包含拓展名）

    public void sync() {
        StepListView list = gameDisplay.getOperationBar().getListView();
        for (int i = 0; i < dataCenter.getLiveThrees().size(); i++)
            dataCenter.getLiveThrees().get(i).setDisplayed(false);
        for (int i = 0; i < dataCenter.getLiveFours().size(); i++)
            dataCenter.getLiveFours().get(i).setDisplayed(false);
        if (dataCenter.getWinner() != null) {
            dataCenter.getWinner().setDisplayed(false);
            gameDisplay.setClosed(true);
        }
        for (int k = 0; k < dataCenter.getSteps().size(); k++) {
            Step step = dataCenter.getSteps().get(k);
            int i = step.getI();
            int j = step.getJ();
            int type = dataCenter.getState()[i][j];
            gameDisplay.getPiece(i, j).push(type);
            list.getItems().add("[" + getTypeName(type) + "]" + " " + getPieceName(i, j));
        }
        list.getSelectionModel().select(list.getItems().size() - 1);
        checkSequence(dataCenter.getState());
        TimeDisplay timeDisplay = gameDisplay.getOperationBar().getTimeDisplay();
        timeDisplay.setTime(dataCenter.getTime());
        if (dataCenter.getWinner() == null && dataCenter.getSteps().size()>0) {
            timeDisplay.start();
        }
    }
    //载入存档后，将界面与数据进行同步

    public int getType(int count) {
        return (count % 2 == 0) ? (PieceType.BLACK) : (PieceType.WHITE);
    }
    //取在下第count步时的执棋者

    public int getCurrentType() {
        return this.getType(dataCenter.getSteps().size());
    }
    //取当前执棋者

    public static String getPieceName(int i, int j) {
        return (String.valueOf((char) ('A' + j)) + (i + 1));
    }
    //取指定位置棋子的名称

    public String getTypeName() {
        return getTypeName(getCurrentType());
    }
    //取当前执棋者名称

    public static String getTypeName(int type) {
        if (type == PieceType.BLACK)
            return "黑方";
        else if (type == PieceType.WHITE)
            return "白方";
        return "无";
    }
    //输入棋子类型(PieceType),返回执棋者名称（"黑方"或"白方"）

    public void updateCountDisplay(int type) {
        int size = dataCenter.getSteps().size();
        switch (type) {
            case PieceType.BLACK:
                gameDisplay.getOperationBar().getBlackCountDisplay().setText(String.valueOf(size / 2 + size % 2));
                break;
            case PieceType.WHITE:
                gameDisplay.getOperationBar().getWhiteCountDisplay().setText(String.valueOf(size / 2));
                break;
        }
    }
    //更新黑白棋数的显示，type指定黑棋还是白棋

    public void updateCountDisplay() {
        this.updateCountDisplay(PieceType.BLACK);
        this.updateCountDisplay(PieceType.WHITE);
    }
    //更新黑白棋数的显示，黑白棋均更新

    public String getCurrentSave() {
        return this.currentSave;
    }
    //取当前存档名称

    public void setCurrentSave(String save) {
        this.currentSave = save;
    }
    //设置当前存档名称

    public void aiPowerOn(){
        ai = new AI(dataCenter,AI.getEnemyType(getCurrentType()));
    }
    //开启AI

    public void aiPowerOff(){
        ai = null;
    }
    //关闭AI
}