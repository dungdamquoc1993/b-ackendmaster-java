package com.vinplay.item;

import java.io.Serializable;

public class CmdGameRecords implements Serializable {
	
	private static final long serialVersionUID = -4212165843685005896L;
	
	private long id;
	private String sourcename;
	private String referenceno;
	private long soctransid;
	private String isfirsthalf;
	private long transdate;
	private String ishomegive;
	private String isbethome;
	private double betamount;
	private double outstanding;
	private double hdp;
	private double odds;
	private String currency;
	private double winamount;
	private double exchangerate;
	private String winlosestatus;
	private String transtype;
	private String dangerstatus;
	private double memcommission;
	private String betip;
	private int homescore;
	private int awayscore;
	private int runhomescore;
	private int runawayscore;
	private String isrunning;
	private String rejectreason;
	private String sporttype;
	private int choice;
	private long workingdate;
	private String oddstype;
	private long matchdate;
	private int hometeamid;
	private int awayteamid;
	private int leagueid;
	private String specialid;
	private int statuschange;
	private long stateupdatets;
	private double memcommissionset;
	private String iscashout;
	private double cashouttotal;
	private double cashouttakeback;
	private double cashoutwinloseamount;
	private int betsource;
	private String aosexcluding;
	private double mmrpercent;
	private long matchid;
	private String matchgroupid;
	private String betremarks;
	private String isspecial;
	private String betdate;
	private String settleddate;
	private String loginname;
	
	private double stake;
	private double payout;
	private double realbet;
	
	private double effective_bet;
	private double valid_bet_new;
	private double payout_new;
	
