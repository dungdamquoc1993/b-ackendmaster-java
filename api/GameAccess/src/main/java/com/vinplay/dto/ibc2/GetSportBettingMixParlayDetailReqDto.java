package com.vinplay.dto.ibc2;

import com.vinplay.dto.ibc.BaseReqDto;

import java.io.Serializable;

public class GetSportBettingMixParlayDetailReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = -151870635541137244L;
	private String RefNo;

	public String getRefNo() {
		return RefNo;
	}

	public void setRefNo(String refNo) {
		RefNo = refNo;
	}


	
	
}
