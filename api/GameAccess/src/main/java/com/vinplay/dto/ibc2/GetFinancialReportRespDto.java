package com.vinplay.dto.ibc2;

import java.io.Serializable;
import java.util.List;
import com.vinplay.dto.ibc2.common.CheckIsOnlineDataRespDto;


public class GetFinancialReportRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = -1540144757482062140L;
	private List<CheckIsOnlineDataRespDto> Data;

	public List<CheckIsOnlineDataRespDto> getData() {
		return Data;
	}

	public void setData(List<CheckIsOnlineDataRespDto> data) {
		Data = data;
	}

}


