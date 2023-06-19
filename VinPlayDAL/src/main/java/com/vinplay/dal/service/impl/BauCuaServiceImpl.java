package com.vinplay.dal.service.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.dal.dao.BauCuaDAO;
import com.vinplay.dal.dao.impl.BauCuaDAOImpl;
import com.vinplay.dal.service.BauCuaService;
import com.vinplay.vbee.common.enums.Games;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.messages.BaseMessage;
import com.vinplay.vbee.common.messages.minigame.baucua.ResultBauCuaMsg;
import com.vinplay.vbee.common.messages.minigame.baucua.ToiChonCaMsg;
import com.vinplay.vbee.common.messages.minigame.baucua.TransactionBauCuaDetailMsg;
import com.vinplay.vbee.common.messages.minigame.baucua.TransactionBauCuaMsg;
import com.vinplay.vbee.common.models.cache.ToiChonCaModel;
import com.vinplay.vbee.common.models.cache.TopWinCache;
import com.vinplay.vbee.common.models.minigame.TopWin;
import com.vinplay.vbee.common.models.minigame.baucua.ResultBauCua;
import com.vinplay.vbee.common.models.minigame.baucua.ToiChonCa;
import com.vinplay.vbee.common.models.minigame.baucua.TransactionBauCua;
import com.vinplay.vbee.common.models.minigame.baucua.TransactionBauCuaDetail;
import com.vinplay.vbee.common.rmq.RMQApi;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class BauCuaServiceImpl
implements BauCuaService {
    private BauCuaDAO dao = new BauCuaDAOImpl();

    @Override
    public void saveTransactionBauCuaDetail(TransactionBauCuaDetail tranDetail) throws IOException, TimeoutException, InterruptedException {
        TransactionBauCuaDetailMsg msg = new TransactionBauCuaDetailMsg();
        msg.username = tranDetail.username;
        msg.referenceId = tranDetail.referenceId;
        msg.room = tranDetail.room;
        msg.transactionCode = tranDetail.transactionCode;
        msg.betValues = tranDetail.betValues;
        msg.moneyType = tranDetail.moneyType;
        RMQApi.publishMessage((String)"queue_baucua", (BaseMessage)msg, (int)130);
    }

    @Override
    public void saveTransactionBauCua(TransactionBauCua transaction) throws IOException, TimeoutException, InterruptedException {
        TransactionBauCuaMsg msg = new TransactionBauCuaMsg();
        msg.username = transaction.username;
        msg.referenceId = transaction.referenceId;
        msg.room = transaction.room;
        msg.dices = transaction.dices;
        msg.transactionCode = transaction.transactionCode;
        msg.betValues = transaction.betValues;
        msg.prizes = transaction.prizes;
        msg.totalExchange = transaction.totalExchange;
        msg.moneyType = transaction.moneyType;
        RMQApi.publishMessage("queue_baucua", (BaseMessage)msg, (int)131);
    }

    @Override
    public void saveResultBauCua(ResultBauCua resultBC) throws IOException, TimeoutException, InterruptedException {
        ResultBauCuaMsg msg = new ResultBauCuaMsg();
        msg.referenceId = resultBC.referenceId;
        msg.room = resultBC.room;
        msg.minBetValue = resultBC.minBetValue;
        msg.dices = resultBC.dices;
        msg.xPot = resultBC.xPot;
        msg.xValue = resultBC.xValue;
        msg.totalBetValues = resultBC.totalBetValues;
        msg.totalPrizes = resultBC.totalPrizes;
        RMQApi.publishMessage((String)"queue_baucua", (BaseMessage)msg, (int)132);
    }

    @Override
    public List<TransactionBauCua> getLSGDBauCua(String username, int page, byte moneyType) {
        return this.dao.getLSGDBauCua(username, page, moneyType);
    }

    @Override
    public ResultBauCua getPhienBauCua(long referenceId) {
        return this.dao.getPhienBauCua(referenceId);
    }

    @Override
    public List<TopWin> getTopBauCua(byte moneyType, String startDate, String endDate) {
        TopWinCache topBCCache;
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap topMap = client.getMap("cacheTop");
        if (topMap.containsKey((Object)(Games.BAU_CUA.getName() + "_" + moneyType)) && (topBCCache = (TopWinCache)topMap.get((Object)(Games.BAU_CUA.getName() + "_" + moneyType))) != null) {
            return topBCCache.getResult();
        }
        return this.dao.getTopBauCua(moneyType, startDate, endDate);
    }

    @Override
    public void updateAllTop() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = df.format(new Date());
        String startDate = currentDate + " 00:00:00";
        String endDate = currentDate + " 23:59:59";
        List<TopWin> topWinVin = this.dao.getTopBauCua((byte)1, startDate, endDate);
        List<TopWin> topWinXu = this.dao.getTopBauCua((byte)0, startDate, endDate);
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap topMap = client.getMap("cacheTop");
        TopWinCache cacheVin = (TopWinCache)topMap.get((Object)(Games.BAU_CUA.getName() + "_1"));
        if (cacheVin == null) {
            cacheVin = new TopWinCache();
        }
        cacheVin.setResult(topWinVin);
        topMap.put((Games.BAU_CUA.getName() + "_1"), cacheVin);
        TopWinCache cacheXu = (TopWinCache)topMap.get((Games.BAU_CUA.getName() + "_0"));
        if (cacheXu == null) {
            cacheXu = new TopWinCache();
        }
        cacheXu.setResult(topWinXu);
        topMap.put((Games.BAU_CUA.getName() + "_0"), cacheXu);
    }

    @Override
    public int countLSGDBauCua(String username, byte moneyType) {
        return this.dao.countLSGDBauCua(username, moneyType) / 10 + 1;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void calculteToiChonCa(byte[] dices, List<TransactionBauCua> transactions) throws IOException, TimeoutException, InterruptedException {
        boolean existCa = false;
        int CA = 3;
        for (int i = 0; i < dices.length; ++i) {
            if (dices[i] != CA) continue;
            existCa = true;
        }
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String,ToiChonCaModel> map = client.getMap("cacheToiChonCa");
        for (TransactionBauCua tran : transactions) {
            ToiChonCaModel model2;
            if (map.containsKey(tran.username)) {
                try {
                    map.lock(tran.username);
                    model2 = map.get(tran.username);
                    if (!model2.playOnToday()) {
                        model2.soCaHighScore = 0;
                        model2.clear();
                    }
                    if (tran.betValues[3] >= 2000L && existCa) {
                        ToiChonCaModel toiChonCaModel = model2;
                        toiChonCaModel.soCa = (short)(toiChonCaModel.soCa + 1);
                        model2.addNewPhien(tran.referenceId);
                        ToiChonCaModel toiChonCaModel2 = model2;
                        toiChonCaModel2.tongDat += tran.betValues[3];
                        ToiChonCaModel toiChonCaModel3 = model2;
                        toiChonCaModel3.tongThang += tran.prizes[3];
                        if (!model2.valid && tran.betValues[3] >= 5000L) {
                            model2.valid = true;
                        }
                        if (model2.valid && model2.soCa > model2.soCaHighScore) {
                            this.newHighScoreToiChonCa(model2);
                        }
                    } else {
                        model2.clear();
                    }
                    map.put(tran.username,model2);
                } catch (Exception e) {
                    continue;
                } finally {
                    map.unlock(tran.username);
                }
            }
            if (tran.betValues[3] < 2000L || !existCa) continue;
            model2 = new ToiChonCaModel(tran.username, tran.referenceId, tran.betValues[3], tran.prizes[3]);
            model2.soCaHighScore = this.dao.getHighScore(tran.username);
            if (tran.betValues[3] >= 5000L) {
                model2.valid = true;
                if (model2.soCa > model2.soCaHighScore) {
                    this.newHighScoreToiChonCa(model2);
                }
            }
            map.put(tran.username, model2);
        }
    }

    private void newHighScoreToiChonCa(ToiChonCaModel model) throws IOException, TimeoutException, InterruptedException {
        model.soCaHighScore = model.soCa;
        ToiChonCaMsg msg = new ToiChonCaMsg();
        msg.username = model.username;
        msg.soCa = model.soCa;
        msg.soVan = model.soVan;
        msg.tongDat = model.tongDat;
        msg.tongThang = model.tongThang;
        msg.currentPhien = model.currentPhien;
        msg.listPhien = model.getListPhien();
        RMQApi.publishMessage((String)"queue_baucua", (BaseMessage)msg, (int)133);
    }

    @Override
    public List<ResultBauCua> getLichSuPhien(int size, byte room) {
        return this.dao.getLichSuPhien(size, room);
    }

    @Override
    public List<ToiChonCa> getTopToiChonCa(String startTime, String endTime) {
        return this.dao.getTopToiChonCa(startTime, endTime);
    }

    @Override
    public void setKetQuaBauCua(short[] ketQuaBauCua){
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap map = client.getMap("ketquabaucua");
        String key = "ketquabaucua";
        map.put(key,ketQuaBauCua);
    }

//    @Override
//    public byte[] getKetQuaBauCua(){
//        HazelcastInstance client = HazelcastClientFactory.getInstance();
//        IMap map = client.getMap("ketquabaucua");
//        String key = "ketquabaucua";
//        if(map.containsKey(key)){
//            return (byte[]) map.get(key);
//        }
//        return null;
//    }

    @Override
    public short[] getKetQuaBauCua(){
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap map = client.getMap("ketquabaucua");
        String key = "ketquabaucua";
        if(map.containsKey(key)){
            return (short[]) map.get(key);
        }
        return null;
    }

    @Override
    public byte[] layKetQuaBauCua(){
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap map = client.getMap("ketquabaucua");
        String key = "ketquabaucua";
        if(map.containsKey(key)){
            return (byte[]) map.remove(key);
        }
        return null;
    }
}

