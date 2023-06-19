package com.vinplay.payment.dao.impl;

import java.util.*;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.model.Projections;
import com.mongodb.util.JSON;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.vinplay.dichvuthe.utils.DvtConst;
import com.vinplay.payment.dao.RechargePaygateDao;
import com.vinplay.payment.entities.Bank;
import com.vinplay.payment.entities.DepositPaygateModel;
import com.vinplay.payment.entities.DepositPaygateReponse;
import com.vinplay.payment.utils.PayCommon.PAYSTATUS;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.common.utils.DateTimeUtils;
import com.vinplay.vbee.common.utils.VinPlayUtils;
import org.bson.conversions.Bson;

public class RechargePaygateDaoImpl implements RechargePaygateDao {

	public static final Logger logger = Logger.getLogger(RechargePaygateDaoImpl.class);
	private static final String DEPOSIT_COLLECTION = "deposit_paygate";

	/**
	 * Check exist transaction is pending by nickname and provider name
	 * @param nickname
	 * @param providerName
	 * @return true: exist; false: not exist
	 */
	@Override
	public boolean CheckPending(String nickname, String providerName) {
		return false;
//		try {
//			MongoDatabase db = MongoDBConnectionFactory.getDB();
//			Document conditions = new Document();
//			conditions.put("Nickname", nickname);
//			//conditions.put("ProviderName", providerName);
//			conditions.put("Status", PAYSTATUS.PENDING.getId());
//			long count = db.getCollection(DEPOSIT_COLLECTION).count(conditions);
////			Document dc = db.getCollection(DEPOSIT_COLLECTION).find(conditions).first();
//			return count > 5;
//		} catch (Exception e) {
//			logger.error(e);
//			return false;
//		}
	}
	
