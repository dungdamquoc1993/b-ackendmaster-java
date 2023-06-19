package com.vinplay.payment.entities;

public class WithDrawPaygateModel {
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
	public String BankBranch;
	public String UserApprove;
	public String Description;

	public String AgentBankCode;
	public String AgentBankAccountNumber;
	public String AgentBankAccountName;
	public String Content;

	public WithDrawPaygateModel() {
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
	 * @param bankBranch
	 * @param userApprove
	 * @param description
	 */
	public WithDrawPaygateModel(String id, String createdAt, String modifiedAt, Boolean isDeleted, String cartId,
			String referenceId, String userId, String username, String nickname, String requestTime, String bankCode,
			String paymentType, String merchantCode, String providerName, long amount, long amountFee, int status, String bankAccountNumber,
			String bankAccountName, String bankBranch, String userApprove, String description) {
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
		BankBranch = bankBranch;
		UserApprove = userApprove;
		Description = description;
	}

	public WithDrawPaygateModel(String id, String createdAt, String modifiedAt, Boolean isDeleted, String cartId, String referenceId, String userId, String username, String nickname, String requestTime, String bankCode, String paymentType, String merchantCode, String providerName, long amount, long amountFee, int status, String bankAccountNumber, String bankAccountName, String bankBranch, String userApprove, String description, String agentBankCode, String agentBankAccountNumber, String agentBankAccountName, String content) {
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
		PaymentType = paymentType;
		MerchantCode = merchantCode;
		ProviderName = providerName;
		Amount = amount;
		AmountFee = amountFee;
		Status = status;
		BankAccountNumber = bankAccountNumber;
		BankAccountName = bankAccountName;
		BankBranch = bankBranch;
		UserApprove = userApprove;
		Description = description;
		AgentBankCode = agentBankCode;
		AgentBankAccountNumber = agentBankAccountNumber;
		AgentBankAccountName = agentBankAccountName;
		Content = content;
	}

	public WithDrawPaygateModel(String cartId, String nickname, String requestTime, String bankCode, String merchantCode,
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
