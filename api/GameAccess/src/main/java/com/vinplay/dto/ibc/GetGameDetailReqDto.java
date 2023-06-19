package com.vinplay.dto.ibc;

import java.io.Serializable;

public class GetGameDetailReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = 8738839996928270349L;
	private String Match_Id;

	public String getMatch_Id() {
		return Match_Id;
	}

	public void setMatch_Id(String match_Id) {
		Match_Id = match_Id;
	}



	
	
}
