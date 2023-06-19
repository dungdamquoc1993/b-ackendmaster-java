package com.vinplay.dto.sg;

import java.io.Serializable;

public class LoginAndLaunchGame extends BaseSGRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String userId;
	private String gameCode;
	private String gameType;
	private String platform;
	private String isMobileLogin;
	private String externalURL;
	private String gameForbidden;
	private String language;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGameCode() {
		return gameCode;
	}
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
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
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	

}
