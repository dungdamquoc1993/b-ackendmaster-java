package com.vinplay.game.XocDia;

import bitzero.util.common.business.Debug;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.game.XocDia.XocDiaHistoryItem;
import com.vinplay.vbee.common.enums.Games;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;

public class XocDiaSoiCauUtil {

    public static XocDiaHistoryModel getListSoiCau(){
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap map = client.getMap("cacheTop");
        String key = Games.XOC_DIA.getId()+"";
        if(map.containsKey(key)){
            return (XocDiaHistoryModel) map.get(key);
        }else{
            XocDiaHistoryModel xocDiaHistoryModel = new XocDiaHistoryModel();
            map.put(key, xocDiaHistoryModel);
            return xocDiaHistoryModel;
        }
    }

    public static synchronized void addListSoiCau(long refID, byte[] result){
        XocDiaHistoryItem xocDiaHistoryItem = new XocDiaHistoryItem(refID,result);
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap map = client.getMap("cacheTop");
        String key = Games.XOC_DIA.getId()+"";
        try {
            if(map.containsKey(key)){
                map.lock(key);
                XocDiaHistoryModel xocDiaHistoryModel = (XocDiaHistoryModel) map.get(key);
                xocDiaHistoryModel.add(xocDiaHistoryItem);
            }else{
                XocDiaHistoryModel xocDiaHistoryModel = new XocDiaHistoryModel();
                xocDiaHistoryModel.add(xocDiaHistoryItem);
                map.put(key, xocDiaHistoryModel);
            }
        }catch (Exception e){
            Debug.trace(e);
        }finally {
            map.unlock(key);
        }

    }
}
