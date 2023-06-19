package com.vinplay.dto.cmd;

import java.io.Serializable;

public class CmdMemberBetTicketInformationDetail implements Serializable {

	private static final long serialVersionUID = -4212165843685005896L;

	private int Id;
	private String SourceName;
	private String ReferenceNo;
	private int SocTransId;
	private boolean IsFirstHalf;
	private long TransDate;
	private boolean IsHomeGive;
	private boolean IsBetHome;
	private double BetAmount;
	private double Outstanding;
	private double Hdp;
	private double Odds;
	private String Currency;
	private double WinAmount;
	private double ExchangeRate;
	private String WinLoseStatus;
	private String TransType;
	private String DangerStatus;
	private double MemCommission;
	private String BetIp;
	private int HomeScore;
	private int AwayScore;
	private int RunHomeScore;
	private int RunAwayScore;
	private boolean IsRunning;
	private String RejectReason;
	private String SportType;
	private int Choice;
	private int WorkingDate;
	private String OddsType;
	private long MatchDate;
	private int HomeTeamId;
	private int AwayTeamId;
	private int LeagueId;
	private String SpecialId;
	private int StatusChange;
	private long StateUpdateTs;
	private double MemCommissionSet;
	private boolean IsCashOut;
	private double CashOutTotal;
	private double CashOutTakeBack;
	private double CashOutWinLoseAmount;
	private int BetSource;
	private String AOSExcluding;
	private double MMRPercent;
	private int MatchID;
	private String MatchGroupID;
	private String BetRemarks;
	private boolean IsSpecial;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getSourceName() {
		return SourceName;
	}

	public void setSourceName(String sourceName) {
		SourceName = sourceName;
	}

	public String getReferenceNo() {
		return ReferenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		ReferenceNo = referenceNo;
	}

	public int getSocTransId() {
		return SocTransId;
	}

	public void setSocTransId(int socTransId) {
		SocTransId = socTransId;
	}

	public boolean isIsFirstHalf() {
		return IsFirstHalf;
	}

	public void setIsFirstHalf(boolean isFirstHalf) {
		IsFirstHalf = isFirstHalf;
	}

	public long getTransDate() {
		return TransDate;
	}

	public void setTransDate(long transDate) {
		TransDate = transDate;
	}

	public boolean isIsHomeGive() {
		return IsHomeGive;
	}

	public void setIsHomeGive(boolean isHomeGive) {
		IsHomeGive = isHomeGive;
	}

	public boolean isIsBetHome() {
		return IsBetHome;
	}

	public void setIsBetHome(boolean isBetHome) {
		IsBetHome = isBetHome;
	}

	public double getBetAmount() {
		return BetAmount;
	}

	public void setBetAmount(double betAmount) {
		BetAmount = betAmount;
	}

	public double getOutstanding() {
		return Outstanding;
	}

	public void setOutstanding(double outstanding) {
		Outstanding = outstanding;
	}

	public double getHdp() {
		return Hdp;
	}

	public void setHdp(double hdp) {
		Hdp = hdp;
	}

	public double getOdds() {
		return Odds;
	}

	public void setOdds(double odds) {
		Odds = odds;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String currency) {
		Currency = currency;
	}

	public double getWinAmount() {
		return WinAmount;
	}

	public void setWinAmount(double winAmount) {
		WinAmount = winAmount;
	}

	public double getExchangeRate() {
		return ExchangeRate;
	}

	public void setExchangeRate(double exchangeRate) {
		ExchangeRate = exchangeRate;
	}

	public String getWinLoseStatus() {
		return WinLoseStatus;
	}

	public void setWinLoseStatus(String winLoseStatus) {
		WinLoseStatus = winLoseStatus;
	}

	public String getTransType() {
		return TransType;
	}

	public void setTransType(String transType) {
		TransType = transType;
	}

	public String getDangerStatus() {
		return DangerStatus;
	}

	public void setDangerStatus(String dangerStatus) {
		DangerStatus = dangerStatus;
	}

	public double getMemCommission() {
		return MemCommission;
	}

	public void setMemCommission(double memCommission) {
		MemCommission = memCommission;
	}

	public String getBetIp() {
		return BetIp;
	}

	public void setBetIp(String betIp) {
		BetIp = betIp;
	}

	public int getHomeScore() {
		return HomeScore;
	}

	public void setHomeScore(int homeScore) {
		HomeScore = homeScore;
	}

	public int getAwayScore() {
		return AwayScore;
	}

