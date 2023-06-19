package com.vinplay.dto.cmd;

import java.io.Serializable;

public class CmdUserBalance implements Serializable {

	private static final long serialVersionUID = 5715071470851292630L;

	private String UserName;
	private double BetAmount;
	private double Outstanding;

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

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

}
