package com.vinplay.item;

import java.io.Serializable;

public class WMGameRecordItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6144820116891162639L;

	private Long betid;
	private String loginname;
	private String user;
	private String bettime;
	private String settime;
	private String rootbettime;
	private String rootsettime;
	private Double bet;
	private Double validbet;
	private Double winloss;
	private Double water;
	private String waterbet;
	private Double result;
	private String betcode;
	private String betresult;
	private Integer gameid;
	private String ip;
	private Integer event;
	private Integer eventchild;
	private Integer tableid;
	private String gameresult;
	private String gamename;
	private Integer commission;
	private String reset;

	public Long getBetid() {
		return betid;
	}

	public void setBetid(Long betid) {
		this.betid = betid;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getBettime() {
		return bettime;
	}

	public void setBettime(String bettime) {
		this.bettime = bettime;
	}

	public String getSettime() {
		return settime;
	}

	public void setSettime(String settime) {
		this.settime = settime;
	}

	public Double getBet() {
		return bet;
	}

	public void setBet(Double bet) {
		this.bet = bet;
	}

	public Double getValidbet() {
		return validbet;
	}

	public void setValidbet(Double validbet) {
		this.validbet = validbet;
	}

	public Double getWinloss() {
		return winloss;
	}

	public void setWinloss(Double winloss) {
		this.winloss = winloss;
	}

	public Double getWater() {
		return water;
	}

	public void setWater(Double water) {
		this.water = water;
	}

	public String getWaterbet() {
		return waterbet;
	}

	public void setWaterbet(String waterbet) {
		this.waterbet = waterbet;
	}

	public Double getResult() {
		return result;
	}

	public void setResult(Double result) {
		this.result = result;
	}

	public String getBetcode() {
		return betcode;
	}

	public void setBetcode(String betcode) {
		this.betcode = betcode;
	}

	public String getBetresult() {
		return betresult;
	}

	public void setBetresult(String betresult) {
		this.betresult = betresult;
	}

	public Integer getGameid() {
		return gameid;
	}

	public void setGameid(Integer gameid) {
		this.gameid = gameid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getEvent() {
		return event;
	}

	public void setEvent(Integer event) {
		this.event = event;
	}

	public Integer getEventchild() {
		return eventchild;
	}

	public void setEventchild(Integer eventchild) {
		this.eventchild = eventchild;
	}

	public Integer getTableid() {
		return tableid;
	}

	public void setTableid(Integer tableid) {
		this.tableid = tableid;
	}

	public String getGameresult() {
		return gameresult;
	}

	public void setGameresult(String gameresult) {
		this.gameresult = gameresult;
	}

	public String getGamename() {
		return gamename;
	}

	public void setGamename(String gamename) {
		this.gamename = gamename;
	}

	public Integer getCommission() {
		return commission;
	}

	public void setCommission(Integer commission) {
		this.commission = commission;
	}

	public String getReset() {
		return reset;
	}

	public void setReset(String reset) {
		this.reset = reset;
	}

	public String getRootbettime() {
		return rootbettime;
	}

	public void setRootbettime(String rootbettime) {
		this.rootbettime = rootbettime;
	}

	public String getRootsettime() {
		return rootsettime;
	}

	public void setRootsettime(String rootsettime) {
		this.rootsettime = rootsettime;
	}

}
