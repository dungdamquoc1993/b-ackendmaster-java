package game.modules.Slot11IconWildLienTuc;

import com.pengrad.telegrambot.model.Game;
import game.GameConfig.GameConfig;
import game.GameConfig.RollLoseConfig.Slot11IconWildLienTucRollLoseConfig;
import game.modules.GameUtil;
import game.modules.SlotUtils.Gift;
import game.modules.SlotUtils.GiftType;
import game.modules.SlotUtils.RowValue;

import java.util.ArrayList;
import java.util.List;

public class Slot11IconWildLienTucUtil {

    public static final int NUMBER_ICONS = 11;

    public static final byte FREE_SPIN = 0;
    public static final byte BONUS = 1;
    public static final byte WILD = 2;
    public static final byte JACKPOT = 3;

    public static final byte[][] ROWS = {
            {5, 6, 7, 8, 9},
            {0, 1, 2, 3, 4},
            {10, 11, 12, 13, 14},
            {10, 6, 2, 8, 14},
            {0, 6, 12, 8, 4},

            {5, 1, 2, 3, 9},
            {5, 11, 12, 13, 9},
            {0, 1, 7, 13, 14},
            {10, 11, 7, 3, 4},
            {5, 11, 7, 3, 9},

            {5, 1, 7, 13, 9},
            {0, 6, 7, 8, 4},
            {10, 6, 7, 8, 14},
            {0, 6, 2, 8, 4},
            {10, 6, 12, 8, 14},

            {5, 6, 2, 8, 9},
            {5, 6, 12, 8, 9},
            {0, 1, 12, 3, 4},
            {10, 11, 2, 13, 14},
            {0, 11, 12, 13, 4},

            {10, 1, 2, 3, 14},
            {5, 1, 12, 3, 9},
            {5, 11, 2, 13, 9},
            {0, 11, 2, 13, 4},
            {10, 1, 12, 3, 14},

    };

    public static final byte[][] COLUMNS = {
            {0, 5, 10},
            {1, 6, 11},
            {2, 7, 12},
            {3, 8, 13},
            {4, 9, 14},
    };

    public static Gift[][] GIFT_TABLES = {
            {null, null, null,
                    new Gift(GiftType.FREE_SPIN, 1), new Gift(GiftType.FREE_SPIN, 2), new Gift(GiftType.FREE_SPIN, 5)},
            {null, null, null,
                    new Gift(GiftType.MINI_GAME, 1), new Gift(GiftType.MINI_GAME, 2), new Gift(GiftType.MINI_GAME, 5)},
            {null, null, null, null,
                    null, null},    // wild
            {null, null, new Gift(GiftType.MONEY, 5),
                    new Gift(GiftType.MONEY, 50), new Gift(GiftType.MONEY, 200), new Gift(GiftType.JACKPOT, 1)},
            {null, null, new Gift(GiftType.MONEY, 2),
                    new Gift(GiftType.MONEY, 15), new Gift(GiftType.MONEY, 100), new Gift(GiftType.MONEY, 200)},
            {null, null, new Gift(GiftType.MONEY, 2),
                    new Gift(GiftType.MONEY, 10), new Gift(GiftType.MONEY, 55), new Gift(GiftType.MONEY, 150)},
            {null, null, new Gift(GiftType.MONEY, 2),
                    new Gift(GiftType.MONEY, 10), new Gift(GiftType.MONEY, 40), new Gift(GiftType.MONEY, 100)},
            {null, null, null,
                    new Gift(GiftType.MONEY, 5), new Gift(GiftType.MONEY, 30), new Gift(GiftType.MONEY, 70)},
            {null, null, null,
                    new Gift(GiftType.MONEY, 5), new Gift(GiftType.MONEY, 20), new Gift(GiftType.MONEY, 55)},
            {null, null, null,
                    new Gift(GiftType.MONEY, 3), new Gift(GiftType.MONEY, 15), new Gift(GiftType.MONEY, 40)},
            {null, null, null,
                    new Gift(GiftType.MONEY, 3), new Gift(GiftType.MONEY, 10), new Gift(GiftType.MONEY, 30)},

    };

    public static Gift getGift(RowValue rowValue) {
        return GIFT_TABLES[rowValue.icon][rowValue.number];
    }

    public static byte[] getIconsInRow(byte[] table, byte[] pos) {
        if (pos.length != 5) {
            throw new IllegalArgumentException();
        }
        byte[] row = new byte[5];
        for (int i = 0; i < pos.length; i++) {
            row[i] = table[pos[i]];
        }
        return row;
    }

