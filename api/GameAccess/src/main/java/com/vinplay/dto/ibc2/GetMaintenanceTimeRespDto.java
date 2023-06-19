package com.vinplay.dto.ibc2;


import java.io.Serializable;

public class GetMaintenanceTimeRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = -1221434527824480821L;
	private String Data;

	public String getData() {
		return Data;
	}

	public void setData(String data) {
		Data = data;
	}

}


