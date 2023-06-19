package com.vinplay.dto.ibc2;

import com.vinplay.dto.ibc.BaseRespDto;
import com.vinplay.dto.ibc.common.BetSettingDataRespDto;

import java.io.Serializable;
import java.util.List;

public class GetMemberBetSettingRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = 7931379128928090419L;
	List<BetSettingDataRespDto> Data;

	public List<BetSettingDataRespDto> getData() {
		return Data;
	}

	public void setData(List<BetSettingDataRespDto> data) {
		Data = data;
	}
	
}
