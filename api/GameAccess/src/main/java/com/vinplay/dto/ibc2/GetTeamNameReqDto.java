package com.vinplay.dto.ibc2;


import java.io.Serializable;

public class GetTeamNameReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = 6819113785563685938L;
	private String team_id;
	private String bet_type;

	public String getTeam_id() {
		return team_id;
	}

	public void setTeam_id(String team_id) {
		this.team_id = team_id;
	}

	public String getBet_type() {
		return bet_type;
	}

	public void setBet_type(String bet_type) {
		this.bet_type = bet_type;
	}
}
