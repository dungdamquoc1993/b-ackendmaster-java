package game.modules.XocDia.model;

import game.modules.XocDia.XocDiaConstant;

public class DiceResult {

    public byte[] getResult(long[] listUserBet){
        long fund = XocDiaFundModel.getInstance().getFund();
        byte[] toReturn = new byte[4];
        for (int i = 0; i < toReturn.length; i++) {
            toReturn[i] = Math.random() < 0.5 ? (byte) 0 : (byte) 1;
        }
        long calculateMoney = this.getAllMoneyBetWithData(listUserBet, toReturn);


        return toReturn;
    }

    public long getAllMoneyBetWithData(long[] listUserBet, byte[] result){
        byte listTrang = 0;
        for (int i = 0; i < result.length; i++) {
            if (result[i] == 0) {
                listTrang++;
            }
        }
        long moneyWin = 0;
        if (listTrang % 2 == 0) {
            moneyWin += listUserBet[XocDiaConstant.BET_CHAN] * 2;     // chan
        } else {
            moneyWin += listUserBet[XocDiaConstant.BET_LE] * 2;     // le
        }
        if (listTrang == 0) {
            moneyWin += listUserBet[XocDiaConstant.BET_0_4] * 16;     // 4 den
        }

        if (listTrang == 4) {
            moneyWin += listUserBet[XocDiaConstant.BET_4_0] * 16;      // 4 trang
        }

        if (listTrang == 1) {
            moneyWin += listUserBet[XocDiaConstant.BET_1_3] * 4;          // 3 den 1 trang
        }

        if (listTrang == 3) {
            moneyWin += listUserBet[XocDiaConstant.BET_3_1] * 4;          // 3 trang 1 den
        }


        return moneyWin;
    }
}
