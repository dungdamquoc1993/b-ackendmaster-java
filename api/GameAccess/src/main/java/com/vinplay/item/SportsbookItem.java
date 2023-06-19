package com.vinplay.item;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SportsbookItem implements Serializable {

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

	public SportsbookItem(String loginname, int sportsbook_countid, String sportsbookid, String sportsbook_username) {
		super();
		this.loginname = loginname;
		this.sportsbook_countid = sportsbook_countid;
		this.sportsbookid = sportsbookid;
		this.sportsbook_username = sportsbook_username;
	}

	public SportsbookItem() {
		super();
	}

	public SportsbookItem(ResultSet rs) throws SQLException {
		super();
		this.loginname = rs.getString("nick_name");
		this.sportsbook_countid = rs.getInt("cmd_countid");
		this.sportsbookid = rs.getString("cmdid");
		this.sportsbook_username = rs.getString("cmd_username");
		if(rs!=null) {
			rs.close();
		}
	}

}
