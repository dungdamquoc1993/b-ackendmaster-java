package com.vinplay.dto.ebet;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EbetUserItem implements Serializable {
	private static final long serialVersionUID = -8475344024037468270L;
	/**
	 * 
	 */
	private String ebetid;
	private String password;
	private String loginname;
	private String token;
	private Long ebetcountid;
	private Integer status;
	
	private Integer timestamp;
	
	public EbetUserItem(String ebetid, String password, String loginname, Long ebetcountid ,Integer timestamp) {
		this.ebetid = ebetid;
		this.password = password;
		this.loginname = loginname;
		this.ebetcountid = ebetcountid;
		this.timestamp=timestamp;
	}

	public EbetUserItem(ResultSet rs) throws SQLException {
		this.ebetid = rs.getInt("ebetid") + "";
		this.password = rs.getString("password");
		this.loginname = rs.getString("nick_name");
		this.ebetcountid = (long) rs.getInt("ebetcountid");
		this.timestamp = rs.getInt("timestamps");
		if (rs != null) {
			rs.close();
		}
	}

	public EbetUserItem() {
		super();
	}

	public Integer getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Integer timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getEbetid() {
		return ebetid;
	}

	public Long getEbetcountid() {
		return ebetcountid;
	}

	public void setEbetcountid(Long ebetcountid) {
		this.ebetcountid = ebetcountid;
	}

	public void setEbetid(String ebetid) {
		this.ebetid = ebetid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
