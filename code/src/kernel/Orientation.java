package kernel;

public class Orientation {
    public static final int NULL = 0;
    //无方向
    public static final int L_R = 1;
    //水平方向（LEFT_RIGHT）
    public static final int U_D = 2;
    //竖直方向（UP_DOWN)
    public static final int LU_RD = 3;
    //主对角线方向(LEFTUP_RIGHTDOWN)
    public static final int RU_LD = 4;
    //副对角线方向(RIGHTUP_LEFTDOWN)
    public static final int[] orientations = {L_R, U_D, LU_RD, RU_LD};

    private Orientation() {

    }
}
