/*
 * Decompiled with CFR 0.144.
 *
 * Could not load the following classes:
 *  com.hazelcast.core.HazelcastInstance
 *  com.hazelcast.core.IMap
 *  com.vinplay.vbee.common.hazelcast.HazelcastClientFactory
 *  com.vinplay.vbee.common.messages.gamebai.LogNoHuGameBaiMessage
 *  com.vinplay.vbee.common.models.cache.TransactionList
 *  com.vinplay.vbee.common.response.LogMoneyUserResponse
 *  com.vinplay.vbee.common.response.LogUserMoneyResponse
 */
package com.vinplay.dal.service.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.dal.dao.LogMoneyUserDao;
import com.vinplay.dal.dao.impl.LogMoneyUserDaoImpl;
import com.vinplay.dal.service.LogMoneyUserService;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.messages.gamebai.LogNoHuGameBaiMessage;
import com.vinplay.vbee.common.models.cache.TransactionList;
import com.vinplay.vbee.common.response.LogMoneyUserResponse;
import com.vinplay.vbee.common.response.LogUserMoneyResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LogMoneyUserServiceImpl implements LogMoneyUserService {
    private LogMoneyUserDao dao = new LogMoneyUserDaoImpl();

    @Override
    public List<LogUserMoneyResponse> searchLogMoneyUser(String nickName, String userName, String moneyType, String serviceName, String actionName, String timeStart, String timeEnd, int page, int like, int totalRecord) {
        //LogMoneyUserDaoImpl dao = new LogMoneyUserDaoImpl();
        List<LogUserMoneyResponse> result = null;
        result = dao.searchLogMoneyUser(nickName, userName, moneyType, serviceName, actionName, timeStart, timeEnd, page, like, totalRecord);
        return result;
    }

    public List<LogUserMoneyResponse> searchLogMoneyUser(String nickName, String referral_code, String serviceName, String actionName, String timeStart,
                                                         String timeEnd, int page, int totalRecord) {
        //LogMoneyUserDaoImpl dao = new LogMoneyUserDaoImpl();
        List<LogUserMoneyResponse> result = null;
        result = dao.searchLogMoneyUser(nickName, referral_code, serviceName, actionName, timeStart, timeEnd, page, totalRecord);
        return result;
    }

    @Override
    public int countsearchLogMoneyUser(String nickName, String moneyType, String serviceName, String actionName, String timeStart, String timeEnd, int like) {
        //LogMoneyUserDaoImpl dao = new LogMoneyUserDaoImpl();
        int totalRecords = dao.countsearchLogMoneyUser(nickName, moneyType, serviceName, actionName, timeStart, timeEnd, like);
        return totalRecords / 100 + 1;
    }

    @Override
    public List<LogMoneyUserResponse> getHistoryTransactionLogMoney(String nickName, int moneyType, int page) {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, TransactionList> transMap = client.getMap("cacheTransaction");
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

        LogMoneyUserDao dao = new LogMoneyUserDaoImpl();
        result = dao.getHistoryTransactionLogMoney(nickName, moneyType, page);

        return result;
    }

    @Override
    public int countHistoryTransactionLogMoney(String nickname, int moneyType) {
        LogMoneyUserDaoImpl dao = new LogMoneyUserDaoImpl();
        int totalRecords = dao.countHistoryTransactionLogMoney(nickname, moneyType);
        return totalRecords / 13 + 1;
    }

    @Override
    public List<LogNoHuGameBaiMessage> getNoHuGameBaiHistory(int pageNumber, String gameName) {
        LogMoneyUserDaoImpl dao = new LogMoneyUserDaoImpl();
        return dao.getNoHuGameBaiHistory(pageNumber, gameName);
    }

    @Override
    public int countNoHuGameBaiHistory() {
        LogMoneyUserDaoImpl dao = new LogMoneyUserDaoImpl();
        return dao.countNoHuGameBaiHistory();
    }

//    @Override
//    public List<LogUserMoneyResponse> searchLogMoneyTranferUser(String nickName, String timestart, String timeEnd, String type, int page) {
//        LogMoneyUserDaoImpl dao = new LogMoneyUserDaoImpl();
//        return dao.searchLogMoneyTranferUser(nickName, timestart, timeEnd, type, page);
//    }

//    @Override
//    public boolean UpdateProcessLogChuyenTienDaiLy(String nickNameSend, String nickNameReceive, String timeLog, String Status) {
//        try {
//            LogMoneyUserDaoImpl dao = new LogMoneyUserDaoImpl();
//            dao.UpdateProcessLogChuyenTienDaiLy(nickNameSend, nickNameReceive, timeLog, Status);
//            dao.UpdateProcessLogChuyenTienDaiLyMySQL(nickNameSend, nickNameReceive, timeLog, Status);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        return true;
//    }

    @Override
    public List<LogMoneyUserResponse> pushHistoryTransactionDBToCache(IMap<String, TransactionList> transMap, String nickname, int queryType) {
        List<LogMoneyUserResponse> result = new ArrayList<LogMoneyUserResponse>();
//        ArrayList<LogMoneyUserResponse> result = new ArrayList();
        String key = nickname + "-" + queryType;
        LogMoneyUserDao dao = new LogMoneyUserDaoImpl();
        result = dao.getTransactionList(nickname, queryType, 0, 65);
        TransactionList tranList = new TransactionList();
        tranList.setList(result);
        result = tranList.get(1);
        transMap.put(key, tranList, 72L, TimeUnit.HOURS);
        return result;
    }
}