    public static void changeColumnWild(byte[] table, byte[] columns) {
        for (int i = 0; i < columns.length; i++) {
//            if (table[columns[i]] >= WILD && table[columns[i]] != JACKPOT) {
            table[columns[i]] = WILD;
//            }
        }
    }

    public static void changeColumnNotWild(byte[] table, byte[] columns) {
        for (int i = 0; i < columns.length; i++) {
            if (table[columns[i]] < WILD || table[columns[i]] == JACKPOT) {
                table[columns[i]] = (byte) GameUtil.randomBetween(4, 9);
            }
        }
    }

    public static byte[] validateTable(byte[] table) {
        for (int i = 0; i < COLUMNS.length; i++) {
            if (i == 0 || i == 4) continue;
            byte[] columns = COLUMNS[i];
            for (int j = 0; j < columns.length; j++) {
                if (table[columns[j]] == WILD) {
                    changeColumnNotWild(table, columns);
                }
            }
        }
        return table;
    }

    public static RowValue getRowValue(byte[] row) {
        byte indexMax = row[0];
        byte valueMax = 0;
        for (byte i = 0; i < row.length; i++) {
            if (indexMax < WILD || indexMax == JACKPOT) {
                if (row[i] == indexMax) {
                    valueMax += 1;
                } else {
                    break;
                }
            } else {
                if (row[i] == indexMax || row[i] == WILD) {
                    valueMax += 1;
                } else {
                    break;
                }
            }
        }
        return new RowValue(indexMax, valueMax);
    }



    public static Slot11IconWildLienTucTableInfo rollLose0(int[] rowIndex, long betLevel, long moneyEatJackpot, List<Integer> boxValues) {
        Slot11IconWildLienTucTableInfo slot11IconWildLienTucTableInfo = new
                Slot11IconWildLienTucTableInfo(GameConfig.getInstance().slot11IconWildLienTucRollLoseConfig.getTableRollLose(),
                betLevel, moneyEatJackpot, boxValues);
        slot11IconWildLienTucTableInfo.calculate(rowIndex);
        return slot11IconWildLienTucTableInfo;
//        while (true) {
//            Slot11IconWildLienTucTableInfo slot11IconWildLienTucTableInfo = new Slot11IconWildLienTucTableInfo(betLevel, moneyEatJackpot, boxValues);
//            slot11IconWildLienTucTableInfo.calculate(rowIndex);
//            if (slot11IconWildLienTucTableInfo.lineWin.size() == 0) {
//                return slot11IconWildLienTucTableInfo;
//            }
//        }
    }

    public static Slot11IconWildLienTucTableInfo rollLose20(int[] rowIndex, long betLevel, long moneyEatJackpot, List<Integer> boxValues) {
        while (true) {
            Slot11IconWildLienTucTableInfo slot11IconWildLienTucTableInfo = new Slot11IconWildLienTucTableInfo(betLevel, moneyEatJackpot, boxValues);
            slot11IconWildLienTucTableInfo.calculate(rowIndex);
            if (!slot11IconWildLienTucTableInfo.jackpot && !(slot11IconWildLienTucTableInfo.freeSpin > 0) && !(slot11IconWildLienTucTableInfo.miniGame > 0)
                    && !(slot11IconWildLienTucTableInfo.money > rowIndex.length * 10)) {
                return slot11IconWildLienTucTableInfo;
            }
        }
    }

//    public static Slot11IconWildLienTucTableInfo getSlot11IconLienTucTableInfoSpecial(byte type, int[] rowIndex){
//        Slot11IconWildLienTucTableInfo slot11IconWildLienTucTableInfo = new Slot11IconWildLienTucTableInfo(type);
//        slot11IconWildLienTucTableInfo.calculate(rowIndex);
//        return slot11IconWildLienTucTableInfo;
//    }

    public static long getMoneyJackPot(long jackPotMoney, boolean isX2, long initPotMoney){
        long moneyEatJackpot = jackPotMoney;
        if (isX2) {
            moneyEatJackpot = jackPotMoney * GameConfig.getInstance().slot11IconWildLienTucConfig.MULTI_JACKPOT;
        }
        return moneyEatJackpot;
    }

