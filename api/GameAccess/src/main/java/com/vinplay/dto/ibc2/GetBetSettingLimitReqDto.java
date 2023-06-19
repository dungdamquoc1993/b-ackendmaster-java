package com.vinplay.dto.ibc2;

import com.vinplay.dto.ibc.BaseReqDto;

import java.io.Serializable;

public class GetBetSettingLimitReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = 8661105998458202207L;
	private Integer Currency;

	public Integer getCurrency() {
		return Currency;
	}

	public void setCurrency(Integer currency) {
		Currency = currency;
	}


	
	
}
