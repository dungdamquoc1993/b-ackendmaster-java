package com.vinplay.dto.ibc;

import java.io.Serializable;

public class GetSportBetLogReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = 5739914799891676159L;
	private Long LastVersionKey;
	private String Lang;
	public Long getLastVersionKey() {
		return LastVersionKey;
	}
	public void setLastVersionKey(Long lastVersionKey) {
		LastVersionKey = lastVersionKey;
	}
	public String getLang() {
		return Lang;
	}
	public void setLang(String lang) {
		Lang = lang;
	}

	
	
}
