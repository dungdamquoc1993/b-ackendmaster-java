package game;

import game.GameConfig.GameConfig;
import game.modules.TaiXiu.TaiXiuUtil;

public class TestMiniGame {

    public static void main(String[] args) {
        long moneyBetXiu = 1000000;
        long moneyBetTai = 2000000;
        long fundTaiXiu = -50000;
        long tax = 2;
        short[] result = TaiXiuUtil.genarateRandomResult();
        if (TaiXiuUtil.isXiu(result)) {
            if (moneyBetXiu > moneyBetTai) {
                long moneyMinusFund = (long) (moneyBetXiu * (100 - tax) / 100) - moneyBetTai;
                if (fundTaiXiu - moneyMinusFund < 0) {
                    result = TaiXiuUtil.genarateResult(true);
                }
            }
        } else {
            if (moneyBetTai > moneyBetXiu) {
                long moneyMinusFund = (long) (moneyBetTai * (100 - tax) / 100) - moneyBetXiu;
                if (fundTaiXiu - moneyMinusFund < 0) {
                    result = TaiXiuUtil.genarateResult(false);
                }
            }
        }

        if (TaiXiuUtil.isXiu(result)) {
            long moneyMinusFund = (long) (moneyBetXiu * (100 - tax) / 100) - moneyBetTai;
            fundTaiXiu += moneyMinusFund;
        } else {
            long moneyMinusFund = (long) (moneyBetTai * (100 - tax) / 100) - moneyBetXiu;
            fundTaiXiu += moneyMinusFund;
        }
        System.out.println("test");

    }
}
