package com.vinplay.payment.service.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.dal.dao.AgentDAO;
import com.vinplay.dal.dao.impl.AgentDAOImpl;
import com.vinplay.dal.entities.agent.UserAgentModel;
import com.vinplay.dichvuthe.response.RechargePaywellResponse;
import com.vinplay.payment.dao.RechargePaygateDao;
import com.vinplay.payment.dao.impl.RechargePaygateDaoImpl;
import com.vinplay.payment.entities.DepositPaygateModel;
import com.vinplay.payment.entities.PaymentConfig;
import com.vinplay.payment.service.PaymentConfigService;
import com.vinplay.payment.service.RechargeManualBankService;
import com.vinplay.payment.utils.Constant;
import com.vinplay.payment.utils.PayCommon;
import com.vinplay.payment.utils.PaymentConstant;
import com.vinplay.usercore.entities.AgentBank;
import com.vinplay.usercore.logger.MoneyLogger;
import com.vinplay.usercore.service.AgentBankService;
import com.vinplay.usercore.service.impl.AgentBankServiceImpl;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.messages.LogMoneyUserMessage;
import com.vinplay.vbee.common.messages.MoneyMessageInMinigame;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.rmq.RMQApi;
import com.vinplay.vbee.common.statics.Consts;
import com.vinplay.vbee.common.utils.VinPlayUtils;

public class RechargeManualBankServiceImpl implements RechargeManualBankService {
	private static final Logger logger = Logger.getLogger(RechargeManualBankServiceImpl.class);
	private static final String PAYMENTNAME = PaymentConstant.PROVIDER.MANUAL_BANK;

	private PaymentConfig getPaymentConfig() {
		PaymentConfigService paymentConfigService = new PaymentConfigServiceImpl();
		return paymentConfigService.getConfigByKey(PAYMENTNAME);
	}

	@Override
	public RechargePaywellResponse create(String userId, String username, String nickname, String bankCode,
			String bankNumber, String bankAccount, String agentCode, String agentBankNumber, long amount,
			String cartId) {
		try {
			RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
			if (StringUtils.isBlank(nickname) || amount <= 0 || StringUtils.isBlank(bankCode)) {
				return res;
			}

			PaymentConfig paymentConfig = getPaymentConfig();
			if (amount < (paymentConfig == null ? 1000 : paymentConfig.getConfig().getMinMoney())) {
				res.setData("Money recharge less than minimum");
				return res;
			}

			// get pending transaction
			RechargePaygateDao rechargeDao = new RechargePaygateDaoImpl();
			if (rechargeDao.CheckPending(nickname, PAYMENTNAME)) {
				res.setCode(Constant.ERROR_PENDING);
				res.setData("Please wait for last request is completed.");
				return res;
			}

			AgentBankService agentBankService = new AgentBankServiceImpl();
			AgentBank agentBank = agentBankService.getByBankNumber(agentBankNumber);
			if (agentBank == null) {
				res.setCode(Constant.ERROR_BANK_AGENT);
				res.setData("Bank number of agent is invalid");
				return res;
			}

			if (!agentCode.equals(agentBank.getAgent_code())) {
				res.setCode(Constant.ERROR_AGENT_CODE);
				res.setData("Code of agent is not match bank number");
				return res;
			}

			AgentDAO agentDao = new AgentDAOImpl();
			UserAgentModel userAgentModel = new UserAgentModel();
			userAgentModel = agentDao.DetailUserAgentByCode(agentCode);
			if (userAgentModel == null) {
				res.setCode(Constant.ERROR_AGENT_CODE);
				res.setData("Code of agent is invalid");
				return res;
			}

			DepositPaygateModel modelCheck = new DepositPaygateModel();
			modelCheck = rechargeDao.GetByReferenceId(cartId);
			if (modelCheck != null) {
				res.setCode(Constant.ERROR_TRANSACTIONID);
				res.setData("Transaction code is exist");
				return res;
			}

			DepositPaygateModel model = new DepositPaygateModel();
			model.Id = "";
			model.Amount = amount;
			model.BankAccountName = bankAccount;
			model.BankAccountNumber = bankNumber;
			model.BankCode = bankCode;
			model.CartId = cartId;
			model.CreatedAt = "";
			model.Description = "";
			model.IsDeleted = false;
			model.PaymentType = "IB_OFFLINE";
			model.MerchantCode = agentCode;
			model.ProviderName = PAYMENTNAME;
			model.ModifiedAt = "";
			model.UserId = userId;
			model.Username = username;
			model.Nickname = nickname;
			model.ReferenceId = cartId;
			model.RequestTime = VinPlayUtils.getCurrentDateTime();
			model.Status = PayCommon.PAYSTATUS.PENDING.getId();
			model.UserApprove = "";
			model.AgentBankAccountName = agentBank.getBank_acount();
			model.AgentBankAccountNumber = agentBank.getBank_number();
			model.AgentBankCode = agentBank.getBank_code();
			model.Content = "Recharge " + amount + " for nickname: " + nickname + " from: " + bankNumber + " to: "
					+ agentBank.getBank_number() + " with TransactionId: " + cartId;
			long id = rechargeDao.Add(model);
			if (id == 0) {
				return res;
			}
			res.setCode(0);
			res.setTid(String.valueOf(id));
			return res;
		} catch (Exception e) {
			logger.error("create " + e.getMessage());
			return null;
		}
	}
	
