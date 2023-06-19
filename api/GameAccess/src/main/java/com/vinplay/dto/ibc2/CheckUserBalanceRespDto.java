package com.vinplay.dto.ibc2;

import java.io.Serializable;
import java.util.List;

import com.vinplay.dto.ibc2.common.CheckBalanceDataRespDto;


public class CheckUserBalanceRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = 3352445800714589129L;
	private List<CheckBalanceDataRespDto> Data;

	public List<CheckBalanceDataRespDto> getData() {
		return Data;
	}
	public void setData(List<CheckBalanceDataRespDto> data) {
		Data = data;
	}
	
}
