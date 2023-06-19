/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.hazelcast.core.HazelcastInstance
 *  com.hazelcast.core.IMap
 *  com.vinplay.vbee.common.hazelcast.HazelcastClientFactory
 *  com.vinplay.vbee.common.models.cache.ReportModel
 *  com.vinplay.vbee.common.utils.VinPlayUtils
 */
package com.vinplay.dal.service.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.dal.dao.LogMoneyUserDao;
import com.vinplay.dal.dao.impl.LogMoneyUserDaoImpl;
import com.vinplay.dal.entities.report.ReportMoneySystemModel;
import com.vinplay.dal.service.GameBaiService;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.models.cache.ReportModel;
import com.vinplay.vbee.common.models.cache.TransactionList;
import com.vinplay.vbee.common.response.LogMoneyUserResponse;
import com.vinplay.vbee.common.utils.VinPlayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GameBaiServiceImpl
implements GameBaiService {
    @Override
    public ReportMoneySystemModel getReportGameToday(String gameName) {
        ReportMoneySystemModel res = new ReportMoneySystemModel();
        String today = VinPlayUtils.getCurrentDate();
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, ReportModel> reportMap = client.getMap("cacheReports");
        for (Map.Entry<String, ReportModel> entry : reportMap.entrySet()) {
            String actionname;
            if (!((String)entry.getKey()).contains(today) || !(actionname = ((String)entry.getKey()).split(",")[1]).equals(gameName)) continue;
            ReportModel model = (ReportModel)entry.getValue();
            if (model.isBot) continue;
            ReportMoneySystemModel reportMoneySystemModel = res;
            reportMoneySystemModel.moneyWin += model.moneyWin;
            ReportMoneySystemModel reportMoneySystemModel2 = res;
            reportMoneySystemModel2.moneyLost += model.moneyLost;
            ReportMoneySystemModel reportMoneySystemModel3 = res;
            reportMoneySystemModel3.moneyOther += model.moneyOther;
            ReportMoneySystemModel reportMoneySystemModel4 = res;
            reportMoneySystemModel4.fee += model.fee;
            ReportMoneySystemModel reportMoneySystemModel5 = res;
            reportMoneySystemModel5.revenuePlayGame += model.moneyWin + model.moneyLost;
            ReportMoneySystemModel reportMoneySystemModel6 = res;
            reportMoneySystemModel6.revenue += model.moneyWin + model.moneyLost + model.moneyOther;
        }
        return res;
    }

    @Override
    public List<LogMoneyUserResponse> getLSGD(String nickName, int page) {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, TransactionList> transMap = client.getMap("cacheHisTLMN");
        int moneyType = 1;
        String key = nickName + "-" + moneyType;
        List<LogMoneyUserResponse> result = null;
        if (page <= 5) {
            if (transMap.containsKey(key)) {
                TransactionList tranList =  transMap.get( key);
                result = tranList.get(page);
                if (result.size() == 0 && !tranList.isUpdated()) { // neu no het size thi sao ?
                    this.pushHistoryTransactionDBToCache(transMap, nickName, moneyType);
                    tranList.isUpdate = true;
                    result = tranList.get(page);
                }
            }else{
                result = this.pushHistoryTransactionDBToCache(transMap, nickName, moneyType);
            }
            return result;
        }

        return result;
    }

    @Override
    public List<LogMoneyUserResponse> getLSXD(String nickName, int page) {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, TransactionList> transMap = client.getMap("cacheHisXD");
        int moneyType = 1;
        String key = nickName + "-" + moneyType;
        List<LogMoneyUserResponse> result = null;
        if (page <= 5) {
            if (transMap.containsKey(key)) {
                TransactionList tranList =  transMap.get( key);
                result = tranList.get(page);
                if (result.size() == 0 && !tranList.isUpdated()) { // neu no het size thi sao ?
                    this.pushXDHistoryTransactionDBToCache(transMap, nickName, moneyType);
                    tranList.isUpdate = true;
                    result = tranList.get(page);
                }
            }else{
                result = this.pushXDHistoryTransactionDBToCache(transMap, nickName, moneyType);
            }
            return result;
        }

        return result;
    }

    public List<LogMoneyUserResponse> pushHistoryTransactionDBToCache(IMap<String, TransactionList> transMap, String nickname, int queryType) {
        List<LogMoneyUserResponse> result = new ArrayList<LogMoneyUserResponse>();
//        ArrayList<LogMoneyUserResponse> result = new ArrayList();
        String key = nickname + "-" + queryType;
        LogMoneyUserDao dao = new LogMoneyUserDaoImpl();
        result = dao.getHisTLMN(nickname, queryType, 0, 65);
        TransactionList tranList = new TransactionList();
        tranList.setList(result);
        result = tranList.get(1);
        transMap.put(key, tranList, 72L, TimeUnit.HOURS);
        return result;
    }

    public List<LogMoneyUserResponse> pushXDHistoryTransactionDBToCache(IMap<String, TransactionList> transMap, String nickname, int queryType) {
        List<LogMoneyUserResponse> result = new ArrayList<LogMoneyUserResponse>();
        String key = nickname + "-" + queryType;
        LogMoneyUserDao dao = new LogMoneyUserDaoImpl();
        result = dao.getHisXD(nickname, queryType, 0, 65);
        TransactionList tranList = new TransactionList();
        tranList.setList(result);
        result = tranList.get(1);
        transMap.put(key, tranList, 72L, TimeUnit.HOURS);
        return result;
    }
}

