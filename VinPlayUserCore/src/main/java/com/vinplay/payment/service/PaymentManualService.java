package com.vinplay.payment.service;

import com.vinplay.dichvuthe.response.RechargePaywellResponse;

public interface PaymentManualService {
	public RechargePaywellResponse withdrawal(String orderId, String approvedName, String ip);

	public RechargePaywellResponse deposit(String nickName, String customerName, Long amount, String bankName,
			String bankNum, String payType, String desc);

	public RechargePaywellResponse depositConfirm(String orderId, String approvedName, String ip, int status,
			String rs);
}
