package com.vinplay.payment.entities;

import com.vinplay.payment.utils.PaymentConstant;

public class DepositPaygateModel {
	public String Id;
	public String CreatedAt;
	public String ModifiedAt;
	public Boolean IsDeleted;
	public String CartId;
	public String ReferenceId;
	public String UserId;
	public String Username;
	public String Nickname;
	public String RequestTime;
	public String BankCode;
	public String PaymentType;
	public String MerchantCode;
	public String ProviderName;
	public long Amount;
	public long AmountFee;
	public int Status;
	public String BankAccountNumber;
	public String BankAccountName;
	public String Description;
	public String UserApprove;
	public String AgentBankCode;
	public String AgentBankAccountNumber;
	public String AgentBankAccountName;
	public String Content;
	public String getPayTypeStr() {
		switch (this.ProviderName) {
		case PaymentConstant.PROVIDER.PAYWELL:
			if ("IB_ONLINE".equals(this.PaymentType)) {
				return "ONLINE";
			} else {
				return "OFFLINE";
			}
		case PaymentConstant.PROVIDER.ROYAL_PAY:
			if ("IB_ONLINE".equals(this.PaymentType)) {
				return "ONLINE";
			} else {
				return "OFFLINE";
			}
		case PaymentConstant.PROVIDER.CLICK_PAY:
			if ("IB_ONLINE".equals(this.PaymentType)) {
				return "ONLINE";
			} else {
				return "OFFLINE";
			}
		case PaymentConstant.PROVIDER.PRINCE_PAY:
			if ("907".equals(this.PaymentType)) {
				return "ONLINE";
			} 
			if ("908".equals(this.PaymentType)) {
				return "OFFLINE";
			}
			break;
		default:
			return "";
		}
		return "";
	}

	public DepositPaygateModel() {
	}

	/**
	 * @param id
	 * @param createdAt
	 * @param modifiedAt
	 * @param isDeleted
	 * @param cartId
	 * @param referenceId
	 * @param userId
	 * @param username
	 * @param nickname
	 * @param requestTime
	 * @param bankCode
	 * @param merchantCode
	 * @param amount
	 * @param amountFee
	 * @param status
	 * @param bankAccountNumber
	 * @param bankAccountName
	 * @param description
	 * @param userApprove
	 */
	public DepositPaygateModel(String id, String createdAt, String modifiedAt, Boolean isDeleted, String cartId,
			String referenceId, String userId, String username, String nickname, String requestTime, String bankCode,
			String paymentType, String merchantCode, String providerName, long amount, long amountFee, int status, String bankAccountNumber,
			String bankAccountName, String description, String userApprove) {
		Id = id;
		CreatedAt = createdAt;
		ModifiedAt = modifiedAt;
		IsDeleted = isDeleted;
		CartId = cartId;
		ReferenceId = referenceId;
		UserId = userId;
		Username = username;
		Nickname = nickname;
		RequestTime = requestTime;
		BankCode = bankCode;
		ProviderName = providerName;
		PaymentType = paymentType;
		MerchantCode = merchantCode;
		Amount = amount;
		AmountFee = amountFee;
		Status = status;
		BankAccountNumber = bankAccountNumber;
		BankAccountName = bankAccountName;
		Description = description;
		UserApprove = userApprove;
	}

	public DepositPaygateModel(String cartId, String nickname, String requestTime, String bankCode, String merchantCode,
			String providerName, int status) {
		CartId = cartId;
		Nickname = nickname;
		RequestTime = requestTime;
		BankCode = bankCode;
		MerchantCode = merchantCode;
		ProviderName = providerName;
		Status = status;
	}
}
