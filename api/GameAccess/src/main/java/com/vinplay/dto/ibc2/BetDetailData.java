package com.vinplay.dto.ibc2;

import java.io.Serializable;
import java.util.List;

public class BetDetailData implements Serializable {
	private static final long serialVersionUID = -8343743299365254708L;
	private int last_version_key;
	private List<BetDetail> BetDetails;
	private List<BetDetail> BetNumberDetails;
	private List<BetDetail> BetVirtualSportDetails;
	private List<BetDetail> BetCasinoDetails;

	public int getLast_version_key() {
		return last_version_key;
	}

	public void setLast_version_key(int last_version_key) {
		this.last_version_key = last_version_key;
	}

	public List<BetDetail> getBetDetails() {
		return BetDetails;
	}

	public void setBetDetails(List<BetDetail> betDetails) {
		BetDetails = betDetails;
	}

	public List<BetDetail> getBetNumberDetails() {
		return BetNumberDetails;
	}

	public void setBetNumberDetails(List<BetDetail> betNumberDetails) {
		BetNumberDetails = betNumberDetails;
	}

	public List<BetDetail> getBetVirtualSportDetails() {
		return BetVirtualSportDetails;
	}

	public void setBetVirtualSportDetails(List<BetDetail> betVirtualSportDetails) {
		BetVirtualSportDetails = betVirtualSportDetails;
	}

	public List<BetDetail> getBetCasinoDetails() {
		return BetCasinoDetails;
	}

	public void setBetCasinoDetails(List<BetDetail> betCasinoDetails) {
		BetCasinoDetails = betCasinoDetails;
	}

}
