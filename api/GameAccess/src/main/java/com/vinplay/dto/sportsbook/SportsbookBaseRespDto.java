package com.vinplay.dto.sportsbook;

import java.io.Serializable;

public class SportsbookBaseRespDto<T> implements Serializable {

	private static final long serialVersionUID = -5395005739687121791L;

	private String SerialKey;
	private long Timestamp;
	private String Code;
	private String Message;
	private T Data;

	public String getSerialKey() {
		return SerialKey;
	}

	public void setSerialKey(String serialKey) {
		SerialKey = serialKey;
	}

	public long getTimestamp() {
		return Timestamp;
	}

	public void setTimestamp(long timestamp) {
		Timestamp = timestamp;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public T getData() {
		return Data;
	}

	public void setData(T data) {
		Data = data;
	}

}
