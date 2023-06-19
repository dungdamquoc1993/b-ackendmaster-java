package game.GameConfig;

import bitzero.util.common.business.Debug;
import game.utils.GameUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class BotBetConfig {
    public static int[] xocDiaNumberBot = {135,90,45,45,45,45,45,90,135,135,180,180,180,180,135,135,135,135,135,180,180,180,180,135};
    public static int[] xocDiaFundBet = {4000000,2500000,2500000,2500000,2500000,2500000,2500000,2500000,4000000,4000000,5000000,5000000,
            5000000,5000000,4000000,4000000,4000000,4000000,4000000,5000000,5000000,5000000,5000000,4000000};
    public static int[] xocDiaFundBetDelta = {4000000,2000000,1000000,1000000,1000000,1000000,1000000,2000000,4000000,4000000,4000000,
            4000000,4000000,4000000,4000000,4000000,4000000,4000000,4000000,4000000,4000000,4000000,4000000,4000000};

    public static byte getHourOfDay() {
        return (byte) Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public static synchronized ArrayList<ArrayList<Long>> getListBetBotXocDia() {
        int hourOfDay = getHourOfDay();
        int[] number = new int[6];
        for (int i = 0; i < number.length; i++) {
            number[i] = xocDiaNumberBot[hourOfDay] / 10;
        }
        //Debug.debug(this.baCayNumberBot[hourOfDay],  this.baCayFundBet[hourOfDay],this.baCayFundBetDelta[hourOfDay] );
        int currentNumber = xocDiaNumberBot[hourOfDay] - number[0] * number.length;
        for (int i = 0; i < currentNumber; i++) {
            int value = GameUtil.randomMax(number.length);
            number[value] += 1;
        }
        int[] goldBet = new int[number.length];
        for (int i = 0; i < goldBet.length; i++) {
            goldBet[i] = xocDiaFundBet[hourOfDay] + GameUtil.randomMax((xocDiaFundBetDelta[hourOfDay] / 10000)) * 10000;
        }
        ArrayList listReturn = new ArrayList();
        for (int i = 0; i < number.length; i++) {
            ArrayList<Long> listBet = getListBet(goldBet[i], number[i]);
            Collections.shuffle(listBet);
            listReturn.add(listBet);
        }
        return listReturn;
    }

    public static ArrayList<Long> getListBet(long money, int number) {
        ArrayList<Long> listBet = new ArrayList<>();
//        Debug.debug("BACAY",money,number);
        money = money / 10;
        long goldTB = money / number;
        if (goldTB > 100000) {
            long[] goldForBetLevel = new long[4];
            goldForBetLevel[3] = (long) (money * 37.5 / 100);
            goldForBetLevel[2] = (long) (money * 37.5 / 100);
            goldForBetLevel[1] = (long) (money * 24.0 / 100);
            goldForBetLevel[0] = money - goldForBetLevel[3] - goldForBetLevel[2] - goldForBetLevel[1];

            long currentGoldBetLevel3 = (goldForBetLevel[3] / 1000000) * 1000000;
            goldForBetLevel[2] += goldForBetLevel[3] - currentGoldBetLevel3;
            goldForBetLevel[3] = currentGoldBetLevel3 / 1000000;

            long currentGoldBetLevel2 = (goldForBetLevel[2] / 100000) * 100000;
            goldForBetLevel[1] += goldForBetLevel[2] - currentGoldBetLevel2;
            goldForBetLevel[2] = currentGoldBetLevel2 / 100000;

            long currentGoldBetLevel1 = (goldForBetLevel[1] / 10000) * 10000;
            goldForBetLevel[0] += goldForBetLevel[1] - currentGoldBetLevel1;
            goldForBetLevel[1] = currentGoldBetLevel1 / 10000;

            goldForBetLevel[0] /= 1000;

            int[] botForBetLevel = new int[4];
            botForBetLevel[3] = number * 5 / 100;
            if (botForBetLevel[3] == 0) botForBetLevel[3] = 1;
            botForBetLevel[2] = number * 25 / 100;
            botForBetLevel[1] = number * 40 / 100;
            botForBetLevel[0] = number - botForBetLevel[3] - botForBetLevel[2] - botForBetLevel[1];

            int[] listBet1K = getList1KByValue(botForBetLevel[0], (int) goldForBetLevel[0]);
            for (int i = 0; i < listBet1K.length; i++) {
                listBet.add((long) listBet1K[i] * 1000 * 10);
            }
            int[] listBet10K = getList10KByValue(botForBetLevel[1], (int) goldForBetLevel[1]);
            for (int i = 0; i < listBet10K.length; i++) {
                listBet.add((long) listBet10K[i] * 10000 * 10);
            }

            int[] listBet100K = getList100KByValue(botForBetLevel[2], (int) goldForBetLevel[2]);
            for (int i = 0; i < listBet100K.length; i++) {
                listBet.add((long) listBet100K[i] * 100000 * 10);
            }

            int[] listBet1000K = getList1000KByValue(botForBetLevel[3], (int) goldForBetLevel[3]);
            for (int i = 0; i < listBet1000K.length; i++) {
                listBet.add((long) listBet1000K[i] * 1000000 * 10);
            }
        } else {
            long[] goldForBetLevel = new long[3];
            goldForBetLevel[2] = (long) (money * 60 / 100);
            goldForBetLevel[1] = (long) (money * 38.5 / 100);
            goldForBetLevel[0] = money - goldForBetLevel[2] - goldForBetLevel[1];
            long currentGoldBetLevel2 = (goldForBetLevel[2] / 100000) * 100000;
            goldForBetLevel[1] += goldForBetLevel[2] - currentGoldBetLevel2;
            goldForBetLevel[2] = currentGoldBetLevel2 / 100000;
            long currentGoldBetLevel1 = (goldForBetLevel[1] / 10000) * 10000;
            goldForBetLevel[0] += goldForBetLevel[1] - currentGoldBetLevel1;
            goldForBetLevel[1] = currentGoldBetLevel1 / 10000;
            goldForBetLevel[0] /= 1000;

            int[] botForBetLevel = new int[3];
            botForBetLevel[2] = number * 30 / 100 + 1;
            botForBetLevel[1] = number * 40 / 100;
            botForBetLevel[0] = number - botForBetLevel[2] - botForBetLevel[1];

            int[] listBet1K = getList1KByValue(botForBetLevel[0], (int) goldForBetLevel[0]);
            for (int i = 0; i < listBet1K.length; i++) {
                listBet.add((long) listBet1K[i] * 1000 * 10);
            }
            int[] listBet10K = getList10KByValue(botForBetLevel[1], (int) goldForBetLevel[1]);
            for (int i = 0; i < listBet10K.length; i++) {
                listBet.add((long) listBet10K[i] * 10000 * 10);
            }

            int[] listBet100K = getList100KByValue(botForBetLevel[2], (int) goldForBetLevel[2]);
            for (int i = 0; i < listBet100K.length; i++) {
                listBet.add((long) listBet100K[i] * 100000 * 10);
            }
        }
//        for(int i =0;i< listBet.size();i++){
//
//        }
        return listBet;
    }

    public static int[] getList1KByValue(int numberList, int value) {
        int[] goldForPlayer = new int[numberList];
        for (int i = 0; i < goldForPlayer.length; i++) {
            goldForPlayer[i] = 1;
            value--;
            if (value <= 0) return goldForPlayer;
        }
        int value10 = value / 18;
        value -= value10 * 9;
        for (int i = 0; i < value10; i++) {
            goldForPlayer[GameUtil.randomMax(goldForPlayer.length)] += 9;
        }
        int value5 = value / 4;
        int valueLe = value % 4;
        for (int i = 0; i < value5; i++) {
            goldForPlayer[GameUtil.randomMax(goldForPlayer.length)] += 4;
        }
        for (int i = 0; i < valueLe; i++) {
            goldForPlayer[GameUtil.randomMax(goldForPlayer.length)] += 1;
        }
        int count = 0;
        for (int i = 0; i < goldForPlayer.length; i++) {
            count += goldForPlayer[i];
        }
        Debug.trace(count);
        return goldForPlayer;
    }

    public static int[] getList10KByValue(int numberList, int value) {
        int[] goldForPlayer = new int[numberList];
        for (int i = 0; i < goldForPlayer.length; i++) {
            goldForPlayer[i] = 1;
            value--;
            if (value <= 0) return goldForPlayer;
        }
        for (int i = 0; i < value; i++) {
            goldForPlayer[GameUtil.randomMax(goldForPlayer.length)] += 1;
        }
        return goldForPlayer;
    }

    public static int[] getList100KByValue(int numberList, int value) {
        int[] goldForPlayer = new int[numberList];
        for (int i = 0; i < goldForPlayer.length; i++) {
            goldForPlayer[i] = 1;
            value--;
            if (value <= 0) return goldForPlayer;
        }
        for (int i = 0; i < value; i++) {
            goldForPlayer[GameUtil.randomMax(goldForPlayer.length)] += 1;
        }
        return goldForPlayer;
    }

    public static int[] getList1000KByValue(int numberList, int value) {
        int[] goldForPlayer = new int[numberList];
        for (int i = 0; i < goldForPlayer.length; i++) {
            goldForPlayer[i] = 1;
            value--;
            if (value <= 0) return goldForPlayer;
        }
        for (int i = 0; i < value; i++) {
            goldForPlayer[GameUtil.randomMax(goldForPlayer.length)] += 1;
        }
        return goldForPlayer;
    }
}
