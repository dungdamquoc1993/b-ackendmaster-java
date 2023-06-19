/**
 * Archie
 */
package com.vinplay.item;

import java.io.Serializable;

/**
 * @author Archie
 *
 */

public class SboRecord implements Serializable {

	private Double actualstake;
	private String bet_id;
	private String currency;
	private String ip;
	private String ishalfwonlose;
	private String islive;
	private Double maxwinwithoutactualstake;
	private String modifydate;
	private String modify_at;
	private Double odds;
	private String oddsstyle;
	private String ordertime;
	private String player_name;
	private String sporttype;
	private Double stake;
	private String status;
	private Double turnover;
	private Double winlose;
	private String winlostdate;
	private String loginname;

	private String betoption = "";
	private String markettype = "";
	private Double hdp = 0d;
	private String league = "";
	private String match = "";
	private String livescore = "";
	private String htscore = "";
	private String ftscore = "";
	private Double payout;
	
	public Double getPayout() {
		return payout;
	}

	public void setPayout(Double payout) {
		this.payout = payout;
	}

	public Double getHdp() {
		return hdp;
	}

	public void setHdp(Double hdp) {
		this.hdp = hdp;
	}

	public String getLeague() {
		return league;
	}

	public void setLeague(String league) {
		this.league = league;
	}

	public String getMatch() {
		return match;
	}

	public void setMatch(String match) {
		this.match = match;
	}

	public Double getActualstake() {
		return actualstake;
	}

	public void setActualstake(Double actualstake) {
		this.actualstake = actualstake;
	}

	public String getBet_id() {
		return bet_id;
	}

	public void setBet_id(String bet_id) {
		this.bet_id = bet_id;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIshalfwonlose() {
		return ishalfwonlose;
	}

	public void setIshalfwonlose(String ishalfwonlose) {
		this.ishalfwonlose = ishalfwonlose;
	}

	public String getIslive() {
		return islive;
	}

	public void setIslive(String islive) {
		this.islive = islive;
	}

	public Double getMaxwinwithoutactualstake() {
		return maxwinwithoutactualstake;
	}

	public void setMaxwinwithoutactualstake(Double maxwinwithoutactualstake) {
		this.maxwinwithoutactualstake = maxwinwithoutactualstake;
	}

	public String getModifydate() {
		return modifydate;
	}

	public void setModifydate(String modifydate) {
		this.modifydate = modifydate;
	}

	public String getModify_at() {
		return modify_at;
	}

	public void setModify_at(String modify_at) {
		this.modify_at = modify_at;
	}

	public Double getOdds() {
		return odds;
	}

	public void setOdds(Double odds) {
		this.odds = odds;
	}

	public String getOddsstyle() {
		return oddsstyle;
	}

	public void setOddsstyle(String oddsstyle) {
		this.oddsstyle = oddsstyle;
	}

	public String getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}

	public String getPlayer_name() {
		return player_name;
	}

	public void setPlayer_name(String player_name) {
		this.player_name = player_name;
	}

	public String getSporttype() {
		return sporttype;
	}

	public void setSporttype(String sporttype) {
		this.sporttype = sporttype;
	}

	public Double getStake() {
		return stake;
	}

	public void setStake(Double stake) {
		this.stake = stake;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getTurnover() {
		return turnover;
	}

	public void setTurnover(Double turnover) {
		this.turnover = turnover;
	}

	public Double getWinlose() {
		return winlose;
	}

	public void setWinlose(Double winlose) {
		this.winlose = winlose;
	}

	public String getWinlostdate() {
		return winlostdate;
	}

	public void setWinlostdate(String winlostdate) {
		this.winlostdate = winlostdate;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getBetoption() {
		return betoption;
	}

	public void setBetoption(String betoption) {
		this.betoption = betoption;
	}

	public String getMarkettype() {
		return markettype;
	}

	public void setMarkettype(String markettype) {
		this.markettype = markettype;
	}

	public String getLivescore() {
		return livescore;
	}

	public void setLivescore(String livescore) {
		this.livescore = livescore;
	}

	public String getHtscore() {
		return htscore;
	}

	public void setHtscore(String htscore) {
		this.htscore = htscore;
	}

	public String getFtscore() {
		return ftscore;
	}

	public void setFtscore(String ftscore) {
		this.ftscore = ftscore;
	}

	@Override
	public String toString() {
		return "SboRecord [actualstake=" + actualstake + ", bet_id=" + bet_id + ", currency=" + currency + ", ip=" + ip
				+ ", ishalfwonlose=" + ishalfwonlose + ", islive=" + islive + ", maxwinwithoutactualstake="
				+ maxwinwithoutactualstake + ", modifydate=" + modifydate + ", modify_at=" + modify_at + ", odds="
				+ odds + ", oddsstyle=" + oddsstyle + ", ordertime=" + ordertime + ", player_name=" + player_name
				+ ", sporttype=" + sporttype + ", stake=" + stake + ", status=" + status + ", turnover=" + turnover
				+ ", winlose=" + winlose + ", winlostdate=" + winlostdate + ", loginname=" + loginname + ", betoption="
				+ betoption + ", markettype=" + markettype + ", hdp=" + hdp + ", league=" + league + ", match=" + match
				+ ", livescore=" + livescore + ", htscore=" + htscore + ", ftscore=" + ftscore + ", payout=" + payout
				+ "]";
	}

}
