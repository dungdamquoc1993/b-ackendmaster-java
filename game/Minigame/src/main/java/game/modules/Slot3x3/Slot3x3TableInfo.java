package game.modules.Slot3x3;

import game.GameConfig.GameConfig;
import game.utils.GameUtil;

import java.util.ArrayList;

public class Slot3x3TableInfo {

    public byte[][] table;
    public long money = 0;
    public boolean isJackPot;
    public ArrayList<Integer> lineWin = new ArrayList<>();
    public ArrayList<Long> moneyWin = new ArrayList<>();

    public long betLevel = 0;
    public long moneyEatPot = 0;

    public Slot3x3TableInfo(byte[][] table, long betLevel, long moneyEatPot) {
        this.table = table;//
        this.betLevel = betLevel;
        this.moneyEatPot = moneyEatPot;
    }

    public Slot3x3TableInfo(byte giftType, long betLevel, long moneyEatPot) {
        this.betLevel = betLevel;
        this.moneyEatPot = moneyEatPot;
        if (giftType == 0) { // jackpot
            this.table = GameConfig.getInstance().slot3x3GameConfig.getTableValue();
            byte[] rows = Slot3x3Util.ROWS[GameUtil.randomMax(Slot3x3Util.ROWS.length)];
            for (int i = 0; i < this.table.length; i++) {
                this.table[i][rows[i]] = 0;
            }
        }
    }

    public void calculateRowIndex(int[] rowIndex) {
        for (int i = 0; i < rowIndex.length; i++) {
            byte[] row = Slot3x3Util.ROWS[rowIndex[i]-1];
            long value = this.getMoneyWithRow(row);
            if (value < 0) {
                this.isJackPot = true;
                this.lineWin.add(rowIndex[i]);
                this.moneyWin.add(this.moneyEatPot);
            }
            if (value > 0) {
                this.money += value;
                this.lineWin.add(rowIndex[i]);
                this.moneyWin.add(value * this.betLevel / 10);
            }
        }
    }

    public long getMoneyWithRow(byte[] row) {
        byte[] listCheckPoint = new byte[Slot3x3Util.MAX_ICON];
        for (int j = 0; j < row.length; j++) {
            if (this.table[j][row[j]] == Slot3x3Util.WILD) {
                for (int k = 0; k < listCheckPoint.length; k++) {
                    listCheckPoint[k] += 1;
                }
            } else {
                listCheckPoint[this.table[j][row[j]]] += 1;
            }
        }
        for (int j = 0; j < 5; j++) {
            if (listCheckPoint[j] == 3) {
                return GameConfig.getInstance().slot3x3GameConfig.winRate[j];
            }
        }
        if (listCheckPoint[5] == 3) {
            return GameConfig.getInstance().slot3x3GameConfig.winRate[6];
        }
        long value = 0;
        if (listCheckPoint[4] == 2) {
            value += GameConfig.getInstance().slot3x3GameConfig.winRate[5];
        }
        if (listCheckPoint[5] == 2) {
            value += GameConfig.getInstance().slot3x3GameConfig.winRate[7];
        }
        //Debug.trace("value", value);
        return value;
    }

    public String lineWinToString() {
        StringBuilder s = new StringBuilder();
        for (Integer integer : this.lineWin) {
            int line = integer;
            if (s.length() == 0) {
                s.append(line);
            } else {
                s.append(",").append(line);
            }
        }
        return s.toString();
    }

    public String moneyWinToString() {
        StringBuilder s = new StringBuilder();
        for (Long value : this.moneyWin) {
            if (s.length() == 0) {
                s.append(value);
            } else {
                s.append(",").append(value);
            }
        }
        return s.toString();
    }

//    public String matrixToString(){
//        StringBuilder s = new StringBuilder();
//        for(byte[] row: this.table){
//            for(byte value : row){
//                if (s.length() == 0) {
//                    s.append(value);
//                } else {
//                    s.append(",").append(value);
//                }
//            }
//        }
//
//        return s.toString();
//    }

    public String matrixToString(){
        StringBuilder s = new StringBuilder();
        for(int j = 0 ; j< 3;j++){ // collumns
            for(int i = 0;i<3;i++){ // rows
                byte value = this.table[i][j];
                if (s.length() == 0) {
                    s.append(value );
                } else {
                    s.append(",").append(value);
                }
            }
        }
//        for(byte[] row: this.table){
//            for(byte value : row){
//                if (s.length() == 0) {
//                    s.append(value );
//                } else {
//                    s.append(",").append(value);
//                }
//            }
//        }

        return s.toString();
    }
}
