package com.vinplay.dto.wm;

import java.io.Serializable;

public class BaseResponseDto<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2361632944493595589L;
	private int errorCode;
	private String errorMessage;
	private T result;

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

}
