package com.vinplay.dto.ibc;

public class BaseRespDto {
	private Integer error_code;
	private String message;

	public Integer getError_code() {
		return error_code;
	}
	public void setError_code(Integer error_code) {
		this.error_code = error_code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
