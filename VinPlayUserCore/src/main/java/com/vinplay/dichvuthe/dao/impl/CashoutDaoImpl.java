/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.mongodb.BasicDBObject
 *  com.mongodb.Block
 *  com.mongodb.client.FindIterable
 *  com.mongodb.client.MongoCollection
 *  com.mongodb.client.MongoDatabase
 *  com.mongodb.client.result.UpdateResult
 *  com.vinplay.vbee.common.messages.dvt.CashoutByBankMessage
 *  com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory
 *  com.vinplay.vbee.common.pools.ConnectionPool
 *  com.vinplay.vbee.common.utils.VinPlayUtils
 *  org.bson.Document
 *  org.bson.conversions.Bson
 */
package com.vinplay.dichvuthe.dao.impl;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import com.vinplay.dichvuthe.dao.CashoutDao;
import com.vinplay.dichvuthe.entities.*;
import com.vinplay.dichvuthe.response.CashoutTransResponse;
import com.vinplay.dichvuthe.response.CashoutUserDailyResponse;
import com.vinplay.dichvuthe.utils.CashoutUtil;
import com.vinplay.dichvuthe.utils.DvtConst;
import com.vinplay.payment.entities.UserWithdraw;
//import com.vinplay.payment.entities.UserWithdrawMomo;
import com.vinplay.usercore.utils.GameCommon;
//import com.vinplay.vbee.common.messages.dvt.CashoutByBankMessage;
import com.vinplay.vbee.common.messages.dvt.CashoutByCardMessage;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.common.pools.ConnectionPool;
import com.vinplay.vbee.common.utils.VinPlayUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.Document;
import org.bson.conversions.Bson;

