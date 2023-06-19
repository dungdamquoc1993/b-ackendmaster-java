package com.vinplay.item;

public class IbcGameRecordItem implements java.io.Serializable{
	private static final long serialVersionUID = -5326635076638587958L;
	/**
	 * 
	 */

	private Long transid;
	private String playername;
	private String transactiontime;
	private Long matchid;
	private Integer leagueid;
	
	private String leaguename;
	private Long awayid;
	private String awayidname;
	private Long homeid;
	private String homeidname;
	
	private String matchdatetime;
	private Integer bettype;
	private Long parlayrefno;
	private String betteam;
	private Double hdp;
	
	private String sporttype;
	private Double awayhdp;
	private Double homehdp;
	private Double odds;
	private Double awayscore;
	
	private Double homescore;
	private String islive;
	private String islucky;
	private String parlay_type;
	private String combo_type;
	
	private Double stake;
	private Double actual_stake;
	private String bettag;
	private Double winloseamount;
	private String winlostdatetime;
	private Long versionkey;
	
	private String lastballno;
	private String ticketstatus;
	private Integer oddstype;
	private String settlingtime ;
	
	public Long getTransid() {
		return transid;
	}
	public String getPlayername() {
		return playername;
	}
	public String getTransactiontime() {
		return transactiontime;
	}
	public Long getMatchid() {
		return matchid;
	}
	public Integer getLeagueid() {
		return leagueid;
	}
	public String getLeaguename() {
		return leaguename;
	}
	public Long getAwayid() {
		return awayid;
	}
	public String getAwayidname() {
		return awayidname;
	}
	public Long getHomeid() {
		return homeid;
	}
	public String getHomeidname() {
		return homeidname;
	}
	public String getMatchdatetime() {
		return matchdatetime;
	}
	public Integer getBettype() {
		return bettype;
	}
	public Long getParlayrefno() {
		return parlayrefno;
	}
	public String getBetteam() {
		return betteam;
	}
	public Double getHdp() {
		return hdp;
	}
	public String getSporttype() {
		return sporttype;
	}
	public Double getAwayhdp() {
		return awayhdp;
	}
	public Double getHomehdp() {
		return homehdp;
	}
	public Double getOdds() {
		return odds;
	}
	public Double getAwayscore() {
		return awayscore;
	}
	public Double getHomescore() {
		return homescore;
	}
	public String getIslive() {
		return islive;
	}
	public String getIslucky() {
		return islucky;
	}
	public String getParlay_type() {
		return parlay_type;
	}
	public String getCombo_type() {
		return combo_type;
	}
	public Double getStake() {
		return stake;
	}
	public String getBettag() {
		return bettag;
	}
	public Double getWinloseamount() {
		return winloseamount;
	}
	public String getWinlostdatetime() {
		return winlostdatetime;
	}
	public Long getVersionkey() {
		return versionkey;
	}
	public String getLastballno() {
		return lastballno;
	}
	public String getTicketstatus() {
		return ticketstatus;
	}
	public void setTransid(Long transid) {
		this.transid = transid;
	}
	public void setPlayername(String playername) {
		this.playername = playername;
	}
	public void setTransactiontime(String transactiontime) {
		this.transactiontime = transactiontime;
	}
	public void setMatchid(Long matchid) {
		this.matchid = matchid;
	}
	public void setLeagueid(Integer leagueid) {
		this.leagueid = leagueid;
	}
	public void setLeaguename(String leaguename) {
		this.leaguename = leaguename;
	}
	public void setAwayid(Long awayid) {
		this.awayid = awayid;
	}
	public void setAwayidname(String awayidname) {
		this.awayidname = awayidname;
	}
	public void setHomeid(Long homeid) {
		this.homeid = homeid;
	}
	public void setHomeidname(String homeidname) {
		this.homeidname = homeidname;
	}
	public void setMatchdatetime(String matchdatetime) {
		this.matchdatetime = matchdatetime;
	}
	public void setBettype(Integer bettype) {
		this.bettype = bettype;
	}
	public void setParlayrefno(Long parlayrefno) {
		this.parlayrefno = parlayrefno;
	}
	public void setBetteam(String betteam) {
		this.betteam = betteam;
	}
	public void setHdp(Double hdp) {
		this.hdp = hdp;
	}
	public void setSporttype(String sporttype) {
		this.sporttype = sporttype;
	}
	public void setAwayhdp(Double awayhdp) {
		this.awayhdp = awayhdp;
	}
	public void setHomehdp(Double homehdp) {
		this.homehdp = homehdp;
	}
	public void setOdds(Double odds) {
		this.odds = odds;
	}
	public void setAwayscore(Double awayscore) {
		this.awayscore = awayscore;
	}
	public void setHomescore(Double homescore) {
		this.homescore = homescore;
	}
	public void setIslive(String islive) {
		this.islive = islive;
	}
	public void setIslucky(String islucky) {
		this.islucky = islucky;
	}
	public void setParlay_type(String parlay_type) {
		this.parlay_type = parlay_type;
	}
	public void setCombo_type(String combo_type) {
		this.combo_type = combo_type;
	}
	public void setStake(Double stake) {
		this.stake = stake;
	}
	public void setBettag(String bettag) {
		this.bettag = bettag;
	}
	public void setWinloseamount(Double winloseamount) {
		this.winloseamount = winloseamount;
	}
	public void setWinlostdatetime(String winlostdatetime) {
		this.winlostdatetime = winlostdatetime;
	}
	public void setVersionkey(Long versionkey) {
		this.versionkey = versionkey;
	}
	public void setLastballno(String lastballno) {
		this.lastballno = lastballno;
	}
	public void setTicketstatus(String ticketstatus) {
		this.ticketstatus = ticketstatus;
	}
	public Integer getOddstype() {
		return oddstype;
	}
	public void setOddstype(Integer oddstype) {
		this.oddstype = oddstype;
	}
	public String getSettlingtime() {
		return settlingtime;
	}
	public void setSettlingtime(String settlingtime) {
		this.settlingtime = settlingtime;
	}
	public Double getActual_stake() {
		return actual_stake;
	}
	public void setActual_stake(Double actual_stake) {
		this.actual_stake = actual_stake;
	}
	
}
