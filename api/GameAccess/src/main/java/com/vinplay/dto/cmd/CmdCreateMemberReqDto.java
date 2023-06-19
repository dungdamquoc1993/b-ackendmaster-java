package com.vinplay.dto.cmd;

import java.io.Serializable;

public class CmdCreateMemberReqDto extends CmdBaseReqDto implements Serializable {

	private static final long serialVersionUID = 1705618825627210151L;

	public String UserName;

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

}
