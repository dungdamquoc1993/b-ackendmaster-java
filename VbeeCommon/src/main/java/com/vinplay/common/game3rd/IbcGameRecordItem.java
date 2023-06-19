package com.vinplay.common.game3rd;

import java.util.Date;

public class IbcGameRecordItem implements java.io.Serializable{
	private static final long serialVersionUID = -5326635076638587958L;
	/**
	 * 
	 */

	private Long transid;
	private String playername;
	private Date transactiontime;
	private Integer matchid;
	private Integer leagueid;
	
	private String leaguename;
	private Integer awayid;
	private String awayidname;
	private Integer homeid;
	private String homeidname;
	
	private Date matchdatetime;
	private Integer bettype;
	private Long parlayrefno;
	private String betteam;
	private Double hdp;
	
	private String sporttype;
	private Double awayhdp;
	private Double homehdp;
	private Double odds;
	private Integer awayscore;
	
	private Integer homescore;
	private String islive;
	private String islucky;
	private String parlay_type;
	private String combo_type;
	
	private Long stake;
	private String bettag;
	private Long winloseamount;
	private Date winlostdatetime;
	private Integer versionkey;
	
	private String lastballno;
	private String ticketstatus;
	private Integer oddstype;
	private Long actual_stake;
	private Long refund_amount;
	private String nick_name;

//	public IbcGameRecordItem(Long transid, String playername, Date transactiontime, Integer matchid, Integer leagueid, String leaguename, Integer awayid, String awayidname, Integer homeid, String homeidname, Date matchdatetime, Integer bettype, Long parlayrefno, String betteam, Double hdp, String sporttype, Double awayhdp, Double homehdp, Double odds, Integer awayscore, Integer homescore, String islive, String islucky, String parlay_type, String combo_type, Long stake, String bettag, Long winloseamount, Date winlostdatetime, Integer versionkey, String lastballno, String ticketstatus, Integer oddstype, Long actual_stake, Long refund_amount, String nick_name) {
//		this.transid = transid;
//		this.playername = playername;
//		this.transactiontime = transactiontime;
//		this.matchid = matchid;
//		this.leagueid = leagueid;
//		this.leaguename = leaguename;
//		this.awayid = awayid;
//		this.awayidname = awayidname;
//		this.homeid = homeid;
//		this.homeidname = homeidname;
//		this.matchdatetime = matchdatetime;
//		this.bettype = bettype;
//		this.parlayrefno = parlayrefno;
//		this.betteam = betteam;
//		this.hdp = hdp;
//		this.sporttype = sporttype;
//		this.awayhdp = awayhdp;
//		this.homehdp = homehdp;
//		this.odds = odds;
//		this.awayscore = awayscore;
//		this.homescore = homescore;
//		this.islive = islive;
//		this.islucky = islucky;
//		this.parlay_type = parlay_type;
//		this.combo_type = combo_type;
//		this.stake = stake;
//		this.bettag = bettag;
//		this.winloseamount = winloseamount;
//		this.winlostdatetime = winlostdatetime;
//		this.versionkey = versionkey;
//		this.lastballno = lastballno;
//		this.ticketstatus = ticketstatus;
//		this.oddstype = oddstype;
//		this.actual_stake = actual_stake;
//		this.refund_amount = refund_amount;
//		this.nick_name = nick_name;
//	}

	public IbcGameRecordItem() {
		super();
	}

