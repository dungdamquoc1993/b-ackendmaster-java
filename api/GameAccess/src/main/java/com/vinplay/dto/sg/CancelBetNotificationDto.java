package com.vinplay.dto.sg;

import java.io.Serializable;

public class CancelBetNotificationDto extends BaseSGRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String platform;
	private String platformTxIds;
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getPlatformTxIds() {
		return platformTxIds;
	}
	public void setPlatformTxIds(String platformTxIds) {
		this.platformTxIds = platformTxIds;
	}
	
}
