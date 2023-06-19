package com.vinplay.dto.ibc2;

import com.vinplay.dto.ibc.BaseRespDto;

import java.io.Serializable;
import java.util.List;

public class GetCasinoBetLogRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = -5232416504329604969L;
	private String LastVersionKey;
	private Integer TotalRecord;
	private List<GetCasinoBetLogDetailsRespDto> Data;
	public String getLastVersionKey() {
		return LastVersionKey;
	}
	public void setLastVersionKey(String lastVersionKey) {
		LastVersionKey = lastVersionKey;
	}
	public Integer getTotalRecord() {
		return TotalRecord;
	}
	public void setTotalRecord(Integer totalRecord) {
		TotalRecord = totalRecord;
	}
	public List<GetCasinoBetLogDetailsRespDto> getData() {
		return Data;
	}
	public void setData(List<GetCasinoBetLogDetailsRespDto> data) {
		Data = data;
	}

	
}
