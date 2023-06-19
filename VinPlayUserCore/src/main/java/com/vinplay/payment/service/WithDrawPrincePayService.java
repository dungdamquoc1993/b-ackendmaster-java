/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.vinplay.vbee.common.enums.ProviderType
 *  com.vinplay.vbee.common.messages.dvt.RechargeByCardMessage
 */
package com.vinplay.payment.service;

import com.vinplay.dichvuthe.response.*;
import com.vinplay.payment.entities.DepositPaygateReponse;
import com.vinplay.payment.entities.WithDrawPrinceReponse;

public interface WithDrawPrincePayService {
	/**
	 * Create order into mongo db
	 * @param userId
	 * @param username
	 * @param nickname
	 * @param amount
	 * @param bankNumber
	 * @return RechargePaywellResponse
	 */
	public RechargePaywellResponse requestWithdrawUser(String userId, String username, String nickname, long amount, String bankNumber);
	
	/**
     * Create withdraw order by staff backend
     * @param orderId
     * @param channel
     * @param approvedName
     * @param ip
     * @return RechargePaywellResponse
     */
	public RechargePaywellResponse withdrawal(String orderId, String channel, String approvedName, String ip);
    
//    /**
//     * Handle call back withdraw from payment gateway
//     * @param cartId
//     * @param referenceId
//     * @param amount
//     * @param amountFee
//     * @param status
//     * @param requestTime
//     * @param signature
//     * @return RechargePaywellResponse
//     */
//	public RechargePaywellResponse callback(String cartId, String referenceId, long amount, long amountFee, Integer status, long requestTime,  String signature);
    
	/**
	 * Handle notify from payment gateway
	 * 
	 * @param status
	 * @param result
	 * @param sign
	 * return RechargePaywellResponse
	 */
	public boolean notify(int status, String result, String sign);
    
    /**
	 * Search transaction withdraw
	 * @param nickname
	 * @param status
	 * @param page
	 * @param maxItem
	 * @param fromTime
	 * @param endTime
	 * @param providerName (If want get all, set value is empty)
	 * @return RechargePaywellResponse
	 */
	public RechargePaywellResponse findWithDraw(String nickname, int status, int page, int maxItem, String fromTime, String endTime, String providerName);
	
	/**
	 * Reject withdraw
	 * @param orderId
	 * @param approvedName
	 * @param reason
	 * @return boolean
	 */
//	public boolean reject(String orderId, String approvedName, String reason);
}
