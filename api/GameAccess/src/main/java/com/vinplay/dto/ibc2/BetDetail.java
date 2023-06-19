package com.vinplay.dto.ibc2;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class BetDetail implements Serializable {
	private static final long serialVersionUID = 1906777576977041453L;
	public Long trans_id;
	public String vendor_member_id;
	public String operator_id;
	public Integer league_id;
	public Integer match_id;
	public Integer home_id;
	public Integer away_id;
	public Date match_datetime;
	public Integer sport_type;
	public Integer bet_type;
	public BigInteger parlay_ref_no;
	public BigDecimal odds;
	public BigDecimal stake;
	public BigDecimal validbetamount;
	public Date transaction_time;
	public String ticket_status;
	public BigDecimal winlost_amount;
	public BigDecimal after_amount;
	public Integer currency;
	public Date winlost_datetime;
	public Integer odds_type;
	public String isLucky;
	public String bet_team;
	public String exculding;
	public BigDecimal home_hdp;
	public BigDecimal away_hdp;
	public Object hdp;
	public String betfrom;
	public String islive;
	public Integer home_score;
	public Integer away_score;
	public String customInfo1;
	public String customInfo2;
	public String customInfo3;
	public String customInfo4;
	public String customInfo5;
	public String ba_status;
	public Integer version_key;

	public Long getTrans_id() {
		return trans_id;
	}

	public void setTrans_id(Long trans_id) {
		this.trans_id = trans_id;
	}

	public String getVendor_member_id() {
		return vendor_member_id;
	}

	public void setVendor_member_id(String vendor_member_id) {
		this.vendor_member_id = vendor_member_id;
	}

	public String getOperator_id() {
		return operator_id;
	}

	public void setOperator_id(String operator_id) {
		this.operator_id = operator_id;
	}

	public Integer getLeague_id() {
		return league_id;
	}

	public void setLeague_id(Integer league_id) {
		this.league_id = league_id;
	}

	public Integer getMatch_id() {
		return match_id;
	}

	public void setMatch_id(Integer match_id) {
		this.match_id = match_id;
	}

	public Integer getHome_id() {
		return home_id;
	}

	public void setHome_id(Integer home_id) {
		this.home_id = home_id;
	}

	public Integer getAway_id() {
		return away_id;
	}

	public void setAway_id(Integer away_id) {
		this.away_id = away_id;
	}

	public Date getMatch_datetime() {
		return match_datetime;
	}

	public void setMatch_datetime(Date match_datetime) {
		this.match_datetime = match_datetime;
	}

	public Integer getSport_type() {
		return sport_type;
	}

	public void setSport_type(Integer sport_type) {
		this.sport_type = sport_type;
	}

	public Integer getBet_type() {
		return bet_type;
	}

	public void setBet_type(Integer bet_type) {
		this.bet_type = bet_type;
	}

	public BigDecimal getOdds() {
		return odds;
	}

	public void setOdds(BigDecimal odds) {
		this.odds = odds;
	}

	public BigDecimal getStake() {
		return stake;
	}

	public void setStake(BigDecimal stake) {
		this.stake = stake;
	}

	public BigDecimal getValidbetamount() {
		return validbetamount;
	}

	public void setValidbetamount(BigDecimal validbetamount) {
		this.validbetamount = validbetamount;
	}

	public Date getTransaction_time() {
		return transaction_time;
	}

	public void setTransaction_time(Date transaction_time) {
		this.transaction_time = transaction_time;
	}

	public String getTicket_status() {
		return ticket_status;
	}

	public void setTicket_status(String ticket_status) {
		this.ticket_status = ticket_status;
	}

	public BigDecimal getWinlost_amount() {
		return winlost_amount;
	}

	public void setWinlost_amount(BigDecimal winlost_amount) {
		this.winlost_amount = winlost_amount;
	}

	public BigDecimal getAfter_amount() {
		return after_amount;
	}

	public void setAfter_amount(BigDecimal after_amount) {
		this.after_amount = after_amount;
	}

	public Integer getCurrency() {
		return currency;
	}

	public void setCurrency(Integer currency) {
		this.currency = currency;
	}

	public Date getWinlost_datetime() {
		return winlost_datetime;
	}

	public void setWinlost_datetime(Date winlost_datetime) {
		this.winlost_datetime = winlost_datetime;
	}

	public Integer getOdds_type() {
		return odds_type;
	}

	public void setOdds_type(Integer odds_type) {
		this.odds_type = odds_type;
	}

	public String getIsLucky() {
		return isLucky;
	}

	public void setIsLucky(String isLucky) {
		this.isLucky = isLucky;
	}

	public String getBet_team() {
		return bet_team;
	}

	public void setBet_team(String bet_team) {
		this.bet_team = bet_team;
	}

	public String getExculding() {
		return exculding;
	}

	public void setExculding(String exculding) {
		this.exculding = exculding;
	}

	public BigDecimal getHome_hdp() {
		return home_hdp;
	}

	public void setHome_hdp(BigDecimal home_hdp) {
		this.home_hdp = home_hdp;
	}

	public BigDecimal getAway_hdp() {
		return away_hdp;
	}

	public void setAway_hdp(BigDecimal away_hdp) {
		this.away_hdp = away_hdp;
	}

	public Object getHdp() {
		return hdp;
	}

	public void setHdp(Object hdp) {
		this.hdp = hdp;
	}

	public String getBetfrom() {
		return betfrom;
	}

	public void setBetfrom(String betfrom) {
		this.betfrom = betfrom;
	}

	public String getIslive() {
		return islive;
	}

	public void setIslive(String islive) {
		this.islive = islive;
	}

	public Integer getHome_score() {
		return home_score;
	}

	public void setHome_score(Integer home_score) {
		this.home_score = home_score;
	}

	public Integer getAway_score() {
		return away_score;
	}

	public void setAway_score(Integer away_score) {
		this.away_score = away_score;
	}

	public String getCustomInfo1() {
		return customInfo1;
	}

	public void setCustomInfo1(String customInfo1) {
		this.customInfo1 = customInfo1;
	}

	public String getCustomInfo2() {
		return customInfo2;
	}

	public void setCustomInfo2(String customInfo2) {
		this.customInfo2 = customInfo2;
	}

	public String getCustomInfo3() {
		return customInfo3;
	}

	public void setCustomInfo3(String customInfo3) {
		this.customInfo3 = customInfo3;
	}

	public String getCustomInfo4() {
		return customInfo4;
	}

	public void setCustomInfo4(String customInfo4) {
		this.customInfo4 = customInfo4;
	}

	public String getCustomInfo5() {
		return customInfo5;
	}

	public void setCustomInfo5(String customInfo5) {
		this.customInfo5 = customInfo5;
	}

	public String getBa_status() {
		return ba_status;
	}

	public void setBa_status(String ba_status) {
		this.ba_status = ba_status;
	}

	public Integer getVersion_key() {
		return version_key;
	}

	public void setVersion_key(Integer version_key) {
		this.version_key = version_key;
	}

	public BigInteger getParlay_ref_no() {
		return parlay_ref_no;
	}

	public void setParlay_ref_no(BigInteger parlay_ref_no) {
		this.parlay_ref_no = parlay_ref_no;
	}

}
