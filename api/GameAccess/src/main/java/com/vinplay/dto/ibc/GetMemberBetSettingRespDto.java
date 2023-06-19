package com.vinplay.dto.ibc;
import java.io.Serializable;
import java.util.List;

import com.vinplay.dto.ibc.common.BetSettingDataRespDto;
public class GetMemberBetSettingRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = -352821185746651365L;
	List<BetSettingDataRespDto> Data;

	public List<BetSettingDataRespDto> getData() {
		return Data;
	}

	public void setData(List<BetSettingDataRespDto> data) {
		Data = data;
	}
	
}
