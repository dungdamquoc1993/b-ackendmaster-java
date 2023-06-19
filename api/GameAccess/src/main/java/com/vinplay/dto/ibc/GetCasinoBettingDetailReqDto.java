package com.vinplay.dto.ibc;

import java.io.Serializable;
import java.util.Date;

public class GetCasinoBettingDetailReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = -1429925970985786014L;
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
