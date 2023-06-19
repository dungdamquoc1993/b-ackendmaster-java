package com.vinplay.payment.service.impl;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.vinplay.payment.dao.impl.RechargePaygateDaoImpl;
import com.vinplay.payment.entities.Config;
import com.vinplay.payment.entities.DepositPaygateModel;
import com.vinplay.payment.entities.DepositPaygateReponse;
import com.vinplay.payment.entities.PaymentConfig;
import com.vinplay.payment.entities.PaywellNotifyRequest;
import com.vinplay.payment.service.PaymentConfigService;
import com.vinplay.payment.service.RechargePayWellService;
import com.vinplay.payment.utils.Constant;
import com.vinplay.payment.utils.PayCommon;
import com.vinplay.payment.utils.PayCommon.PAYSTATUS;
import com.vinplay.payment.utils.PayUtils;
import com.vinplay.payment.utils.PaymentConstant;
import com.vinplay.usercore.logger.MoneyLogger;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.messages.BaseMessage;
import com.vinplay.vbee.common.messages.LogMoneyUserMessage;
import com.vinplay.vbee.common.messages.MoneyMessageInMinigame;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.response.BaseResponse;
import com.vinplay.vbee.common.rmq.RMQApi;
import com.vinplay.vbee.common.statics.Consts;
import com.vinplay.vbee.common.utils.VinPlayUtils;

public class RechargePayWellServiceImpl implements RechargePayWellService {
    private static final Logger logger = Logger.getLogger("RechargePayWell");
    private static final String PAYMENTNAME = "paywell";
    private static final String USERAPPROVE = "system";
    
    private static final List<Integer> RIGHT_STATUS = Arrays.asList(new Integer[] { 0, 1, 2, 3, 4, 11 });

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
            if(nickname.isEmpty() || amount <= 0 || bankCode.isEmpty()){
                return res;
            }
            
            PaymentConfigService paymentConfigService = new PaymentConfigServiceImpl();
            PaymentConfig paymentConfig = paymentConfigService.getConfigByKey(PAYMENTNAME);
            if (amount < paymentConfig.getConfig().getMinMoney()) {
				res.setData("So tien nap nho hon so tien quy dinh");
				return res;
			}
            //get pending transaction
            RechargePaygateDao rechargeDao = new RechargePaygateDaoImpl();
            if(rechargeDao.CheckPending(nickname, PAYMENTNAME)){
            	res.setCode(Constant.ERROR_PENDING);
            	res.setData("Vui lòng đợi yêu cầu nạp tiền trước đó hoàn thành");
                return res;
            }

