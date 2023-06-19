package com.vinplay.dto.ebet;

import java.io.Serializable;

public class MaintenanceModeRespDto implements Serializable {
	private static final long serialVersionUID = -999865849845952424L;
	private Boolean Success;
	private String Message;

	public Boolean getSuccess() {
		return Success;
	}

	public void setSuccess(Boolean success) {
		Success = success;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}
}
