package com.vinplay.dto.ibc2;

import com.vinplay.dto.ibc.BaseReqDto;

import java.io.Serializable;
import java.util.Date;

public class GetCasinoBettingDetailReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = -5660870551819837015L;
	private Date StartTime;
	private Date EndTime;
	public Date getStartTime() {
		return StartTime;
	}
	public void setStartTime(Date startTime) {
		StartTime = startTime;
	}
	public Date getEndTime() {
		return EndTime;
	}
	public void setEndTime(Date endTime) {
		EndTime = endTime;
	}



}
