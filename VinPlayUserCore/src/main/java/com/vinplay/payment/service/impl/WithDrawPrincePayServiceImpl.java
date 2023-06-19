package com.vinplay.payment.service.impl;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.dichvuthe.response.RechargePaywellResponse;
import com.vinplay.payment.dao.RechargePaygateDao;
import com.vinplay.payment.dao.WithDrawPaygateDao;
import com.vinplay.payment.dao.impl.RechargePaygateDaoImpl;
import com.vinplay.payment.dao.impl.WithDrawPaygateDaoImpl;
import com.vinplay.payment.entities.Config;
import com.vinplay.payment.entities.PaymentConfig;
import com.vinplay.payment.entities.UserBank;
import com.vinplay.payment.entities.UserWithdraw;
import com.vinplay.payment.entities.WithDrawPaygateModel;
import com.vinplay.payment.entities.WithDrawPaygateReponse;
import com.vinplay.payment.service.PaymentConfigService;
import com.vinplay.payment.service.WithDrawPrincePayService;
import com.vinplay.payment.utils.PayCommon;
import com.vinplay.payment.utils.PayCommon.PAYSTATUS;
import com.vinplay.payment.utils.PayUtils;
import com.vinplay.payment.utils.PaymentConstant;
import com.vinplay.usercore.dao.UserBankDao;
import com.vinplay.usercore.dao.impl.UserBankDaoImpl;
import com.vinplay.usercore.logger.MoneyLogger;
import com.vinplay.utils.TelegramAlert;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.messages.LogMoneyUserMessage;
import com.vinplay.vbee.common.messages.MoneyMessageInMinigame;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.rmq.RMQApi;
import com.vinplay.vbee.common.statics.Consts;
import com.vinplay.vbee.common.utils.VinPlayUtils;