	public IbcGameRecordItem(Long transid, String playername, Date transactiontime, Integer matchid, Integer leagueid,
			String leaguename, Integer awayid, String awayidname, Integer homeid, String homeidname, Date matchdatetime,
			Integer bettype, Long parlayrefno, String betteam, Double hdp, String sporttype, Double awayhdp,
			Double homehdp, Double odds, Integer awayscore, Integer homescore, String islive, String islucky,
			String parlay_type, String combo_type, Long stake, String bettag, Long winloseamount, Date winlostdatetime,
			Integer versionkey, String lastballno, String ticketstatus, Integer oddstype, Long actual_stake,
			Long refund_amount, String nick_name) {
		super();
		this.transid = transid;
		this.playername = playername;
		this.transactiontime = transactiontime;
		this.matchid = matchid;
		this.leagueid = leagueid;
		this.leaguename = leaguename;
		this.awayid = awayid;
		this.awayidname = awayidname;
		this.homeid = homeid;
		this.homeidname = homeidname;
		this.matchdatetime = matchdatetime;
		this.bettype = bettype;
		this.parlayrefno = parlayrefno;
		this.betteam = betteam;
		this.hdp = hdp;
		this.sporttype = sporttype;
		this.awayhdp = awayhdp;
		this.homehdp = homehdp;
		this.odds = odds;
		this.awayscore = awayscore;
		this.homescore = homescore;
		this.islive = islive;
		this.islucky = islucky;
		this.parlay_type = parlay_type;
		this.combo_type = combo_type;
		this.stake = stake;
		this.bettag = bettag;
		this.winloseamount = winloseamount;
		this.winlostdatetime = winlostdatetime;
		this.versionkey = versionkey;
		this.lastballno = lastballno;
		this.ticketstatus = ticketstatus;
		this.oddstype = oddstype;
		this.actual_stake = actual_stake;
		this.refund_amount = refund_amount;
		this.nick_name = nick_name;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Long getTransid() {
		return transid;
	}

	public void setTransid(Long transid) {
		this.transid = transid;
	}

	public String getPlayername() {
		return playername;
	}

	public void setPlayername(String playername) {
		this.playername = playername;
	}

	public Date getTransactiontime() {
		return transactiontime;
	}

	public void setTransactiontime(Date transactiontime) {
		this.transactiontime = transactiontime;
	}

	public Integer getMatchid() {
		return matchid;
	}

	public void setMatchid(Integer matchid) {
		this.matchid = matchid;
	}

	public Integer getLeagueid() {
		return leagueid;
	}

	public void setLeagueid(Integer leagueid) {
		this.leagueid = leagueid;
	}

	public String getLeaguename() {
		return leaguename;
	}

	public void setLeaguename(String leaguename) {
		this.leaguename = leaguename;
	}

	public Integer getAwayid() {
		return awayid;
	}

	public void setAwayid(Integer awayid) {
		this.awayid = awayid;
	}

	public String getAwayidname() {
		return awayidname;
	}

	public void setAwayidname(String awayidname) {
		this.awayidname = awayidname;
	}

	public Integer getHomeid() {
		return homeid;
	}

	public void setHomeid(Integer homeid) {
		this.homeid = homeid;
	}

	public String getHomeidname() {
		return homeidname;
	}

	public void setHomeidname(String homeidname) {
		this.homeidname = homeidname;
	}

	public Date getMatchdatetime() {
		return matchdatetime;
	}

	public void setMatchdatetime(Date matchdatetime) {
		this.matchdatetime = matchdatetime;
	}

	public Integer getBettype() {
		return bettype;
	}

	public void setBettype(Integer bettype) {
		this.bettype = bettype;
	}

	public Long getParlayrefno() {
		return parlayrefno;
	}

	public void setParlayrefno(Long parlayrefno) {
		this.parlayrefno = parlayrefno;
	}

	public String getBetteam() {
		return betteam;
	}

	public void setBetteam(String betteam) {
		this.betteam = betteam;
	}

	public Double getHdp() {
		return hdp;
	}

	public void setHdp(Double hdp) {
		this.hdp = hdp;
	}

	public String getSporttype() {
		return sporttype;
	}

	public void setSporttype(String sporttype) {
		this.sporttype = sporttype;
	}

	public Double getAwayhdp() {
		return awayhdp;
	}

	public void setAwayhdp(Double awayhdp) {
		this.awayhdp = awayhdp;
	}

	public Double getHomehdp() {
		return homehdp;
	}

	public void setHomehdp(Double homehdp) {
		this.homehdp = homehdp;
	}

	public Double getOdds() {
		return odds;
	}

	public void setOdds(Double odds) {
		this.odds = odds;
	}

	public Integer getAwayscore() {
		return awayscore;
	}

	public void setAwayscore(Integer awayscore) {
		this.awayscore = awayscore;
	}

	public Integer getHomescore() {
		return homescore;
	}

	public void setHomescore(Integer homescore) {
		this.homescore = homescore;
	}

	public String getIslive() {
		return islive;
	}

	public void setIslive(String islive) {
		this.islive = islive;
	}

	public String getIslucky() {
		return islucky;
	}

	public void setIslucky(String islucky) {
		this.islucky = islucky;
	}

	public String getParlay_type() {
		return parlay_type;
	}

	public void setParlay_type(String parlay_type) {
		this.parlay_type = parlay_type;
	}

	public String getCombo_type() {
		return combo_type;
	}

	public void setCombo_type(String combo_type) {
		this.combo_type = combo_type;
	}

	public Long getStake() {
		return stake;
	}

	public void setStake(Long stake) {
		this.stake = stake;
	}

	public String getBettag() {
		return bettag;
	}

	public void setBettag(String bettag) {
		this.bettag = bettag;
	}

	public Long getWinloseamount() {
		return winloseamount;
	}

	public void setWinloseamount(Long winloseamount) {
		this.winloseamount = winloseamount;
	}

	public Date getWinlostdatetime() {
		return winlostdatetime;
	}

	public void setWinlostdatetime(Date winlostdatetime) {
		this.winlostdatetime = winlostdatetime;
	}

	public Integer getVersionkey() {
		return versionkey;
	}

	public void setVersionkey(Integer versionkey) {
		this.versionkey = versionkey;
	}

	public String getLastballno() {
		return lastballno;
	}

	public void setLastballno(String lastballno) {
		this.lastballno = lastballno;
	}

	public String getTicketstatus() {
		return ticketstatus;
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

	public Long getActual_stake() {
		return actual_stake;
	}

	public void setActual_stake(Long actual_stake) {
		this.actual_stake = actual_stake;
	}

	public Long getRefund_amount() {
		return refund_amount;
	}

	public void setRefund_amount(Long refund_amount) {
		this.refund_amount = refund_amount;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
}