    public static Slot11IconWildLienTucTableInfo getSlot11IconLienTucBigWinTableInfo(int[] rowIndex, long betLevel, long fund, long fundJackpot,
                                                                               long fundMinigame, boolean isX2, long jackPotMoney, long initPotMoney,
                                                                               List<Integer> boxValues, boolean isSpinFree, long maxiumWin) {
        long moneyEatJackpot = getMoneyJackPot(jackPotMoney, isX2, initPotMoney);

        int x = GameUtil.randomMax(100);

        if(x<50){
            return rollLose0(rowIndex, betLevel, moneyEatJackpot, boxValues);
        }

        while (true){
            Slot11IconWildLienTucTableInfo slot11IconWildLienTucTableInfo = new Slot11IconWildLienTucTableInfo(betLevel, moneyEatJackpot, boxValues);
            slot11IconWildLienTucTableInfo.calculate(rowIndex);
            if (slot11IconWildLienTucTableInfo.jackpot) {
                continue;
            }
            if (slot11IconWildLienTucTableInfo.freeSpin > 0) {
                continue;
            }
            return slot11IconWildLienTucTableInfo;
        }
    }

    public static Slot11IconWildLienTucTableInfo getSlot11IconLienTucTableInfo(int[] rowIndex, long betLevel, long fund, long fundJackpot,
                                                           long fundMinigame, boolean isX2, long jackPotMoney, long initPotMoney,
                                                                               List<Integer> boxValues, boolean isSpinFree, long maxiumWin) {

        long moneyEatJackpot = getMoneyJackPot(jackPotMoney, isX2, initPotMoney);

        Slot11IconWildLienTucTableInfo slot11IconWildLienTucTableInfo = new Slot11IconWildLienTucTableInfo(betLevel, moneyEatJackpot, boxValues);
        slot11IconWildLienTucTableInfo.calculate(rowIndex);
        long totalWin = 0;
        if (slot11IconWildLienTucTableInfo.jackpot) {
            if(isSpinFree){
                return rollLose0(rowIndex, betLevel, moneyEatJackpot, boxValues);
            }
            if (rowIndex.length < ROWS.length) { // roll khong du hang thi khong duoc an jackpot
                return rollLose0(rowIndex, betLevel, moneyEatJackpot, boxValues);
            }
            if (slot11IconWildLienTucTableInfo.miniGame > 0) { // khong cho an minigame
                return rollLose0(rowIndex, betLevel, moneyEatJackpot, boxValues);
            }
            if (slot11IconWildLienTucTableInfo.freeSpin > 0) { // khong cho an freeSpin
                return rollLose0(rowIndex, betLevel, moneyEatJackpot, boxValues);
            }
            if(isX2){
//                long moneyMinusFundJackpot = jackPotMoney * GameConfig.getInstance().slot11IconWildLienTucConfig.MULTI_JACKPOT - (jackPotMoney - initPotMoney);
//                if(fundJackpot < moneyMinusFundJackpot)
                if(fundJackpot < jackPotMoney * GameConfig.getInstance().slot11IconWildLienTucConfig.MULTI_JACKPOT)
                    return rollLose0(rowIndex, betLevel, moneyEatJackpot, boxValues);
            }else{
//                if(fundJackpot < initPotMoney) return rollLose0(rowIndex, betLevel, moneyEatJackpot, boxValues);
                if(fundJackpot < jackPotMoney) return rollLose0(rowIndex, betLevel, moneyEatJackpot, boxValues);
            }
            totalWin += moneyEatJackpot;
        }
        if (slot11IconWildLienTucTableInfo.miniGame > 0) {
            if(isSpinFree){
                return rollLose0(rowIndex, betLevel, moneyEatJackpot, boxValues);
            }
            if (slot11IconWildLienTucTableInfo.miniGame > 5) {// khong cho roll qua 4 lan minigame
                return rollLose0(rowIndex, betLevel, moneyEatJackpot, boxValues);
            }
            if (slot11IconWildLienTucTableInfo.freeSpin > 0) {// khong cho an free spin
                return rollLose0(rowIndex, betLevel, moneyEatJackpot, boxValues);
            }
            if (rowIndex.length < ROWS.length) { // roll khong du hang thi khong duoc an miniGame
                return rollLose0(rowIndex, betLevel, moneyEatJackpot, boxValues);
            }
            if (slot11IconWildLienTucTableInfo.miniGameSlotResponse == null) {
                return rollLose0(rowIndex, betLevel, moneyEatJackpot, boxValues);
            }
            if (slot11IconWildLienTucTableInfo.miniGameSlotResponse.getTotalPrize() > fundMinigame) {
                return rollLose0(rowIndex, betLevel, moneyEatJackpot, boxValues);
            }
            totalWin += slot11IconWildLienTucTableInfo.miniGameSlotResponse.getTotalPrize();
        }

        if (slot11IconWildLienTucTableInfo.freeSpin > 0) {
            if(isSpinFree){
                return rollLose0(rowIndex, betLevel, moneyEatJackpot, boxValues);
            }
            if (slot11IconWildLienTucTableInfo.freeSpin > 4) { // khong cho roll qua 4 lan free spin
                return rollLose0(rowIndex, betLevel, moneyEatJackpot, boxValues);
            }
            if (rowIndex.length < ROWS.length) { // roll khong du hang thi khong duoc an freeSpin
                return rollLose0(rowIndex, betLevel, moneyEatJackpot, boxValues);
            }
            if (slot11IconWildLienTucTableInfo.freeSpin * ROWS.length * betLevel > fund) {
                return rollLose0(rowIndex, betLevel, moneyEatJackpot, boxValues);
            }
        }

        if (rowIndex.length < ROWS.length) { // roll khong du hang
            if (slot11IconWildLienTucTableInfo.money > rowIndex.length * 2) { // an qua 2 lan so tien cuoc thi thua
                return rollLose0(rowIndex, betLevel, moneyEatJackpot, boxValues);
            }
        }

        if (slot11IconWildLienTucTableInfo.money * betLevel > fund) { // qua quy thi cho thua
            return rollLose0(rowIndex, betLevel, moneyEatJackpot, boxValues);
        }

        totalWin += slot11IconWildLienTucTableInfo.money* betLevel;
        if(totalWin > maxiumWin){
            return rollLose0(rowIndex, betLevel, moneyEatJackpot, boxValues);
        }
        return slot11IconWildLienTucTableInfo;
    }

