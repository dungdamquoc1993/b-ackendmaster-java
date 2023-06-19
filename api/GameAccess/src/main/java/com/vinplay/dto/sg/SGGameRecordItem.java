package com.vinplay.dto.sg;

import java.io.Serializable;

public class SGGameRecordItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String userid;
	private String loginname;
	private Double betamt;
	private Double winamt;
	private Double turnover;
	private String txformattime;
	private String formatupdatetime;
	private String platformtxid;
	private String platform;
	private String roundid;
	private String gamecode;
	private String gametype;
	private String bettype;
	private String txtime;
	private Integer txstatus;
	private Double realbetamt;
	private Double realwinamt;
	private Double jackpotbetamt;
	private Double jackpotwinamt;
	private String currency;
	private Integer comm;
	private String createtime;
	private String updatetime;
	private String bizdate;
	private String gameinfo;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public Double getBetamt() {
		return betamt;
	}
	public void setBetamt(Double betamt) {
		this.betamt = betamt;
	}
	public Double getWinamt() {
		return winamt;
	}
	public void setWinamt(Double winamt) {
		this.winamt = winamt;
	}
	public Double getTurnover() {
		return turnover;
	}
	public void setTurnover(Double turnover) {
		this.turnover = turnover;
	}
	public String getTxformattime() {
		return txformattime;
	}
	public void setTxformattime(String txformattime) {
		this.txformattime = txformattime;
	}
	public String getFormatupdatetime() {
		return formatupdatetime;
	}
	public void setFormatupdatetime(String formatupdatetime) {
		this.formatupdatetime = formatupdatetime;
	}
	public String getPlatformtxid() {
		return platformtxid;
	}
	public void setPlatformtxid(String platformtxid) {
		this.platformtxid = platformtxid;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getRoundid() {
		return roundid;
	}
	public void setRoundid(String roundid) {
		this.roundid = roundid;
	}
	public String getGamecode() {
		return gamecode;
	}
	public void setGamecode(String gamecode) {
		this.gamecode = gamecode;
	}
	public String getGametype() {
		return gametype;
	}
	public void setGametype(String gametype) {
		this.gametype = gametype;
	}
	public String getBettype() {
		return bettype;
	}
	public void setBettype(String bettype) {
		this.bettype = bettype;
	}
	public String getTxtime() {
		return txtime;
	}
	public void setTxtime(String txtime) {
		this.txtime = txtime;
	}
	
	public Integer getTxstatus() {
		return txstatus;
	}
	public void setTxstatus(Integer txstatus) {
		this.txstatus = txstatus;
	}
	public Double getRealbetamt() {
		return realbetamt;
	}
	public void setRealbetamt(Double realbetamt) {
		this.realbetamt = realbetamt;
	}
	public Double getRealwinamt() {
		return realwinamt;
	}
	public void setRealwinamt(Double realwinamt) {
		this.realwinamt = realwinamt;
	}
	public Double getJackpotbetamt() {
		return jackpotbetamt;
	}
	public void setJackpotbetamt(Double jackpotbetamt) {
		this.jackpotbetamt = jackpotbetamt;
	}
	public Double getJackpotwinamt() {
		return jackpotwinamt;
	}
	public void setJackpotwinamt(Double jackpotwinamt) {
		this.jackpotwinamt = jackpotwinamt;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Integer getComm() {
		return comm;
	}
	public void setComm(Integer comm) {
		this.comm = comm;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getBizdate() {
		return bizdate;
	}
	public void setBizdate(String bizdate) {
		this.bizdate = bizdate;
	}
	public String getGameinfo() {
		return gameinfo;
	}
	public void setGameinfo(String gameinfo) {
		this.gameinfo = gameinfo;
	}
}
