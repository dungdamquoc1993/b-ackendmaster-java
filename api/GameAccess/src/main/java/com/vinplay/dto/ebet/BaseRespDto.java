package com.vinplay.dto.ebet;

import java.io.Serializable;

public class BaseRespDto implements Serializable {
	
	private String apiVersion;
	private Integer status;
	public String getApiVersion() {
		return apiVersion;
	}
	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
