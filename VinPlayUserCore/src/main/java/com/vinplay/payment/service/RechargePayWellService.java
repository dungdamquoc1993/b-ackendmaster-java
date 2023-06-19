package com.vinplay.payment.service;

import com.vinplay.dichvuthe.response.RechargePaywellResponse;
import com.vinplay.payment.entities.DepositPaygateReponse;
import com.vinplay.payment.entities.PaywellNotifyRequest;

import java.util.List;
import java.util.Map;

public interface RechargePayWellService {
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
	public RechargePaywellResponse createTransaction(String userId, String username,  String nickname, String fullName, long amount, String bankCode, String payType);
	
	
    public RechargePaywellResponse notification(PaywellNotifyRequest requestObj);
	
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
    public RechargePaywellResponse callback(String cartId, String referenceId, long amount, long amountFee, Integer status, long requestTime,  String signature);
    
	/**
     * Check status transaction
     * @param cartId
     * @see RechargePaywellResponse
     */
	public RechargePaywellResponse checkStatusTrans(String cartId);
	
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
	public RechargePaywellResponse find(String nickname, int status, int page, int maxItem, String fromTime, String endTime, String providerName);
	
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
	public DepositPaygateReponse search(String nickname, int status, int page, int maxItem, String fromTime, String endTime, String providerName);

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
	public Map<String, Object> FindTransaction(String nickname, int status, int page, int maxItem, String fromTime,
													 String endTime, String providerName);
}