public class WithDrawPrincePayServiceImpl
implements WithDrawPrincePayService {
	private static final String NOTIFY_WITHDRAW_URL = "https://iwspay.roy88.vip/payprince/withdraw/notify";
    private static final Logger logger = Logger.getLogger("backend");
    private static final String PAYMENTNAME = "princepay";
    
	private PaymentConfig getPaymentConfig() {
		PaymentConfigService paymentConfigService = new PaymentConfigServiceImpl();
		return paymentConfigService.getConfigByKey(PAYMENTNAME);
	}
    
	/**
     * Update request time by orderId
     * @param orderId
     * @param requesTime
     * @param userApprove
     * @see RechargePaywellResponse
     */
    
    /**
     * Update transactionId of oder by orderId
     * @param orderId
     * @param amount
     * @param bankCode
     * @param payType
     * @param customerId
     * @param customerFullName
     * @see RechargePaywellResponse
     * @throws Exception
     */
    private String getSignal(String []signArray) {
    	
    	Arrays.sort(signArray);
    	StringBuilder resu =new StringBuilder();
		for (String s : signArray) {
			resu.append(s).append("&");
		}
		resu.append("key=").append(this.getPaymentConfig().getConfig().getMerchantKey());
		return PayCommon.getMd5(resu.toString()).toUpperCase();
    }
	
	private RechargePaywellResponse requestCreateWithDrawTransaction(WithDrawPaygateModel model, String ip) throws Exception {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
		ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
		long timetick = utc.toEpochSecond();
//		if (updateRequestTime(model.CartId, String.valueOf(timetick), "superadmin").getCode() != 0) {
//			res.setData("");
//			return res;
//		}

		Config config = this.getPaymentConfig().getConfig();
		String notifyUrlWithdraw = config.getNotifyWithdrawUrl();
		if (notifyUrlWithdraw == null || "".equals(notifyUrlWithdraw)) {
			notifyUrlWithdraw = NOTIFY_WITHDRAW_URL;
		}
		String []paramArray = {
					"uid="+config.getMerchantCode(),
					"orderid="+model.CartId,
					"channel="+model.PaymentType,
					"notify_url="+notifyUrlWithdraw,
					"amount="+model.Amount,
					"userip="+ip,
					"timestamp="+timetick,
					"custom="+model.BankAccountName,
					"bank_account="+model.BankAccountName,
					"bank_no="+model.BankAccountNumber,
					"bank_id="+model.BankCode,
					"bank_province="+model.BankBranch,
					"bank_city=VN",
					"bank_sub="+model.BankBranch,
					"user_name=" + ""
				};
		String sign =getSignal(paramArray);
		if (sign.isEmpty()) {
			res.setData("Sai chữ ký");
			return res;
		}
		// call api		//amount=200000","uid=roy88","orderid=roy88","channel=908","notify_url=https://roy88.vip",
		//"return_url=https://roy88.vip","userip=127.0.0.1","timestamp=1608823688","custom=nguyen"
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("uid", config.getMerchantCode()));
		urlParameters.add(new BasicNameValuePair("orderid", model.CartId));
		urlParameters.add(new BasicNameValuePair("channel", model.PaymentType));
		urlParameters.add(new BasicNameValuePair("notify_url", notifyUrlWithdraw));
		urlParameters.add(new BasicNameValuePair("amount", String.valueOf(model.Amount)));
		urlParameters.add(new BasicNameValuePair("userip", ip));
		urlParameters.add(new BasicNameValuePair("timestamp", timetick+""));
		urlParameters.add(new BasicNameValuePair("custom", model.BankAccountName));
		urlParameters.add(new BasicNameValuePair("sign", sign));
		urlParameters.add(new BasicNameValuePair("bank_account", model.BankAccountName));
		urlParameters.add(new BasicNameValuePair("bank_no", model.BankAccountNumber));
		urlParameters.add(new BasicNameValuePair("bank_id", model.BankCode));
		urlParameters.add(new BasicNameValuePair("bank_province", model.BankBranch));
		urlParameters.add(new BasicNameValuePair("bank_city", "VN"));
		urlParameters.add(new BasicNameValuePair("bank_sub", model.BankBranch));
		urlParameters.add(new BasicNameValuePair("user_name", ""));
		logger.info(PAYMENTNAME + "Request: " + urlParameters.toString());
		String url = "https://api.princepay.support/applyfor";
		String result = PayUtils.requestAPI(url, urlParameters);//{"code":2,"message":"api.princeapplyfor.support: Name or service not known"}
		logger.info(PAYMENTNAME + "Response: " + result.toString());
		JSONObject jsonObj = new JSONObject(result);
		WithDrawPaygateDao withDrawPaygateDao = new WithDrawPaygateDaoImpl();
		if (jsonObj.getInt("status") != 10000) {
			if (jsonObj.getInt("status") == 30020) {
				//hết tiền ví
				res.setCode(jsonObj.getInt("status"));
				res.setData("Ví cổng thanh toán PRINCEPAY hết tiền !");
				TelegramAlert.SendMessage("Ví cổng thanh toán PRINCEPAY hết tiền !");
			}else {
				//error
				res.setCode(jsonObj.getInt("status"));
				withDrawPaygateDao.UpdateStatus(model.CartId, PayCommon.PAYSTATUS.FAILED.getId(), model.UserApprove);
				res.setData(jsonObj.getString("result"));
				//push notification Telegram
				TelegramAlert.SendMessage("[NOTIFY_WITHDRAW] NOTIFY status ="+PayCommon.PAYSTATUS.FAILED.getId() +" . Vui lòng kiểm tra order_id trên princepay và cộng tiền thủ công lại cho khách , order_id ="+model.CartId);
			}
		}else {
			//success
			JSONObject jsonObjResult = new JSONObject(jsonObj.getString("result"));
			String transactionid;
			try {
				transactionid = jsonObjResult.getString("transactionid");
			} catch (Exception e) {
				transactionid = "";
			}
			if (transactionid==null || transactionid.isEmpty()) {
				withDrawPaygateDao.UpdateStatus(model.CartId, PayCommon.PAYSTATUS.FAILED.getId(), model.UserApprove);
				res.setData(jsonObj.getString("result"));
				//push notification Telegram
				TelegramAlert.SendMessage("[NOTIFY_WITHDRAW] Không thể lấy đc transactionid trên princepay . Status ="+PayCommon.PAYSTATUS.FAILED.getId() +" . Vui lòng kiểm tra order_id trên princepay  , order_id ="+model.CartId);
				return res;
			}
			
			res.setCode(0);
			withDrawPaygateDao.UpdateStatus(model.CartId, transactionid, PayCommon.PAYSTATUS.PENDING.getId(), model.UserApprove);
			res.setData(jsonObj.getString("result"));
		}
		
		return res;
	}
    
    /**
	 * Discount money customer
	 * 
	 * @param orderId
	 * @param transId
	 * @param nickname
	 * @param amount
	 * @see RechargePaywellResponse
	 */
	private RechargePaywellResponse discountMoney(String orderId, String nickname, long amount) {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "fail");
		// TODO: get ratio exchange
		// money = Math.round((double)money *
		// GameCommon.getValueDouble("RATIO_RECHARGE_BANK"));
		HazelcastInstance client = HazelcastClientFactory.getInstance();
		if (client == null) {
			MoneyLogger.log(nickname, Consts.REQUEST_CASHOUT, amount, 0L, "vin", "Yêu cầu rút tiền",
					"1031", "Cannot connect hazelcast");
			return res;
		}

		String username = "";
		IMap<String, UserModel> userMap = client.getMap("users");
		if (!userMap.containsKey(nickname))
			return res;

		try {
			userMap.lock(nickname);
			WithDrawPaygateDao rechargeDao = new WithDrawPaygateDaoImpl();
			WithDrawPaygateModel model = rechargeDao.GetById(orderId);
			if (model == null)
				return res;

			UserCacheModel user = (UserCacheModel) userMap.get(nickname);
			username = user.getUsername();
			long moneyUser = user.getVin();
			long currentMoney = user.getVinTotal();
			int cashoutMoney = user.getCashout();

			if (moneyUser < amount || currentMoney < amount) {
				res.setData("Tai khoan khong du tien");
				return res;
			}

			user.setVin(moneyUser -= amount);
			user.setVinTotal(currentMoney -= amount);
			user.setCashout(cashoutMoney += (int) amount);
			user.setCashoutTime(new Date());
			String desc = "Rút tiền về STK: "+model.BankAccountNumber;
//			String desc = "Ma GD: " + orderId + "(" + model.ReferenceId + ")" + ". Payment gateway "// + PAYMENTNAME
//					+ ". Paytype: Cashout. Bank: " + model.BankCode + ". Username: " + username;
			MoneyMessageInMinigame messageMoney = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), user.getId(),
					nickname, Consts.REQUEST_CASHOUT, moneyUser, currentMoney, amount, "vin", 0L, 0, 0);
			LogMoneyUserMessage messageLog = new LogMoneyUserMessage(user.getId(), nickname,
					Consts.REQUEST_CASHOUT, "Cashout", currentMoney, amount * (-1), "vin", desc, 0L,
					false, user.isBot());
			RMQApi.publishMessagePayment(messageMoney, 16);
			RMQApi.publishMessageLogMoney(messageLog);
			userMap.put(nickname, user);
			res.setCode(0);
			res.setData("");
			res.setCurrentMoney(currentMoney);
		} catch (Exception e2) {
			logger.debug(e2);
			MoneyLogger.log(username, Consts.REQUEST_CASHOUT, amount, 0L, "vin", "Yêu cầu rút tiền", "1031",
					"rmq error: " + e2.getMessage());
		} finally {
			userMap.unlock(nickname);
		}

		return res;
	}
   
    
    /**
	 * Create order into mongo db
	 * 
	 * @param userId
	 * @param username
	 * @param nickname
	 * @param amount
	 * @param bankNumber
	 * @return RechargePaywellResponse
	 */
	@Override
	public RechargePaywellResponse requestWithdrawUser(String userId, String username, String nickname, long amount,
			String bankNumber) {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
		//check user đã deposit chua
		RechargePaygateDao depositPaygate = new RechargePaygateDaoImpl();
		WithDrawPaygateDao withdrawDao = new WithDrawPaygateDaoImpl();
//		if(!depositPaygate.isExistDeposit(nickname)) {
//			//not allow
//			res.setCode(88);
//			return res;
//		}
		if (withdrawDao.countNumberWithdrawSuccessInDay(nickname) >= 5) {
			res.setCode(89);
			return res;
		}
		try {
			
			
			UserBank userBank = new UserBank();
			UserBankDao userBankDao = new UserBankDaoImpl();
			try {
				userBank = userBankDao.getByBankNumber(nickname, bankNumber);
			} catch (Exception e) {
				// TODO: handle exception
			}

			if (userBank == null) {
				res.setData("Bank number is invalid");
				return res;
			}

			HazelcastInstance client = HazelcastClientFactory.getInstance();
			if (client == null) {
				MoneyLogger.log(username, "REQUEST_WITHDRAW", amount, 0L, "vin", "Request withdraw ",
						"1031", "Cannot connect hazelcast");
				return res;
			}

			IMap<String, UserModel> userMap = client.getMap("users");
			if (!userMap.containsKey(nickname))
				return res;

			UserCacheModel user = (UserCacheModel) userMap.get(nickname);
			long moneyUser = user.getVin();
			long currentMoney = user.getVinTotal();
			if (moneyUser < amount || currentMoney < amount) {
				res.setData("Tai khoan khong du tien");
				return res;
			}
			
			
			WithDrawPaygateModel model = new WithDrawPaygateModel();
			model.Id = "";
			model.Amount = amount;
			model.BankAccountName = userBank.getCustomerName();
			model.BankAccountNumber = bankNumber;
			model.BankCode = userBank.getBankName();//bankCode;
			model.BankBranch = userBank.getBranch();
			model.CartId = "";
			model.CreatedAt = "";
			model.IsDeleted = false;
			model.PaymentType = "";
			model.MerchantCode = "";//paymentConfig.getConfig().getMerchantCode();
			model.ProviderName = "";//PAYMENTNAME;
			model.ModifiedAt = "";
			model.UserId = userId;
			model.Username = username;
			model.Nickname = nickname;
			model.ReferenceId = "";
			model.RequestTime = "";
			model.Status = PayCommon.PAYSTATUS.REQUEST.getId();
			model.UserApprove = username;
			long id = withdrawDao.Add(model);
			if (id == 0) {
				return res;
			}
			model = withdrawDao.GetById(String.valueOf(id));
			res = discountMoney(model.CartId, model.Nickname, model.Amount);
			if (res.getCode() != 0) {
				return res;
			}

			// TODO: Send message using telegram
			UserWithdraw userWithdraw = new UserWithdraw(model.Nickname,  model.Amount, model.BankAccountNumber,
					model.BankAccountName, userBank.getBankName() + " (Chi nhanh: " + userBank.getBranch() + ")");
			TelegramAlert.SendMessageCashout(userWithdraw);
			res.setCode(0);
			res.setTid(String.valueOf(id));
			return res;
		} catch (Exception e) {
			return null;
		}
	}
	
    /**
     * Create withdraw order by staff backend
     * @param orderId
     * @param channel
     * @param approvedName
     * @param ip
     * @return
     */
    @Override
    public RechargePaywellResponse withdrawal(String orderId, String channel, String approvedName, String ip) {
    	RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L,"");
    	try {
			WithDrawPaygateDao withdrawDao = new WithDrawPaygateDaoImpl();
			WithDrawPaygateModel model = withdrawDao.GetByOrderId(orderId);
			channel = PayUtils.getPayType(PaymentConstant.PayType.WITHDRAW.getKey(),
					PaymentConstant.PROVIDER.PRINCE_PAY);//712
			if(model == null) {
				res.setData("Object does not exist");
				return res;
			}
			if (PayCommon.PAYSTATUS.REQUEST.getId() != model.Status) {
				res.setData("status wrong , status = " + model.Status);
				return res;
			}
			PaymentConfigService paymentConfigService = new PaymentConfigServiceImpl();
			PaymentConfig paymentConfig = paymentConfigService.getConfigByKey(PAYMENTNAME);
			if (model.Amount < paymentConfig.getConfig().getMinMoney()) {
				res.setData("So tien rut nho hon so tien quy dinh");
				return res;
			}
			String bankName = model.BankCode;
			String bankCode = "";
			try {
				bankCode = paymentConfig.getConfig().getBanks().stream().filter(item -> item.getName().equalsIgnoreCase(bankName))
						.findFirst().orElse(null).getKey();
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (StringUtils.isBlank(bankCode)) {
				res.setData("Không có mã ngân hàng backcode phù hợp , bankName=" + bankName);
				return res;
			}
			String merchantCode = paymentConfig.getConfig().getMerchantCode();
			if (!withdrawDao.UpdateInfo(orderId, merchantCode, channel, PAYMENTNAME, bankCode, approvedName))
				return res;
			//create model
			model.MerchantCode = merchantCode;
			model.PaymentType = channel;
			model.ProviderName = PAYMENTNAME;
			model.UserApprove = approvedName;
			model.BankCode = bankCode;
			model.Status = PayCommon.PAYSTATUS.PENDING.getId();
			model.ModifiedAt = VinPlayUtils.getCurrentDateTime();
			
			return requestCreateWithDrawTransaction(model, ip);
    	}catch (Exception e) {
    		logger.error(e);
    		return res;
		}
    }


    /**
	 * Handle notify from payment gateway
	 * 
	 * @param status
	 * @param result
	 * @param sign
	 * @return RechargePaywellResponse
	 */
	@Override
	public boolean notify(int status, String result, String sign) {
		Double amount;
		String transactionId;
		String orderId = "";
		String custom = "";
		try {
			JSONObject json = new JSONObject(result);
			String amountStr =json.getString("amount");
			String realAmountStr = json.getString("real_amount");
			try {
				amount = Double.parseDouble(amountStr);
				//realAmount = Double.parseDouble(realAmountStr);
			} catch (NumberFormatException e) {
				logger.error(e);
				return false;
			}
			transactionId = json.getString("transactionid");
			orderId = json.getString("orderid");
			custom = json.getString("custom");
			// check cardId
			WithDrawPaygateDao withDrawDao = new WithDrawPaygateDaoImpl();
            WithDrawPaygateModel model = withDrawDao.GetById(orderId);
            if(model == null){
				logger.error("[NOTIFY_WITHDRAW] orderId is not exist , orderid=" + orderId);
				return false;
            }

			// check amount
			if (model.Amount != amount.longValue()) {
				logger.error("[NOTIFY_WITHDRAW]  amount request is incorrect , orderid=" + orderId);
				return false;
			}
			// check status
			if (model.Status != PayCommon.PAYSTATUS.PENDING.getId()) {
				logger.error("[NOTIFY_WITHDRAW]  status is wrong , orderid=" + orderId);
				return false;
			}
			
			String[] paramArray = { "result=" + result, "status=" + status };
			
			String signEncode =getSignal(paramArray);
			
			if (!sign.equals(signEncode)) {
				logger.error("[NOTIFY_WITHDRAW]  sign is invalid , orderid=" + orderId);
				return false;
			}

			if (status == 10000) {
				// success
				if (!withDrawDao.UpdateStatus(orderId, transactionId, PAYSTATUS.COMPLETED.getId(), model.UserApprove)) {
					logger.error("[NOTIFY_WITHDRAW] update status = COMPLETED fail, orderid=" + orderId);
					return false;
				}else {
					return true;
				}
			} else {
				if(!withDrawDao.UpdateStatus(orderId, transactionId, PAYSTATUS.FAILED.getId(), model.UserApprove)) {
					logger.error("[NOTIFY_WITHDRAW] update status = FAILED fail, orderid=" + orderId);
	            }
				//push notification Telegram
				TelegramAlert.SendMessage("[NOTIFY_WITHDRAW] NOTIFY status ="+status +" . Vui lòng kiểm tra order_id trên princepay và cộng tiền thủ công lại cho khách , order_id ="+orderId);
				return false;
			}
			
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
	}
	
//	/**
//	 * Reject withdraw
//	 * @param orderId
//	 * @param approvedName
//	 * @param reason
//	 * @return boolean
//	 */
//	@Override
//	public boolean reject(String orderId, String approvedName, String reason) {
//		try {
//			
//			// check cardId
//			WithDrawPaygateDao withDrawDao = new WithDrawPaygateDaoImpl();
//            WithDrawPaygateModel model = withDrawDao.GetById(orderId);
//            if(model == null){
//				logger.error("[NOTIFY_WITHDRAW] orderId is not exist , orderid=" + orderId);
//				return false;
//            }
//            
//            if(PAYSTATUS.getById(model.Status) != PAYSTATUS.REQUEST || PAYSTATUS.getById(model.Status) != PAYSTATUS.PENDING || PAYSTATUS.getById(model.Status) != PAYSTATUS.RECEIVED) {
//	            return false;
//            }
//            
//            if(!withDrawDao.UpdateStatus(orderId, model.ReferenceId, PAYSTATUS.FAILED.getId(), approvedName, reason)) {
//				logger.error("[NOTIFY_WITHDRAW] update status = FAILED fail, orderid=" + orderId);
//				return false;
//            }
//
//            RechargePaywellResponse res = addBackMoney(orderId, reason, model.Amount);
//            if(res.getCode() != 0) {
//    			//push notification Telegram
//    			TelegramAlert.SendMessage("[NOTIFY_WITHDRAW] NOTIFY status ="+ PAYSTATUS.FAILED.getId() +" . Vui lòng kiểm tra order_id trên princepay và cộng tiền thủ công lại cho khách , order_id ="+orderId);
//    			return false;
//            }
//			return true;
//			
//		} catch (Exception e) {
//			logger.error(e);
//			return false;
//		}
//	}

    /**
	 * Search transaction withdraw
	 * @param nickname
	 * @param status
	 * @param page
	 * @param maxItem
	 * @param fromTime
	 * @param endTime
	 * @param providerName (If want get all, set value is empty)
	 * @see RechargePaywellResponse
	 */
    @Override
    public RechargePaywellResponse findWithDraw(String nickname, int status, int page, int maxItem, String fromTime, String endTime, String providerName) {
    	RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L,"");
    	try{
    		WithDrawPaygateDao withDrawDao = new WithDrawPaygateDaoImpl();
    		WithDrawPaygateReponse withDrawPayWellReponses = withDrawDao.Find(nickname, status, page, maxItem, fromTime, endTime, providerName);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(withDrawPayWellReponses);
			res.setCode(0);
			res.setData(json.toString());
	        return res;
        }catch (Exception e){
        	logger.debug((Object)e);
            return res;
        }
    }
}

