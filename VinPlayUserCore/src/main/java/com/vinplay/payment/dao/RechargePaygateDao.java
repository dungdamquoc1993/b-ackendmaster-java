package com.vinplay.payment.dao;

import com.vinplay.payment.entities.DepositPaygateModel;
import com.vinplay.payment.entities.DepositPaygateReponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface RechargePaygateDao {
	public boolean isExistDeposit(String nickName);
	/**
	 * Check exist transaction is pending by nickname and provider name
	 * @param nickname
	 * @param providerName
	 * @return true: exist; false: not exist
	 */
	public boolean CheckPending(String nickname, String providerName);
	
	/**
	 * Get detail transaction by id
	 * @param Id: Id of transaction
	 * @return DepositPaygateModel
	 */
	public DepositPaygateModel GetById(String Id);
	public long topupByCash(DepositPaygateModel depositPayWellModel);
	public Boolean delete(String id);
	/**
	 * Add transaction
	 * @param depositPayWellModel
	 * @return orderId
	 */
	public long Add(DepositPaygateModel depositPayWellModel);
	
	public Boolean Update(DepositPaygateModel depositPayWellModel);
	
	public Boolean Delete(String id);
	
	public boolean updatePendingStatusToFailedAfterMinus(int minutes, String provider);
	
	public Boolean UpdateStatus(String orderId, String cartId, int status, String userApprove);
	
	public Boolean UpdateStatus(String orderId, int status, String userApprove);
	
	public Boolean UpdateStatus(String orderId, String cartId, int status, String userApprove, String reason);
	
	public Boolean UpdateRequestTime(String orderId, String reRequestTime, String userApprove);
	
	public Boolean UpdateAmount(String orderId, long amount, long amountFee, String userApprove);
	
	public DepositPaygateModel GetByReferenceId(String cartId);
	
	public DepositPaygateModel GetByOrderId(String orderId);
	
	public DepositPaygateReponse Find(DepositPaygateModel depositPayWellModel, int page, int maxItem, String fromTime, String endTime);
	
	public DepositPaygateReponse Find(String nickname, int status, int page, int maxItem, String fromTime, String endTime, String providerName);

	public List<Object> find(HashMap<String, Object> condition, int page, int maxItem) throws Exception;
	
	public long count(HashMap<String, Object> condition);
	
	public Long[] statistic(HashMap<String, Object> condition);
	
	public DepositPaygateReponse Find(String nickname, int status, int page, int maxItem, String fromTime,
			String endTime, String providerName, String orderId, String transactionId,String bankName,Double fromAmount,Double toAmount);

	public Map<String, Object> FindTransaction(String nickname, int status, int page, int maxItem, String fromTime,
			String endTime, String providerName);

	public Map<String, Object> FindTransaction(String nickname, String agentCode, int status, int page, int maxItem,
			String fromTime, String endTime, String providerName);
}

