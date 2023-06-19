package com.vinplay.payment.service.impl;
import com.vinplay.utils.TelegramAlert;
import java.sql.SQLException;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.dal.dao.AgentDAO;
import com.vinplay.dal.dao.impl.AgentDAOImpl;
import com.vinplay.dal.entities.agent.UserAgentModel;
import com.vinplay.dichvuthe.response.RechargePaywellResponse;
import com.vinplay.payment.dao.WithDrawPaygateDao;
import com.vinplay.payment.dao.impl.WithDrawPaygateDaoImpl;
import com.vinplay.payment.entities.PaymentConfig;
import com.vinplay.payment.entities.WithDrawPaygateModel;
import com.vinplay.payment.service.PaymentConfigService;
import com.vinplay.payment.service.WithDrawManualBankService;
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

public class WithDrawManualBankServiceImpl implements WithDrawManualBankService {
	private static final Logger logger = Logger.getLogger(WithDrawManualBankServiceImpl.class);
	private static final String PAYMENTNAME = PaymentConstant.PROVIDER.MANUAL_BANK;

	public WithDrawManualBankServiceImpl() {
	}

	private PaymentConfig getPaymentConfig() {
		PaymentConfigService paymentConfigService = new PaymentConfigServiceImpl();
		return paymentConfigService.getConfigByKey(PAYMENTNAME);
	}

	private RechargePaywellResponse discountMoney(WithDrawPaygateModel model) {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "fail");
		HazelcastInstance client = HazelcastClientFactory.getInstance();
		if (client == null) {
			MoneyLogger.log(model.Nickname, Consts.REQUEST_CASHOUT, model.Amount, 0L, "vin",
					"Cashout request " + model.Amount + " from player " + model.Nickname + "(Bank number: "
							+ model.BankAccountNumber + ") to agent code: " + model.MerchantCode,
					"1031", "Cannot connect hazelcast");
			return res;
		}

		String username = "";
		IMap<String, UserModel> userMap = client.getMap("users");
		if (!userMap.containsKey(model.Nickname))
			return res;

