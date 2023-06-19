package com.vinplay.vbee.rmq.log.processor;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.messages.BaseMessage;
import com.vinplay.vbee.common.messages.LogMoneyUserMessage;
import com.vinplay.vbee.common.models.cache.ReportModel;
import com.vinplay.vbee.common.models.cache.TransactionList;
import com.vinplay.vbee.common.response.LogMoneyUserResponse;
import com.vinplay.vbee.common.rmq.RMQApi;
import com.vinplay.vbee.common.statics.Consts;
import com.vinplay.vbee.common.utils.VinPlayUtils;
import com.vinplay.vbee.main.VBeeMain;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

public class LogMoneyUserExtraProcessor implements BaseProcessor<byte[], Boolean> {
    private static final Logger logger = Logger.getLogger("vbee");

    public Boolean execute(Param<byte[]> param) {
        LogMoneyUserMessage message = (LogMoneyUserMessage)BaseMessage.fromBytes(param.get());
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        if (!message.isBot()) {
            block14 : {
                long transId = 0L;
                int moneyType = -1;
                int queryType = -1;
                if (message.getMoneyType().equalsIgnoreCase("vin")) {
                    transId = VBeeMain.moneyVinReferenceId;
                    moneyType = 1;
                    if (message.getMoneyExchange() > 0L) {
                        if (Consts.NAP_VIN.contains(message.getActionName())) {
                            queryType = 3;
                        }
                    } else if (Consts.TIEU_VIN.contains(message.getActionName())) {
                        queryType = 5;
                    }
                } else {
                    transId = VBeeMain.moneyXuReferenceId;
                    moneyType = 2;
                    if (message.getMoneyExchange() > 0L && Consts.NAP_XU.contains(message.getActionName())) {
                        queryType = 4;
                    }
                }
                LogMoneyUserResponse model = new LogMoneyUserResponse();
                model.transId = transId;
                model.serviceName = message.getServiceName();
                model.description = message.getDescription();
                model.currentMoney = message.getCurrentMoney();
                model.moneyExchange = message.getMoneyExchange();
                model.transactionTime = message.getCreateTime();
                IMap<String, TransactionList> transMap = client.getMap("cacheTransaction");
                this.pushNewTransaction(transMap, message.getNickname(), model, moneyType);

                if(model.serviceName.equalsIgnoreCase("11")){
                    IMap<String, TransactionList> transMapTLMN = client.getMap("cacheHisTLMN");
                    this.pushNewTransaction(transMapTLMN, message.getNickname(), model, moneyType);
                }

                if(model.serviceName.equalsIgnoreCase("15")){
                    IMap<String, TransactionList> transMapXD = client.getMap("cacheHisXD");
                    this.pushNewTransaction(transMapXD, message.getNickname(), model, moneyType);
                }

                if (queryType > 2) {
                    this.pushNewTransaction(transMap, message.getNickname(), model, queryType);
                }
                try {
                    RMQApi.publishMessage("queue_report", message, 701);
                }
                catch (Exception e1) {
                    e1.printStackTrace();
                    if (!message.getMoneyType().equals("vin")) break block14;
                    if (message.getActionName().equals("TaiXiu") && (message.getServiceName().equals("T\u00e0i x\u1ec9u - T\u00e1n l\u1ed9c") || message.getServiceName().equals("T\u00e0i x\u1ec9u - R\u00fat l\u1ed9c"))) {
                        return true;
                    }
                    IMap<String, ReportModel> reportMap = client.getMap("cacheReports");
                    String date = "";
                    try {
                        date = VinPlayUtils.getDateFromDateTime((String)message.getCreateTime());
                    }
                    catch (ParseException e2) {
                        date = VinPlayUtils.getCurrentDate();
                    }
                    this.pushReportMap(reportMap, message.getNickname(), message.getActionName(), date, message.getMoneyExchange(), message.getFee(), message.isVp(), message.isBot());
                }
            }
            return true;
        }
        return true;
    }

    private void pushNewTransaction(IMap<String, TransactionList> transMap, String nickname, LogMoneyUserResponse model, int queryType) {
        String key = String.valueOf(nickname) + "-" + queryType;
        TransactionList tranList = transMap.get(key);
        if (transMap.containsKey(key) && tranList != null) {
            tranList.add(model);
            transMap.put(key, tranList, 72L, TimeUnit.HOURS);
        }
    }

    private void pushReportMap(IMap<String, ReportModel> reportMap, String nickname, String actionname, String date, long money, long fee, boolean playGame, boolean isBot) {
        String key = nickname + "," + actionname + "," + date;
        try {
            if (reportMap.containsKey(key)) {
                try {
                    ReportModel reportModel = (ReportModel)reportMap.get(key);
                    if (playGame) {
                        if (money > 0L) {
                            ReportModel reportModel2 = reportModel;
                            reportModel2.moneyWin += money;
                        } else {
                            ReportModel reportModel3 = reportModel;
                            reportModel3.moneyLost += money;
                        }
                    } else {
                        ReportModel reportModel4 = reportModel;
                        reportModel4.moneyOther += money;
                    }
                    ReportModel reportModel5 = reportModel;
                    reportModel5.fee += fee;
                    reportMap.put(key, reportModel);
                }
                catch (Exception e) {
                    logger.debug(e);
                }
            } else {
                ReportModel reportModel = new ReportModel();
                reportModel.isBot = isBot;
                if (playGame) {
                    if (money > 0L) {
                        reportModel.moneyWin = money;
                    } else {
                        reportModel.moneyLost = money;
                    }
                } else {
                    reportModel.moneyOther = money;
                }
                reportModel.fee = fee;
                reportMap.put(key, reportModel);
            }
        }
        catch (Exception e) {
            logger.error(e);
        }
    }
}

