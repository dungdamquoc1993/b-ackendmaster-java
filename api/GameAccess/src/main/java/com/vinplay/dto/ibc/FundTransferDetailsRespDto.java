package com.vinplay.dto.ibc;

import java.io.Serializable;

public class FundTransferDetailsRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = 922884128145375373L;
	private String trans_id;
	private Double before_amount;
	private Double after_amount;
	private Integer status;
	public String getTrans_id() {
		return trans_id;
	}
	public void setTrans_id(String trans_id) {
		this.trans_id = trans_id;
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