public class CashoutDaoImpl
implements CashoutDao {
    @Override
    public long getSystemCashout() throws SQLException {
        long money = 0L;
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");){
            String sql = "SELECT money FROM system_cashout WHERE date=?";
            PreparedStatement stm = conn.prepareStatement("SELECT money FROM system_cashout WHERE date=?");
            stm.setString(1, VinPlayUtils.getCurrentDate());
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                money = rs.getLong("money");
            }
            rs.close();
            stm.close();
        }
        return money;
    }

    @Override
    public boolean updateSystemCashout(long money) throws SQLException {
        boolean res = false;
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");){
            String sql = "INSERT INTO system_cashout(date, money, update_time) VALUES(?, ?, now()) ON DUPLICATE KEY UPDATE money=money+?, update_time=now()";
            PreparedStatement stm = conn.prepareStatement("INSERT INTO system_cashout(date, money, update_time) VALUES(?, ?, now()) ON DUPLICATE KEY UPDATE money=money+?, update_time=now()");
            stm.setString(1, VinPlayUtils.getCurrentDate());
            stm.setLong(2, money);
            stm.setLong(3, money);
            if (stm.executeUpdate() == 1) {
                res = true;
            }
            stm.close();
        }
        return res;
    }

    

    @Override
    public CashoutUserDailyResponse getCashoutUserToday(String nickname) {
        CashoutUserDailyResponse model = new CashoutUserDailyResponse(new Date(), 0);
        try {
            MongoDatabase db = MongoDBConnectionFactory.getDB();
            MongoCollection col = db.getCollection("log_cash_out_user_daily");
            if (col != null) {
                HashMap<String, Object> conditions = new HashMap<String, Object>();
//                HashMap<String, String> conditions = new HashMap<String, String>();
                conditions.put("nick_name", nickname);
                conditions.put("date", VinPlayUtils.getCurrentDate());
                Document doccument = (Document)col.find((Bson)new Document(conditions)).first();
                if (doccument != null) {
                    model = new CashoutUserDailyResponse(VinPlayUtils.getDateTimeFromDate((String)doccument.getString((Object)"date")), doccument.getInteger((Object)"money"));
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    @Override
    public List<CashoutTransResponse> getListCashoutByCardPending(String partner, String startTime, String endTime) throws Exception {
        final ArrayList<CashoutTransResponse> response = new ArrayList<CashoutTransResponse>();
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        FindIterable iterable = null;
        Document conditions = new Document();
        conditions.put("code", (Object)30);
        conditions.put("partner", (Object)partner);
        if (!startTime.isEmpty() && !endTime.isEmpty()) {
            BasicDBObject obj = new BasicDBObject();
            obj.put("$gte", (Object)startTime);
            obj.put("$lte", (Object)endTime);
            conditions.put("time_log", (Object)obj);
        }
        iterable = db.getCollection("dvt_cash_out_by_card").find((Bson)conditions);
        iterable.forEach((Block)new Block<Document>(){

            public void apply(Document document) {
                CashoutTransResponse message = new CashoutTransResponse(document.getString((Object)"reference_id"), document.getString((Object)"provider"), document.getInteger((Object)"amount"), document.getInteger((Object)"quantity"), document.getString((Object)"partner"), document.getString((Object)"partner_trans_id"), document.getInteger((Object)"is_scanned"), document.getInteger((Object)"code"), document.getString((Object)"message"), document.getInteger((Object)"status"), document.getString((Object)"softpin"), document.getString((Object)"nick_name"));
                response.add(message);
            }
        });
        return response;
    }

    @Override
    public List<CashoutTransResponse> getListCashoutByCardPending() throws Exception {
        final ArrayList<CashoutTransResponse> response = new ArrayList<CashoutTransResponse>();
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        FindIterable iterable = null;
        Document conditions = new Document();
        conditions.put("code", (Object)30);
        conditions.put("time_log", (Object)new BasicDBObject("$gte", (Object)VinPlayUtils.parseDateTimeToString((Date)VinPlayUtils.subtractDay((Date)new Date(), (int)GameCommon.getValueInt("TIME_RECHECK_CASHOUT_BY_CARD")))));
        iterable = db.getCollection("dvt_cash_out_by_card").find((Bson)conditions);
        iterable.forEach((Block)new Block<Document>(){

            public void apply(Document document) {
                CashoutTransResponse message = new CashoutTransResponse(document.getString((Object)"reference_id"), document.getString((Object)"provider"), document.getInteger((Object)"amount"), document.getInteger((Object)"quantity"), document.getString((Object)"partner"), document.getString((Object)"partner_trans_id"), document.getInteger((Object)"is_scanned"), document.getInteger((Object)"code"), document.getString((Object)"message"), document.getInteger((Object)"status"), document.getString((Object)"softpin"), document.getString((Object)"nick_name"));
                response.add(message);
            }
        });
        return response;
    }

    @Override
    public void updateCashOutByCard(String referenceId, String softpin, int status, String message, int code, int isScanned) throws Exception {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("softpin", (Object)softpin);
        updateFields.append("status", (Object)status);
        updateFields.append("message", (Object)message);
        updateFields.append("code", (Object)code);
        updateFields.append("is_scanned", (Object)isScanned);
        updateFields.append("update_time", (Object)VinPlayUtils.getCurrentDateTime());
        db.getCollection("dvt_cash_out_by_card").updateOne((Bson)new Document("reference_id", (Object)referenceId), (Bson)new Document("$set", (Object)updateFields));
    }

    @Override
    public void insertCardIntoDB(String provider, int amount, String serial, String pin, String expire) throws Exception {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection col = db.getCollection("card_store");
        Document doc = new Document();
        doc.append("reference_id", (Object)VinPlayUtils.genMessageId());
        doc.append("provider", (Object)provider);
        doc.append("amount", (Object)amount);
        doc.append("serial", (Object)serial);
        doc.append("pin", (Object)pin);
        doc.append("expire_time", (Object)expire);
        doc.append("status", (Object)0);
        doc.append("message", (Object)"Th\u00e1\u00ba\u00bb ch\u00c6\u00b0a s\u00e1\u00bb\u00ad d\u00e1\u00bb\u00a5ng");
        doc.append("create_time", (Object)VinPlayUtils.getCurrentDateTime());
        doc.append("update_time", (Object)VinPlayUtils.getCurrentDateTime());
        doc.append("partner_trans_id", (Object)"");
        col.insertOne((Object)doc);
    }

    
    public boolean InsertCashoutByBankManual(UserWithdraw userWithdraw) {
        try{
            MongoDatabase db = MongoDBConnectionFactory.getDB();
            MongoCollection col = db.getCollection(CashoutUtil.CASHOUT_BY_BANK_COLLECTION);
            Gson gson = new Gson();
            String json = gson.toJson(userWithdraw);
            // Parse to bson document and insert
            Document doc = Document.parse(json);

            col.insertOne((Object)doc);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    

    

    

    

    

    

    

    

   

    
}

