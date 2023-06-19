package com.vinplay.dto.ibc2;


import java.io.Serializable;

public class GetReportTokenRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = 2652171956022074901L;
	private String Data;

	public String getData() {
		return Data;
	}

	public void setData(String data) {
		Data = data;
	} 

}


