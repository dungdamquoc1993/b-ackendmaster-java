package game.tienlen.server;

import bitzero.util.common.business.Debug;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DealCard {

    public static final int NUMBER_CARD_OF_PLAYER = 13;
    public static final int DECK_CARD = 52;

    public static final int FULL_STRAIGHT = 0;
    public static final int FOUR_OF_A_KIND_2 = 1;
    public static final int SIX_PAIR = 2;
    public static final int FIVE_STRAIGHT_PAIR = 3;

    public static List<Byte> createDeck() {
        List<Byte> deck = new ArrayList<>();
        for (byte i = 0; i < DECK_CARD; i++) {
            deck.add(i);
        }
        return deck;
    }

    public static List<List<Byte>> dealCardNormal(byte numberOfPlayer) {
        List<Byte> deck = createDeck();
        Collections.shuffle(deck);
        List<List<Byte>> listCardForPlayer = new ArrayList<>();
        for (int i = 0; i < numberOfPlayer; i++) {
            listCardForPlayer.add(deck.subList(i * NUMBER_CARD_OF_PLAYER, i * NUMBER_CARD_OF_PLAYER + NUMBER_CARD_OF_PLAYER));
        }
        return listCardForPlayer;
    }

    public static List<List<Byte>> dealCardFullStraight(byte numberOfPlayer, int indexAutoWin) {
        List<Byte> deck = createDeck();
        List<Byte> winner = new ArrayList<>();
        for (byte i = 11; i >= 0; i--) {
            int value = GameUtil.randomMax(4);
            byte card = deck.get(i * 4 + value);
            winner.add((card));
            deck.remove(card);
        }
        Collections.shuffle(deck);
        Byte card0 = deck.get(0);
        winner.add(card0); // xong bai cua thang winner va con lai bai cua dua con lai
        deck.remove(card0);
        List<List<Byte>> listCardForPlayer = new ArrayList<>();
        int compareValue = Math.max(indexAutoWin, 0);
        for (int i = 0; i < numberOfPlayer; i++) {
            if (i == compareValue) {
                listCardForPlayer.add(winner);
            } else {
                listCardForPlayer.add(deck.subList(i * NUMBER_CARD_OF_PLAYER - (i < compareValue ? 0 : NUMBER_CARD_OF_PLAYER),
                        i * NUMBER_CARD_OF_PLAYER + (i < compareValue ? NUMBER_CARD_OF_PLAYER : 0)));
            }
        }
        if (indexAutoWin < 0) {
            Collections.shuffle(listCardForPlayer);
        }
        return listCardForPlayer;
    }

    private static List<List<Byte>> dealCardFourOfAKind2(byte numberOfPlayer, int indexAutoWin) {
        List<Byte> deck = createDeck();
        List<Byte> winner = new ArrayList<>();
        for (int i = DECK_CARD - 1; i > DECK_CARD - 5; i--) {
            Debug.trace("index", i);
            byte card = deck.get(i);
            winner.add(card);
            deck.remove(card);
        }
        Collections.shuffle(deck);
        int deckSize = deck.size();
        for (int i = deckSize - 1; i >= deckSize - NUMBER_CARD_OF_PLAYER + 4; i--) {
            Byte card = deck.get(i);
            winner.add(card);
            deck.remove(card);
        }
        List<List<Byte>> listCardForPlayer = new ArrayList<>();
        int compareValue = Math.max(indexAutoWin, 0);
        for (int i = 0; i < numberOfPlayer; i++) {
            if (i == compareValue) {
                listCardForPlayer.add(winner);
            } else {
                listCardForPlayer.add(deck.subList(i * NUMBER_CARD_OF_PLAYER - (i < compareValue ? 0 : NUMBER_CARD_OF_PLAYER),
                        i * NUMBER_CARD_OF_PLAYER + (i < compareValue ? NUMBER_CARD_OF_PLAYER : 0)));
            }
        }
        if (indexAutoWin < 0) {
            Collections.shuffle(listCardForPlayer);
        }
        return listCardForPlayer;
    }

    private static List<List<Byte>> dealCardSixPair(byte numberOfPlayer, int indexAutoWin) {
        List<Byte> deck = createDeck();
        List<Byte> winner = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            int value = GameUtil.randomMax(deck.size() / 2);
            Byte card1 = deck.get(value * 2 + 1);
            Byte card2 = deck.get(value * 2);
            winner.add(card1);
            winner.add(card2);
            deck.remove(card1);
            deck.remove(card2);
        }
        Collections.shuffle(deck);

        Byte card0 = deck.get(0);
        winner.add(card0); // xong bai cua thang winner va con lai bai cua dua con lai
        deck.remove(card0);
        List<List<Byte>> listCardForPlayer = new ArrayList<>();
        int compareValue = Math.max(indexAutoWin, 0);
        for (int i = 0; i < numberOfPlayer; i++) {
            if (i == compareValue) {
                listCardForPlayer.add(winner);
            } else {
                listCardForPlayer.add(deck.subList(i * NUMBER_CARD_OF_PLAYER - (i < compareValue ? 0 : NUMBER_CARD_OF_PLAYER),
                        i * NUMBER_CARD_OF_PLAYER + (i < compareValue ? NUMBER_CARD_OF_PLAYER : 0)));
            }
        }
        if (indexAutoWin < 0) {
            Collections.shuffle(listCardForPlayer);
        }
        return listCardForPlayer;
    }

    private static void addPairCard(List<Byte> deck, int start, int i, List<Byte> winner) {
        int value = GameUtil.randomMax(4);
        int distance = GameUtil.randomMax(3) + 1;
        int value1 = (value + distance) % 4;
        Debug.trace(value);
        Debug.trace(value1);
        Byte card1 = deck.get((start + i) * 4 + value);
        Byte card2 = deck.get((start + i) * 4 + value1);
        winner.add(card1);
        winner.add(card2);
        deck.remove(card1);
        deck.remove(card2);
    }

    private static List<List<Byte>> dealCardFiveStraightPair(byte numberOfPlayer, int indexAutoWin) {
        List<Byte> deck = createDeck();
        List<Byte> winner = new ArrayList<>();
        int start = GameUtil.randomMax(8);
        for (int i = 4; i >= 0; i--) {
            addPairCard(deck, start, i, winner);
        }
        Collections.shuffle(deck);
        int deckSize = deck.size();
        for (int i = deckSize - 1; i > deckSize - 4; i--) {
            Byte card = deck.get(i);
            winner.add(card);
            deck.remove(card);
        }

        List<List<Byte>> listCardForPlayer = new ArrayList<>();
        int compareValue = Math.max(indexAutoWin, 0);
        for (int i = 0; i < numberOfPlayer; i++) {
            if (i == compareValue) {
                listCardForPlayer.add(winner);
            } else {
                listCardForPlayer.add(deck.subList(i * NUMBER_CARD_OF_PLAYER - (i < compareValue ? 0 : NUMBER_CARD_OF_PLAYER),
                        i * NUMBER_CARD_OF_PLAYER + (i < compareValue ? NUMBER_CARD_OF_PLAYER : 0)));
            }
        }
        if (indexAutoWin < 0) {
            Collections.shuffle(listCardForPlayer);
        }
        return listCardForPlayer;
    }

    public static byte[][] dealCardBotAutoWin(int indexAutoWin) {
        List<List<Byte>> listCardForPlayer = new ArrayList<>();
        int value = GameUtil.randomMax(4);
        switch (value) {
            case FULL_STRAIGHT: {
                listCardForPlayer = dealCardFullStraight((byte) 4, indexAutoWin);
                break;
            }
            case FOUR_OF_A_KIND_2: {
                listCardForPlayer = dealCardFourOfAKind2((byte) 4, indexAutoWin);
                break;
            }
            case SIX_PAIR: {
                listCardForPlayer = dealCardSixPair((byte) 4, indexAutoWin);
                break;
            }
            case FIVE_STRAIGHT_PAIR: {
                listCardForPlayer = dealCardFiveStraightPair((byte) 4, indexAutoWin);
                break;
            }
            default: {
                listCardForPlayer = dealCardFiveStraightPair((byte) 4, indexAutoWin);
                break;
            }
        }
        byte[][] listCardReturn = new byte[listCardForPlayer.size()][];
        for(int i =0;i<listCardReturn.length;i++){
            listCardReturn[i] = changeListToArrayCard(listCardForPlayer.get(i));
        }
        return listCardReturn;
    }

    public static byte[] changeListToArrayCard(List listCard) {
        Iterator it = listCard.iterator();
        byte[] card = new byte[listCard.size()];
        int i = 0;
        while (it.hasNext()) {
            byte b = (byte) it.next();
            card[i] = b;
            i++;
        }
        return card;
    }
}
