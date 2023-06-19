package com.vinplay.dto.ibc2;

import com.vinplay.dto.ibc.BaseRespDto;

import java.io.Serializable;
import java.util.Date;

public class GetGameDetailRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = 5378628522521435168L;
	private Date match_id;
	private String league_id;
	private String home_id;
	private String away_id;
	private String ht_home_score;
	private String ht_away_score;
	private String game_status;
	private Integer sport_type;
	private Integer Is_neutral;
	public Date getMatch_id() {
		return match_id;
	}
	public void setMatch_id(Date match_id) {
		this.match_id = match_id;
	}
	public String getLeague_id() {
		return league_id;
	}
	public void setLeague_id(String league_id) {
		this.league_id = league_id;
	}
	public String getHome_id() {
		return home_id;
	}
	public void setHome_id(String home_id) {
		this.home_id = home_id;
	}
	public String getAway_id() {
		return away_id;
	}
	public void setAway_id(String away_id) {
		this.away_id = away_id;
	}
	public String getHt_home_score() {
		return ht_home_score;
	}
	public void setHt_home_score(String ht_home_score) {
		this.ht_home_score = ht_home_score;
	}
	public String getHt_away_score() {
		return ht_away_score;
	}
	public void setHt_away_score(String ht_away_score) {
		this.ht_away_score = ht_away_score;
	}
	public String getGame_status() {
		return game_status;
	}
	public void setGame_status(String game_status) {
		this.game_status = game_status;
	}
	public Integer getSport_type() {
		return sport_type;
	}
	public void setSport_type(Integer sport_type) {
		this.sport_type = sport_type;
	}
	public Integer getIs_neutral() {
		return Is_neutral;
	}
	public void setIs_neutral(Integer is_neutral) {
		Is_neutral = is_neutral;
	}
	
	
}
