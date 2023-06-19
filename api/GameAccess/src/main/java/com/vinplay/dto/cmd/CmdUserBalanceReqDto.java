package com.vinplay.dto.cmd;

import java.io.Serializable;

public class CmdUserBalanceReqDto extends CmdBaseReqDto implements Serializable {

	private static final long serialVersionUID = 8454075412054307080L;
	private String UserName;

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

}
