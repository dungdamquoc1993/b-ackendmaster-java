/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.vinplay.vbee.common.enums.ProviderType
 *  com.vinplay.vbee.common.messages.dvt.RechargeByCardMessage
 */
package com.vinplay.payment.service;

import com.vinplay.dichvuthe.response.RechargePaywellResponse;
import com.vinplay.payment.entities.DepositPaygateReponse;

public interface RechargePrincePayService {
	public RechargePaywellResponse createTransaction(String userId, String username, String nickname, long amount,
			String channel, String customerName,String bankcode, String ip);

//	public RechargePaywellResponse callback(String cartId, String referenceId, long amount, long amountFee,
//			Integer status, long requestTime, String signature);

	public RechargePaywellResponse notify(int status ,String result ,String sign);

	public RechargePaywellResponse checkStatusTrans(String cartId);

	public RechargePaywellResponse find(String nickname, int status, int page, int maxItem, String fromTime,
			String endTime, String providerName);

	public DepositPaygateReponse search(String nickname, int status, int page, int maxItem, String fromTime,
			String endTime, String providerName);
}
