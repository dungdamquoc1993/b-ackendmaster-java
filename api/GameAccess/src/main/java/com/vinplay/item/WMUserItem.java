package com.vinplay.item;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WMUserItem implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String wmid;
	private String loginname;
	private String username;
	private String user1;
	private String password;
	private Integer wmcountid;

	public WMUserItem() {
	}

	public WMUserItem(String wmid, String loginname, String username, String password, Integer wmcountid) {
		this.wmid = wmid;
		this.loginname = loginname;
		this.username = username;
		this.password = password;
		this.wmcountid = wmcountid;
	}
	
	
	public WMUserItem(ResultSet rs) throws SQLException {
		this.wmid = rs.getString("wmid");
		this.loginname = rs.getString("nick_name");
		this.username = rs.getString("username");
		this.password = rs.getString("password");
		this.wmcountid = rs.getInt("wmcountid");
		if (rs != null) {
			rs.close();
		}
	}

	public String getWmid() {
		return wmid;
	}

	public void setWmid(String wmid) {
		this.wmid = wmid;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUser1() {
		return user1;
	}

	public void setUser1(String user1) {
		this.user1 = user1;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getWmcountid() {
		return wmcountid;
	}

	public void setWmcountid(Integer wmcountid) {
		this.wmcountid = wmcountid;
	}

}
