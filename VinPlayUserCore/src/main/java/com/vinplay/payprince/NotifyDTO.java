package com.vinplay.payprince;

import java.io.Serializable;

public class NotifyDTO implements Serializable {
	
	// {"status":"30916","result":"{\"transactionid\":\"1432\",\"orderid\":\"W_20210322110718596342\",\"amount\":\"100000.00\",\"real_amount\":0,\"custom\":\"EZJKSc\"}","sign":"B774FDE176677005D2D6ECC16A4DB714"}
	private String status;
	private ResultNotifyDTO result;
	private String sign;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ResultNotifyDTO getResult() {
		return result;
	}

	public void setResult(ResultNotifyDTO result) {
		this.result = result;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public NotifyDTO(String status, ResultNotifyDTO result, String sign) {
		super();
		this.status = status;
		this.result = result;
		this.sign = sign;
	}

	public NotifyDTO() {
		super();
	}

}
