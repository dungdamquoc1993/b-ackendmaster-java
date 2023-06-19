package game;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.dal.dao.PokeGoDAO;
import com.vinplay.vbee.common.enums.Games;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.messages.minigame.pokego.LogPokeGoMessage;
import com.vinplay.vbee.common.models.cache.TopPokeGoModel;
import com.vinplay.vbee.common.models.minigame.pokego.LSGDPokeGo;
import com.vinplay.vbee.common.models.minigame.pokego.TopPokeGo;
import com.vinplay.vbee.common.rmq.RMQApi;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class SlotExtendServiceImplement implements SlotExtendService {
    private PokeGoDAO dao = new SlotExtendDaoImpl();

    public SlotExtendServiceImplement() {
    }

    @Override
    public void logSlotExtend(long referenceId, String username, long betValue, String linesBetting, String linesWin, String prizesOnLine, short result, long totalPrizes, short moneyType, String time) throws IOException, TimeoutException, InterruptedException {
        LogPokeGoMessage message = new LogPokeGoMessage();
        message.referenceId = referenceId;
        message.username = username;
        message.betValue = betValue;
        message.linesBetting = linesBetting;
        message.linesWin = linesWin;
        message.prizesOnLine = prizesOnLine;
        message.result = result;
        message.totalPrizes = totalPrizes;
        message.moneyType = moneyType;
        message.time = time;
        RMQApi.publishMessage("queue_slotExtend", message, 135);
    }

    public int countLSDG(String username, int moneyType) {
        return this.dao.countLSGD(username, moneyType);
    }

    public List<LSGDPokeGo> getLSGD(String username, int pageNumber, int moneyType) {
        return this.dao.getLSGD(username, moneyType, pageNumber);
    }

    public void addTop(String username, int betValue, long totalPrizes, int moneyType, String time, int result) throws IOException, TimeoutException, InterruptedException {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, Object> topMap = client.getMap("cacheTop");
        TopPokeGoModel topPokeGo = (TopPokeGoModel) topMap.get("SlotExtend" + "_" + moneyType);
        if (topPokeGo == null) {
            topPokeGo = new TopPokeGoModel();
        }

        TopPokeGo entry = new TopPokeGo();
        entry.un = username;
        entry.bv = betValue;
        entry.pz = totalPrizes;
        entry.ts = time;
        entry.rs = result;
        topPokeGo.put(entry);
        topMap.put("SlotExtend" + "_" + moneyType, topPokeGo);
    }

    @Override
    public List<TopPokeGo> getTopSlotExtend(int moneyType, int page) {
        if (page <= 10) {
            HazelcastInstance client = HazelcastClientFactory.getInstance();
            IMap<String, Object> topMap = client.getMap("cacheTop");
            TopPokeGoModel topPokeGo = (TopPokeGoModel) topMap.get("SlotExtend" + "_" + moneyType);
            if (topPokeGo == null) {
                topPokeGo = new TopPokeGoModel();
            }

            if (topPokeGo.getResults().size() == 0) {
                List<TopPokeGo> results = this.dao.getTop(moneyType, 100);
                topPokeGo.setResults(results);
                topMap.put("SlotExtend" + "_" + moneyType, topPokeGo);
            }

            return topPokeGo.getResults(page, 10);
        } else {
            return this.dao.getTopPokeGo(moneyType, page);
        }
    }

    public long getLastReferenceId() {
        return this.dao.getLastRefenceId();
    }
}
