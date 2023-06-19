package com.vinplay.dto.ibc2;

import com.vinplay.dto.ibc.BaseRespDto;
import com.vinplay.dto.ibc.common.ParlayDataRespDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class GetSportBetLogDetailsRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = -4623046451157636983L;
	private Long TransId;
	private String PlayerName;
	private Date TransactionTime;
	private Long MatchId;
	private Integer  LeagueId;
	private String LeagueName;
	private String SportType;
	private Long AwayId;
	private String AwayIDName;
	private Long HomeId;
	private String HomeIDName;
	private Date MatchDateTime;
	private Integer BetType;
	private Long ParlayRefNo;
	private String BetTeam;
	private Double HDP;
	private Double AwayHDP;
	private Double HomeHDP;
	private Double Odds;
	private Double AwayScore;
	private Double HomeScore;
	private String IsLive;
	private String IsLucky;
	private String TicketStatus;
	private Integer Stake;
	private String Bet_tag;
	private Double WinLoseAmount;
	private Date WinLostDateTime;
	private List<ParlayDataRespDto> ParlayData;
	private Long VersionKey;
	private String LastBallNo;
	public Long getTransId() {
		return TransId;
	}
	public void setTransId(Long transId) {
		TransId = transId;
	}
	public String getPlayerName() {
		return PlayerName;
	}
	public void setPlayerName(String playerName) {
		PlayerName = playerName;
	}
	public Date getTransactionTime() {
		return TransactionTime;
	}
	public void setTransactionTime(Date transactionTime) {
		TransactionTime = transactionTime;
	}
	public Long getMatchId() {
		return MatchId;
	}
	public void setMatchId(Long matchId) {
		MatchId = matchId;
	}
	public Integer getLeagueId() {
		return LeagueId;
	}
	public void setLeagueId(Integer leagueId) {
		LeagueId = leagueId;
	}
	public String getLeagueName() {
		return LeagueName;
	}
	public void setLeagueName(String leagueName) {
		LeagueName = leagueName;
	}
	public String getSportType() {
		return SportType;
	}
	public void setSportType(String sportType) {
		SportType = sportType;
	}
	public Long getAwayId() {
		return AwayId;
	}
	public void setAwayId(Long awayId) {
		AwayId = awayId;
	}
	public String getAwayIDName() {
		return AwayIDName;
	}
	public void setAwayIDName(String awayIDName) {
		AwayIDName = awayIDName;
	}
	public Long getHomeId() {
		return HomeId;
	}
	public void setHomeId(Long homeId) {
		HomeId = homeId;
	}
	public String getHomeIDName() {
		return HomeIDName;
	}
	public void setHomeIDName(String homeIDName) {
		HomeIDName = homeIDName;
	}
	public Date getMatchDateTime() {
		return MatchDateTime;
	}
	public void setMatchDateTime(Date matchDateTime) {
		MatchDateTime = matchDateTime;
	}
	public Integer getBetType() {
		return BetType;
	}
	public void setBetType(Integer betType) {
		BetType = betType;
	}
	public Long getParlayRefNo() {
		return ParlayRefNo;
	}
	public void setParlayRefNo(Long parlayRefNo) {
		ParlayRefNo = parlayRefNo;
	}
	public String getBetTeam() {
		return BetTeam;
	}
	public void setBetTeam(String betTeam) {
		BetTeam = betTeam;
	}
	public Double getHDP() {
		return HDP;
	}
	public void setHDP(Double hDP) {
		HDP = hDP;
	}
	public Double getAwayHDP() {
		return AwayHDP;
	}
	public void setAwayHDP(Double awayHDP) {
		AwayHDP = awayHDP;
	}
	public Double getHomeHDP() {
		return HomeHDP;
	}
	public void setHomeHDP(Double homeHDP) {
		HomeHDP = homeHDP;
	}
	public Double getOdds() {
		return Odds;
	}
	public void setOdds(Double odds) {
		Odds = odds;
	}
	public Double getAwayScore() {
		return AwayScore;
	}
	public void setAwayScore(Double awayScore) {
		AwayScore = awayScore;
	}
	public Double getHomeScore() {
		return HomeScore;
	}
	public void setHomeScore(Double homeScore) {
		HomeScore = homeScore;
	}
	public String getIsLive() {
		return IsLive;
	}
	public void setIsLive(String isLive) {
		IsLive = isLive;
	}
	public String getIsLucky() {
		return IsLucky;
	}
	public void setIsLucky(String isLucky) {
		IsLucky = isLucky;
	}
	public String getTicketStatus() {
		return TicketStatus;
	}
	public void setTicketStatus(String ticketStatus) {
		TicketStatus = ticketStatus;
	}
	public Integer getStake() {
		return Stake;
	}
	public void setStake(Integer stake) {
		Stake = stake;
	}
	public String getBet_tag() {
		return Bet_tag;
	}
	public void setBet_tag(String bet_tag) {
		Bet_tag = bet_tag;
	}
	public Double getWinLoseAmount() {
		return WinLoseAmount;
	}
	public void setWinLoseAmount(Double winLoseAmount) {
		WinLoseAmount = winLoseAmount;
	}
	public Date getWinLostDateTime() {
		return WinLostDateTime;
	}
	public void setWinLostDateTime(Date winLostDateTime) {
		WinLostDateTime = winLostDateTime;
	}
	public List<ParlayDataRespDto> getParlayData() {
		return ParlayData;
	}
	public void setParlayData(List<ParlayDataRespDto> parlayData) {
		ParlayData = parlayData;
	}
	public Long getVersionKey() {
		return VersionKey;
	}
	public void setVersionKey(Long versionKey) {
		VersionKey = versionKey;
	}
	public String getLastBallNo() {
		return LastBallNo;
	}
	public void setLastBallNo(String lastBallNo) {
		LastBallNo = lastBallNo;
	}
	
	
}
