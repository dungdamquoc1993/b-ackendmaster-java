package com.vinplay.dto.ag;

import java.io.Serializable;

public class BaseAGRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1498393743009251823L;

	private String loginname;

	private String password;

	private String oddtype;

	private String cur;

	private String method;

	private String cagent;

	private String actype;
	
	private String dm;
	
	private String lang;
	
	private String sid;
	
	private String gameType;
	
	private String mh5;
	
	private String type;
	
	private String credit;
	
	private String billno;
	
	private String flag;
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getBillno() {
		return billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public String getDm() {
		return dm;
	}

	public void setDm(String dm) {
		this.dm = dm;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public String getMh5() {
		return mh5;
	}

	public void setMh5(String mh5) {
		this.mh5 = mh5;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getCagent() {
		return cagent;
	}

	public void setCagent(String cagent) {
		this.cagent = cagent;
	}

	public String getActype() {
		return actype;
	}

	public void setActype(String actype) {
		this.actype = actype;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOddtype() {
		return oddtype;
	}

	public void setOddtype(String oddtype) {
		this.oddtype = oddtype;
	}

	public String getCur() {
		return cur;
	}

	public void setCur(String cur) {
		this.cur = cur;
	}
}
