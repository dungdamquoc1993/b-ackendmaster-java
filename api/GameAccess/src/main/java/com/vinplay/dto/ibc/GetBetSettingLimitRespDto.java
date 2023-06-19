package com.vinplay.dto.ibc;
import java.io.Serializable;
import java.util.*;

import com.vinplay.dto.ibc.common.BetSettingDataRespDto;
public class GetBetSettingLimitRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = -7866781675753970428L;
	List<BetSettingDataRespDto> Data;

	public List<BetSettingDataRespDto> getData() {
		return Data;
	}

	public void setData(List<BetSettingDataRespDto> data) {
		Data = data;
	}
	
}
