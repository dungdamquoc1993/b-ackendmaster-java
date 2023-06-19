package com.vinplay.payprince;

import java.io.Serializable;

public class ResultNotifyDTO implements Serializable {
	// transactionid\":\"1432\",\"orderid\":\"W_20210322110718596342\",\"amount\":\"100000.00\",\"real_amount\":0,\"custom\":\"EZJKSc\"}
	private String transactionid;
	private String orderid;
	private long amount;
	private long real_amount;
	private String custom;

	public String getTransactionid() {
		return transactionid;
	}

	public void setTransactionid(String transactionid) {
		this.transactionid = transactionid;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public long getReal_amount() {
		return real_amount;
	}

	public void setReal_amount(long real_amount) {
		this.real_amount = real_amount;
	}

	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}

	public ResultNotifyDTO(String transactionid, String orderid, long amount, long real_amount, String custom) {
		super();
		this.transactionid = transactionid;
		this.orderid = orderid;
		this.amount = amount;
		this.real_amount = real_amount;
		this.custom = custom;
	}

	public ResultNotifyDTO() {
		super();
	}

}
