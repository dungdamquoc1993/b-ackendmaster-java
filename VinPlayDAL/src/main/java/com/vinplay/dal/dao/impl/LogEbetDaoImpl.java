package com.vinplay.dal.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.vinplay.dal.dao.LogEbetDao;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;

public class LogEbetDaoImpl implements LogEbetDao {
	private static final Logger LOGGER = Logger.getLogger(LogEbetDaoImpl.class);
	private static final String COLLECTION_EBETRECORD = "ebetgamerecord";

	@Override
	public Map<String, Object> search(String nickName, String timeStart, String timeEnd, int flagTime, String ebetId, int page, int limitItem) {
		Map<String, Object> data = new HashMap<>();
		try {
			BasicDBObject conditions = new BasicDBObject();
			if (timeStart != null && !timeStart.isEmpty() && timeEnd != null && !timeEnd.isEmpty()) {
				BasicDBObject obj = new BasicDBObject();
				obj.put("$gte", timeStart + " 00:00:00");
				obj.put("$lte", timeEnd + " 23:59:59");
				switch (flagTime) {
					case 1:
						conditions.put("createtime", obj);
						break;
					case 2:
						conditions.put("payouttime", obj);
						break;
				}
			}

			if (!StringUtils.isBlank(nickName)) {
				conditions.append("nick_name", nickName);
			}
			
			if (!StringUtils.isBlank(ebetId)) {
				conditions.append("ebetid", ebetId);
			}

			Document match = new Document("$match", conditions);
			Document sort = new Document();
			switch (flagTime) {
				case 1:
					sort = new Document("$sort", new Document("createtime", -1));
					break;
				case 2:
					sort = new Document("$sort", new Document("payouttime", -1));
					break;
				default:
					sort = new Document("$sort", new Document("createtime", -1));
					break;
			}
			
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			MongoCollection<?> collection = db.getCollection(COLLECTION_EBETRECORD);
			int num_start = limitItem == -1 ? 0 : (page - 1) * limitItem;
			int num_end = limitItem == -1 ? 50 : limitItem;
			Document skip = new Document();
			skip = new Document("$skip", num_start);
			Document limit = new Document();
			limit = new Document("$limit", num_end);
			Document addField = new Document();
			addField = new Document("$addFields", new BasicDBObject("id",new Document("$toString", "$_id")));
			Document project = new Document();
			project = new Document("$project", new Document("_id", 0));
			List<Object> result = new ArrayList<>();
			result = collection.aggregate(Arrays.asList(match, addField, project, sort, skip, limit)).allowDiskUse(true)
					.into(new ArrayList<>());
			data.put("ebetrecords", result);
			if(result.size() == 0 || result.isEmpty()) {
				data.put("totalRecord", 0);
				data.put("totalPlayer", 0);
				return data;
			}
			
			// Get total record found
			Document count = new Document();
			count = new Document("$count", "nick_name");
			AggregateIterable<Document> aggregateCount = (AggregateIterable<Document>) collection
					.aggregate(Arrays.asList(match, count)).allowDiskUse(true);
			for (Document document : aggregateCount) {
				try {
					data.put("totalRecord", document.getInteger("nick_name", 0));
				} catch (Exception e) {
				}
			}
			
			// Get total bet found
			Document sumBet = new Document();
			BasicDBObject totalBetCondis = new BasicDBObject();
			totalBetCondis.put("_id", 0);
			totalBetCondis.put("totalBet", new BasicDBObject("$sum", "$bet"));
			totalBetCondis.put("totalValidbet", new BasicDBObject("$sum", "$validbet"));
			sumBet = new Document("$group", totalBetCondis);
			Long totalBet = 0l; Long totalValidbet = 0l;
			AggregateIterable<Document> aggregateTotalBet = (AggregateIterable<Document>) collection
					.aggregate(Arrays.asList(match, sumBet)).allowDiskUse(true);
			for (Document document : aggregateTotalBet) {
				try {
					totalBet = document.getLong("totalBet");
					totalValidbet = document.getLong("totalBet");
				} catch (Exception e) {
				}
			}
			
			data.put("totalBet", totalBet);
			data.put("totalValidbet", totalValidbet);
			
			// Get total player
			Document matchPlayers = new Document();
			if (timeStart != null && !timeStart.isEmpty() && timeEnd != null && !timeEnd.isEmpty()) {
				switch (flagTime) {
					case 1:
						matchPlayers = new Document("$match", new Document("createtime",
								new Document("$gte", timeStart + " 00:00:00").append("$lte", timeEnd + " 23:59:59")));
						break;
					case 2:
						matchPlayers = new Document("$match", new Document("payouttime",
								new Document("$gte", timeStart + " 00:00:00").append("$lte", timeEnd + " 23:59:59")));
						break;
					default:
						matchPlayers = new Document("$match", new Document("createtime", new Document("$exists", true)));
						break;
				}
			} else {
				matchPlayers = new Document("$match", new Document("createtime", new Document("$exists", true)));
			}

			Document group = new Document();
			group = new Document("$group", new Document("_id", "$nick_name"));
			AggregateIterable<Document> aggregateCountPlayer = (AggregateIterable<Document>) collection
					.aggregate(Arrays.asList(matchPlayers, group, count)).allowDiskUse(true);
			for (Document document : aggregateCountPlayer) {
				try {
					data.put("totalPlayer", document.getInteger("nick_name", 0));
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			LOGGER.error("Search EbetGameRecord error: " + e.getMessage());
			data = new HashMap<>();
			data.put("ebetrecords", new ArrayList<>());
			data.put("totalRecord", 0);
			data.put("totalPlayer", 0);
		}

		return data;
	}
	
	@Override
	public Object detail(String id) {
		try {
			BasicDBObject conditions = new BasicDBObject();
			if (!StringUtils.isBlank(id)) {
				conditions.put("_id", new ObjectId(id));
			}else 
				return null;

			Document match = new Document("$match", conditions);
			Document project = new Document();
			project = new Document("$project", new Document("_id", 0));
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			MongoCollection<?> collection = db.getCollection(COLLECTION_EBETRECORD);
			return collection.aggregate(Arrays.asList(match, project)).allowDiskUse(true)
					.first();
		} catch (Exception e) {
			LOGGER.error("Search EbetGameRecord error: " + e.getMessage());
			return null;
		}
	}
}
