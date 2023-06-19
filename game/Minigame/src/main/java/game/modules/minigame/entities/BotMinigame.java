package game.modules.minigame.entities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.SplittableRandom;

import com.vinplay.dal.service.BotService;
import com.vinplay.dal.service.impl.BotServiceImpl;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.config.VBeePath;
import com.vinplay.vbee.common.utils.DateTimeUtils;

import bitzero.util.common.business.Debug;
import game.utils.ConfigGame;

public class BotMinigame {
    private static List<String> bots = new ArrayList<String>();
    private static List<String> botsVipDaily = new ArrayList<String>();
    private static List<String> botsVip = new ArrayList<String>();
    private static UserService userService = new UserServiceImpl();
    private static BotService botService = new BotServiceImpl();
//    private static List<Integer> betValueDefault = Arrays.asList(5555, 6666, 7777, 8888, 9999, 6789, 11111, 22222, 33333, 44444, 55555);
    private static List<Integer> betValueDefault = Arrays.asList(
            1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 15000);
    private static long[] soVinBan = new long[]{30000000L, 85000000L, 40000000L, 100000000L};
    private static long updateTime = System.currentTimeMillis();

    public static void loadData() {
        String botName;
        BotServiceImpl service = new BotServiceImpl();
        BufferedReader br22;
        try {
            br22 = new BufferedReader(new FileReader(VBeePath.basePath+"config/bots.txt"));
            while ((botName = br22.readLine()) != null) {
                try {
                    service.login(botName);
                    bots.add(botName);
                }
                catch (NoSuchAlgorithmException | SQLException e) {
                    Debug.trace(new Object[]{"Load bot " + botName + " error: ", e});
                }
            }
            br22.close();
        }
        catch (FileNotFoundException e) {
        }
        catch (IOException e) {
            // empty catch block
        }
        try {
            br22 = new BufferedReader(new FileReader(VBeePath.basePath+"config/bots_vip.txt"));
            service = new BotServiceImpl();
            while ((botName = br22.readLine()) != null) {
                try {
                    service.login(botName);
                    botsVip.add(botName);
                }
                catch (NoSuchAlgorithmException | SQLException e) {
                    Debug.trace(new Object[]{"Load vip bot " + botName + " error: ", e});
                }
            }
            br22.close();
        }
        catch (FileNotFoundException e) {
        }
        catch (IOException e) {
            // empty catch block
        }
        BotMinigame.loadBotsVip();
        Debug.trace(("TOTAL BOTS: " + bots.size()));
    }

    public static void loadBotsVip() {
        botsVipDaily.clear();
        int maxBotVip = ConfigGame.getIntValue("tx_vip_max_vin");
        int numBotVip = maxBotVip + 10;
        SplittableRandom rd = new SplittableRandom();
        
        if (numBotVip >= botsVip.size()) {
            Debug.trace("Khong the tao bot vip hang ngay");
            return;
        }
        int i = 0;
        while (i < numBotVip) {
            int n = rd.nextInt(botsVip.size());
            String bot = botsVip.get(n);
            if (bot == null) continue;
            boolean exist = false;
            for (String str : botsVipDaily) {
                if (!str.equals(bot)) continue;
                exist = true;
                break;
            }
            if (exist) continue;
            botsVipDaily.add(bot);
            ++i;
        }
    }

    public static String getRandomBot(String moneyType) {
        SplittableRandom rd = new SplittableRandom();
        int index = rd.nextInt(bots.size());
        String nickname = bots.get(index);
        BotMinigame.pushMoneyToBot(nickname, moneyType);
        return nickname;
    }

    private static void pushMoneyToBot(String nickname, String moneyType) {
        long currentMoney = userService.getCurrentMoneyUserCache(nickname, moneyType);
        if (currentMoney < 10000000L) {
            botService.addMoney(nickname, 100000000L, moneyType, "Chuyen tien cho bot minigame");
        } else {
            BotMinigame.banVin(nickname, moneyType, currentMoney);
        }
    }

    private static void banVin(String nickname, String moneyType, long currentMoney) {
        if (currentMoney >= 90000000L) {
            SplittableRandom rd = new SplittableRandom();
            int index = rd.nextInt(soVinBan.length);
            long tienBan = soVinBan[index];
            botService.addMoney(nickname, -tienBan, moneyType, "Chuyen tien");
        }
    }

