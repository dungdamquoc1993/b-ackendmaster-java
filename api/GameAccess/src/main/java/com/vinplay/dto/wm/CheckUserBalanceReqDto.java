package com.vinplay.dto.wm;

@SuppressWarnings("serial")
public class CheckUserBalanceReqDto extends BaseReqDto {

	private String user;
	private Integer syslang;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Integer getSyslang() {
		return syslang;
	}

	public void setSyslang(Integer syslang) {
		this.syslang = syslang;
	}

}
