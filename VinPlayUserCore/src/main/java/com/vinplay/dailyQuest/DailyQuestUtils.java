package com.vinplay.dailyQuest;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.dailyQuest.model.DailyQuestModel;

public class DailyQuestUtils {
    public static DailyQuestModel getDailyQuestModel(String userName) {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap slotMap = client.getMap("dailyQuestCache");
        String key = userName;
        if (slotMap.containsKey(userName)) {
            return (DailyQuestModel) slotMap.get(key);
        } else {
            DailyQuestModel dailyQuestModel = new DailyQuestModel(userName);
            slotMap.put(key, dailyQuestModel);
            return dailyQuestModel;
        }
    }

    public static boolean playerReceiveGift(String userName, int index) {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap slotMap = client.getMap("dailyQuestCache");
        String key = userName;
        boolean check = true;
        if (slotMap.containsKey(userName)) {
            try {
                slotMap.lock(userName);
                DailyQuestModel dailyQuestModel = (DailyQuestModel) slotMap.get(key);
                check = dailyQuestModel.receiveGiftDailyQuest(index);
                slotMap.put(userName, dailyQuestModel);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                slotMap.unlock(userName);
            }

        }
        return check;
    }

    public static void playerPlayGame(String userName, int gameID, long value) {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap slotMap = client.getMap("dailyQuestCache");
        String key = userName;
        if (slotMap.containsKey(userName)) {
            try {
                slotMap.lock(userName);
                DailyQuestModel dailyQuestModel = (DailyQuestModel) slotMap.get(key);
                dailyQuestModel.playGame(gameID, value);
                slotMap.put(userName, dailyQuestModel);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                slotMap.unlock(userName);
            }

        }
    }

    public static void playerLogin(String userName) {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap slotMap = client.getMap("dailyQuestCache");
        String key = userName;
        if (slotMap.containsKey(userName)) {
            try {
                slotMap.lock(userName);
                DailyQuestModel dailyQuestModel = (DailyQuestModel) slotMap.get(key);
                dailyQuestModel.playerLogin();
                slotMap.put(userName, dailyQuestModel);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                slotMap.unlock(userName);
            }

        } else {
            DailyQuestModel dailyQuestModel = new DailyQuestModel(userName);
            slotMap.put(key, dailyQuestModel);
        }
    }
}
