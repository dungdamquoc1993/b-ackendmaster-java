package com.vinplay.dto.ibc2;


import java.io.Serializable;

public class GetOnlineUserCountRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = 2933018399082840022L;
	private String Data;

	public String getData() {
		return Data;
	}

	public void setData(String data) {
		Data = data;
	}

}


