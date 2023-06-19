package game.tienlen.server.BotTlmn;

import java.util.List;

public class CompareCardTLMN {
    public static boolean compareCard(Card card1, Card card2) {
        return card1.id > card2.id;
    }

    public static boolean checkPairCard(List<Card> cards) {
        return cards.size() == 2 && cards.get(0).number == cards.get(1).number;
    }

    public static boolean checkThreeOfAKindCard(List<Card> cards) {
        return cards.size() == 3 && cards.get(0).number == cards.get(2).number;
    }

    public static boolean checkFourOfAKindCard(List<Card> cards) {
        return cards.size() == 4 && cards.get(0).number == cards.get(3).number;
    }

    public static boolean checkStraightCard(List<Card> cards) {
        if (cards.size() < 3) {
            return false;
        }
        if (cards.get(cards.size() - 1).number == 12)
            return false; // co quan 2 ko la sanh

        for (int i = 0; i < cards.size() - 1; i++) {
            if (cards.get(i).number + 1 != cards.get(i + 1).number)
                return false;
        }
        return true;
    }

    public static boolean check3PairStraightCard(List<Card> cards) {
        if (cards.get(cards.size() - 1).number == 12) return false;
        if (cards.size() == 6) {
            for (int i = 0; i < cards.size() - 1; i += 2){
//				System.out.print(i);
                if (i + 2 >= 6) {
                    if ((cards.get(i).number != cards.get(i + 1).number)){
                        return false;
                    }
                } else {
                    if ((cards.get(i).number != cards.get(i + 1).number)
                            || (cards.get(i + 1).number + 1 != cards.get(i + 2).number)) {
                        return false;
                    }
                }

            }
            return true;
        }
        return false;
    }

    public static boolean check4PairStraightCard(List<Card> cards) {
        if (cards.get(cards.size() - 1).number == 12) return false;
        if (cards.size() == 8) {
            for (int i = 0; i < cards.size() - 1; i += 2) {
                if (i + 2 >= 8) {
                    if ((cards.get(i).number != cards.get(i + 1).number)){
                        return false;
                    }
                } else {
                    if ((cards.get(i).number != cards.get(i + 1).number)
                            || (cards.get(i + 1).number + 1 != cards.get(i + 2).number)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public static boolean canAttack(GroupCard cards1, GroupCard cards2) {
        if (cards1.type != cards2.type) {
            if (cards1.strong > 0 && cards2.strong > 0) {
                if (cards1.strong < cards2.strong) {
                    if (cards2.type == GroupCard.ONE_CARD || cards2.type == GroupCard.PAIR_CARD) {
                        return false;
                    }
                    return true;
                }

                if (cards1.strong == cards2.strong) {
                    return !compareCard(cards1.cards.get(cards1.cards.size() - 1),
                            cards2.cards.get(cards2.cards.size() - 1));
                }
            }
        } else {
            if (cards1.type == GroupCard.STRAIGH_CARD) {
                if (cards1.cards.size() != cards2.cards.size()) return false;
            }
            return !compareCard(cards1.cards.get(cards1.cards.size() - 1),
                    cards2.cards.get(cards2.cards.size() - 1));
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println("Main");
        byte[] card = {0, 1, 4, 5, 10, 11,15,16};
        GroupCard groupCard1 = new GroupCard(card);
        GroupCard groupCard2 = new GroupCard(new byte[]{51});
        System.out.println(canAttack(groupCard2, groupCard1));
        //System.out.println(groupCard1.type);
    }
}
//0 1 2 3 :3
//4 5 6 7 :4
//8 9 10 11: 5
//12 13 14 15: 6