	public RechargePaywellResponse topupByCash(String userId, String username, String nickname, String agentCode, long amount) {
		try {
			RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
			if (StringUtils.isBlank(nickname) || amount <= 0) {
				return res;
			}
			
			AgentDAO agentDao = new AgentDAOImpl();
			UserAgentModel userAgentModel = new UserAgentModel();
			userAgentModel = agentDao.DetailUserAgentByCode(agentCode);
			if (userAgentModel == null) {
				res.setCode(Constant.ERROR_AGENT_CODE);
				res.setData("Code of agent is invalid");
				return res;
			}
			
			if(username.equals(userAgentModel.getUsername()))
			{
				res.setCode(Constant.ERROR_AGENT_CODE);
				res.setData("You can not topup money for owner");
				return res;
			}
			
			HazelcastInstance client = HazelcastClientFactory.getInstance();
			if (client == null) {
				res.setCode(Integer.valueOf(Constant.ERROR_SYSTEM));
				res.setData("Can not connect cached svr");
				return res;
			}

			IMap<String, UserModel> userMap = client.getMap("users");
			if (!userMap.containsKey(userAgentModel.getNickname())) {
				res.setCode(Integer.valueOf(Constant.ERROR_SESSION));
				res.setData("Agent must be login on lobby before process topup request");
				return res;
			}
			
			if (!userMap.containsKey(nickname)) {
				res.setCode(Integer.valueOf(Constant.ERROR_SESSION));
				res.setData("User must be login on lobby before request topup");
				return res;
			}
			
			RechargePaygateDao rechargeDao = new RechargePaygateDaoImpl();
			DepositPaygateModel model = new DepositPaygateModel();
			model.Id = "";
			model.Amount = amount;
			model.BankAccountName = "CASH";
			model.BankAccountNumber = "CASH";
			model.BankCode = "CASH";
			model.CartId = String.valueOf(VinPlayUtils.generateTransId());
			model.CreatedAt = VinPlayUtils.getCurrentDateTime();
			model.IsDeleted = false;
			model.PaymentType = "IB_OFFLINE";
			model.MerchantCode = agentCode;
			model.ProviderName = PAYMENTNAME;
			model.ModifiedAt = VinPlayUtils.getCurrentDateTime();
			model.UserId = userId;
			model.Username = username;
			model.Nickname = nickname;
			model.ReferenceId = model.CartId;
			model.RequestTime = VinPlayUtils.getCurrentDateTime();
			model.Status = PayCommon.PAYSTATUS.COMPLETED.getId();
			model.UserApprove = userAgentModel.getNickname();
			model.AgentBankAccountName = "CASH";
			model.AgentBankAccountNumber = "CASH";
			model.AgentBankCode = "CASH";
			model.Content = "Recharge " + amount + " for nickname: " + nickname + " by CASH";
			model.Description = "Recharge " + amount + " for nickname: " + nickname + " via CASH by agent: " + userAgentModel.getNickname();
			long id = rechargeDao.topupByCash(model);
			if (id == 0) {
				return res;
			}
			
			res = discountMoney(model);
			if (res.getCode() != 0) {
				rechargeDao.delete(String.valueOf(id));
				return res;
			}

			res = addMoney(model);
			if (res.getCode() != 0) {
				model = rechargeDao.GetById(String.valueOf(id));
				if(model != null) {
					rechargeDao.UpdateStatus(String.valueOf(id), PayCommon.PAYSTATUS.FAILED.getId(), "system");
					addMoneyComeBackForAgent(model);
				}
				
				res.setData("Add point for player failure. Please contact customer care suport for help.");
				return res;
			}
			
			res.setCode(0);
			res.setTid(String.valueOf(id));
			return res;
		} catch (Exception e) {
			logger.error("topupByCash " + e.getMessage());
			return null;
		}
	}

