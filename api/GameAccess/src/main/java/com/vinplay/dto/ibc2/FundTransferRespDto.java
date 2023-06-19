package com.vinplay.dto.ibc2;

import com.vinplay.dto.ibc2.common.FundTransferDataRespDto;

import java.io.Serializable;


public class FundTransferRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = 1260956735022335777L;
	private FundTransferDataRespDto Data;

	public FundTransferDataRespDto getData() {
		return Data;
	}

	public void setData(FundTransferDataRespDto data) {
		Data = data;
	}

}
