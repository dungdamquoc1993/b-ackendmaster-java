package com.vinplay.dto.ibc2;

import com.vinplay.dto.ibc2.common.CheckFundTransferDataRespDto;

import java.io.Serializable;


public class CheckFundTransferRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = 3076082214705547400L;
	private CheckFundTransferDataRespDto Data;

	public CheckFundTransferDataRespDto getData() {
		return Data;
	}

	public void setData(CheckFundTransferDataRespDto data) {
		Data = data;
	}
	
}
