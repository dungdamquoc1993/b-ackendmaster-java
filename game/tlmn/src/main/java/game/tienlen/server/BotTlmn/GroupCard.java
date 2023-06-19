package game.tienlen.server.BotTlmn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupCard {

	public static final int NONE_CARD = 0;
	public static final int ONE_CARD = 1;
	public static final int PAIR_CARD = 2;
	public static final int THREE_OF_A_KIND_CARD = 3;
	public static final int STRAIGH_CARD = 4;
	public static final int THREE_PAIR_CARD = 5;
	public static final int FOUR_OF_A_KIND_CARD = 6;
	public static final int FOUR_PAIR_CARD = 7;

	public int type = NONE_CARD;
	public int strong = 0;
	public List<Card> cards = new ArrayList<Card>();


	public static final int NONE_STRONG = 0;
	public static final int ONE_2_STRONG = 1;
	public static final int THREE_PAIR_CARD_SRONG = 2;
	public static final int PAIR_CARD_2_STRONG = 3;
	public static final int FOUR_OF_A_KIND_CARD_STRONG = 4;
	public static final int FOUR_PAIR_CARD_STRONG = 5;


	public GroupCard(byte[] ids) {
		Arrays.sort(ids);
		for (byte id : ids) {
			Card card = new Card(id);
			this.cards.add(card);
		}
		this.type = this.checkTypeOfListCard();
		this.strong = getStrongHand();
	}

	private int getStrongHand(){
		switch(this.type){
			case ONE_CARD:{
				if(this.cards.get(0).number == 12){
					return ONE_2_STRONG;
				}
				return NONE_STRONG;
			}
			case THREE_PAIR_CARD:{
				return THREE_PAIR_CARD_SRONG;
			}
			case PAIR_CARD:{
				if(this.cards.get(0).number == 12){
					return PAIR_CARD_2_STRONG;
				}
				return NONE_STRONG;
			}
			case FOUR_OF_A_KIND_CARD:{
				return FOUR_OF_A_KIND_CARD_STRONG;
			}
			case FOUR_PAIR_CARD:{
				return FOUR_PAIR_CARD_STRONG;
			}
		}
		return NONE_STRONG;
	}

	private int checkTypeOfListCard() {
		if (this.cards.size() == 1) {
			return  ONE_CARD;
		}
		if (CompareCardTLMN.checkPairCard(this.cards)) {
			return PAIR_CARD;
		}
		if (CompareCardTLMN.checkThreeOfAKindCard(this.cards)) {
			return THREE_OF_A_KIND_CARD;
		}
		if (CompareCardTLMN.checkFourOfAKindCard(this.cards)) {
			return FOUR_OF_A_KIND_CARD;
		}
		if (CompareCardTLMN.checkStraightCard(this.cards)) {
			return STRAIGH_CARD;
		}
		if(CompareCardTLMN.check3PairStraightCard(this.cards)){
			return THREE_PAIR_CARD;
		}
		if(CompareCardTLMN.check4PairStraightCard(this.cards)){
			return FOUR_PAIR_CARD;
		}
		return NONE_CARD;
	}
}
