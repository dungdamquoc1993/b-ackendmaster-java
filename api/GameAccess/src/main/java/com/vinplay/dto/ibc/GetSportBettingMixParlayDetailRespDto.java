package com.vinplay.dto.ibc;
import java.io.Serializable;
import java.util.*;

import com.vinplay.dto.ibc.common.ParlayDataRespDto;
public class GetSportBettingMixParlayDetailRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = -4106685209697953637L;
	List<ParlayDataRespDto> Data;

	public List<ParlayDataRespDto> getData() {
		return Data;
	}

	public void setData(List<ParlayDataRespDto> data) {
		Data = data;
	}
	
}
