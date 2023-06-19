package com.vinplay.dto.sportsbook;

import java.io.Serializable;

public class SportsbookFundTransferResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5453840257872405684L;

	private double BetAmount;
	private double Outstanding;
	private long PaymentId;

	public double getBetAmount() {
		return BetAmount;
	}

	public void setBetAmount(double betAmount) {
		BetAmount = betAmount;
	}

	public double getOutstanding() {
		return Outstanding;
	}

	public void setOutstanding(double outstanding) {
		Outstanding = outstanding;
	}

	public long getPaymentId() {
		return PaymentId;
	}

	public void setPaymentId(long paymentId) {
		PaymentId = paymentId;
	}

}
