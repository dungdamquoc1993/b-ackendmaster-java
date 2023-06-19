package com.vinplay.dto.ibc;

import java.io.Serializable;

public class CheckFundTransferDetailsRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = 5699983129279614958L;
	private String trans_id;
	private Double amount;
	private Double before_amount;
	private Double after_amount;
	private String transfer_date;
	private Integer currency;
	private Integer status;
	public String getTrans_id() {
		return trans_id;
	}
	public void setTrans_id(String trans_id) {
		this.trans_id = trans_id;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
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
	public String getTransfer_date() {
		return transfer_date;
	}
	public void setTransfer_date(String transfer_date) {
		this.transfer_date = transfer_date;
	}
	public Integer getCurrency() {
		return currency;
	}
	public void setCurrency(Integer currency) {
		this.currency = currency;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
