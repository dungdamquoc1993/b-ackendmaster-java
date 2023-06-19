/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.mongodb.BasicDBObject
 *  com.mongodb.Block
 *  com.mongodb.client.FindIterable
 *  com.mongodb.client.MongoCollection
 *  com.mongodb.client.MongoDatabase
 *  com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory
 *  com.vinplay.vbee.common.response.LogCaoThapResponse
 *  org.bson.Document
 *  org.bson.conversions.Bson
 */
package com.vinplay.dal.dao.impl;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DBObject;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.vinplay.dal.dao.LogCaoThapDAO;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.common.response.LogCaoThapResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

public class LogCaoThapDAOImpl
implements LogCaoThapDAO {
    @Override
    public List<LogCaoThapResponse> listCaoThap(String nickName, String transId, String bet_value, String timeStart, String timeEnd, String moneyType, int page) {
        final ArrayList<LogCaoThapResponse> results = new ArrayList<LogCaoThapResponse>();
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        FindIterable iterable = null;
        BasicDBObject obj = new BasicDBObject();
        BasicDBObject objsort = new BasicDBObject();
        int num_start = (page - 1) * 50;
        int num_end = 50;
        objsort.put("_id", -1);
        if (nickName != null && !nickName.equals("")) {
            String pattern = ".*" + nickName + ".*";
            conditions.put("nick_name", (Object)new BasicDBObject().append("$regex", (Object)pattern).append("$options", (Object)"i"));
        }
        if (transId != null && !transId.equals("")) {
            conditions.put("trans_id", Long.parseLong(transId));
        }
        if (bet_value != null && !bet_value.equals("")) {
            conditions.put("bet_value", Long.parseLong(bet_value));
        }
        if (timeStart != null && !timeStart.equals("") && timeEnd != null && !timeEnd.equals("")) {
            obj.put("$gte", (Object)timeStart);
            obj.put("$lte", (Object)timeEnd);
            conditions.put("time_log", (Object)obj);
        }
        if (moneyType != null && !moneyType.equals("")) {
            conditions.put("money_type", Integer.parseInt(moneyType));
        }
        iterable = db.getCollection("log_cao_thap").find((Bson)new Document(conditions)).sort((Bson)objsort).skip(num_start).limit(50);
        iterable.forEach((Block)new Block<Document>(){

            public void apply(Document document) {
                LogCaoThapResponse caothap = new LogCaoThapResponse();
                caothap.tran_id = document.getLong((Object)"trans_id");
                caothap.nickName = document.getString((Object)"nick_name");
                caothap.pot_bet = document.getInteger((Object)"pot_bet");
                caothap.step = document.getInteger((Object)"step");
                caothap.bet_value = document.getLong((Object)"bet_value");
                caothap.result = document.getInteger((Object)"result");
                caothap.prize = document.getLong((Object)"prize");
                caothap.cards = document.getString((Object)"cards");
                caothap.current_pot = document.getLong((Object)"current_pot");
                caothap.current_fund = document.getLong((Object)"current_fund");
                caothap.money_type = document.getInteger((Object)"money_type");
                caothap.time_log = document.getString((Object)"time_log");
                results.add(caothap);
            }
        });
        return results;
    }

    @Override
    public int countCaoThap(String nickName, String transId, String bet_value, String timeStart, String timeEnd, String moneyType) {
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        BasicDBObject obj = new BasicDBObject();
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        if (nickName != null && !nickName.equals("")) {
            String pattern = ".*" + nickName + ".*";
            conditions.put("nick_name", (Object)new BasicDBObject().append("$regex", (Object)pattern).append("$options", (Object)"i"));
        }
        if (transId != null && !transId.equals("")) {
            conditions.put("trans_id", Long.parseLong(transId));
        }
        if (bet_value != null && !bet_value.equals("")) {
            conditions.put("bet_value", Long.parseLong(bet_value));
        }
        if (timeStart != null && !timeStart.equals("") && timeEnd != null && !timeEnd.equals("")) {
            obj.put("$gte", (Object)(timeStart + " 00:00:00"));
            obj.put("$lte", (Object)(timeEnd + " 23:59:59"));
            conditions.put("time_log", (Object)obj);
        }
        if (moneyType != null && !moneyType.equals("")) {
            conditions.put("money_type", Integer.parseInt(moneyType));
        }
        int record = (int)db.getCollection("log_cao_thap").count((Bson)new Document(conditions));
        return record;
    }

    @Override
    public int countPlayerCaoThap(String nickName, String transId, String bet_value, String timeStart, String timeEnd, String moneyType) {
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        BasicDBObject obj = new BasicDBObject();
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        if (nickName != null && !nickName.equals("")) {
            String pattern = ".*" + nickName + ".*";
            conditions.put("nick_name", (Object)new BasicDBObject().append("$regex", (Object)pattern).append("$options", (Object)"i"));
        }

        if (transId != null && !transId.equals("")) {
            conditions.put("trans_id", Long.parseLong(transId));
        }
        if (bet_value != null && !bet_value.equals("")) {
            conditions.put("bet_value", Long.parseLong(bet_value));
        }
        if (timeStart != null && !timeStart.equals("") && timeEnd != null && !timeEnd.equals("")) {
            obj.put("$gte", (Object)(timeStart + " 00:00:00"));
            obj.put("$lte", (Object)(timeEnd + " 23:59:59"));
            conditions.put("time_log", (Object)obj);
        }
        if (moneyType != null && !moneyType.equals("")) {
            conditions.put("money_type", Integer.parseInt(moneyType));
        }

        DistinctIterable recordPlayer = db.getCollection("log_cao_thap").distinct("nick_name", (Bson)new Document(conditions), String.class );

        return recordPlayer.into(new ArrayList<>()).size();
    }

}

