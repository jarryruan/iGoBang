package kernel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

public class DataCenter implements Serializable {
    private int[][] state;
    private Stack<Step> steps;
    private ArrayList<PieceSequence> liveThrees;
    private ArrayList<PieceSequence> liveFours;
    private PieceSequence winner;
    private long time = 0;

    public DataCenter() {
        state = new int[15][15];
        steps = new Stack<>();
        liveThrees = new ArrayList<>();
        liveFours = new ArrayList<>();
    }

    public void push(int type, int i, int j) {
        state[i][j] = type;
        steps.push(new Step(i, j));
    }
    //在指定位置放置一个指定类型的棋子（只操作数据，不操作界面）

    public PieceSequence getWinner() {
        return this.winner;
    }
    //获取胜利者，如胜负未分则返回不含棋子的PieceSequence

    public long getTime() {
        return this.time;
    }
    //获取游戏用时

    public void setTime(long time) {
        this.time = time;
    }
    //设置游戏用时

    public void setWinner(PieceSequence winner) {
        this.winner = winner;
    }
    //设置游戏胜利者

    public Stack<Step> getSteps() {
        return this.steps;
    }
    //获取悔棋栈

    public ArrayList<PieceSequence> getLiveThrees() {
        return this.liveThrees;
    }
    //获取活三列表

    public ArrayList<PieceSequence> getLiveFours() {
        return this.liveFours;
    }
    //获取活四列表

    public int[][] getState() {
        return this.state;
    }
    //获取棋盘数据（二维数组）

    public void setState(int[][] state) {
        this.state = state;
    }
    //替换棋盘数据（二维数组）
}
