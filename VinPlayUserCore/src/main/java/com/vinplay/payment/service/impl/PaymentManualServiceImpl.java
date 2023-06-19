package com.vinplay.payment.service.impl;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoDatabase;
import com.vinplay.dichvuthe.response.RechargePaywellResponse;
import com.vinplay.payment.dao.RechargePaygateDao;
import com.vinplay.payment.dao.WithDrawPaygateDao;
import com.vinplay.payment.dao.impl.RechargePaygateDaoImpl;
import com.vinplay.payment.dao.impl.WithDrawPaygateDaoImpl;
import com.vinplay.payment.entities.DepositPaygateModel;
import com.vinplay.payment.entities.WithDrawPaygateModel;
import com.vinplay.payment.service.PaymentConfigService;
import com.vinplay.payment.service.PaymentManualService;
import com.vinplay.payment.utils.PayCommon;
import com.vinplay.payment.utils.PayCommon.PAYSTATUS;
import com.vinplay.usercore.logger.MoneyLogger;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.messages.LogMoneyUserMessage;
import com.vinplay.vbee.common.messages.MoneyMessageInMinigame;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.common.rmq.RMQApi;
import com.vinplay.vbee.common.statics.Consts;
import com.vinplay.vbee.common.utils.VinPlayUtils;
import com.vinplay.payment.utils.PayUtils;
import com.vinplay.utils.TelegramAlert;

public class PaymentManualServiceImpl implements PaymentManualService {
	
	private static final Logger logger = Logger.getLogger("backend");
	
	private static final String PAYMENTNAME = "manual";
	
	@Override
	public RechargePaywellResponse withdrawal(String orderId, String approvedName, String ip) {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
		try {
			WithDrawPaygateDao withdrawDao = new WithDrawPaygateDaoImpl();
			WithDrawPaygateModel model = withdrawDao.GetByOrderId(orderId);
			
			if (model == null) {
				res.setData("Object does not exist");
				return res;
			}
			if (PayCommon.PAYSTATUS.REQUEST.getId() != model.Status) {
				res.setData("status wrong , status = " + model.Status);
				return res;
			}
			if (model.Amount < 100000) {
				res.setData("So tien rut nho hon so tien quy dinh");
				return res;
			}
			String transID = "WD"+PayUtils.getCurDate("yyMMddHHmmss") + PayUtils.getids();
			//update status =COMPLETED
			boolean isUpdate = this.updateStatus(orderId, transID, PAYSTATUS.COMPLETED.getId(), approvedName);
			if(isUpdate) {
				res.setCode(0);
				res.setData("");
			}else {
				res.setCode(1);
				res.setData("update status to COMPLETED was fail");
			}
			return res;
		} catch (Exception e) {
			logger.error(e);
			return res;
		}
	}
	