		try {
			userMap.lock(model.Nickname);
			UserCacheModel user = (UserCacheModel) userMap.get(model.Nickname);
			username = user.getUsername();
			long moneyUser = user.getVin();
			long currentMoney = user.getVinTotal();
			int cashoutMoney = user.getCashout();

			if (moneyUser < model.Amount || currentMoney < model.Amount) {
				res.setData("Insufficient balance");
				return res;
			}

			user.setVin(moneyUser -= model.Amount);
			user.setVinTotal(currentMoney -= model.Amount);
			user.setCashout(cashoutMoney += (int) model.Amount);
			user.setCashoutTime(new Date());
//			String desc = "Cashout request " + model.Amount + " from player " + model.Nickname + "(Bank number: "
//					+ model.BankAccountNumber + ") to agent code: " + model.MerchantCode;
			String desc = "Bank number: "
					+ model.BankAccountNumber + "; Agent code: " + model.MerchantCode;
			MoneyMessageInMinigame messageMoney = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), user.getId(),
					model.Nickname, Consts.REQUEST_CASHOUT, moneyUser, currentMoney, model.Amount, "vin", 0L, 0, 0);
			LogMoneyUserMessage messageLog = new LogMoneyUserMessage(user.getId(), model.Nickname,
					Consts.REQUEST_CASHOUT, "Cashout", currentMoney, model.Amount * (-1), "vin", desc, 0L, false,
					user.isBot());
			RMQApi.publishMessagePayment(messageMoney, 16);
			RMQApi.publishMessageLogMoney(messageLog);
			userMap.put(model.Nickname, user);
			res.setCode(0);
			res.setData("");
			res.setCurrentMoney(currentMoney);
		} catch (Exception e2) {
			logger.debug(e2);
			MoneyLogger.log(username, Consts.REQUEST_CASHOUT, model.Amount, 0L, "vin",
					"Cashout request " + model.Amount + " from player " + model.Nickname + "(Bank number: "
							+ model.BankAccountNumber + ") to agent code: " + model.MerchantCode,
					"1031", "rmq error: " + e2.getMessage());
		} finally {
			userMap.unlock(model.Nickname);
		}

		return res;
	}

	private RechargePaywellResponse addMoney(WithDrawPaygateModel model) {
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
					"Cashout request " + model.Amount + " from player " + model.Nickname + "(Bank number: "
							+ model.BankAccountNumber + ") to agent code: " + model.MerchantCode,
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
//			String desc = "Cashout request " + model.Amount + " from player " + model.Nickname + "(Bank number: "
//					+ model.BankAccountNumber + ") to agent code: " + model.MerchantCode;
			String desc = "Bank number: " + model.BankAccountNumber + "; Agent code: " + model.MerchantCode;
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
					"Cashout request " + model.Amount + " from player " + model.Nickname + "(Bank number: "
							+ model.BankAccountNumber + ") to agent code: " + model.MerchantCode,
					"1031", "rmq error: " + e2.getMessage());
		} finally {
			userMap.unlock(userAgentModel.getNickname());
		}

		return res;
	}

	private RechargePaywellResponse addMoney(String nickname, long amount, String description) {
		RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "fail");
		// TODO: get ratio exchange
		// money = Math.round((double)money *
		// GameCommon.getValueDouble("RATIO_RECHARGE_BANK"));
		HazelcastInstance client = HazelcastClientFactory.getInstance();
		if (client == null) {
			MoneyLogger.log(nickname, PAYMENTNAME, amount, 0L, "vin", description, "1031", "Cannot connect hazelcast");
			return res;
		}

		IMap<String, UserModel> userMap = client.getMap("users");
		if (!userMap.containsKey(nickname))
			return res;
		try {
			userMap.lock(nickname);
			UserCacheModel user = (UserCacheModel) userMap.get(nickname);
			long moneyUser = user.getVin();
			long currentMoney = user.getVinTotal();
			long rechargeMoney = user.getRechargeMoney();
			user.setVin(moneyUser += amount);
			user.setVinTotal(currentMoney += amount);
			user.setRechargeMoney(rechargeMoney += amount);
			String desc = description;
			MoneyMessageInMinigame messageMoney = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), user.getId(),
					nickname, Consts.RECHARGE_BY_BANK, moneyUser, currentMoney, amount, "vin", 0L, 0, 0);
			LogMoneyUserMessage messageLog = new LogMoneyUserMessage(user.getId(), nickname, Consts.RECHARGE_BY_BANK,
					PAYMENTNAME, currentMoney, amount, "vin", desc, 0L, false, user.isBot());
			RMQApi.publishMessagePayment(messageMoney, 16);
			RMQApi.publishMessageLogMoney(messageLog);
			userMap.put(nickname, user);
			res.setCode(0);
			res.setData("");
		} catch (Exception e2) {
			logger.debug(e2);
			MoneyLogger.log(nickname, PAYMENTNAME, amount, 0L, "vin", description, "1031",
					"rmq error: " + e2.getMessage());
		} finally {
			userMap.unlock(nickname);
		}

		return res;
	}

	@Override
	public RechargePaywellResponse create(String userId, String username, String nickname, String bankCode,
			String bankNumber, String bankAccount, String agentCode, long amount) {
		try {
			RechargePaywellResponse res = new RechargePaywellResponse(1, 0L, 0, 0L, "");
			if (StringUtils.isBlank(nickname) || amount <= 0 || StringUtils.isBlank(bankCode)) {
				return res;
			}

			PaymentConfig paymentConfig = getPaymentConfig();
			if (amount < (paymentConfig == null ? 1000 : paymentConfig.getConfig().getMinMoney())) {
				res.setData("Money withdraw less than minimum");
				return res;
			}

			// get pending transaction
			WithDrawPaygateDao withdrawDao = new WithDrawPaygateDaoImpl();
			if (withdrawDao.CheckPending(nickname, PAYMENTNAME)) {
				res.setCode(Constant.ERROR_PENDING);
				res.setData("Please wait for last request is completed.");
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

			WithDrawPaygateModel model = new WithDrawPaygateModel();
			model.Id = "";
			model.Amount = amount;
			model.BankAccountName = bankAccount;
			model.BankAccountNumber = bankNumber;
			model.BankCode = bankCode;
			model.CartId = "";
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
			model.ReferenceId = "";
			model.RequestTime = VinPlayUtils.getCurrentDateTime();
			model.Status = PayCommon.PAYSTATUS.PENDING.getId();
			model.UserApprove = "";
			model.AgentBankAccountName = "";
			model.AgentBankAccountNumber = "";
			model.AgentBankCode = "";
			model.Content = "Cashout request " + amount + " from nickname: " + nickname + " to agent code: "
					+ agentCode;
			long id = withdrawDao.Add(model);
			if (id == 0) {
				return res;
			}

			model = withdrawDao.GetById(String.valueOf(id));
			if (model == null)
				return res;

			res = discountMoney(model);
			if (res.getCode() != 0) {
				withdrawDao.delete(String.valueOf(id));
				return res;
			}
                String message = "<b>[ADMIN-AGENT][CREATE-NEW] Yêu cầu rút tiền từ User " + nickname + "</b>";
				message += "\n Thời gian tạo: <b>" + VinPlayUtils.getCurrentDateTime() + "</b>";
	            message += "\n Số tiền: <b>"+ amount + "</b>";
	            message += "\n Mã Đại Lý: <b>" + agentCode + "</b>";
	           // message += "\n Ngân hàng nhận: <b>" + toBankNumber + "</b>";
				TelegramAlert.SendMessageRechard(message);
			res.setCode(0);
			res.setTid(String.valueOf(id));
			return res;
		} catch (Exception e) {
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

			WithDrawPaygateDao withdrawDao = new WithDrawPaygateDaoImpl();
			WithDrawPaygateModel model = new WithDrawPaygateModel();
			model = withdrawDao.GetById(id);
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
			Boolean rs = withdrawDao.Update(model);
			if (!rs) {
				return res;
			}

			res.setCode(0);
			res.setTid(String.valueOf(id));
			return res;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public RechargePaywellResponse Approved(String id, String userApproved, String cartId, String agentBankNumber) {
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

			WithDrawPaygateDao withdrawDao = new WithDrawPaygateDaoImpl();
			WithDrawPaygateModel model = new WithDrawPaygateModel();
			model = withdrawDao.GetByReferenceId(cartId);
			if (model != null) {
				res.setCode(Constant.ERROR_TRANSACTIONID);
				res.setData("Transaction code is exist");
				return res;
			}

			model = withdrawDao.GetById(id);
			if (model == null) {
				res.setCode(Constant.ERROR_TRANSACTIONID);
				res.setData("Transaction can not found");
				return res;
			}

			AgentBankService agentBankService = new AgentBankServiceImpl();
			AgentBank agentBank = agentBankService.getByBankNumber(agentBankNumber);
			if (agentBank == null) {
				res.setCode(Constant.ERROR_BANK_AGENT);
				res.setData("Bank number of agent is invalid");
				return res;
			}

			if (!model.MerchantCode.equals(agentBank.getAgent_code())) {
				res.setCode(Constant.ERROR_AGENT_CODE);
				res.setData("Code of agent is not match bank number");
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
			model.CartId = cartId;
			model.ReferenceId = cartId;
			model.AgentBankAccountName = agentBank.getBank_acount();
			model.AgentBankAccountNumber = agentBank.getBank_number();
			model.AgentBankCode = agentBank.getBank_code();
			model.UserApprove = userApproved;
			Boolean rs = withdrawDao.Update(model);
			if (!rs) {
				return res;
			}

			res = addMoney(model);
			if (res.getCode() != 0) {
				model.Status = statusBefore;
				model.Description = descriptionBefore;
				model.CartId = "";
				model.ReferenceId = "";
				model.AgentBankAccountName = "";
				model.AgentBankAccountNumber = "";
				model.AgentBankCode = "";
				model.UserApprove = userApprovedBefore;
				withdrawDao.Update(model);
				
				
				return res;
				
			}

			res.setCode(0);
			res.setTid(String.valueOf(id));
			return res;
		} catch (Exception e) {
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

			WithDrawPaygateDao withdrawDao = new WithDrawPaygateDaoImpl();
			WithDrawPaygateModel model = new WithDrawPaygateModel();
			model = withdrawDao.GetById(id);
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
			
			int statusBefore = model.Status;
			String descriptionBefore = model.Description;
			String userApprovedBefore = model.UserApprove;
			String description = "Change status from " + PayCommon.PAYSTATUS.getById(model.Status) + " to "
					+ PayCommon.PAYSTATUS.FAILED + " by " + userApproved;
			model.Status = PayCommon.PAYSTATUS.FAILED.getId();
			model.Description = description;
			model.UserApprove = userApproved;
			Boolean rs = withdrawDao.Update(model);
			if (!rs) {
				return res;
			}

//			description = "Rollback money cashout request " + model.Amount + " from player " + model.Nickname
//					+ "(Bank number: " + model.BankAccountNumber + ") to agent code: " + model.MerchantCode
//					+ " because agent reject";
			description = "Rollback because Agent: " + model.MerchantCode + " rejected";
			res = addMoney(model.Nickname, model.Amount, description);
			if (res.getCode() != 0) {
				model.Status = statusBefore;
				model.Description = descriptionBefore;
				model.UserApprove = userApprovedBefore;
				withdrawDao.Update(model);
				res.setData("Refund money failure. Please contact customer care support for help.");
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
			WithDrawPaygateDao dao = new WithDrawPaygateDaoImpl();
			data = dao.FindTransaction(nickname, status, page, maxItem, fromTime, endTime, providerName);
			return data;
		} catch (Exception e) {
			logger.debug((Object) e);
			return new HashMap<>();
		}
	}
	
	@Override
	public Map<String, Object> FindTransaction(String nickname, String agentCode, int status, int page, int maxItem, String fromTime,
			String endTime, String providerName) {
		Map<String, Object> data = new HashMap<>();
		try {
			WithDrawPaygateDao dao = new WithDrawPaygateDaoImpl();
			data = dao.FindTransaction(nickname, agentCode, status, page, maxItem, fromTime, endTime, providerName);
			return data;
		} catch (Exception e) {
			logger.debug((Object) e);
			return new HashMap<>();
		}
	}
}
