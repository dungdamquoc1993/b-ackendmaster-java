package com.vinplay.dto.ibc2;

import com.vinplay.dto.ibc.BaseRespDto;
import com.vinplay.dto.ibc.common.DataItemRespDto;

import java.io.Serializable;
import java.util.List;

public class GetSportBettingDetailRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = -2257480584646860497L;
	private List<DataItemRespDto> Data;

	public List<DataItemRespDto> getData() {
		return Data;
	}

	public void setData(List<DataItemRespDto> data) {
		Data = data;
	}
	
	
}
