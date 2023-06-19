package com.vinplay.dto.sg;

@SuppressWarnings("serial")
public class GetBalanceReqDto extends BaseSGRequest{
	private String userIds;
	private Integer isOffline = 0;
	private Integer isFilterBalance = 0;
	private Integer alluser = 0;
	public String getUserIds() {
		return userIds;
	}
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	public Integer getIsOffline() {
		return isOffline;
	}
	public void setIsOffline(Integer isOffline) {
		this.isOffline = isOffline;
	}
	public Integer getIsFilterBalance() {
		return isFilterBalance;
	}
	public void setIsFilterBalance(Integer isFilterBalance) {
		this.isFilterBalance = isFilterBalance;
	}
	public Integer getAlluser() {
		return alluser;
	}
	public void setAlluser(Integer alluser) {
		this.alluser = alluser;
	}
	
}
