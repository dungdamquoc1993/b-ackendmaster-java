package com.vinplay.dto.sg;

@SuppressWarnings("serial")
public class SGFundTransferRespDto extends BaseRespDto{
	private Double amount;
	private Double currentBalance;
	private Double databaseId;
	private String method;
	private String txCode;
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getCurrentBalance() {
		return currentBalance;
	}
	public void setCurrentBalance(Double currentBalance) {
		this.currentBalance = currentBalance;
	}
	public Double getDatabaseId() {
		return databaseId;
	}
	public void setDatabaseId(Double databaseId) {
		this.databaseId = databaseId;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getTxCode() {
		return txCode;
	}
	public void setTxCode(String txCode) {
		this.txCode = txCode;
	}
	
}
