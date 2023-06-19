package com.vinplay.dal.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.dal.service.BroadcastMessageService;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.models.BroadcastMsgEntry;
import java.util.ArrayList;
import java.util.List;

public class BroadcastMessageServiceImpl
implements BroadcastMessageService {
    private static int MAX_SIZE = 20;
    public static int MIN_MONEY = 10000;
    private static final String KEY_BROADCAST = "keyBroadcast";

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void putMessage(int gameId, String nickname, long money) {
        if (money >= MIN_MONEY) {
            BroadcastMsgEntry newEntry = new BroadcastMsgEntry(gameId, nickname, money);
            HazelcastInstance client = HazelcastClientFactory.getInstance();
            IMap map = client.getMap("cacheBroadcast");
            if (map != null && map.containsKey(KEY_BROADCAST)) {
                map.lock(KEY_BROADCAST);
                try {
                    BroadcastMsgEntry minEntry;
                    List entries = (List)map.get(KEY_BROADCAST);
                    if (entries.size() < MAX_SIZE) {
                        this.add(entries, newEntry);
                    } else if (entries.size() == MAX_SIZE && (minEntry = (BroadcastMsgEntry)entries.get(entries.size() - 1)).getM() < money) {
                        entries.remove(entries.size() - 1);
                        this.add(entries, newEntry);
                    }
                    map.put(KEY_BROADCAST, entries);
                }
                catch (Exception entries) {
                }
                finally {
                    map.unlock(KEY_BROADCAST);
                }
            } else {
                ArrayList<BroadcastMsgEntry> entries = new ArrayList<BroadcastMsgEntry>();
                entries.add(newEntry);
                map.put(KEY_BROADCAST, new ArrayList(entries));
            }
        }
    }

    private void add(List<BroadcastMsgEntry> entries, BroadcastMsgEntry newEntry) {
        int index = -1;
        for (int i = 0; i < entries.size(); ++i) {
            BroadcastMsgEntry entry = entries.get(i);
            if (entry.getM() >= newEntry.getM()) continue;
            index = i;
            break;
        }
        if (index > -1) {
            entries.add(index, newEntry);
        } else {
            entries.add(newEntry);
        }
    }

    @Override
    public String toJson() {
        try {
            HazelcastInstance client = HazelcastClientFactory.getInstance();
            IMap map = client.getMap("cacheBroadcast");
            List entries = (List)map.get(KEY_BROADCAST);
            BroadcastMessageServiceImpl this$0 = new BroadcastMessageServiceImpl();
            this$0.getClass();
            BroadcastMsgModel model = this$0.new BroadcastMsgModel();
            model.setEntries(entries);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(model);
        }
        catch (JsonProcessingException e) {
            return "{\"success\":false,\"errorCode\":\"1001\"}";
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void clearMessage() {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap map = client.getMap("cacheBroadcast");
        if (map != null && map.containsKey(KEY_BROADCAST)) {
            map.lock(KEY_BROADCAST);
            try {
                List entries = (List)map.get(KEY_BROADCAST);
                entries.clear();
                map.put(KEY_BROADCAST, entries);
            }
            catch (Exception entries) {
            }
            finally {
                map.unlock(KEY_BROADCAST);
            }
        }
    }

    public class BroadcastMsgModel {
        private List<BroadcastMsgEntry> entries = new ArrayList<BroadcastMsgEntry>();

        public List<BroadcastMsgEntry> getEntries() {
            return this.entries;
        }

        public void setEntries(List<BroadcastMsgEntry> entries) {
            this.entries = entries;
        }
    }

}

