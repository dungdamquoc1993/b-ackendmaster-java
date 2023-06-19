package com.vinplay.payment.dao.impl;

import java.util.*;
import com.mongodb.client.AggregateIterable;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.vinplay.payment.dao.AgentTransactionsDao;
import com.vinplay.payment.entities.AgentTransaction;
import com.vinplay.payment.utils.PayCommon.PAYSTATUS;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.common.utils.VinPlayUtils;

public class AgentTransactionsDaoImpl implements AgentTransactionsDao {

	public static final Logger logger = Logger.getLogger(AgentTransactionsDaoImpl.class);
	private static final String COLLECTION = "agent_transactions";

	@Override
	public long create(AgentTransaction model) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			MongoCollection<Document> col = db.getCollection(COLLECTION);
			Document doc = new Document();
			long Id = VinPlayUtils.generateTransId();
			doc.append("Id", String.valueOf(Id));
			doc.append("CreatedAt", VinPlayUtils.getCurrentDateTime());
			doc.append("ModifiedAt", VinPlayUtils.getCurrentDateTime());
			doc.append("IsDeleted", false);
			doc.append("AgentId", model.AgentId);
			doc.append("Username", model.Username);
			doc.append("Nickname", model.Nickname);
			doc.append("AgentCode", model.AgentCode);
			doc.append("RequestTime", VinPlayUtils.getCurrentDateTime());
			doc.append("Point", model.Point);
			doc.append("Money", model.Money);
			doc.append("Fee", model.Fee);
			doc.append("Bonus", model.Bonus);
			doc.append("Status", PAYSTATUS.PENDING.getId());
			doc.append("FromBankNumber", model.FromBankNumber);
			doc.append("ToBankNumber", model.ToBankNumber);
			doc.append("Content", model.Content);
			doc.append("Description", model.Description);
			doc.append("UserApprove", model.UserApprove);
			col.insertOne((Document) doc);
			return Id;
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public Boolean updateStatus(String id, int status, long fee, String description, String userApprove) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			BasicDBObject updateFields = new BasicDBObject();
			updateFields.append("Status", status);
			updateFields.append("Fee", fee);
			updateFields.append("ModifiedAt", VinPlayUtils.getCurrentDateTime());
			updateFields.append("Description", description);
			updateFields.append("UserApprove", userApprove);
			db.getCollection(COLLECTION).updateOne(new Document("Id", id), new Document("$set", updateFields));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Boolean updateStatus(String id, int status, String description, String userApprove) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			BasicDBObject updateFields = new BasicDBObject();
			updateFields.append("Status", status);
			updateFields.append("ModifiedAt", VinPlayUtils.getCurrentDateTime());
			updateFields.append("Description", description);
			updateFields.append("UserApprove", userApprove);
			db.getCollection(COLLECTION).updateOne(new Document("Id", id), new Document("$set", updateFields));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Boolean delete(String id, String description, String userApprove) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			BasicDBObject updateFields = new BasicDBObject();
			updateFields.append("IsDeleted", true);
			updateFields.append("ModifiedAt", VinPlayUtils.getCurrentDateTime());
			updateFields.append("Description", description);
			updateFields.append("UserApprove", userApprove);
			db.getCollection(COLLECTION).updateOne(new Document("Id", id), new Document("$set", updateFields));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public AgentTransaction getById(String Id) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			Document conditions = new Document();
			conditions.put("Id", Id);
			Document document = (Document) db.getCollection(COLLECTION).find(conditions).first();
			if (document == null)
				return null;
			AgentTransaction model = new AgentTransaction(document.getString("Id"), document.getString("CreatedAt"),
					document.getString("ModifiedAt"), document.getBoolean("IsDeleted"), document.getString("AgentId"),
					document.getString("Username"), document.getString("Nickname"), document.getString("AgentCode"),
					document.getString("RequestTime"), document.getLong("Point"), document.getLong("Money"),
					document.getLong("Fee"), document.getLong("Bonus"), document.getInteger("Status"),
					document.getString("ToBankNumber"), document.getString("FromBankNumber"),
					document.getString("Content"), document.getString("Description"),
					document.getString("UserApprove"));
			return model;
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	@Override
	public Map<String, Object> search(String keyword, int status, String timeStart, String timeEnd, int page) {
		Map<String, Object> data = new HashMap<>();
		try {

			List<Object> lstConditions = new ArrayList<>();
			BasicDBObject condCreatedAt = new BasicDBObject();
			if (!StringUtils.isBlank(timeStart) && !StringUtils.isBlank(timeEnd)) {
				BasicDBObject obj = new BasicDBObject();
				obj.put("$gte", timeStart + " 00:00:00");
				obj.put("$lte", timeEnd + " 23:59:59");
				condCreatedAt.put("RequestTime", obj);
				lstConditions.add(condCreatedAt);
			}

			BasicDBObject condKeyword = new BasicDBObject();
			List<Object> lstKeyword = new ArrayList<>();
			if (!StringUtils.isBlank(keyword)) {
				lstKeyword.add(new BasicDBObject("AgentId", keyword));
				lstKeyword.add(new BasicDBObject("Username", keyword));
				lstKeyword.add(new BasicDBObject("Nickname", keyword));
				lstKeyword.add(new BasicDBObject("AgentCode", keyword));
				condKeyword.put("$or", lstKeyword);
				lstConditions.add(condKeyword);
			}

			BasicDBObject condStatus = new BasicDBObject();
			if (status > -1) {
				condStatus.put("Status", status);
				lstConditions.add(condStatus);
			}

			BasicDBObject condIsDel = new BasicDBObject();
			condIsDel.put("IsDeleted", false);
			lstConditions.add(condIsDel);

			BasicDBObject conditions = new BasicDBObject();
			conditions.put("$and", lstConditions);
			Document match = new Document("$match", conditions);
			Document project = new Document();
			project = new Document("$project", new Document("_id", 0));
			Document sort = new Document();
			sort = new Document("$sort", new Document("CreatedAt", -1));
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			MongoCollection<?> collection = db.getCollection(COLLECTION);
			int num_start = (page - 1) * 50;
			int num_end = 50;
			Document skip = new Document();
			skip = new Document("$skip", num_start);
			Document limit = new Document();
			limit = new Document("$limit", num_end);
			List<Object> result = new ArrayList<>();
			result = collection.aggregate(Arrays.asList(match, sort, skip, limit, project)).allowDiskUse(true)
					.into(new ArrayList<>());
			data.put("transactions", result);

			// Get total record
			Document count = new Document();
			count = new Document("$count", "Nickname");
			AggregateIterable<Document> aggregateCount = (AggregateIterable<Document>) collection
					.aggregate(Arrays.asList(match, count)).allowDiskUse(true);
			for (Document document : aggregateCount) {
				try {
					data.put("totalRecord", document.getInteger("Nickname", 0));
				} catch (Exception e) {
				}
			}
			
			// Get total money
			Document sumMoney = new Document();
			BasicDBObject totalBetCondis = new BasicDBObject();
			totalBetCondis.put("_id", 0);
			totalBetCondis.put("totalMoney", new BasicDBObject("$sum", "$Money"));
			sumMoney = new Document("$group", totalBetCondis);
			Long totalMoney = 0l;
			AggregateIterable<Document> aggregateTotalBet = (AggregateIterable<Document>) collection
					.aggregate(Arrays.asList(match, sumMoney)).allowDiskUse(true);
			for (Document document : aggregateTotalBet) {
				try {
					totalMoney = document.getLong("totalMoney");
				} catch (Exception e) {
				}
			}

			data.put("totalMoney", totalMoney);
			
			// Get total player
			Document group = new Document();
			group = new Document("$group", new Document("_id", "$Nickname"));
			AggregateIterable<Document> aggregateCountPlayer = (AggregateIterable<Document>) collection
					.aggregate(Arrays.asList(match, group, count)).allowDiskUse(true);
			for (Document document : aggregateCountPlayer) {
				try {
					data.put("totalPlayer", document.getInteger("Nickname", 0));
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			logger.error("Search AgentTransactions error: " + e.getMessage());
			data.put("transactions", new ArrayList<>());
			data.put("totalRecord", 0);
			data.put("totalPlayer", 0);
			data.put("totalMoney", 0);
		}

		return data;
	}

	@Override
	public Map<String, Object> searchWithAgentCode(String agentCode, int status, String timeStart, String timeEnd,
			int page) {
		Map<String, Object> data = new HashMap<>();
		try {

			List<Object> lstConditions = new ArrayList<>();
			BasicDBObject condCreatedAt = new BasicDBObject();
			if (!StringUtils.isBlank(timeStart) && !StringUtils.isBlank(timeEnd)) {
				BasicDBObject obj = new BasicDBObject();
				obj.put("$gte", timeStart + " 00:00:00");
				obj.put("$lte", timeEnd + " 23:59:59");
				condCreatedAt.put("RequestTime", obj);
				lstConditions.add(condCreatedAt);
			}

			BasicDBObject condKeyword = new BasicDBObject();
			List<Object> lstKeyword = new ArrayList<>();
			if (!StringUtils.isBlank(agentCode)) {
				lstKeyword.add(new BasicDBObject("AgentCode", agentCode));
				condKeyword.put("$or", lstKeyword);
				lstConditions.add(condKeyword);
			}

			BasicDBObject condStatus = new BasicDBObject();
			if (status > -1) {
				condStatus.put("Status", status);
				lstConditions.add(condStatus);
			}

			BasicDBObject condIsDel = new BasicDBObject();
			condIsDel.put("IsDeleted", false);
			lstConditions.add(condIsDel);

			BasicDBObject conditions = new BasicDBObject();
			conditions.put("$and", lstConditions);
			Document match = new Document("$match", conditions);
			Document project = new Document();
			project = new Document("$project", new Document("_id", 0));
			Document sort = new Document();
			sort = new Document("$sort", new Document("CreatedAt", -1));
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			MongoCollection<?> collection = db.getCollection(COLLECTION);
			int num_start = (page - 1) * 50;
			int num_end = 50;
			Document skip = new Document();
			skip = new Document("$skip", num_start);
			Document limit = new Document();
			limit = new Document("$limit", num_end);
			List<Object> result = new ArrayList<>();
			result = collection.aggregate(Arrays.asList(match, sort, skip, limit, project)).allowDiskUse(true)
					.into(new ArrayList<>());
			data.put("transactions", result);
			if(result.size() == 0 || result.isEmpty()) {
				data.put("totalRecord", 0);
				data.put("totalPlayer", 0);
				data.put("totalMoney", 0);
				return data;
			}

			// Get total record
			Document count = new Document();
			count = new Document("$count", "Nickname");
			AggregateIterable<Document> aggregateCount = (AggregateIterable<Document>) collection
					.aggregate(Arrays.asList(match, count)).allowDiskUse(true);
			for (Document document : aggregateCount) {
				try {
					data.put("totalRecord", document.getInteger("Nickname", 0));
				} catch (Exception e) {
				}
			}

			// Get total money
			Document sumMoney = new Document();
			BasicDBObject totalBetCondis = new BasicDBObject();
			totalBetCondis.put("_id", 0);
			totalBetCondis.put("totalMoney", new BasicDBObject("$sum", "$Money"));
			sumMoney = new Document("$group", totalBetCondis);
			Long totalMoney = 0l;
			AggregateIterable<Document> aggregateTotalBet = (AggregateIterable<Document>) collection
					.aggregate(Arrays.asList(match, sumMoney)).allowDiskUse(true);
			for (Document document : aggregateTotalBet) {
				try {
					totalMoney = document.getLong("totalMoney");
				} catch (Exception e) {
				}
			}

			data.put("totalMoney", totalMoney);
			
			// Get total player
			Document group = new Document();
			group = new Document("$group", new Document("_id", "$Nickname"));
			AggregateIterable<Document> aggregateCountPlayer = (AggregateIterable<Document>) collection
					.aggregate(Arrays.asList(match, group, count)).allowDiskUse(true);
			for (Document document : aggregateCountPlayer) {
				try {
					data.put("totalPlayer", document.getInteger("Nickname", 0));
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			logger.error("Search AgentTransactions error: " + e.getMessage());
			data = new HashMap<>();
			data.put("transactions", new ArrayList<>());
			data.put("totalRecord", 0);
			data.put("totalPlayer", 0);
			data.put("totalMoney", 0);
		}

		return data;
	}
}
