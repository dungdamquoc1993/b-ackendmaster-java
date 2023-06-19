package com.vinplay.dto.ibc;

import java.io.Serializable;
import java.util.List;

public class CheckUserBalanceRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = 6780917398575712697L;
	private String IsCashOut;

	private List<CheckUserBalanceDetailsRespDto> Data;
	public String getIsCashOut() {
		return IsCashOut;
	}
	public void setIsCashOut(String isCashOut) {
		IsCashOut = isCashOut;
	}

	public List<CheckUserBalanceDetailsRespDto> getData() {
		return Data;
	}
	public void setData(List<CheckUserBalanceDetailsRespDto> data) {
		Data = data;
	}
	
}
