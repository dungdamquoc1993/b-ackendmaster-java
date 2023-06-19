package com.vinplay.dto.ibc;

import java.io.Serializable;

public class CheckFundTransferRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = -5583977881744931220L;
	private CheckFundTransferDetailsRespDto Data;

	public CheckFundTransferDetailsRespDto getData() {
		return Data;
	}

	public void setData(CheckFundTransferDetailsRespDto data) {
		Data = data;
	}
	
}