	public double getEffective_bet() {
		return effective_bet;
	}
	public void setEffective_bet(double effective_bet) {
		this.effective_bet = effective_bet;
	}
	public double getValid_bet_new() {
		return valid_bet_new;
	}
	public void setValid_bet_new(double valid_bet_new) {
		this.valid_bet_new = valid_bet_new;
	}
	public double getPayout_new() {
		return payout_new;
	}
	public void setPayout_new(double payout_new) {
		this.payout_new = payout_new;
	}
	public double getStake() {
		return stake;
	}
	public void setStake(double stake) {
		this.stake = stake;
	}
	public double getPayout() {
		return payout;
	}
	public void setPayout(double payout) {
		this.payout = payout;
	}
	public double getRealbet() {
		return realbet;
	}
	public void setRealbet(double realbet) {
		this.realbet = realbet;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSourcename() {
		return sourcename;
	}
	public void setSourcename(String sourcename) {
		this.sourcename = sourcename;
	}
	public String getReferenceno() {
		return referenceno;
	}
	public void setReferenceno(String referenceno) {
		this.referenceno = referenceno;
	}
	public long getSoctransid() {
		return soctransid;
	}
	public void setSoctransid(long soctransid) {
		this.soctransid = soctransid;
	}
	public String getIsfirsthalf() {
		return isfirsthalf;
	}
	public void setIsfirsthalf(String isfirsthalf) {
		this.isfirsthalf = isfirsthalf;
	}
	public long getTransdate() {
		return transdate;
	}
	public void setTransdate(long transdate) {
		this.transdate = transdate;
	}
	public String getIshomegive() {
		return ishomegive;
	}
	public void setIshomegive(String ishomegive) {
		this.ishomegive = ishomegive;
	}
	public String getIsbethome() {
		return isbethome;
	}
	public void setIsbethome(String isbethome) {
		this.isbethome = isbethome;
	}
	public double getBetamount() {
		return betamount;
	}
	public void setBetamount(double betamount) {
		this.betamount = betamount;
	}
	public double getOutstanding() {
		return outstanding;
	}
	public void setOutstanding(double outstanding) {
		this.outstanding = outstanding;
	}
	public double getHdp() {
		return hdp;
	}
	public void setHdp(double hdp) {
		this.hdp = hdp;
	}
	public double getOdds() {
		return odds;
	}
	public void setOdds(double odds) {
		this.odds = odds;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public double getWinamount() {
		return winamount;
	}
	public void setWinamount(double winamount) {
		this.winamount = winamount;
	}
	public double getExchangerate() {
		return exchangerate;
	}
	public void setExchangerate(double exchangerate) {
		this.exchangerate = exchangerate;
	}
	public String getWinlosestatus() {
		return winlosestatus;
	}
	public void setWinlosestatus(String winlosestatus) {
		this.winlosestatus = winlosestatus;
	}
	public String getTranstype() {
		return transtype;
	}
	public void setTranstype(String transtype) {
		this.transtype = transtype;
	}
	public String getDangerstatus() {
		return dangerstatus;
	}
	public void setDangerstatus(String dangerstatus) {
		this.dangerstatus = dangerstatus;
	}
	public double getMemcommission() {
		return memcommission;
	}
	public void setMemcommission(double memcommission) {
		this.memcommission = memcommission;
	}
	public String getBetip() {
		return betip;
	}
	public void setBetip(String betip) {
		this.betip = betip;
	}
	public int getHomescore() {
		return homescore;
	}
	public void setHomescore(int homescore) {
		this.homescore = homescore;
	}
	public int getAwayscore() {
		return awayscore;
	}
	public void setAwayscore(int awayscore) {
		this.awayscore = awayscore;
	}
	public int getRunhomescore() {
		return runhomescore;
	}
	public void setRunhomescore(int runhomescore) {
		this.runhomescore = runhomescore;
	}
	public int getRunawayscore() {
		return runawayscore;
	}
	public void setRunawayscore(int runawayscore) {
		this.runawayscore = runawayscore;
	}
	public String getIsrunning() {
		return isrunning;
	}
	public void setIsrunning(String isrunning) {
		this.isrunning = isrunning;
	}
	public String getRejectreason() {
		return rejectreason;
	}
	public void setRejectreason(String rejectreason) {
		this.rejectreason = rejectreason;
	}
	public String getSporttype() {
		return sporttype;
	}
	public void setSporttype(String sporttype) {
		this.sporttype = sporttype;
	}
	public int getChoice() {
		return choice;
	}
	public void setChoice(int choice) {
		this.choice = choice;
	}
	public long getWorkingdate() {
		return workingdate;
	}
	public void setWorkingdate(long workingdate) {
		this.workingdate = workingdate;
	}
	public String getOddstype() {
		return oddstype;
	}
	public void setOddstype(String oddstype) {
		this.oddstype = oddstype;
	}
	public long getMatchdate() {
		return matchdate;
	}
	public void setMatchdate(long matchdate) {
		this.matchdate = matchdate;
	}
	public int getHometeamid() {
		return hometeamid;
	}
	public void setHometeamid(int hometeamid) {
		this.hometeamid = hometeamid;
	}
	public int getAwayteamid() {
		return awayteamid;
	}
	public void setAwayteamid(int awayteamid) {
		this.awayteamid = awayteamid;
	}
	public int getLeagueid() {
		return leagueid;
	}
	public void setLeagueid(int leagueid) {
		this.leagueid = leagueid;
	}
	public String getSpecialid() {
		return specialid;
	}
	public void setSpecialid(String specialid) {
		this.specialid = specialid;
	}
	public int getStatuschange() {
		return statuschange;
	}
	public void setStatuschange(int statuschange) {
		this.statuschange = statuschange;
	}
	public long getStateupdatets() {
		return stateupdatets;
	}
	public void setStateupdatets(long stateupdatets) {
		this.stateupdatets = stateupdatets;
	}
	public double getMemcommissionset() {
		return memcommissionset;
	}
	public void setMemcommissionset(double memcommissionset) {
		this.memcommissionset = memcommissionset;
	}
	public String getIscashout() {
		return iscashout;
	}
	public void setIscashout(String iscashout) {
		this.iscashout = iscashout;
	}
	public double getCashouttotal() {
		return cashouttotal;
	}
	public void setCashouttotal(double cashouttotal) {
		this.cashouttotal = cashouttotal;
	}
	public double getCashouttakeback() {
		return cashouttakeback;
	}
	public void setCashouttakeback(double cashouttakeback) {
		this.cashouttakeback = cashouttakeback;
	}
	public double getCashoutwinloseamount() {
		return cashoutwinloseamount;
	}
	public void setCashoutwinloseamount(double cashoutwinloseamount) {
		this.cashoutwinloseamount = cashoutwinloseamount;
	}
	public int getBetsource() {
		return betsource;
	}
	public void setBetsource(int betsource) {
		this.betsource = betsource;
	}
	public String getAosexcluding() {
		return aosexcluding;
	}
	public void setAosexcluding(String aosexcluding) {
		this.aosexcluding = aosexcluding;
	}
	public double getMmrpercent() {
		return mmrpercent;
	}
	public void setMmrpercent(double mmrpercent) {
		this.mmrpercent = mmrpercent;
	}
	public long getMatchid() {
		return matchid;
	}
	public void setMatchid(long matchid) {
		this.matchid = matchid;
	}
	public String getMatchgroupid() {
		return matchgroupid;
	}
	public void setMatchgroupid(String matchgroupid) {
		this.matchgroupid = matchgroupid;
	}
	public String getBetremarks() {
		return betremarks;
	}
	public void setBetremarks(String betremarks) {
		this.betremarks = betremarks;
	}
	public String getIsspecial() {
		return isspecial;
	}
	public void setIsspecial(String isspecial) {
		this.isspecial = isspecial;
	}
	public String getBetdate() {
		return betdate;
	}
	public void setBetdate(String betdate) {
		this.betdate = betdate;
	}
	public String getSettleddate() {
		return settleddate;
	}
	public void setSettleddate(String settleddate) {
		this.settleddate = settleddate;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

}
