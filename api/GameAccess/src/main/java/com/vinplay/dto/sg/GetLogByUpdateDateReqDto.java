package com.vinplay.dto.sg;


@SuppressWarnings("serial")
public class GetLogByUpdateDateReqDto extends BaseSGRequest{
	private String timeFrom;
	private int status;
	private String platform;
	private String currency;
	
	
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getTimeFrom() {
		return timeFrom;
	}
	public void setTimeFrom(String timeFrom) {
		this.timeFrom = timeFrom;
	}
	
	
}
