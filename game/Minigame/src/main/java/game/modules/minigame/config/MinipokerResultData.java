package game.modules.minigame.config;

import com.vinplay.cardlib.models.Card;
import com.vinplay.cardlib.models.GroupType;

import java.util.ArrayList;
import java.util.List;

public class MinipokerResultData {

    public List<Card> cards = new ArrayList<>();
    public boolean isJackPot = false;
    public GroupType groupType;
    public short result = 0;
    public long moneyWin = 0;
}
