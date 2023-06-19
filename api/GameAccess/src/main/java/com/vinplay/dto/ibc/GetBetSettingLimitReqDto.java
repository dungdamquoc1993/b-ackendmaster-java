package com.vinplay.dto.ibc;

import java.io.Serializable;

public class GetBetSettingLimitReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = -854835904153422708L;
	private Integer Currency;

	public Integer getCurrency() {
		return Currency;
	}

	public void setCurrency(Integer currency) {
		Currency = currency;
	}


	
	
}
