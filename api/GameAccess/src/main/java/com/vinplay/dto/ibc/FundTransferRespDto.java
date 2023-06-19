package com.vinplay.dto.ibc;

import java.io.Serializable;

public class FundTransferRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = 1850247798518821230L;
	private FundTransferDetailsRespDto Data;

	public FundTransferDetailsRespDto getData() {
		return Data;
	}

	public void setData(FundTransferDetailsRespDto data) {
		Data = data;
	}

}
