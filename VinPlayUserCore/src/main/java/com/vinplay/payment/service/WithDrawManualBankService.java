package com.vinplay.payment.service;

import com.vinplay.dichvuthe.response.RechargePaywellResponse;
import java.util.Map;

public interface WithDrawManualBankService {
	public RechargePaywellResponse create(String userId, String username, String nickname, String bankCode,
			String bankNumber, String bankAccount, String agentCode, long amount);

	public RechargePaywellResponse update(String id, int status, String userApproved);

	public RechargePaywellResponse Approved(String id, String userApproved, String cartId, String agentBankNumber);

	public RechargePaywellResponse Reject(String id, String userApproved);

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
	public Map<String, Object> FindTransaction(String nickname, int status, int page, int maxItem, String fromTime,
			String endTime, String providerName);

	Map<String, Object> FindTransaction(String nickname, String agentCode, int status, int page, int maxItem,
			String fromTime, String endTime, String providerName);
}