    private static void pushMoneyToBotVip(String nickname, String moneyType, long moneyPushed) {
        long currentMoney = userService.getCurrentMoneyUserCache(nickname, moneyType);
        if (currentMoney < moneyPushed) {
            botService.addMoney(nickname, moneyPushed, moneyType, "Cong tien cho bot minigame");
        } else {
            BotMinigame.banVin(nickname, moneyType, currentMoney);
        }
    }

    public static List<String> getBots(int amount, String moneyType) {
        List<String> results = new ArrayList<String>();
        List<String> copyBots = new ArrayList<String>(bots);
        SplittableRandom rd = new SplittableRandom();
        for (int i = 0; i < amount; ++i) {
            if(copyBots.size() == 0){
                copyBots = new ArrayList<String>(bots);
            }
            int index = rd.nextInt(copyBots.size());
            String nickname = copyBots.get(index);
            BotMinigame.pushMoneyToBot(nickname, moneyType);
            results.add(copyBots.remove(index));
        }
        return results;
    }

    public static List<String> getBotsJackPot(int amount, String moneyType) {
        List<String> results = new ArrayList<String>();
        List<String> copyBots = new ArrayList<String>(bots);
        SplittableRandom rd = new SplittableRandom();
        for (int i = 0; i < amount; ++i) {
            int index = rd.nextInt(copyBots.size());
            results.add(copyBots.remove(index));
        }
        return results;
    }

    public static List<String> getBotsVip(int amount, String moneyType) {
       List<String> results = new ArrayList<String>();
       List<String> copyBots = new ArrayList<String>(botsVipDaily);
        for (int i = 0; i < amount; ++i) {
            SplittableRandom rd = new SplittableRandom();
            int index = rd.nextInt(copyBots.size());
            String nickname = copyBots.get(index);
            BotMinigame.pushMoneyToBotVip(nickname, moneyType, 600000000L);
            results.add(copyBots.remove(index));
        }
        return results;
    }

    public static List<String> getBotsSuperVip(int amount, String moneyType, long moneyPushed) {
        List<String> results = new ArrayList<String>();
        List<String> copyBots = new ArrayList<String>(botsVipDaily);
        for (int i = 0; i < amount; ++i) {
            SplittableRandom rd = new SplittableRandom();
            int index = rd.nextInt(copyBots.size());
            String nickname = copyBots.get(index);
            BotMinigame.pushMoneyToBotVip(nickname, moneyType, moneyPushed);
            results.add(copyBots.remove(index));
        }
        return results;
    }

    public static List<BotTaiXiu> getVipBotTaiXiu() {
        int betValue;
        if (updateTime < DateTimeUtils.getStartTimeToDayAsLong()) {
            BotMinigame.loadBotsVip();
        }
        updateTime = System.currentTimeMillis();
        ArrayList<BotTaiXiu> results = new ArrayList<BotTaiXiu>();
        ArrayList<Integer> betValues = new ArrayList<Integer>();
        SplittableRandom rd = new SplittableRandom();
        int numBetTai = 0;
        int minBetValue = 0;
        int maxBetValue = 0;
        int minBettingTime = 0;
        int maxBettingTime = 0;
        int minBotsVin = ConfigGame.getIntValue("tx_vip_min_vin");
        int maxBotsVin = ConfigGame.getIntValue("tx_vip_max_vin");
        if (maxBotsVin == 0) {
            return new ArrayList<BotTaiXiu>();
        }
        int totalBot = rd.nextInt(maxBotsVin - minBotsVin) + minBotsVin + 10;
        if (BotMinigame.isNight()) {
            int n = rd.nextInt(2) + 3;
            totalBot /= n;
        }
        int minBot = totalBot * 5 / 10;
        int maxBot = totalBot - minBot + 5;
        numBetTai = rd.nextInt(maxBot - minBot) + minBot;
        minBetValue = 25000000;
        maxBetValue = 70000000;
        int stepBetting = ConfigGame.getIntValue("tx_vip_step_betting_vin");
        for (betValue = minBetValue; betValue < maxBetValue; betValue += stepBetting) {
            betValues.add(betValue);
        }
        try
        {
            minBettingTime = ConfigGame.getIntValue("tx_vip_min_betting_time");
            maxBettingTime = ConfigGame.getIntValue("tx_vip_max_betting_time");
            List<String> botsName = BotMinigame.getBotsVip(totalBot, "vin");
            for (int i = 0; i < totalBot && i < botsName.size(); ++i) {
                String nickname = botsName.get(i);
                int n = rd.nextInt(betValues.size());
                betValue = (Integer)betValues.get(n);
                short bettingTime = (short)BotMinigame.randomBettingTime(minBettingTime, maxBettingTime, 80);
                short betSide = 0;
                if (i < numBetTai) {
                    betSide = 1;
                }
                BotTaiXiu bot = new BotTaiXiu(nickname, bettingTime, betValue, betSide);
                results.add(bot);
            }
        }
        catch (Exception ex)
        {
            Debug.trace("Exception:" + ex.getMessage());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            Debug.trace(sStackTrace);
        }
        return results;
    }

