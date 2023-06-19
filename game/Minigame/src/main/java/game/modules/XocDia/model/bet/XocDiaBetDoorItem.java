package game.modules.XocDia.model.bet;

import com.vinplay.game.XocDia.history.XocDiaGamePlayHistoryDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XocDiaBetDoorItem {
    public Map<Long, BetItem> listBet = new HashMap<>();
    public List<XocDiaGamePlayHistoryDetail> historyBet = new ArrayList<>();

    public void clear() {
        this.listBet.clear();
        this.historyBet.clear();
    }
}
