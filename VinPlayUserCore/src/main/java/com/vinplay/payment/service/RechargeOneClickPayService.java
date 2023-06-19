/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.vinplay.vbee.common.enums.ProviderType
 *  com.vinplay.vbee.common.messages.dvt.RechargeByCardMessage
 */
package com.vinplay.payment.service;

import java.util.List;

import com.vinplay.dichvuthe.response.RechargePaywellResponse;
import com.vinplay.payment.entities.BankOneClick;
import com.vinplay.payment.entities.DepositPaygateReponse;

public interface RechargeOneClickPayService {
	
	/**
	 * Create transaction
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
	public RechargePaywellResponse createTransaction(String userId, String username, String nickname, long amount,
			String channel, String customerName, String bankCode, String ip);
	
	/**
	 * Handle notify success from payment gateway
	 * @param amountStr
	 * @param netAmountStr
	 * @param transactionId
	 * @param orderId
	 * @param sign
	 * @return RechargePaywellResponse
	 */
	public RechargePaywellResponse notify(String amountStr, String netAmountStr, String transactionId, String orderId , String sign);

	/**
	 * Get list bank support
	 * @param cartId
	 * @see List<BankOneClick>
	 */
	public List<BankOneClick> getListBankSupport();
	
	public List<BankOneClick> getLstOneClickBank();
	
	/**
	 * Check status transaction
	 * @param orderId
	 * @return RechargePaywellResponse : code = 0 --> Transaction success; code = 1 --> Transaction failed
	 */
	public RechargePaywellResponse checkStatus(String orderId); 
	
	/**
	 * Get data transaction
	 * @param orderId
	 * @return RechargePaywellResponse
	 */
	public RechargePaywellResponse getDataTrans(String orderId);

	/**
	 * Search transaction
	 * @param nickname
	 * @param status
	 * @param page
	 * @param maxItem
	 * @param fromTime
	 * @param endTime
	 * @param providerName (If want get all, set value is empty)
	 * @return RechargePaywellResponse
	 */
	public RechargePaywellResponse find(String nickname, int status, int page, int maxItem, String fromTime,
			String endTime, String providerName);

	/**
	 * Search transaction
	 * @param nickname
	 * @param status
	 * @param page
	 * @param maxItem
	 * @param fromTime
	 * @param endTime
	 * @param providerName (If want get all, set value is empty)
	 * @return RechargePaywellResponse
	 */
	public DepositPaygateReponse search(String nickname, int status, int page, int maxItem, String fromTime,
			String endTime, String providerName);
}
