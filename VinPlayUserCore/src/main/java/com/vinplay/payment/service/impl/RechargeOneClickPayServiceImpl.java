package com.vinplay.payment.service.impl;

import java.lang.reflect.Type;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.dichvuthe.response.RechargePaywellResponse;
import com.vinplay.payment.dao.RechargePaygateDao;
import com.vinplay.payment.dao.impl.RechargePaygateDaoImpl;
import com.vinplay.payment.entities.Bank;
import com.vinplay.payment.entities.BankOneClick;
import com.vinplay.payment.entities.Config;
import com.vinplay.payment.entities.DepositPaygateModel;
import com.vinplay.payment.entities.DepositPaygateReponse;
import com.vinplay.payment.entities.PaymentConfig;
import com.vinplay.payment.service.PaymentConfigService;
import com.vinplay.payment.service.RechargeOneClickPayService;
import com.vinplay.payment.utils.Constant;
import com.vinplay.payment.utils.PayCommon;
import com.vinplay.payment.utils.PayCommon.PAYSTATUS;
import com.vinplay.payment.utils.PayUtils;
import com.vinplay.payment.utils.PaymentConstant;
import com.vinplay.usercore.logger.MoneyLogger;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.messages.BaseMessage;
import com.vinplay.vbee.common.messages.LogMoneyUserMessage;
import com.vinplay.vbee.common.messages.MoneyMessageInMinigame;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.rmq.RMQApi;
import com.vinplay.vbee.common.statics.Consts;
import com.vinplay.vbee.common.utils.VinPlayUtils;

public class RechargeOneClickPayServiceImpl implements RechargeOneClickPayService {
	private static final Logger logger = Logger.getLogger(RechargeOneClickPayServiceImpl.class);
	private static final String PAYMENTNAME = "clickpay";
	private static final String USERAPPROVE = "system";

	private PaymentConfig paymentConfig = null;

	public RechargeOneClickPayServiceImpl() {
		initConfig();
	}

	private void initConfig() {
		this.paymentConfig = new PaymentConfig();
		this.paymentConfig = getPaymentConfig();
	}

	private PaymentConfig getPaymentConfig() {
		PaymentConfigService paymentConfigService = new PaymentConfigServiceImpl();
		return paymentConfigService.getConfigByKey(PAYMENTNAME);
	}

