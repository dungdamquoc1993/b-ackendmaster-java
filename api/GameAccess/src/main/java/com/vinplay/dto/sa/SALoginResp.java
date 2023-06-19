package com.vinplay.dto.sa;

import java.io.Serializable;

public class SALoginResp extends BaseResponseDto implements Serializable{
	private static final long serialVersionUID = 344828548938179125L;
	private String token;
	private String displayName;
	private String gameURL;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getGameURL() {
		return gameURL;
	}
	public void setGameURL(String gameURL) {
		this.gameURL = gameURL;
	}
	
	
}
