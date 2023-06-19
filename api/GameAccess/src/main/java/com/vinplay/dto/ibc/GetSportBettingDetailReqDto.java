package com.vinplay.dto.ibc;

import java.io.Serializable;
import java.util.Date;

public class GetSportBettingDetailReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = 6552240354068025709L;
	private Date StartTime;
	private Date EndTime;
	private Integer rMode;
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
	public Integer getrMode() {
		return rMode;
	}
	public void setrMode(Integer rMode) {
		this.rMode = rMode;
	}
	
	

	
	
}
