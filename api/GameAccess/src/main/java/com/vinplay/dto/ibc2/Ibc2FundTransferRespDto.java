package com.vinplay.dto.ibc2;

import com.vinplay.dto.ibc2.common.FundTransferDataRespDto;

import java.io.Serializable;


public class Ibc2FundTransferRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = -7127321360787699024L;
	private FundTransferDataRespDto Data;

	public FundTransferDataRespDto getData() {
		return Data;
	}

	public void setData(FundTransferDataRespDto data) {
		Data = data;
	}

}
