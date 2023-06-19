package com.vinplay.dto.ibc;

import java.io.Serializable;

public class GetCasinoBetLogReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = 2582604671675638215L;
	private Long LastVersionKey;

	public Long getLastVersionKey() {
		return LastVersionKey;
	}

	public void setLastVersionKey(Long lastVersionKey) {
		LastVersionKey = lastVersionKey;
	}

}
