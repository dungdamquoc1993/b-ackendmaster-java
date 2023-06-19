package com.vinplay.dto.sg;

import java.io.Serializable;

public class LogoutDto extends BaseSGRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String userIds;
	public String getUserIds() {
		return userIds;
	}
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	
	
}
