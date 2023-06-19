package com.vinplay.dto.ibc2;


import java.io.Serializable;

public class GetLeagueNameReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = -5144203057048127419L;
	private String league_id;

	public String getLeague_id() {
		return league_id;
	}

	public void setLeague_id(String league_id) {
		this.league_id = league_id;
	}

}
