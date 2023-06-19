package com.vinplay.dailyQuest.model;

import com.vinplay.vbee.common.enums.Games;
import com.vinplay.dailyQuest.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class DailyQuestModel
        implements Serializable {
    private static final long serialVersionUID = 1123581347;
    public long lastTimeChange = 0;
    public ArrayList<DailyGiftData> dailyGiftData = new ArrayList<>();
    public String userName;

    public DailyQuestModel(String userName) {
        this.lastTimeChange = getTimeStampInDay();
        this.userName = userName;
//        this.checkDailyGift();
        for(int i =0;i< DailyQuestConfig.allQuest.length;i++){
            this.dailyGiftData.add(new DailyGiftData());
        }
    }
    public boolean receiveGiftDailyQuest(int index) {
        if (DailyQuestConfig.questActive[index]){
            if (this.dailyGiftData.get(index).receiveGift()) {
                if(DailyQuestConfig.allQuest[index].giftType == GiftType.FREE_SPIN){
                    if(DailyQuestConfig.allQuest[index].giftTypeData.gameID == Games.SPARTAN.getId()){
                        DailyQuestActionReceiveGift.addFreeSpinSpartan(this.userName);
                    }
                    if(DailyQuestConfig.allQuest[index].giftTypeData.gameID == Games.POKE_GO.getId()){
                        DailyQuestActionReceiveGift.addFreeSpinPokeGo(this.userName);
                    }
                    if(DailyQuestConfig.allQuest[index].giftTypeData.gameID == Games.GALAXY.getId()){
                        DailyQuestActionReceiveGift.addFreeSpinGalaxy(this.userName);
                    }
                    if(DailyQuestConfig.allQuest[index].giftTypeData.gameID == Games.BENLEY.getId()){
                        DailyQuestActionReceiveGift.addFreeSpinBenley(this.userName);
                    }
                    if(DailyQuestConfig.allQuest[index].giftTypeData.gameID == Games.AUDITION.getId()){
                        DailyQuestActionReceiveGift.addFreeSpinAudition(this.userName);
                    }
                    if(DailyQuestConfig.allQuest[index].giftTypeData.gameID == Games.TAMHUNG.getId()){
                        DailyQuestActionReceiveGift.addFreeSpinTamHung(this.userName);
                    }
                    if(DailyQuestConfig.allQuest[index].giftTypeData.gameID == Games.MAYBACH.getId()){
                        DailyQuestActionReceiveGift.addFreeSpinMayBach(this.userName);
                    }
                    if(DailyQuestConfig.allQuest[index].giftTypeData.gameID == Games.CHIEM_TINH.getId()){
                        DailyQuestActionReceiveGift.addFreeSpinChiemtinh(this.userName);
                    }
                    if(DailyQuestConfig.allQuest[index].giftTypeData.gameID == Games.ROLL_ROYE.getId()){
                        DailyQuestActionReceiveGift.addFreeSpinThanBai(this.userName);
                    }
                    if(DailyQuestConfig.allQuest[index].giftTypeData.gameID == Games.BIKINI.getId()){
                        DailyQuestActionReceiveGift.addFreeSpinBikini(this.userName);
                    }
                }
                if(DailyQuestConfig.allQuest[index].giftType == GiftType.MONEY){
                    if(DailyQuestConfig.allQuest[index].gameId == Games.NHIEM_VU.getId()){ //nap the
                        DailyQuestActionReceiveGift.addMoney(this.userName,
                                DailyQuestConfig.allQuest[index].gift, DailyQuestDescriptionAction.ACTION_NAP_THE);
                    }

                    if(DailyQuestConfig.allQuest[index].gameId == Games.TAI_XIU.getId()){ //nap the
                        DailyQuestActionReceiveGift.addMoney(this.userName,
                                DailyQuestConfig.allQuest[index].gift, DailyQuestDescriptionAction.ACTION_TAI_XIU);
                    }

                    if(DailyQuestConfig.allQuest[index].gameId == Games.BAU_CUA.getId()){ //nap the
                        DailyQuestActionReceiveGift.addMoney(this.userName,
                                DailyQuestConfig.allQuest[index].gift, DailyQuestDescriptionAction.ACTION_BAU_CUA);
                    }
                }
                return true;
            }
        }
        return false;
    }

    public void playGame(int gameId, long value) {
        for (int i = 0; i < this.dailyGiftData.size(); i++) {
            if (DailyQuestConfig.questActive[i] && DailyQuestConfig.allQuest[i].gameId == gameId){
                this.dailyGiftData.get(i).currentValue += value;
                if (this.dailyGiftData.get(i).currentValue >= DailyQuestConfig.allQuest[i].valueDone &&
                        !this.dailyGiftData.get(i).isReceive && !this.dailyGiftData.get(i).isSuccess) {
                    this.dailyGiftData.get(i).isSuccess = true;
                }
            }
        }
    }

    public void playerLogin() {
        long currentTime = getTimeStampInDay();
        long deltaDay = (currentTime - this.lastTimeChange);
        if (deltaDay > 0) {
            this.lastTimeChange = currentTime;
            for (int i = 0; i < this.dailyGiftData.size(); i++) {
                this.dailyGiftData.get(i).resetData();
            }
        }
    }

    public static long getTimeStampInDay() {
        Calendar time = Calendar.getInstance();
        time.add(Calendar.MILLISECOND, time.getTimeZone().getOffset(time.getTimeInMillis()));
        return time.getTimeInMillis()/(86400000);
    }

    //    public void checkDailyGift() {
//        //them daily gift vao thi se add lai nhung cai da co
//        if (this.dailyGiftData.size() < DailyQuestConfig.allQuest.length) {
//            while (this.dailyGiftData.size() < DailyQuestConfig.allQuest.length) {
//                this.dailyGiftData.add(this.createDailyGiftData(this.dailyGiftData.size()));
//            }
//        }
//    }

//    public DailyGiftData createDailyGiftData(int index) {
//        DailyGiftData dailyGiftData = new DailyGiftData();
//        int gameId = DailyQuestConfig.allQuest[index].gameId;
//        for (int i = 0; i < this.dailyGiftData.size(); i++) {
//            DailyGiftData dailyGiftData1 = this.dailyGiftData.get(i);
//            if (DailyQuestConfig.allQuest[i].gameId == gameId) {
//                dailyGiftData.currentValue = dailyGiftData1.currentValue;
//            }
//        }
//        return dailyGiftData;
//    }

}
