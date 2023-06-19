//package com.vinplay.vbee.rmq.loguserinday;
//
//import com.hazelcast.core.HazelcastInstance;
//import com.hazelcast.core.IMap;
//import com.hazelcast.transaction.TransactionContext;
//import com.hazelcast.transaction.TransactionOptions;
//import com.vinplay.dal.entities.report.LogCountUserPlay;
//import com.vinplay.dal.entities.report.LogReportModel;
//import com.vinplay.vbee.common.cp.BaseProcessor;
//import com.vinplay.vbee.common.cp.Param;
//import com.vinplay.vbee.common.enums.Games;
//import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
//import com.vinplay.vbee.common.messages.BaseMessage;
//import com.vinplay.vbee.common.messages.LogMoneyUserMessage;
//import com.vinplay.vbee.common.statics.Consts;
//import com.vinplay.vbee.common.utils.VinPlayUtils;
//
//import java.util.logging.Logger;
//
//public class LogSumReportUserProcessor
//        implements BaseProcessor<byte[], Boolean> {
//    private static final Logger logger = Logger.getLogger((String) "vbee");
//
//    public static final Object lock = new Object();
//    public Boolean execute(Param<byte[]> param) {
//        LogMoneyUserMessage message = (LogMoneyUserMessage) BaseMessage.fromBytes(param.get());
//        if (!message.isBot()) {
//            try {
//                this.getDataUserCache(message);
//            } catch (Exception e) {
////                logger.log(e);
//            }
//        }
//        return true;
//    }
//
//
//    public void getDataUserCache(LogMoneyUserMessage message) {
//        String time = VinPlayUtils.getCurrentDateMarketing();//LogReportUtil.getTimeStampInDay();
//        HazelcastInstance client = HazelcastClientFactory.getInstance();
//        if (client == null) {
//            return;
//        }
//        IMap<String, LogReportModel> dataMap = client.getMap("cacheUserSumLog");
//        String key = message.getNickname() + "|_|" + time;
//        IMap<String, LogCountUserPlay> mapLogCountUserPlay = client.getMap("cacheUserCountLog");
//        boolean writeNewData = false;
//        boolean writeNewDataCount = false;
//        if (!dataMap.containsKey(key)) {
//            // lay data ra, neu data = null tao data moi roi cho vao cache
//            synchronized (lock){
//                if (!dataMap.containsKey(key)){
//                    LogReportModel logReportModel = LogReportUtil.getLogReportModel(message.getNickname(), time);
//                    if (logReportModel.id == 0) {// chua co trong database
//                        logReportModel.nick_name = message.getNickname();
//                        logReportModel.time = time;
//                        writeNewData = true;
//                    }
//                    dataMap.put(key, logReportModel);
//                }
//            }
//        }
//        if(!mapLogCountUserPlay.containsKey(key)){
//            synchronized (lock) {
//                if (!mapLogCountUserPlay.containsKey(key)) {
//                    LogCountUserPlay logCountUserPlay = LogCountReportUtil.getLogReportModelSQL(message.getNickname(),time);
//                    if(logCountUserPlay.id == 0){
//                        logCountUserPlay.nick_name = message.getNickname();
//                        logCountUserPlay.time = time;
//                        writeNewDataCount = true;
//                    }
//                    mapLogCountUserPlay.put(key, logCountUserPlay);
//                }
//            }
//        }
//        TransactionContext context = client.newTransactionContext(new TransactionOptions().setTransactionType(TransactionOptions.TransactionType.ONE_PHASE));
//        context.beginTransaction();
//        try {
//            dataMap.lock(key);
//            LogReportModel logReportModel = dataMap.get(key);
//            LogCountUserPlay logCountUserPlay = mapLogCountUserPlay.get(key);
//            int serviceID = Integer.parseInt(message.getServiceName());
//            // cong tru trong cache
//            if (serviceID == Games.TAI_XIU.getId()) {
//                // can phai tinh tien tra lai tien cuoc
//                // check service name = TaiXiuHoanTien
//                if (message.getMoneyExchange() < 0) {
//                    logReportModel.taixiu += Math.abs(message.getMoneyExchange());
//                    logCountUserPlay.taixiu++;
//                } else {
//                    if (message.getServiceName().equalsIgnoreCase("TaiXiuHoanTien")) {
//                        logReportModel.taixiu -= Math.abs(message.getMoneyExchange());
//                    } else {
//                        logReportModel.taixiu_win += Math.abs(message.getMoneyExchange());
//                    }
//                }
//            } else if (serviceID == Games.BAU_CUA.getId()) {
//                if (message.getMoneyExchange() < 0) {
//                    logReportModel.baucua += Math.abs(message.getMoneyExchange());
//                    logCountUserPlay.baucua++;
//                } else {
//                    logReportModel.baucua_win += Math.abs(message.getMoneyExchange());
//                }
//            } else if (serviceID == Games.TLMN.getId()) {
//                if (message.getMoneyExchange() < 0) {
//                    logReportModel.tlmn += message.getMoneyExchange();
//                }else{
//                    logReportModel.tlmn_win += message.getMoneyExchange();
//                }
//
//                logCountUserPlay.tlmn++;
//            } else if (serviceID == Games.BA_CAY.getId()) {
//                if (message.getMoneyExchange() < 0) {
//                    logReportModel.bacay += Math.abs(message.getMoneyExchange());
//                } else {
//                    logReportModel.bacay_win += Math.abs(message.getMoneyExchange());
//                }
//            } else if (serviceID == Games.XOC_DIA.getId()) {
//                if (message.getMoneyExchange() < 0) {
//                    logReportModel.xocdia += Math.abs(message.getMoneyExchange());
//                } else {
//                    logReportModel.xocdia_win += Math.abs(message.getMoneyExchange());
//                }
//            } else if (serviceID == Games.MINI_POKER.getId()) {
//                if (message.getMoneyExchange() < 0) {
//                    logReportModel.minipoker += Math.abs(message.getMoneyExchange());
//                } else {
//                    logReportModel.minipoker_win += Math.abs(message.getMoneyExchange());
//                }
//            } else if (serviceID == Games.POKE_GO.getId()) { // pokemon
//                if (message.getMoneyExchange() < 0) {
//                    logReportModel.slot_pokemon += Math.abs(message.getMoneyExchange());
//                } else {
//                    logReportModel.slot_pokemon_win += Math.abs(message.getMoneyExchange());
//                }
//            } else if (serviceID == Games.CAO_THAP.getId()) {
//                if (message.getMoneyExchange() < 0) {
//                    logReportModel.caothap += Math.abs(message.getMoneyExchange());
//                } else {
//                    logReportModel.caothap_win += Math.abs(message.getMoneyExchange());
//                }
//            } else if (serviceID == Games.BENLEY.getId()) { // bitcoin
//                if (message.getMoneyExchange() < 0) {
//                    logReportModel.slot_bitcoin += Math.abs(message.getMoneyExchange());
//                } else {
//                    logReportModel.slot_bitcoin_win += Math.abs(message.getMoneyExchange());
//                }
//            } else if (serviceID == Games.AUDITION.getId()) { // taydu
//                if (message.getMoneyExchange() < 0) {
//                    logReportModel.slot_taydu += Math.abs(message.getMoneyExchange());
//                } else {
//                    logReportModel.slot_taydu_win += Math.abs(message.getMoneyExchange());
//                }
//            } else if (serviceID == Games.TAMHUNG.getId()) { // angrybird
//                if (message.getMoneyExchange() < 0) {
//                    logReportModel.slot_angrybird += Math.abs(message.getMoneyExchange());
//                } else {
//                    logReportModel.slot_angrybird_win += Math.abs(message.getMoneyExchange());
//                }
//            } else if (serviceID == Games.SPARTAN.getId()) { // than tai
//                if (message.getMoneyExchange() < 0) {
//                    logReportModel.slot_thantai += Math.abs(message.getMoneyExchange());
//                } else {
//                    logReportModel.slot_thantai_win += Math.abs(message.getMoneyExchange());
//                }
//            } else if (serviceID == Games.MAYBACH.getId()) { // the thao
//                if (message.getMoneyExchange() < 0) {
//                    logReportModel.slot_thethao += Math.abs(message.getMoneyExchange());
//                } else {
//                    logReportModel.slot_thethao_win += Math.abs(message.getMoneyExchange());
//                }
//            } else if (Consts.NAP_VIN.contains(message.getActionName())) {
//                if (message.getMoneyExchange() > 0L) {
//                    logReportModel.deposit += Math.abs(message.getMoneyExchange());
//                }
//            } else if (Consts.TIEU_VIN.contains(message.getActionName())) {
//                logReportModel.withdraw += Math.abs(message.getMoneyExchange());
//            }
//            dataMap.put(key, logReportModel);
//            context.commitTransaction();
//            // thuc hien lenh write vao database
//            if (writeNewData) { // tao row moi
//                boolean insert = LogReportUtil.insertNewLog(logReportModel, message.getUserId());
//            } else { // update data
//                boolean update = LogReportUtil.updateLog(logReportModel, message.getUserId());
//            }
//        } catch (Exception e) {
////            logger.log(e);
//            context.rollbackTransaction();
//        } finally {
//            dataMap.unlock(key);
//
//        }
//
//    }
//
//}