    public static List<BotTaiXiu> getBotTaiXiu(String moneyType) {
        SplittableRandom rd = new SplittableRandom();
        int phanTramVaoSom = 80;
        int[] arr = new int[]{60, 70, 80, 85, 90};
        int index = rd.nextInt(arr.length);
        phanTramVaoSom = arr[index];
        ArrayList<BotTaiXiu> results = new ArrayList<BotTaiXiu>();
        ArrayList<Integer> betValues = new ArrayList<Integer>(betValueDefault);
        int numBetTai = 0;
        int numBetXiu = 0;
        int minBetValue = 0;
        int maxBetValue = 0;
        int minBettingTime = 0;
        int maxBettingTime = 0;
        if (moneyType.equalsIgnoreCase("vin")) {
            int minBotsVin = ConfigGame.getIntValue("tx_min_bot_betting_vin");
            int maxBotsVin = ConfigGame.getIntValue("tx_max_bot_betting_vin");
            if (maxBotsVin == 0 || minBotsVin == 0) {
                return new ArrayList<BotTaiXiu>();
            }
            int totalBot = rd.nextInt(maxBotsVin - minBotsVin) + minBotsVin;
//            totalBot*=5;
            int n = BotMinigame.ratioTXInNight();
            Debug.trace(("Bot n = " + n));
            totalBot = totalBot * 1 * n / 300;
            int minBot = totalBot*4 / 10;
            int maxBot = totalBot - minBot;
            numBetTai = rd.nextInt(maxBot - minBot) + minBot;
            numBetXiu = totalBot - numBetTai;
            Debug.trace("NUM BET TAI= " + numBetTai + ", NUM BET XIU= " + numBetXiu);
            minBetValue = ConfigGame.getIntValue("tx_min_bet_value_vin");;
            maxBetValue = ConfigGame.getIntValue("tx_max_bet_value_vin");;
            int stepBetting = ConfigGame.getIntValue("tx_step_betting_vin");
            for (int betValue = minBetValue; betValue < maxBetValue; betValue += stepBetting) {
                betValues.add(betValue);
            }
        } else {
            int minBotsXu = ConfigGame.getIntValue("tx_min_bot_betting_xu");
            int maxBotsXu = ConfigGame.getIntValue("tx_max_bot_betting_xu");
            if (maxBotsXu == 0) {
                return new ArrayList<BotTaiXiu>();
            }
            int totalBots = rd.nextInt(maxBotsXu - minBotsXu) + minBotsXu;
            numBetTai = rd.nextInt(totalBots);
            numBetXiu = totalBots - numBetTai;
            minBetValue = ConfigGame.getIntValue("tx_min_bet_value_xu");
            maxBetValue = ConfigGame.getIntValue("tx_max_bet_value_xu");
            int stepBetting = ConfigGame.getIntValue("tx_step_betting_xu");
            for (int betValue = minBetValue; betValue < maxBetValue; betValue += stepBetting) {
                betValues.add(betValue);
            }
        }
        try
        {
            minBettingTime = ConfigGame.getIntValue("tx_min_betting_time");
            maxBettingTime = ConfigGame.getIntValue("tx_max_betting_time");
            int totalBot = numBetTai + numBetXiu;
            List<String> botsName = BotMinigame.getBots(totalBot, moneyType);
            for (int i = 0; i < totalBot && i < botsName.size(); ++i) {
                String nickname = botsName.get(i);
                int n = rd.nextInt(betValues.size());
                long betValue = betValues.get(n).intValue();
                short bettingTime = (short)BotMinigame.randomBettingTime(minBettingTime, maxBettingTime, phanTramVaoSom);
                short betSide = 0;
                if (i < numBetTai) {
                    betSide = 1;
                }
                BotTaiXiu bot = new BotTaiXiu(nickname, bettingTime, betValue, betSide);
                results.add(bot);
            }
            Debug.trace(("NUMBER BOTS:" + results.size()));
        }
        catch (Exception ex)
        {
            Debug.trace("Exception:" + ex.getMessage());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            Debug.trace(sStackTrace);
        }
        return results;
    }

