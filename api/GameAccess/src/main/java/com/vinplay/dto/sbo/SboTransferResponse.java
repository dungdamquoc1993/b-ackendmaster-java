package com.vinplay.dto.sbo;

import java.io.Serializable;

public class SboTransferResponse extends AbsSboBaseResponse<String, SboError> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4362329923599262678L;
	
	private Double amount;
	private String txnId;
	private String refno;
	private Double balance;
	private Double outstanding;

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getRefno() {
		return refno;
	}

	public void setRefno(String refno) {
		this.refno = refno;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getOutstanding() {
		return outstanding;
	}

	public void setOutstanding(Double outstanding) {
		this.outstanding = outstanding;
	}

	public SboTransferResponse(Double amount, String txnId, String refno, Double balance, Double outstanding) {
		super();
		this.amount = amount;
		this.txnId = txnId;
		this.refno = refno;
		this.balance = balance;
		this.outstanding = outstanding;
	}

	public SboTransferResponse() {
		super();
	}

	@Override
	public String toString() {
		return "SboTransferResponse [amount=" + amount + ", txnId=" + txnId + ", refno=" + refno + ", balance="
				+ balance + ", outstanding=" + outstanding + "]";
	}

}
