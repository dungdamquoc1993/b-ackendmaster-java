package com.vinplay.item;

public class DgUserItem implements java.io.Serializable {

	private static final long serialVersionUID = -9173954288983974994L;
	private String dgid;
	private String loginname;
	private String oddtype;

	public String getDgid() {
		return dgid;
	}

	public void setDgid(String dgid) {
		this.dgid = dgid;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getOddtype() {
		return oddtype;
	}

	public void setOddtype(String oddtype) {
		this.oddtype = oddtype;
	}

}
