package com.vinplay.dto.ibc2;

import com.vinplay.dto.ibc.BaseReqDto;

import java.io.Serializable;

public class GetSportBetLogReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = 1666606055394097223L;
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