	@Override
	public RechargePaywellResponse update(String id, int status, String userApproved) {
		try {
			RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
			if (StringUtils.isBlank(id)) {
				res.setCode(Integer.parseInt(Constant.ERROR_PARAM));
				res.setData("TransactionId can not empty");
				return res;
			}

			if (StringUtils.isBlank(userApproved)) {
				res.setCode(Integer.parseInt(Constant.ERROR_PARAM));
				res.setData("User approved can not empty");
				return res;
			}

			RechargePaygateDao rechargeDao = new RechargePaygateDaoImpl();
			DepositPaygateModel model = new DepositPaygateModel();
			model = rechargeDao.GetById(id);
			if (model == null) {
				res.setCode(Constant.ERROR_TRANSACTIONID);
				res.setData("Transaction can not found");
				return res;
			}

			if (model.Status != 0 && model.Status != 1) {
				res.setCode(Constant.ERROR_STATUS);
				res.setData("Status is invalid");
				return res;
			}

			String description = "Change status from " + PayCommon.PAYSTATUS.getById(model.Status) + " to "
					+ PayCommon.PAYSTATUS.getById(status) + " by " + userApproved;
			model.Status = status;
			model.Description = description;
			Boolean rs = rechargeDao.Update(model);
			if (!rs) {
				return res;
			}

			res.setCode(0);
			res.setTid(String.valueOf(id));
			return res;
		} catch (Exception e) {
			logger.error("update " + e.getMessage());
			return null;
		}
	}

	@Override
	public RechargePaywellResponse Approved(String id, String userApproved) {
		try {
			RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
			if (StringUtils.isBlank(id)) {
				res.setCode(Integer.parseInt(Constant.ERROR_PARAM));
				res.setData("TransactionId can not empty");
				return res;
			}

			if (StringUtils.isBlank(userApproved)) {
				res.setCode(Integer.parseInt(Constant.ERROR_PARAM));
				res.setData("User approved can not empty");
				return res;
			}

			RechargePaygateDao rechargeDao = new RechargePaygateDaoImpl();
			DepositPaygateModel model = new DepositPaygateModel();
			model = rechargeDao.GetById(id);
			if (model == null) {
				res.setCode(Constant.ERROR_TRANSACTIONID);
				res.setData("Transaction can not found");
				return res;
			}

			if (model.Status != 0 && model.Status != 1) {
				res.setCode(Constant.ERROR_STATUS);
				res.setData("Status is invalid");
				return res;
			}
			
			AgentDAO agentDao = new AgentDAOImpl();
			UserAgentModel userAgentModel = new UserAgentModel();
			userAgentModel = agentDao.DetailUserAgentByCode(model.MerchantCode);
			if (userAgentModel == null) {
				res.setCode(Constant.ERROR_AGENT_CODE);
				res.setData("Code of agent is invalid");
				return res;
			}
			
			HazelcastInstance client = HazelcastClientFactory.getInstance();
			if (client == null) {
				res.setCode(Integer.valueOf(Constant.ERROR_SYSTEM));
				res.setData("Can not connect cached svr");
				return res;
			}

			IMap<String, UserModel> userMap = client.getMap("users");
			if (!userMap.containsKey(userAgentModel.getNickname())) {
				res.setCode(Integer.valueOf(Constant.ERROR_SESSION));
				res.setData("Agent must be login on lobby before process topup request");
				return res;
			}
			
			if (!userMap.containsKey(model.Nickname)) {
				res.setCode(Integer.valueOf(Constant.ERROR_SESSION));
				res.setData("User must be login on lobby before request topup");
				return res;
			}
			
			int statusBefore = model.Status;
			String descriptionBefore = model.Description;
			String userApprovedBefore = model.UserApprove;
			String description = "Change status from " + PayCommon.PAYSTATUS.getById(model.Status) + " to "
					+ PayCommon.PAYSTATUS.COMPLETED + " by " + userApproved;
			model.Status = PayCommon.PAYSTATUS.COMPLETED.getId();
			model.Description = description;
			model.UserApprove = userApproved;
			Boolean rs = rechargeDao.Update(model);
			if (!rs) {
				return res;
			}
			
			res = discountMoney(model);
			if (res.getCode() != 0) {
				model.Status = statusBefore;
				model.Description = descriptionBefore;
				model.UserApprove = userApprovedBefore;
				rechargeDao.Update(model);
				return res;
			}

			res = addMoney(model);
			if (res.getCode() != 0) {
				res.setData("Add point for player failure. Please contact customer care suport for help.");
				return res;
			}

			res.setCode(0);
			res.setTid(String.valueOf(id));
			return res;
		} catch (Exception e) {
			logger.error("Approved " + e.getMessage());
			return null;
		}
	}

