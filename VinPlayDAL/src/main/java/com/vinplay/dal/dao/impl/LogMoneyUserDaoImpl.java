package com.vinplay.dal.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vinplay.dal.dao.UserDao;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.util.JSON;
import com.vinplay.dal.dao.LogMoneyUserDao;
import com.vinplay.dal.entities.gamebai.TopGameBaiModel;
import com.vinplay.dal.entities.log.LogMoneyUserNapTieuVinModel;
import com.vinplay.dal.entities.log.LogMoneyUserVinModel;
import com.vinplay.vbee.common.messages.gamebai.LogNoHuGameBaiMessage;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.common.pools.ConnectionPool;
import com.vinplay.vbee.common.response.LogMoneyUserResponse;
import com.vinplay.vbee.common.response.LogUserMoneyResponse;
import com.vinplay.vbee.common.statics.Consts;
import com.vinplay.vbee.common.statics.MongoCollections;
import com.vinplay.vbee.common.utils.UserUtil;

public class LogMoneyUserDaoImpl
implements LogMoneyUserDao {
    @Override
    public List<LogUserMoneyResponse> searchLogMoneyUser(String nickName, String userName, String moneyType, String serviceName, String actionName, String timeStart, String timeEnd, int page, int like, int totalRecord) {
        List<LogUserMoneyResponse> results = new ArrayList<LogUserMoneyResponse>();
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        FindIterable iterable = null;
        BasicDBObject obj = new BasicDBObject();
        int numStart = (page - 1) * totalRecord;
        if (nickName != null && !nickName.equals("")) {
            conditions.put("nick_name", nickName);
        }
        if (userName != null && !userName.equals("")) {
            conditions.put("user_name", userName);
        }
        if (actionName != null && !actionName.equals("")) {
            conditions.put("action_name", actionName);
        }
        if (serviceName != null && !serviceName.equals("")) {
            conditions.put("service_name", serviceName);
        }
        if (timeStart != null && !timeStart.equals("") && timeEnd != null && !timeEnd.equals("")) {
            obj.put("$gte", timeStart);
            obj.put("$lte", timeEnd);
            conditions.put("trans_time", obj);
        }
        if (moneyType.equals("vin")) {
            iterable = db.getCollection(MongoCollections.LOG_MONEY_USER_VIN).find(new Document(conditions)).skip(numStart).limit(totalRecord);
        } else if (moneyType.equals("xu")) {
            BasicDBObject objsort = new BasicDBObject();
            objsort.put("_id", -1);
            iterable = db.getCollection("log_money_user_xu").find(new Document(conditions)).sort(objsort).skip(numStart).limit(totalRecord);
        }
        iterable.forEach(new Block<Document>(){

            public void apply(Document document) {
                LogUserMoneyResponse tranlogmoney = new LogUserMoneyResponse();
                tranlogmoney.nickName = document.getString("nick_name");
                tranlogmoney.serviceName = document.getString("service_name");
                tranlogmoney.currentMoney = document.getLong("current_money");
                tranlogmoney.moneyExchange = document.getLong("money_exchange");
                tranlogmoney.description = document.getString("description");
                tranlogmoney.transactionTime = document.getString("trans_time");
                tranlogmoney.actionName = document.getString("action_name");
                tranlogmoney.fee = document.getLong("fee");
                results.add(tranlogmoney);
            }
        });
        return results;
    }
    @Override
    public List<LogUserMoneyResponse> searchLogMoneyUser(String nickName, String referral_code, String serviceName, String actionName, String timeStart, String timeEnd, int page, int totalRecord) {
        final ArrayList<LogUserMoneyResponse> results = new ArrayList<LogUserMoneyResponse>();
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        FindIterable iterable = null;
        BasicDBObject obj = new BasicDBObject();
        int numStart = (page - 1) * totalRecord;
        if (nickName != null && !nickName.equals("")) {
            conditions.put("nick_name", nickName);
        }

        if (actionName != null && !actionName.equals("")) {
            conditions.put("action_name", actionName);
        }
        if (serviceName != null && !serviceName.equals("")) {
            conditions.put("service_name", serviceName);
        }
        //conditions.put("is_bot", false);
        if (timeStart != null && !timeStart.equals("") && timeEnd != null && !timeEnd.equals("")) {
            obj.put("$gte", timeStart+" 00:00:00");
            obj.put("$lte", timeEnd+ " 23:59:59");
            conditions.put("trans_time", obj);
        }
        if(numStart > -1 && totalRecord > -1){
            iterable = db.getCollection(MongoCollections.LOG_MONEY_USER_VIN).find(new Document(conditions)).skip(numStart).limit(totalRecord);
        }else{
            iterable = db.getCollection(MongoCollections.LOG_MONEY_USER_VIN).find(new Document(conditions));
        }

        iterable.forEach((Block)new Block<Document>(){

            public void apply(Document document) {
                LogUserMoneyResponse tranlogmoney = new LogUserMoneyResponse();
                tranlogmoney.user_id = document.getInteger("user_id");
                tranlogmoney.nickName = document.getString("nick_name");
                tranlogmoney.serviceName = document.getString("service_name");
                tranlogmoney.currentMoney = document.getLong("current_money");
                tranlogmoney.moneyExchange = document.getLong("money_exchange");
                tranlogmoney.description = document.getString("description");
                tranlogmoney.transactionTime = document.getString("trans_time");
                tranlogmoney.actionName = document.getString("action_name");
                tranlogmoney.fee = document.getLong("fee");
                if (referral_code==null || referral_code.isEmpty()){
                    results.add(tranlogmoney);
                } else {
                    UserDao userDao = new UserDaoImpl();
                    try {
                        if (userDao.checkUserBelongAgent(tranlogmoney.user_id, referral_code)){
                            results.add(tranlogmoney);
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });
        return results;
    }

    @Override
    public LogUserMoneyResponse searchLastLogMoneyUser(String nick_name, String type, ArrayList<String> agents)
    {
        final ArrayList<LogUserMoneyResponse> results = new ArrayList<LogUserMoneyResponse>();
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        FindIterable iterable = null;
        BasicDBObject obj = new BasicDBObject();        
        if (nick_name != null && !nick_name.equals("")) {
            conditions.put("nick_name", nick_name);
        }                
        conditions.put("action_name", "TransferMoney");        
        
        BasicDBObject objsort = new BasicDBObject();
        objsort.put("create_time", -1);
        
        // money
        BasicDBObject objMoney = new BasicDBObject();     
        BasicDBObject objAgent = new BasicDBObject();   
        if ("TRANSFER_USER".equals(type))
        {
            // chuyen tien cho user
            objMoney.put("$lt", 0);
            conditions.put("money_exchange", objMoney);            
        }
        else if ("TRANSFER_AGENT".equals(type))
        {
            // chuyen tien cho dai ly
            objMoney.put("$lt", 0);
            conditions.put("money_exchange", objMoney);   
        }
        else if ("RECEIVE_AGENT".equals(type))
        {
            // nhan tien tu dai ly
            objMoney.put("$gt", 0);
            conditions.put("money_exchange", objMoney);  
        }
        else if ("RECEIVE_USER".equals(type))
        {
            // nha tien tu user
            objMoney.put("$gt", 0);
            conditions.put("money_exchange", objMoney);  
        }
        else
        {
            
        }        
        iterable = db.getCollection("log_money_user_xu").find( new Document(conditions)).sort( objsort).skip(0).limit(100);
        iterable.forEach((Block)new Block<Document>(){

            public void apply(Document document) {
                LogUserMoneyResponse tranlogmoney = new LogUserMoneyResponse();
                tranlogmoney.nickName = document.getString("nick_name");
                tranlogmoney.serviceName = document.getString("service_name");
                tranlogmoney.currentMoney = document.getLong("current_money");
                tranlogmoney.moneyExchange = document.getLong("money_exchange");
                tranlogmoney.description = document.getString("description");
                tranlogmoney.transactionTime = document.getString("trans_time");
                tranlogmoney.actionName = document.getString("action_name");
                tranlogmoney.fee = document.getLong("fee");
                tranlogmoney.createdTime = document.getDate("create_time").getTime();
                results.add(tranlogmoney);
            }
        });       
        if (results != null && results.size() > 0)
        {
            LogUserMoneyResponse resp = null;
            for (LogUserMoneyResponse response : results)
            {
                if ("TRANSFER_USER".equals(type) || "RECEIVE_USER".equals(type))
                {
                    // khong phai agent  
                    boolean match = false;
                    for (String s : agents)
                    {
                        if (response.description.contains(s))
                        {
                            match = true;
                        }
                    }
                    if (!match)
                    {
                        // tai khoan user
                        resp = response;
                        break;
                    }
                }
                else
                {
                    // khong phai agent  
                    boolean match = false;
                    for (String s : agents)
                    {
                        if (response.description.contains(s))
                        {
                            match = true;
                        }
                    }
                    if (match)
                    {
                        // tai khoan user
                        resp = response;
                        break;
                    }
                }
            }
            return resp;
        }
        return null;
    }
    @Override
    public List<LogUserMoneyResponse> searchAllLogMoneyUser(String nick_name, String type, boolean seven_days)
    {
        final ArrayList<LogUserMoneyResponse> results = new ArrayList<LogUserMoneyResponse>();
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        FindIterable iterable = null;        
        if (nick_name != null && !nick_name.equals("")) {
            conditions.put("nick_name", nick_name);
        }    
        if (seven_days)
        {
            Date currentDate = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(currentDate);
            c.add(Calendar.DATE, -7);
            Date currentDatePlusSeven = c.getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
            String strDate = dateFormat.format(currentDatePlusSeven);  
            BasicDBObject obj = new BasicDBObject();
            obj.put("$gte",  strDate);            
            conditions.put("trans_time", obj);
        }
        
        BasicDBObject objsort = new BasicDBObject();
        objsort.put("create_time", -1);        
        if ("TRANSFER".equals(type))
        {
            conditions.put("action_name", "TransferMoney");      
            iterable = db.getCollection("log_money_user_tieu_vin").find( new Document(conditions)).sort( objsort);
        }
        else if ("GIFTCODE".equals(type))
        {
            //conditions.put("action_name", "GiftCode");
            //BasicDBObject objGiftCode = new BasicDBObject();   
            //objGiftCode.put("$regex", "GiftCode");
            //conditions.put("action_name", objGiftCode); 
            String pattern = ".*" + "GiftCode" + ".*";
            conditions.put("action_name", new BasicDBObject().append("$regex", pattern).append("$options", "i"));
            iterable = db.getCollection("log_money_user_nap_vin").find( new Document(conditions)).sort( objsort);
        }
        else if ("CARD".equals(type))
        {
            conditions.put("action_name", "RechargeByCard");      
            iterable = db.getCollection("dvt_recharge_by_gachthe").find( new Document(conditions)).sort( objsort);
        }
        else if ("BANK".equals(type))
        {
            conditions.put("action_name", "RechargeByBank");
            iterable = db.getCollection("log_money_user_nap_vin").find( new Document(conditions)).sort( objsort);
        }
        else if ("MOMO".equals(type))
        {
            conditions.put("action_name", "RechargeByMomo");
            iterable = db.getCollection("log_money_user_nap_vin").find( new Document(conditions)).sort( objsort);
        }

        else
        {
            conditions.put("action_name", "TransferMoney");      
            iterable = db.getCollection("log_money_user_nap_vin").find( new Document(conditions)).sort( objsort);
        }
        iterable.forEach((Block)new Block<Document>(){

            public void apply(Document document) {
                LogUserMoneyResponse tranlogmoney = new LogUserMoneyResponse();
                tranlogmoney.nickName = document.getString("nick_name");
                tranlogmoney.serviceName = document.getString("service_name");
                tranlogmoney.currentMoney = document.getLong("current_money");
                tranlogmoney.moneyExchange = document.getLong("money_exchange");
                tranlogmoney.description = document.getString("description");
                tranlogmoney.transactionTime = document.getString("trans_time");
                tranlogmoney.actionName = document.getString("action_name");
                tranlogmoney.fee = document.getLong("fee");
                if (document.getDate("create_time") != null)
                    tranlogmoney.createdTime = document.getDate("create_time").getTime() + 7 * 60 * 60 * 1000;
                results.add(tranlogmoney);
            }
        });       
        return results;
    }
    
    @Override
    public long getTotalBetWin(String nick_name, String type, String action_name)
    {        
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        HashMap<String, Object> conditions = new HashMap<String, Object>();
//        FindIterable iterable = null;        
//        conditions.put("is_bot", false);
//        if (nick_name != null && !nick_name.equals("")) {
//            conditions.put("nick_name", nick_name);
//        }  
//        conditions.put("play_game", true);
//        
//        
//        iterable = db.getCollection(MongoCollections.LOG_MONEY_USER_VIN).find();
        List<String> slots = new ArrayList<>();
        slots.add("KhoBau");
        slots.add("NuDiepVien");
        slots.add("SieuAnhHung");
        slots.add("VuongQuocVin");
        List<BasicDBObject> pipeline = new ArrayList<>();
        if ("BET".equals(type))
        {
            if (action_name != null && !"".equals(action_name))
            {              
                if ("SLOT".equals(action_name))
                {
                    BasicDBObject match = new BasicDBObject(
                        "$match", (new BasicDBObject("is_bot", false))
                                .append("nick_name", nick_name)
                                .append("action_name", new BasicDBObject("$in",slots))
                                .append("play_game", true)
                                .append("money_exchange", new BasicDBObject("$lt",0)));            
                    pipeline.add(match);
                }
                else
                {
                    BasicDBObject match = new BasicDBObject(
                        "$match", (new BasicDBObject("is_bot", false))
                                .append("nick_name", nick_name)
                                .append("action_name", action_name)
                                .append("play_game", true)
                                .append("money_exchange", new BasicDBObject("$lt",0)));            
                    pipeline.add(match);
                }
            }
            else
            {
                BasicDBObject match = new BasicDBObject(
                    "$match", (new BasicDBObject("is_bot", false))
                            .append("nick_name", nick_name)                            
                            .append("play_game", true)
                            .append("money_exchange", new BasicDBObject("$lt",0)));            
                pipeline.add(match);
            }
        }
        else
        {
            if (action_name != null && !"".equals(action_name))
            {  
                if ("SLOT".equals(action_name))
                {
                    BasicDBObject match = new BasicDBObject(
                        "$match", (new BasicDBObject("is_bot", false))
                                .append("nick_name", nick_name)
                                .append("action_name", new BasicDBObject("$in",slots))
                                .append("play_game", true)
                                .append("money_exchange", new BasicDBObject("$gt",0)));            
                    pipeline.add(match);
                }
                else
                {
                    BasicDBObject match = new BasicDBObject(
                    "$match", (new BasicDBObject("is_bot", false))
                            .append("nick_name", nick_name)
                            .append("action_name", action_name)
                            .append("play_game", true)
                            .append("money_exchange", new BasicDBObject("$gt",0)));            
                    pipeline.add(match);
                }
            }
            else
            {
                BasicDBObject match = new BasicDBObject(
                    "$match", (new BasicDBObject("is_bot", false))
                            .append("nick_name", nick_name)
                            .append("play_game", true)
                            .append("money_exchange", new BasicDBObject("$gt",0)));            
                pipeline.add(match);
            }
        }

        BasicDBObject group = new BasicDBObject(
            "$group", new BasicDBObject("_id", null).append(
                "total", new BasicDBObject( "$sum", "$money_exchange" )
            )
        );
        pipeline.add(group); 
        AggregateIterable<Document> output = db.getCollection(MongoCollections.LOG_MONEY_USER_VIN).aggregate(pipeline);
        List<Document> results = new ArrayList<Document>();
        output.forEach((Block)new Block<Document>(){

            public void apply(Document document) {
                results.add(document);
            }
        });     
        if (results != null && results.size() > 0)
        {
            return results.get(0).getLong("total");
        }
        return 0;
    }

    @Override
    public int countsearchLogMoneyUser(String nickName, String moneyType, String serviceName, String actionName, String timeStart, String timeEnd, int like) {
        int countRecord = 0;
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        BasicDBObject obj = new BasicDBObject();
        BasicDBObject objsort = new BasicDBObject();
        objsort.put("_id", -1);
        if (nickName != null && !nickName.equals("")) {
            if (like == 0) {
                conditions.put("nick_name", nickName);
            } else {
                conditions.put("nick_name", nickName);
            }
        }
        if (actionName != null && !actionName.equals("")) {
            conditions.put("action_name", actionName);
        }
        if (serviceName != null && !serviceName.equals("")) {
            conditions.put("service_name", serviceName);
        }
        if (timeStart != null && !timeStart.equals("") && timeEnd != null && !timeEnd.equals("")) {
            obj.put("$gte", timeStart);
            obj.put("$lte", timeEnd);
            conditions.put("trans_time", obj);
        }
        if (moneyType.equals("vin")) {
            countRecord = (int)db.getCollection(MongoCollections.LOG_MONEY_USER_VIN).count(new Document(conditions));
        } else if (moneyType.equals("xu")) {
            countRecord = (int)db.getCollection("log_money_user_xu").count(new Document(conditions));
        }
        return countRecord;
    }

    @Override
    public List<LogMoneyUserResponse> getHistoryTransactionLogMoney(String nickName, int moneyType, int page) {
        int numStart = (page - 1) * 13;
        int numEnd = 13;
        List<LogMoneyUserResponse> result = this.getTransactionList(nickName, moneyType, numStart, numEnd);
        return result;
    }

    private Map<String, Object> buildConditionNapVin(String nickName) {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("nick_name", nickName);
        return conditions;
    }

    private Map<String, Object> buildConditionTieuVin(String nickName) {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("nick_name", nickName);
        return conditions;
    }

    private Map<String, Object> buildConditionTransaction(String nickName, int moneyType) {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("nick_name", nickName);
        ArrayList<BasicDBObject> lstServerName = new ArrayList<BasicDBObject>();
        switch (moneyType) {
            case 4: {
                for (String action : Consts.NAP_XU) {
                    BasicDBObject query = new BasicDBObject("action_name", action);
                    lstServerName.add(query);
                }
                conditions.put("$or", lstServerName);
                conditions.put("money_exchange", new BasicDBObject("$gt", 0));
            }
        }
        return conditions;
    }

    @Override
    public int countHistoryTransactionLogMoney(String nickName, int queryType) {
        int countRecord = 0;
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        Map<String, Object> conditions = this.buildConditionTransaction(nickName, queryType);
        Map<String, Object> conditionNapVin = this.buildConditionNapVin(nickName);
        Map<String, Object> conditionTieuVin = this.buildConditionTieuVin(nickName);
        switch (queryType) {
            case 1: {
                countRecord = (int)db.getCollection(MongoCollections.LOG_MONEY_USER_VIN).count(new Document(conditions));
                break;
            }
            case 3: {
                countRecord = (int)db.getCollection("log_money_user_nap_vin").count(new Document(conditionNapVin));
                break;
            }
            case 5: {
                countRecord = (int)db.getCollection("log_money_user_tieu_vin").count(new Document(conditionTieuVin));
                break;
            }
            default: {
                countRecord = (int)db.getCollection("log_money_user_xu").count(new Document(conditions));
            }
        }
        return countRecord;
    }

    @Override
    public List<LogMoneyUserResponse> getTransactionList(String nickName, int queryType, int start, int end) {
        final ArrayList<LogMoneyUserResponse> results = new ArrayList<LogMoneyUserResponse>();
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        Map<String, Object> conditions = this.buildConditionTransaction(nickName, queryType);
        Map<String, Object> conditionNapVin = this.buildConditionNapVin(nickName);
        Map<String, Object> conditionTieuVin = this.buildConditionTieuVin(nickName);
        FindIterable iterable = null;
        BasicDBObject objsort = new BasicDBObject();
        objsort.put("_id", -1);
        switch (queryType) {
            case 1: {
                iterable = db.getCollection(MongoCollections.LOG_MONEY_USER_VIN).find(new Document(conditions)).sort(objsort).skip(start).limit(end);
                break;
            }
            case 3: {
                iterable = db.getCollection("log_money_user_nap_vin").find(new Document(conditionNapVin)).sort(objsort).skip(start).limit(end);
                break;
            }
            case 5: {
                iterable = db.getCollection("log_money_user_tieu_vin").find(new Document(conditionTieuVin)).sort(objsort).skip(start).limit(end);
                break;
            }
            default: {
                iterable = db.getCollection("log_money_user_xu").find(new Document(conditions)).sort(objsort).skip(start).limit(end);
            }
        }
        iterable.forEach((Block)new Block<Document>(){

            public void apply(Document document) {
                LogMoneyUserResponse tranlogmoney = new LogMoneyUserResponse();
                tranlogmoney.transId = document.getLong("trans_id");
                tranlogmoney.serviceName = document.getString("service_name");
                tranlogmoney.currentMoney = document.getLong("current_money");
                tranlogmoney.moneyExchange = document.getLong("money_exchange");
                tranlogmoney.description = document.getString("description");
                tranlogmoney.transactionTime = document.getString("trans_time");
                tranlogmoney.actionName = document.getString("action_name");
                results.add(tranlogmoney);
            }
        });
        return results;
    }

    @Override
    public Map<String, TopGameBaiModel> getTopGameBai(String gameName) {
        TopGameBaiModel model;
        HashMap<String, TopGameBaiModel> result = new HashMap<String, TopGameBaiModel>();
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        MongoCollection collection = db.getCollection("log_money_user_xu");
        Document conditionWin = new Document();
        conditionWin.put("action_name", gameName);
        conditionWin.put("money_exchange", new BasicDBObject("$gt", 0));
        Document conditionWinWeek = new Document();
        conditionWinWeek.put("action_name", gameName);
        conditionWinWeek.put("money_exchange", new BasicDBObject("$gt", 0));
        String startTime = "2020-10-17 00;00;00";
        BasicDBObject obj = new BasicDBObject();
        obj.put("$gte", startTime);
        conditionWinWeek.put("trans_time", obj);
        AggregateIterable iterableWin = collection.aggregate(Arrays.asList(new Document[]{new Document("$match", conditionWin), new Document("$group", new Document("_id", "$nick_name").append("money", new Document("$sum", "$money_exchange")).append("count", new Document("$sum", 1)))}));
        final ArrayList<TopGameBaiModel> resultWin = new ArrayList();
        iterableWin.forEach((Block)new Block<Document>(){

            public void apply(Document document) {
                TopGameBaiModel top = new TopGameBaiModel();
                top.setNickName(document.getString("_id"));
                top.setWinCount(document.getInteger("count"));
                top.setMoneyWin(document.getLong("money"));
                resultWin.add(top);
            }
        });
        AggregateIterable iterableWinWeek = collection.aggregate(Arrays.asList(new Document[]{new Document("$match", conditionWinWeek), new Document("$group", new Document("_id", "$nick_name").append("money", new Document("$sum", "$money_exchange")).append("count", new Document("$sum", 1)))}));
        final ArrayList<TopGameBaiModel> resultWinWeek = new ArrayList();
        iterableWinWeek.forEach((Block)new Block<Document>(){

            public void apply(Document document) {
                TopGameBaiModel top = new TopGameBaiModel();
                top.setNickName(document.getString("_id"));
                top.setWinCountThisWeek(document.getInteger("count"));
                top.setMoneyWinThisWeek(document.getLong("money"));
                resultWinWeek.add(top);
            }
        });
        Document conditionLost = new Document();
        conditionLost.put("action_name", gameName);
        conditionLost.put("money_exchange", new BasicDBObject("$lt", 0));
        Document conditionLostWeek = new Document();
        conditionLostWeek.put("action_name", gameName);
        conditionLostWeek.put("money_exchange", new BasicDBObject("$lt", 0));
        conditionLostWeek.put("trans_time", obj);
        AggregateIterable iterableLost = collection.aggregate(Arrays.asList(new Document[]{new Document("$match", conditionLost), new Document("$group", new Document("_id", "$nick_name").append("money", new Document("$sum", "$money_exchange")).append("count", new Document("$sum", 1)))}));
        final ArrayList<TopGameBaiModel> resultLost = new ArrayList();
        iterableLost.forEach((Block)new Block<Document>(){

            public void apply(Document document) {
                TopGameBaiModel top = new TopGameBaiModel();
                top.setNickName(document.getString("_id"));
                top.setLostCount(document.getInteger("count"));
                top.setMoneyLost(document.getLong("money"));
                resultLost.add(top);
            }
        });
        AggregateIterable iterablLostWeek = collection.aggregate(Arrays.asList(new Document[]{new Document("$match", conditionLostWeek), new Document("$group", new Document("_id", "$nick_name").append("money", new Document("$sum", "$money_exchange")).append("count", new Document("$sum", 1)))}));
        final ArrayList<TopGameBaiModel> resultLostWeek = new ArrayList();
        iterablLostWeek.forEach((Block)new Block<Document>(){

            public void apply(Document document) {
                TopGameBaiModel top = new TopGameBaiModel();
                top.setNickName(document.getString("_id"));
                top.setLostCountThisWeek(document.getInteger("count"));
                top.setMoneyLostThisWeek(document.getLong("money"));
                resultLostWeek.add(top);
            }
        });
        for (TopGameBaiModel top : resultWin) {
            top.setWinCountThisMonth(top.getWinCount());
            top.setWinCountThisYear(top.getWinCount());
            top.setMoneyWinThisMonth(top.getMoneyWin());
            top.setMoneyWinThisYear(top.getMoneyWin());
            result.put(top.getNickName(), top);
        }
        for (TopGameBaiModel top : resultWinWeek) {
            if (!result.containsKey(top.getNickName())) continue;
            model = (TopGameBaiModel)result.get(top.getNickName());
            model.setWinCountThisWeek(top.getWinCountThisWeek());
            model.setMoneyWinThisWeek(top.getMoneyWinThisWeek());
            result.put(top.getNickName(), model);
        }
        for (TopGameBaiModel top : resultLost) {
            if (result.containsKey(top.getNickName())) {
                model = (TopGameBaiModel)result.get(top.getNickName());
                model.setLostCount(top.getLostCount());
                model.setLostCountThisMonth(top.getLostCount());
                model.setLostCountThisYear(top.getLostCount());
                model.setMoneyLost(top.getMoneyLost());
                model.setMoneyLostThisMonth(top.getMoneyLost());
                model.setMoneyLostThisYear(top.getMoneyLost());
                result.put(top.getNickName(), model);
                continue;
            }
            top.setLostCountThisMonth(top.getLostCount());
            top.setLostCountThisYear(top.getLostCount());
            top.setMoneyLostThisMonth(top.getMoneyLost());
            top.setMoneyLostThisYear(top.getMoneyLost());
            result.put(top.getNickName(), top);
        }
        for (TopGameBaiModel top : resultLostWeek) {
            if (!result.containsKey(top.getNickName())) continue;
            model = (TopGameBaiModel)result.get(top.getNickName());
            model.setLostCountThisWeek(top.getLostCountThisWeek());
            model.setMoneyLostThisWeek(top.getMoneyLostThisWeek());
            result.put(top.getNickName(), model);
        }
        return result;
    }

    @Override
    public List<LogNoHuGameBaiMessage> getNoHuGameBaiHistory(int pageNumber, String gameName) {
        int pageSize = 10;
        int skipNumber = (pageNumber - 1) * 10;
        final ArrayList<LogNoHuGameBaiMessage> results = new ArrayList<LogNoHuGameBaiMessage>();
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        FindIterable iterable = null;
        Document conditions = new Document();
        if (gameName != null && !gameName.isEmpty()) {
            conditions.put("game_name", gameName);
        } else {
            conditions.put("game_name", new Document("$ne", "PokerTour"));
        }
        BasicDBObject sortCondtions = new BasicDBObject();
        sortCondtions.put("_id", -1);
        iterable = db.getCollection("log_no_hu_game_bai").find(conditions).sort(sortCondtions).skip(skipNumber).limit(10);
        iterable.forEach((Block)new Block<Document>(){

            public void apply(Document document) {
                LogNoHuGameBaiMessage entry = new LogNoHuGameBaiMessage(document.getString("nick_name"), document.getInteger("room").intValue(), document.getLong("pot_value").longValue(), document.getLong("money_win").longValue(), document.getString("game_name"), document.getString("description"), document.getString("tour_id"));
                entry.setCreateTime(document.getString("trans_time"));
                results.add(entry);
            }
        });
        return results;
    }

    @Override
    public int countNoHuGameBaiHistory() {
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        Document conditions = new Document();
        long totalRows = db.getCollection("log_no_hu_game_bai").count(conditions);
        return (int)totalRows;
    }

    @Override
    public UserModel getUserByNickName(String nickname) throws SQLException {
        UserModel user = null;
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");){
            String sql = "SELECT * FROM users WHERE nick_name=?";
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM users WHERE nick_name=?");
            stm.setString(1, nickname);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                user = UserUtil.parseResultSetToUserModel((ResultSet)rs);
            }
            rs.close();
            stm.close();
        }
        return user;
    }


    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getLogMoneyUserNapVin(Long trans_id, Integer user_id, String nick_name, String service_name,
     Long current_money, Long money_exchange, String description, String fromTime, String endTime, String action_name, Long fee, Date create_time, int page, int maxItem) {
        try{
            Map<String, Object> map = new HashMap();
            List<LogMoneyUserNapTieuVinModel> records = new ArrayList<LogMoneyUserNapTieuVinModel>();
            List<Long> num = new ArrayList<Long>();
            num.add(0, 0L);
            MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
            MongoCollection<?> col = db.getCollection("log_money_user_nap_vin");
            page = (page - 1) < 0 ? 0 : (page - 1);
            int numStart = page * maxItem;
            int numEnd = maxItem;
            BasicDBObject objsort = new BasicDBObject();
            objsort.put("_id", -1);
            HashMap<String, Object> conditions = new HashMap<String, Object>();
            if (nick_name!=null && !nick_name.isEmpty()) {
                String pattern = ".*" + nick_name + ".*";
                conditions.put("nick_name", new BasicDBObject().append("$regex", pattern)
                        .append("$options", "i"));
            }
            if (service_name!=null && !service_name.isEmpty()) {
                String pattern = ".*" + service_name + ".*";
                conditions.put("service_name", new BasicDBObject().append("$regex", pattern)
                        .append("$options", "i"));
            }
            if (action_name!=null && !action_name.isEmpty()) {
                String pattern = ".*" + action_name + ".*";
                conditions.put("action_name", new BasicDBObject().append("$regex", pattern)
                        .append("$options", "i"));
            }
            if (trans_id!=null){
                conditions.put("trans_id", trans_id);
            }
            if (user_id!=null){
                conditions.put("user_id", user_id);
            }
            if (current_money!=null){
                BasicDBObject obj = new BasicDBObject();
                obj.put("$gte", current_money);
                conditions.put("current_money", obj);
            }
            if (money_exchange!=null){
                BasicDBObject obj = new BasicDBObject();
                obj.put("$gte", money_exchange);
                conditions.put("money_exchange", obj);
            }
            if (fee!=null){
                BasicDBObject obj = new BasicDBObject();
                obj.put("$gte", fee);
                conditions.put("fee", obj);
            }
            if (fromTime!=null && !fromTime.isEmpty() && endTime!=null && !endTime.isEmpty()) {
                BasicDBObject obj = new BasicDBObject();
                obj.put("$gte", fromTime);
                obj.put("$lte", endTime);
                conditions.put("trans_time", obj);
            }
            FindIterable<?> iterable = col.find((Bson) new Document(conditions)).sort((Bson) objsort).skip(numStart)
                    .limit(maxItem);
            iterable.forEach((Block) new Block<Document>() {
                public void apply(Document document) {

                    LogMoneyUserNapTieuVinModel model =new LogMoneyUserNapTieuVinModel(
                            document.getLong("trans_id"),
                            document.getInteger("user_id"),
                            document.getString("nick_name"),
                            document.getString("service_name"),
                            document.getLong("current_money"),
                            document.getLong("money_exchange"),
                            document.getString("description"),
                            document.getString("trans_time"),
                            document.getString("action_name"),
                            document.getLong("fee"),
                            document.getDate("create_time")
                    );
                    records.add(model);
                }
            });
            FindIterable<?> iterable1 = col.find((Bson) new Document(conditions));
            iterable1.forEach((Block) new Block<Document>() {
                public void apply(Document document) {
                    num.set(0, num.get(0)+document.getLong("money_exchange"));
                }
            });
            Long count = col.count((Bson) new Document(conditions));//fill without paging
            map.put("listData", records);
            map.put("totalData", count);
            map.put("totalNap", num.get(0));
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getLogMoneyUserTieuVin(Long trans_id, Integer user_id, String nick_name, String service_name, Long current_money, Long money_exchange, String description, String fromTime, String endTime, String action_name, Long fee, Date create_time, int page, int maxItem) {
        try{
            Map<String, Object> map = new HashMap();
            List<LogMoneyUserNapTieuVinModel> records = new ArrayList<LogMoneyUserNapTieuVinModel>();
            List<Long> num = new ArrayList<Long>();
            num.add(0, 0L);
            MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
            MongoCollection<?> col = db.getCollection("log_money_user_tieu_vin");
            page = (page - 1) < 0 ? 0 : (page - 1);
            int numStart = page * maxItem;
            int numEnd = maxItem;
            BasicDBObject objsort = new BasicDBObject();
            objsort.put("_id", -1);
            HashMap<String, Object> conditions = new HashMap<String, Object>();
            if (nick_name!=null && !nick_name.isEmpty()) {
                String pattern = ".*" + nick_name + ".*";
                conditions.put("nick_name", new BasicDBObject().append("$regex", pattern)
                        .append("$options", "i"));
            }
            if (service_name!=null && !service_name.isEmpty()) {
                String pattern = ".*" + service_name + ".*";
                conditions.put("service_name", new BasicDBObject().append("$regex", pattern)
                        .append("$options", "i"));
            }
            if (action_name!=null && !action_name.isEmpty()) {
                String pattern = ".*" + action_name + ".*";
                conditions.put("action_name", new BasicDBObject().append("$regex", pattern)
                        .append("$options", "i"));
            }
            if (trans_id!=null){
                conditions.put("trans_id", trans_id);
            }
            if (user_id!=null){
                conditions.put("user_id", user_id);
            }
            if (current_money!=null){
                BasicDBObject obj = new BasicDBObject();
                obj.put("$gte", current_money);
                conditions.put("current_money", obj);
            }
            if (money_exchange!=null){
                BasicDBObject obj = new BasicDBObject();
                obj.put("$gte", money_exchange);
                conditions.put("money_exchange", obj);
            }
            if (fee!=null){
                BasicDBObject obj = new BasicDBObject();
                obj.put("$gte", fee);
                conditions.put("fee", obj);
            }
            if (fromTime!=null && !fromTime.isEmpty() && endTime!=null && !endTime.isEmpty()) {
                BasicDBObject obj = new BasicDBObject();
                obj.put("$gte", fromTime);
                obj.put("$lte", endTime);
                conditions.put("trans_time", obj);
            }

            FindIterable<?> iterable = col.find((Bson) new Document(conditions)).sort((Bson) objsort).skip(numStart)
                    .limit(maxItem);
            iterable.forEach((Block) new Block<Document>() {
                public void apply(Document document) {

                    LogMoneyUserNapTieuVinModel model =new LogMoneyUserNapTieuVinModel(
                            document.getLong("trans_id"),
                            document.getInteger("user_id"),
                            document.getString("nick_name"),
                            document.getString("service_name"),
                            document.getLong("current_money"),
                            document.getLong("money_exchange"),
                            document.getString("description"),
                            document.getString("trans_time"),
                            document.getString("action_name"),
                            document.getLong("fee"),
                            document.getDate("create_time")
                    );
                    records.add(model);
                }
            });
            FindIterable<?> iterable1 = col.find((Bson) new Document(conditions));
            iterable1.forEach((Block) new Block<Document>() {
                public void apply(Document document) {
                    num.set(0, num.get(0)+document.getLong("money_exchange"));
                }
            });
            Long count = col.count((Bson) new Document(conditions));//fill without paging
            map.put("listData", records);
            map.put("totalData", count);
            map.put("totalRut", num.get(0));
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getLogMoneyUserVin(Long trans_id, Integer user_id, String nick_name, String service_name, Long current_money, Long money_exchange, String description, String fromTime, String endTime, String action_name, Long fee, Date create_time, Boolean is_bot, Boolean play_game, int page, int maxItem) {
        try{
            Map<String, Object> map = new HashMap<String, Object>();
            List<LogMoneyUserVinModel> records = new ArrayList<LogMoneyUserVinModel>();
            List<Long> num = new ArrayList<Long>();
            num.add(0, 0L);
            num.add(1, 0L);
            num.add(2, 0L);
            num.add(3, 0L);
            MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
            MongoCollection<?> col = db.getCollection(MongoCollections.LOG_MONEY_USER_VIN);
            page = (page - 1) < 0 ? 0 : (page - 1);
            int numStart = page * maxItem;
            int numEnd = maxItem;
            BasicDBObject objsort = new BasicDBObject();
            objsort.put("_id", -1);
            HashMap<String, Object> conditions = new HashMap<String, Object>();
            if (nick_name!=null && !nick_name.isEmpty()) {
                conditions.put("nick_name", nick_name);
            }
            if (service_name!=null && !service_name.isEmpty()) {
                conditions.put("service_name", service_name);
            }
            if (action_name!=null && !action_name.isEmpty()) {
                conditions.put("action_name", action_name);
            }
            if (trans_id!=null){
                conditions.put("trans_id", trans_id);
            }
            if (user_id!=null){
                conditions.put("user_id", user_id);
            }
            if (current_money!=null){
                BasicDBObject obj = new BasicDBObject();
                obj.put("$gte", current_money);
                conditions.put("current_money", obj);
            }
            if (money_exchange!=null){
                BasicDBObject obj = new BasicDBObject();
                obj.put("$gte", money_exchange);
                conditions.put("money_exchange", obj);
            }
            if (fee!=null){
                BasicDBObject obj = new BasicDBObject();
                obj.put("$gte", fee);
                conditions.put("fee", obj);
            }
            if (fromTime!=null && !fromTime.isEmpty() && endTime!=null && !endTime.isEmpty()) {
                BasicDBObject obj = new BasicDBObject();
                obj.put("$gte", fromTime.trim());
                obj.put("$lte", endTime.trim());
                conditions.put("trans_time", obj);
            }

            FindIterable<?> iterable = col.find((Bson) new Document(conditions)).sort((Bson) objsort).skip(numStart)
                    .limit(maxItem);
            iterable.forEach((Block) new Block<Document>() {
                public void apply(Document document) {

                    LogMoneyUserVinModel model =new LogMoneyUserVinModel(
                            document.getLong("trans_id"),
                            document.getInteger("user_id"),
                            document.getString("nick_name"),
                            document.getString("service_name"),
                            document.getLong("current_money"),
                            document.getLong("money_exchange"),
                            document.getString("description"),
                            document.getString("trans_time"),
                            document.getString("action_name"),
                            document.getLong("fee"),
                            document.getDate("create_time"),
                            document.getBoolean("is_bot"),
                            document.getBoolean("play_game")
                    );
                    records.add(model);
                }
            });
            FindIterable<?> iterable2 = col.find(new Document(conditions));//fill without paging
			
			iterable2.forEach((Block) new Block<Document>() {
				public void apply(Document document) {
					long betReal = document.getLong("money_exchange");
					long bet = Math.abs(betReal);
					
					long fee = Math.abs(document.getLong("fee"));
					String serviceName =document.getString("service_name");
					String actionName =document.getString("action_name");
					long count = num.get(0) + 1L;
					num.set(0, count);
					if (!Consts.LIST_RECHARGE.contains(actionName) 
							&& !Consts.LIST_CASHOUT_REAL.contains(actionName)
							&& !Consts.GAME3RD.LIST_GAME.contains(serviceName)
							&& !Consts.ADMIN.contains(serviceName)) {
						if (betReal < 0) {
                            long totalBet = num.get(1) + bet;
                            long countBet = num.get(3) + 1L;
                            num.set(1, totalBet);
                            num.set(3, countBet);
						} else {
                            long totalFee = num.get(2) + fee;
                            num.set(2, totalFee);
						}
					}
				}
			});
            map.put("listData", records);
            map.put("totalData", num.get(0));
            map.put("totalBet", num.get(1));
            map.put("totalFee", num.get(2));
            map.put("soVongCuoc", num.get(3));
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<LogMoneyUserResponse> getHisTLMN(String nickName, int queryType, int start, int end) {
        final ArrayList<LogMoneyUserResponse> results = new ArrayList<LogMoneyUserResponse>();
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        Map<String, Object> conditions = this.buildConditionTLMN(nickName, queryType);
        FindIterable iterable = null;
        BasicDBObject objsort = new BasicDBObject();
        objsort.put("_id", -1);
        iterable = db.getCollection(MongoCollections.LOG_MONEY_USER_VIN).find(new Document(conditions)).sort(objsort).skip(start).limit(end);

        iterable.forEach((Block)new Block<Document>(){

            public void apply(Document document) {
                LogMoneyUserResponse tranlogmoney = new LogMoneyUserResponse();
                tranlogmoney.transId = document.getLong("trans_id");
                tranlogmoney.serviceName = document.getString("service_name");
                tranlogmoney.currentMoney = document.getLong("current_money");
                tranlogmoney.moneyExchange = document.getLong("money_exchange");
                tranlogmoney.description = document.getString("description");
                tranlogmoney.transactionTime = document.getString("trans_time");
                tranlogmoney.actionName = document.getString("action_name");
                results.add(tranlogmoney);
            }
        });
        return results;
    }

    @Override
    public List<LogMoneyUserResponse> getHisXD(String nickName, int queryType, int start, int end) {
        final ArrayList<LogMoneyUserResponse> results = new ArrayList<LogMoneyUserResponse>();
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        Map<String, Object> conditions = this.buildConditionXD(nickName, queryType);
        FindIterable iterable = null;
        BasicDBObject objsort = new BasicDBObject();
        objsort.put("_id", -1);
        iterable = db.getCollection(MongoCollections.LOG_MONEY_USER_VIN).find(new Document(conditions)).sort(objsort).skip(start).limit(end);

        iterable.forEach((Block)new Block<Document>(){

            public void apply(Document document) {
                LogMoneyUserResponse tranlogmoney = new LogMoneyUserResponse();
                tranlogmoney.transId = document.getLong("trans_id");
                tranlogmoney.serviceName = document.getString("service_name");
                tranlogmoney.currentMoney = document.getLong("current_money");
                tranlogmoney.moneyExchange = document.getLong("money_exchange");
                tranlogmoney.description = document.getString("description");
                tranlogmoney.transactionTime = document.getString("trans_time");
                tranlogmoney.actionName = document.getString("action_name");
                results.add(tranlogmoney);
            }
        });
        return results;
    }

    private Map<String, Object> buildConditionTLMN(String nickName, int moneyType) {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("nick_name", nickName);
        conditions.put("service_name", "11");
        ArrayList<BasicDBObject> lstServerName = new ArrayList<BasicDBObject>();
        switch (moneyType) {
            case 4: {
                for (String action : Consts.NAP_XU) {
                    BasicDBObject query = new BasicDBObject("action_name", action);
                    lstServerName.add(query);
                }
                conditions.put("$or", lstServerName);
                conditions.put("money_exchange", new BasicDBObject("$gt", 0));
            }
        }
        return conditions;
    }

    private Map<String, Object> buildConditionXD(String nickName, int moneyType) {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("nick_name", nickName);
        conditions.put("service_name", "15");
        ArrayList<BasicDBObject> lstServerName = new ArrayList<BasicDBObject>();
        switch (moneyType) {
            case 4: {
                for (String action : Consts.NAP_XU) {
                    BasicDBObject query = new BasicDBObject("action_name", action);
                    lstServerName.add(query);
                }
                conditions.put("$or", lstServerName);
                conditions.put("money_exchange", new BasicDBObject("$gt", 0));
            }
        }
        return conditions;
    }
    
    @Override
    public List<Map<String, Object>> searchLogMoneyUser4Report(String nickName, String userName, String moneyType, String serviceName, String actionName, String timeStart, String timeEnd, int page, int totalRecord) {
    	List<Map<String, Object>> data = new ArrayList<>();
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        BasicDBObject obj = new BasicDBObject();
        int numStart = (page - 1) * totalRecord;
        if (nickName != null && !nickName.equals("")) {
            conditions.put("nick_name", nickName);
        }
        if (userName != null && !userName.equals("")) {
            conditions.put("user_name", userName);
        }
        if (actionName != null && !actionName.equals("")) {
            conditions.put("action_name", actionName);
        }
        if (serviceName != null && !serviceName.equals("")) {
            conditions.put("service_name", serviceName);
        }
        if (timeStart != null && !timeStart.equals("") && timeEnd != null && !timeEnd.equals("")) {
            obj.put("$gte", timeStart);
            obj.put("$lte", timeEnd);
            conditions.put("trans_time", obj);
        }
        
        List<Object> result = new ArrayList<>();
        long totalRecords = 0L;
        if (moneyType.equals("vin")) {
        	try{
        		result = db.getCollection(MongoCollections.LOG_MONEY_USER_VIN)
            		.find(new Document(conditions)).skip(numStart).limit(totalRecord)
            		.map(x -> JSON.parse(x.toJson()))
					.into(new ArrayList<>());
        		totalRecords = db.getCollection(MongoCollections.LOG_MONEY_USER_VIN)
                		.count(new Document(conditions));
        	}catch (Exception e) { }
        } else if (moneyType.equals("xu")) {
            BasicDBObject objsort = new BasicDBObject();
            objsort.put("trans_time", -1);
            try{
            	result = db.getCollection("log_money_user_xu")
            		.find(new Document(conditions)).sort(objsort).skip(numStart).limit(totalRecord)
            		.map(x -> JSON.parse(x.toJson()))
					.into(new ArrayList<>());
            	totalRecords = db.getCollection("log_money_user_xu")
                		.count(new Document(conditions));
            }catch (Exception e) { }
        }
        
        Map<String, Object> objectMap = new HashMap<>();
    	objectMap.put("data", result);
    	data.add(objectMap);
    	objectMap = new HashMap<>();
    	objectMap.put("data", totalRecords);
    	data.add(objectMap);
    	return data;
    }
    
    @Override
    public List<Map<String, Object>> getLogMoneyUserVin4Report(Long trans_id, Integer user_id, String nick_name, String service_name, Long current_money, Long money_exchange, String description, String fromTime, String endTime, String action_name, Long fee, Date create_time, Boolean is_bot, Boolean play_game, int page, int maxItem) {
        try{
        	List<Map<String, Object>> data = new ArrayList<>();
            MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
            MongoCollection<Document> collection = db.getCollection(MongoCollections.LOG_MONEY_USER_VIN);
            page = (page - 1) < 0 ? 0 : (page - 1);
            int numStart = page * maxItem;
            int numEnd = maxItem;
            BasicDBObject objsort = new BasicDBObject();
            objsort.put("_id", -1);
//            HashMap<String, Object> conditions = new HashMap<String, Object>();
            BasicDBObject conditions = new BasicDBObject();
            BasicDBObject conditions4Calculator = new BasicDBObject();
            if (nick_name!=null && !nick_name.isEmpty()) {
                conditions.put("nick_name", nick_name);
            }
            if (trans_id!=null){
                conditions.put("trans_id", trans_id);
            }
            if (user_id!=null){
                conditions.put("user_id", user_id);
            }
            if (current_money!=null){
                BasicDBObject obj = new BasicDBObject();
                obj.put("$gte", current_money);
                conditions.put("current_money", obj);
            }
            if (money_exchange!=null){
                BasicDBObject obj = new BasicDBObject();
                obj.put("$gte", money_exchange);
                conditions.put("money_exchange", obj);
            }
            if (fee!=null){
                BasicDBObject obj = new BasicDBObject();
                obj.put("$gte", fee);
                conditions.put("fee", obj);
            }
            if (fromTime!=null && !fromTime.isEmpty() && endTime!=null && !endTime.isEmpty()) {
                BasicDBObject obj = new BasicDBObject();
                obj.put("$gte", fromTime.trim());
                obj.put("$lte", endTime.trim());
                conditions.put("trans_time", obj);
            }
            
            conditions4Calculator = new BasicDBObject(conditions);
            if (service_name!=null && !service_name.isEmpty()) {
            	conditions.put("service_name", service_name);
            	BasicDBObject obj = new BasicDBObject();
            	List<String> lstAction = new ArrayList<>();
	            lstAction.addAll(Consts.LIST_RECHARGE);
	            lstAction.addAll(Consts.LIST_CASHOUT_REAL);
	            obj.put("$nin", lstAction);
	            obj.put("$eq", service_name);
	            conditions4Calculator.put("service_name", obj);
            }
            if (action_name!=null && !action_name.isEmpty()) {
            	conditions.put("action_name", action_name);
            	BasicDBObject obj = new BasicDBObject();
            	List<String> lstAction = new ArrayList<>();
	            lstAction.addAll(Consts.LIST_RECHARGE);
	            lstAction.addAll(Consts.LIST_CASHOUT_REAL);
	            obj.put("$nin", lstAction);
	            obj.put("$eq", action_name);
	            conditions4Calculator.put("action_name", obj);
            }
            
            Map<String, Object> map = new HashMap<String, Object>();
            List<Object> result = new ArrayList<>();
            Long totalRecords = 0L;
            try{
        		result = collection.find(new Document(conditions)).projection(Projections.exclude("_id"))
        				.skip(numStart).limit(maxItem).sort(objsort).map(x -> JSON.parse(x.toJson())).into(new ArrayList<>());
        		totalRecords = collection.count(new Document(conditions));
        	}catch (Exception e) { }
            
            Map<String, Object> objectMap = new HashMap<>();
        	objectMap.put("data", result);
        	data.add(objectMap);
        	objectMap = new HashMap<>();
        	objectMap.put("totalRecords", totalRecords);
        	data.add(objectMap);
            //Calculator sum
        	Document match = new Document("$match", conditions4Calculator);
        	Document project = new Document("$project", Document.parse("{\"bet\":{\"$cond\":[{\"$lt\":[\"$money_exchange\",0]},\"$money_exchange\",0]},\"countBet\":{\"$cond\":[{\"$lt\":[\"$money_exchange\",0]},1,0]},\"fee\":{\"$cond\":[{\"$lt\":[\"$money_exchange\",0]},0,\"$fee\"]}}"));
        	Document group = new Document("$group", Document.parse("{\"_id\":1,\"totalBet\":{\"$sum\":{\"$abs\":\"$bet\"}},\"totalFee\":{\"$sum\":\"$fee\"},\"soVongCuoc\":{\"$sum\":\"$countBet\"}}"));
//        	project.toJson()
        	AggregateIterable<Document> aggregates = (AggregateIterable<Document>) collection.aggregate(Arrays.asList(match, project, group));
        	for (Document document : aggregates) {
				String nickname = "";
				Long totalBet = 0l;
				Long totalFee = 0l;
				Long soVongCuoc = 0l;
				try {
					totalBet = document.getDouble("totalBet").longValue();
				} catch (Exception e) {
					totalBet = Long.parseLong(document.get("totalBet").toString());
				}
				
				objectMap = new HashMap<>();
	        	objectMap.put("totalBet", totalBet);
	        	data.add(objectMap);
				try {
					totalFee = document.getDouble("totalFee").longValue();
				} catch (Exception e) {
					totalFee = Long.parseLong(document.get("totalFee").toString());
				}
				
				objectMap = new HashMap<>();
	        	objectMap.put("totalFee", totalFee);
	        	data.add(objectMap);
				try {
					soVongCuoc = document.getDouble("soVongCuoc").longValue();
				} catch (Exception e) {
					soVongCuoc = Long.parseLong(document.get("soVongCuoc").toString());
				}
				
				objectMap = new HashMap<>();
	        	objectMap.put("soVongCuoc", soVongCuoc);
	        	data.add(objectMap);
        	}
        	
            return data;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}

