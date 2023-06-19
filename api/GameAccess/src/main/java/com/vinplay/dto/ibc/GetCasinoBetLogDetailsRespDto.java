package com.vinplay.dto.ibc;

import java.io.Serializable;
import java.util.Date;

public class GetCasinoBetLogDetailsRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = -4742833162575638871L;
	private Long TransId;
	private String PlayerName;
	private Date TransactionTime;
	private Long MatchId;
	private String SportType;
	private String TableNo;
	private String HandNo;
	private String ShoeNo;
	private Integer BetType;
	private String BetTeam;
	private Double Odds;
	private Integer OddsType;
	private String TicketStatus;
	private Integer Stake;
	private Double WinLoseAmount;
	private Double AfterAmount;
	private Date WinLostDateTime;
	private String Currency;
	private Long VersionKey;
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
	public String getSportType() {
		return SportType;
	}
	public void setSportType(String sportType) {
		SportType = sportType;
	}
	public String getTableNo() {
		return TableNo;
	}
	public void setTableNo(String tableNo) {
		TableNo = tableNo;
	}
	public String getHandNo() {
		return HandNo;
	}
	public void setHandNo(String handNo) {
		HandNo = handNo;
	}
	public String getShoeNo() {
		return ShoeNo;
	}
	public void setShoeNo(String shoeNo) {
		ShoeNo = shoeNo;
	}
	public Integer getBetType() {
		return BetType;
	}
	public void setBetType(Integer betType) {
		BetType = betType;
	}
	public String getBetTeam() {
		return BetTeam;
	}
	public void setBetTeam(String betTeam) {
		BetTeam = betTeam;
	}
	public Double getOdds() {
		return Odds;
	}
	public void setOdds(Double odds) {
		Odds = odds;
	}
	public Integer getOddsType() {
		return OddsType;
	}
	public void setOddsType(Integer oddsType) {
		OddsType = oddsType;
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
	public Double getWinLoseAmount() {
		return WinLoseAmount;
	}
	public void setWinLoseAmount(Double winLoseAmount) {
		WinLoseAmount = winLoseAmount;
	}
	public Double getAfterAmount() {
		return AfterAmount;
	}
	public void setAfterAmount(Double afterAmount) {
		AfterAmount = afterAmount;
	}
	public Date getWinLostDateTime() {
		return WinLostDateTime;
	}
	public void setWinLostDateTime(Date winLostDateTime) {
		WinLostDateTime = winLostDateTime;
	}
	public String getCurrency() {
		return Currency;
	}
	public void setCurrency(String currency) {
		Currency = currency;
	}
	public Long getVersionKey() {
		return VersionKey;
	}
	public void setVersionKey(Long versionKey) {
		VersionKey = versionKey;
	}

}