	/**
	 * Create order into mongo db
	 * 
	 * @param userId
	 * @param username
	 * @param nickname
	 * @param amount
	 * @param bankCode
	 * @param paymentType
	 * @param providerName
	 * @see RechargePaywellResponse
	 */
	private RechargePaywellResponse createOrder(String userId, String username, String nickname, long amount,
			String bankCode, String paymentType) {
		try {
			RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
			// HazelcastInstance client;
			// IMap<String, UserModel> userMap;
			if (nickname.isEmpty() || amount <= 0) {
				return res;
			}

			PaymentConfigService paymentConfigService = new PaymentConfigServiceImpl();
			PaymentConfig paymentConfig = paymentConfigService.getConfigByKey(PAYMENTNAME);
			// get pending transaction
			RechargePaygateDao rechargeDao = new RechargePaygateDaoImpl();

			if (rechargeDao.CheckPending(nickname, PAYMENTNAME)) {
				res.setCode(Constant.ERROR_PENDING);
				res.setData("Vui lòng đợi yêu cầu nạp tiền trước đó hoàn thành");
				return res;
			}
			if (amount < paymentConfig.getConfig().getMinMoney()) {
				res.setData("So tien nap nho hon so tien quy dinh");
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
			model.RequestTime = VinPlayUtils.getCurrentDateTime();
			model.Status = PayCommon.PAYSTATUS.PENDING.getId();
			model.UserApprove = USERAPPROVE;
			long id = rechargeDao.Add(model);
			if (id == 0) {
				return res;
			}
			res.setCode(0);
			res.setTid(String.valueOf(id));
			return res;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Update request time by orderId
	 * 
	 * @param orderId
	 * @param requesTime
	 * @param userApprove
	 * @see RechargePaywellResponse
	 */
	private RechargePaywellResponse updateRequestTime(String orderId, String requesTime, String userApprove) {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
		try {

			// HazelcastInstance client;
			// IMap<String, UserModel> userMap;
			if (orderId.isEmpty() || requesTime.isEmpty() || userApprove.isEmpty()) {
				return res;
			}

			// get pending transaction
			RechargePaygateDao rechargeDao = new RechargePaygateDaoImpl();
			DepositPaygateModel model = rechargeDao.GetById(orderId);
			if (model == null) {
				return res;
			}

			if (rechargeDao.UpdateRequestTime(orderId, requesTime, USERAPPROVE) == false) {
				return res;
			}

			res.setCode(0);
			return res;
		} catch (Exception e) {
			return res;
		}
	}

	private String getSignal(String[] signArray) {
		if (paymentConfig == null) {
			paymentConfig = getPaymentConfig();
		}
		// Arrays.sort(signArray);
		StringBuilder resu = new StringBuilder();
		for (String s : signArray) {
			resu.append(s).append("");
		}
		resu.append(paymentConfig.getConfig().getMerchantKey());
		return PayCommon.getMd5(resu.toString());
	}

	private RechargePaywellResponse requestCreateTransaction(String orderId, long amount, String channel,
			String customerName, String ip, String bankCode) throws Exception {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
		ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
		long timetick = utc.toEpochSecond();
		if (updateRequestTime(orderId, String.valueOf(timetick), "superadmin").getCode() != 0) {
			res.setData("");
			return res;
		}

		if (paymentConfig == null) {
			paymentConfig = getPaymentConfig();
		}
		Config config = this.paymentConfig.getConfig();
		String[] paramArray = { config.getMerchantCode(), orderId, config.getMerchantCode(), String.valueOf(amount),
				bankCode };
		String sign = getSignal(paramArray);
		if (sign.isEmpty()) {
			res.setData("");
			return res;
		}
		// call api
		// //amount=200000","merchant_id=roy88","merchant_txn=loteas2808199","url_success=https://roy88.vip",
		// "bank_code=VCB","url_error=https://roy88.vip","merchant_customer=nguyen"
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("merchant_id", config.getMerchantCode()));
		urlParameters.add(new BasicNameValuePair("merchant_txn", orderId));
		urlParameters.add(new BasicNameValuePair("merchant_customer", config.getMerchantCode()));
		urlParameters.add(new BasicNameValuePair("amount", String.valueOf(amount)));
		urlParameters.add(new BasicNameValuePair("bank_code", bankCode));
		urlParameters.add(new BasicNameValuePair("url_success", config.getNotifyUrl()));
		urlParameters.add(new BasicNameValuePair("url_error", config.getNotifyUrl()));
		urlParameters.add(new BasicNameValuePair("description", "Nap tien online"));
		urlParameters.add(new BasicNameValuePair("sign", sign));
		String result = PayUtils.requestAPI(config.getRequestAPI(), urlParameters);
		logger.info(PAYMENTNAME + "Response: " + result.toString());
		JSONObject jsonObj = new JSONObject(result);
		res.setCode(jsonObj.getInt("code"));
		if (jsonObj.getInt("code") != 0) {
			// error
			res.setData(jsonObj.getString("message"));
			RechargePaygateDao rechargePaygateDao = new RechargePaygateDaoImpl();
			rechargePaygateDao.UpdateStatus(orderId, PayCommon.PAYSTATUS.FAILED.getId(), USERAPPROVE);
			res.setCode(PaymentConstant.MAINTAINCE);
		} else {
			res.setData(jsonObj.getString("data"));
		}

		return res;
	}

	/**
	 * Check status transaction on payment gateway
	 * 
	 * @param orderId
	 * @see RechargePaywellResponse
	 * @throws Exception
	 */
	private RechargePaywellResponse requestCheckStatusTransaction(String orderId) throws Exception {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
		try {
			PaymentConfigService paymentConfigService = new PaymentConfigServiceImpl();
			PaymentConfig paymentConfig = paymentConfigService.getConfigByKey(PAYMENTNAME);
			Config config = paymentConfig.getConfig();
			String[] paramArray = { config.getMerchantCode(), orderId };
			String sign = getSignal(paramArray);
			if (sign.isEmpty()) {
				res.setData("");
				return res;
			}

			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("merchant_id", config.getMerchantCode()));
			urlParameters.add(new BasicNameValuePair("mer_txn", orderId));
			urlParameters.add(new BasicNameValuePair("sign", sign));
			String result = PayUtils.requestAPI(config.getStatusAPI(), urlParameters);
			logger.debug((PAYMENTNAME + " Response: " + result.toString()));
			JSONObject jsonObj = new JSONObject(result.toString());
			res.setCode(jsonObj.getInt("code"));
			Integer code = jsonObj.getInt("code");
			if (code == 0) {
				res.setCode(code);
				String data = jsonObj.getString("data");
				res.setData(data);
				return res;
			}

			return res;
		} catch (Exception e) {
			logger.debug(e);
			return res;
		}
	}

	/**
	 * Get list bank support
	 * 
	 * @see RechargePaywellResponse
	 * @throws Exception
	 */
	private RechargePaywellResponse requestBanks() throws Exception {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
		try {
			PaymentConfigService paymentConfigService = new PaymentConfigServiceImpl();
			PaymentConfig paymentConfig = paymentConfigService.getConfigByKey(PAYMENTNAME);
			Config config = paymentConfig.getConfig();
			String[] paramArray = { config.getMerchantCode() };
			String sign = getSignal(paramArray);
			if (sign.isEmpty()) {
				res.setData("");
				return res;
			}

			String url = config.getRequestAPI().replace("request", "listBank");
			url = url + "?merchant_id=" + config.getMerchantCode() + "&sign=" + sign;
			String result = PayUtils.requestGetAPI(url);
//			logger.debug(PAYMENTNAME + " Response: " + result.toString());
			JSONObject jsonObj = new JSONObject(result.toString());
			res.setCode(jsonObj.getInt("code"));
			Integer code = jsonObj.getInt("code");
			if (code == 0) {
				res.setCode(code);
				String data = jsonObj.getString("data");
				res.setData(data);
				return res;
			}

			return res;
		} catch (Exception e) {
			logger.debug(e);
			return res;
		}
	}

	/**
	 * Add money customer
	 * 
	 * @param depositOneClick
	 * @see RechargePaywellResponse
	 */
	private String jsonDesc(String desc) {
		
		return desc;
	}
	private RechargePaywellResponse addMoney(DepositPaygateModel depositOneClick) {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "fail");
		long money = depositOneClick.Amount;
		HazelcastInstance client = HazelcastClientFactory.getInstance();
		if (client == null) {
			MoneyLogger.log(depositOneClick.Nickname, PAYMENTNAME, money, 0L, "vin", "Nap qua ngan hang", "1031",
					"Cannot connect hazelcast");
			return res;
		}

		IMap<String, UserModel> userMap = client.getMap("users");
		if (!userMap.containsKey(depositOneClick.Nickname))
			return res;
		try {
			userMap.lock(depositOneClick.Nickname);
			UserCacheModel user = (UserCacheModel) userMap.get(depositOneClick.Nickname);
			long moneyUser = user.getVin();
			long currentMoney = user.getVinTotal();
			long rechargeMoney = user.getRechargeMoney();
			user.setVin(moneyUser += money);
			user.setVinTotal(currentMoney += money);
			user.setRechargeMoney(rechargeMoney += money);
			String desc = "ONLINE".equals(depositOneClick.getPayTypeStr()) ? "Nạp tiền nhanh OneClickPay"
					: "Nạp tiền OneClickPay";
			MoneyMessageInMinigame messageMoney = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), user.getId(),
					depositOneClick.Nickname, Consts.RECHARGE_BY_CLICKPAY, moneyUser, currentMoney, money, "vin", 0L, 0,
					0);
			LogMoneyUserMessage messageLog = new LogMoneyUserMessage(user.getId(), depositOneClick.Nickname,
					Consts.RECHARGE_BY_CLICKPAY, "Nap qua " + PAYMENTNAME, currentMoney, money, "vin", desc, 0L, false,
					user.isBot());
			//add or minus money
			RMQApi.publishMessagePayment(messageMoney, 16);
			//log money
			RMQApi.publishMessageLogMoney(messageLog);//insert into log_money_user_vin  and log_money_user_nap_vin (extra >0) and push cached cacheTransaction
			userMap.put(depositOneClick.Nickname, user);
			res.setCode(0);
			res.setData("");
		} catch (Exception e2) {
			logger.debug(e2);
			MoneyLogger.log(depositOneClick.Nickname, PAYMENTNAME, money, 0L, "vin", "Nap vin qua " + PAYMENTNAME,
					"1031", "rmq error: " + e2.getMessage());
		} finally {
			userMap.unlock(depositOneClick.Nickname);
		}

		return res;
	}

	/**
	 * Create transaction
	 * 
	 * @param userId
	 * @param username
	 * @param nickname
	 * @param amount
	 * @param channel
	 * @param customerName
	 * @param bankCode
	 * @param ip
	 * @return RechargePaywellResponse
	 */
	@Override
	public RechargePaywellResponse createTransaction(String userId, String username, String nickname, long amount,
			String channel, String customerName, String bankCode, String ip) {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
		try {
			res = createOrder(userId, username, nickname, amount, bankCode, channel);
			if (res.getCode() != 0)
				return res;

			long id = Long.parseLong(res.getTid());
			res = requestCreateTransaction(String.valueOf(id), amount, channel, customerName, ip, bankCode);
			// String orderId, long amount,String bankCode, String payType, String
			// customerId, String customerFullName
			return res;
		} catch (Exception e) {
			logger.debug(e);
			return res;
		}
	}

	/**
	 * Handle notify from payment gateway
	 * 
	 * @param status
	 * @param amountStr
	 * @param netAmountStr
	 * @param transactionId
	 * @param orderId
	 * @param sign
	 * @return RechargePaywellResponse
	 */
	@Override
	public RechargePaywellResponse notify(String amountStr, String netAmountStr, String transactionId, String orderId,
			String sign) {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
		Double amount;
		RechargePaygateDao rechargeDao = new RechargePaygateDaoImpl();
		try {
			try {
				amount = Double.parseDouble(amountStr);
				// netAmountStr = Double.parseDouble(net_amount);
			} catch (NumberFormatException e) {
				logger.error(e);
				return res;
			}

			// check cardId
			DepositPaygateModel model = rechargeDao.GetById(orderId);
			if (model == null) {
				return res;
			}

			// check amount
			if (model.Amount != amount.longValue()) {
				res.setData("Amount wrong");
				return res;
			}
			// check status
			if (model.Status != PayCommon.PAYSTATUS.PENDING.getId()
					&& model.Status != PayCommon.PAYSTATUS.RECEIVED.getId()) {
				res.setData("status wrong");
				return res;
			}

			String[] paramArray = { "merchant_txn=" + transactionId, "merchant_customer=" + model.BankAccountName,
					"amount=" + model.Amount, "net_amount=" + netAmountStr, "tnx=" + transactionId, };
			String signEncode = getSignal(paramArray);
			if (!sign.equals(signEncode)) {
				res.setData("Invalid signature");
				return res;
			}

			if (!rechargeDao.UpdateStatus(orderId, transactionId, PAYSTATUS.SUCCESS.getId(), USERAPPROVE)) {
				res.setData("UpdateStatus SUCCESS fail");
				return res;
			}

			res = addMoney(model);
			if (res.getCode() != 0) {
				res.setData("Add money fail");
				return res;
			} else {
				if (!rechargeDao.UpdateStatus(orderId, transactionId, PAYSTATUS.COMPLETED.getId(), USERAPPROVE)) {
					res.setData("UpdateStatus COMPLETED fail");
					return res;
				}
			}
			res.setCode(0);
			return res;
		} catch (Exception e) {
			logger.debug(e);
			return res;
		}
	}

	/**
	 * Get list bank support
	 * 
	 * @param cartId
	 * @see List<BankOneClick>
	 */
	@Override
	public List<BankOneClick> getListBankSupport() {
		try {
			RechargePaywellResponse resResult = requestBanks();
			if (resResult.getCode() == 0) {
				Type listType = new TypeToken<List<BankOneClick>>() {
				}.getType();
				List<BankOneClick> banks = new Gson().fromJson(resResult.getData(), listType);
				return banks;
			}

			return null;
		} catch (Exception e) {
			logger.debug(e);
			return null;
		}
	}

	/**
	 * Check status transaction
	 * 
	 * @param cartId
	 * @return RechargePaywellResponse : code = 0 --> Transaction success; code = 1
	 *         --> Transaction failed
	 */
	@Override
	public RechargePaywellResponse checkStatus(String orderId) {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
		try {
			if (orderId.isEmpty()) {
				if (orderId.isEmpty()) {
					res.setData("Invalid parram(s)");
					return res;
				}
			}

			RechargePaywellResponse resResult = requestCheckStatusTransaction(orderId);
			if (resResult.getCode() == 0) {
				JSONObject jsonObj = new JSONObject(resResult.getData());
				String transaction_id = jsonObj.getString("transaction_id");
				JSONObject jsonObjLog = new JSONObject(jsonObj.getString("log"));
				Integer status = jsonObjLog.getInt("status");
				res.setCode(status);
				res.setData(transaction_id);
			}

			return res;
		} catch (Exception e) {
			logger.debug(e);
			return res;
		}
	}

	/**
	 * Get data transaction
	 * 
	 * @param orderId
	 * @return RechargePaywellResponse
	 */
	@Override
	public RechargePaywellResponse getDataTrans(String orderId) {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
		try {
			if (orderId.isEmpty()) {
				res.setData("Invalid parram(s)");
				return res;
			}

			return requestCheckStatusTransaction(orderId);
		} catch (Exception e) {
			logger.debug(e);
			return res;
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
	 * @return RechargePaywellResponse
	 */
	@Override
	public RechargePaywellResponse find(String nickname, int status, int page, int maxItem, String fromTime,
			String endTime, String providerName) {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
		try {
			RechargePaygateDao rechargeDao = new RechargePaygateDaoImpl();
			DepositPaygateReponse depositPayWellReponses = rechargeDao.Find(nickname, status, page, maxItem, fromTime,
					endTime, providerName);
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(depositPayWellReponses);
			res.setCode(0);
			res.setData(json.toString());
			return res;
		} catch (Exception e) {
			logger.debug((Object) e);
			return res;
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
	 * @return RechargePaywellResponse
	 */
	@Override
	public DepositPaygateReponse search(String nickname, int status, int page, int maxItem, String fromTime,
			String endTime, String providerName) {
		DepositPaygateReponse depositPayWellReponses = new DepositPaygateReponse(0, 0, 0,
				new ArrayList<DepositPaygateModel>());
		try {
			RechargePaygateDao rechargeDao = new RechargePaygateDaoImpl();
			depositPayWellReponses = rechargeDao.Find(nickname, status, page, maxItem, fromTime, endTime, providerName);
			return depositPayWellReponses;
		} catch (Exception e) {
			logger.debug((Object) e);
			return depositPayWellReponses;
		}
	}

	@Override
	public List<BankOneClick> getLstOneClickBank() {
		List<BankOneClick> resultBank = new ArrayList<>();
		try {
			// lst bank lot
			List<Bank> lstBankLote = GameCommon.LIST_BANK_NAME;
			RechargeOneClickPayService onePayService = new RechargeOneClickPayServiceImpl();
			// lst bank oneclick
			long t1 = System.currentTimeMillis();
			List<BankOneClick> banks = onePayService.getListBankSupport();
			long t2 = System.currentTimeMillis();
			logger.info("check time clickpay "+ (t2-t1));
			if (banks != null) {
				for (BankOneClick bankOneClick : banks) {
					for (Bank bankLote : lstBankLote) {
						if (bankLote.getCode().equalsIgnoreCase(bankOneClick.code)) {
							bankOneClick.bank_logo = bankLote.getLogo();
							resultBank.add(bankOneClick);
						}
					}
				}
			}
			return resultBank;
		} catch (Exception e) {
			logger.debug(e);
			return null;
		}
	}
}
