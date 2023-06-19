package com.vinplay.dto.sg;

@SuppressWarnings("serial")
public class CheckTransferRespDto extends BaseRespDto{
	private Integer txStatus;
	private String transferType;
	private String txCode;
	private Double transferAmount;
	public Integer getTxStatus() {
		return txStatus;
	}
	public void setTxStatus(Integer txStatus) {
		this.txStatus = txStatus;
	}
	public String getTransferType() {
		return transferType;
	}
	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}
	public String getTxCode() {
		return txCode;
	}
	public void setTxCode(String txCode) {
		this.txCode = txCode;
	}
	public Double getTransferAmount() {
		return transferAmount;
	}
	public void setTransferAmount(Double transferAmt) {
		this.transferAmount = transferAmt;
	}
	
	
}
