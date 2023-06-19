package com.vinplay.dto.ibc2;

import com.vinplay.dto.ibc2.common.GetTeamNameDataRespDto;

import java.io.Serializable;


public class GetTeamNameRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = -867543194218167134L;
	private GetTeamNameDataRespDto Data;

	public GetTeamNameDataRespDto getData() {
		return Data;
	}

	public void setData(GetTeamNameDataRespDto data) {
		Data = data;
	}
}
