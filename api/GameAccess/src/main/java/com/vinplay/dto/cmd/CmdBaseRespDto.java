package com.vinplay.dto.cmd;

import java.io.Serializable;

public class CmdBaseRespDto<T> implements Serializable {

	private static final long serialVersionUID = -5395005739687121791L;

	private String SerialKey;
	private long Timestamp;
	private int code;
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

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
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
