package com.vinplay.dto.ibc;

import java.io.Serializable;

public class GetSportBettingMixParlayDetailReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = 7373734548930975024L;
	private String RefNo;

	public String getRefNo() {
		return RefNo;
	}

	public void setRefNo(String refNo) {
		RefNo = refNo;
	}


	
	
}
