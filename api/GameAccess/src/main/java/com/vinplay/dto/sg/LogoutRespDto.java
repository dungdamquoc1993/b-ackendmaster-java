package com.vinplay.dto.sg;

@SuppressWarnings("serial")
public class LogoutRespDto extends BaseRespDto{
	private String[] logoutUsers;
	private int count;
	public String[] getLogoutUsers() {
		return logoutUsers;
	}
	public void setLogoutUsers(String[] logoutUsers) {
		this.logoutUsers = logoutUsers;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
