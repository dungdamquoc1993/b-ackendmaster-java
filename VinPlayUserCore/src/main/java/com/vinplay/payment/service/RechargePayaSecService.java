package com.vinplay.payment.service;

import com.vinplay.dichvuthe.response.RechargePaywellResponse;
import com.vinplay.payment.entities.DepositPaygateReponse;
import com.vinplay.payment.entities.PaywellNotifyRequest;
import java.util.Map;

public interface RechargePayaSecService {
	
	public RechargePaywellResponse createTransaction(String userId, String username,  String nickname, String fullName, long amount, String typeCard, String serial, String pin);
	
	public RechargePaywellResponse notification(String created, String updated, String refId, 
			String refIdPartner, String gateway, String gatewayDetail, long amount, long fee, 
			long netAmount, int status, String token);
	
	public RechargePaywellResponse find(String nickname, int status, int page, int maxItem, String fromTime, String endTime, String providerName);
	
	public DepositPaygateReponse search(String nickname, int status, int page, int maxItem, String fromTime, String endTime, String providerName);

	public Map<String, Object> FindTransaction(String nickname, int status, int page, int maxItem, String fromTime,
													 String endTime, String providerName);
}

