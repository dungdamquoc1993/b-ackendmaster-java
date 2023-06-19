package com.vinplay.dto.sg;

@SuppressWarnings("serial")
public class LoginDto extends BaseSGRequest{
	private String userId;
	private String isMobileLogin;
	private String externalURL;
	private String gameForbidden;
	private String gameType;
	private String platform;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getIsMobileLogin() {
		return isMobileLogin;
	}
	public void setIsMobileLogin(String isMobileLogin) {
		this.isMobileLogin = isMobileLogin;
	}
	public String getExternalURL() {
		return externalURL;
	}
	public void setExternalURL(String externalURL) {
		this.externalURL = externalURL;
	}
	public String getGameForbidden() {
		return gameForbidden;
	}
	public void setGameForbidden(String gameForbidden) {
		this.gameForbidden = gameForbidden;
	}
	public String getGameType() {
		return gameType;
	}
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
}