	public void setAwayScore(int awayScore) {
		AwayScore = awayScore;
	}

	public int getRunHomeScore() {
		return RunHomeScore;
	}

	public void setRunHomeScore(int runHomeScore) {
		RunHomeScore = runHomeScore;
	}

	public boolean isIsRunning() {
		return IsRunning;
	}

	public void setIsRunning(boolean isRunning) {
		IsRunning = isRunning;
	}

	public String getRejectReason() {
		return RejectReason;
	}

	public void setRejectReason(String rejectReason) {
		RejectReason = rejectReason;
	}

	public String getSportType() {
		return SportType;
	}

	public void setSportType(String sportType) {
		SportType = sportType;
	}

	public int getChoice() {
		return Choice;
	}

	public void setChoice(int choice) {
		Choice = choice;
	}

	public int getWorkingDate() {
		return WorkingDate;
	}

	public void setWorkingDate(int workingDate) {
		WorkingDate = workingDate;
	}

	public String getOddsType() {
		return OddsType;
	}

	public void setOddsType(String oddsType) {
		OddsType = oddsType;
	}

	public long getMatchDate() {
		return MatchDate;
	}

	public void setMatchDate(long matchDate) {
		MatchDate = matchDate;
	}

	public int getHomeTeamId() {
		return HomeTeamId;
	}

	public void setHomeTeamId(int homeTeamId) {
		HomeTeamId = homeTeamId;
	}

	public int getAwayTeamId() {
		return AwayTeamId;
	}

	public void setAwayTeamId(int awayTeamId) {
		AwayTeamId = awayTeamId;
	}

	public int getLeagueId() {
		return LeagueId;
	}

	public void setLeagueId(int leagueId) {
		LeagueId = leagueId;
	}

	public String getSpecialId() {
		return SpecialId;
	}

	public void setSpecialId(String specialId) {
		SpecialId = specialId;
	}

	public int getStatusChange() {
		return StatusChange;
	}

	public void setStatusChange(int statusChange) {
		StatusChange = statusChange;
	}

	public long getStateUpdateTs() {
		return StateUpdateTs;
	}

	public void setStateUpdateTs(long stateUpdateTs) {
		StateUpdateTs = stateUpdateTs;
	}

	public double getMemCommissionSet() {
		return MemCommissionSet;
	}

	public void setMemCommissionSet(double memCommissionSet) {
		MemCommissionSet = memCommissionSet;
	}

	public boolean isIsCashOut() {
		return IsCashOut;
	}

	public void setIsCashOut(boolean isCashOut) {
		IsCashOut = isCashOut;
	}

	public double getCashOutTotal() {
		return CashOutTotal;
	}

	public void setCashOutTotal(double cashOutTotal) {
		CashOutTotal = cashOutTotal;
	}

	public double getCashOutTakeBack() {
		return CashOutTakeBack;
	}

	public void setCashOutTakeBack(double cashOutTakeBack) {
		CashOutTakeBack = cashOutTakeBack;
	}

	public double getCashOutWinLoseAmount() {
		return CashOutWinLoseAmount;
	}

	public void setCashOutWinLoseAmount(double cashOutWinLoseAmount) {
		CashOutWinLoseAmount = cashOutWinLoseAmount;
	}

	public int getBetSource() {
		return BetSource;
	}

	public void setBetSource(int betSource) {
		BetSource = betSource;
	}

	public String getAOSExcluding() {
		return AOSExcluding;
	}

	public void setAOSExcluding(String aOSExcluding) {
		AOSExcluding = aOSExcluding;
	}

	public double getMMRPercent() {
		return MMRPercent;
	}

	public void setMMRPercent(double mMRPercent) {
		MMRPercent = mMRPercent;
	}

	public int getMatchID() {
		return MatchID;
	}

	public void setMatchID(int matchID) {
		MatchID = matchID;
	}

	public String getMatchGroupID() {
		return MatchGroupID;
	}

	public void setMatchGroupID(String matchGroupID) {
		MatchGroupID = matchGroupID;
	}

	public String getBetRemarks() {
		return BetRemarks;
	}

	public void setBetRemarks(String betRemarks) {
		BetRemarks = betRemarks;
	}

	public boolean isIsSpecial() {
		return IsSpecial;
	}

	public void setIsSpecial(boolean isSpecial) {
		IsSpecial = isSpecial;
	}

	public int getRunAwayScore() {
		return RunAwayScore;
	}

	public void setRunAwayScore(int runAwayScore) {
		RunAwayScore = runAwayScore;
	}

}
