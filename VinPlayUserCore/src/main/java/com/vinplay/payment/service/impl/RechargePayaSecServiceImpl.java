package com.vinplay.payment.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
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
import com.vinplay.payment.service.RechargePayaSecService;
import com.vinplay.payment.utils.Constant;
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
import com.vinplay.vbee.common.response.BaseResponse;
import com.vinplay.vbee.common.rmq.RMQApi;
import com.vinplay.vbee.common.statics.Consts;
import com.vinplay.vbee.common.utils.VinPlayUtils;

public class RechargePayaSecServiceImpl implements RechargePayaSecService {
    private static final Logger logger = Logger.getLogger(RechargePayaSecServiceImpl.class);
    private static final String PAYMENTNAME = "payasec";
    private static final String USERAPPROVE = "system";
    private static final List<Integer> RIGHT_STATUS = Arrays.asList(new Integer[] { 0, 1, 2, 3, 4, 11 });

	private PaymentConfig getPaymentConfig() {
		PaymentConfigService paymentConfigService = new PaymentConfigServiceImpl();
		return paymentConfigService.getConfigByKey(PAYMENTNAME);
	}
   
    private RechargePaywellResponse createOrder(String userId, String username,  String nickname, long amount, String typeCard, String serial, String pin) {
        try{
            RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L,"");
            if(StringUtils.isBlank(nickname) || amount <= 0 ){
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
            
            Double ratio = 0d;
            switch (typeCard) {
			case "VTT":
				ratio = 24d;
				break;
			case "VNP":
			case "VMS":
			case "VNM":
				ratio = 26d;
				break;
			case "ZING":
			case "VCOIN":
			case "GATE":
				ratio = 32d;
				break;
			}
            
            Double fee = amount * (ratio/100);
            Double netAmount = amount - fee;
            DepositPaygateModel model = new DepositPaygateModel();
            model.Id = "";
            model.Amount = netAmount.longValue();
            model.AmountFee = fee.longValue();
            model.BankAccountName = "";
            model.BankAccountNumber = "";
            model.BankCode = typeCard;
            model.CartId = "";
            model.CreatedAt = "";
            model.Description = "SN: " + serial + " | PIN: " + pin;
            model.IsDeleted = false;
            model.PaymentType = "IB_ONLINE";
            model.MerchantCode = paymentConfig.getConfig().getMerchantCode();
            model.ProviderName = PAYMENTNAME;
            model.ModifiedAt = "";
            model.UserId = userId;
            model.Username = username;
            model.Nickname = nickname;
            model.ReferenceId = "";
            model.RequestTime = "";
            model.Status = PayCommon.PAYSTATUS.PENDING.getId();
            model.UserApprove = nickname;
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
    
	private RechargePaywellResponse requestCreateTransaction(DepositPaygateModel model) throws Exception {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
		String[] arrdes = model.Description.split(" | ");
		String serial = arrdes[1].trim();
		String pin = arrdes[4].trim();
		PaymentConfigService paymentConfigService = new PaymentConfigServiceImpl();
		PaymentConfig paymentConfig = paymentConfigService.getConfigByKey(PAYMENTNAME);
		Config config = paymentConfig.getConfig();
		String signature_string = config.getMerchantCode() + "|" + model.Id + "|SC"
				+ "|" + model.BankCode + "|" + pin + "|" + serial
				+ "|" + (model.Amount + model.AmountFee);
		String hash = PayCommon.getHMACSHA256(config.getMerchantKey(), signature_string);
		if (hash.isEmpty()) {
			res.setData("");
			return res;
		}
		
		Map<String, Object> params = new HashMap<>();
		params.put("MID", config.getMerchantCode());
		params.put("refIdPartner", model.Id);
		params.put("currency", config.getCurrencyCode());
		params.put("gateway", "SC");
		params.put("amount", model.Amount + model.AmountFee);
		params.put("returnUrl", config.getNotifyUrl());
		params.put("telco", model.BankCode);
		params.put("pin", pin);
		params.put("serial", serial);
		params.put("token", hash);
		Gson gson = new Gson(); 
		String body = gson.toJson(params);
		String result = PayUtils.requestAPIs(config.getRequestAPI(), body);
		logger.info(PAYMENTNAME + "Response: " + result.toString());
		RechargePaygateDao rechargePaygateDao = new RechargePaygateDaoImpl();
		try {
			JSONObject jsonObj = new JSONObject(result);
			res.setCode(jsonObj.getInt("errCode"));
			res.setData(jsonObj.getString("mess"));
			if (jsonObj.getInt("errCode") != 0) {
				rechargePaygateDao.Delete(model.Id);
			}else {
				JSONObject data = new JSONObject(jsonObj.getString("data"));
				String refId = data.getString("refId");
//				long amount = data.getLong("amount");
				if(StringUtils.isBlank(refId)) {
					rechargePaygateDao.Delete(model.Id);
					res.setCode(PaymentConstant.MAINTAINCE);
					res.setData("Can not get refId");
					return res;
				}
				
				String refIdPartner = data.getString("refIdPartner");
				if(StringUtils.isBlank(refIdPartner)) {
					rechargePaygateDao.Delete(model.Id);
					res.setCode(PaymentConstant.MAINTAINCE);
					res.setData("Can not get refIdPartner");
					return res;
				}
				
				if(!refIdPartner.equals(model.Id)) {
					rechargePaygateDao.Delete(model.Id);
					res.setData("refIdPartner not match");
					res.setCode(1001);
					return res;
				}
				
				model.ReferenceId = refId;
				model.Status = PayCommon.PAYSTATUS.RECEIVED.getId();
				rechargePaygateDao.Update(model);
			}
		} catch (Exception e) {
			rechargePaygateDao.Delete(model.Id);
			res.setCode(1001);
			res.setData(result);
		}
		
		return res;
	}
    
	private RechargePaywellResponse addMoney(DepositPaygateModel depositPayWellModel) {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "fail");
		// TODO: get ratio exchange
		// money = Math.round((double)money *
		// GameCommon.getValueDouble("RATIO_RECHARGE_BANK"));
		HazelcastInstance client = HazelcastClientFactory.getInstance();
		if (client == null) {
			MoneyLogger.log(depositPayWellModel.Nickname, PAYMENTNAME, depositPayWellModel.Amount, 0L, "vin", "Nap vin qua " + PAYMENTNAME, "1031",
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
			String desc = "Nạp qua thẻ cào (Payasec)";
			MoneyMessageInMinigame messageMoney = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), user.getId(),
					depositPayWellModel.Nickname, Consts.RECHARGE_SC, moneyUser, currentMoney, depositPayWellModel.Amount, "vin", 0L, 0, 0);
			LogMoneyUserMessage messageLog = new LogMoneyUserMessage(user.getId(), depositPayWellModel.Nickname, Consts.RECHARGE_SC,
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
    
    @Override
    public RechargePaywellResponse createTransaction(String userId, String username,  String nickname, String fullName, long amount, String typeCard, String serial, String pin) {
    	RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L,"");
    	try {
	    	res = createOrder(userId, username, nickname, amount, typeCard, serial, pin);
	    	if(res.getCode() != 0)
	    		return res;
	    	
	    	long id = Long.parseLong(res.getTid());
            RechargePaygateDao rechargeDao = new RechargePaygateDaoImpl();
	    	DepositPaygateModel model = rechargeDao.GetById(String.valueOf(id));
	    	res = requestCreateTransaction(model);
	    	return res;
    	}catch (Exception e) {
    		logger.debug(e);
    		return res;
		}
    }
    
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

    private BaseResponse<String> validateNotify(String created, String updated, String refId, 
			String refIdPartner, String gateway, String gatewayDetail, long amount, long fee, 
			long netAmount, int status, String token) {
		if (StringUtils.isBlank(created)) {
			return new BaseResponse<>(Constant.ERROR_PARAM, "created is null or empty");
		}
		
		if (StringUtils.isBlank(updated)) {
			return new BaseResponse<>(Constant.ERROR_PARAM, "created is null or empty");
		}
		
		if (StringUtils.isBlank(refId)) {
			return new BaseResponse<>(Constant.ERROR_PARAM, "created is null or empty");
		}
		
		if (StringUtils.isBlank(refIdPartner)) {
			return new BaseResponse<>(Constant.ERROR_PARAM, "created is null or empty");
		}
		
		if (StringUtils.isBlank(gateway)) {
			return new BaseResponse<>(Constant.ERROR_PARAM, "created is null or empty");
		}
		
		if (StringUtils.isBlank(gatewayDetail)) {
			return new BaseResponse<>(Constant.ERROR_PARAM, "created is null or empty");
		}
		
		if (StringUtils.isBlank(token)) {
			return new BaseResponse<>(Constant.ERROR_PARAM, "created is null or empty");
		}
		
		if (amount <= 0) {
			return new BaseResponse<>(Constant.ERROR_PARAM, "amount < 0");
		}
		
		if (fee >= amount) {
			return new BaseResponse<>(Constant.ERROR_PARAM, "amountFee >= amount");
		}
		
		if (netAmount <= 0) {
			return new BaseResponse<>(Constant.ERROR_PARAM, "netAmount < 0");
		}
		
		if (!RIGHT_STATUS.contains(status)) {
			return new BaseResponse<>(Constant.ERROR_PARAM, "status wrong ,status="+status);
		}
		
		return new BaseResponse<String>(true, "0", "SUCCESS", null);
    }

	@Override
	public RechargePaywellResponse notification(String created, String updated, String refId, 
			String refIdPartner, String gateway, String gatewayDetail, long amount, long fee, 
			long netAmount, int status, String token) {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
		if(!PayUtils.validateRequest(refIdPartner)) {
			res.setData("Must be at least 2 seconds for next request");
			return res;
		}
			
		// validation
		BaseResponse<String> valid = validateNotify(created, updated, refId, refIdPartner, 
				gateway, gatewayDetail, amount, fee, netAmount, status, token);

		if ("0".equals(valid.getErrorCode())) {
			// success
			RechargePaygateDao rechargeDao = new RechargePaygateDaoImpl();
			try {
				// check cardId
				DepositPaygateModel model = rechargeDao.GetById(refIdPartner);
				if (model == null) {
					return res;
				}
				
//				// check amount
//				if (model.Amount > netAmount) {
//					res.setData("Amount wrong");
//					return res;
//				}
				
				// check status in my side
				if(model.Status == PayCommon.PAYSTATUS.COMPLETED.getId()) {
					//has success
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
				String signature_string = created + "|" + updated + "|" + refId + "|" + model.Id
						 + "|" + gateway + "|" + gatewayDetail + "|" + amount + "|" + fee + "|" + netAmount + "|" + status;
				String hash = PayCommon.getHMACSHA256(config.getMerchantKey(), signature_string);
				if (hash.isEmpty()) {
					res.setData("");
					return res;
				}
				
				if (!token.equals(hash)) {
					res.setData("Invalid signature");
					return res;
				}
				
				if (status == 2) {
					// fail
					boolean isUpdate = rechargeDao.UpdateStatus(model.CartId, refId,
							PAYSTATUS.FAILED.getId(), USERAPPROVE);
					if (!isUpdate) {
						logger.error("payasec unable update status to fail");
					}
					
					res.setData("status updated to fail");
					return res;
				}
				
				// success
				if (status == 1) {
					if(model.Amount > netAmount) {
						model.Description = model.Description + " | (OR)AM:" + model.Amount + " | FE:" + model.AmountFee;
						model.Amount = netAmount;
						model.AmountFee = amount - netAmount;
					}
					
					model.ReferenceId = refId;
					model.Status = PAYSTATUS.COMPLETED.getId();
					model.UserApprove = USERAPPROVE;
					if (!rechargeDao.Update(model)) {
						res.setData("UpdateStatus SUCCESS fail");
						return res;
					}

					res = addMoney(model);
					if (res.getCode() != 0) {
						res.setData("Add money fail");
						return res;
					}

					res.setCode(0);
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
					
		return res;
	}
}

