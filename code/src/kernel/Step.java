package kernel;

import java.io.Serializable;

/**
 * 该类(Step)的作用是保存单次落棋的落棋点，以辅助活三、活四、胜利的判断以及悔棋功能，变量中的i，j代表
 * 棋子的行标和列标
 */

public class Step implements Serializable {
    private int i;
    private int j;

    public Step(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public Step() {
        this(0, 0);
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setJ(int j) {
        this.j = j;
    }
}
