package kernel;

import java.io.Serializable;
import java.util.ArrayList;

public class PieceSequence implements Serializable {
    private int orientation = Orientation.NULL;
    protected DataCenter dataCenter;
    protected Step spot;
    protected ArrayList<Step> matchResult;
    protected int stepIndex = 0;
    protected boolean displayed = false;

    protected static final int[] matchCode_liveThree_common_white = {PieceType.EMPTY,
            PieceType.WHITE, PieceType.WHITE, PieceType.WHITE, PieceType.EMPTY};
    protected static final int[] matchCode_liveThree_common_black = {PieceType.EMPTY,
            PieceType.BLACK, PieceType.BLACK, PieceType.BLACK, PieceType.EMPTY};
    protected static final int[] matchCode_liveThree_leap_white = {PieceType.EMPTY,
            PieceType.WHITE, PieceType.EMPTY, PieceType.WHITE, PieceType.WHITE, PieceType.EMPTY};
    protected static final int[] matchCode_liveThree_leap_black = {PieceType.EMPTY,
            PieceType.BLACK, PieceType.EMPTY, PieceType.BLACK, PieceType.BLACK, PieceType.EMPTY};
    protected static final int[] matchCode_liveFour_white = {PieceType.EMPTY,
            PieceType.WHITE, PieceType.WHITE, PieceType.WHITE, PieceType.WHITE, PieceType.EMPTY};
    protected static final int[] matchCode_liveFour_black = {PieceType.EMPTY,
            PieceType.BLACK, PieceType.BLACK, PieceType.BLACK, PieceType.BLACK, PieceType.EMPTY};
    protected static final int[] matchCode_win_white = {PieceType.WHITE,
            PieceType.WHITE, PieceType.WHITE, PieceType.WHITE, PieceType.WHITE};
    protected static final int[] matchCode_win_black = {PieceType.BLACK,
            PieceType.BLACK, PieceType.BLACK, PieceType.BLACK, PieceType.BLACK};
    //以上是匹配码，可用于匹配黑白棋的活三、活四、胜利，详细原理请参考开发文档


    public PieceSequence(DataCenter dataCenter, Step spot, int orientation, int stepIndex) {
        this.dataCenter = dataCenter;
        this.spot = spot;
        this.orientation = orientation;
        this.stepIndex = stepIndex;
    }

    public boolean match(int[] matchCode, boolean reverseCheck) {
        int[][] state = dataCenter.getState();
        int i = spot.getI();
        int j = spot.getJ();

        int[] directions = orientationToDirections(orientation);
        if (directions == null) {
            return false;
        }
        int directionI = directions[0];
        int directionJ = directions[1];

        int n = matchCode.length;
        for (int p = 0; p < n; p++) {
            int[] source = getStateCode(state, i, j, n, p, this.orientation);
            if (arrayEquals(source, matchCode)) {
                matchResult = getSequence(i - directionI * p, j - directionJ * p, orientation, matchCode);
                return true;
            }
            if (reverseCheck) {
                int[] reverse = arrayReverse(matchCode);
                if (arrayEquals(source, reverse)) {
                    matchResult = getSequence(i - directionI * p, j - directionJ * p, orientation, reverse);
                    return true;
                }
            }
        }
        return false;
    }
    //检查各个方向是否有与匹配码matchCode匹配的棋盘数据，参数reverseCheck标记是否允许将matchCode首位倒置后检查

    protected static ArrayList<Step> getSequence(int startI, int startJ, int orientation, int[] matchCode) {
        int[] directions = orientationToDirections(orientation);
        if (directions == null) {
            return null;
        }
        int directionI = directions[0];
        int directionJ = directions[1];
        ArrayList<Step> result = new ArrayList<>();
        for (int k = 0; k < matchCode.length; k++) {
            if (matchCode[k] != PieceType.EMPTY) {
                result.add(new Step(startI + k * directionI, startJ + k * directionJ));
            }
        }
        return result;
    }
    /*由棋子(startI,startJ)开始，在指定方向上的n个棋格中，取满足标记条件的棋子坐标（如活三活四），以一维数组(ArrayList)的形式返回
    其中方向由orientation指定，n与匹配码matchCode的长度相等*/

    public boolean matchLiveThree() {
        if (match(matchCode_liveThree_common_white, false) ||
                match(matchCode_liveThree_common_black, false) ||
                match(matchCode_liveThree_leap_white, true) ||
                match(matchCode_liveThree_leap_black, true)) {
            return true;
        }
        return false;
    }
    //判断是否满足活三条件

    public boolean matchLiveFour() {
        if (match(matchCode_liveFour_white, false) || match(matchCode_liveFour_black, false)) {
            return true;
        }
        return false;
    }
    //判断是否满足活四条件

    public boolean matchWinner() {
        if (match(matchCode_win_white, false) || match(matchCode_win_black, false)) {
            return true;
        }
        return false;
    }
    //判断是否满足胜利条件

    public boolean isDisplayed() {
        return this.displayed;
    }
    //返回boolean值，标记该序列是否已经在棋盘上高亮显出

    public void setDisplayed(boolean displayed) {
        this.displayed = displayed;
    }
    //设置是否已经高亮显出

    public int getStepIndex() {
        return this.stepIndex;
    }
    //取生成该棋子序列时，已经走过的步数

    public ArrayList<Step> getMatchResult() {
        return this.matchResult;
    }
    //返回高亮棋子数列（比如对于满足活三的棋子序列，则返回活三中三个棋子的坐标）

    public static int[] orientationToDirections(int orientation) {
        int directionI = Direction.NULL;
        int directionJ = Direction.NULL;
        switch (orientation) {
            case Orientation.L_R:
                directionJ = Direction.RIGHT;
                break;
            case Orientation.U_D:
                directionI = Direction.DOWN;
                break;
            case Orientation.LU_RD:
                directionI = Direction.DOWN;
                directionJ = Direction.RIGHT;
                break;
            case Orientation.RU_LD:
                directionI = Direction.DOWN;
                directionJ = Direction.LEFT;
                break;
            default:
                return null;
        }
        int directions[] = {directionI, directionJ};
        return directions;

    }
    //orientation到direction的转换，返回值中int[0]代表行方向(i的增加方向)，int[1]代表列方向(j的增加方向)

    protected static int[] getStateCode(int[][] state, int i, int j, int n, int offset, int orientation) {
        int[] result = new int[n];
        for (int k = 0; k < result.length; k++) {
            result[k] = -1;
        }
        int[] directions = orientationToDirections(orientation);
        if (directions == null) {
            return result;
        }
        int directionI = directions[0];
        int directionJ = directions[1];
        int currentI = i - offset * directionI;
        int currentJ = j - offset * directionJ;
        for (int k = 0; k < n; k++) {
            if (currentI < 0 || currentI >= state[0].length || currentJ < 0 || currentJ >= state.length)
                break;
            result[k] = state[currentI][currentJ];
            currentI += directionI;
            currentJ += directionJ;
        }
        return result;
    }
    /*从给定的棋盘数组state[][]中，棋子(i,j)开始，沿着指定方向(orientation)取n个棋格的状态，其中offset是偏移量，
      代表棋子(i,j)在n个棋子中的相对位置，offset = 0代表(i,j)是第一个棋子*/

    protected static boolean arrayEquals(int[] a, int[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }
    //辅助函数，判断两个数组的内容是否相同

    protected static int[] arrayReverse(int[] a) {
        int n = a.length;
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = a[n - i - 1];
        }
        return result;
    }
    //辅助函数，返回数组a首尾倒置后生成的新数组
}
