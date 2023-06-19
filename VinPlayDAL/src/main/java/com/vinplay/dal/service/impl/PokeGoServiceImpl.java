/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.hazelcast.core.HazelcastInstance
 *  com.hazelcast.core.IMap
 *  com.vinplay.vbee.common.enums.Games
 *  com.vinplay.vbee.common.hazelcast.HazelcastClientFactory
 *  com.vinplay.vbee.common.messages.BaseMessage
 *  com.vinplay.vbee.common.messages.minigame.pokego.LogPokeGoMessage
 *  com.vinplay.vbee.common.models.cache.TopPokeGoModel
 *  com.vinplay.vbee.common.models.minigame.pokego.LSGDPokeGo
 *  com.vinplay.vbee.common.models.minigame.pokego.TopPokeGo
 *  com.vinplay.vbee.common.rmq.RMQApi
 */
package com.vinplay.dal.service.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.dal.dao.PokeGoDAO;
import com.vinplay.dal.dao.impl.PokeGoDaoImpl;
import com.vinplay.dal.service.PokeGoService;
import com.vinplay.vbee.common.enums.Games;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.messages.BaseMessage;
import com.vinplay.vbee.common.messages.minigame.pokego.LogPokeGoMessage;
import com.vinplay.vbee.common.models.cache.TopPokeGoModel;
import com.vinplay.vbee.common.models.minigame.pokego.LSGDPokeGo;
import com.vinplay.vbee.common.models.minigame.pokego.TopPokeGo;
import com.vinplay.vbee.common.rmq.RMQApi;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class PokeGoServiceImpl
implements PokeGoService {
    private PokeGoDAO dao = new PokeGoDaoImpl();

    @Override
    public void logPokeGo(long referenceId, String username, long betValue, String linesBetting, String linesWin, String prizesOnLine, short result, long totalPrizes, short moneyType, String time) throws IOException, TimeoutException, InterruptedException {
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
        RMQApi.publishMessage((String)"queue_pokego", (BaseMessage)message, (int)134);
    }

    @Override
    public int countLSDG(String username, int moneyType) {
        return this.dao.countLSGD(username, moneyType);
    }

    @Override
    public List<LSGDPokeGo> getLSGD(String username, int pageNumber, int moneyType) {
        return this.dao.getLSGD(username, moneyType, pageNumber);
    }

    @Override
    public void addTop(String username, int betValue, long totalPrizes, int moneyType, String time, int result) throws IOException, TimeoutException, InterruptedException {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap topMap = client.getMap("cacheTop");
        TopPokeGoModel topPokeGo = (TopPokeGoModel)topMap.get((Object)(Games.POKE_GO.getName() + "_" + moneyType));
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
        topMap.put((Object)(Games.POKE_GO.getName() + "_" + moneyType), (Object)topPokeGo);
    }

    @Override
    public List<TopPokeGo> getTopPokeGo(int moneyType, int page) {
        if (page <= 10) {
            HazelcastInstance client = HazelcastClientFactory.getInstance();
            IMap topMap = client.getMap("cacheTop");
            TopPokeGoModel topPokeGo = (TopPokeGoModel)topMap.get((Object)(Games.POKE_GO.getName() + "_" + moneyType));
            if (topPokeGo == null) {
                topPokeGo = new TopPokeGoModel();
            }
            if (topPokeGo.getResults().size() == 0) {
                List<TopPokeGo> results = this.dao.getTop(moneyType, 100);
                topPokeGo.setResults(results);
                topMap.put((Object)(Games.POKE_GO.getName() + "_" + moneyType), (Object)topPokeGo);
            }
            return topPokeGo.getResults(page, 10);
        }
        return this.dao.getTopPokeGo(moneyType, page);
    }

    @Override
    public long getLastReferenceId() {
        return this.dao.getLastRefenceId();
    }
}

