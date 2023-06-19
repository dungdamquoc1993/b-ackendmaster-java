package com.vinplay.dto.ibc;

import java.io.Serializable;
import java.util.List;

import com.vinplay.dto.ibc.common.DataItemRespDto;

public class GetSportBetLogRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = -8631371467136606255L;
	private List<DataItemRespDto> Data;

	public List<DataItemRespDto> getData() {
		return Data;
	}

	public void setData(List<DataItemRespDto> data) {
		Data = data;
	}


	
} 	
