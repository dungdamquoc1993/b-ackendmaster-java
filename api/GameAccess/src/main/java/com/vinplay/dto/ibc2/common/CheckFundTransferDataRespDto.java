package com.vinplay.dto.ibc2.common;

import java.io.Serializable;

public class CheckFundTransferDataRespDto implements Serializable {
	private static final long serialVersionUID = 4094403815017900416L;
	private String trans_id;
	private String transfer_date;
	private Double amount;
	private Integer currency;
	private Double before_amount;
	private Double after_amount;
	private Integer status;
	
	public String getTrans_id() {
		return trans_id;
	}
	public void setTrans_id(String trans_id) {
		this.trans_id = trans_id;
	}
	public String getTransfer_date() {
		return transfer_date;
	}
	public void setTransfer_date(String transfer_date) {
		this.transfer_date = transfer_date;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Integer getCurrency() {
		return currency;
	}
	public void setCurrency(Integer currency) {
		this.currency = currency;
	}
	public Double getBefore_amount() {
		return before_amount;
	}
	public void setBefore_amount(Double before_amount) {
		this.before_amount = before_amount;
	}
	public Double getAfter_amount() {
		return after_amount;
	}
	public void setAfter_amount(Double after_amount) {
		this.after_amount = after_amount;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
