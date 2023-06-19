package com.vinplay.payment.service.impl;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.vinplay.payment.dao.impl.RechargePaygateDaoImpl;
import com.vinplay.payment.entities.Config;
import com.vinplay.payment.entities.DepositPaygateModel;
import com.vinplay.payment.entities.DepositPaygateReponse;
import com.vinplay.payment.entities.PaymentConfig;
import com.vinplay.payment.service.PaymentConfigService;
import com.vinplay.payment.service.RechargePrincePayService;
import com.vinplay.payment.utils.PayCommon;
import com.vinplay.payment.utils.PayCommon.PAYSTATUS;
import com.vinplay.payment.utils.PayUtils;
import com.vinplay.payment.utils.PaymentConstant;
import com.vinplay.usercore.logger.MoneyLogger;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.messages.LogMoneyUserMessage;
import com.vinplay.vbee.common.messages.MoneyMessageInMinigame;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.rmq.RMQApi;
import com.vinplay.vbee.common.statics.Consts;
import com.vinplay.vbee.common.utils.VinPlayUtils;

public class RechargePrincePayServiceImpl
implements RechargePrincePayService {
    private static final Logger logger = Logger.getLogger(RechargePrincePayServiceImpl.class);
    private static final String PAYMENTNAME = "princepay";
    private static final String USERAPPROVE = "system";
    
	private PaymentConfig getPaymentConfig() {
		PaymentConfigService paymentConfigService = new PaymentConfigServiceImpl();
		return paymentConfigService.getConfigByKey(PAYMENTNAME);
	}
    
	/**
     * Create order into mongo db
     * @param userId
     * @param username
     * @param nickname
     * @param amount
     * @param bankCode
     * @param paymentType
     * @param providerName
     * @see RechargePaywellResponse
     */
    private RechargePaywellResponse createOrder(String userId, String username,  String nickname, long amount, String bankCode, String paymentType) {
        try{
            RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L,"");
            //HazelcastInstance client;
            //IMap<String, UserModel> userMap;
            if(nickname.isEmpty() || amount <= 0){
                return res;
            }
            
            PaymentConfigService paymentConfigService = new PaymentConfigServiceImpl();
            PaymentConfig paymentConfig = paymentConfigService.getConfigByKey(PAYMENTNAME);
            if(paymentType.equals("923")) {
            	if (amount < 20000) {
    				res.setData("Số tiền nạp nhỏ hơn 20.000 VNĐ");
    				return res;
    			}
            	if (amount > 20000000) {
    				res.setData("Số tiền nạp lớn hơn 20.000.000 VNĐ");
    				return res;
    			}
            }else {
            	if (amount < paymentConfig.getConfig().getMinMoney()) {
    				res.setData("So tien nap nho hon so tien quy dinh");
    				return res;
    			}
			}
            
            //get pending transaction
            RechargePaygateDao rechargeDao = new RechargePaygateDaoImpl();
            if(rechargeDao.CheckPending(nickname, PAYMENTNAME)){
            	res.setCode(Consts.IN_COMPLETE);
            	res.setData("Vui lòng hoàn thành đơn nạp tiền trước đó");
                return res;
            }

            DepositPaygateModel model = new DepositPaygateModel();
            model.Id = "";
            model.Amount = amount;
            model.BankAccountName = "";
            model.BankAccountNumber = "";
            model.BankCode = "";
            model.CartId = "";
            model.CreatedAt = "";
            model.Description = "";
            model.IsDeleted = false;
            model.PaymentType = paymentType;
            model.MerchantCode = paymentConfig.getConfig().getMerchantCode();
            model.ProviderName = PAYMENTNAME;
            model.ModifiedAt = "";
            model.UserId = userId;
            model.Username = username;
            model.Nickname = nickname;
            model.ReferenceId = "";
            model.RequestTime = VinPlayUtils.getCurrentDateTime();
            model.Status = PayCommon.PAYSTATUS.PENDING.getId();
            model.UserApprove = USERAPPROVE;
            long id = rechargeDao.Add(model);
            if(id == 0){
            	logger.error("Lỗi tạo bản ghi database mongo");
                return res;
            }
            res.setCode(0);
            res.setTid(String.valueOf(id));
            return res;
        }catch (Exception e){
            return null;
        }
    }
    
    /**
     * Update request time by orderId
     * @param orderId
     * @param requesTime
     * @param userApprove
     * @see RechargePaywellResponse
     */
    private RechargePaywellResponse updateRequestTime(String orderId, String requesTime, String userApprove) {
    	RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L,"");
    	try{
            
            //HazelcastInstance client;
            //IMap<String, UserModel> userMap;
            if(orderId.isEmpty() || requesTime.isEmpty() || userApprove.isEmpty()){
                return res;
            }

            //get pending transaction
            RechargePaygateDao rechargeDao = new RechargePaygateDaoImpl();
            DepositPaygateModel model = rechargeDao.GetById(orderId);
            if(model == null){
                return res;
            }

            if(rechargeDao.UpdateRequestTime(orderId, requesTime, USERAPPROVE) == false) {
                return res;
            }
            
            res.setCode(0);
            return res;
        }catch (Exception e){
            return res;
        }
    }

    private String getSignal(String []signArray) {
    	Arrays.sort(signArray);
    	StringBuilder resu =new StringBuilder();
		for (String s : signArray) {
			resu.append(s).append("&");
		}
		resu.append("key=").append(getPaymentConfig().getConfig().getMerchantKey());
		return PayCommon.getMd5(resu.toString()).toUpperCase();
    }
    
	private RechargePaywellResponse requestCreateTransaction(String orderId, long amount, String channel,
			String customerName, String ip) throws Exception {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
		ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
		long timetick = utc.toEpochSecond();
//		if (updateRequestTime(orderId, timetick+"", "superadmin").getCode() != 0) {
//			res.setData("");
//			return res;
//		}

		Config config = this.getPaymentConfig().getConfig();
		String []paramArray = {"uid="+config.getMerchantCode(),
				"amount="+amount,
				"orderid="+orderId,
				"channel="+channel,
				"notify_url="+config.getNotifyUrl(),
				"return_url="+config.getReturnUrl(),
				"userip="+ip,
				"timestamp="+timetick,
				"custom="+customerName};
		String sign =getSignal(paramArray);
		if (sign.isEmpty()) {
			res.setData("sign is empty");
			return res;
		}
		// call api		//amount=200000","uid=roy88","orderid=roy88","channel=908","notify_url=https://roy88.vip",
		//"return_url=https://roy88.vip","userip=127.0.0.1","timestamp=1608823688","custom=nguyen"
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("uid", config.getMerchantCode()));
		urlParameters.add(new BasicNameValuePair("orderid", orderId));
		urlParameters.add(new BasicNameValuePair("amount", String.valueOf(amount)));
		urlParameters.add(new BasicNameValuePair("channel", channel));
		urlParameters.add(new BasicNameValuePair("notify_url", config.getNotifyUrl()));
		urlParameters.add(new BasicNameValuePair("return_url", config.getReturnUrl()));
		urlParameters.add(new BasicNameValuePair("userip", ip));
		urlParameters.add(new BasicNameValuePair("timestamp", timetick+""));
		urlParameters.add(new BasicNameValuePair("custom", customerName));
		urlParameters.add(new BasicNameValuePair("sign", sign));
		
		String result = PayUtils.requestAPI(config.getRequestAPI(), urlParameters);
		logger.info(PAYMENTNAME + "Response: " + result.toString());
		try {
			JSONObject jsonObj = new JSONObject(result);
			res.setCode(jsonObj.getInt("status"));
			if (jsonObj.getInt("status") != 10000) {
				//error
				logger.error("CreateTransaction PRINCEPAY" + jsonObj.toString());
				res.setData(jsonObj.getString("sign"));
				RechargePaygateDao rechargePaygateDao = new RechargePaygateDaoImpl();
				rechargePaygateDao.UpdateStatus(orderId, PayCommon.PAYSTATUS.FAILED.getId(), USERAPPROVE);
				res.setCode(PaymentConstant.MAINTAINCE);
			}else {
				res.setCode(PaymentConstant.SUCCESS);
				res.setData(jsonObj.getString("result"));
			}
		} catch (Exception e) {
			logger.error(PAYMENTNAME + "Response: " + result.toString());
		}
		
		return res;
	}
    
    /**
     * Check status transaction on payment gateway
     * @param orderId
     * @see RechargePaywellResponse
     * @throws Exception
     */
    private RechargePaywellResponse requestCheckStatusTransaction(String orderId) throws Exception {
    	RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L,"");
    	try {
	    	ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
	        long timetick = utc.toEpochSecond();
	        if(updateRequestTime(orderId, String.valueOf(timetick), "superadmin").getCode() != 0) { 
	    		res.setData("");
	    		return res;
	        }
	        
	        PaymentConfigService paymentConfigService = new PaymentConfigServiceImpl();
            PaymentConfig paymentConfig = paymentConfigService.getConfigByKey(PAYMENTNAME);
            Config config = paymentConfig.getConfig();
	        String signature_string = "merchantCode=" + config.getMerchantCode() + "&cartId=" + orderId + "&requestTime=" + String.valueOf(timetick);
	        String hash = PayCommon.getHMACSHA256(config.getMerchantKey(), signature_string);
	        if(hash.isEmpty()) {
	    		res.setData("");
	    		return res;
	        }
	        
	        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
	        urlParameters.add(new BasicNameValuePair("merchantCode", config.getMerchantCode()));
	        urlParameters.add(new BasicNameValuePair("cartId", orderId));
	        urlParameters.add(new BasicNameValuePair("requestTime", String.valueOf(timetick)));
	        urlParameters.add(new BasicNameValuePair("signature", hash));
	        
			String result = PayUtils.requestAPI(config.getStatusAPI(), urlParameters);
			logger.debug((PAYMENTNAME + " Response: " + result.toString()));
			try {
				JSONObject jsonObj = new JSONObject(result.toString());
				res.setCode(jsonObj.getInt("code"));
				res.setData(jsonObj.getString("message"));
				Integer code = jsonObj.getInt("code");
				String status = jsonObj.getString("status");
				if(code == 1) {
					res.setCode(0);
					ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
					String data = ow.writeValueAsString(PAYSTATUS.getByKey(status));
					res.setData(data);
				}
				return res;
			} catch (Exception e) { }
	        return res;
    	}catch (Exception e) {
    		logger.debug(e);
    		return res;
		}
    }
    
    /**
     * Add money customer
     * @param orderId
     * @param transId
     * @param nickName
     * @param amount
     * @see RechargePaywellResponse
     */
    private RechargePaywellResponse addMoney(String orderId, String transId, String nickName, long amount) {
    	RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L,"fail");
        //TODO: get ratio exchange
        //money = Math.round((double)money * GameCommon.getValueDouble("RATIO_RECHARGE_BANK"));
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        if (client == null) {
        	MoneyLogger.log(nickName, PAYMENTNAME, amount, 0L, "vin", "Nap princepay qua ngan hang", "1031", "Cannot connect hazelcast");
        	return res;
        }
        
        IMap<String, UserModel> userMap = client.getMap("users");
        if (!userMap.containsKey(nickName))
        	return res;
        try {
            userMap.lock(nickName);
            RechargePaygateDao rechargeDao = new RechargePaygateDaoImpl();
            DepositPaygateModel depositPayGateModel = rechargeDao.GetById(orderId);
            if(depositPayGateModel == null)
            	return res;
            
            UserCacheModel user = (UserCacheModel)userMap.get(nickName);
            long moneyUser = user.getVin();
            long currentMoney = user.getVinTotal();
            long rechargeMoney = user.getRechargeMoney();
            user.setVin(moneyUser += amount);
            user.setVinTotal(currentMoney += amount);
            user.setRechargeMoney(rechargeMoney += amount);
            String desc = "ONLINE".equals(depositPayGateModel.getPayTypeStr()) ? "Nạp tiền nhanh Princepay"
					: "Nạp tiền Princepay";
            MoneyMessageInMinigame messageMoney = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), user.getId(), nickName, Consts.RECHARGE_BY_PRINCEPAY, moneyUser, currentMoney, amount, "vin", 0L, 0, 0);
            LogMoneyUserMessage messageLog = new LogMoneyUserMessage(user.getId(), nickName, Consts.RECHARGE_BY_PRINCEPAY, PAYMENTNAME, currentMoney, amount, "vin", desc, 0L, false, user.isBot());
            RMQApi.publishMessagePayment(messageMoney, 16);
            RMQApi.publishMessageLogMoney(messageLog);
            userMap.put(nickName, user);
            res.setCode(0);
            res.setData("");
        }
        catch (Exception e2) {
            logger.debug(e2);
            MoneyLogger.log(nickName, PAYMENTNAME, amount, 0L, "vin", "Nap vin qua " + PAYMENTNAME, "1031", "rmq error: " + e2.getMessage());
        }
        finally {
            userMap.unlock(nickName);
        }
    	
    	return res;
    }

    /**
     * Create transaction pay well
     * @param userId
     * @param username
     * @param nickname
     * @param fullName
     * @param amount
     * @param bankCode
     * @param paymentType
     * @see RechargePaywellResponse
     */
    @Override
    public RechargePaywellResponse createTransaction(String userId, String username, String nickname, long amount,
			String channel, String customerName,String bankCode, String ip) {
    	RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L,"");
    	try {
	    	res = createOrder(userId, username, nickname, amount, bankCode, channel);
	    	if(res.getCode() != 0)
	    		return res;
	    	
	    	long id = Long.parseLong(res.getTid());
			res = requestCreateTransaction(id + "", amount, channel, customerName, ip);
	    	//String orderId, long amount,String bankCode, String payType, String customerId, String customerFullName
	    	return res;
    	}catch (Exception e) {
    		logger.debug(e);
    		return res;
		}
    }
    
    
    
    /**
     * Handle notify from payment gateway
     * @param cartId
     * @param referenceId
     * @param amount
     * @param amountFee
     * @param status
     * @param requestTime
     * @param signature
     * @see RechargePaywellResponse
     */
    @Override
    public RechargePaywellResponse notify(int status ,String result ,String signature) {
    	RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L,"");
		Double amount;
    	String transactionId;  
    	String orderId ="";
    	 RechargePaygateDao rechargeDao = new RechargePaygateDaoImpl();
    	try{
    		JSONObject json = new JSONObject(result);
			String amountStr =json.getString("amount");
			//String realAmountStr = json.getString("real_amount");
			try {
				amount = Double.parseDouble(amountStr);
				//realAmount = Double.parseDouble(realAmountStr);
			} catch (NumberFormatException e) {
				logger.error(e);
				return res;
			}
			transactionId = json.getString("transactionid");
			orderId = json.getString("orderid");
            //check cardId
            DepositPaygateModel model = rechargeDao.GetById(orderId);
            if(model == null){
                return res;
            }
            
            //check amount
			if (model.Amount != amount.longValue()) {
				res.setData("Amount wrong ,amount = "+model.Amount);
				return res;
			}
			//check status
			if (model.Status != PayCommon.PAYSTATUS.PENDING.getId() && model.Status != PayCommon.PAYSTATUS.RECEIVED.getId()) {
				res.setData("status wrong , status = "+model.Status);
				return res;
			}
			
			String[] paramArray = { "result=" + result, "status=" + status };
			
			String signEncode =getSignal(paramArray);
			
			if(!signature.equals(signEncode)) {
				res.setData("Invalid signature");
				return res;
			}
			if (status == 10000) {
				if (!rechargeDao.UpdateStatus(orderId, transactionId, PAYSTATUS.SUCCESS.getId(), USERAPPROVE)) {
					res.setData("UpdateStatus SUCCESS fail");
					return res;
				}

				res = addMoney(orderId, transactionId, model.Nickname, model.Amount);
				if (res.getCode() != 0) {
					res.setData("Add money fail");
					return res;
				} else {
					if (!rechargeDao.UpdateStatus(orderId, transactionId, PAYSTATUS.COMPLETED.getId(), USERAPPROVE)) {
						res.setData("UpdateStatus COMPLETED fail");
						return res;
					}
				}
			}else {
				if(!rechargeDao.UpdateStatus(orderId, transactionId, PAYSTATUS.FAILED.getId(), USERAPPROVE)) {
					res.setData("UpdateStatus FAILED fail");
					return res;
	            }
			}
            res.setCode(0);
            return res;
        }catch (Exception e){
        	logger.debug(e);
            return res;
        }
    }
    
    /**
     * Check status transaction
     * @param cartId
     * @see RechargePaywellResponse
     */
    @Override
    public RechargePaywellResponse checkStatusTrans(String cartId) {
    	RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L,"");
    	try{
            if(cartId.isEmpty()){
                res.setData("Invalid parram(s)");
                return res;
            }

            RechargePaygateDao rechargeDao = new RechargePaygateDaoImpl();
            DepositPaygateModel model = rechargeDao.GetById(cartId);
            if(model == null){
                return res;
            }
            
            return requestCheckStatusTransaction(cartId);
        }catch (Exception e){
        	logger.debug(e);
            return res;
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
	 * @see RechargePaywellResponse
	 */
    @Override
    public RechargePaywellResponse find(String nickname, int status, int page, int maxItem, String fromTime, String endTime, String providerName) {
    	RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L,"");
    	try{
    		RechargePaygateDao rechargeDao = new RechargePaygateDaoImpl();
    		DepositPaygateReponse depositPayWellReponses = rechargeDao.Find(nickname, status, page, maxItem, fromTime, endTime, providerName);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(depositPayWellReponses);
			res.setCode(0);
			res.setData(json.toString());
	        return res;
        }catch (Exception e){
        	logger.debug((Object)e);
            return res;
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
	 * @see RechargePaywellResponse
	 */
    @Override
    public DepositPaygateReponse search(String nickname, int status, int page, int maxItem, String fromTime, String endTime, String providerName) {
    	DepositPaygateReponse depositPayWellReponses = new DepositPaygateReponse(0, 0, 0, new ArrayList<DepositPaygateModel>());
    	try{
    		RechargePaygateDao rechargeDao = new RechargePaygateDaoImpl();
            depositPayWellReponses = rechargeDao.Find(nickname, status, page, maxItem, fromTime, endTime, providerName);
	        return depositPayWellReponses;
        }catch (Exception e){
        	logger.debug((Object)e);
            return depositPayWellReponses;
        }
    }
    public static void main(String[] args) {
    	String [] xxx= {"amount=200000","uid=roy88","orderid=12280869","channel=908","notify_url=https://roy88.vip",
    			"return_url=https://roy88.vip","userip=127.0.0.1","timestamp=1608823688","custom=nguyenz"};//,"key=IOXI4or5xIywV0hL"
    	Arrays.sort(xxx);
    	String t="";
    	for (String string : xxx) {
    		t+=string+"&";
		}
    	System.out.println(t);
	}
}

