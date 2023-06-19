package com.vinplay.dto.ibc;

import java.io.Serializable;
import java.util.List;

import com.vinplay.dto.ibc.common.DataItemRespDto;

public class GetSportBettingDetailRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = -6960998537355361763L;
	private List<DataItemRespDto> Data;

	public List<DataItemRespDto> getData() {
		return Data;
	}

	public void setData(List<DataItemRespDto> data) {
		Data = data;
	}
	
	
}
