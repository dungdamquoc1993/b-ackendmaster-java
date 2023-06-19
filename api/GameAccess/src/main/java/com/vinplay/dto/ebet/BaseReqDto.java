package com.vinplay.dto.ebet;

import java.io.Serializable;

public class BaseReqDto implements Serializable {
	private Integer channelId;
	public Integer getChannelId() {
		return channelId;
	}
	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
}
