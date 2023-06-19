package game;

import game.GameConfig.GameConfig;
import game.modules.GameUtil;
import game.modules.slot.entities.slot.MiniGameSlotResponse;
import game.modules.slot.entities.slot.PickStarGift;
import game.modules.slot.entities.slot.PickStarGiftItem;
import game.modules.slot.entities.slot.PickStarGifts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class TestSlotBonus {
    public static List<Integer> boxValues = new ArrayList<Integer>();
    public static int betValue = 10;
    public static void main(String[] args) {
        Date d = new Date(1344855183166L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(d);
        System.out.println("test");
//        GameConfig.getInstance().init();
//        GameConfig.getInstance().slotMultiJackpotConfig.checkIsJackPotAllGame();
//        GameConfig.getInstance().slotMultiJackpotConfig.isMultiJackpot("");
//        int day = GameUtil.getDayNumber();
//        boxValues.add(10);
//        boxValues.add(10);
//        boxValues.add(10);
//        boxValues.add(15);
//        boxValues.add(20);
//        long moneyWin = 0;
//        int loop = 10000000;
//        long maxMoneyWin = 0;
//        long minMoneyWin = Integer.MAX_VALUE;
//        for(int i =0;i<loop;i++){
//            MiniGameSlotResponse miniGameSlotResponse = generatePickStars();
//            moneyWin += miniGameSlotResponse.getTotalPrize();
//            maxMoneyWin = Math.max(miniGameSlotResponse.getTotalPrize(),maxMoneyWin);
//            minMoneyWin = Math.min(miniGameSlotResponse.getTotalPrize(),minMoneyWin);
//        }
//        System.out.println("betLevel     " + betValue);
//        System.out.println("loop     " + loop);
//        System.out.println("rate win with total betlevel   " + moneyWin/loop/betValue);
//        System.out.println("rate win with maxMoneyWin   " + maxMoneyWin/betValue);
//        System.out.println("rate win with minMoneyWin   " + minMoneyWin/betValue);
    }

    public static MiniGameSlotResponse generatePickStars() {
        MiniGameSlotResponse response = new MiniGameSlotResponse();
        int totalMoney = 0;
        ArrayList<PickStarGift> gifts = new ArrayList<PickStarGift>();
        PickStarGifts pickStarGifts = new PickStarGifts();
        String responsePickStars = "";
        int totalKeys = 1;
        block5:
        for (int numPicks = 10; numPicks > 0; --numPicks) {
            PickStarGiftItem gift = pickStarGifts.pickRandomAndRandomGift();
            switch (gift) {
                case GOLD: {
                    totalMoney += 4 * betValue * totalKeys;
                    gifts.add(new PickStarGift(PickStarGiftItem.GOLD, 0));
                    continue block5;
                }
                case KEY: {
                    ++numPicks;
                    ++totalKeys;
                    gifts.add(new PickStarGift(PickStarGiftItem.KEY, 0));
                    continue block5;
                }
                case BOX: {
                    int boxValue = randomBoxValue();
                    totalMoney += boxValue * betValue * totalKeys;
                    gifts.add(new PickStarGift(PickStarGiftItem.BOX, boxValue));
                    break;
                }
            }
        }
        for (PickStarGift pickStarGift : gifts) {
            if (responsePickStars.length() == 0) {
                responsePickStars = pickStarGift.getName();
                continue;
            }
            responsePickStars = String.valueOf(responsePickStars) + "," + pickStarGift.getName();
        }
        response.setTotalPrize(totalMoney);
        response.setPrizes(responsePickStars);
        return response;
    }

    public static int randomBoxValue() {
        Random rd = new Random();
        int n = rd.nextInt(boxValues.size());
        return boxValues.get(n);
    }
}
