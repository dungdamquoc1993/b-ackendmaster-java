package com.vinplay.item;

import java.io.Serializable;

public class CmdItem implements Serializable {

	private static final long serialVersionUID = -8195844365074085376L;

	private String loginname;
	private int sportsbook_countid;
	private String sportsbookid;
	private String sportsbook_username;

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public int getSportsbook_countid() {
		return sportsbook_countid;
	}

	public void setSportsbook_countid(int sportsbook_countid) {
		this.sportsbook_countid = sportsbook_countid;
	}

	public String getSportsbookid() {
		return sportsbookid;
	}

	public void setSportsbookid(String sportsbookid) {
		this.sportsbookid = sportsbookid;
	}

	public String getSportsbook_username() {
		return sportsbook_username;
	}

	public void setSportsbook_username(String sportsbook_username) {
		this.sportsbook_username = sportsbook_username;
	}

}