    private static int randomBettingTime(int minTime, int maxTime, int phanTramVaoSom) {
        SplittableRandom rd = new SplittableRandom();
        int n = rd.nextInt(100);
        if (n > phanTramVaoSom) {
            int minTime5s = maxTime - 5;
            return rd.nextInt(maxTime - minTime5s) + minTime5s;
        }
        return rd.nextInt(maxTime - minTime) + minTime;
    }

    public static List<BotBauCua> getBotBauCua(int roomId) {
        String moneyType = "xu";
        if (roomId < 3) {
            moneyType = "vin";
        }
        long baseBetValue = BotMinigame.getBaseBettingBC(roomId);
        ArrayList<BotBauCua> results = new ArrayList<BotBauCua>();
        SplittableRandom rd = new SplittableRandom();
        int minBot = 10;//ConfigGame.getIntValue("bc_min_bot_" + roomId);
        int maxBot = 80;//ConfigGame.getIntValue("bc_max_bot_" + roomId);
//        if (maxBot <= minBot || maxBot == 0) {
//            return new ArrayList<BotBauCua>();
//        }
        int minRatio = 5;//ConfigGame.getIntValue("bc_min_ratio_" + roomId);
        int maxRatio = 1000;//ConfigGame.getIntValue("bc_max_ratio_" + roomId);
        int minBettingTime = 10;// ConfigGame.getIntValue("bc_min_betting_time");
        int maxBettingTime = 58; //ConfigGame.getIntValue("bc_max_betting_time");
        int numBots = rd.nextInt(maxBot - minBot) + minBot;
        int maxBetSide = 5;//ConfigGame.getIntValue("bc_max_bet_side");
        List<String> botsName = BotMinigame.getBots(numBots, moneyType);
        for (int i = 0; i < numBots && i < botsName.size(); ++i) {
            String nickname = botsName.get(i);
            short bettingTime = (short)BotMinigame.randomBettingTime(minBettingTime, maxBettingTime, 70);
            long[] betArr = new long[6];
            int j = maxBetSide;
            while (j > 0) {
                long betValue;
                short betSide = (short)rd.nextInt(6);
                if (betArr[betSide] != 0L) continue;
                betArr[betSide] = betValue = baseBetValue * (long)(rd.nextInt(maxRatio - minRatio) + minRatio);
                --j;
            }
            StringBuilder builder = new StringBuilder();
            for (j = 0; j < 6; ++j) {
                builder.append(",");
                builder.append(betArr[j]);
            }
            if (builder.length() > 0) {
                builder.deleteCharAt(0);
            }
            BotBauCua bot = new BotBauCua(nickname, bettingTime, builder.toString());
            results.add(bot);
        }
        return results;
    }

    private static long getBaseBettingBC(int roomId) {
        switch (roomId) {
            case 0: {
                return 1000L;
            }
            case 1: {
                return 10000L;
            }
            case 2: {
                return 100000L;
            }
            case 3: {
                return 10000L;
            }
            case 4: {
                return 100000L;
            }
            case 5: {
                return 1000000L;
            }
        }
        return 1000L;
    }

    public static List<String> getBotChat() {
        int number = 0;
        SplittableRandom rd = new SplittableRandom();
        if (BotMinigame.isNight()) {
            int n = rd.nextInt(5);
            if (n == 0) {
                number = 1;
            }
        } else {
            number = rd.nextInt(3);
        }
        List<String> results = new ArrayList<String>();
        if (number > 0) {
            for (int i = 0; i < number; ++i) {
                int n = rd.nextInt(10);
                if (n == 0) {
                    n = rd.nextInt(botsVip.size());
                    results.add(botsVip.get(n));
                    continue;
                }
                n = rd.nextInt(bots.size());
                results.add(bots.get(n));
            }
        }
        return results;
    }

    public static boolean isNight() {
        Calendar cal = Calendar.getInstance();
        int hourOfDay = cal.get(11);
        return 2 <= hourOfDay && hourOfDay <= 8;
    }

    public static int ratioTXInNight() {
        Calendar cal = Calendar.getInstance();
        int hourOfDay = cal.get(11);
        SplittableRandom rd = new SplittableRandom();
        if (2 <= hourOfDay && hourOfDay <= 8) {
            switch (hourOfDay) {
                case 2:
                case 8: {
                    return rd.nextInt(20) + 80;
                }
                case 3:
                case 7: {
                    return rd.nextInt(30) + 50;
                }
                case 4:
                case 5:
                case 6: {
                    return rd.nextInt(20) + 30;
                }
            }
        }
        return 100;
    }

    public static void main(String[] args) {
        BotMinigame.isNight();
    }
}

