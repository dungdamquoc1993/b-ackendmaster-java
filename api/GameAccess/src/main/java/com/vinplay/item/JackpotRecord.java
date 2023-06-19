package com.vinplay.item;

import java.io.Serializable;

public class JackpotRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String loginname;
	private int usertype;
	private String jackpotname;
	private int amount;
	private int amountreq;
	private int jackpotid;
	private String jackpottime;
	private String loginnamereal;
	private String param1;
	private String param2;
	private String param3;
	
	public JackpotRecord() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public int getUsertype() {
		return usertype;
	}

	public void setUsertype(int usertype) {
		this.usertype = usertype;
	}

	public String getJackpotname() {
		return jackpotname;
	}

	public void setJackpotname(String jackpotname) {
		this.jackpotname = jackpotname;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getJackpottime() {
		return jackpottime;
	}

	public void setJackpottime(String jackpottime) {
		this.jackpottime = jackpottime;
	}

	public String getLoginnamereal() {
		return loginnamereal;
	}

	public void setLoginnamereal(String loginnamereal) {
		this.loginnamereal = loginnamereal;
	}

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public String getParam3() {
		return param3;
	}

	public void setParam3(String param3) {
		this.param3 = param3;
	}

	public int getJackpotid() {
		return jackpotid;
	}

	public void setJackpotid(int jackpotid) {
		this.jackpotid = jackpotid;
	}

	public int getAmountreq() {
		return amountreq;
	}

	public void setAmountreq(int amountreq) {
		this.amountreq = amountreq;
	}
	
	
}
