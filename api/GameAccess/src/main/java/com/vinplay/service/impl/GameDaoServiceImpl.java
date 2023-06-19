package com.vinplay.service.impl;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.vinplay.service.GameDaoService;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.common.utils.VinPlayUtils;

public class GameDaoServiceImpl implements GameDaoService {

	public static final Logger LOGGER = Logger.getLogger(GameDaoServiceImpl.class);
	private static final String COLLECTION_LAST_LOG_TIME="last_logtime";

	// get max field
	@Override
	public Integer getMaxFieldValue(String fieldName, String collectionName) {
		MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
		MongoCollection<Document> collection = db.getCollection(collectionName);
		FindIterable<?> cursor = collection.find().sort(new BasicDBObject(fieldName, -1)).limit(1);
		Document doc = cursor != null ? (Document)cursor.first() : null;
		int id = 0;
		if (doc != null) {
			id = doc.getInteger(fieldName);
		} else {
			return 0;
		}
		return id;
	}
	
//	private boolean insertCollection(String gameName, String collectionName) {
//		try {
//			MongoDatabase db = MongoDBConnectionFactory.getDB();
//			MongoCollection<Document> collection = db.getCollection(COLLECTION_LAST_LOG_TIME);
//
//			
//			Document doc = new Document();
//			doc.append("agcountid", gameName);
//			doc.append("update_date", lastTime);
//			collection.insertOne(doc);
//			return true;
//		} catch (Exception e) {
//			LOGGER.error(e);
//		}
//		return false;
//	}

	// count number of remain user
	@Override
	public Integer countUserRemain(String nickName, String collectionName) {
		MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
		MongoCollection<Document> collection = db.getCollection(collectionName);

		Document conditions = new Document();
		conditions.put("nick_name", nickName);
		FindIterable<Document> iterable = collection.find(conditions);
		int count = 0;
		try (MongoCursor<Document> iterator = iterable.iterator()) {
			while (iterator.hasNext()) {
				iterator.next();
				count++;
			}
		} catch (Exception e) {
			LOGGER.error(e);
			count =0;
		}
		return count;
	}

	// find game 3rd id by Nickname
	@Override
	public String findGameUserIdByNickName(String idName, String nickName, String collectionName) {
		MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
		MongoCollection<Document> collection = db.getCollection(collectionName);

		Document conditions = new Document();
		conditions.put("nick_name", nickName);
		FindIterable<Document> iterable = collection.find(conditions);
		Document doc = null;
		try (MongoCursor<Document> iterator = iterable.iterator()) {
			while (iterator.hasNext()) {
				doc = iterator.next();
				break;
			}
		}
		String gameId = "";
		if (doc != null) {
			gameId = doc.getString(idName);
		}
		return gameId;
	}
	
	// find nickName by GameID
	@Override
	public String findNickNameByGameUserId(String idKey, String idValue, String collectionName) {
		MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
		MongoCollection<Document> collection = db.getCollection(collectionName);

		Document conditions = new Document();
		conditions.put(idKey, idValue);
		FindIterable<Document> iterable = collection.find(conditions);
		Document doc = null;
		try (MongoCursor<Document> iterator = iterable.iterator()) {
			while (iterator.hasNext()) {
				doc = iterator.next();
				break;
			}
		}
		String nickName = "";
		if (doc != null) {
			nickName = doc.getString("nick_name");
		}
		return nickName;
	}

	@Override
	public String getLastUpdateTime(String gameName) {
		MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        MongoCollection<Document> collection = db.getCollection(COLLECTION_LAST_LOG_TIME);
        
        Document conditions = new Document();
        conditions.put("game_name", gameName);
        FindIterable<?> iterable = collection.find(conditions).limit(1);
		Document doc = iterable != null ? (Document)iterable.first() : null;
		String lastUpdateTime = "";
		if (doc != null) {
			lastUpdateTime = doc.getString("update_date");
		}
//		if("".equals(lastUpdateTime)) {
//			//insert
//			if(gameName.equals("ag")) {
//				insertLastTime(gameName, "2021-04-23 11:59:59");
//			}else if (gameName.equals("ibc2")) {
//				insertLastTime(gameName, "0");
//			}
//			
//		}
		return lastUpdateTime;
	}

	@Override
	public boolean updateLastTime(String gameName, String lastTime) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			MongoCollection<Document> collection = db.getCollection(COLLECTION_LAST_LOG_TIME);
			BasicDBObject doc = new BasicDBObject();
			Document conditions = new Document();
			conditions.put("game_name", gameName);
			
			doc.append("update_date", lastTime);
			collection.updateOne(conditions, new Document("$set", doc));
			return true;
		} catch (Exception e) {
			LOGGER.error(e);
		}
		return false;
	}
	
	private boolean insertLastTime(String gameName, String lastTime) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			MongoCollection<Document> collection = db.getCollection(COLLECTION_LAST_LOG_TIME);

			Document conditions = new Document();
			conditions.put("game_name", gameName);
			Document doc = new Document();
			doc.append("game_name", gameName);
			doc.append("update_date", lastTime);
			collection.insertOne(doc);
			return true;
		} catch (Exception e) {
			LOGGER.error(e);
		}
		return false;
	}
	

}
