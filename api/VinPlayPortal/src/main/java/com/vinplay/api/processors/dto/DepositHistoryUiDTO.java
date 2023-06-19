package com.vinplay.api.processors.dto;

public class DepositHistoryUiDTO {
	private String Id;
	private String CreatedAt;
	private String Nickname;
	private String PaymentType;
	private long Amount;
	private long AmountFee;
	private String BankCode;
	private int Status;
	private String BankAccountNumber;
	private String BankAccountName;
	private String Description;
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getCreatedAt() {
		return CreatedAt;
	}
	public void setCreatedAt(String createdAt) {
		CreatedAt = createdAt;
	}
	public String getNickname() {
		return Nickname;
	}
	public void setNickname(String nickname) {
		Nickname = nickname;
	}
	public String getPaymentType() {
		return PaymentType;
	}
	public void setPaymentType(String paymentType) {
		PaymentType = paymentType;
	}
	public long getAmount() {
		return Amount;
	}
	public void setAmount(long amount) {
		Amount = amount;
	}
	public long getAmountFee() {
		return AmountFee;
	}
	public void setAmountFee(long amountFee) {
		AmountFee = amountFee;
	}
	public String getBankCode() {
		return BankCode;
	}
	public void setBankCode(String bankCode) {
		BankCode = bankCode;
	}
	public int getStatus() {
		return Status;
	}
	public void setStatus(int status) {
		Status = status;
	}
	public String getBankAccountNumber() {
		return BankAccountNumber;
	}
	public void setBankAccountNumber(String bankAccountNumber) {
		BankAccountNumber = bankAccountNumber;
	}
	public String getBankAccountName() {
		return BankAccountName;
	}
	public void setBankAccountName(String bankAccountName) {
		BankAccountName = bankAccountName;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	
}
