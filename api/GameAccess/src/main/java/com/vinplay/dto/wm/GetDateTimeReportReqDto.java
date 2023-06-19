package com.vinplay.dto.wm;

public class GetDateTimeReportReqDto extends BaseReqDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4154950002713027488L;
	
	private String user; // Optional
	private Long startTime;
	private Long endTime;
	private Integer syslang;
	private Integer timetype;
	private Integer datatype;
	private Integer gameno1; // Optional
	private Integer gameno2; // Optional

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Integer getSyslang() {
		return syslang;
	}

	public void setSyslang(Integer syslang) {
		this.syslang = syslang;
	}

	public Integer getTimetype() {
		return timetype;
	}

	public void setTimetype(Integer timetype) {
		this.timetype = timetype;
	}

	public Integer getDatatype() {
		return datatype;
	}

	public void setDatatype(Integer datatype) {
		this.datatype = datatype;
	}

	public Integer getGameno1() {
		return gameno1;
	}

	public void setGameno1(Integer gameno1) {
		this.gameno1 = gameno1;
	}

	public Integer getGameno2() {
		return gameno2;
	}

	public void setGameno2(Integer gameno2) {
		this.gameno2 = gameno2;
	}
	
}
