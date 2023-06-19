/**
 * Archie
 */
package com.vinplay.dto.sbo;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Archie
 *
 */
public class SboUserDto {
	private String sboid;
	private String loginname;
	private Long sbocountid;

	public String getSboid() {
		return sboid;
	}

	public void setSboid(String sboid) {
		this.sboid = sboid;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public Long getSbocountid() {
		return sbocountid;
	}

	public void setSbocountid(Long sbocountid) {
		this.sbocountid = sbocountid;
	}

	public SboUserDto(String sboid, String loginname, Long sbocountid) {
		super();
		this.sboid = sboid;
		this.loginname = loginname;
		this.sbocountid = sbocountid;
	}

	public SboUserDto(ResultSet rs) throws SQLException {
		this.sboid = rs.getString("sboid");
		this.loginname = rs.getString("nick_name");
		this.sbocountid = (long) rs.getInt("sbocountid");
		if (rs != null) {
			rs.close();
		}
	}
	
	public SboUserDto() {
		super();
	}
	
	@Override
	public String toString() {
		return "SboUserDto [sboid=" + sboid + ", loginname=" + loginname + ", sbocountid=" + sbocountid + "]";
	}

}
