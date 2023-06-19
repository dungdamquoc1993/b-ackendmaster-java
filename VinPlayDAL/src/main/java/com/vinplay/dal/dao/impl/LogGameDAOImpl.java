/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.mongodb.BasicDBObject
 *  com.mongodb.Block
 *  com.mongodb.client.FindIterable
 *  com.mongodb.client.MongoCollection
 *  com.mongodb.client.MongoDatabase
 *  com.vinplay.vbee.common.messages.LogGameMessage
 *  com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory
 *  org.bson.Document
 *  org.bson.conversions.Bson
 */
package com.vinplay.dal.dao.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import com.vinplay.dal.dao.LogGameDAO;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.messages.LogGameMessage;
import com.vinplay.vbee.common.models.LogReportModel;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;

import java.text.SimpleDateFormat;
import java.util.*;

import org.bson.Document;
import org.bson.conversions.Bson;

public class LogGameDAOImpl
implements LogGameDAO {
    @Override
    public List<LogGameMessage> searchLogGameByNickName(String sessionId, String nickName, String gameName, String timeStart, String timeEnd, String moneyType, int page) {
        final ArrayList<LogGameMessage> results = new ArrayList<LogGameMessage>();
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        BasicDBObject objsort = new BasicDBObject();
        BasicDBObject obj = new BasicDBObject();
        int num_start = (page - 1) * 50;
        int num_end = 50;
        FindIterable iterable = null;
        objsort.put("_id", -1);
        if (nickName != null && !nickName.equals("")) {
            conditions.put("nick_name", nickName);
        }
//        if (gameName != null && !gameName.equals("")) {
//            conditions.put("game_name", gameName);
//        }
        if (sessionId != null && !sessionId.equals("")) {
            conditions.put("session_id", sessionId);
        }
        if (moneyType != null && !moneyType.equals("")) {
            conditions.put("money_type", moneyType);
        }
        if (timeStart != null && !timeStart.equals("") && timeEnd != null && !timeEnd.equals("")) {
            obj.put("$gte", (Object)timeStart);
            obj.put("$lte", (Object)timeEnd);
            conditions.put("time_log", obj);
        }
        iterable = db.getCollection("log_game_"+gameName).find((Bson)new Document(conditions)).sort((Bson)objsort).skip(num_start).limit(50);
//        iterable = db.getCollection("log_game").find((Bson)new Document(conditions)).sort((Bson)objsort).skip(num_start).limit(50);
        iterable.forEach((Block)new Block<Document>(){

            public void apply(Document document) {
                LogGameMessage loggame = new LogGameMessage();
                loggame.sessionId = document.getString((Object)"session_id");
                loggame.gameName = document.getString((Object)"game_name");
                loggame.timeLog = document.getString((Object)"time_log");
                loggame.nickName = document.getString((Object)"nick_name");
                loggame.moneyType = document.getString((Object)"money_type");
                results.add(loggame);
            }
        });
        return results;
    }

    @Override
    public int countSearchLogGameByNickName(String sessionId, String nickName, String gameName, String timeStart, String timeEnd, String moneyType) {
        int count = 0;
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        BasicDBObject objsort = new BasicDBObject();
        BasicDBObject obj = new BasicDBObject();
        objsort.put("_id", -1);
        if (nickName != null && !nickName.equals("")) {
            conditions.put("nick_name", nickName);
        }
        if (sessionId != null && !sessionId.equals("")) {
            conditions.put("session_id", sessionId);
        }
        if (moneyType != null && !moneyType.equals("")) {
            conditions.put("money_type", moneyType);
        }
        if (timeStart != null && !timeStart.equals("") && timeEnd != null && !timeEnd.equals("")) {
            obj.put("$gte", (Object)timeStart);
            obj.put("$lte", (Object)timeEnd);
            conditions.put("time_log", obj);
        }
        count = (int) db.getCollection("log_game_"+gameName).count((Bson)new Document(conditions));
        return count;
    }

    @Override
    public int countPlayerLogGameByNickName(String sessionId, String nickName, String gameName, String timeStart, String timeEnd, String moneyType) {
        Set<String> set = new HashSet<>();
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        BasicDBObject objsort = new BasicDBObject();
        BasicDBObject obj = new BasicDBObject();
        objsort.put("_id", -1);
        if (nickName != null && !nickName.equals("")) {
            conditions.put("nick_name", nickName);
        }
        if (sessionId != null && !sessionId.equals("")) {
            conditions.put("session_id", sessionId);
        }
        if (moneyType != null && !moneyType.equals("")) {
            conditions.put("money_type", moneyType);
        }
        if (timeStart != null && !timeStart.equals("") && timeEnd != null && !timeEnd.equals("")) {
            obj.put("$gte", (Object)timeStart);
            obj.put("$lte", (Object)timeEnd);
            conditions.put("time_log", obj);
        }
        FindIterable iterable = db.getCollection("log_game_"+gameName).find((Bson)new Document(conditions));
        iterable.forEach((Block)new Block<Document>(){
            public void apply(Document document) {
                set.add(document.getString((Object)"nick_name"));
            }
        });
        return set.size();
    }

    @Override
    public LogGameMessage getLogGameDetailBySessionID(String sessionId, String gameName, String timelog) {
        final LogGameMessage loggame = new LogGameMessage();
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        HashMap<String, Object> conditions = new HashMap<String, Object>();
//        HashMap<String, String> conditions = new HashMap<String, String>();
        conditions.put("session_id", sessionId);
//        conditions.put("game_name", gameName);
        conditions.put("time_log", timelog);
        FindIterable iterable = db.getCollection("log_game_"+gameName+"_detail").find((Bson)new Document(conditions));
//        FindIterable iterable = db.getCollection("log_game_detail").find((Bson)new Document(conditions));
        iterable.forEach((Block)new Block<Document>(){

            public void apply(Document document) {
                loggame.sessionId = document.getString((Object)"session_id");
                loggame.gameName = document.getString((Object)"game_name");
                loggame.timeLog = document.getString((Object)"time_log");
                loggame.logDetail = document.getString((Object)"log_detail");
            }
        });
        return loggame;
    }
    
    public List<Map<String, Object>> searchLogGameByNickNameNEW(String sessionId, String nickName, String gameName, String timeStart, String timeEnd, String moneyType, int page) {
		List<Map<String, Object>> data = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		try {
			if (gameName == null || gameName.trim().isEmpty())
				return new ArrayList<>();
			
			String collectionName = "log_game_" + gameName;
//			Document match = new Document();
//			match = new Document("$match", 
//					new Document("time_log",
//						new Document("$gte", (Object)timeStart)
//						.append( "$lte", (Object)timeEnd)));
			
			BasicDBObject conditions = new BasicDBObject();
			if (timeStart!=null && !timeStart.isEmpty() && timeEnd!=null && !timeEnd.isEmpty()) {
                BasicDBObject obj = new BasicDBObject();
                obj.put("$gte", (Object)timeStart);
                obj.put("$lte", (Object)timeEnd);
                conditions.put("time_log", obj);
            }
					
			if(nickName != null && !nickName.trim().isEmpty()){
				conditions.append("nick_name", nickName);
			}
			
			if(sessionId != null && !sessionId.trim().isEmpty()){
				conditions.append("session_id", sessionId);
			}
			
			Document match = new Document("$match", conditions);
			Document lookup = new Document();
			lookup = new Document("$lookup", new Document("from",collectionName + "_detail")
					.append("localField","session_id").append("foreignField","session_id").append("as", "details"));
			Document set = new Document();
			set = new Document("$addFields", new Document("log_detail",new Document("$arrayElemAt", Arrays.asList("$details.log_detail", 0))));
			Document project = new Document();
			project = new Document("$project", new Document("_id", 0).append("sessionId", "$session_id")
					.append("gameName", "$game_name").append("timeLog", "$time_log").append("nickName", "$nick_name")
					.append("moneyType", "$money_type").append("createTime", "$create_time").append("detail", "$log_detail"));
			Document sort = new Document();
			sort = new Document("$sort", new Document("sessionId",-1));
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			MongoCollection<?> collection = db.getCollection(collectionName);
			int num_start = (page - 1) * 50;
	        int num_end = 50;
	        Document skip = new Document();
	        skip = new Document("$skip", num_start);
	        Document limit = new Document();
	        limit = new Document("$limit", num_end);
	        List<Object> result = new ArrayList<>();
	        result = collection.aggregate(Arrays.asList(match, skip, limit, lookup, set, project, sort))
	        		.allowDiskUse(true).into(new ArrayList<>());
	        Map<String, Object> objectMap = new HashMap<>();
	        objectMap.put("data", result);
	        data.add(objectMap);
//			for (Document document : aggregates) {
//				try {
//					Map<String, Object> objectMap = new HashMap<>();
//					objectMap.put("sessionId", document.getString("sessionId"));
//					objectMap.put("gameName", document.getString("gameName"));
//					objectMap.put("timeLog", document.getString("timeLog"));
//					objectMap.put("nickName", document.getString("nickName"));
//					objectMap.put("createTime", document.getDate("createTime") == null ? "" 
//							: simpleDateFormat.format(document.getDate("createTime")));
//					objectMap.put("detail", document.getString("detail"));
//					data.add(objectMap);
//				} catch (Exception e) { }
//			}
			
			//Get total record found
			Document count = new Document();
			count = new Document("$count", "nick_name");
			AggregateIterable<Document> aggregateCount = (AggregateIterable<Document>) collection
					.aggregate(Arrays.asList(match, count)).allowDiskUse(true);
			for (Document document : aggregateCount) {
				try {
					objectMap = new HashMap<>();
					objectMap.put("totalRecord", document.getInteger("nick_name", 0));
					data.add(objectMap);
				} catch (Exception e) { }
			}
			
			//Get total player
			Document matchPlayers = new Document();
			matchPlayers = new Document("$match", 
					new Document("time_log",
						new Document("$gte", (Object)timeStart)
						.append( "$lte", (Object)timeEnd)));
			Document group = new Document();
			group =  new Document("$group", new Document("_id", "$nick_name"));
			AggregateIterable<Document> aggregateCountPlayer = (AggregateIterable<Document>) collection
					.aggregate(Arrays.asList(matchPlayers, group, count)).allowDiskUse(true);
			for (Document document : aggregateCountPlayer) {
				try {
					objectMap = new HashMap<>();
					objectMap.put("totalPlayer", document.getInteger("nick_name", 0));
					data.add(objectMap);
				} catch (Exception e) { }
			}
		} catch (Exception e) {
		}

		return data;
	}
}