	public Boolean updateStatus(String orderId, String cartId, int status, String userApprove) {
		try {
			MongoDatabase db = MongoDBConnectionFactory.getDB();
			BasicDBObject updateFields = new BasicDBObject();
			updateFields.append("ReferenceId", cartId);
			updateFields.append("Status", status);
			updateFields.append("ProviderName", "manualbank");
			updateFields.append("MerchantCode", "manualbank");
			updateFields.append("ModifiedAt", VinPlayUtils.getCurrentDateTime());
			updateFields.append("UserApprove", userApprove);
			db.getCollection("withdrawal_paygate").updateOne((Bson) new Document("CartId", orderId),
					(Bson) new Document("$set", updateFields));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private RechargePaywellResponse createOrder(String nickName, String customerName, Long amount, String bankName,
			String bankNum, String payType, String desc) {
	        try{
	            RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L,"");
	            
	            if (amount < 20000) {
    				res.setData("Số tiền nạp nhỏ hơn 20.000 VNĐ");
    				return res;
    			}
	            
//	            if(paymentType.equals("bank_recharge")) {
//	            	if (amount < 20000) {
//	    				res.setData("Số tiền nạp nhỏ hơn 20.000 VNĐ");
//	    				return res;
//	    			}
//	            	if (amount > 20000000) {
//	    				res.setData("Số tiền nạp lớn hơn 20.000.000 VNĐ");
//	    				return res;
//	    			}
//	            }else {
//	            	if (amount < paymentConfig.getConfig().getMinMoney()) {
//	    				res.setData("So tien nap nho hon so tien quy dinh");
//	    				return res;
//	    			}
//				}
				// get pending transaction
	            DepositPaygateModel model = new DepositPaygateModel();
	            model.Id = "";
	            model.Amount = amount;
	            model.BankAccountName = customerName;
	            model.BankAccountNumber = bankNum;
	            model.BankCode = bankName;
	            model.CartId = "";
	            model.CreatedAt = "";
	            model.IsDeleted = false;
	            model.PaymentType = payType;
	            model.MerchantCode = "manual";
	            model.ProviderName = PAYMENTNAME;
	            model.ModifiedAt = "";
	            model.Nickname = nickName;
	            model.ReferenceId = "";
	            model.RequestTime = VinPlayUtils.getCurrentDateTime();
	            model.Status = PayCommon.PAYSTATUS.PENDING.getId();
				model.Description = desc;
	            model.UserApprove = nickName;
	            RechargePaygateDao rechargeDao = new RechargePaygateDaoImpl();
	            long id = rechargeDao.Add(model);
	            if(id == 0){
	            	logger.error("Lỗi tạo bản ghi database mongo");
	                return res;
	            }
	            res.setCode(0);
	            res.setTid(String.valueOf(id));
	            //send message telegram
	            TelegramAlert.SendMessageDepositBank(model);
	            return res;
	        }catch (Exception e){
	            return null;
	        }
	    }
	 
		@Override
		public RechargePaywellResponse deposit(String nickName, String customerName, Long amount, String bankName,
				String bankNum, String payType, String desc) {
			try {
				return createOrder(nickName, customerName, amount, bankName, bankNum, payType, desc);
			} catch (Exception e) {
				logger.error(e);
				return new RechargePaywellResponse(1, 0L, 0, 0L, e.getMessage());
			}
		}

		@Override
		public RechargePaywellResponse depositConfirm(String orderId, String approvedName, String ip, int status,
				String rs) {
			RechargePaygateDao dao = new RechargePaygateDaoImpl();
			//check old status and check exist
			DepositPaygateModel modelOld = dao.GetByOrderId(orderId);
			if (modelOld != null) {
				int statusOld = modelOld.Status;
				if (statusOld != PayCommon.PAYSTATUS.PENDING.getId()) {
					return new RechargePaywellResponse(1, 0L, 0, 0L, "Chỉ có thể phê duyệt đc từ trạng thái pending");
				}
				String providerName = modelOld.ProviderName;
				if(!PAYMENTNAME.equals(providerName)) {
					return new RechargePaywellResponse(1, 0L, 0, 0L, "Chỉ có thể phê duyệt đc từ hình thức nạp thủ công");
				}
			}else {
				return new RechargePaywellResponse(1, 0L, 0, 0L, "Chỉ có thể phê duyệt đc từ hình thức nạp thủ công");
			}
			modelOld.Status=status;
			modelOld.UserApprove = approvedName;
			modelOld.Description =rs;
			modelOld.ReferenceId = orderId;
			boolean isuc = dao.UpdateStatus(orderId, orderId, status, approvedName, rs);
			// add money
			if (isuc) {
				if(status==PayCommon.PAYSTATUS.COMPLETED.getId()) {
					return addMoney(modelOld);
				}else {
					return new RechargePaywellResponse(0, 0L, 0, 0L, "");
				}
			} else {
				return new RechargePaywellResponse(1, 0L, 0, 0L, "");
			}
		}
		private RechargePaywellResponse addMoney(DepositPaygateModel depositPayWellModel) {
			RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "fail");
			// TODO: get ratio exchange
			// money = Math.round((double)money *
			// GameCommon.getValueDouble("RATIO_RECHARGE_BANK"));
			HazelcastInstance client = HazelcastClientFactory.getInstance();
			if (client == null) {
				MoneyLogger.log(depositPayWellModel.Nickname, PAYMENTNAME, depositPayWellModel.Amount, 0L, "vin", "Nap vin qua ngan hang", "1031",
						"Cannot connect hazelcast");
				return res;
			}

			IMap<String, UserModel> userMap = client.getMap("users");
			if (!userMap.containsKey(depositPayWellModel.Nickname))
				return res;
			try {
				userMap.lock(depositPayWellModel.Nickname);
				UserCacheModel user = (UserCacheModel) userMap.get(depositPayWellModel.Nickname);
				long moneyUser = user.getVin();
				long currentMoney = user.getVinTotal();
				long rechargeMoney = user.getRechargeMoney();
				user.setVin(moneyUser += depositPayWellModel.Amount);
				user.setVinTotal(currentMoney += depositPayWellModel.Amount);
				user.setRechargeMoney(rechargeMoney += depositPayWellModel.Amount);
				String desc = "Nạp tiền Roy88";
				MoneyMessageInMinigame messageMoney = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), user.getId(),
						depositPayWellModel.Nickname, Consts.RECHARGE_MANUAL, moneyUser, currentMoney, depositPayWellModel.Amount, "vin", 0L, 0, 0);
				LogMoneyUserMessage messageLog = new LogMoneyUserMessage(user.getId(), depositPayWellModel.Nickname, Consts.RECHARGE_MANUAL,
						PAYMENTNAME, currentMoney, depositPayWellModel.Amount, "vin", desc, 0L, false, user.isBot());
				RMQApi.publishMessagePayment(messageMoney, 16);
				RMQApi.publishMessageLogMoney( messageLog);
				userMap.put(depositPayWellModel.Nickname, user);
				res.setCode(0);
				res.setData("");
			} catch (Exception e2) {
				logger.debug(e2);
				MoneyLogger.log(depositPayWellModel.Nickname, PAYMENTNAME, depositPayWellModel.Amount, 0L, "vin", "Nap vin qua " + PAYMENTNAME, "1031",
						"rmq error: " + e2.getMessage());
			} finally {
				userMap.unlock(depositPayWellModel.Nickname);
			}

			return res;
		}
}