	@Override
	public RechargePaywellResponse Reject(String id, String userApproved) {
		try {
			RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
			if (StringUtils.isBlank(id)) {
				res.setCode(Integer.parseInt(Constant.ERROR_PARAM));
				res.setData("TransactionId can not empty");
				return res;
			}

			if (StringUtils.isBlank(userApproved)) {
				res.setCode(Integer.parseInt(Constant.ERROR_PARAM));
				res.setData("User approved can not empty");
				return res;
			}

			RechargePaygateDao rechargeDao = new RechargePaygateDaoImpl();
			DepositPaygateModel model = new DepositPaygateModel();
			model = rechargeDao.GetById(id);
			if (model == null) {
				res.setCode(Constant.ERROR_TRANSACTIONID);
				res.setData("Transaction can not found");
				return res;
			}

			if (model.Status != 0 && model.Status != 1) {
				res.setCode(Constant.ERROR_STATUS);
				res.setData("Status is invalid");
				return res;
			}
			
			AgentDAO agentDao = new AgentDAOImpl();
			UserAgentModel userAgentModel = new UserAgentModel();
			userAgentModel = agentDao.DetailUserAgentByCode(model.MerchantCode);
			if (userAgentModel == null) {
				res.setCode(Constant.ERROR_AGENT_CODE);
				res.setData("Code of agent is invalid");
				return res;
			}
			
			HazelcastInstance client = HazelcastClientFactory.getInstance();
			if (client == null) {
				res.setCode(Integer.valueOf(Constant.ERROR_SYSTEM));
				res.setData("Can not connect cached svr");
				return res;
			}

			IMap<String, UserModel> userMap = client.getMap("users");
			if (!userMap.containsKey(userAgentModel.getNickname())) {
				res.setCode(Integer.valueOf(Constant.ERROR_SESSION));
				res.setData("Agent must be login on lobby before process topup request");
				return res;
			}
			
			if (!userMap.containsKey(model.Nickname)) {
				res.setCode(Integer.valueOf(Constant.ERROR_SESSION));
				res.setData("User must be login on lobby before request topup");
				return res;
			}

			String description = "Change status from " + PayCommon.PAYSTATUS.getById(model.Status) + " to "
					+ PayCommon.PAYSTATUS.FAILED + " by " + userApproved;
			model.Status = PayCommon.PAYSTATUS.FAILED.getId();
			model.Description = description;
			model.UserApprove = userApproved;
			Boolean rs = rechargeDao.Update(model);
			if (!rs) {
				return res;
			}

			res.setCode(0);
			res.setTid(String.valueOf(id));
			return res;
		} catch (Exception e) {
			logger.error("Approved " + e.getMessage());
			return null;
		}
	}

