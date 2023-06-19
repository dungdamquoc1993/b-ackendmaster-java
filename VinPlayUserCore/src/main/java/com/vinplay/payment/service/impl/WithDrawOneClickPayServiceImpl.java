package com.vinplay.payment.service.impl;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

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
import com.vinplay.payment.entities.DepositPaygateModel;
import com.vinplay.payment.entities.PaymentConfig;
import com.vinplay.payment.entities.UserBank;
import com.vinplay.payment.entities.UserWithdraw;
import com.vinplay.payment.entities.WithDrawPaygateModel;
import com.vinplay.payment.entities.WithDrawPaygateReponse;
import com.vinplay.payment.service.PaymentConfigService;
import com.vinplay.payment.service.WithDrawOneClickPayService;
import com.vinplay.payment.utils.PayCommon;
import com.vinplay.payment.utils.PayCommon.PAYSTATUS;
import com.vinplay.payment.utils.PayUtils;
import com.vinplay.payment.utils.PaymentConstant.PROVIDER;
import com.vinplay.usercore.dao.UserBankDao;
import com.vinplay.usercore.dao.impl.UserBankDaoImpl;
import com.vinplay.usercore.logger.MoneyLogger;
import com.vinplay.utils.TelegramAlert;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.messages.BaseMessage;
import com.vinplay.vbee.common.messages.LogMoneyUserMessage;
import com.vinplay.vbee.common.messages.MoneyMessageInMinigame;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.rmq.RMQApi;
import com.vinplay.vbee.common.statics.Consts;
import com.vinplay.vbee.common.utils.VinPlayUtils;

public class WithDrawOneClickPayServiceImpl implements WithDrawOneClickPayService {
	private static final Logger logger = Logger.getLogger("backend");
	private static final String PAYMENTNAME = "clickpay";
	private static final String USERAPPROVE = "system";
	private static final String UID = "roy88";
	private static final String CHANNEL = "WITHDRAWONECLICK";

	private PaymentConfig paymentConfig = null;

