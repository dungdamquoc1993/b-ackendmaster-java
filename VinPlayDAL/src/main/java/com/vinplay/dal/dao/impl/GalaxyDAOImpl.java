package com.vinplay.dal.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.vinplay.dal.dao.GalaxyDAO;
import com.vinplay.vbee.common.models.minigame.galaxy.LSGDGalaxy;
import com.vinplay.vbee.common.models.minigame.galaxy.TopGalaxy;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GalaxyDAOImpl implements GalaxyDAO {
    @Override
    public List<TopGalaxy> getTopGalaxy(int moneyType, int pageNumber) {
        int pageSize = 10;
        int skipNumber = (pageNumber - 1) * 10;
        final ArrayList<TopGalaxy> results = new ArrayList<TopGalaxy>();
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        FindIterable iterable = null;
        Document conditions = new Document();
        conditions.put("$or", Arrays.asList(new Document[]{new Document("result", (Object)3), new Document("result", (Object)4)}));
        conditions.put("money_type", (Object)moneyType);
        BasicDBObject sortCondtions = new BasicDBObject();
        sortCondtions.put("_id", -1);
        iterable = db.getCollection("log_GALAXY").find((Bson)conditions).sort((Bson)sortCondtions).skip(skipNumber).limit(10);
        iterable.forEach((Block)new Block<Document>(){

            public void apply(Document document) {
                TopGalaxy entry = new TopGalaxy();
                entry.un = document.getString((Object)"user_name");
                entry.bv = document.getLong((Object)"bet_value");
                entry.pz = document.getLong((Object)"prize");
                entry.ts = document.getString((Object)"time_log");
                results.add(entry);
            }
        });
        return results;
    }

    @Override
    public int countLSGD(String username, int moneyType) {
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        Document conditions = new Document();
        conditions.put("user_name", (Object)username);
        conditions.put("money_type", (Object)moneyType);
        long totalRows = db.getCollection("log_GALAXY").count((Bson)conditions);
        return (int)totalRows;
    }

    @Override
    public List<LSGDGalaxy> getLSGD(String username, int moneyType, int pageNumber) {
        int pageSize = 10;
        int skipNumber = (pageNumber - 1) * 10;
        final ArrayList<LSGDGalaxy> results = new ArrayList<LSGDGalaxy>();
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        FindIterable iterable = null;
        Document conditions = new Document();
        conditions.put("user_name", (Object)username);
        conditions.put("money_type", (Object)moneyType);
        BasicDBObject sortCondtions = new BasicDBObject();
        sortCondtions.put("_id", -1);
        iterable = db.getCollection("log_GALAXY").find((Bson)conditions).sort((Bson)sortCondtions).skip(skipNumber).limit(10);
        iterable.forEach((Block)new Block<Document>(){

            public void apply(Document document) {
                LSGDGalaxy entry = new LSGDGalaxy();
                entry.rf = document.getLong((Object)"reference_id");
                entry.un = document.getString((Object)"user_name");
                entry.bv = document.getLong((Object)"bet_value");
                entry.pz = document.getLong((Object)"prize");
                entry.lb = document.getString((Object)"lines_betting");
                entry.lw = document.getString((Object)"lines_win");
                entry.ps = document.getString((Object)"prizes_on_line");
                entry.ts = document.getString((Object)"time_log");
                results.add(entry);
            }
        });
        return results;
    }

    @Override
    public long getLastRefenceId() {
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        HashMap conditions = new HashMap();
        BasicDBObject objsort = new BasicDBObject();
        objsort.put("_id", -1);
        FindIterable iterable = db.getCollection("log_GALAXY").find((Bson)new Document(conditions)).sort((Bson)objsort).limit(1);
        Document document = iterable != null ? (Document)iterable.first() : null;
        long referenceId = document == null ? 0L : document.getLong((Object)"reference_id");
        return referenceId;
    }

    @Override
    public List<TopGalaxy> getTop(int moneyType, int number) {
        final ArrayList<TopGalaxy> results = new ArrayList<TopGalaxy>();
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        FindIterable iterable = null;
        Document conditions = new Document();
        conditions.put("$or", Arrays.asList(new Document[]{new Document("result", (Object)3), new Document("result", (Object)4)}));
        conditions.put("money_type", (Object)moneyType);
        BasicDBObject sortCondtions = new BasicDBObject();
        sortCondtions.put("_id", -1);
        iterable = db.getCollection("log_GALAXY").find((Bson)conditions).sort((Bson)sortCondtions).limit(number);
        iterable.forEach((Block)new Block<Document>(){

            public void apply(Document document) {
                TopGalaxy entry = new TopGalaxy();
                entry.un = document.getString((Object)"user_name");
                entry.bv = document.getLong((Object)"bet_value");
                entry.pz = document.getLong((Object)"prize");
                entry.ts = document.getString((Object)"time_log");
                results.add(entry);
            }
        });
        return results;
    }
}
