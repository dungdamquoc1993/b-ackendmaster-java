package com.vinplay.dto.ibc2;


import java.io.Serializable;

public class LogInRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = 4578700081785031693L;
	private String Data;

	public String getData() {
		return Data;
	}

	public void setData(String data) {
		Data = data;
	}
}