	private RechargePaywellResponse discountMoney(DepositPaygateModel depositPaygateModel) {
		AgentDAO agentDao = new AgentDAOImpl();
		UserAgentModel userAgentModel = new UserAgentModel();
		try {
			userAgentModel = agentDao.DetailUserAgentByCode(depositPaygateModel.MerchantCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "fail");
		HazelcastInstance client = HazelcastClientFactory.getInstance();
		if (client == null) {
			MoneyLogger.log(userAgentModel.getNickname(), Consts.REQUEST_CASHOUT, depositPaygateModel.Amount, 0L, "vin",
					"Topup for player " + depositPaygateModel.Nickname + " by agent: " + userAgentModel.getNickname(),
					"1031", "Cannot connect hazelcast");
			return res;
		}

		String username = "";
		IMap<String, UserModel> userMap = client.getMap("users");
		if (!userMap.containsKey(userAgentModel.getNickname()))
			return res;

		try {
			userMap.lock(userAgentModel.getNickname());
			UserCacheModel user = (UserCacheModel) userMap.get(userAgentModel.getNickname());
			username = user.getUsername();
			long moneyUser = user.getVin();
			long currentMoney = user.getVinTotal();
			int cashoutMoney = user.getCashout();

			if (moneyUser < depositPaygateModel.Amount || currentMoney < depositPaygateModel.Amount) {
				res.setData("Balance of agent is not enough");
				return res;
			}

			user.setVin(moneyUser -= depositPaygateModel.Amount);
			user.setVinTotal(currentMoney -= depositPaygateModel.Amount);
			user.setCashout(cashoutMoney += (int) depositPaygateModel.Amount);
			user.setCashoutTime(new Date());
//			String desc = "Topup for player " + depositPaygateModel.Nickname + " by agent: "
//					+ userAgentModel.getNickname();
			String desc = "Topup by agent code: " + userAgentModel.getCode();
			MoneyMessageInMinigame messageMoney = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), user.getId(),
					userAgentModel.getNickname(), Consts.REQUEST_CASHOUT, moneyUser, currentMoney,
					depositPaygateModel.Amount, "vin", 0L, 0, 0);
			LogMoneyUserMessage messageLog = new LogMoneyUserMessage(user.getId(), userAgentModel.getNickname(),
					Consts.REQUEST_CASHOUT, "Cashout", currentMoney, depositPaygateModel.Amount * (-1), "vin", desc, 0L,
					false, user.isBot());
			RMQApi.publishMessagePayment(messageMoney, 16);
			RMQApi.publishMessageLogMoney(messageLog);
			userMap.put(userAgentModel.getNickname(), user);
			res.setCode(0);
			res.setData("");
			res.setCurrentMoney(currentMoney);
		} catch (Exception e2) {
			logger.debug(e2);
			MoneyLogger.log(username, Consts.REQUEST_CASHOUT, depositPaygateModel.Amount, 0L, "vin",
					"Topup for player " + depositPaygateModel.Nickname + " by agent: " + userAgentModel.getNickname(),
					"1031", "rmq error: " + e2.getMessage());
		} finally {
			userMap.unlock(userAgentModel.getNickname());
		}

