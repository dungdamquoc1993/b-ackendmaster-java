/*
 * Decompiled with CFR 0.144.
 *
 * Could not load the following classes:
 *  com.hazelcast.core.HazelcastInstance
 *  com.hazelcast.core.IMap
 *  com.vinplay.vbee.common.enums.Games
 *  com.vinplay.vbee.common.hazelcast.HazelcastClientFactory
 *  com.vinplay.vbee.common.messages.BaseMessage
 *  com.vinplay.vbee.common.messages.minigame.LogNoHuSlotMessage
 *  com.vinplay.vbee.common.messages.slot.LogSlotMachineMessage
 *  com.vinplay.vbee.common.models.cache.SlotFreeDaily
 *  com.vinplay.vbee.common.models.cache.TopPokeGoModel
 *  com.vinplay.vbee.common.models.minigame.pokego.LSGDPokeGo
 *  com.vinplay.vbee.common.models.minigame.pokego.TopPokeGo
 *  com.vinplay.vbee.common.models.slot.NoHuModel
 *  com.vinplay.vbee.common.models.slot.SlotFreeSpin
 *  com.vinplay.vbee.common.rmq.RMQApi
 */
package com.vinplay.dal.service.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.dal.dao.SlotMachineDAO;
import com.vinplay.dal.dao.impl.SlotMachineDAOImpl;
import com.vinplay.dal.service.SlotMachineService;
import com.vinplay.vbee.common.enums.Games;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.messages.BaseMessage;
import com.vinplay.vbee.common.messages.minigame.LogNoHuSlotMessage;
import com.vinplay.vbee.common.messages.slot.LogSlotMachineMessage;
import com.vinplay.vbee.common.models.cache.SlotFreeDaily;
import com.vinplay.vbee.common.models.cache.TopPokeGoModel;
import com.vinplay.vbee.common.models.minigame.pokego.LSGDPokeGo;
import com.vinplay.vbee.common.models.minigame.pokego.TopPokeGo;
import com.vinplay.vbee.common.models.slot.NoHuModel;
import com.vinplay.vbee.common.models.slot.SlotFreeSpin;
import com.vinplay.vbee.common.rmq.RMQApi;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class SlotMachineServiceImpl
implements SlotMachineService {
    private SlotMachineDAO dao = new SlotMachineDAOImpl();

    @Override
    public void logKhoBau(long referenceId, String username, long betValue, String linesBetting, String linesWin, String prizesOnLine, short result, long totalPrizes, String time) throws IOException, TimeoutException, InterruptedException {
        LogSlotMachineMessage msg = this.buildLogSlotMsg(Games.KHO_BAU.getName(), referenceId, username, betValue, linesBetting, linesWin, prizesOnLine, result, totalPrizes, time);
        this.publishSlotMsg("queue_kho_bau", msg, 135);
    }

    @Override
    public void logAvengers(long referenceId, String username, long betValue, String linesBetting, String linesWin, String prizesOnLine, short result, long totalPrizes, String time) throws IOException, TimeoutException, InterruptedException {
        LogSlotMachineMessage msg = this.buildLogSlotMsg(Games.AVENGERS.getName(), referenceId, username, betValue, linesBetting, linesWin, prizesOnLine, result, totalPrizes, time);
        this.publishSlotMsg("queue_avengers", msg, 136);
    }

    @Override
    public void logSpartan(long referenceId, String username, long betValue, String linesBetting, String linesWin, String prizesOnLine, short result, long totalPrizes, String time) throws IOException, TimeoutException, InterruptedException {
        LogSlotMachineMessage msg = this.buildLogSlotMsg(Games.SPARTAN.getName(), referenceId, username, betValue, linesBetting, linesWin, prizesOnLine, result, totalPrizes, time);
        this.publishSlotMsg("queue_spartan", msg, 10136);
    }

    @Override
    public void logMyNhanNgu(long referenceId, String username, long betValue, String linesBetting, String linesWin, String prizesOnLine, short result, long totalPrizes, String time) throws IOException, TimeoutException, InterruptedException {
        LogSlotMachineMessage msg = this.buildLogSlotMsg(Games.MY_NHAN_NGU.getName(), referenceId, username, betValue, linesBetting, linesWin, prizesOnLine, result, totalPrizes, time);
        this.publishSlotMsg("queue_my_nhan_ngu", msg, 136);
    }

    @Override
    public void logVQV(long referenceId, String username, long betValue, String linesBetting, String linesWin, String prizesOnLine, short result, long totalPrizes, String time) throws IOException, TimeoutException, InterruptedException {
        LogSlotMachineMessage msg = this.buildLogSlotMsg(Games.VUONG_QUOC_VIN.getName(), referenceId, username, betValue, linesBetting, linesWin, prizesOnLine, result, totalPrizes, time);
        this.publishSlotMsg("queue_vqv", msg, 139);
    }

    @Override
    public void logRangeRover(long referenceId, String username, long betValue, String linesBetting, String linesWin, String prizesOnLine, short result, long totalPrizes, String time) throws IOException, TimeoutException, InterruptedException {
        LogSlotMachineMessage msg = this.buildLogSlotMsg(Games.RANGE_ROVER.getName(), referenceId, username, betValue, linesBetting, linesWin, prizesOnLine, result, totalPrizes, time);
        this.publishSlotMsg("queue_range_rover", msg, 8002);
    }

    @Override
    public void logMaybach(long referenceId, String username, long betValue, String linesBetting, String linesWin, String prizesOnLine, short result, long totalPrizes, String time) throws IOException, TimeoutException, InterruptedException {
        LogSlotMachineMessage msg = this.buildLogSlotMsg(Games.MAYBACH.getName(), referenceId, username, betValue, linesBetting, linesWin, prizesOnLine, result, totalPrizes, time);
        this.publishSlotMsg("queue_maybach", msg, 8003);
    }

    @Override
    public void logTamHung(long referenceId, String username, long betValue, String linesBetting, String linesWin, String prizesOnLine, short result, long totalPrizes, String time) throws IOException, TimeoutException, InterruptedException {
        LogSlotMachineMessage msg = this.buildLogSlotMsg(Games.TAMHUNG.getName(), referenceId, username, betValue, linesBetting, linesWin, prizesOnLine, result, totalPrizes, time);
        this.publishSlotMsg("queue_tamhung", msg, 8004);
    }

    @Override
    public void logRollRoye(long referenceId, String username, long betValue, String linesBetting, String linesWin, String prizesOnLine, short result, long totalPrizes, String time) throws IOException, TimeoutException, InterruptedException {
        LogSlotMachineMessage msg = this.buildLogSlotMsg(Games.ROLL_ROYE.getName(), referenceId, username, betValue, linesBetting, linesWin, prizesOnLine, result, totalPrizes, time);
        this.publishSlotMsg("queue_roll_roye", msg, 8005);
    }

    @Override
    public void logBenley(long referenceId, String username, long betValue, String linesBetting, String linesWin, String prizesOnLine, short result, long totalPrizes, String time) throws IOException, TimeoutException, InterruptedException {
        LogSlotMachineMessage msg = this.buildLogSlotMsg(Games.BENLEY.getName(), referenceId, username, betValue, linesBetting, linesWin, prizesOnLine, result, totalPrizes, time);
        this.publishSlotMsg("queue_benley", msg, 8006);
    }

    @Override
    public void logBikini(long referenceId, String username, long betValue, String linesBetting, String linesWin, String prizesOnLine, short result, long totalPrizes, String time) throws IOException, TimeoutException, InterruptedException {
        LogSlotMachineMessage msg = this.buildLogSlotMsg(Games.BIKINI.getName(), referenceId, username, betValue, linesBetting, linesWin, prizesOnLine, result, totalPrizes, time);
        this.publishSlotMsg("queue_bikini", msg, 8011);
    }

    private LogSlotMachineMessage buildLogSlotMsg(String gameName, long referenceId, String username, long betValue, String linesBetting, String linesWin, String prizesOnLine, short result, long totalPrizes, String time) {
        LogSlotMachineMessage message = new LogSlotMachineMessage();
        message.gameName = gameName;
        message.referenceId = referenceId;
        message.username = username;
        message.betValue = betValue;
        message.linesBetting = linesBetting;
        message.linesWin = linesWin;
        message.prizesOnLine = prizesOnLine;
        message.result = result;
        message.totalPrizes = totalPrizes;
        message.time = time;
        return message;
    }

    private void publishSlotMsg(String queueName, LogSlotMachineMessage msg, int commnad) throws IOException, TimeoutException, InterruptedException {
        RMQApi.publishMessage((String)queueName, (BaseMessage)msg, (int)commnad);
    }

    @Override
    public int countLSDG(String gameName, String username) {
        return this.dao.countLSGD(gameName, username);
    }

    @Override
    public List<LSGDPokeGo> getLSGD(String gameName, String username, int pageNumber) {
        return this.dao.getLSGD(gameName, username, pageNumber);
    }

    @Override
    public void addTop(String gameName, String username, int betValue, long totalPrizes, String time, int result) throws IOException, TimeoutException, InterruptedException {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap topMap = client.getMap("cacheTop");
        TopPokeGoModel topPokeGo = (TopPokeGoModel)topMap.get((Object)gameName);
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
        topMap.put((Object)gameName, (Object)topPokeGo);
        System.out.println("test");
    }

    @Override
    public List<TopPokeGo> getTopSlotMachine(String gameName, int page) {
        if (page <= 10) {
            TopPokeGoModel topPokeGo;
            IMap topMap;
            HazelcastInstance client;
            int pageSize = 10;
            if (gameName.equalsIgnoreCase(Games.KHO_BAU.getName())) {
                pageSize = 5;
            }
            if ((topPokeGo = (TopPokeGoModel)(topMap = (client = HazelcastClientFactory.getInstance()).getMap("cacheTop")).get((Object)gameName)) == null) {
                topPokeGo = new TopPokeGoModel();
            }
            if (topPokeGo.getResults().size() == 0) {
                List<TopPokeGo> results = this.dao.getTop(gameName, 100);
                topPokeGo.setResults(results);
                topMap.put((Object)gameName, (Object)topPokeGo);
            }
            return topPokeGo.getResults(page, pageSize);
        }
        return this.dao.getTopByPage(gameName, page);
    }

    @Override
    public long getLastReferenceId(String gameName) {
        return this.dao.getLastRefenceId(gameName);
    }

    @Override
    public SlotFreeSpin updateLuotQuaySlotFree(String gameName, String username) {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap slotMap = client.getMap(this.buildKeySlot(gameName));
        int soLuotFree = 0;
        SlotFreeSpin slotModel = new SlotFreeSpin();
        if (slotMap.containsKey((Object)username)) {
            slotModel = (SlotFreeSpin)slotMap.get((Object)username);
            slotModel.setNum(slotModel.getNum() - 1);
            soLuotFree = slotModel.getNum();
            if (soLuotFree <= 0) {
                slotMap.remove((Object)username);
            } else {
                slotMap.put((Object)username, (Object)slotModel);
            }
        }
        return slotModel;
    }

    @Override
    public void logNuDiepVien(long referenceId, String username, long betValue, String linesBetting, String linesWin, String prizesOnLine, short result, long totalPrizes, String time) throws IOException, TimeoutException, InterruptedException {
        LogSlotMachineMessage msg = this.buildLogSlotMsg(Games.NU_DIEP_VIEN.getName(), referenceId, username, betValue, linesBetting, linesWin, prizesOnLine, result, totalPrizes, time);
        this.publishSlotMsg("queue_nu_diep_vien", msg, 138);
    }

    @Override
    public void logAudition(long referenceId, String username, long betValue, String linesBetting, String linesWin, String prizesOnLine, short result, long totalPrizes, String time) throws IOException, TimeoutException, InterruptedException {
        LogSlotMachineMessage msg = this.buildLogSlotMsg(Games.AUDITION.getName(), referenceId, username, betValue, linesBetting, linesWin, prizesOnLine, result, totalPrizes, time);
        this.publishSlotMsg("queue_audition", msg, 8001);
    }
    
    @Override
    public void logChiemtinh(long referenceId, String username, long betValue, String linesBetting, String linesWin, String prizesOnLine, short result, long totalPrizes, String time) throws IOException, TimeoutException, InterruptedException {
        LogSlotMachineMessage msg = this.buildLogSlotMsg(Games.CHIEM_TINH.getName(), referenceId, username, betValue, linesBetting, linesWin, prizesOnLine, result, totalPrizes, time);
        this.publishSlotMsg("queue_chiemtinh", msg, 8010);
    }

    @Override
    public void setLuotQuayFreeSlot(String gameName, String nickName, String lines, int soLuot, int ratio) {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap slotMap = client.getMap(this.buildKeySlot(gameName));
        if (slotMap.containsKey((Object)nickName)) {
            try {
                SlotFreeSpin slotFreeModel = (SlotFreeSpin)slotMap.get((Object)nickName);
                slotFreeModel.setNum(soLuot);
                slotFreeModel.setRatio(ratio);
                slotFreeModel.setLines(lines);
                slotMap.put((Object)nickName, (Object)slotFreeModel);
            }
            catch (Exception e) {}
        } else {
            SlotFreeSpin slotFreeModel = new SlotFreeSpin();
            slotFreeModel.setNum(soLuot);
            slotFreeModel.setRatio(ratio);
            slotFreeModel.setLines(lines);
            slotMap.put((Object)nickName, (Object)slotFreeModel);
        }
    }

    @Override
    public SlotFreeSpin getLuotQuayFreeSlot(String gameName, String nickName) {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap slotMap = client.getMap(this.buildKeySlot(gameName));
        SlotFreeSpin slotFreeSpin = new SlotFreeSpin();
        if (slotMap.containsKey(nickName)) {
            try {
                slotFreeSpin = (SlotFreeSpin)slotMap.get(nickName);
                if(slotFreeSpin == null){
                    System.out.println("error get freeSlot");
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return slotFreeSpin;
    }

    private String buildKeySlot(String gameName) {
        return "cache" + gameName + "FreeSpin";
    }

    @Override
    public void setItemsWild(String gameName, String nickName, String itemsWild) {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap slotMap = client.getMap(this.buildKeySlot(gameName));
        if (slotMap.containsKey((Object)nickName)) {
            try {
                SlotFreeSpin slotFreeSpin = (SlotFreeSpin)slotMap.get((Object)nickName);
                slotFreeSpin.setItemsWild(itemsWild);
                slotMap.put((Object)nickName, (Object)slotFreeSpin);
            }
            catch (Exception slotFreeSpin) {
                // empty catch block
            }
        }
    }

    @Override
    public int getPrizes(String gameName, String nickName) {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap slotMap = client.getMap(this.buildKeySlot(gameName));
        int prizes = 0;
        if (slotMap.containsKey((Object)nickName)) {
            try {
                SlotFreeSpin slotFreeSpin = (SlotFreeSpin)slotMap.get((Object)nickName);
                prizes = slotFreeSpin.getPrizes();
            }
            catch (Exception slotFreeSpin) {
                // empty catch block
            }
        }
        return prizes;
    }

    @Override
    public void addPrizes(String gameName, String nickName, int prize) {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap slotMap = client.getMap(this.buildKeySlot(gameName));
        if (slotMap.containsKey((Object)nickName)) {
            try {
                SlotFreeSpin slotFreeSpin = (SlotFreeSpin)slotMap.get((Object)nickName);
                slotFreeSpin.addPrize(prize);
                slotMap.put((Object)nickName, (Object)slotFreeSpin);
            }
            catch (Exception slotFreeSpin) {
                // empty catch block
            }
        }
    }

    @Override
    public SlotFreeDaily getLuotQuayFreeDaily(String gameName, String nickName, int room) {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        String key = nickName + "-" + gameName + "-" + room;
        IMap slotMap = client.getMap("cacheSlotFree");
        if (slotMap.containsKey((Object)key)) {
            SlotFreeDaily slotModel = (SlotFreeDaily)slotMap.get((Object)key);
            return slotModel;
        }
        return null;
    }

    @Override
    public void setLuotQuayFreeDaily(String gameName, String nickName, int room) {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        String key = nickName + "-" + gameName + "-" + room;
        IMap slotMap = client.getMap("cacheSlotFree");
        if (slotMap.containsKey((Object)key)) {
            try {
                slotMap.lock(nickName);
                SlotFreeDaily slotModel = (SlotFreeDaily)slotMap.get(key);
                if (slotModel.getRotateFree() > 0) {
                    slotModel.setRotateFree(slotModel.getRotateFree() + 1);
                    slotMap.put(key, slotModel);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                slotMap.unlock(nickName);
            }
        }else{
            SlotFreeDaily slotModel = new SlotFreeDaily(1,0);
            slotMap.put(key, slotModel);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean updateLuotQuayFreeDaily(String gameName, String nickName, int room) {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        String key = nickName + "-" + gameName + "-" + room;
        IMap slotMap = client.getMap("cacheSlotFree");
        boolean success = false;
        if (slotMap.containsKey((Object)key)) {
            try {
                slotMap.lock((Object)nickName);
                SlotFreeDaily slotModel = (SlotFreeDaily)slotMap.get((Object)key);
                if (slotModel.getRotateFree() > 0) {
                    slotModel.setRotateFree(slotModel.getRotateFree() - 1);
                    success = true;
                    slotMap.put((Object)key, (Object)slotModel);
//                    this.dao.updateSlotFreeDaily(gameName, nickName, room, slotModel.getRotateFree());
                }
            }
            catch (Exception e) {
                success = false;
            }
            finally {
                slotMap.unlock(nickName);
            }
        }
        return success;
    }

    @Override
    public void logNoHu(long referenceId, String gameName, String nickName, int betValue, String linesBetting, String matrix, String linesWin, String prizesOnLine, long totalPrizes, short result, String time) {
        LogNoHuSlotMessage msg = new LogNoHuSlotMessage();
        msg.referenceId = referenceId;
        msg.gameName = gameName;
        msg.nickName = nickName;
        msg.betValue = betValue;
        msg.linesBetting = linesBetting;
        msg.matrix = matrix;
        msg.linesWin = linesWin;
        msg.prizesOnLine = prizesOnLine;
        msg.totalPrizes = totalPrizes;
        msg.result = result;
        msg.time = time;
        try {
            RMQApi.publishMessage("queue_log_nohu", msg, 140);
        }
        catch (IOException | InterruptedException | TimeoutException ex2) {
            Exception e = ex2;
            e.printStackTrace();
        }
    }

    @Override
    public List<TopPokeGo> getLogNoHu(String gameName, int page) {
        List<TopPokeGo> results = this.dao.getListNoHu(gameName, page);
        return results;
    }

    public void logSamTruyen(long referenceId, String username, long betValue, String linesBetting, String linesWin, String prizesOnLine, short result, long totalPrizes, String time) throws IOException, TimeoutException, InterruptedException {
        LogSlotMachineMessage msg = this.buildLogSlotMsg(Games.SAMTRUYEN.getName(), referenceId, username, betValue, linesBetting, linesWin, prizesOnLine, result, totalPrizes, time);
        this.publishSlotMsg("queue_samtruyen", msg, 10236);
    }

    @Override
    public void logSlot(String gameName, long referenceId, String username, long betValue, String linesBetting, String linesWin, String prizesOnLine, short result, long totalPrizes, String time) throws IOException, TimeoutException, InterruptedException {
        if(gameName.equals(Games.AUDITION.getName())){
            this.logAudition( referenceId,  username,  betValue,  linesBetting,  linesWin,  prizesOnLine,  result,  totalPrizes,  time);
        } else if(gameName.equals(Games.RANGE_ROVER.getName())){
            this.logRangeRover( referenceId,  username,  betValue,  linesBetting,  linesWin,  prizesOnLine,  result,  totalPrizes,  time);
        }
        else if(gameName.equals(Games.MAYBACH.getName())){
            this.logMaybach( referenceId,  username,  betValue,  linesBetting,  linesWin,  prizesOnLine,  result,  totalPrizes,  time);
        }
        else if(gameName.equals(Games.SPARTAN.getName())){
            this.logSpartan( referenceId,  username,  betValue,  linesBetting,  linesWin,  prizesOnLine,  result,  totalPrizes,  time);
        }
        else if(gameName.equals(Games.TAMHUNG.getName())){
            this.logTamHung( referenceId,  username,  betValue,  linesBetting,  linesWin,  prizesOnLine,  result,  totalPrizes,  time);
        }
        else if(gameName.equals(Games.ROLL_ROYE.getName())){
            this.logRollRoye( referenceId,  username,  betValue,  linesBetting,  linesWin,  prizesOnLine,  result,  totalPrizes,  time);
        }
        else if(gameName.equals(Games.BENLEY.getName())){
            this.logBenley( referenceId,  username,  betValue,  linesBetting,  linesWin,  prizesOnLine,  result,  totalPrizes,  time);
        }
        else if(gameName.equals(Games.CHIEM_TINH.getName())){
            this.logChiemtinh( referenceId,  username,  betValue,  linesBetting,  linesWin,  prizesOnLine,  result,  totalPrizes,  time);
        }
    }

    @Override
    public boolean isSetJackpotForUser(String gameName, String userName, int betValue){
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap map = client.getMap("cacheSetUserJackpot");
        String key = gameName+"_"+betValue+"_"+userName;
        if(map.containsKey(key)){
            map.remove(key);
            return true;
        }
        return false;
    }

    @Override
    public void setUserJackpotForGame(String gameName, String userName, int betValue, int gameID){
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap map = client.getMap("cacheSetUserJackpot");
        String key = gameName+"_"+betValue+"_"+userName;
        map.put(key,gameID);
    }

    @Override
    public void deleteUserJackpotForGame(String gameName, String userName, int betValue){
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap map = client.getMap("cacheSetUserJackpot");
        String key = gameName+"_"+betValue+"_"+userName;
        if(map.containsKey(key)){
            map.remove(key);
        }
    }

    @Override
    public Map getListUserCache(){
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap map = client.getMap("cacheSetUserJackpot");
        return map;
    }

//    public int
}

