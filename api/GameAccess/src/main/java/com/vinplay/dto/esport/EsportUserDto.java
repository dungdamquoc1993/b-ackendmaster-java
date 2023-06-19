/**
 * Archie
 */
package com.vinplay.dto.esport;

import java.io.Serializable;

/**
 * @author Archie
 *
 */
public class EsportUserDto implements Serializable {
	private String esportId;
	private String loginname;
	private String token;

	public String getEsportId() {
		return esportId;
	}

	public void setEsportId(String esportId) {
		this.esportId = esportId;
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

	public EsportUserDto(String esportId, String loginname, String token) {
		super();
		this.esportId = esportId;
		this.loginname = loginname;
		this.token = token;
	}

	public EsportUserDto() {
		super();
	}

	@Override
	public String toString() {
		return "EsportUserDto [esportId=" + esportId + ", loginname=" + loginname + ", token=" + token + "]";
	}

}