	public WithDrawOneClickPayServiceImpl() {
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
			WithDrawPaygateDao withDrawPaygateDao = new WithDrawPaygateDaoImpl();
			WithDrawPaygateModel model = withDrawPaygateDao.GetById(orderId);
			if (model == null) {
				return res;
			}

			if (withDrawPaygateDao.UpdateRequestTime(orderId, requesTime, "supadmin") == false) {
				return res;
			}

			res.setCode(0);
			return res;
		} catch (Exception e) {
			return res;
		}
	}

	/**
	 * Update transactionId of oder by orderId
	 * 
	 * @param orderId
	 * @param amount
	 * @param bankCode
	 * @param payType
	 * @param customerId
	 * @param customerFullName
	 * @see RechargePaywellResponse
	 * @throws Exception
	 */
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

	private RechargePaywellResponse requestCreateWithDrawTransaction(WithDrawPaygateModel model, String ip)
			throws Exception {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
		ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
		long timetick = utc.toEpochSecond();
		if (updateRequestTime(model.CartId, String.valueOf(timetick), USERAPPROVE).getCode() != 0) {
			res.setData("");
			return res;
		}

		if (paymentConfig == null) {
			paymentConfig = getPaymentConfig();
		}
		Config config = this.paymentConfig.getConfig();
		String[] paramArray = { config.getMerchantCode(), model.CartId, model.BankAccountNumber, model.BankCode,
				String.valueOf(model.Amount) };
		String sign = getSignal(paramArray);
		if (sign.isEmpty()) {
			res.setData("");
			return res;
		}
		// call api
		// //amount=200000","uid=roy88","orderid=roy88","channel=908","notify_url=https://roy88.vip",
		// "return_url=https://roy88.vip","userip=127.0.0.1","timestamp=1608823688","custom=nguyen"
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("merchant_id", config.getMerchantCode()));
		urlParameters.add(new BasicNameValuePair("merchant_txn", model.CartId));
		urlParameters.add(new BasicNameValuePair("merchant_customer", config.getMerchantCode()));
		urlParameters.add(new BasicNameValuePair("bank_account_no", model.BankAccountNumber));
		urlParameters.add(new BasicNameValuePair("bank_account_name", model.BankAccountName));
		urlParameters.add(new BasicNameValuePair("bank_code", model.BankCode));
		urlParameters.add(new BasicNameValuePair("amount", String.valueOf(model.Amount)));
		urlParameters.add(new BasicNameValuePair("sign", sign));

		String urlRequest = config.getRequestAPI().replace("payment", "payout").replace("request", "create");
		String result = PayUtils.requestAPI(urlRequest, urlParameters);
		logger.info(PAYMENTNAME + "Response: " + result.toString());
		JSONObject jsonObj = new JSONObject(result);
		Integer code = jsonObj.getInt("code");
		if (code != 0) {
			res.setCode(code);
			updateStatusWithDraw(model, PayCommon.PAYSTATUS.FAILED, USERAPPROVE);
			// error
			res.setData(jsonObj.getString("message"));

		} else {
			res.setCode(code);
			String referenceId = jsonObj.getString("payout_id");
			if (referenceId.isEmpty()) {
				res.setCode(code);
				res = updateStatusWithDraw(model, PayCommon.PAYSTATUS.FAILED, USERAPPROVE);
				return res;
			} else {
				model.ReferenceId = referenceId;
				res = updateStatusWithDraw(model, PayCommon.PAYSTATUS.PENDING, USERAPPROVE);
				res.setData(jsonObj.getString("result"));
			}
		}

		return res;
	}

	/**
	 * Update status of order withdraw by orderId
	 * 
	 * @param orderId
	 * @param transId
	 * @param status
	 * @param userApprove
	 * @see RechargePaywellResponse
	 */
	private RechargePaywellResponse updateStatusWithDraw(WithDrawPaygateModel model, PayCommon.PAYSTATUS status,
			String userApprove) {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
		try {
			// get pending transaction
			WithDrawPaygateDao withDrawPaygateDao = new WithDrawPaygateDaoImpl();
			if (model == null) {
				return res;
			}

			switch (PayCommon.PAYSTATUS.getById(model.Status)) {//trạng thái bên db mình trong 7 phút
			case PENDING:
				if (status != PAYSTATUS.RECEIVED) {
					res.setData("Transaction has rejected");
					break;
				}

				if (withDrawPaygateDao.UpdateStatus(model.CartId, model.ReferenceId, status.getId(),
						userApprove.isEmpty() ? USERAPPROVE : userApprove) == false) {
					break;
				}

				res.setCode(0);
				break;
			case RECEIVED:
				if (status != PAYSTATUS.FAILED || status != PAYSTATUS.SUCCESS || status != PAYSTATUS.REVIEW
						|| status != PAYSTATUS.SPAM) {
					res.setData("Transaction has rejected");
					break;
				}

				if (status == PAYSTATUS.SUCCESS) {
					if(withDrawPaygateDao.UpdateStatus(model.CartId, model.ReferenceId, PAYSTATUS.COMPLETED.getId(), userApprove.isEmpty() ? USERAPPROVE : userApprove) == false) {
						res.setData("Update status COMPLETED failed");
		            }
					
					break;
				} else {
					if (withDrawPaygateDao.UpdateStatus(model.CartId, model.ReferenceId, status.getId(),
							userApprove.isEmpty() ? USERAPPROVE : userApprove) == false) {
						break;
					}

					if (status == PAYSTATUS.FAILED) {
						res = addBackMoney(model.CartId, model.Username, model.Amount);
						break;
					}

					res.setCode(0);
					break;
				}

			case FAILED:
				res.setData("Transaction has rejected");
				break;

			case COMPLETED:
				res.setData("Transaction has completed");
				break;

			case REVIEW:
				if (status != PAYSTATUS.COMPLETED || status != PAYSTATUS.FAILED || status != PAYSTATUS.SPAM) {
					res.setData("Invalid status");
					break;
				}

				if (withDrawPaygateDao.UpdateStatus(model.CartId, model.ReferenceId, status.getId(),
						userApprove.isEmpty() ? USERAPPROVE : userApprove) == false) {
					break;
				}

				res.setCode(0);
				break;

			case SPAM:
				res.setData("Transaction request many times");
				break;

			default:
				res.setData("Invalid status");
				break;
			}

			return res;
		} catch (Exception e) {
			logger.debug(e);
			return res;
		}
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
			MoneyLogger.log(nickname, Consts.CASH_OUT_BY_CLICKPAY, amount, 0L, "vin", "Cashout qua " + PAYMENTNAME + " fail",
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
					nickname, Consts.CASH_OUT_BY_CLICKPAY, moneyUser, currentMoney, amount, "vin", 0L, 0, 0);
			LogMoneyUserMessage messageLog = new LogMoneyUserMessage(user.getId(), nickname,
					Consts.CASH_OUT_BY_CLICKPAY, "Cashout", currentMoney, amount * (-1), "vin", desc, 0L,
					false, user.isBot());
			RMQApi.publishMessagePayment(messageMoney, 16);
			RMQApi.publishMessageLogMoney(messageLog);
			userMap.put(nickname, user);
			res.setCode(0);
			res.setData("");
		} catch (Exception e2) {
			logger.debug(e2);
			MoneyLogger.log(username, Consts.CASH_OUT_BY_CLICKPAY, amount, 0L, "vin", "Cashout qua " + PAYMENTNAME + " fail", "1031",
					"rmq error: " + e2.getMessage());
		} finally {
			userMap.unlock(nickname);
		}

		return res;
	}

	private RechargePaywellResponse addBackMoney(String orderId, String nickname, long amount) {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "fail");
		String username = "";
		// TODO: get ratio exchange
		// money = Math.round((double)money *
		// GameCommon.getValueDouble("RATIO_RECHARGE_BANK"));
		HazelcastInstance client = HazelcastClientFactory.getInstance();
		if (client == null) {
			MoneyLogger.log(nickname, Consts.CASH_OUT_BY_CLICKPAY, amount, 0L, "vin", "Nap tien do cashout fail qua " + PAYMENTNAME + " fail", "1031",
					"Cannot connect hazelcast");
			return res;
		}

		IMap<String, UserModel> userMap = client.getMap("users");
		if (!userMap.containsKey(nickname))
			return res;
		try {
			userMap.lock(nickname);
			WithDrawPaygateDao rechargeDao = new WithDrawPaygateDaoImpl();
			WithDrawPaygateModel withdrawModel = rechargeDao.GetById(orderId);
			if (withdrawModel == null)
				return res;

			UserCacheModel user = (UserCacheModel) userMap.get(nickname);
			username = user.getUsername();
			long moneyUser = user.getVin();
			long currentMoney = user.getVinTotal();
			long rechargeMoney = user.getRechargeMoney();
			user.setVin(moneyUser += amount);
			user.setVinTotal(currentMoney += amount);
			user.setRechargeMoney(rechargeMoney += amount);
			String desc = "Hoàn trả " + PAYMENTNAME ;
			MoneyMessageInMinigame messageMoney = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), user.getId(),
					nickname, Consts.CASH_OUT_BY_CLICKPAY, moneyUser, currentMoney, amount, "vin", 0L, 0, 0);
			LogMoneyUserMessage messageLog = new LogMoneyUserMessage(user.getId(), nickname,
					Consts.CASH_OUT_BY_CLICKPAY, "Cashout qua " + PAYMENTNAME + " fail", currentMoney, amount, "vin",
					desc, 0L, false, user.isBot());
			RMQApi.publishMessagePayment(messageMoney, 16);
			RMQApi.publishMessageLogMoney(messageLog);
			userMap.put(nickname, user);
			res.setCode(0);
			res.setData("");
		} catch (Exception e2) {
			logger.debug(e2);
			MoneyLogger.log(username, Consts.CASH_OUT_BY_CLICKPAY, amount, 0L, "vin", "Cashout qua " + PAYMENTNAME + " fail", "1031",
					"rmq error: " + e2.getMessage());
		} finally {
			userMap.unlock(nickname);
		}

		return res;
	}
	
	private RechargePaywellResponse refundMoney(String orderId, String nickname, long amount,String reason) {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "fail");
		String username = "";
		// TODO: get ratio exchange
		// money = Math.round((double)money *
		// GameCommon.getValueDouble("RATIO_RECHARGE_BANK"));
		HazelcastInstance client = HazelcastClientFactory.getInstance();
		if (client == null) {
			MoneyLogger.log(nickname, Consts.REFUND_RECHARGE, amount, 0L, "vin", "Hoàn trả , lý do "+ reason , "1031",
					"Cannot connect hazelcast");
			return res;
		}

		IMap<String, UserModel> userMap = client.getMap("users");
		if (!userMap.containsKey(nickname))
			return res;
		try {
			userMap.lock(nickname);
			WithDrawPaygateDao rechargeDao = new WithDrawPaygateDaoImpl();
			WithDrawPaygateModel withdrawModel = rechargeDao.GetById(orderId);
			if (withdrawModel == null)
				return res;

			UserCacheModel user = (UserCacheModel) userMap.get(nickname);
			username = user.getUsername();
			long moneyUser = user.getVin();
			long currentMoney = user.getVinTotal();
			long rechargeMoney = user.getRechargeMoney();
			user.setVin(moneyUser += amount);
			user.setVinTotal(currentMoney += amount);
			user.setRechargeMoney(rechargeMoney += amount);
			String desc = "Từ chối rút tiền  , lý do :" + reason ;
			MoneyMessageInMinigame messageMoney = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), user.getId(),
					nickname, Consts.REFUND_RECHARGE, moneyUser, currentMoney, amount, "vin", 0L, 0, 0);
			LogMoneyUserMessage messageLog = new LogMoneyUserMessage(user.getId(), nickname,
					Consts.REFUND_RECHARGE, Consts.SERVICE_PAYMENT.REJECT_CASHOUT, currentMoney, amount, "vin",
					desc, 0L, false, user.isBot());
			RMQApi.publishMessagePayment(messageMoney, 16);
			RMQApi.publishMessageLogMoney(messageLog);
			userMap.put(nickname, user);
			res.setCode(0);
			res.setData("");
		} catch (Exception e2) {
			logger.debug(e2);
			MoneyLogger.log(username, Consts.REFUND_RECHARGE, amount, 0L, "vin", "Hoàn trả , lý do "+ reason, "1031",
					"rmq error: " + e2.getMessage());
		} finally {
			userMap.unlock(nickname);
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
			urlParameters.add(new BasicNameValuePair("merchant_txn", orderId));
			urlParameters.add(new BasicNameValuePair("sign", sign));
			String url = config.getStatusAPI().replace("payment", "payout");
			String result = PayUtils.requestAPI(url, urlParameters);
			logger.debug((PAYMENTNAME + " Response: " + result.toString()));
			try {
				JSONObject jsonObj = new JSONObject(result.toString());
				res.setCode(jsonObj.getInt("code"));
				Integer code = jsonObj.getInt("code");
				if (code == 0) {
					res.setCode(code);
					String data = jsonObj.getString("data");
					res.setData(data);
				}
				return res;
			} catch (Exception e) {
			}
			return res;
		} catch (Exception e) {
			logger.debug(e);
			return res;
		}
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
//	@Override
//	public RechargePaywellResponse requestWithdrawUser(String userId, String username, String nickname, long amount,
//			String bankNumber) {
//
//		try {
//			RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
//			// HazelcastInstance client;
//			// IMap<String, UserModel> userMap;
//			if (nickname.isEmpty() || amount <= 0 || bankNumber.isEmpty()) {
//				return res;
//			}
//
//			UserBank userBank = new UserBank();
//			UserBankDao userBankDao = new UserBankDaoImpl();
//			try {
//				userBank = userBankDao.getByBankNumber(nickname, bankNumber);
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//
//			if (userBank == null) {
//				res.setData("Bank number is invalid");
//				return res;
//			}
//
//			HazelcastInstance client = HazelcastClientFactory.getInstance();
//			if (client == null) {
//				MoneyLogger.log(username, PAYMENTNAME, amount, 0L, "vin", "Cashout qua payment gateway " + PAYMENTNAME,
//						"1031", "Cannot connect hazelcast");
//				return res;
//			}
//
//			IMap<String, UserModel> userMap = client.getMap("users");
//			if (!userMap.containsKey(nickname))
//				return res;
//
//			UserCacheModel user = (UserCacheModel) userMap.get(nickname);
//			long moneyUser = user.getVin();
//			long currentMoney = user.getVinTotal();
//			if (moneyUser < amount || currentMoney < amount) {
//				res.setData("Tai khoan khong du tien");
//				return res;
//			}
//
////			String bankName = userBank.getBankName();
////			PaymentConfigService paymentConfigService = new PaymentConfigServiceImpl();
////			PaymentConfig paymentConfig = paymentConfigService.getConfigByKey(PAYMENTNAME);
////			if (amount < paymentConfig.getConfig().getMinMoney()) {
////				res.setData("So tien rut nho hon so tien quy dinh");
////				return res;
////			}
////			String bankCode = "";
////			try {
////				bankCode = paymentConfig.getConfig().getBanks().stream().filter(item -> item.getName().equals(bankName))
////						.findFirst().orElse(null).getKey();
////			} catch (Exception e) {
////				// TODO: handle exception
////			}
////            //get pending transaction
//			WithDrawPaygateDao withdrawDao = new WithDrawPaygateDaoImpl();
////            if(rechargeDao.CheckPending(nickname, PAYMENTNAME)){
////                return res;
////            }
//
//			WithDrawPaygateModel model = new WithDrawPaygateModel();
//			model.Id = "";
//			model.Amount = amount;
//			model.BankAccountName = userBank.getCustomerName();
//			model.BankAccountNumber = bankNumber;
//			model.BankCode = userBank.getBankName();//bankCode;
//			model.BankBranch = userBank.getBranch();
//			model.CartId = "";
//			model.CreatedAt = "";
//			model.IsDeleted = false;
//			model.PaymentType = "";
//			model.MerchantCode = "";//paymentConfig.getConfig().getMerchantCode();
//			model.ProviderName = "";//PAYMENTNAME;
//			model.ModifiedAt = "";
//			model.UserId = userId;
//			model.Username = username;
//			model.Nickname = nickname;
//			model.ReferenceId = "";
//			model.RequestTime = "";
//			model.Status = PayCommon.PAYSTATUS.REQUEST.getId();
//			model.UserApprove = username;
//			long id = withdrawDao.Add(model);
//			if (id == 0) {
//				return res;
//			}
//
//			model = withdrawDao.GetById(String.valueOf(id));
//			res = discountMoney(model.CartId, model.Nickname, model.Amount);
//			if (res.getCode() != 0) {
//				return res;
//			}
//
//			// TODO: Send message using telegram
//			UserWithdraw userWithdraw = new UserWithdraw(model.Username, (int) model.Amount, model.BankAccountNumber,
//					model.BankAccountName, userBank.getBankName() + " (Chi nhanh: " + userBank.getBranch() + ")");
//			TelegramAlert.SendMessageCashout(userWithdraw);
//			res.setCode(0);
//			res.setTid(String.valueOf(id));
//			return res;
//		} catch (Exception e) {
//			return null;
//		}
//	}

	/**
	 * Create withdraw order by staff backend
	 * 
	 * @param orderId
	 * @param approvedName
	 * @param ip
	 * @return
	 */
	@Override
	public RechargePaywellResponse withdrawal(String orderId, String approvedName, String ip) {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
		try {
			WithDrawPaygateModel model = new WithDrawPaygateModel();
			WithDrawPaygateDao withdrawDao = new WithDrawPaygateDaoImpl();
			model = withdrawDao.GetByOrderId(orderId);
			if (model == null) {
				res.setData("Object does not exist");
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
				bankCode = paymentConfig.getConfig().getBanks().stream().filter(item -> item.getName().equals(bankName))
						.findFirst().orElse(null).getKey();
			} catch (Exception e) {
				// TODO: handle exception
			}

			if (!withdrawDao.UpdateInfo(orderId, paymentConfig.getConfig().getMerchantCode(), CHANNEL, PAYMENTNAME, bankCode, approvedName))
				return res;

			model = withdrawDao.GetByOrderId(orderId);
			if (model == null) {
				res.setData("Object does not exist");
				return res;
			}

			res = requestCreateWithDrawTransaction(model, ip);
			return res;
		} catch (Exception e) {
			logger.debug(e);
			return res;
		}
	}

	/**
	 * Handle notify from payment gateway
	 * 
	 * @param model
	 * @param result
	 * @return RechargePaywellResponse
	 */
	@Override
	public boolean notify(WithDrawPaygateModel model, int status) {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
		try {
			// check cardId
			res = updateStatusWithDraw(model, PayCommon.PAYSTATUS.getById(status), USERAPPROVE);
			if (res.getCode() == 0) {
				return true;
			}

			return false;
		} catch (Exception e) {
			logger.debug(e);
			return false;
		}
	}

	/**
	 * Check status transaction
	 * 
	 * @param orderId
	 * @return RechargePaywellResponse : code = 0 --> Transaction success; code = 1
	 *         --> Transaction failed
	 */
	@Override
	public RechargePaywellResponse checkStatus(String orderId) {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
		try {
			if (orderId==null || orderId.isEmpty()) {
				res.setData("Invalid parram(s)");
				return res;
			}

			RechargePaywellResponse resResult = requestCheckStatusTransaction(orderId);
			if (res.getCode() == 0) {
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

			RechargePaygateDao rechargeDao = new RechargePaygateDaoImpl();
			DepositPaygateModel model = rechargeDao.GetById(orderId);
			if (model == null) {
				return res;
			}

			return requestCheckStatusTransaction(orderId);
		} catch (Exception e) {
			logger.debug(e);
			return res;
		}
	}

	/**
	 * Search transaction withdraw
	 * 
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
	public RechargePaywellResponse find(String nickname, int status, int page, int maxItem, String fromTime,
			String endTime, String providerName) {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
		try {
			WithDrawPaygateDao withDrawDao = new WithDrawPaygateDaoImpl();
			WithDrawPaygateReponse withDrawPayWellReponses = withDrawDao.Find(nickname, status, page, maxItem, fromTime,
					endTime, providerName);
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(withDrawPayWellReponses);
			res.setCode(0);
			res.setData(json.toString());
			return res;
		} catch (Exception e) {
			logger.debug((Object) e);
			return res;
		}
	}

	/**
	 * Reject withdraw
	 * @param orderId
	 * @param approvedName
	 * @param reason
	 * @return boolean
	 */
	@Override
	public boolean reject(String orderId, String approvedName, String reason) {
		try {
			// check cardId
			WithDrawPaygateDao withDrawDao = new WithDrawPaygateDaoImpl();
			WithDrawPaygateModel model = withDrawDao.GetById(orderId);
			if (model == null) {
				logger.error("[NOTIFY_WITHDRAW] orderId is not exist , orderid=" + orderId);
				return false;
			}

			if (model.Status != PAYSTATUS.REQUEST.getId()
					&& model.Status != PAYSTATUS.PENDING.getId()
					&& model.Status != PAYSTATUS.RECEIVED.getId()) {
				System.out.println(model.Status);
				System.out.println(PAYSTATUS.REQUEST.getId());
				logger.error("[NOTIFY_WITHDRAW] reject status " + PAYSTATUS.getById(model.Status));
				return false;
			}

			if (!withDrawDao.UpdateStatus(orderId, model.ReferenceId, PAYSTATUS.FAILED.getId(), approvedName, reason)) {
				logger.error("[NOTIFY_WITHDRAW] update status = FAILED fail, orderid=" + orderId);
				return false;
			}
			
			RechargePaywellResponse res = refundMoney(orderId, model.Nickname, model.Amount,reason);
            if(res.getCode() != 0) {
    			// push notification Telegram
    			TelegramAlert.SendMessage("[NOTIFY_WITHDRAW] NOTIFY status =" + PAYSTATUS.FAILED.getId()
    					+ " . Vui long kiem tra order_id tren he thong  , order_id ="
    					+ orderId);
    			return false;
            }
			return true;

		} catch (Exception e) {
			logger.error(e);
			return false;
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
			WithDrawPaygateDao dao = new WithDrawPaygateDaoImpl();
			data = dao.FindTransaction(nickname, status, page, maxItem, fromTime, endTime, providerName);
			return data;
		}catch (Exception e){
			logger.debug((Object)e);
			return new HashMap<>();
		}
	}
}
