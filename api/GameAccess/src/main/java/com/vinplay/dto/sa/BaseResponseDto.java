package com.vinplay.dto.sa;

import java.io.Serializable;

public class BaseResponseDto<T> implements Serializable {

	/**
	 * 
	 */

	private String ErrorMsgId;

	private String ErrorMsg;

	public String getErrorMsgId() {
		return ErrorMsgId;
	}

	public void setErrorMsgId(String errorMsgId) {
		ErrorMsgId = errorMsgId;
	}

	public String getErrorMsg() {
		return ErrorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		ErrorMsg = errorMsg;
	}
	
	

}
