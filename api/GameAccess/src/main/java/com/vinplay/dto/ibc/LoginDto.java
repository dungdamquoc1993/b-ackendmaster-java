package com.vinplay.dto.ibc;

import java.io.Serializable;

public class LoginDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = 5801361276213946996L;
	private String sessionToken;

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
}
