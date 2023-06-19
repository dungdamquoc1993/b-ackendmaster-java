package game.GameConfig.SlotConfig;

import game.modules.GameUtil;

public class Slot7IconConfig {

    public byte[] rateIcon = {3, 4, 2, 8, 6, 5, 5};
    public int totalRate = 33;

    public int RATE_TO_JACKPOT = 1;
    public int RATE_TO_FUND_JACKPOT = 1;
    public final int RATE_TO_FUND_MINIGAME = 5;
    public final int FEE = 3;
    public final int MULTI_JACKPOT = 2;

    public byte[] generateRandomTable() {
        byte[] table = new byte[15];
        for (int i = 0; i < 15; i++) {
            table[i] = getValueOfIcon();
            //System.out.println(table[i]);
        }

        return table;
    }

    public byte getValueOfIcon() {
        byte random = (byte) GameUtil.randomMax(this.totalRate);
        for (byte i = 0; i < this.rateIcon.length; i++) {
            if (random < this.rateIcon[i]) {
                return i;
            }
            random -= this.rateIcon[i];
        }
        return (byte) (rateIcon.length - 1);
    }
}
