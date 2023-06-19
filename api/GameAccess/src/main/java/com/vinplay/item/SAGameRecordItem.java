package com.vinplay.item;

import java.io.Serializable;

public class SAGameRecordItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9161921910017538872L;

	private String bettime;
	private String payouttime;
	private String username;
	private Integer hostid;
	private String detail;
	private String gameid;
	private Integer round;
	private Integer set;
	private Long betid;
	private Double betamount;
	private Double rolling;
	private Double resultamount;
	private Double balance;
	private String gametype;
	private Integer bettype;
	private Integer betsource;
	private Double transactionid;
	private String state;
	private String loginname;

	// add new
	private String gameTypeStr;
	private String betTypeStr;
	private String hostName;
	private String betSourceStr;
	private String game_result;
	
	public String getGame_result() {
		return game_result;
	}
	public void setGame_result(String game_result) {
		this.game_result = game_result;
	}
	public String getBettime() {
		return bettime;
	}
	public void setBettime(String bettime) {
		this.bettime = bettime;
	}
	public String getPayouttime() {
		return payouttime;
	}
	public void setPayouttime(String payouttime) {
		this.payouttime = payouttime;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getHostid() {
		return hostid;
	}
	public void setHostid(Integer hostid) {
		this.hostid = hostid;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getGameid() {
		return gameid;
	}
	public void setGameid(String gameid) {
		this.gameid = gameid;
	}
	public Integer getRound() {
		return round;
	}
	public void setRound(Integer round) {
		this.round = round;
	}
	public Integer getSet() {
		return set;
	}
	public void setSet(Integer set) {
		this.set = set;
	}
	public Long getBetid() {
		return betid;
	}
	public void setBetid(Long betid) {
		this.betid = betid;
	}
	public Double getBetamount() {
		return betamount;
	}
	public void setBetamount(Double betamount) {
		this.betamount = betamount;
	}
	public Double getRolling() {
		return rolling;
	}
	public void setRolling(Double rolling) {
		this.rolling = rolling;
	}
	public Double getResultamount() {
		return resultamount;
	}
	public void setResultamount(Double resultamount) {
		this.resultamount = resultamount;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public String getGametype() {
		return gametype;
	}
	public void setGametype(String gametype) {
		this.gametype = gametype;
	}
	public Integer getBettype() {
		return bettype;
	}
	public void setBettype(Integer bettype) {
		this.bettype = bettype;
	}
	public Integer getBetsource() {
		return betsource;
	}
	public void setBetsource(Integer betsource) {
		this.betsource = betsource;
	}
	public Double getTransactionid() {
		return transactionid;
	}
	public void setTransactionid(Double transactionid) {
		this.transactionid = transactionid;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getGameTypeStr() {
		return gameTypeStr;
	}
	public void setGameTypeStr(String gameTypeStr) {
		this.gameTypeStr = gameTypeStr;
	}
	public String getBetTypeStr() {
		return betTypeStr;
	}
	public void setBetTypeStr(String betTypeStr) {
		this.betTypeStr = betTypeStr;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getBetSourceStr() {
		return betSourceStr;
	}
	public void setBetSourceStr(String betSourceStr) {
		this.betSourceStr = betSourceStr;
	}
	

}
