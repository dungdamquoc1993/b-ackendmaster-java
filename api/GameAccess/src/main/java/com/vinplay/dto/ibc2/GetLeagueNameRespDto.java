package com.vinplay.dto.ibc2;

import com.vinplay.dto.ibc2.common.GetLeagueNameDataRespDto;

import java.io.Serializable;


public class GetLeagueNameRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = 4331396300149668153L;
	private GetLeagueNameDataRespDto Data;

	public GetLeagueNameDataRespDto getData() {
		return Data;
	}

	public void setData(GetLeagueNameDataRespDto data) {
		Data = data;
	}
}
