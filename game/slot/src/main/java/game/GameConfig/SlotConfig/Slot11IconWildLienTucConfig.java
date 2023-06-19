package game.GameConfig.SlotConfig;

import game.modules.GameUtil;

public class Slot11IconWildLienTucConfig {
    public byte[] rateIcon = {1, 3, 5, 4, 7, 7, 3, 3, 3, 2, 2};
    public int totalRate = 40;

    public byte[] rateIconNotWild = {1, 3, 0, 4, 7, 7, 3, 3, 3, 2, 2};
    public int totalRateNotWild = 35;

    public int RATE_TO_JACKPOT = 1;
    public int RATE_TO_FUND_JACKPOT = 1;
    public int RATE_TO_FUND_MINIGAME = 1;
    public int FEE = 3;
    public int MULTI_JACKPOT = 2;

    public byte[] generateRandomTable() {
        byte[] table = new byte[15];
        for (int i = 0; i < 15; i++) {
            int value = i % 5;
            if (value > 0 && value < 4) {
                table[i] = this.getValueOfIcon();
            } else {
                table[i] = this.getValueOfIconNotWild();
            }
        }
        return table;
    }

    public byte[] generateRandomTableNoWild() {
        byte[] table = new byte[15];
        for (int i = 0; i < 15; i++) {
            int value = i % 5;
            if (value > 0 && value < 4) {
                table[i] = this.getValueOfIconNotWild();
            } else {
                table[i] = this.getValueOfIconNotWild();
            }
        }
        return table;
    }

    public byte getValueOfIconNotWild() {
        byte random = (byte) GameUtil.randomMax(this.totalRateNotWild);
        for (byte i = 0; i < this.rateIconNotWild.length; i++) {
            if (random < this.rateIconNotWild[i]) {
                return i;
            }
            random -= this.rateIconNotWild[i];
        }
        return (byte) (this.rateIconNotWild.length - 1);
    }

    public byte getValueOfIcon() {
        byte random = (byte) GameUtil.randomMax(this.totalRate);
        for (byte i = 0; i < this.rateIcon.length; i++) {
            if (random < this.rateIcon[i]) {
                return i;
            }
            random -= this.rateIcon[i];
        }
        return (byte) (this.rateIcon.length - 1);
    }
}
