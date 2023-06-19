package com.vinplay.item;

import java.io.Serializable;

public class BetHistoriesStrDto implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer gametype;
	private String judgeresult;
	private String roundno;
	private Double bet;
	private Double payout;
	private String bankercards;
	private String playercards;
	private String alldices;
	private Integer dragoncard;
	private Integer tigercard;
	private Integer ebetnumber;
	private String createtime;
	private String payouttime;
	private String bethistoryid;
	private Double validbet;
	private String username;
	private Integer userid;
	private String gamename;
	private Integer platform;
	private String vncreatetime;
	private String vnpayouttime;
	
	public Integer getGametype() {
		return gametype;
	}
	public void setGametype(Integer gametype) {
		this.gametype = gametype;
	}
	public String getJudgeresult() {
		return judgeresult;
	}
	public void setJudgeresult(String judgeresult) {
		this.judgeresult = judgeresult;
	}
	public String getRoundno() {
		return roundno;
	}
	public void setRoundno(String roundno) {
		this.roundno = roundno;
	}
	public Double getBet() {
		return bet;
	}
	public void setBet(Double bet) {
		this.bet = bet;
	}
	public Double getPayout() {
		return payout;
	}
	public void setPayout(Double payout) {
		this.payout = payout;
	}
	public String getBankercards() {
		return bankercards;
	}
	public void setBankercards(String bankercards) {
		this.bankercards = bankercards;
	}
	public String getPlayercards() {
		return playercards;
	}
	public void setPlayercards(String playercards) {
		this.playercards = playercards;
	}
	public String getAlldices() {
		return alldices;
	}
	public void setAlldices(String alldices) {
		this.alldices = alldices;
	}
	public Integer getDragoncard() {
		return dragoncard;
	}
	public void setDragoncard(Integer dragoncard) {
		this.dragoncard = dragoncard;
	}
	public Integer getTigercard() {
		return tigercard;
	}
	public void setTigercard(Integer tigercard) {
		this.tigercard = tigercard;
	}
	public Integer getEbetnumber() {
		return ebetnumber;
	}
	public void setEbetnumber(Integer ebetnumber) {
		this.ebetnumber = ebetnumber;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getPayouttime() {
		return payouttime;
	}
	public void setPayouttime(String payouttime) {
		this.payouttime = payouttime;
	}
	public String getBethistoryid() {
		return bethistoryid;
	}
	public void setBethistoryid(String bethistoryid) {
		this.bethistoryid = bethistoryid;
	}
	public Double getValidbet() {
		return validbet;
	}
	public void setValidbet(Double validbet) {
		this.validbet = validbet;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getGamename() {
		return gamename;
	}
	public void setGamename(String gamename) {
		this.gamename = gamename;
	}
	public Integer getPlatform() {
		return platform;
	}
	public void setPlatform(Integer platform) {
		this.platform = platform;
	}
	public String getVncreatetime() {
		return vncreatetime;
	}
	public void setVncreatetime(String vncreatetime) {
		this.vncreatetime = vncreatetime;
	}
	public String getVnpayouttime() {
		return vnpayouttime;
	}
	public void setVnpayouttime(String vnpayouttime) {
		this.vnpayouttime = vnpayouttime;
	}
	
}
