package game.modules.Minipoker;

import com.vinplay.cardlib.models.Card;
import com.vinplay.cardlib.models.GroupType;
import com.vinplay.cardlib.utils.CardLibUtils;
import game.modules.minigame.config.MinipokerResultData;
import game.modules.minigame.utils.GenerationMiniPoker;
import game.utils.GameUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DealCardMinipoker {

    public static byte[] rollJackPot() {
        int start = GameUtil.randomBetween(5, 9);
        List<Byte> card = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            card.add((byte) ((start + i) % 13));
        }
        int color = GameUtil.randomMax(4);
        byte[] listCard = new byte[5];
        for (int i = 0; i < listCard.length; i++) {
            listCard[i] = (byte) (card.get(i) * 4 + color);
        }
        return listCard;
    }

    public static List<Card> dealCardJackpotMinipoker() {
        byte[] listCard = rollJackPot();
        List<Card> listReturn = new ArrayList<>();
        for (int i = 0; i < listCard.length; i++) {
            listReturn.add(new Card(listCard[i]));
        }
        return listReturn;
    }

    public static byte[] rollStraighFlush() {
        int start = GameUtil.randomMax(5);
        List<Byte> card = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            card.add((byte) ((start - 1 + 13 + i) % 13));
        }
        int color = GameUtil.randomMax(4);
        byte[] listCard = new byte[5];
        for (int i = 0; i < listCard.length; i++) {
            listCard[i] = (byte) (card.get(i) * 4 + color);
        }

        return listCard;
    }

    public static byte[] rollStraight() {
        int start = GameUtil.randomMax(10);
        List<Byte> card = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            card.add((byte) ((start - 1 + 13 + i) % 13));
        }

        byte[] listCard = new byte[5];
        for (int i = 0; i < listCard.length; i++) {
            listCard[i] = (byte) (card.get(i) * 4 + GameUtil.randomMax(4));
        }

        return listCard;
    }

    public static byte[] rollFlush() {
        List<Byte> card = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            card.add((byte) i);
        }
        Collections.shuffle(card);
        int color = GameUtil.randomMax(4);
        List<Byte> listCard1 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            listCard1.add((byte) (card.get(i) * 4 + color));
        }
        byte[] listCard = new byte[5];
        for (int i = 0; i < listCard.length; i++) {
            listCard[i] = listCard1.get(i);
        }
        return listCard;
    }

    public static byte[] rollFullHouse() {
        byte[] listCard = new byte[5];
        List<Byte> card = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            card.add((byte) i);
        }
        Collections.shuffle(card);

        byte index0 = card.get(0);
        byte index1 = card.get(1);
        card.clear();
        for (int i = 0; i < 4; i++) {
            card.add((byte) (index0 * 4 + i));
        }
        Collections.shuffle(card);
        for (int i = 0; i < 3; i++) {
            listCard[i] = card.get(i);
        }
        card.clear();
        for (int i = 0; i < 4; i++) {
            card.add((byte) (index1 * 4 + i));
        }
        Collections.shuffle(card);
        for (int i = 2; i < 4; i++) {
            listCard[i] = card.get(i);
        }
        return listCard;
    }

    public static byte[] rollFourOfAKind() {
        byte[] listCard = new byte[5];
        int value = GameUtil.randomMax(13);
        byte card = (byte) ((value + 1) % 13 + GameUtil.randomMax(4));
        for (int i = 0; i < 4; i++) {
            listCard[i] = (byte) (value * 4 + i);
        }
        listCard[4] = card;
        return listCard;
    }

    public static byte[] rollThreeOfAKind() {
        byte[] listCard = new byte[5];
        int value = GameUtil.randomMax(13);
        byte card1 = (byte) ((value + 1) % 13 + GameUtil.randomMax(4));
        byte card2 = (byte) ((value - 1) % 13 + GameUtil.randomMax(4));
        List<Byte> listCardThree = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            listCardThree.add((byte) (value * 4 + i));
        }
        Collections.shuffle(listCardThree);
        for (int i = 0; i < 3; i++) {
            listCard[i] = listCardThree.get(i);
        }
        listCard[3] = card1;
        listCard[4] = card2;
        return listCard;
    }

    public static byte[] rollTwoPair() {
        byte[] listCard = new byte[5];
        List<Byte> card = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            card.add((byte) i);
        }
        Collections.shuffle(card);

        byte index0 = card.get(0);
        byte index1 = card.get(1);
        byte index2 = card.get(2);
        card.clear();
        for (int i = 0; i < 4; i++) {
            card.add((byte) (index0 * 4 + i));
        }
        Collections.shuffle(card);
        listCard[0] = card.get(0);
        listCard[1] = card.get(1);

        card.clear();
        for (int i = 0; i < 4; i++) {
            card.add((byte) (index1 * 4 + i));
        }
        Collections.shuffle(card);
        listCard[2] = card.get(2);
        listCard[3] = card.get(3);

        card.clear();
        for (int i = 0; i < 4; i++) {
            card.add((byte) (index2 * 4 + i));
        }
        Collections.shuffle(card);
        listCard[4] = card.get(3);
        return listCard;
    }

    public static int[] rate = {
            35,                // lose
            35,                 // pair J++
            15,                 // two pair
            10,                 // three of a kind
            5,                 // straigh
    };

    public static List<Card> dealCardForUserBigWin() {
        int x = GameUtil.randomMax(100);

        if(x<50){
            return playLose().cards;
        }

        int index = getIndexRollBigWin();
        if(index == 0){
            return playLose().cards;
        }
        if(index == 1){
            return playPairJ();
        }
        if(index == 2){
            return byteArrayToList(rollTwoPair());
        }
        if(index == 3){
            return byteArrayToList(rollThreeOfAKind());
        }
        if(index == 4){
            return byteArrayToList(rollStraight());
        }
        return playPairJ();
    }

    public static int getIndexRollBigWin() {
        int random = GameUtil.randomMax(100);
        for (int i = 0; i < rate.length; i++) {
            if (random < rate[i]) {
                return i;
            }
            random -= rate[i];
        }
        return rate.length - 1;
    }

    public static GenerationMiniPoker gen = new GenerationMiniPoker();

    public static List<Card> playPairJ(){
        while (true) {
            List<Card> cards = gen.randomCards();
            GroupType groupType;
            if ((groupType = CardLibUtils.calculateTypePoker(cards)) == null) continue;
            if (groupType == GroupType.OnePair) {
                if (CardLibUtils.pairEqualOrGreatJack(cards)) {
                    return cards;
                }
            }
        }
    }

    public static MinipokerResultData playLose() {
        MinipokerResultData minipokerResultData = new MinipokerResultData();
        while (true) {
            List<Card> cards = gen.randomCards();
            GroupType groupType;
            if ((groupType = CardLibUtils.calculateTypePoker(cards)) == null) continue;
            if (groupType == GroupType.HighCard) {
                minipokerResultData.cards = cards;
                minipokerResultData.groupType = groupType;
                minipokerResultData.result = 11;
                return minipokerResultData;
            }
        }
    }


    public static List<Card> byteArrayToList(byte[] listCard) {
        List<Card> listReturn = new ArrayList<>();
        for (int i = 0; i < listCard.length; i++) {
            listReturn.add(new Card(listCard[i]));
        }
        return listReturn;
    }
}
