package com.vinplay.dto.sg;

@SuppressWarnings("serial")
public class WithdrawReqDto extends BaseSGRequest{
	private String userId;
	private String txCode;
	private Integer withdrawType = 0;
	private Double transferAmount;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTxCode() {
		return txCode;
	}
	public void setTxCode(String txCode) {
		this.txCode = txCode;
	}
	public Integer getWithdrawType() {
		return withdrawType;
	}
	public void setWithdrawType(Integer withdrawType) {
		this.withdrawType = withdrawType;
	}
	public Double getTransferAmount() {
		return transferAmount;
	}
	public void setTransferAmount(Double transferAmt) {
		this.transferAmount = transferAmt;
	}
	
}
