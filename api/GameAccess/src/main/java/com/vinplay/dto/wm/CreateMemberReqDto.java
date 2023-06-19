package com.vinplay.dto.wm;

public class CreateMemberReqDto extends BaseReqDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 469642559693614404L;

	private String user;
	private String password;
	private String username;
	private int profile;
	private int maxwin;
	private int maxlose;
	private String mark;
	private int rakeback;
	private String limitType;
	private String chip;
	private int syslang;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getProfile() {
		return profile;
	}

	public void setProfile(int profile) {
		this.profile = profile;
	}

	public int getMaxwin() {
		return maxwin;
	}

	public void setMaxwin(int maxwin) {
		this.maxwin = maxwin;
	}

	public int getMaxlose() {
		return maxlose;
	}

	public void setMaxlose(int maxlose) {
		this.maxlose = maxlose;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public int getRakeback() {
		return rakeback;
	}

	public void setRakeback(int rakeback) {
		this.rakeback = rakeback;
	}

	public String getLimitType() {
		return limitType;
	}

	public void setLimitType(String limitType) {
		this.limitType = limitType;
	}

	public String getChip() {
		return chip;
	}

	public void setChip(String chip) {
		this.chip = chip;
	}

	public int getSyslang() {
		return syslang;
	}

	public void setSyslang(int syslang) {
		this.syslang = syslang;
	}

}
