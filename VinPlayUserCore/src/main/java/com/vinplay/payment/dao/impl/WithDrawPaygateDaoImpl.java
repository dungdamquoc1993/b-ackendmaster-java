package com.vinplay.payment.dao.impl;

import java.text.SimpleDateFormat;
import java.util.*;

import com.mongodb.client.model.Projections;
import com.mongodb.util.JSON;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.vinplay.dichvuthe.utils.DvtConst;
import com.vinplay.payment.dao.WithDrawPaygateDao;
import com.vinplay.payment.entities.Bank;
import com.vinplay.payment.entities.WithDrawPaygateModel;
import com.vinplay.payment.entities.WithDrawPaygateReponse;
import com.vinplay.payment.utils.PayCommon;
import com.vinplay.payment.utils.PayCommon.PAYSTATUS;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.common.utils.DateTimeUtils;
import com.vinplay.vbee.common.utils.VinPlayUtils;

public class WithDrawPaygateDaoImpl implements WithDrawPaygateDao {

	public static final Logger logger = Logger.getLogger(WithDrawPaygateDaoImpl.class);
	private static final String WITHDRAW_COLLECTION = "withdrawal_paygate";

	/**
	 * Check exist transaction is pending by nickname and provider name
	 * 
	 * @param nickname
	 * @param providerName
	 * @return true: exist; false: not exist
	 */
	@Override
	public boolean CheckPending(String nickname, String providerName) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			Document conditions = new Document();
			conditions.put("Nickname", nickname);
			conditions.put("ProviderName", providerName);
			conditions.put("Status", PayCommon.PAYSTATUS.PENDING);
			Document dc = (Document) db.getCollection(WITHDRAW_COLLECTION).find((Bson) conditions).first();
			return dc != null;
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
	}

	@Override
	public long countNumberWithdrawSuccessInDay(String nickname) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			Document conditions = new Document();
			conditions.put("Nickname", nickname);
			conditions.put("Status", PAYSTATUS.COMPLETED.getId());
			BasicDBObject obj = new BasicDBObject();
			obj.put("$gte", DateTimeUtils.getStartTimeToDay());
			obj.put("$lte", DateTimeUtils.getEndTimeToDay());
			conditions.put("ModifiedAt", obj);
			long count = db.getCollection(WITHDRAW_COLLECTION).count(conditions);
			return count;
		} catch (Exception e) {
			logger.error(e);
			return 0;
		}
	}

	/**
	 * Add transaction
	 * 
	 * @param withDrawPaygateModel
	 * @return orderId
	 */
	@Override
	public long Add(WithDrawPaygateModel withDrawPaygateModel) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			MongoCollection<Document> col = db.getCollection(WITHDRAW_COLLECTION);
			Document doc = new Document();
			long Id = VinPlayUtils.generateTransId();
			doc.append("Id", String.valueOf(Id));
			doc.append("CreatedAt", VinPlayUtils.getCurrentDateTime());
			doc.append("ModifiedAt", VinPlayUtils.getCurrentDateTime());
			doc.append("IsDeleted", false);
			doc.append("CartId", String.valueOf(Id));
			doc.append("ReferenceId", "");
			doc.append("UserId", withDrawPaygateModel.UserId);
			doc.append("Username", withDrawPaygateModel.Username);
			doc.append("Nickname", withDrawPaygateModel.Nickname);
			doc.append("RequestTime", withDrawPaygateModel.RequestTime);
			doc.append("BankCode", withDrawPaygateModel.BankCode);
			doc.append("MerchantCode", withDrawPaygateModel.MerchantCode);
			doc.append("PaymentType", withDrawPaygateModel.PaymentType);
			doc.append("ProviderName", withDrawPaygateModel.ProviderName);
			doc.append("Amount", withDrawPaygateModel.Amount);
			doc.append("AmountFee", 0l);
			doc.append("Status", withDrawPaygateModel.Status);
			doc.append("BankAccountNumber", withDrawPaygateModel.BankAccountNumber);
			doc.append("BankAccountName", withDrawPaygateModel.BankAccountName);
			doc.append("BankBranch", withDrawPaygateModel.BankBranch);
			doc.append("UserApprove", withDrawPaygateModel.UserApprove);
			doc.append("Description", withDrawPaygateModel.Description);
			col.insertOne((Document) doc);
			return Id;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Update info transaction
	 * 
	 * @param OrderId:      Order id of transaction (not Id)
	 * @param MerchantCode: Id reference
	 * @param paymentType:  Payment type
	 * @param providerName: Provider name
	 * @param userApprove:  User name change status
	 * @param bankCode
	 * @return true: success; false: failed
	 */
	@Override
	public Boolean UpdateInfo(String orderId, String merchantCode, String paymentType, String providerName,
			String bankCode, String userApprove) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			BasicDBObject updateFields = new BasicDBObject();
			if (!StringUtils.isEmpty(merchantCode)) {
				updateFields.append("MerchantCode", merchantCode);
			}
			if (!StringUtils.isEmpty(paymentType)) {
				updateFields.append("PaymentType", paymentType);
			}
			if (!StringUtils.isEmpty(providerName)) {
				updateFields.append("ProviderName", providerName);
			}
			if (!StringUtils.isEmpty(userApprove)) {
				updateFields.append("UserApprove", userApprove);
			}
			if (!StringUtils.isEmpty(bankCode)) {
				updateFields.append("BankCode", bankCode);
			}

			updateFields.append("Status", PayCommon.PAYSTATUS.PENDING.getId());
			updateFields.append("ModifiedAt", VinPlayUtils.getCurrentDateTime());

			db.getCollection(WITHDRAW_COLLECTION).updateOne((Bson) new Document("CartId", orderId),
					(Bson) new Document("$set", updateFields));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Update info transaction
	 * 
	 * @param OrderId:      Order id of transaction (not Id)
	 * @param MerchantCode: Id reference
	 * @param paymentType:  Payment type
	 * @param providerName: Provider name
	 * @param userApprove:  User name change status
	 * @return true: success; false: failed
	 */
	@Override
	public Boolean UpdateInfo(String orderId, String merchantCode, String paymentType, String providerName,
			String userApprove) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			BasicDBObject updateFields = new BasicDBObject();
			updateFields.append("MerchantCode", merchantCode);
			updateFields.append("PaymentType", paymentType);
			updateFields.append("ProviderName", providerName);
			updateFields.append("Status", PayCommon.PAYSTATUS.PENDING.getId());
			updateFields.append("ModifiedAt", VinPlayUtils.getCurrentDateTime());
			updateFields.append("UserApprove", userApprove);
			db.getCollection(WITHDRAW_COLLECTION).updateOne((Bson) new Document("CartId", orderId),
					(Bson) new Document("$set", updateFields));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Update status transaction
	 * 
	 * @param OrderId:     Order id of transaction (not Id)
	 * @param cartId:      Id reference
	 * @param status:      Status of transaction will be change
	 * @param userApprove: User name change status
	 * @param reason
	 * @return true: success; false: failed
	 */
	@Override
	public Boolean UpdateStatus(String orderId, String cartId, int status, String userApprove, String reason) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			BasicDBObject updateFields = new BasicDBObject();
			updateFields.append("ReferenceId", cartId);
			updateFields.append("Status", status);
			updateFields.append("ModifiedAt", VinPlayUtils.getCurrentDateTime());
			updateFields.append("UserApprove", userApprove);
			updateFields.append("Description", reason);
			db.getCollection(WITHDRAW_COLLECTION).updateOne((Bson) new Document("CartId", orderId),
					(Bson) new Document("$set", updateFields));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Update status transaction
	 * 
	 * @param OrderId:     Order id of transaction (not Id)
	 * @param cartId:      Id reference
	 * @param status:      Status of transaction will be change
	 * @param userApprove: User name change status
	 * @return true: success; false: failed
	 */
	@Override
	public Boolean UpdateStatus(String orderId, String cartId, int status, String userApprove) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			BasicDBObject updateFields = new BasicDBObject();
			updateFields.append("ReferenceId", cartId);
			updateFields.append("Status", status);
			updateFields.append("ModifiedAt", VinPlayUtils.getCurrentDateTime());
			updateFields.append("UserApprove", userApprove);
			db.getCollection(WITHDRAW_COLLECTION).updateOne((Bson) new Document("CartId", orderId),
					(Bson) new Document("$set", updateFields));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Update status transaction
	 * 
	 * @param OrderId:     Order id of transaction (not Id)
	 * @param status:      Status of transaction will be change
	 * @param userApprove: User name change status
	 * @return true: success; false: failed
	 */
	public Boolean UpdateStatus(String orderId, int status, String userApprove) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			BasicDBObject updateFields = new BasicDBObject();
			updateFields.append("Status", status);
			updateFields.append("ModifiedAt", VinPlayUtils.getCurrentDateTime());
			updateFields.append("UserApprove", userApprove);
			db.getCollection(WITHDRAW_COLLECTION).updateOne((Bson) new Document("CartId", orderId),
					(Bson) new Document("$set", updateFields));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Boolean UpdateStatus(String orderId, int status, String userApprove, String reason) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			BasicDBObject updateFields = new BasicDBObject();
			updateFields.append("Status", status);
			updateFields.append("ModifiedAt", VinPlayUtils.getCurrentDateTime());
			updateFields.append("UserApprove2", userApprove);
			updateFields.append("Description", reason);
			db.getCollection(WITHDRAW_COLLECTION).updateOne((Bson) new Document("CartId", orderId),
					(Bson) new Document("$set", updateFields));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Update request time of transaction
	 * 
	 * @param OrderId:       Order id of transaction (not Id)
	 * @param reRequestTime: Time request
	 * @param userApprove:   User name change status
	 * @param userApprove:   User name change status
	 * @return true: success; false: failed
	 */
	public Boolean UpdateRequestTime(String orderId, String reRequestTime, String userApprove) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			BasicDBObject updateFields = new BasicDBObject();
			updateFields.append("RequestTime", reRequestTime);
			updateFields.append("ModifiedAt", VinPlayUtils.getCurrentDateTime());
			updateFields.append("UserApprove", userApprove);
			db.getCollection(WITHDRAW_COLLECTION).updateOne((Bson) new Document("CartId", orderId),
					(Bson) new Document("$set", updateFields));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Update amount, amount fee of transaction
	 * 
	 * @param OrderId:     Order id of transaction (not Id)
	 * @param amount:      Amount of transaction
	 * @param amountFee:   Amount fee of transaction
	 * @param userApprove: User name change status
	 * @return true: success; false: failed
	 */
	public Boolean UpdateAmount(String orderId, long amount, long amountFee, String userApprove) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			BasicDBObject updateFields = new BasicDBObject();
			updateFields.append("Amount", amount);
			updateFields.append("AmountFee", amountFee);
			updateFields.append("ModifiedAt", VinPlayUtils.getCurrentDateTime());
			updateFields.append("UserApprove", userApprove);
			db.getCollection(WITHDRAW_COLLECTION).updateOne((Bson) new Document("CartId", orderId),
					(Bson) new Document("$set", updateFields));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Get detail transaction by id
	 * 
	 * @param Id: Id of transaction
	 * @return WithDrawPaygateModel
	 */
	@Override
	public WithDrawPaygateModel GetById(String Id) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			Document conditions = new Document();
			conditions.put("Id", Id);
			Document document = (Document) db.getCollection(WITHDRAW_COLLECTION).find((Bson) conditions).first();
			if (document == null)
				return null;
			WithDrawPaygateModel model = new WithDrawPaygateModel(document.getString("Id"),
					document.getString("CreatedAt"), document.getString("ModifiedAt"), document.getBoolean("IsDeleted"),
					document.getString("CartId"), document.getString("ReferenceId"), document.getString("UserId"),
					document.getString("Username"), document.getString("Nickname"), document.getString("RequestTime"),
					document.getString("BankCode"), document.getString("PaymentType"),
					document.getString("MerchantCode"), document.getString("ProviderName"), document.getLong("Amount"),
					document.getLong("AmountFee"), document.getInteger("Status"),
					document.getString("BankAccountNumber"), document.getString("BankAccountName"),
					document.getString("BankBranch"), document.getString("UserApprove"),
					document.getString("Description"));
			return model;
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	/**
	 * Get detail transaction by reference Id
	 * 
	 * @param cartId: Reference Id of transaction
	 * @return WithDrawPaygateModel
	 */
	@Override
	public WithDrawPaygateModel GetByReferenceId(String cartId) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			Document conditions = new Document();
			conditions.put("ReferenceId", cartId);
			Document document = (Document) db.getCollection(WITHDRAW_COLLECTION).find((Bson) conditions).first();
			if (document == null)
				return null;
			WithDrawPaygateModel model = new WithDrawPaygateModel(document.getString("Id"),
					document.getString("CreatedAt"), document.getString("ModifiedAt"), document.getBoolean("IsDeleted"),
					document.getString("CartId"), document.getString("ReferenceId"), document.getString("UserId"),
					document.getString("Username"), document.getString("Nickname"), document.getString("RequestTime"),
					document.getString("BankCode"), document.getString("PaymentType"),
					document.getString("MerchantCode"), document.getString("ProviderName"), document.getLong("Amount"),
					document.getLong("AmountFee"), document.getInteger("Status"),
					document.getString("BankAccountNumber"), document.getString("BankAccountName"),
					document.getString("BankBranch"), document.getString("UserApprove"),
					document.getString("Description"));
			return model;
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	/**
	 * Get detail transaction by reference Id
	 * 
	 * @param orderId: Order id of transaction (not Id)
	 * @return WithDrawPaygateModel
	 */
	@Override
	public WithDrawPaygateModel GetByOrderId(String orderId) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			Document conditions = new Document();
			conditions.put("CartId", orderId);
			Document document = (Document) db.getCollection(WITHDRAW_COLLECTION).find((Bson) conditions).first();
			if (document == null)
				return null;
			WithDrawPaygateModel model = new WithDrawPaygateModel(document.getString("Id"),
					document.getString("CreatedAt"), document.getString("ModifiedAt"), document.getBoolean("IsDeleted"),
					document.getString("CartId"), document.getString("ReferenceId"), document.getString("UserId"),
					document.getString("Username"), document.getString("Nickname"), document.getString("RequestTime"),
					document.getString("BankCode"), document.getString("PaymentType"),
					document.getString("MerchantCode"), document.getString("ProviderName"), document.getLong("Amount"),
					document.getLong("AmountFee"), document.getInteger("Status"),
					document.getString("BankAccountNumber"), document.getString("BankAccountName"),
					document.getString("BankBranch"), document.getString("UserApprove"),
					document.getString("Description"));
			return model;
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	/**
	 * Search transaction
	 * 
	 * @param withDrawPaygateModel: Contain value of fields want to search
	 * @param status
	 * @param page
	 * @param maxItem
	 * @param fromTime
	 * @param endTime
	 * @return WithDrawPaygateReponse
	 */
	@Override
	public WithDrawPaygateReponse Find(WithDrawPaygateModel withDrawPaygateModel, int page, int maxItem,
			String fromTime, String endTime) {
		try {
			final ArrayList<WithDrawPaygateModel> records = new ArrayList<WithDrawPaygateModel>();
			final ArrayList<Long> num = new ArrayList<Long>();
			num.add(0, 0L);
			num.add(1, 0L);
			num.add(2, 0L);
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			MongoCollection<?> col = db.getCollection(WITHDRAW_COLLECTION);
			page = (page - 1) < 0 ? 0 : (page - 1);
			int numStart = page * maxItem;
			int numEnd = maxItem;
			BasicDBObject objsort = new BasicDBObject();
			objsort.put("_id", -1);
			HashMap<String, Object> conditions = new HashMap<String, Object>();
			if (!withDrawPaygateModel.Nickname.isEmpty()) {
				String pattern = ".*" + withDrawPaygateModel.Nickname + ".*";
				conditions.put("Nickname", new BasicDBObject().append("$regex", pattern).append("$options", "i"));
			}

			if (!withDrawPaygateModel.Id.isEmpty()) {
				conditions.put("Id", withDrawPaygateModel.Id);
			}

			if (!withDrawPaygateModel.ProviderName.isEmpty()) {
				conditions.put("ProviderName", withDrawPaygateModel.ProviderName);
			}

			if (withDrawPaygateModel.Status > 0) {
				conditions.put("Status", withDrawPaygateModel.Status);
			}

			if (!fromTime.isEmpty() && !endTime.isEmpty()) {
				BasicDBObject obj = new BasicDBObject();
				obj.put("$gte", fromTime);
				obj.put("$lte", endTime);
				conditions.put("CreatedAt", obj);
			}

			FindIterable<?> iterable = col.find((Bson) new Document(conditions)).sort((Bson) objsort).skip(numStart)
					.limit(maxItem);
			iterable.forEach((Block) new Block<Document>() {
				public void apply(Document document) {
					WithDrawPaygateModel model = new WithDrawPaygateModel(document.getString("Id"),
							document.getString("CreatedAt"), document.getString("ModifiedAt"),
							document.getBoolean("IsDeleted"), document.getString("CartId"),
							document.getString("ReferenceId"), document.getString("UserId"),
							document.getString("Username"), document.getString("Nickname"),
							document.getString("RequestTime"), document.getString("BankCode"),
							document.getString("PaymentType"), document.getString("MerchantCode"),
							document.getString("ProviderName"), document.getLong("Amount"),
							document.getLong("AmountFee"), document.getInteger("Status"),
							document.getString("BankAccountNumber"), document.getString("BankAccountName"),
							document.getString("BankBranch"), document.getString("UserApprove"),
							document.getString("Description"));
					records.add(model);
				}
			});
			FindIterable<?> iterable2 = col.find((Bson) new Document(conditions));
			iterable2.forEach((Block) new Block<Document>() {

				public void apply(Document document) {
					long amount = document.getLong("Amount");
					int code = document.getInteger("Status");
					long count = (Long) num.get(0) + 1L;
					num.set(0, count);
					if (code == DvtConst.STATUS_APPROVE) {
						long numSuccess = (Long) num.get(1) + 1L;
						num.set(1, numSuccess);
						long moneySuccess = (Long) num.get(2) + (long) amount;
						num.set(2, moneySuccess);
					}
				}
			});

			WithDrawPaygateReponse res = new WithDrawPaygateReponse(num.get(0), num.get(2), num.get(1), records);
			return res;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Search transaction
	 * 
	 * @param nickname
	 * @param status
	 * @param page
	 * @param maxItem
	 * @param fromTime
	 * @param endTime
	 * @param providerName (If want get all, set value is empty)
	 * @return WithDrawPaygateReponse
	 */
	@Override
	public WithDrawPaygateReponse Find(String nickname, int status, int page, int maxItem, String fromTime,
			String endTime, String providerName) {
		try {
			final ArrayList<WithDrawPaygateModel> records = new ArrayList<WithDrawPaygateModel>();
			final ArrayList<Long> num = new ArrayList<Long>();
			num.add(0, 0L);
			num.add(1, 0L);
			num.add(2, 0L);
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			MongoCollection<?> col = db.getCollection(WITHDRAW_COLLECTION);
			page = (page - 1) < 0 ? 0 : (page - 1);
			int numStart = page * maxItem;
			int numEnd = maxItem;
			BasicDBObject objsort = new BasicDBObject();
			objsort.put("_id", -1);
			HashMap<String, Object> conditions = new HashMap<String, Object>();
			if (!nickname.isEmpty()) {
				String pattern = ".*" + nickname + ".*";
				conditions.put("Nickname", new BasicDBObject().append("$regex", pattern).append("$options", "i"));
			}

			if (!providerName.isEmpty()) {
				conditions.put("ProviderName", providerName);
			}

			if (status > 0) {
				conditions.put("Status", status);
			}

			if (!fromTime.isEmpty() && !endTime.isEmpty()) {
				BasicDBObject obj = new BasicDBObject();
				obj.put("$gte", fromTime);
				obj.put("$lte", endTime);
				conditions.put("CreatedAt", obj);
			}

			FindIterable<?> iterable = col.find((Bson) new Document(conditions)).sort((Bson) objsort).skip(numStart)
					.limit(maxItem);
			iterable.forEach((Block) new Block<Document>() {
				public void apply(Document document) {
					WithDrawPaygateModel model = new WithDrawPaygateModel(document.getString("Id"),
							document.getString("CreatedAt"), document.getString("ModifiedAt"),
							document.getBoolean("IsDeleted"), document.getString("CartId"),
							document.getString("ReferenceId"), document.getString("UserId"),
							document.getString("Username"), document.getString("Nickname"),
							document.getString("RequestTime"), document.getString("BankCode"),
							document.getString("PaymentType"), document.getString("MerchantCode"),
							document.getString("ProviderName"), document.getLong("Amount"),
							document.getLong("AmountFee"), document.getInteger("Status"),
							document.getString("BankAccountNumber"), document.getString("BankAccountName"),
							document.getString("BankBranch"), document.getString("UserApprove"),
							document.getString("Description"));
					records.add(model);
				}
			});
			FindIterable<?> iterable2 = col.find((Bson) new Document(conditions));
			iterable2.forEach((Block) new Block<Document>() {
				public void apply(Document document) {
					long amount = document.getLong("Amount");
					int code = document.getInteger("Status");
					long count = (Long) num.get(0) + 1L;
					num.set(0, count);
					if (code == DvtConst.STATUS_APPROVE) {
						long numSuccess = (Long) num.get(1) + 1L;
						num.set(1, numSuccess);
						long moneySuccess = (Long) num.get(2) + (long) amount;
						num.set(2, moneySuccess);
					}
				}
			});

			WithDrawPaygateReponse res = new WithDrawPaygateReponse(num.get(0), num.get(2), num.get(1), records);
			return res;

		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	/**
	 * Search transaction
	 * 
	 * @param nickname
	 * @param status
	 * @param page
	 * @param maxItem
	 * @param fromTime
	 * @param endTime
	 * @param providerName      (If want get all, set value is empty)
	 * @param orderId
	 * @param transactionId
	 * @param bankCode
	 * @param bankAccountNumber
	 * @param bankAccountName
	 * @return WithDrawPaygateReponse
	 */
	@Override
	public WithDrawPaygateReponse Find(String nickname, Integer status, int page, int maxItem, String fromTime,
			String endTime, String providerName, String orderId, String transactionId, String bankCode,
			String bankAccountNumber, String bankAccountName, Double fromAmount, Double toAmount) {
		try {
			final ArrayList<WithDrawPaygateModel> records = new ArrayList<WithDrawPaygateModel>();
			final ArrayList<Long> num = new ArrayList<Long>();
			num.add(0, 0L);
			num.add(1, 0L);
			num.add(2, 0L);
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			MongoCollection<?> col = db.getCollection(WITHDRAW_COLLECTION);
			page = (page - 1) < 0 ? 0 : (page - 1);
			int numStart = page * maxItem;
			int numEnd = maxItem;
			BasicDBObject objsort = new BasicDBObject();
			objsort.put("_id", -1);
			HashMap<String, Object> conditions = new HashMap<String, Object>();
			if (nickname != null && !nickname.isEmpty()) {
				String pattern = ".*" + nickname + ".*";
				conditions.put("Nickname", new BasicDBObject().append("$regex", pattern).append("$options", "i"));
			}

			if (providerName != null && !providerName.isEmpty()) {
				conditions.put("ProviderName", providerName);
			}

			if (orderId != null && !orderId.isEmpty()) {
				conditions.put("CartId", orderId);
			}

			if (transactionId != null && !transactionId.isEmpty()) {
				conditions.put("ReferenceId", transactionId);
			}

			if (bankCode != null && !bankCode.isEmpty()) {
				conditions.put("BankCode", bankCode);
			}

			if (bankAccountNumber != null && !bankAccountNumber.isEmpty()) {
				conditions.put("BankAccountNumber", bankAccountNumber);
			}

			if (bankAccountName != null && !bankAccountName.isEmpty()) {
				conditions.put("BankAccountName", bankAccountName);
			}

			if (status != null && status >= 0) {
				conditions.put("Status", status);
			}
			if (fromAmount != null) {
				BasicDBObject obj = new BasicDBObject();
				obj.put("$gte", fromAmount.longValue());
				conditions.put("Amount", obj);
			}

			if (toAmount != null) {
				BasicDBObject obj = new BasicDBObject();
				obj.put("$lte", toAmount.longValue());
				conditions.put("Amount", obj);
			}

			if (fromTime != null && !fromTime.isEmpty() && endTime != null && !endTime.isEmpty()) {
				BasicDBObject obj = new BasicDBObject();
				obj.put("$gte", fromTime);
				obj.put("$lte", endTime);
				conditions.put("CreatedAt", obj);
			}

			FindIterable<?> iterable = col.find((Bson) new Document(conditions)).sort((Bson) objsort).skip(numStart)
					.limit(maxItem);
			iterable.forEach((Block) new Block<Document>() {
				public void apply(Document document) {
					WithDrawPaygateModel model = new WithDrawPaygateModel(document.getString("Id"),
							document.getString("CreatedAt"), document.getString("ModifiedAt"),
							document.getBoolean("IsDeleted"), document.getString("CartId"),
							document.getString("ReferenceId"), document.getString("UserId"),
							document.getString("Username"), document.getString("Nickname"),
							document.getString("RequestTime"), document.getString("BankCode"),
							document.getString("PaymentType"), document.getString("MerchantCode"),
							document.getString("ProviderName"), document.getLong("Amount"),
							document.getLong("AmountFee"), document.getInteger("Status"),
							document.getString("BankAccountNumber"), document.getString("BankAccountName"),
							document.getString("BankBranch"), document.getString("UserApprove"),
							document.getString("Description"));
					records.add(model);
				}
			});
			FindIterable<?> iterable2 = col.find((Bson) new Document(conditions));
			iterable2.forEach((Block) new Block<Document>() {
				public void apply(Document document) {
					long amount = document.getLong("Amount");
					int code = document.getInteger("Status");
					long count = (Long) num.get(0) + 1L;
					num.set(0, count);
					if (code == DvtConst.STATUS_APPROVE) {
						long numSuccess = (Long) num.get(1) + 1L;
						num.set(1, numSuccess);
						long moneySuccess = (Long) num.get(2) + (long) amount;
						num.set(2, moneySuccess);
					}
				}
			});

			WithDrawPaygateReponse res = new WithDrawPaygateReponse(num.get(0), num.get(2), num.get(1), records);
			return res;

		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	@Override
	public ArrayList<Object> find(HashMap<String, Object> conditions, int page, int maxItem) throws Exception {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			page = Math.max((page - 1), 0);

			BasicDBObject objsort = new BasicDBObject();
			objsort.put("_id", -1);

			return db.getCollection(WITHDRAW_COLLECTION).find((Bson) new Document(conditions))
					.projection(Projections.exclude("_id")).sort((Bson) objsort).skip(page * maxItem).limit(maxItem)
					.map(x -> JSON.parse(x.toJson())).into(new ArrayList<>());

		} catch (Exception e) {
			throw e;
		}
	}

	public long count(HashMap<String, Object> conditions) {
		MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
		return db.getCollection(WITHDRAW_COLLECTION).count((Bson) new Document(conditions));
	}

	public Long[] statistic(HashMap<String, Object> conditions) {
		Long[] num = { 0L, // tong thanh cong
				0L, // tong so tien thanh cong
				0L // tong so tien
		};

		MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
		db.getCollection(WITHDRAW_COLLECTION).find((Bson) new Document(conditions)).forEach(new Block<Document>() {
			public void apply(Document document) {
				long amount = document.getLong("Amount");
				int code = document.getInteger("Status");
				if (code == DvtConst.STATUS_COMPLETE) {
					num[0]++;
					num[1] += amount;
				}
				num[2] += amount;
			}
		});

		return num;
	}

	/**
	 * Get all records have status is RECEIVED
	 * 
	 * @param minutes
	 * @return
	 */
	@Override
	public ArrayList<WithDrawPaygateModel> GetRecevied(Integer minutes) {
		try {
			final ArrayList<WithDrawPaygateModel> records = new ArrayList<WithDrawPaygateModel>();
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			MongoCollection<?> col = db.getCollection(WITHDRAW_COLLECTION);
			BasicDBObject objsort = new BasicDBObject();
			objsort.put("_id", -1);
			Document conditions = new Document();
			BasicDBObject obj = new BasicDBObject();
			Calendar currentDate = Calendar.getInstance();
			long t = currentDate.getTimeInMillis();
			Date afterAddingTenMins = new Date(t - (10 * minutes));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = format.format(afterAddingTenMins);
			obj.put("$lte", (Object) date);
			conditions.put("ModifiedAt", obj);
			conditions.put("Status", PAYSTATUS.RECEIVED.getId());
			conditions.put("ProviderName", "clickpay");

			FindIterable<?> iterable = col.find(new Document(conditions)).sort(objsort);
			iterable.forEach((Block) new Block<Document>() {
				public void apply(Document document) {
					WithDrawPaygateModel model = new WithDrawPaygateModel(document.getString("Id"),
							document.getString("CreatedAt"), document.getString("ModifiedAt"),
							document.getBoolean("IsDeleted"), document.getString("CartId"),
							document.getString("ReferenceId"), document.getString("UserId"),
							document.getString("Username"), document.getString("Nickname"),
							document.getString("RequestTime"), document.getString("BankCode"),
							document.getString("PaymentType"), document.getString("MerchantCode"),
							document.getString("ProviderName"), document.getLong("Amount"),
							document.getLong("AmountFee"), document.getInteger("Status"),
							document.getString("BankAccountNumber"), document.getString("BankAccountName"),
							document.getString("BankBranch"), document.getString("UserApprove"),
							document.getString("Description"));
					records.add(model);
				}
			});

			return records;

		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	/**
	 * Search transaction only display for user play
	 * 
	 * @param nickname
	 * @param status
	 * @param page
	 * @param maxItem
	 * @param fromTime
	 * @param endTime
	 * @param providerName (If want get all, set value is empty)
	 * @return DepositPaygateModel
	 */
	@Override
	public Map<String, Object> FindTransaction(String nickname, int status, int page, int maxItem, String fromTime,
			String endTime, String providerName) {
		Map<String, Object> data = new HashMap<>();
		try {
			BasicDBObject conditions = new BasicDBObject();
			if (!StringUtils.isBlank(fromTime) && !StringUtils.isBlank(endTime)) {
				BasicDBObject obj = new BasicDBObject();
				obj.put("$gte", fromTime);
				obj.put("$lte", endTime);
				conditions.put("CreatedAt", obj);
			}

			if (!StringUtils.isBlank(nickname)) {
				String pattern = ".*" + nickname + ".*";
				conditions.put("Nickname", new BasicDBObject().append("$regex", pattern).append("$options", "i"));
			}

			if (!StringUtils.isBlank(providerName)) {
				conditions.put("ProviderName", providerName);
			}

			if (status > -1) {
				conditions.put("Status", status);
			} else {
				BasicDBObject st = new BasicDBObject();
				List<Integer> statusDefault = Arrays.asList(0, 1, 2, 3, 4, 12);
				st.put("$in", statusDefault);
				conditions.append("Status", st);
			}

			Document match = new Document("$match", conditions);
			Document project = new Document();
			project = new Document("$project",
					new Document("_id", 0).append("CreatedAt", 1).append("Amount", 1).append("AmountFee", 1).append("OrderId", 1)
							.append("BankCode", 1).append("BankAccountNumber", 1).append("BankAccountName", 1)
							.append("ProviderName", 1).append("Status", 1));
			Document sort = new Document();
			sort = new Document("$sort", new Document("CreatedAt", -1));
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			MongoCollection<?> collection = db.getCollection(WITHDRAW_COLLECTION);
			page = (page - 1) < 0 ? 0 : (page - 1);
			int numStart = page * maxItem;
			int numEnd = maxItem;
			Document skip = new Document();
			skip = new Document("$skip", numStart);
			Document limit = new Document();
			limit = new Document("$limit", numEnd);
			List<Object> result = new ArrayList<>();
//			result = collection.aggregate(Arrays.asList(match, sort, skip, limit, project)).allowDiskUse(true)
//					.into(new ArrayList<>());
			AggregateIterable<Document> aggregate = (AggregateIterable<Document>) collection
					.aggregate(Arrays.asList(match, sort, skip, limit, project)).allowDiskUse(true);
			for (Document document : aggregate) {
				try {
					String bankCode = document.get("BankCode").toString();
					Bank bank = GameCommon.LIST_BANK_NAME.stream().filter(x -> bankCode.equals(x.getCode())).findAny()
							.orElse(null);
					if (bank != null) {
						document.remove("BankCode");
						document.append("BankCode", bank == null ? bankCode : bank.getBank_name());
					}

					result.add(document);
				} catch (Exception e) {
				}
			}

			data.put("data", result);
			// Get total record found
			Document count = new Document();
			int totalRecord = 0;
			count = new Document("$count", "OrderId");
			AggregateIterable<Document> aggregateCount = (AggregateIterable<Document>) collection
					.aggregate(Arrays.asList(match, count)).allowDiskUse(true);
			for (Document document : aggregateCount) {
				try {
					totalRecord = document.getInteger("OrderId", 0);
				} catch (Exception e) {
					totalRecord = 0;
				}
			}

			data.put("totalRecord", totalRecord);
		} catch (Exception e) {
			logger.error("Search FindTransaction error: " + e.getMessage());
			data = new HashMap<>();
			data.put("data", new ArrayList<>());
			data.put("totalRecord", 0);
		}

		return data;
	}
	public Map<String, Object> FindTransaction(String nickname, String agentCode, int status, int page, int maxItem, String fromTime,
			String endTime, String providerName) {
		Map<String, Object> data = new HashMap<>();
		try {
			BasicDBObject conditions = new BasicDBObject();
			if (!StringUtils.isBlank(fromTime) && !StringUtils.isBlank(endTime)) {
				BasicDBObject obj = new BasicDBObject();
				obj.put("$gte", fromTime + " 00:00:00");
				obj.put("$lte", endTime + " 23:59:59");
				conditions.put("CreatedAt", obj);
			}

			if (!StringUtils.isBlank(nickname)) {
				String pattern = ".*" + nickname + ".*";
				conditions.put("Nickname", new BasicDBObject().append("$regex", pattern).append("$options", "i"));
			}

			if (!StringUtils.isBlank(providerName)) {
				conditions.put("ProviderName", providerName);
			}

			if (!StringUtils.isBlank(agentCode)) {
				conditions.put("MerchantCode", agentCode);
			}

			if (status > -1) {
				conditions.put("Status", status);
			} else {
				BasicDBObject st = new BasicDBObject();
				List<Integer> statusDefault = Arrays.asList(0, 1, 2, 3, 4);
				st.put("$in", statusDefault);
				conditions.append("Status", st);
			}

			Document match = new Document("$match", conditions);
			Document project = new Document();
			project = new Document("$project",
					new Document("_id", 0).append("CreatedAt", 1).append("Amount", 1).append("Id", 1)
							.append("BankCode", 1).append("BankAccountNumber", 1).append("BankAccountName", 1)
							.append("ProviderName", 1).append("Status", 1).append("MerchantCode", 1)
							.append("Nickname", 1).append("Username", 1));
			Document sort = new Document();
			sort = new Document("$sort", new Document("CreatedAt", -1));
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			MongoCollection<?> collection = db.getCollection(WITHDRAW_COLLECTION);
			page = (page - 1) < 0 ? 0 : (page - 1);
			int numStart = page * maxItem;
			int numEnd = maxItem;
			Document skip = new Document();
			skip = new Document("$skip", numStart);
			Document limit = new Document();
			limit = new Document("$limit", numEnd);
			List<Object> result = new ArrayList<>();
//			result = collection.aggregate(Arrays.asList(match, sort, skip, limit, project)).allowDiskUse(true)
//					.into(new ArrayList<>());
			AggregateIterable<Document> aggregate = (AggregateIterable<Document>) collection
					.aggregate(Arrays.asList(match, sort, skip, limit, project)).allowDiskUse(true);
			for (Document document : aggregate) {
				try {
					String bankCode = document.get("BankCode").toString();
					Bank bank = GameCommon.LIST_BANK_NAME.stream().filter(x -> bankCode.equals(x.getCode())).findAny()
							.orElse(null);
					if (bank != null) {
						document.remove("BankCode");
						document.append("BankCode", bank == null ? bankCode : bank.getBank_name());
					}

					result.add(document);
				} catch (Exception e) {
				}
			}

			data.put("data", result);
			// Get total record found
			Document count = new Document();
			int totalRecord = 0;
			count = new Document("$count", "Id");
			AggregateIterable<Document> aggregateCount = (AggregateIterable<Document>) collection
					.aggregate(Arrays.asList(match, count)).allowDiskUse(true);
			for (Document document : aggregateCount) {
				try {
					totalRecord = document.getInteger("Id", 0);
				} catch (Exception e) {
					totalRecord = 0;
				}
			}

			data.put("totalRecord", totalRecord);
		} catch (Exception e) {
			logger.error("Search FindTransaction error: " + e.getMessage());
			data = new HashMap<>();
			data.put("data", new ArrayList<>());
			data.put("totalRecord", 0);
		}

		return data;
	}

	public Boolean delete(String id) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			db.getCollection(WITHDRAW_COLLECTION).deleteOne((Bson) new Document("Id", id));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Boolean Update(WithDrawPaygateModel withDrawPaygateModel) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			BasicDBObject updateFields = new BasicDBObject();
			updateFields.append("CreatedAt", withDrawPaygateModel.CreatedAt);
			updateFields.append("ModifiedAt", VinPlayUtils.getCurrentDateTime());
			updateFields.append("IsDeleted", withDrawPaygateModel.IsDeleted);
			updateFields.append("CartId", withDrawPaygateModel.CartId);
			updateFields.append("ReferenceId", withDrawPaygateModel.ReferenceId);
			updateFields.append("UserId", withDrawPaygateModel.UserId);
			updateFields.append("Username", withDrawPaygateModel.Username);
			updateFields.append("Nickname", withDrawPaygateModel.Nickname);
			updateFields.append("RequestTime", withDrawPaygateModel.RequestTime);
			updateFields.append("BankCode", withDrawPaygateModel.BankCode);
			updateFields.append("MerchantCode", withDrawPaygateModel.MerchantCode);
			updateFields.append("PaymentType", withDrawPaygateModel.PaymentType);
			updateFields.append("ProviderName", withDrawPaygateModel.ProviderName);
			updateFields.append("Amount", withDrawPaygateModel.Amount);
			updateFields.append("AmountFee", 0l);
			updateFields.append("Status", withDrawPaygateModel.Status);
			updateFields.append("BankAccountNumber", withDrawPaygateModel.BankAccountNumber);
			updateFields.append("BankAccountName", withDrawPaygateModel.BankAccountName);
			updateFields.append("BankBranch", withDrawPaygateModel.BankBranch);
			updateFields.append("UserApprove", withDrawPaygateModel.UserApprove);
			updateFields.append("Description", withDrawPaygateModel.Description);
			updateFields.append("AgentBankAccountName", withDrawPaygateModel.AgentBankAccountName);
			updateFields.append("AgentBankAccountNumber", withDrawPaygateModel.AgentBankAccountNumber);
			updateFields.append("AgentBankCode", withDrawPaygateModel.AgentBankCode);
			updateFields.append("Content", withDrawPaygateModel.Content);
			db.getCollection(WITHDRAW_COLLECTION).updateOne((Bson) new Document("Id", withDrawPaygateModel.Id),
					(Bson) new Document("$set", updateFields));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