	@Override
	public boolean isExistDeposit(String nickname) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			Document conditions = new Document();
			conditions.put("Nickname", nickname);
			conditions.put("Status", PAYSTATUS.COMPLETED.getId());
			long count = db.getCollection(DEPOSIT_COLLECTION).count(conditions);
			return count > 0;
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
	}


	/**
	 * Update status all record not callback from paygate clickpay
	 * @param minutes
	 * @return
	 */
	@Override
	public boolean updatePendingStatusToFailedAfterMinus(int minutes , String provider) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			Document conditions = new Document();
			BasicDBObject obj = new BasicDBObject();
			String currenTime = DateTimeUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss");
			String dateTimeAfterMinus = DateTimeUtils.getTimeSpace(currenTime, "yyyy-MM-dd HH:mm:ss", minutes * (-1), "mm");
			obj.put("$lte", dateTimeAfterMinus);
			conditions.put("ModifiedAt", obj);
			conditions.put("Status", PAYSTATUS.PENDING.getId());
			if(!"all".equals(provider)) {
				conditions.put("ProviderName", provider);
			}
			BasicDBObject updateFields = new BasicDBObject();
			updateFields.append("Status", PAYSTATUS.FAILED.getId());
			updateFields.append("Description", "Yêu cầu nạp tiền bị quá hạn (>60 phút)");
			updateFields.append("UserApprove", "AUTOMATION");
			updateFields.append("ModifiedAt", VinPlayUtils.getCurrentDateTime());
			db.getCollection(DEPOSIT_COLLECTION).updateMany(conditions,
					 new Document("$set", updateFields));
			return true;
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
	}

	/**
	 * Add transaction
	 * @param depositPayWellModel
	 * @return orderId
	 */
	@Override
	public long Add(DepositPaygateModel depositPayWellModel) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			MongoCollection<Document> col = db.getCollection(DEPOSIT_COLLECTION);
			Document doc = new Document();
			long Id = VinPlayUtils.generateTransId();
			doc.append("Id", String.valueOf(Id));
			doc.append("CreatedAt", VinPlayUtils.getCurrentDateTime());
			doc.append("ModifiedAt", VinPlayUtils.getCurrentDateTime());
			doc.append("IsDeleted", false);
			doc.append("CartId", String.valueOf(Id));
			doc.append("ReferenceId", "");
			doc.append("UserId", depositPayWellModel.UserId);
			doc.append("Username", depositPayWellModel.Username);
			doc.append("Nickname", depositPayWellModel.Nickname);
			doc.append("RequestTime", depositPayWellModel.RequestTime);
			doc.append("BankCode", depositPayWellModel.BankCode);
			doc.append("MerchantCode", depositPayWellModel.MerchantCode);
			doc.append("PaymentType", depositPayWellModel.getPayTypeStr());
			doc.append("ProviderName", depositPayWellModel.ProviderName);
			doc.append("Amount", depositPayWellModel.Amount);
			doc.append("AmountFee", depositPayWellModel.AmountFee);
			doc.append("Status", PAYSTATUS.PENDING.getId());
			doc.append("BankAccountNumber", depositPayWellModel.BankAccountNumber);
			doc.append("BankAccountName", depositPayWellModel.BankAccountName);
			doc.append("Description", depositPayWellModel.Description);
			doc.append("UserApprove", depositPayWellModel.UserApprove);
			col.insertOne((Document) doc);
			return Id;
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public long topupByCash(DepositPaygateModel depositPayWellModel) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			MongoCollection<Document> col = db.getCollection(DEPOSIT_COLLECTION);
			Document doc = new Document();
			long Id = VinPlayUtils.generateTransId();
			doc.append("Id", String.valueOf(Id));
			doc.append("CreatedAt", VinPlayUtils.getCurrentDateTime());
			doc.append("ModifiedAt", VinPlayUtils.getCurrentDateTime());
			doc.append("IsDeleted", false);
			doc.append("CartId", depositPayWellModel.CartId);
			doc.append("ReferenceId", depositPayWellModel.ReferenceId);
			doc.append("UserId", depositPayWellModel.UserId);
			doc.append("Username", depositPayWellModel.Username);
			doc.append("Nickname", depositPayWellModel.Nickname);
			doc.append("RequestTime", depositPayWellModel.RequestTime);
			doc.append("BankCode", depositPayWellModel.BankCode);
			doc.append("MerchantCode", depositPayWellModel.MerchantCode);
			doc.append("PaymentType", depositPayWellModel.getPayTypeStr());
			doc.append("ProviderName", depositPayWellModel.ProviderName);
			doc.append("Amount", depositPayWellModel.Amount);
			doc.append("AmountFee", 0l);
			doc.append("Status", PAYSTATUS.COMPLETED.getId());
			doc.append("BankAccountNumber", depositPayWellModel.BankAccountNumber);
			doc.append("BankAccountName", depositPayWellModel.BankAccountName);
			doc.append("Description", depositPayWellModel.Description);
			doc.append("UserApprove", depositPayWellModel.UserApprove);
			doc.append("AgentBankCode", depositPayWellModel.AgentBankCode);
			doc.append("AgentBankAccountName", depositPayWellModel.AgentBankAccountName);
			doc.append("AgentBankAccountNumber", depositPayWellModel.AgentBankAccountNumber);
			doc.append("Content", depositPayWellModel.Content);
			col.insertOne((Document) doc);
			return Id;
		} catch (Exception e) {
			return 0;
		}
	}
	
	public Boolean Update(DepositPaygateModel depositPayWellModel) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			Document doc = new Document();
			doc.append("Id", depositPayWellModel.Id);
			doc.append("CreatedAt", depositPayWellModel.CreatedAt);
			doc.append("ModifiedAt", VinPlayUtils.getCurrentDateTime());
			doc.append("IsDeleted", depositPayWellModel.IsDeleted);
			doc.append("CartId", depositPayWellModel.CartId);
			doc.append("ReferenceId", depositPayWellModel.ReferenceId);
			doc.append("UserId", depositPayWellModel.UserId);
			doc.append("Username", depositPayWellModel.Username);
			doc.append("Nickname", depositPayWellModel.Nickname);
			doc.append("RequestTime", depositPayWellModel.RequestTime);
			doc.append("BankCode", depositPayWellModel.BankCode);
			doc.append("MerchantCode", depositPayWellModel.MerchantCode);
			doc.append("PaymentType", depositPayWellModel.getPayTypeStr());
			doc.append("ProviderName", depositPayWellModel.ProviderName);
			doc.append("Amount", depositPayWellModel.Amount);
			doc.append("AmountFee", depositPayWellModel.AmountFee);
			doc.append("Status", depositPayWellModel.Status);
			doc.append("BankAccountNumber", depositPayWellModel.BankAccountNumber);
			doc.append("BankAccountName", depositPayWellModel.BankAccountName);
			doc.append("Description", depositPayWellModel.Description);
			doc.append("UserApprove", depositPayWellModel.UserApprove);
			db.getCollection(DEPOSIT_COLLECTION).updateOne(new Document("Id", depositPayWellModel.Id),
					 new Document("$set", doc));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public Boolean Delete(String id) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			db.getCollection(DEPOSIT_COLLECTION).deleteOne(new Document("Id", id));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Update status transaction
	 * @param OrderId: Order id of transaction (not Id)
	 * @param cartId: Id reference
	 * @param status: Status of transaction will be change
	 * @param userApprove: User name change status
	 * @return true: success; false: failed
	 */
	@Override
	public Boolean UpdateStatus(String orderId, String referenceId, int status, String userApprove) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			BasicDBObject updateFields = new BasicDBObject();
			updateFields.append("ReferenceId", referenceId);
			updateFields.append("Status", status);
			updateFields.append("ModifiedAt", VinPlayUtils.getCurrentDateTime());
			updateFields.append("UserApprove", userApprove);
			db.getCollection(DEPOSIT_COLLECTION).updateOne(new Document("CartId", orderId),
					 new Document("$set", updateFields));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public Boolean UpdateStatus(String orderId, String referenceId, int status, String userApprove,String reason) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			BasicDBObject updateFields = new BasicDBObject();
			updateFields.append("ReferenceId", referenceId);
			updateFields.append("Status", status);
			updateFields.append("ModifiedAt", VinPlayUtils.getCurrentDateTime());
			updateFields.append("UserApprove", userApprove);
			updateFields.append("Description", reason);
			db.getCollection(DEPOSIT_COLLECTION).updateOne(new Document("CartId", orderId),
					 new Document("$set", updateFields));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

    @Override
    public Boolean delete(String id) {
        try {
            MongoDatabase db = MongoDBConnectionFactory.getDB();
            db.getCollection(DEPOSIT_COLLECTION).deleteOne((Bson) new Document("Id", id));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Update status transaction
     *
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
            db.getCollection(DEPOSIT_COLLECTION).updateOne(new Document("CartId", orderId),
                    new Document("$set", updateFields));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

	/**
	 * Update request time of transaction
	 * @param OrderId: Order id of transaction (not Id)
	 * @param reRequestTime: Time request
	 * @param userApprove: User name change status
	 * @param userApprove: User name change status
	 * @return true: success; false: failed
	 */
	public Boolean UpdateRequestTime(String orderId, String reRequestTime, String userApprove) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			BasicDBObject updateFields = new BasicDBObject();
			updateFields.append("RequestTime", reRequestTime);
			updateFields.append("ModifiedAt", VinPlayUtils.getCurrentDateTime());
			updateFields.append("UserApprove", userApprove);
			db.getCollection(DEPOSIT_COLLECTION).updateOne(new Document("CartId", orderId),
					 new Document("$set", updateFields));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Update amount, amount fee of transaction
	 * @param OrderId: Order id of transaction (not Id)
	 * @param amount: Amount of transaction
	 * @param amountFee: Amount fee of transaction
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
			db.getCollection(DEPOSIT_COLLECTION).updateOne(new Document("CartId", orderId),
					 new Document("$set", updateFields));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Get detail transaction by id
	 * @param Id: Id of transaction
	 * @return DepositPaygateModel
	 */
	@Override
	public DepositPaygateModel GetById(String Id) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			Document conditions = new Document();
			conditions.put("Id", Id);
			Document document = (Document) db.getCollection(DEPOSIT_COLLECTION).find(conditions).first();
			if (document == null)
				return null;
			DepositPaygateModel model = new DepositPaygateModel(document.getString("Id"),
					document.getString("CreatedAt"), document.getString("ModifiedAt"),
					document.getBoolean("IsDeleted"), document.getString("CartId"),
					document.getString("ReferenceId"), document.getString("UserId"),
					document.getString("Username"), document.getString("Nickname"),
					document.getString("RequestTime"), document.getString("BankCode"),
					document.getString("PaymentType"), document.getString("MerchantCode"),
					document.getString("ProviderName"), document.getLong("Amount"),
					document.getLong("AmountFee"), document.getInteger("Status"),
					document.getString("BankAccountNumber"), document.getString("BankAccountName"),
					document.getString("Description"), document.getString("UserApprove"));
			return model;
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	/**
	 * Get detail transaction by reference Id
	 * @param cartId: Reference Id of transaction
	 * @return DepositPaygateModel
	 */
	@Override
	public DepositPaygateModel GetByReferenceId(String cartId) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			Document conditions = new Document();
			conditions.put("ReferenceId", cartId);
			Document document = (Document) db.getCollection(DEPOSIT_COLLECTION).find(conditions).first();
			if (document == null)
				return null;
			DepositPaygateModel model = new DepositPaygateModel(document.getString("Id"),
					document.getString("CreatedAt"), document.getString("ModifiedAt"),
					document.getBoolean("IsDeleted"), document.getString("CartId"),
					document.getString("ReferenceId"), document.getString("UserId"),
					document.getString("Username"), document.getString("Nickname"),
					document.getString("RequestTime"), document.getString("BankCode"),
					document.getString("PaymentType"), document.getString("MerchantCode"),
					document.getString("ProviderName"), document.getLong("Amount"),
					document.getLong("AmountFee"), document.getInteger("Status"),
					document.getString("BankAccountNumber"), document.getString("BankAccountName"),
					document.getString("Description"), document.getString("UserApprove"));
			return model;
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	/**
	 * Get detail transaction by reference Id
	 * @param orderId: Order id of transaction (not Id)
	 * @return DepositPaygateModel
	 */
	@Override
	public DepositPaygateModel GetByOrderId(String orderId) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			Document conditions = new Document();
			conditions.put("CartId", orderId);
			Document document = (Document) db.getCollection(DEPOSIT_COLLECTION).find(conditions).first();
			if (document == null)
				return null;
			DepositPaygateModel model = new DepositPaygateModel(document.getString("Id"),
					document.getString("CreatedAt"), document.getString("ModifiedAt"),
					document.getBoolean("IsDeleted"), document.getString("CartId"),
					document.getString("ReferenceId"), document.getString("UserId"),
					document.getString("Username"), document.getString("Nickname"),
					document.getString("RequestTime"), document.getString("BankCode"),
					document.getString("PaymentType"), document.getString("MerchantCode"),
					document.getString("ProviderName"), document.getLong("Amount"),
					document.getLong("AmountFee"), document.getInteger("Status"),
					document.getString("BankAccountNumber"), document.getString("BankAccountName"),
					document.getString("Description"), document.getString("UserApprove"));
			return model;
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	/**
	 * Search transaction
	 * @param depositPayWellModel: Contain value of fields want to search
	 * @param status
	 * @param page
	 * @param maxItem
	 * @param fromTime
	 * @param endTime
	 * @return DepositPaygateModel
	 */
	@Override
	public DepositPaygateReponse Find(DepositPaygateModel depositPayWellModel, int page, int maxItem, String fromTime,
			String endTime) {
		try {
			final ArrayList<DepositPaygateModel> records = new ArrayList<DepositPaygateModel>();
			final ArrayList<Long> num = new ArrayList<Long>();
			num.add(0, 0L);
			num.add(1, 0L);
			num.add(2, 0L);
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			MongoCollection<?> col = db.getCollection(DEPOSIT_COLLECTION);
			page = (page - 1) < 0 ? 0 : (page - 1);
			int numStart = page * maxItem;
			int numEnd = maxItem;
			BasicDBObject objsort = new BasicDBObject();
			objsort.put("_id", -1);
			HashMap<String, Object> conditions = new HashMap<String, Object>();
			if (!depositPayWellModel.Nickname.isEmpty()) {
				String pattern = ".*" + depositPayWellModel.Nickname + ".*";
				conditions.put("Nickname", new BasicDBObject().append("$regex", pattern)
						.append("$options", "i"));
			}

			if (!depositPayWellModel.Id.isEmpty()) {
				conditions.put("Id", depositPayWellModel.Id);
			}

			if (!depositPayWellModel.ProviderName.isEmpty()) {
				conditions.put("ProviderName", depositPayWellModel.ProviderName);
			}

			if (depositPayWellModel.Status > 0) {
				conditions.put("Status", depositPayWellModel.Status);
			}

			if (!fromTime.isEmpty() && !endTime.isEmpty()) {
				BasicDBObject obj = new BasicDBObject();
				obj.put("$gte", fromTime);
				obj.put("$lte", endTime);
				conditions.put("CreatedAt", obj);
			}

			FindIterable<?> iterable = col.find(new Document(conditions)).sort(objsort).skip(numStart)
					.limit(maxItem);
			iterable.forEach((Block) new Block<Document>() {
				public void apply(Document document) {
					DepositPaygateModel model = new DepositPaygateModel(document.getString("Id"),
							document.getString("CreatedAt"), document.getString("ModifiedAt"),
							document.getBoolean("IsDeleted"), document.getString("CartId"),
							document.getString("ReferenceId"), document.getString("UserId"),
							document.getString("Username"), document.getString("Nickname"),
							document.getString("RequestTime"), document.getString("BankCode"),
							document.getString("PaymentType"), document.getString("MerchantCode"),
							document.getString("ProviderName"), document.getLong("Amount"),
							document.getLong("AmountFee"), document.getInteger("Status"),
							document.getString("BankAccountNumber"), document.getString("BankAccountName"),
							document.getString("Description"), document.getString("UserApprove"));
					records.add(model);
				}
			});
			FindIterable<?> iterable2 = col.find(new Document(conditions));
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

			DepositPaygateReponse res = new DepositPaygateReponse(num.get(0), num.get(2), num.get(1), records);
			return res;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Search transaction
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
	public DepositPaygateReponse Find(String nickname, int status, int page, int maxItem, String fromTime,
			String endTime, String providerName) {
		try {
			final ArrayList<DepositPaygateModel> records = new ArrayList<DepositPaygateModel>();
			final ArrayList<Long> num = new ArrayList<Long>();
			num.add(0, 0L);
			num.add(1, 0L);
			num.add(2, 0L);
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			MongoCollection<?> col = db.getCollection(DEPOSIT_COLLECTION);
			page = (page - 1) < 0 ? 0 : (page - 1);
			int numStart = page * maxItem;
			int numEnd = maxItem;
			BasicDBObject objsort = new BasicDBObject();
			objsort.put("_id", -1);
			HashMap<String, Object> conditions = new HashMap<String, Object>();
			if (!nickname.isEmpty()) {
				String pattern = ".*" + nickname + ".*";
				conditions.put("Nickname", new BasicDBObject().append("$regex", pattern)
						.append("$options", "i"));
			}

			if (!providerName.isEmpty()) {
				conditions.put("ProviderName", providerName);
			}

			if (status > -1) {
				conditions.put("Status", status);
			}

			if (!fromTime.isEmpty() && !endTime.isEmpty()) {
				BasicDBObject obj = new BasicDBObject();
				obj.put("$gte", fromTime);
				obj.put("$lte", endTime);
				conditions.put("CreatedAt", obj);
			}

			FindIterable<?> iterable = col.find(new Document(conditions)).sort(objsort).skip(numStart)
					.limit(maxItem);
			iterable.forEach((Block) new Block<Document>() {
				public void apply(Document document) {
					DepositPaygateModel model = new DepositPaygateModel(document.getString("Id"),
							document.getString("CreatedAt"), document.getString("ModifiedAt"),
							document.getBoolean("IsDeleted"), document.getString("CartId"),
							document.getString("ReferenceId"), document.getString("UserId"),
							document.getString("Username"), document.getString("Nickname"),
							document.getString("RequestTime"), document.getString("BankCode"),
							document.getString("PaymentType"), document.getString("MerchantCode"),
							document.getString("ProviderName"), document.getLong("Amount"),
							document.getLong("AmountFee"), document.getInteger("Status"),
							document.getString("BankAccountNumber"), document.getString("BankAccountName"),
							document.getString("Description"), document.getString("UserApprove"));
					records.add(model);
				}
			});
			FindIterable<?> iterable2 = col.find(new Document(conditions));
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

			DepositPaygateReponse res = new DepositPaygateReponse(num.get(0), num.get(2), num.get(1), records);
			return res;

		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Search transaction only display for user play
	 * @param nickname
	 * @param status
	 * @param page
	 * @param maxItem
	 * @param fromTime
	 * @param endTime
	 * @param providerName (If want get all, set value is empty)
	 * @return Map<String, Object>
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
				conditions.put("Nickname", new BasicDBObject().append("$regex", pattern)
						.append("$options", "i"));
			}

			if (!StringUtils.isBlank(providerName)) {
				conditions.put("ProviderName", providerName);
			}
			
			if (status > -1) {
				conditions.put("Status", status);
			}
			else {
				BasicDBObject st = new BasicDBObject();
				List<Integer> statusDefault = Arrays.asList(0, 1, 2, 3, 4);
				st.put("$in", statusDefault);
				conditions.append("Status", st);
			}

			Document match = new Document("$match", conditions);
			Document project = new Document();
			project = new Document("$project", new Document("_id", 0).append("CreatedAt", 1)
					.append("Amount", 1).append("AmountFee", 1).append("OrderId", 1)
					.append("BankCode", 1).append("BankAccountNumber", 1)
					.append("BankAccountName", 1).append("ProviderName", 1)
					.append("Status", 1));
			Document sort = new Document();
			sort = new Document("$sort", new Document("CreatedAt", -1));
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			MongoCollection<?> collection = db.getCollection(DEPOSIT_COLLECTION);
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
			AggregateIterable<Document> aggregate = (AggregateIterable<Document>) collection.aggregate(Arrays.asList(match, sort, skip, limit, project)).allowDiskUse(true)
					.allowDiskUse(true);
			for (Document document : aggregate) {
				try {
					String bankCode = document.get("BankCode").toString();
					Bank bank = GameCommon.LIST_BANK_NAME.stream()
							  .filter(x -> bankCode.equals(x.getCode()))
							  .findAny()
							  .orElse(null);
					if(bank != null) {
						document.remove("BankCode");
						document.append("BankCode", bank == null ? bankCode : bank.getBank_name());
					}else {
						document.remove("BankCode");
						switch (bankCode) {
						case "VTT":
							document.append("BankCode","Viettel");
							break;
						case "VNP":
							document.append("BankCode","Vinaphone");
							break;
						case "VMS":
							document.append("BankCode","Mobiphone");
							break;
						case "VNM":
							document.append("BankCode","VietNam Mobile");
							break;
						case "VCOIN":
							document.append("BankCode","VCOIN");
							break;
						case "ZING":
							document.append("BankCode","ZING");
							break;
						case "GATE":
							document.append("BankCode","FPT GATE");
							break;
						default:
							document.append("BankCode",bankCode);
							break;
						}
					}
					
					result.add(document);
				} catch (Exception e) { }
			}
			logger.error("FindTransaction 1  " + result.size());
			result.addAll(resultDataMomo(nickname, page, maxItem));
			result.addAll(resultDataThe(nickname, page, maxItem));
			Collections.sort(result, (o1, o2) -> {
				String bank1 = ((Document) o1).get("CreatedAt").toString();
				String bank2 = ((Document) o2).get("CreatedAt").toString();
				return bank2.compareTo(bank1);
			});
			logger.error("FindTransaction 2  " + result.size());
			
			data.put("data", result);
			// Get total record found
			Document count = new Document();
			count = new Document("$count", "OrderId");
			int totalRecord = 0;
			AggregateIterable<Document> aggregateCount = (AggregateIterable<Document>) collection
					.aggregate(Arrays.asList(match, count)).allowDiskUse(true);
			for (Document document : aggregateCount) {
				try {
					totalRecord = document.getInteger("OrderId", 0);
				} catch (Exception e) { totalRecord = 0; }
			}
			
			data.put("totalRecord", 100);
		} catch (Exception e) {
			logger.error("Search FindTransaction error: " + e.getMessage());
			data = new HashMap<>();
			data.put("data", new ArrayList<>());
			data.put("totalRecord", 0);
		}

		return data;
	}

	public List<Object> resultDataMomo(String nickname, int page, int maxItem) {
		List<Object> result = new ArrayList<>();
		BasicDBObject conditions = new BasicDBObject();

		if (!StringUtils.isBlank(nickname)) {
			String pattern = ".*" + nickname + ".*";
			conditions.put("Nickname", new BasicDBObject().append("$regex", pattern)
					.append("$options", "i"));
		}
		conditions.put("Status", 100);

		Document match = new Document("$match", conditions);
		Document project = new Document();
		project = new Document("$project", new Document("_id", 0).append("CreatedAt", 1)
				.append("Amount", 1).append("AmountFee", 1).append("OrderId", 1)
				.append("BankCode", 1).append("BankAccountNumber", 1)
				.append("BankAccountName", 1).append("ProviderName", 1)
				.append("Status", 1));
		Document sort = new Document();
		sort = new Document("$sort", new Document("CreatedAt", -1));
		MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
		MongoCollection<?> collection = db.getCollection(DvtConst.DEPOSIT_MOMO_COLLECTION);
		Document skip = new Document();
		skip = new Document("$skip", page * maxItem);
		Document limit = new Document();
		limit = new Document("$limit", maxItem);
		AggregateIterable<Document> aggregate = (AggregateIterable<Document>) collection.aggregate(Arrays.asList(match, sort, skip, limit, project)).allowDiskUse(true)
				.allowDiskUse(true);
		for (Document document : aggregate) {
			document.remove("BankCode");
			document.append("BankCode", "MoMo");
			result.add(document);
			logger.error("FindTransaction mm" + document.get("Amount").toString());
		}
		return result;
	}

	public List<Object> resultDataThe(String nickname, int page, int maxItem) {
		List<Object> result = new ArrayList<>();
		BasicDBObject conditions = new BasicDBObject();

		if (!StringUtils.isBlank(nickname)) {
			String pattern = ".*" + nickname + ".*";
			conditions.put("nick_name", new BasicDBObject().append("$regex", pattern)
					.append("$options", "i"));
		}

		Document match = new Document("$match", conditions);
		Document project = new Document();
		project = new Document("$project", new Document("_id", 0).append("time_log", 1)
				.append("amount", 1).append("AmountFee", 1).append("OrderId", 1)
				.append("BankCode", 1).append("BankAccountNumber", 1)
				.append("BankAccountName", 1).append("ProviderName", 1)
				.append("code", 1));
		Document sort = new Document();
		sort = new Document("$sort", new Document("time_log", -1));
		MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
		MongoCollection<?> collection = db.getCollection("dvt_recharge_by_gachthe");
		Document skip = new Document();
		skip = new Document("$skip", page * maxItem);
		Document limit = new Document();
		limit = new Document("$limit", maxItem);
		logger.error("FindTransaction the  " + limit + " "  + maxItem);
		AggregateIterable<Document> aggregate = (AggregateIterable<Document>) collection.aggregate(Arrays.asList(match, sort, skip, limit, project)).allowDiskUse(true)
				.allowDiskUse(true);
		for (Document document : aggregate) {
			logger.error("FindTransaction the  " + document);
			document.remove("BankCode");
			document.append("BankCode", "NapThe");
			document.append("CreatedAt", document.get("time_log").toString());
			logger.error("FindTransaction the 1 " + document);
			document.remove("time_log");
			document.append("Status", document.getInteger("code"));
			logger.error("FindTransaction the 2 " + document);
			document.remove("code");
			document.append("Amount", document.getLong("amount"));
			logger.error("FindTransaction the 3 " + document);
			document.remove("amount");
			logger.error("FindTransaction the 4 " + document);
			result.add(document);
			logger.error("FindTransaction the  " + document.get("Amount").toString());
		}
		return result;
	}

	public Map<String, Object> FindTransaction(String nickname, String agentCode, int status, int page, int maxItem,
											   String fromTime, String endTime, String providerName) {
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

			if (!StringUtils.isBlank(agentCode)) {
				conditions.put("MerchantCode", agentCode);
			}

			if (!StringUtils.isBlank(providerName)) {
				conditions.put("ProviderName", providerName);
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
							.append("CartId", 1).append("Nickname", 1).append("Username", 1));
			Document sort = new Document();
			sort = new Document("$sort", new Document("CreatedAt", -1));
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			MongoCollection<?> collection = db.getCollection(DEPOSIT_COLLECTION);
			page = (page - 1) < 0 ? 0 : (page - 1);
			int numStart = page * maxItem;
			int numEnd = maxItem;
			Document skip = new Document();
			skip = new Document("$skip", numStart);
			Document limit = new Document();
			limit = new Document("$limit", numEnd);
			List<Object> result = new ArrayList<>();
			AggregateIterable<Document> aggregate = (AggregateIterable<Document>) collection
					.aggregate(Arrays.asList(match, sort, skip, limit, project)).allowDiskUse(true).allowDiskUse(true);
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
			count = new Document("$count", "Id");
			int totalRecord = 0;
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

	@Override
	public ArrayList<Object> find(HashMap<String, Object> conditions, int page, int maxItem) throws Exception{
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			page = Math.max((page - 1), 0);

			BasicDBObject objsort = new BasicDBObject();
			objsort.put("_id", -1);

			return db.getCollection(DEPOSIT_COLLECTION)
					.find((Bson) new Document(conditions))
					.projection(Projections.exclude("_id"))
					.sort((Bson) objsort)
					.skip(page * maxItem)
					.limit(maxItem)
					.map(x -> JSON.parse(x.toJson()))
					.into(new ArrayList<>());

		} catch (Exception e) {
			throw e;
		}
	}

	public long count(HashMap<String, Object> conditions) {
		MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
		return db.getCollection(DEPOSIT_COLLECTION).count((Bson) new Document(conditions));
	}

	public Long[] statistic(HashMap<String, Object> conditions){
		Long[] num = {
				0L, // tong thanh cong
				0L, // tong so tien thanh cong
				0L, // tong so tien
				0L
		};
		Set<String> set = new HashSet<>();
		MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
		db.getCollection(DEPOSIT_COLLECTION).find((Bson) new Document(conditions)).forEach(new Block<Document>() {
			public void apply(Document document) {
				long amount = document.getLong("Amount");
				int code = document.getInteger("Status");
				if (code == DvtConst.STATUS_COMPLETE) {
					num[0]++;
					num[1] += amount;
				}
				num[2] += amount;
				set.add(document.getString("Username"));
			}
		});
		num[3] = Long.valueOf(set.size());

		return num;
	}

	/**
	 * Search transaction
	 * @param nickname
	 * @param status
	 * @param page
	 * @param maxItem
	 * @param fromTime
	 * @param endTime
	 * @param providerName (If want get all, set value is empty)
	 * @param orderId
	 * @param transactionId
	 * @return DepositPaygateModel
	 */
	@Override
	public DepositPaygateReponse Find(String nickname, int status, int page, int maxItem, String fromTime,
			String endTime, String providerName, String orderId, String transactionId, String bankName,Double fromAmount,Double toAmount) {
		try {
			final ArrayList<DepositPaygateModel> records = new ArrayList<DepositPaygateModel>();
			final ArrayList<Long> num = new ArrayList<Long>();
			num.add(0, 0L);
			num.add(1, 0L);
			num.add(2, 0L);
			MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
			MongoCollection<?> col = db.getCollection(DEPOSIT_COLLECTION);
			page = (page - 1) < 0 ? 0 : (page - 1);
			int numStart = page * maxItem;
			int numEnd = maxItem;
			BasicDBObject objsort = new BasicDBObject();
			objsort.put("_id", -1);
			HashMap<String, Object> conditions = new HashMap<String, Object>();
			if (nickname!=null && !nickname.isEmpty()) {
				String pattern = ".*" + nickname + ".*";
				conditions.put("Nickname", new BasicDBObject().append("$regex", pattern)
						.append("$options", "i"));
			}

			if (providerName!=null &&!providerName.isEmpty()) {
				conditions.put("ProviderName", providerName);
			}

			if (orderId!=null &&!orderId.isEmpty()) {
				conditions.put("CartId", orderId);
			}

			if (transactionId!=null &&!transactionId.isEmpty()) {
				conditions.put("ReferenceId", transactionId);
			}

			if (status >= 0) {
				conditions.put("Status", status);
			}

			if(bankName!=null &&!"".equals(bankName)) {
				conditions.put("BankCode", bankName);
			}
			if(fromAmount!=null) {
				BasicDBObject obj = new BasicDBObject();
				obj.put("$gte", fromAmount.longValue());
				conditions.put("Amount", obj);
			}
			if(toAmount!=null) {
				BasicDBObject obj = new BasicDBObject();
				obj.put("$lte", toAmount.longValue());
				conditions.put("Amount", obj);
			}

			if (fromTime!=null &&!fromTime.isEmpty() && endTime!=null &&!endTime.isEmpty()) {
				BasicDBObject obj = new BasicDBObject();
				obj.put("$gte", fromTime);
				obj.put("$lte", endTime);
				conditions.put("CreatedAt", obj);
			}

			FindIterable<?> iterable = col.find(new Document(conditions)).sort(objsort).skip(numStart)
					.limit(maxItem);
			iterable.forEach((Block) new Block<Document>() {
				public void apply(Document document) {
					DepositPaygateModel model = new DepositPaygateModel(document.getString("Id"),
							document.getString("CreatedAt"), document.getString("ModifiedAt"),
							document.getBoolean("IsDeleted"), document.getString("CartId"),
							document.getString("ReferenceId"), document.getString("UserId"),
							document.getString("Username"), document.getString("Nickname"),
							document.getString("RequestTime"), document.getString("BankCode"),
							document.getString("PaymentType"), document.getString("MerchantCode"),
							document.getString("ProviderName"), document.getLong("Amount"),
							document.getLong("AmountFee"), document.getInteger("Status"),
							document.getString("BankAccountNumber"), document.getString("BankAccountName"),
							document.getString("Description"), document.getString("UserApprove"));
					records.add(model);
				}
			});
			FindIterable<?> iterable2 = col.find(new Document(conditions));
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

			DepositPaygateReponse res = new DepositPaygateReponse(num.get(0), num.get(2), num.get(1), records);
			return res;

		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}
}