    public static void main(String[] args) {
        GameConfig.getInstance().init();
        List<Integer> boxValues = new ArrayList<Integer>();
        boxValues.add(10);
        boxValues.add(10);
        boxValues.add(10);
        boxValues.add(15);
        boxValues.add(20);
        int[] rowIndex = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
                11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
                21, 22, 23, 24, 25};
        Slot11IconWildLienTucTableInfo slot11IconWildLienTucTableInfo =
                rollLose0(rowIndex,100,50000,boxValues);

        int number = 10000;
//        byte[][] listRollLose0 = new byte[number][];
//        for(int i =0;i<number;i++){
//            listRollLose0[i] = rollLose0(rowIndex,100,50000,boxValues).table;
//        }
//
//        Slot11IconWildLienTucRollLoseConfig slot11IconWildLienTucRollLoseConfig = new Slot11IconWildLienTucRollLoseConfig();
//        slot11IconWildLienTucRollLoseConfig.rollLose0 = listRollLose0;
//        GameConfig.getInstance().setFileConfig("Slot11IconWildLienTucRollLoseConfig.json",
//                GameConfig.gson.toJson(slot11IconWildLienTucRollLoseConfig.rollLose0));
////        byte[] table = {7, 6, 3, 2, 3, 3, 4, 4, 6, 4, 1, 3, 5, 5, 7};
////        Slot11IconWildLienTucTableInfo slot11IconWildLienTucTableInfo = new Slot11IconWildLienTucTableInfo(100, 500000, boxValues);
////        slot11IconWildLienTucTableInfo.table = table;
////        slot11IconWildLienTucTableInfo.tableCalculate = table.clone();
////        slot11IconWildLienTucTableInfo.calculate(rowIndex);
////        slot11IconWildLienTucTableInfo.printTable();
////        System.out.println(slot11IconWildLienTucTableInfo.lineWinToString());
////        System.out.println(slot11IconWildLienTucTableInfo.money);
////        System.out.println(slot11IconWildLienTucTableInfo.moneyWinToString());
////        System.out.println("Test");
//        for(int i =0;i<100;i++){
//            System.out.println("index " + i);
//            Slot11IconWildLienTucTableInfo slot11IconWildLienTucTableInfo = new Slot11IconWildLienTucTableInfo(100, 500000, boxValues);
//            slot11IconWildLienTucTableInfo.calculate(rowIndex);
//            slot11IconWildLienTucTableInfo.printTable();
//            System.out.println();
//            System.out.println(slot11IconWildLienTucTableInfo.money);
//            System.out.println(slot11IconWildLienTucTableInfo.lineWinToString());
//            System.out.println(slot11IconWildLienTucTableInfo.moneyWinToString());
//        }
    }

}