            DepositPaygateModel model = new DepositPaygateModel();
            model.Id = "";
            model.Amount = amount;
            model.BankAccountName = "";
            model.BankAccountNumber = "";
            model.BankCode = bankCode;
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
            model.RequestTime = "";
            model.Status = PayCommon.PAYSTATUS.PENDING.getId();
            model.UserApprove = USERAPPROVE;
            long id = rechargeDao.Add(model);
            if(id == 0){
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
            DepositPaygateModel model = rechargeDao.GetByOrderId(orderId);
            if(model == null){
                return res;
            }

            if(rechargeDao.UpdateRequestTime(orderId, requesTime, userApprove.isEmpty() ? USERAPPROVE : userApprove) == false) {
                return res;
            }
            
            res.setCode(0);
            return res;
        }catch (Exception e){
            return res;
        }
    }
    
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
	private RechargePaywellResponse requestCreateTransaction(String orderId, long amount, String bankCode,
			String payType, String customerId, String customerFullName) throws Exception {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");

		ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
		long timetick = utc.toEpochSecond();
		if (updateRequestTime(orderId, String.valueOf(timetick), USERAPPROVE).getCode() != 0) {
			res.setData("");
			return res;
		}

		PaymentConfigService paymentConfigService = new PaymentConfigServiceImpl();
		PaymentConfig paymentConfig = paymentConfigService.getConfigByKey(PAYMENTNAME);
		Config config = paymentConfig.getConfig();
		String signature_string = "merchantCode=" + config.getMerchantCode() + "&cartId=" + orderId + "&amount="
				+ amount + "&currencyCode=" + config.getCurrencyCode() + "&payType=" + payType + "&bankCode=" + bankCode
				+ "&returnUrl=" + config.getReturnUrl() + "&notifyUrl=" + config.getNotifyUrl() + "&customerId="
				+ customerId + "&requestTime=" + String.valueOf(timetick);
		String hash = PayCommon.getHMACSHA256(config.getMerchantKey(), signature_string);
		if (hash.isEmpty()) {
			res.setData("");
			return res;
		}
		// call api
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("merchantCode", config.getMerchantCode()));
		urlParameters.add(new BasicNameValuePair("cartId", orderId));
		urlParameters.add(new BasicNameValuePair("amount", String.valueOf(amount)));
		urlParameters.add(new BasicNameValuePair("currencyCode", config.getCurrencyCode()));
		urlParameters.add(new BasicNameValuePair("payType", payType));
		urlParameters.add(new BasicNameValuePair("bankCode", bankCode));
		urlParameters.add(new BasicNameValuePair("returnUrl", config.getReturnUrl()));
		urlParameters.add(new BasicNameValuePair("notifyUrl", config.getNotifyUrl()));
		urlParameters.add(new BasicNameValuePair("customerId", customerId));
		urlParameters.add(new BasicNameValuePair("customerFullName", customerFullName));
		urlParameters.add(new BasicNameValuePair("requestTime", String.valueOf(timetick)));
		urlParameters.add(new BasicNameValuePair("signature", hash));
		
		String result = PayUtils.requestAPI(config.getRequestAPI(), urlParameters);
		logger.info(PAYMENTNAME + "Response: " + result.toString());
		//{"code":1,"message":"Success","url":"https://shantitest-api.shanti.live/api/v1/payin/processdeposit?token=019de18a-944b-4c21-8bf7-b44c58c7ab7e"}
		try {
			JSONObject jsonObj = new JSONObject(result);
			res.setCode(jsonObj.getInt("code"));
			if (jsonObj.getInt("code") != 1) {
				if(jsonObj.getInt("code")==20) {
					res.setData(jsonObj.getString("message"));
					//TODO: Set status failed
					RechargePaygateDao rechargePaygateDao = new RechargePaygateDaoImpl();
					rechargePaygateDao.UpdateStatus(orderId, PayCommon.PAYSTATUS.FAILED.getId(), USERAPPROVE);
					res.setCode(PaymentConstant.TOO_MANY_REQUEST);
				}else {
					res.setData(jsonObj.getString("message"));
					//TODO: Set status failed
					RechargePaygateDao rechargePaygateDao = new RechargePaygateDaoImpl();
					rechargePaygateDao.UpdateStatus(orderId, PayCommon.PAYSTATUS.FAILED.getId(), USERAPPROVE);
					res.setCode(PaymentConstant.MAINTAINCE);
				}
				
			}else {
				res.setCode(0);
				res.setData(jsonObj.getString("url"));
			}
		} catch (Exception e) {
			res.setCode(0);
			res.setData(result);
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
	 * 
	 * @param depositPayWellModel
	 * @see RechargePaywellResponse
	 */
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
			String desc = "ONLINE".equals(depositPayWellModel.getPayTypeStr()) ? "Nạp tiền nhanh Paywell"
					: "Nạp tiền Paywell";
			MoneyMessageInMinigame messageMoney = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), user.getId(),
					depositPayWellModel.Nickname, Consts.RECHARGE_BY_PAYWELL, moneyUser, currentMoney, depositPayWellModel.Amount, "vin", 0L, 0, 0);
			LogMoneyUserMessage messageLog = new LogMoneyUserMessage(user.getId(), depositPayWellModel.Nickname, Consts.RECHARGE_BY_PAYWELL,
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
    public RechargePaywellResponse createTransaction(String userId, String username,  String nickname, String fullName, long amount, String bankCode, String payType) {
    	RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L,"");
    	try {
	    	res = createOrder(userId, username, nickname, amount, bankCode, payType);
	    	if(res.getCode() != 0)
	    		return res;
	    	
	    	long id = Long.parseLong(res.getTid());
	    	res = requestCreateTransaction(String.valueOf(id), amount, bankCode, payType, userId, fullName);
	    	//String orderId, long amount,String bankCode, String payType, String customerId, String customerFullName
	    	return res;
    	}catch (Exception e) {
    		logger.debug(e);
    		return res;
		}
    }
    
    
    /**
     * Handle call back from payment gateway
     * @param cartId
     * @param referenceId
     * @param amount
     * @param amountFee
     * @param status
     * @param requestTime
     * @param signature
     * @return
     */
    @Override
    public RechargePaywellResponse callback(String cartId, String referenceId, long amount, long amountFee, Integer status, long requestTime,  String signature) {
    	RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L,"");
    	try{
            
            //HazelcastInstance client;
            //IMap<String, UserModel> userMap;
            if(cartId.isEmpty() || referenceId.isEmpty() || amount < 0 || amountFee < 0 || signature.isEmpty()){
            	res.setData("PARRAM IS INVALID");
                return res;
            }
			Config config = getPaymentConfig().getConfig();
            RechargePaygateDao rechargeDao = new RechargePaygateDaoImpl();
            DepositPaygateModel model = rechargeDao.GetById(cartId);
            if(model == null){
                return res;
            }
            
            String signature_string = "merchantCode=" + config.getMerchantCode() + "&cartId=" + cartId + "&referenceId=" + referenceId 
            		+ "&amount=" + amount +"&amountFee=" + amountFee + "&currencyCode=" + config.getCurrencyCode()
	                + "&status=" + status + "&requestTime=" + requestTime;
			String hash = PayCommon.getHMACSHA256(config.getMerchantKey(), signature_string);
			if(hash.isEmpty()) {
				res.setData("");
				return res;
			}
			
			if(!signature.equals(hash)) {
				res.setData("Invalid signature");
				return res;
			}
			
            if(!rechargeDao.UpdateStatus(cartId, referenceId, PayCommon.PAYSTATUS.RECEIVED.getId(), USERAPPROVE)) {
            	res.setData("Update status not success");
                return res;
            }
            
            res.setCode(0);
            return res;
        }catch (Exception e){
        	logger.debug((Object)e);
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


	/**
	 * Search transaction only display for user play
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
		try{
			RechargePaygateDao dao = new RechargePaygateDaoImpl();
			data = dao.FindTransaction(nickname, status, page, maxItem, fromTime, endTime, providerName);
			return data;
		}catch (Exception e){
			logger.debug((Object)e);
			return new HashMap<>();
		}
	}

    private BaseResponse<String> validateNotify(PaywellNotifyRequest requestObj) {
    	//validation
		String cartId = requestObj.getCartId();
		Double amount = requestObj.getAmount();
		Double amountFee = requestObj.getAmountFee();
		String referenceId = requestObj.getReferenceId();
		int status =  requestObj.getStatus();
		long requestTime = requestObj.getRequestTime();
		String signature = requestObj.getSignature();
		String merchantCode = requestObj.getMerchantCode();
		String currencyCode = requestObj.getCurrencyCode();

		if (StringUtils.isBlank(cartId)) {
			return new BaseResponse<>(Constant.ERROR_PARAM, "cartId is null or empty");
		}
		if (amount <= 0) {
			return new BaseResponse<>(Constant.ERROR_PARAM, "amount < 0");
		}
		if (amountFee >= amount) {
			return new BaseResponse<>(Constant.ERROR_PARAM, "amountFee >= amount");
		}
		if (StringUtils.isBlank(referenceId)) {
			return new BaseResponse<>(Constant.ERROR_PARAM, "referenceId is null or empty");
		}
		if (StringUtils.isBlank(merchantCode)) {
			return new BaseResponse<>(Constant.ERROR_PARAM, "merchantCode is null or empty");
		}
		if (StringUtils.isBlank(currencyCode)) {
			return new BaseResponse<>(Constant.ERROR_PARAM, "currentcyCode is null or empty");
		}
		if (!RIGHT_STATUS.contains(status)) {
			return new BaseResponse<>(Constant.ERROR_PARAM, "status wrong ,status="+status);
		}
		if (requestTime <= 0) {
			return new BaseResponse<>(Constant.ERROR_PARAM, "requestTime is invalid");
		}
		if (StringUtils.isBlank(signature)) {
			return new BaseResponse<>(Constant.ERROR_PARAM, "signature is null or empty");
		}
		return new BaseResponse<String>(true, "0", "SUCCESS", null);
    }

	@Override
	public RechargePaywellResponse notification(PaywellNotifyRequest requestObj) {

		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
		int paywellStatus = requestObj.getStatus();
		if (paywellStatus == 1) {
			res.setCode(0);
			res.setData("status =1 , ignore");
			return res;
		} else {
			// validation
			BaseResponse<String> valid = validateNotify(requestObj);

			if ("0".equals(valid.getErrorCode())) {
				// success
				RechargePaygateDao rechargeDao = new RechargePaygateDaoImpl();
				try {
					// check cardId
					DepositPaygateModel model = rechargeDao.GetById(requestObj.getCartId());
					if (model == null) {
						return res;
					}
					// check amount
					if (model.Amount != requestObj.getAmount().longValue()) {
						res.setData("Amount wrong");
						return res;
					}
					// check status in my side
					if(model.Status == PayCommon.PAYSTATUS.COMPLETED.getId()) {
						//đon nay đã complete r
						res.setCode(0);
						return res;
					}
					if (model.Status != PayCommon.PAYSTATUS.PENDING.getId()
							&& model.Status != PayCommon.PAYSTATUS.RECEIVED.getId()
							&& model.Status != PayCommon.PAYSTATUS.FAILED.getId()) {
						res.setData("status wrong");
						return res;
					}
					//verify
					Config config = getPaymentConfig().getConfig();
					model.AmountFee = requestObj.getAmountFee().longValue();

					String paramArrays = "merchantCode=" + config.getMerchantCode() + "&cartId="
							+ requestObj.getCartId() + "&referenceId=" + requestObj.getReferenceId() + "&amount="
							+ model.Amount + "&amountFee=" + requestObj.getAmountFee().longValue() + "&currencyCode="
							+ requestObj.getCurrencyCode() + "&status=" + paywellStatus + "&requestTime="
							+ requestObj.getRequestTime();
					// merchantCode=shantijs&cartId=1619111616574&referenceId=GODETOXD5CYTI0&amount=200000&amountFee=5000.0&currencyCode=VND&status=2&requestTime=1619111625
					String signEncode = PayCommon.getHMACSHA256(getPaymentConfig().getConfig().getMerchantKey(),
							paramArrays);
					// String signEncode =getSignal(paramArrays);
					if (!requestObj.getSignature().equals(signEncode)) {
						res.setData("Invalid signature");
						return res;
					}
					if (paywellStatus == 3) {
						// fail
						boolean isUpdate = rechargeDao.UpdateStatus(requestObj.getCartId(), requestObj.getReferenceId(),
								PAYSTATUS.FAILED.getId(), USERAPPROVE);
						if (!isUpdate) {
							logger.error("paywell unable update status to fail");
						}
						res.setData("status updated to fail");
						return res;
					} else if (paywellStatus == 11) {
						// spam
						boolean isUpdate = rechargeDao.UpdateStatus(requestObj.getCartId(), requestObj.getReferenceId(),
								PAYSTATUS.SPAM.getId(), USERAPPROVE);
						if (!isUpdate) {
							logger.error("paywell unable update status to spam");
						}
						res.setData("status updated to spam");
						return res;
					} else if (paywellStatus == 2 || paywellStatus == 4) {
						// success
						if (!rechargeDao.UpdateStatus(requestObj.getCartId(), requestObj.getReferenceId(),
								PAYSTATUS.SUCCESS.getId(), USERAPPROVE)) {
							res.setData("UpdateStatus SUCCESS fail");
							return res;
						}

						res = addMoney(model);
						if (res.getCode() != 0) {
							res.setData("Add money fail");
							return res;
						} else {
							if (!rechargeDao.UpdateStatus(requestObj.getCartId(), requestObj.getReferenceId(),
									PAYSTATUS.COMPLETED.getId(), USERAPPROVE)) {
								res.setData("UpdateStatus COMPLETED fail");
								return res;
							}
						}
						
						res.setCode(0);
						return res;
					} else {
						res.setCode(0);
						res.setData("status =" + paywellStatus);
						return res;
					}

				} catch (Exception e) {
					logger.debug(e);
					return res;
				}
			} else {
				// fail
				res.setCode(Integer.parseInt(valid.getErrorCode()));
				res.setData(valid.getMessage());
			}
		}
		return res;
	}
}

