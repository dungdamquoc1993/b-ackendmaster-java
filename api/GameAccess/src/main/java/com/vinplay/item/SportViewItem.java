/**
 * Archie
 */
package com.vinplay.item;

/**
 * @author Archie
 *
 */
public class SportViewItem implements java.io.Serializable {

	/**
	 * 
	 */
	private Long id;
	private String team1_name;
	private String team2_name;
	private Integer status;
	private String description;
	private Double team1_rate_full;
	private Double team2_rate_full;
	private Double team1_rate_bet;
	private Double team2_rate_bet;
	private String team1_photo;
	private String team2_photo;
	private String startdate;
	
	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTeam1_name() {
		return team1_name;
	}

	public void setTeam1_name(String team1_name) {
		this.team1_name = team1_name;
	}

	public String getTeam2_name() {
		return team2_name;
	}

	public void setTeam2_name(String team2_name) {
		this.team2_name = team2_name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getTeam1_rate_full() {
		return team1_rate_full;
	}

	public void setTeam1_rate_full(Double team1_rate_full) {
		this.team1_rate_full = team1_rate_full;
	}

	public Double getTeam2_rate_full() {
		return team2_rate_full;
	}

	public void setTeam2_rate_full(Double team2_rate_full) {
		this.team2_rate_full = team2_rate_full;
	}

	public Double getTeam1_rate_bet() {
		return team1_rate_bet;
	}

	public void setTeam1_rate_bet(Double team1_rate_bet) {
		this.team1_rate_bet = team1_rate_bet;
	}

	public Double getTeam2_rate_bet() {
		return team2_rate_bet;
	}

	public void setTeam2_rate_bet(Double team2_rate_bet) {
		this.team2_rate_bet = team2_rate_bet;
	}

	public String getTeam1_photo() {
		return team1_photo;
	}

	public void setTeam1_photo(String team1_photo) {
		this.team1_photo = team1_photo;
	}

	public String getTeam2_photo() {
		return team2_photo;
	}

	public void setTeam2_photo(String team2_photo) {
		this.team2_photo = team2_photo;
	}

}
