/*
 * Decompiled with CFR 0.150.
 */
package game.modules.TaiXiu;

import com.vinplay.dal.service.CacheService;
import com.vinplay.dal.service.impl.CacheServiceImpl;
import game.utils.GameUtil;

import java.util.SplittableRandom;

public class TaiXiuUtil {
    private static final SplittableRandom rdom = new SplittableRandom();
    private static final int DICE_TAI_VALUE = 11;

    public static boolean isXiu(short[] dices) {
        int value = 0;
        for (int i = 0; i < dices.length; ++i) {
            value += dices[i];
        }
        return value < 11;
    }

    //Khong cho ket qua ve 1 1 1 va 6 6 6
    public static short[] genarateResult(boolean isTai) {
        int value;
        short[] dices = new short[3];
        do {
            value = 0;
            for (int i = 0; i < dices.length; ++i) {
                dices[i] = (short) GameUtil.randomBetween(1, 7);
                value += dices[i];
            }
        } while (value < 11 == isTai);
//        while (((value < 11 && value > 3) == isTai) || ((value > 10 && value < 18) == !isTai));
//        while (value < 11 == isTai);
        int totalDiceTemp = dices[0] + dices[1] + dices[2];
        if (totalDiceTemp == 3 || totalDiceTemp == 18) {
            genarateResult(isTai);
        }
        return dices;
    }

    public static short[] genarateRandomResult() {
        CacheService cacheService = new CacheServiceImpl();

        try {
            int currentReference = cacheService.getValueInt("Tai_xiu_current_reference") - 1;
            int force = Integer.valueOf(cacheService.getValueStr("force_result_" + currentReference));
            cacheService.removeKey("force_result_" + currentReference);
            if (force == 1) {
                int totalNum = rdom.nextInt(8) + 11;
                while (totalNum == 18) {
                    totalNum = rdom.nextInt(8) + 11;
                }
                int t1 = totalNum / 3;
                int t2 = rdom.nextInt(6 - (totalNum - t1 - 6) + 1) + (totalNum - t1 - 6);
                int t3 = totalNum - t1 - t2;
                return new short[]{(short) t1, (short) t2, (short) t3};
            } else {
                int totalNum = rdom.nextInt(4) + 6;
                while (totalNum == 3) {
                    totalNum = rdom.nextInt(4) + 6;
                }
                int t1 = totalNum / 3;
                int t2 = 0;
                t2 = totalNum - t1 <= 6 ? rdom.nextInt(totalNum - t1 - 1 - 1 + 1) + 1 : rdom.nextInt(5) + 1;
                int t3 = totalNum - t1 - t2;
                return new short[]{(short) t1, (short) t2, (short) t3};
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (rdom.nextBoolean()) {
                int totalNum = rdom.nextInt(4) + 6;
                int t1 = totalNum / 3;
                int t2 = 0;
                t2 = totalNum - t1 <= 6 ? rdom.nextInt(totalNum - t1 - 1 - 1 + 1) + 1 : rdom.nextInt(5) + 1;
                int t3 = totalNum - t1 - t2;
                return new short[]{(short) t1, (short) t2, (short) t3};
            }
            int totalNum = rdom.nextInt(8) + 11;
            int t1 = totalNum / 3;
            int t2 = rdom.nextInt(6 - (totalNum - t1 - 6) + 1) + (totalNum - t1 - 6);
            int t3 = totalNum - t1 - t2;
            return new short[]{(short) t1, (short) t2, (short) t3};
        }

    }
}

