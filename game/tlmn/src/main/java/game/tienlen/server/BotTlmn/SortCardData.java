package game.tienlen.server.BotTlmn;

import java.util.List;
import java.util.Map;

/**
 * Created by hp on 9/30/2019.
 */
public class SortCardData {
    public List<Card> listCard;
    public Map<Integer, Object> mapTypeOfAllCard;    // se la kieu va quan bai max cua bo do
    //bon doi thong object la byte
    // ba doi thong object la byte
    // tu quy object la array
    // sam object la array
    // doi object la array
    // sanh object la list Array
    public int numberCardOne = 0; // so luong quan le
    public int numberBo = 0;

    public SortCardData(List<Card> listCard, Map<Integer, Object> mapTypeOfAllCard, int numberCardOne, int numberBo){
        this.listCard = listCard;
        this.mapTypeOfAllCard = mapTypeOfAllCard;
        this.numberCardOne = numberCardOne;
        this.numberBo = numberBo;
    }
}
