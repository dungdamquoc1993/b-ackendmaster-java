package com.vinplay.dto.ibc2;

import com.vinplay.dto.ibc.BaseReqDto;

import java.io.Serializable;

public class GetCasinoBetLogReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = 2300043306353684698L;
	private Long LastVersionKey;

	public Long getLastVersionKey() {
		return LastVersionKey;
	}

	public void setLastVersionKey(Long lastVersionKey) {
		LastVersionKey = lastVersionKey;
	}

}
