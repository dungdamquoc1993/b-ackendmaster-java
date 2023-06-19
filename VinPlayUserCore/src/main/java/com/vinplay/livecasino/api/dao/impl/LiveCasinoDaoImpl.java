package com.vinplay.livecasino.api.dao.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.vinplay.livecasino.api.dao.LiveCasinoService;
import com.vinplay.livecasino.api.response.LiveCasinoUserResponse;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import org.bson.Document;

public class LiveCasinoDaoImpl implements LiveCasinoService {
    @Override
    public boolean insertUserCasino(String user, String pass) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection<Document> col = db.getCollection("live_casino_user");
        Document doc = new Document();
        doc.append("user_name", user);
        doc.append("pass_word", pass);
        col.insertOne(doc);
        return true;
    }

    @Override
    public LiveCasinoUserResponse getUserCasino(String userName) {
        LiveCasinoUserResponse result = null;
        Document conditions = new Document();
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        conditions.put("user_name", userName);
        Document dc = db.getCollection("live_casino_user").find(conditions).first();
        if (dc != null) {
            result = new LiveCasinoUserResponse(dc.getString("user_name"), dc.getString("pass_word"));
        }
        return result;
    }
}
