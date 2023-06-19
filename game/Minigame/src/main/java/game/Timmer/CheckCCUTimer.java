package game.Timmer;//package game.Timmer;
//
//import bitzero.server.BitZeroServer;
//import com.hazelcast.core.HazelcastInstance;
//import com.hazelcast.core.IMap;
//import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
//import game.GameConfig.GameConfig;
//
//public class CheckCCUTimer  implements Runnable {
//    public static final String keyMap = "mapCheckCCU";
//    @Override
//    public void run() {
//        int ccu = BitZeroServer.getInstance().getUserManager().getUserCount();
//        HazelcastInstance client = HazelcastClientFactory.getInstance();
//        if (client != null) {
//            IMap map = client.getMap(keyMap);
//            map.put(keyMap,ccu);
//        }
//
//    }
//
//}