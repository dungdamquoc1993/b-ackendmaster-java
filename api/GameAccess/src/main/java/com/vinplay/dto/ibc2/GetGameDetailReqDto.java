package com.vinplay.dto.ibc2;

import com.vinplay.dto.ibc.BaseReqDto;

import java.io.Serializable;

public class GetGameDetailReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = 5151462778607213990L;
	private String Match_Id;

	public String getMatch_Id() {
		return Match_Id;
	}

	public void setMatch_Id(String match_Id) {
		Match_Id = match_Id;
	}



	
	
}
