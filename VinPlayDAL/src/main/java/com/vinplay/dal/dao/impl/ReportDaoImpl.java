/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.mongodb.BasicDBObject
 *  com.mongodb.Block
 *  com.mongodb.client.AggregateIterable
 *  com.mongodb.client.FindIterable
 *  com.mongodb.client.MongoCollection
 *  com.mongodb.client.MongoDatabase
 *  com.mongodb.client.model.FindOneAndUpdateOptions
 *  com.vinplay.vbee.common.models.cache.ReportModel
 *  com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory
 *  com.vinplay.vbee.common.pools.ConnectionPool
 *  com.vinplay.vbee.common.utils.VinPlayUtils
 *  org.bson.Document
 *  org.bson.conversions.Bson
 */
package com.vinplay.dal.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.vinplay.common.game3rd.WMGameRecordItem;
import com.vinplay.dal.dao.ReportDAO;
import com.vinplay.dal.entities.report.ReportMoneySystemModel;
import com.vinplay.dal.entities.report.ReportTotalMoneyModel;
import com.vinplay.dal.entities.report.ReportTransactionDetailModel;
import com.vinplay.vbee.common.models.cache.ReportModel;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.common.pools.ConnectionPool;
import com.vinplay.vbee.common.utils.VinPlayUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.sql.Date;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReportDaoImpl
implements ReportDAO {
    @Override
    public List<String> getAllBot() throws SQLException {
        ArrayList<String> res = new ArrayList<String>();
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");){
            String sql = "SELECT nick_name FROM users WHERE is_bot=1";
            PreparedStatement stm = conn.prepareStatement("SELECT nick_name FROM users WHERE is_bot=1");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                res.add(rs.getString("nick_name"));
            }
            rs.close();
            stm.close();
        }
        return res;
    }

    @Override
    public Map<String, ReportMoneySystemModel> getReportMoneySystemMySQL(String startTime, String endTime, boolean isBot) throws Exception {
        HashMap<String, ReportMoneySystemModel> results = new HashMap<String, ReportMoneySystemModel>();
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
        CallableStatement call = null;
        try {
            call = conn.prepareCall("CALL report_money_system(?,?)");
            int param = 1;
            call.setString(param++, VinPlayUtils.getDateTimeStr((java.util.Date)VinPlayUtils.getDateTimeFromDate(startTime)));
            call.setString(param++, VinPlayUtils.getDateTimeStr((java.util.Date)VinPlayUtils.getDateTimeFromDate(endTime)));
            ResultSet rs = call.executeQuery();
            while (rs.next()) {
                ReportMoneySystemModel model = new ReportMoneySystemModel();
                model.moneyWin = rs.getLong("money_win");
                model.moneyLost = rs.getLong("money_lost");
                model.moneyOther = rs.getLong("money_other");
                model.fee = rs.getLong("fee");
                model.revenuePlayGame = model.moneyWin + model.moneyLost;
                model.revenue = model.revenuePlayGame + model.moneyOther;
                String actionName = rs.getString("action_name");
                results.put(actionName, model);
            }
            rs.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            if (call != null) {
                call.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return results;
    }

    @Override
    public Map<String, ReportMoneySystemModel> getReportMoneySystem(String startTime, String endTime, boolean isBot) throws Exception {
        final HashMap<String, ReportMoneySystemModel> results = new HashMap<String, ReportMoneySystemModel>();
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        Document conditions = new Document();
        if (!startTime.isEmpty() && !endTime.isEmpty()) {
            BasicDBObject obj = new BasicDBObject();
            obj.put("$gte", VinPlayUtils.getDateTimeStr((java.util.Date)VinPlayUtils.getDateTimeFromDate(startTime)));
            obj.put("$lte", VinPlayUtils.getDateTimeStr((java.util.Date)VinPlayUtils.getDateTimeFromDate(endTime)));
            conditions.put("time_log", obj);
        }
        MongoCollection col = null;
        if (!isBot) {
            col = db.getCollection("report_money_vin");
            AggregateIterable iterable = col.aggregate(Arrays.asList(new Document[]{new Document("$match", conditions), new Document("$group", new Document("_id", "$action_name").append("money_win", new Document("$sum", "$money_win")).append("money_lost", new Document("$sum", "$money_lost")).append("money_other", new Document("$sum", "$money_other")).append("fee", new Document("$sum", "$fee")))}));
            iterable.forEach((Block)new Block<Document>(){

                public void apply(Document document) {
                    ReportMoneySystemModel model = new ReportMoneySystemModel();
                    model.moneyWin = document.getLong("money_win");
                    model.moneyLost = document.getLong("money_lost");
                    model.moneyOther = document.getLong("money_other");
                    model.fee = document.getLong("fee");
                    model.revenuePlayGame = model.moneyWin + model.moneyLost;
                    model.revenue = model.revenuePlayGame + model.moneyOther;
                    String actionName = document.getString("_id");
                    results.put(actionName, model);
                }
            });
            return results;
        }
        return results;
    }

    @Override
    public Map<String, ReportMoneySystemModel> getReportMoneyUser(String startTime, String endTime, String nickname, boolean isBot) throws Exception {
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        Document conditions = new Document();
        if (!startTime.isEmpty() && !endTime.isEmpty()) {
            BasicDBObject obj = new BasicDBObject();
            obj.put("$gte", VinPlayUtils.getDateTimeStr((java.util.Date)VinPlayUtils.getDateTimeFromDate(startTime)));
            obj.put("$lte", VinPlayUtils.getDateTimeStr((java.util.Date)VinPlayUtils.getDateTimeFromDate(endTime)));
            conditions.put("time_log", obj);
        }
        conditions.put("nick_name", nickname);
        MongoCollection col = null;
        col = !isBot ? db.getCollection("report_money_vin") : db.getCollection("report_money_vin_bot");
        AggregateIterable iterable = col.aggregate(Arrays.asList(new Document[]{new Document("$match", conditions), new Document("$group", new Document("_id", "$action_name").append("money_win", new Document("$sum", "$money_win")).append("money_lost", new Document("$sum", "$money_lost")).append("money_other", new Document("$sum", "$money_other")).append("fee", new Document("$sum", "$fee")))}));
        final HashMap<String, ReportMoneySystemModel> results = new HashMap<String, ReportMoneySystemModel>();
        iterable.forEach((Block)new Block<Document>(){

            public void apply(Document document) {
                ReportMoneySystemModel model = new ReportMoneySystemModel();
                String actionName = document.getString("_id");
                model.moneyWin = document.getLong("money_win");
                model.moneyLost = document.getLong("money_lost");
                model.moneyOther = document.getLong("money_other");
                model.fee = document.getLong("fee");
                model.revenuePlayGame = model.moneyWin + model.moneyLost;
                model.revenue = model.revenuePlayGame + model.moneyOther;
                results.put(actionName, model);
            }
        });
        return results;
    }

    
    public ReportTotalMoneyModel getTotalMoney_OLD(String superAgent) throws SQLException {
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
        long moneyBot = 0L;
        long moneyUser = 0L;
        long moneyAgent1 = 0L;
        long moneyAgent2 = 0L;
        long moneySuperAgent = 0L;
        try {
            String sqlBot = "SELECT (SUM(vin_total) + SUM(safe)) as sum FROM users WHERE is_bot=1";
            String sqlUser = "SELECT (SUM(vin_total) + SUM(safe)) as sum FROM users WHERE is_bot=0 AND dai_ly <> 1 AND dai_ly <> 2";
            String sqlAgent1 = "SELECT (SUM(vin_total) + SUM(safe)) as sum FROM users WHERE is_bot=0 AND dai_ly = 1";
            String sqlAgent2 = "SELECT (SUM(vin_total) + SUM(safe)) as sum FROM users WHERE is_bot=0 AND dai_ly = 2";
            String sqlSuperAgent = "SELECT (SUM(vin_total) + SUM(safe)) as sum FROM users WHERE nick_name = '" + superAgent + "'";
            PreparedStatement stmBot = conn.prepareStatement("SELECT (SUM(vin_total) + SUM(safe)) as sum FROM users WHERE is_bot=1");
            PreparedStatement stmUser = conn.prepareStatement("SELECT (SUM(vin_total) + SUM(safe)) as sum FROM users WHERE is_bot=0 AND dai_ly <> 1 AND dai_ly <> 2");
            PreparedStatement stmAgent1 = conn.prepareStatement("SELECT (SUM(vin_total) + SUM(safe)) as sum FROM users WHERE is_bot=0 AND dai_ly = 1");
            PreparedStatement stmAgent2 = conn.prepareStatement("SELECT (SUM(vin_total) + SUM(safe)) as sum FROM users WHERE is_bot=0 AND dai_ly = 2");
            PreparedStatement stmSuperAgent = conn.prepareStatement(sqlSuperAgent);
            ResultSet rsBot = stmBot.executeQuery();
            ResultSet rsUser = stmUser.executeQuery();
            ResultSet rsAgent1 = stmAgent1.executeQuery();
            ResultSet rsAgent2 = stmAgent2.executeQuery();
            ResultSet rsSuperAgent = stmSuperAgent.executeQuery();
            if (rsBot.next()) {
                moneyBot = rsBot.getLong("sum");
            }
            if (rsUser.next()) {
                moneyUser = rsUser.getLong("sum");
            }
            if (rsAgent1.next()) {
                moneyAgent1 = rsAgent1.getLong("sum");
            }
            if (rsAgent2.next()) {
                moneyAgent2 = rsAgent2.getLong("sum");
            }
            if (rsSuperAgent.next()) {
                moneySuperAgent = rsSuperAgent.getLong("sum");
            }
            rsBot.close();
            rsUser.close();
            rsAgent1.close();
            rsAgent2.close();
            rsSuperAgent.close();
            stmBot.close();
            stmUser.close();
            stmAgent1.close();
            stmAgent2.close();
            stmSuperAgent.close();
        }
        catch (SQLException e) {
            throw e;
        }
        finally {
            if (conn != null) {
                conn.close();
            }
        }
        long total = moneyBot + moneyUser + (moneyAgent1 -= moneySuperAgent) + moneyAgent2 + moneySuperAgent;
        ReportTotalMoneyModel model = new ReportTotalMoneyModel(moneyBot, moneyUser, moneyAgent1, moneyAgent2, moneySuperAgent, total, null);
        return model;
    }
    
    @Override
    public ReportTotalMoneyModel getTotalMoney(String superAgent) throws SQLException {
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
        long moneyBot = 0L;
        long moneyUser = 0L;
        long moneyAgent1 = 0L;
        long moneyAgent2 = 0L;
        long moneySuperAgent = 0L;
        try {
        	String sql = "select count(*) count,"
        			+ " (SELECT ifnull((SUM(ifnull(vin_total,0)) + SUM(ifnull(safe,0))),0) FROM users WHERE is_bot=1) moneyBot,"
        			+ " (SELECT ifnull((SUM(ifnull(vin_total,0)) + SUM(ifnull(safe,0))),0) FROM users WHERE is_bot=0 AND dai_ly <> 1 AND dai_ly <> 2) moneyUser,"
        			+ " (SELECT ifnull((SUM(ifnull(vin_total,0)) + SUM(ifnull(safe,0))),0) FROM users WHERE is_bot=0 AND dai_ly = 1) moneyAgent1,"
        			+ " (SELECT ifnull((SUM(ifnull(vin_total,0)) + SUM(ifnull(safe,0))),0) FROM users WHERE is_bot=0 AND dai_ly = 2) moneyAgent2,"
        			+ " (SELECT ifnull((SUM(ifnull(vin_total,0)) + SUM(ifnull(safe,0))),0) as sum FROM users WHERE nick_name = '" + superAgent + "') moneySuperAgent"
        			+ " from users";
			
        	PreparedStatement stm = conn.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                moneyBot = rs.getLong("moneyBot");
                moneyUser = rs.getLong("moneyUser");
                moneyAgent1 = rs.getLong("moneyAgent1");
                moneyAgent2 = rs.getLong("moneyAgent2");
                moneySuperAgent = rs.getLong("moneySuperAgent");
            }
        }
        catch (SQLException e) {
            throw e;
        }
        finally {
            if (conn != null) {
                conn.close();
            }
        }
        long total = moneyBot + moneyUser + (moneyAgent1 -= moneySuperAgent) + moneyAgent2 + moneySuperAgent;
        ReportTotalMoneyModel model = new ReportTotalMoneyModel(moneyBot, moneyUser, moneyAgent1, moneyAgent2, moneySuperAgent, total, null);
        return model;
    }

    @Override
    public long getCurrentMoney(String nickname) throws SQLException {
        long res = 0L;
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");){
            String sql = "SELECT vin_total FROM users WHERE nick_name=?";
            PreparedStatement stm = conn.prepareStatement("SELECT vin_total FROM users WHERE nick_name=?");
            stm.setString(1, nickname);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                res = rs.getLong("vin_total");
            }
            rs.close();
            stm.close();
        }
        return res;
    }

    @Override
    public long getSafeMoney(String nickname) throws SQLException {
        long res = 0L;
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");){
            String sql = "SELECT safe FROM users WHERE nick_name=?";
            PreparedStatement stm = conn.prepareStatement("SELECT safe FROM users WHERE nick_name=?");
            stm.setString(1, nickname);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                res = rs.getLong("safe");
            }
            rs.close();
            stm.close();
        }
        return res;
    }

    @Override
    public boolean checkBot(String nickname) throws SQLException {
        boolean res = false;
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");){
            String sql = "SELECT is_bot FROM users WHERE nick_name=?";
            PreparedStatement stm = conn.prepareStatement("SELECT is_bot FROM users WHERE nick_name=?");
            stm.setString(1, nickname);
            ResultSet rs = stm.executeQuery();
            if (rs.next() && rs.getInt("is_bot") == 1) {
                res = true;
            }
            rs.close();
            stm.close();
        }
        return res;
    }

    @Override
    public boolean saveLogTotalMoney(ReportTotalMoneyModel model) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection col = db.getCollection("report_total_money");
        Document doc = new Document();
        doc.append("money_bot", model.moneyBot);
        doc.append("money_user", model.moneyUser);
        doc.append("money_agent_1", model.moneyAgent1);
        doc.append("money_agent_2", model.moneyAgent2);
        doc.append("money_super_agent", model.moneySuperAgent);
        doc.append("time_log", VinPlayUtils.getCurrentDateTime());
        col.insertOne(doc);
        return true;
    }

    @Override
    public List<ReportTotalMoneyModel> getReportTotalMoney(int pageNumber, String startTime, String endTime) {
        final ArrayList<ReportTotalMoneyModel> res = new ArrayList<ReportTotalMoneyModel>();
        int pageSize = 50;
        int skipNumber = (pageNumber - 1) * 50;
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        Document conditions = new Document();
        if (!startTime.isEmpty() && !endTime.isEmpty()) {
            BasicDBObject obj = new BasicDBObject();
            obj.put("$gte", startTime);
            obj.put("$lte", endTime);
            conditions.put("time_log", obj);
        }
        BasicDBObject sortCondtions = new BasicDBObject();
        sortCondtions.put("_id", -1);
        FindIterable iterable = db.getCollection("report_total_money").find((Bson)conditions).sort((Bson)sortCondtions).skip(skipNumber).limit(50);
        iterable.forEach((Block)new Block<Document>(){

            public void apply(Document document) {
                ReportTotalMoneyModel model = new ReportTotalMoneyModel();
                model.moneyBot = document.getLong("money_bot");
                model.moneyUser = document.getLong("money_user");
                model.moneyAgent1 = document.getLong("money_agent_1");
                model.moneyAgent2 = document.getLong("money_agent_2");
                model.moneySuperAgent = document.getLong("money_super_agent");
                model.total = model.moneyBot + model.moneyUser + model.moneyAgent1 + model.moneyAgent2 + model.moneySuperAgent;
                model.timeLog = document.getString("time_log");
                res.add(model);
            }
        });
        return res;
    }

    @Override
    public ReportTotalMoneyModel getReportTotalMoneyAtTime(String date, boolean bStart) throws ParseException {
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        Document conditions = new Document();
        BasicDBObject obj = new BasicDBObject();
        BasicDBObject sortCondtions = new BasicDBObject();
        java.util.Date dateTime = VinPlayUtils.getDateTimeFromDate(date);
        if (bStart) {
            obj.put("$gte", VinPlayUtils.getDateTimeStr((java.util.Date)dateTime));
            sortCondtions.put("_id", 1);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateTime);
            cal.add(5, 1);
            obj.put("$lte", VinPlayUtils.getDateTimeStr((java.util.Date)cal.getTime()));
            sortCondtions.put("_id", -1);
        }
        conditions.put("time_log", obj);
        Document document = db.getCollection("report_total_money").find(conditions).sort(sortCondtions).first();
        ReportTotalMoneyModel model = new ReportTotalMoneyModel();
        if (document != null) {
            model.moneyBot = document.getLong("money_bot");
            model.moneyUser = document.getLong("money_user");
            model.moneyAgent1 = document.getLong("money_agent_1");
            model.moneyAgent2 = document.getLong("money_agent_2");
            model.moneySuperAgent = document.getLong("money_super_agent");
            model.total = model.moneyBot + model.moneyUser + model.moneyAgent1 + model.moneyAgent2 + model.moneySuperAgent;
            model.timeLog = document.getString("time_log");
        }
        return model;
    }

    @Override
    public boolean saveLogMoneyForReport(String nickname, String actionname, String date, ReportModel model) throws ParseException {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection col = null;
        col = !model.isBot ? db.getCollection("report_money_vin") : db.getCollection("report_money_vin_bot");
        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("money_win", model.moneyWin);
        updateFields.append("money_lost", model.moneyLost);
        updateFields.append("money_other", model.moneyOther);
        updateFields.append("fee", model.fee);
        updateFields.append("time_log", VinPlayUtils.getDateTimeStr((java.util.Date)VinPlayUtils.getDateTimeFromDate(date)));
        updateFields.append("create_time", VinPlayUtils.getDateTimeFromDate(date));
        BasicDBObject conditions = new BasicDBObject();
        conditions.append("nick_name", nickname);
        conditions.append("action_name", actionname);
        conditions.append("date", date);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.upsert(true);
        col.findOneAndUpdate((Bson)conditions, (Bson)new Document("$set", updateFields), options);
        return true;
    }

    @Override
    public boolean saveTopCaoThu(String nickname, String date, long moneyWin) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection col = db.getCollection("top_user_play_game_vin");
        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("money_win", moneyWin);
        BasicDBObject conditions = new BasicDBObject();
        conditions.append("nick_name", nickname);
        conditions.append("date", date);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.upsert(true);
        col.findOneAndUpdate((Bson)conditions, (Bson)new Document("$set", updateFields), options);
        return true;
    }

    @Override
    public HashMap<String, Long> getReportTopGame(String startTime, String endTime, String actionName, boolean isBot) throws Exception {
        final HashMap<String, Long> results = new HashMap<String, Long>();
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        Document conditions = new Document();
        if (!startTime.isEmpty() && !endTime.isEmpty()) {
            BasicDBObject obj = new BasicDBObject();
            obj.put("$gte", VinPlayUtils.getDateTimeStr((java.util.Date)VinPlayUtils.getDateTimeFromDate(startTime)));
            obj.put("$lte", VinPlayUtils.getDateTimeStr((java.util.Date)VinPlayUtils.getDateTimeFromDate(endTime)));
            conditions.put("time_log", obj);
        }
        conditions.put("action_name", actionName);
        MongoCollection col = null;
        if (!isBot) {
            col = db.getCollection("report_money_vin");
            AggregateIterable iterable = col.aggregate(Arrays.asList(new Document[]{new Document("$match", conditions), new Document("$group", new Document("_id", "$nick_name").append("money_win", new Document("$sum", "$money_win")).append("money_lost", new Document("$sum", "$money_lost")).append("money_other", new Document("$sum", "$money_other")))}));
            iterable.forEach((Block)new Block<Document>(){

                public void apply(Document document) {
                    String nickName = document.getString("_id");
                    long money = document.getLong("money_win") + document.getLong("money_lost") + document.getLong("money_other");
                    results.put(nickName, money);
                }
            });
            return results;
        }
        return results;
    }

    @Override
    public Map<String, ReportModel> getListReportModelByDay(final String date, final boolean isBot) throws Exception {
        final HashMap<String, ReportModel> results = new HashMap<String, ReportModel>();
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        Document conditions = new Document();
        conditions.put("time_log", VinPlayUtils.getDateTimeStr((java.util.Date)VinPlayUtils.getDateTimeFromDate(date)));
        MongoCollection col = null;
        col = !isBot ? db.getCollection("report_money_vin") : db.getCollection("report_money_vin_bot");
        FindIterable iterable = col.find((Bson)conditions);
        iterable.forEach((Block)new Block<Document>(){

            public void apply(Document document) {
                ReportModel model = new ReportModel();
                String nickname = document.getString("nick_name");
                String actionname = document.getString("action_name");
                model.moneyWin = document.getLong("money_win");
                model.moneyLost = document.getLong("money_lost");
                model.moneyOther = document.getLong("money_other");
                model.fee = document.getLong("fee");
                model.isBot = isBot;
                String key = nickname + "," + actionname + "," + date;
                results.put(key, model);
            }
        });
        return results;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void saveReportMoneyVin(Map<String, ReportModel> input) {
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO report_money_daily(action_name, money_win, money_lost, money_other, fee, date) VALUES(?, ?, ?, ?, ?, ?)");
            Calendar cal = Calendar.getInstance();
            cal.add(5, -1);
            Date yesterday = new Date(cal.getTimeInMillis());
            for (Map.Entry<String, ReportModel> entry : input.entrySet()) {
                if (entry.getValue().isBot) continue;
                stmt.setString(1, entry.getKey());
                stmt.setLong(2, entry.getValue().moneyWin);
                stmt.setLong(3, entry.getValue().moneyLost);
                stmt.setLong(4, entry.getValue().moneyOther);
                stmt.setLong(5, entry.getValue().fee);
                stmt.setDate(6, yesterday);
                stmt.execute();
            }
            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e);
            if (conn != null) {
                try {
                    conn.close();
                }
                catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        }
        finally {
            if (conn != null) {
                try {
                    conn.close();
                }
                catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getReportTransactionDetail(String action_name, String date,
     String nick_name, Date create_time, Long fee, Long money_lost, Long money_other, Long money_win,int flagTime, String fromTime, String endTime, int page, int maxItem) {

        try{
            Map<String, Object> map = new HashMap();
            List<ReportTransactionDetailModel> records = new ArrayList<ReportTransactionDetailModel>();
            List<Long> num = new ArrayList<Long>();
            num.add(0, 0L);
            MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
            MongoCollection<?> col = db.getCollection("report_money_vin");
            page = (page - 1) < 0 ? 0 : (page - 1);
            int numStart = page * maxItem;
            int numEnd = maxItem;
            BasicDBObject objsort = new BasicDBObject();
            objsort.put("_id", -1);
            HashMap<String, Object> conditions = new HashMap<String, Object>();
            if (action_name!=null && !action_name.isEmpty()) {
                conditions.put("action_name", action_name);
            }

            if (nick_name!=null && !nick_name.isEmpty()) {
                conditions.put("nick_name", nick_name);
            }
            if (fee!=null){
                BasicDBObject obj = new BasicDBObject();
                obj.put("$gte", fee);
                conditions.put("fee", obj);
            }
            if (money_lost!=null){
                BasicDBObject obj = new BasicDBObject();
                obj.put("$gte", money_lost);
                conditions.put("money_lost", obj);
            }
            if (money_other!=null){
                BasicDBObject obj = new BasicDBObject();
                obj.put("$gte", money_other);
                conditions.put("money_other", obj);
            }
            if (money_win!=null){
                BasicDBObject obj = new BasicDBObject();
                obj.put("$gte", money_win);
                conditions.put("money_win", obj);
            }
            if(flagTime == 1){
                if (fromTime!=null && !fromTime.isEmpty() && endTime!=null && !endTime.isEmpty()) {
                    try {
                        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss" );
                        java.util.Date ts = sim.parse(fromTime);
                        java.util.Date te = sim.parse(endTime);
                        BasicDBObject object = new BasicDBObject();
                        object.put("$gte", ts);
                        object.put("$lte", te);
                        conditions.put("create_time",  object);
                    } catch (ParseException e) {
                        try {
                            SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd" );
                            java.util.Date ts = sim.parse(fromTime);
                            java.util.Date te = sim.parse(endTime);
                            BasicDBObject object = new BasicDBObject();
                            object.put("$gte", ts);
                            object.put("$lte", te);
                            conditions.put("create_time",  object);
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }
                    }
                }
            } else if (flagTime == 2){
                if (fromTime!=null && !fromTime.isEmpty() && endTime!=null && !endTime.isEmpty()) {
                    BasicDBObject obj = new BasicDBObject();
                    obj.put("$gte", fromTime);
                    obj.put("$lte", endTime);
                    conditions.put("time_log", obj);
                }
            }


            FindIterable<?> iterable = col.find((Bson) new Document(conditions)).sort((Bson) objsort).skip(numStart)
                    .limit(maxItem);
            iterable.forEach((Block) new Block<Document>() {
                public void apply(Document document) {
                    ReportTransactionDetailModel model =new ReportTransactionDetailModel(
                            document.getString("action_name"),
                            document.getString("date"),
                            document.getString("nick_name"),
                            document.getDate("create_time"),
                            document.getLong("fee"),
                            document.getLong("money_lost"),
                            document.getLong("money_other"),
                            document.getLong("money_win"),
                            document.getString("time_log")
                    );
                    records.add(model);
                }
            });
            long total = col.count((Bson) new Document(conditions));//fill without paging
            map.put("listData", records);
            map.put("totalData", total);
            return map;
        } catch (Exception e) {
            return null;
        }
    }

}

