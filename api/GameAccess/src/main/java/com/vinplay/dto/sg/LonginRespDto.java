package com.vinplay.dto.sg;

import java.io.Serializable;

public class LonginRespDto extends BaseRespDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
