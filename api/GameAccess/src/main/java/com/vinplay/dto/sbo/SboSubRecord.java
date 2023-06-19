/**
 * Archie
 */
package com.vinplay.dto.sbo;

import java.io.Serializable;

/**
 * @author Archie
 *
 */
public class SboSubRecord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8535065879004026184L;
	private Long transId = -1l;
	private String betOption = "";
	private String marketType = "";
	private Double hdp = 0d;
	private String league = "";
	private String match = "";
	private String liveScore = "";
	private String htScore = "";
	private String ftScore = "";
	
	public Long getTransId() {
		return transId;
	}

	public void setTransId(Long transId) {
		this.transId = transId;
	}

	public String getBetOption() {
		return betOption;
	}

	public void setBetOption(String betOption) {
		this.betOption = betOption;
	}

	public String getMarketType() {
		return marketType;
	}

	public void setMarketType(String marketType) {
		this.marketType = marketType;
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

	public String getLiveScore() {
		return liveScore;
	}

	public void setLiveScore(String liveScore) {
		this.liveScore = liveScore;
	}

	public String getHtScore() {
		return htScore;
	}

	public void setHtScore(String htScore) {
		this.htScore = htScore;
	}

	public String getFtScore() {
		return ftScore;
	}

	public void setFtScore(String ftScore) {
		this.ftScore = ftScore;
	}

	@Override
	public String toString() {
		return "SboSubRecord [betOption=" + betOption + ", marketType=" + marketType + ", hdp=" + hdp + ", league="
				+ league + ", match=" + match + ", liveScore=" + liveScore + ", htScore=" + htScore + ", ftScore="
				+ ftScore + "]";
	}

}
