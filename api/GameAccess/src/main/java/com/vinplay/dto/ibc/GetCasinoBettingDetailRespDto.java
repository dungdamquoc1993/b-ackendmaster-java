package com.vinplay.dto.ibc;

import java.io.Serializable;
import java.util.List;

public class GetCasinoBettingDetailRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = -4620705890416309910L;
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