		return res;
	}

	/**
	 * Add money customer
	 * 
	 * @param depositPayWellModel
	 * @see RechargePaywellResponse
	 */
	private RechargePaywellResponse addMoney(DepositPaygateModel depositPaygateModel) {
		AgentDAO agentDao = new AgentDAOImpl();
		UserAgentModel userAgentModel = new UserAgentModel();
		try {
			userAgentModel = agentDao.DetailUserAgentByCode(depositPaygateModel.MerchantCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "fail");
		// TODO: get ratio exchange
		// money = Math.round((double)money *
		// GameCommon.getValueDouble("RATIO_RECHARGE_BANK"));
		HazelcastInstance client = HazelcastClientFactory.getInstance();
		if (client == null) {
			MoneyLogger.log(depositPaygateModel.Nickname, PAYMENTNAME, depositPaygateModel.Amount, 0L, "vin",
					"Topup for player " + depositPaygateModel.Nickname + " by agent: " + userAgentModel.getNickname(),
					"1031", "Cannot connect hazelcast");
			return res;
		}

		IMap<String, UserModel> userMap = client.getMap("users");
		if (!userMap.containsKey(depositPaygateModel.Nickname))
			return res;
		try {
			userMap.lock(depositPaygateModel.Nickname);
			UserCacheModel user = (UserCacheModel) userMap.get(depositPaygateModel.Nickname);
			long moneyUser = user.getVin();
			long currentMoney = user.getVinTotal();
			long rechargeMoney = user.getRechargeMoney();
			user.setVin(moneyUser += depositPaygateModel.Amount);
			user.setVinTotal(currentMoney += depositPaygateModel.Amount);
			user.setRechargeMoney(rechargeMoney += depositPaygateModel.Amount);
//			String desc = "Topup for player " + depositPaygateModel.Nickname + " by agent: "
//					+ userAgentModel.getNickname();
			String desc = "Topup by agent code: " + userAgentModel.getCode();
			MoneyMessageInMinigame messageMoney = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), user.getId(),
					depositPaygateModel.Nickname, Consts.RECHARGE_BY_BANK, moneyUser, currentMoney,
					depositPaygateModel.Amount, "vin", 0L, 0, 0);
			LogMoneyUserMessage messageLog = new LogMoneyUserMessage(user.getId(), depositPaygateModel.Nickname,
					Consts.RECHARGE_BY_BANK, PAYMENTNAME, currentMoney, depositPaygateModel.Amount, "vin", desc, 0L,
					false, user.isBot());
			RMQApi.publishMessagePayment(messageMoney, 16);
			RMQApi.publishMessageLogMoney(messageLog);
			userMap.put(depositPaygateModel.Nickname, user);
			res.setCode(0);
			res.setData("");
		} catch (Exception e2) {
			logger.debug(e2);
			MoneyLogger.log(depositPaygateModel.Nickname, PAYMENTNAME, depositPaygateModel.Amount, 0L, "vin",
					"Topup for player " + depositPaygateModel.Nickname + " by agent: " + userAgentModel.getNickname(),
					"1031", "rmq error: " + e2.getMessage());
		} finally {
			userMap.unlock(depositPaygateModel.Nickname);
		}

		return res;
	}
	
	private RechargePaywellResponse addMoneyComeBackForAgent(DepositPaygateModel model) {
		AgentDAO agentDao = new AgentDAOImpl();
		UserAgentModel userAgentModel = new UserAgentModel();
		try {
			userAgentModel = agentDao.DetailUserAgentByCode(model.MerchantCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "fail");
		// TODO: get ratio exchange
		// money = Math.round((double)money *
		// GameCommon.getValueDouble("RATIO_RECHARGE_BANK"));
		HazelcastInstance client = HazelcastClientFactory.getInstance();
		if (client == null) {
			MoneyLogger.log(userAgentModel.getNickname(), PAYMENTNAME, model.Amount, 0L, "vin",
					"Add money comeback " + model.Amount + " from transaction(deposit_paygate) id: " 
					+ String.valueOf(model.Id) + " nickname " + model.Nickname + " agent code: " + model.MerchantCode,
					"1031", "Cannot connect hazelcast");
			return res;
		}

		IMap<String, UserModel> userMap = client.getMap("users");
		if (!userMap.containsKey(userAgentModel.getNickname()))
			return res;
		try {
			userMap.lock(userAgentModel.getNickname());
			UserCacheModel user = (UserCacheModel) userMap.get(userAgentModel.getNickname());
			long moneyUser = user.getVin();
			long currentMoney = user.getVinTotal();
			long rechargeMoney = user.getRechargeMoney();
			user.setVin(moneyUser += model.Amount);
			user.setVinTotal(currentMoney += model.Amount);
			user.setRechargeMoney(rechargeMoney += model.Amount);
			String desc = "Add money comeback (CASH) " + userAgentModel.getCode() + " from transaction(deposit_paygate) id: " + String.valueOf(model.Id) ;
			MoneyMessageInMinigame messageMoney = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), user.getId(),
					userAgentModel.getNickname(), Consts.RECHARGE_BY_BANK, moneyUser, currentMoney, model.Amount, "vin",
					0L, 0, 0);
			LogMoneyUserMessage messageLog = new LogMoneyUserMessage(user.getId(), userAgentModel.getNickname(),
					Consts.RECHARGE_BY_BANK, PAYMENTNAME, currentMoney, model.Amount, "vin", desc, 0L, false,
					user.isBot());
			RMQApi.publishMessagePayment(messageMoney, 16);
			RMQApi.publishMessageLogMoney(messageLog);
			userMap.put(userAgentModel.getNickname(), user);
			res.setCode(0);
			res.setData("");
		} catch (Exception e2) {
			logger.debug(e2);
			MoneyLogger.log(userAgentModel.getNickname(), PAYMENTNAME, model.Amount, 0L, "vin",
					"Add money comeback " + model.Amount + " from transaction(deposit_paygate) id: " 
					+ String.valueOf(model.Id) + " nickname " + model.Nickname + " agent code: " + model.MerchantCode,
					"1031", "rmq error: " + e2.getMessage());
		} finally {
			userMap.unlock(userAgentModel.getNickname());
		}

		return res;
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
			RechargePaygateDao dao = new RechargePaygateDaoImpl();
			data = dao.FindTransaction(nickname, status, page, maxItem, fromTime, endTime, providerName);
			return data;
		} catch (Exception e) {
			logger.error("FindTransaction " + e.getMessage());
			return new HashMap<>();
		}
	}
	
	@Override
	public Map<String, Object> FindTransaction(String nickname, String agentCode, int status, int page, int maxItem, String fromTime,
			String endTime, String providerName) {
		Map<String, Object> data = new HashMap<>();
		try {
			RechargePaygateDao dao = new RechargePaygateDaoImpl();
			data = dao.FindTransaction(nickname, agentCode, status, page, maxItem, fromTime, endTime, providerName);
			return data;
		} catch (Exception e) {
			logger.error("FindTransaction " + e.getMessage());
			return new HashMap<>();
		}
	}
}
