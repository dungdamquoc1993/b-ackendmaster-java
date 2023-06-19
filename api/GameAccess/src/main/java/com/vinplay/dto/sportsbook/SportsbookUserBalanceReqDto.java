package com.vinplay.dto.sportsbook;

import java.io.Serializable;

public class SportsbookUserBalanceReqDto extends SportsbookBaseReqDto implements Serializable {

	private static final long serialVersionUID = 8454075412054307080L;
	private String UserName;

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public SportsbookUserBalanceReqDto(String userName) {
		super();
		UserName = userName;
	}

	public SportsbookUserBalanceReqDto() {
		super();
	}
}
