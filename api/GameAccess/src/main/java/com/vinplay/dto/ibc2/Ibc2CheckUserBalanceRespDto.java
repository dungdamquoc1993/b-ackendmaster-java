package com.vinplay.dto.ibc2;

import java.io.Serializable;
import java.util.List;

import com.vinplay.dto.ibc2.common.CheckBalanceDataRespDto;


public class Ibc2CheckUserBalanceRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = 1546858068930290091L;
	private List<CheckBalanceDataRespDto> Data;

	public List<CheckBalanceDataRespDto> getData() {
		return Data;
	}
	public void setData(List<CheckBalanceDataRespDto> data) {
		Data = data;
	}
	
}
