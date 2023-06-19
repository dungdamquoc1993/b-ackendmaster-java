package com.vinplay.dto.ibc2;

import com.vinplay.dto.ibc.BaseRespDto;
import com.vinplay.dto.ibc.common.ParlayDataRespDto;

import java.io.Serializable;
import java.util.List;

public class GetSportBettingMixParlayDetailRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = -5731706543577989854L;
	List<ParlayDataRespDto> Data;

	public List<ParlayDataRespDto> getData() {
		return Data;
	}

	public void setData(List<ParlayDataRespDto> data) {
		Data = data;
	}
	
}
