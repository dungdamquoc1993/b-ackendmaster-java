/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.mongodb.client.MongoCollection
 *  com.mongodb.client.MongoDatabase
 *  com.vinplay.vbee.common.messages.LogGameMessage
 *  com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory
 *  org.bson.Document
 */
package com.vinplay.vbee.dao.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.vinplay.vbee.common.messages.LogGameMessage;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.dao.LogGameDao;
import java.util.Date;
import org.bson.Document;

public class LogGameDaoImpl implements LogGameDao {
    @Override
    public boolean saveLogGameByNickName(LogGameMessage message) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        String gameName = message.gameName;
        MongoCollection col = db.getCollection("log_game_" + gameName);
        Document doc = new Document();
        doc.append("nick_name", message.nickName);
        doc.append("session_id", message.sessionId);
        doc.append("game_name", message.gameName);
        doc.append("time_log", message.timeLog);
        doc.append("create_time", new Date());
        doc.append("money_type", message.moneyType);
        col.insertOne(doc);
        return true;
    }

    @Override
    public boolean saveLogGameDetail(LogGameMessage message) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        String gameName = message.gameName;
        MongoCollection col = db.getCollection("log_game_"+gameName+"_detail");
        Document doc = new Document();
        doc.append("log_detail", message.logDetail);
        doc.append("session_id", message.sessionId);
        doc.append("game_name", message.gameName);
        doc.append("time_log", message.timeLog);
        doc.append("create_time", new Date());
        col.insertOne(doc);
        return true;
    }
}

