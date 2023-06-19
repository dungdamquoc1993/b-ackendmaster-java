package com.vinplay.dto.sportsbook;

import java.io.Serializable;

public class SportsbookFundTransferReqDto extends SportsbookBaseReqDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3552286772094703277L;

	private String UserName;
	private int PaymentType; // 0: withdrawal, 1: deposit
	private double Money;
	private String TicketNo;

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public int getPaymentType() {
		return PaymentType;
	}

	public void setPaymentType(int paymentType) {
		PaymentType = paymentType;
	}

	public double getMoney() {
		return Money;
	}

	public void setMoney(double money) {
		Money = money;
	}

	public String getTicketNo() {
		return TicketNo;
	}

	public void setTicketNo(String ticketNo) {
		TicketNo = ticketNo;
	}

}
