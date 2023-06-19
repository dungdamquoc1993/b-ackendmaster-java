package game.tienlen.server.BotTlmn;

import bitzero.util.common.business.Debug;

import java.util.*;

/**
 * Đại diện cho bộ bài của 1 người chơi
 */
public class Hand {

    public List<Card> cards = Collections.synchronizedList(new ArrayList<>());
    public Hand(byte[] ids){
        Arrays.sort(ids);
        for(int i=0;i<ids.length;i++){
            this.cards.add(new Card(ids[i]));
        }
    }


    public byte[] getCardIds(){
        Iterator it = this.cards.iterator();
        byte[] card = new byte[this.cards.size()];
        int i = 0;
        while(it.hasNext()){
            Card b = (Card)it.next();
            card[i] = b.id;
            i++;
        }
        return card;
    }


    public byte getNumberCards(){
        return (byte) this.cards.size();
    }

    private boolean isContainCard(byte id){
        for(Card card:this.cards){
            if(card.id == id) return true;
        }
        return false;
    }

    public boolean isContainCards(byte[] ids){
        for(int i =0;i<ids.length;i++){
            if(!this.isContainCard(ids[i])){
                Debug.trace("Card ERROR: ",ids[i]);
                return false;
            }
        }
        return true;
    }

    public  void removeCards(byte[] ids){
        for(int i =0;i<ids.length;i++){
            Iterator it = this.cards.iterator();
            while(it.hasNext()){
                Card c = (Card) it.next();
                if(c.id == ids[i]){
                    Debug.trace("RemoveCard ",ids[i]);
                    it.remove();
                }
            }
        }
    }
}
