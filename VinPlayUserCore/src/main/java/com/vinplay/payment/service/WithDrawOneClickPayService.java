/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.vinplay.vbee.common.enums.ProviderType
 *  com.vinplay.vbee.common.messages.dvt.RechargeByCardMessage
 */
package com.vinplay.payment.service;

import com.vinplay.dichvuthe.response.*;
import com.vinplay.payment.entities.WithDrawPaygateModel;

import java.util.List;
import java.util.Map;

public interface WithDrawOneClickPayService {
	/**
	 * Create order into mongo db
	 * @param userId
	 * @param username
	 * @param nickname
	 * @param amount
	 * @param bankNumber
	 * @return RechargePaywellResponse
	 */
//	public RechargePaywellResponse requestWithdrawUser(String userId, String username, String nickname, long amount, String bankNumber);
	
	/**
     * Create withdraw order by staff backend
     * @param orderId
     * @param approvedName
     * @param ip
     * @return
     */
    public RechargePaywellResponse withdrawal(String orderId, String approvedName, String ip);
    
    /**
	 * Handle notify from payment gateway
	 * @param model
	 * @param result
	 * @return RechargePaywellResponse
	 */
	public boolean notify(WithDrawPaygateModel model, int status);
	
	/**
	 * Check status transaction
	 * @param orderId
	 * @return RechargePaywellResponse : code = 0 --> Transaction success; code = 1 --> Transaction failed
	 */
	public RechargePaywellResponse checkStatus(String orderId) ;
	
	/**
	 * Get data transaction
	 * @param orderId
	 * @return RechargePaywellResponse
	 */
	public RechargePaywellResponse getDataTrans(String orderId);
    
	/**
	 * Search transaction withdraw
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
	 * Reject withdraw
	 * @param orderId
	 * @param approvedName
	 * @param reason
	 * @return boolean
	 */
	public boolean reject(String orderId, String approvedName, String reason);

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
