package com.vinplay.dto.ebet;

import java.io.Serializable;

public class UserBetHistoriReqDto extends BaseReqDto implements Serializable{
	private static final long serialVersionUID = 6161471704686224848L;
	private String username;
	private String startTimeStr;
	private String endTimeStr;
	private Integer subChannelId;
	private Integer betStatus;
	private Integer pageNum;
	private Integer pageSize;
	private Integer timestamp;
	private String signature;
	private Integer gameType;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getStartTimeStr() {
		return startTimeStr;
	}
	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}
	public String getEndTimeStr() {
		return endTimeStr;
	}
	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}
	public Integer getSubChannelId() {
		return subChannelId;
	}
	public void setSubChannelId(Integer subChannelId) {
		this.subChannelId = subChannelId;
	}
	public Integer getBetStatus() {
		return betStatus;
	}
	public void setBetStatus(Integer betStatus) {
		this.betStatus = betStatus;
	}
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Integer timestamp) {
		this.timestamp = timestamp;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public Integer getGameType() {
		return gameType;
	}
	public void setGameType(Integer gameType) {
		this.gameType = gameType;
	}
	
}
