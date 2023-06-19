package game.modules.Slot3x3;

import game.GameConfig.GameConfig;
import game.utils.GameUtil;

public class Slot3x3Util {
    public static final byte WILD = 0;
    public static final byte MAX_ICON = 6;
    public static final byte[][] ROWS = {
            {0, 0, 0},
            {1, 1, 1},
            {2, 2, 2},
            {0, 2, 0},
            {2, 0, 2},

            {0, 1, 0},
            {0, 1, 2},
            {2, 1, 0},
            {1, 2, 1},
            {1, 0, 1},

            {2, 1, 2},
            {0, 0, 1},
            {1, 1, 2},
            {1, 1, 0},
            {2, 2, 1},

            {1, 0, 0},
            {2, 1, 1},
            {0, 1, 1},
            {1, 2, 2},
            {0, 2, 1}
    };


    public static Slot3x3TableInfo rollLose0(int[] rowIndex, long betLevel, long moneyEatJackpot) {
        while (true) {
            Slot3x3TableInfo slot3x3TableInfo = new Slot3x3TableInfo(GameConfig.getInstance().slot3x3GameConfig.getTableValue(), betLevel, moneyEatJackpot);
            slot3x3TableInfo.calculateRowIndex(rowIndex);
            if (slot3x3TableInfo.lineWin.size() == 0) {
                return slot3x3TableInfo;
            }
        }
    }

    public static Slot3x3TableInfo rollLose20(int[] rowIndex, long betLevel, long moneyEatJackpot) {
        while (true) {
            Slot3x3TableInfo slot3x3TableInfo = new Slot3x3TableInfo(GameConfig.getInstance().slot3x3GameConfig.getTableValue(), betLevel, moneyEatJackpot);
            slot3x3TableInfo.calculateRowIndex(rowIndex);
            if (!slot3x3TableInfo.isJackPot && !(slot3x3TableInfo.money > rowIndex.length * 10)) {
                return slot3x3TableInfo;
            }
        }
    }

    public static long getMoneyJackPot(long jackPotMoney, boolean isX2, long initPotMoney){
        long moneyEatJackpot = jackPotMoney;
        if (isX2) {
            moneyEatJackpot = jackPotMoney * GameConfig.getInstance().slot3x3GameConfig.MULTI_JACKPOT - (jackPotMoney - initPotMoney);
        }
        return moneyEatJackpot;
    }

    public static Slot3x3TableInfo getSlot3x1TableInfoBigWin(int[] rowIndex, long betLevel, boolean isX2,
                                                             long jackPotMoney, long initPotMoney){
        long moneyEatJackpot = getMoneyJackPot(jackPotMoney,isX2, initPotMoney);

        int x = GameUtil.randomMax(100);

        if(x<50){
            return rollLose0(rowIndex, betLevel, moneyEatJackpot);
        }

        while (true){
            Slot3x3TableInfo slot3x3TableInfo = new Slot3x3TableInfo(GameConfig.getInstance().
                    slot3x3GameConfig.getTableValue(), betLevel, moneyEatJackpot);
            slot3x3TableInfo.calculateRowIndex(rowIndex);
            if (slot3x3TableInfo.isJackPot) {
                continue;
            }


            return slot3x3TableInfo;
        }
    }

    public static Slot3x3TableInfo getSlot3x1TableInfo(int[] rowIndex, long betLevel, long fund, long fundJackpot, boolean isX2,
                                                       long jackPotMoney, long initPotMoney, long maxiumWin) {
        long moneyEatJackpot = getMoneyJackPot(jackPotMoney,isX2, initPotMoney);
        Slot3x3TableInfo slot3x3TableInfo = new Slot3x3TableInfo(GameConfig.getInstance().
                slot3x3GameConfig.getTableValue(), betLevel, moneyEatJackpot);

        slot3x3TableInfo.calculateRowIndex(rowIndex);
        if (slot3x3TableInfo.isJackPot) {
            if (rowIndex.length < ROWS.length) { // roll khong du hang thi khong duoc an jackpot
                return rollLose0(rowIndex, betLevel, moneyEatJackpot);
            }
            if (isX2) {
                long moneyToFundJackpot = jackPotMoney * GameConfig.getInstance().slot3x3GameConfig.MULTI_JACKPOT;
                if (fundJackpot < moneyToFundJackpot) {
                    return rollLose0(rowIndex, betLevel, moneyEatJackpot);
                }
            } else {
                if (fundJackpot < jackPotMoney) {
                    return rollLose0(rowIndex, betLevel, moneyEatJackpot);
                }
            }
            if (slot3x3TableInfo.money * betLevel / 10 > fund) { // qua quy thi cho thua
                return rollLose0(rowIndex, betLevel, moneyEatJackpot);
            }

            if ((slot3x3TableInfo.money * betLevel / 10 + moneyEatJackpot)> maxiumWin) { // gioi han tien toi da
                return rollLose0(rowIndex, betLevel, moneyEatJackpot);
            }
        } else {

            if (slot3x3TableInfo.money * betLevel / 10 > maxiumWin) { // gioi han tien toi da
                return rollLose0(rowIndex, betLevel, moneyEatJackpot);
            }

            if (slot3x3TableInfo.money * betLevel / 10 > fund) { // qua quy thi cho thua
                return rollLose0(rowIndex, betLevel, moneyEatJackpot);
            }
            if (rowIndex.length < ROWS.length) { // roll khong du hang
                if (slot3x3TableInfo.money > rowIndex.length * 10 * 2) { // an qua 2 lan so tien cuoc thi thua
                    return rollLose0(rowIndex, betLevel, moneyEatJackpot);
                }
            }
        }
        return slot3x3TableInfo;
    }
}
