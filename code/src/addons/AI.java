package addons;

import kernel.*;

import java.util.ArrayList;

public class AI {
    private DataCenter dataCenter;
    private int type;
    private ArrayList<Step> attackList;
    private ArrayList<Step> defenceList;
    private Step lastAttack = null;
    private int di = 0;
    private int dj = 0;
    private static final int maxTry = 50;
    private final AttackDirection[] attackDirections = {
            new AttackDirection(Direction.NULL, Direction.RIGHT),
            new AttackDirection(Direction.DOWN, Direction.LEFT),
            new AttackDirection(Direction.DOWN, Direction.RIGHT),
            new AttackDirection(Direction.DOWN, Direction.NULL),
            new AttackDirection(Direction.UP, Direction.RIGHT),
            new AttackDirection(Direction.NULL, Direction.LEFT),
            new AttackDirection(Direction.UP, Direction.LEFT),
            new AttackDirection(Direction.UP, Direction.NULL)
    };

    public AI(DataCenter dataCenter, int type) {
        attackList = new ArrayList<>();
        defenceList = new ArrayList<>();
        update(dataCenter, type);
    }

    public void setType(int type) {
        this.type = type;
    }
    //设置AI棋子颜色

    public int getType() {
        return this.type;
    }
    //获取AI棋子颜色

    public void update(DataCenter dataCenter, int type) {
        this.dataCenter = dataCenter;
        this.type = type;
        findNewSpot();
    }
    //更新AI信息

    public void clear() {
        attackList.clear();
        defenceList.clear();
    }
    //重置AI

    public void setDataCenter(DataCenter dataCenter) {
        this.dataCenter = dataCenter;
    }
    //设置AI对应的数据对象

    public boolean findNewSpot() {
        attackList.clear();
        if (findExistedSpot()) {
            return true;
        } else if (findRandomSpot()) {
            return true;
        }
        return false;
    }
    //寻找新的据点用于进攻（智能选取）

    public boolean findExistedSpot() {
        int[][] state = dataCenter.getState();
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (state[i][j] == type && addSpot(i, j)) {
                    return true;
                }
            }
        }
        return false;
    }
    //以已经存在的己方棋子为基础建立据点

    public boolean findRandomSpot() {
        int count = 0;
        int i, j;
        do {
            i = random(1, 15);
            j = random(1, 15);
            count++;
            if (addSpot(i, j)) {
                return true;
            }
        } while (count < maxTry);
        return false;
    }
    //寻找随机的位置来建立据点

    public boolean addSpot(int i, int j, int di, int dj) {
        int state[][] = dataCenter.getState();
        ArrayList<Step> buffer = new ArrayList<>();
        for (int k = 0; k < 5; k++) {
            int ci = i + k * di;
            int cj = j + k * dj;
            if (!withinRange(ci) || !withinRange(cj) || state[ci][cj] == getEnemyType(type)) {
                buffer.clear();
                return false;
            } else if (state[ci][cj] == type) {
                continue;
            } else if (state[ci][cj] == PieceType.EMPTY) {
                buffer.add(new Step(ci, cj));
            }
        }
        if (!buffer.isEmpty()) {
            attackList.addAll(0, buffer);
            return true;
        }
        return false;
    }
    //增加据点（单个方向），带有判断合法性功能，当据点不被敌方棋子阻挡时返回真

    public boolean addSpot(int i, int j) {
        for (AttackDirection d : attackDirections) {
            di = d.getDirectionI();
            dj = d.getDirectionJ();
            if (addSpot(i, j, di, dj)) {
                return true;
            }
        }
        return false;
    }
    //增加据点（遍历各个方向），带有判断合法性功能，当据点不被敌方棋子阻挡时返回真

    public boolean defence() {
        int s = type;
        int e = getEnemyType(type);
        int n = PieceType.EMPTY;
        Step last = dataCenter.getSteps().peek();

        for (int o : Orientation.orientations) {
            int[] directions = PieceSequence.orientationToDirections(o);
            int di = directions[0];
            int dj = directions[1];
            PieceSequence ps = new PieceSequence(dataCenter, last, o, dataCenter.getSteps().size() - 1);
            int matchCode[][] = {
                    {e, e, n, e},
                    {e, n, e, e},
                    {e, e, e, n},
                    {n, e, e, e},
                    {e, e, e, n, e},
                    {e, n, e, e, e}
            };
            if (ps.match(matchCode[0], false)) {
                Step r = ps.getMatchResult().get(1);
                defenceList.add(new Step(r.getI() + di, r.getJ() + dj));
            } else if (ps.match(matchCode[1], false)) {
                Step r = ps.getMatchResult().get(0);
                defenceList.add(new Step(r.getI() + di, r.getJ() + dj));
            } else if (ps.match(matchCode[2], false)) {
                Step r = ps.getMatchResult().get(2);
                defenceList.add(new Step(r.getI() + di, r.getJ() + dj));
            } else if (ps.match(matchCode[3], false)) {
                Step r = ps.getMatchResult().get(0);
                defenceList.add(new Step(r.getI() - di, r.getJ() - dj));
            } else if (ps.match(matchCode[4], false)) {
                Step r = ps.getMatchResult().get(2);
                defenceList.add(new Step(r.getI() + di, r.getJ() + dj));
            } else if (ps.match(matchCode[5], false)) {
                Step r = ps.getMatchResult().get(0);
                defenceList.add(new Step(r.getI() + di, r.getJ() + dj));
            }

        }
        return false;
    }
    //判断是否需要防御

    public Step push() {
        defence();
        if (!defenceList.isEmpty()) {
            Step r = defenceList.get(0);
            defenceList.clear();
            return r;
        }
        if (!attackList.isEmpty()) {
            Step r = attackList.get(0);
            if (dataCenter.getState()[r.getI()][r.getJ()] == getEnemyType(type)) {
                if (addSpot(lastAttack.getI(), lastAttack.getJ(), -di, -dj)) {
                    return push();
                }
                attackList.clear();
                if (findNewSpot()) {
                    return push();
                } else {
                    return null;
                }
            } else if (dataCenter.getState()[r.getI()][r.getJ()] == type) {
                attackList.remove(0);
                return push();
            }
            attackList.remove(0);
            lastAttack = r;
            return r;
        } else if (findNewSpot()) {
            return push();
        }
        return null;
    }
    //取得AI最终需要放置的棋子的坐标

    public static int getEnemyType(int type) {
        return (type == PieceType.BLACK) ? PieceType.WHITE : PieceType.BLACK;
    }
    //获取敌方棋子类型

    private int random(int min, int max) {
        return (int) (Math.random() * max + min);
    }
    //取随机整数（位于min与max之间，包括min和max）

    private static boolean withinRange(int i) {
        return (i >= 0 && i < 15);
    }
    //判断坐标是否在棋盘范围内

    private class AttackDirection {
        private int directionI = 0;
        private int directionJ = 0;

        public AttackDirection(int directionI, int directionJ) {
            this.directionI = directionI;
            this.directionJ = directionJ;
        }

        public int getDirectionI() {
            return this.directionI;
        }

        public int getDirectionJ() {
            return this.directionJ;
        }
    }
    //内部类，用于记录AI进攻方向
}
