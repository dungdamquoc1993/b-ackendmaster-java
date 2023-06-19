package com.vinplay.item;

import java.io.Serializable;

public class DSGameRecordItem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3014831107828316138L;

	private String id;
	
	private String loginname;
	
	private String member;
	
	private String bet_at;
	
	private String finisha_at;
	
	private String finisha_time;
	
	private String agent;
	
	private String game_id;
	
	private String game_serial;
	
	private Integer game_type;
	
	private Long round_id;
	
	private Double bet_amount;
	
	private Double payout_amount;
	
	private Double valid_amount;
	
	private Integer status;
	
	private Double fee_amount;
	
	private String bet_at_utc7;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getBet_at() {
		return bet_at;
	}

	public void setBet_at(String bet_at) {
		this.bet_at = bet_at;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getGame_id() {
		return game_id;
	}

	public void setGame_id(String game_id) {
		this.game_id = game_id;
	}

	public String getGame_serial() {
		return game_serial;
	}

	public void setGame_serial(String game_serial) {
		this.game_serial = game_serial;
	}

	public Integer getGame_type() {
		return game_type;
	}

	public void setGame_type(Integer game_type) {
		this.game_type = game_type;
	}

	public Long getRound_id() {
		return round_id;
	}

	public void setRound_id(Long round_id) {
		this.round_id = round_id;
	}

	public Double getBet_amount() {
		return bet_amount;
	}

	public void setBet_amount(Double bet_amount) {
		this.bet_amount = bet_amount;
	}

	public Double getPayout_amount() {
		return payout_amount;
	}

	public void setPayout_amount(Double payout_amount) {
		this.payout_amount = payout_amount;
	}

	public Double getValid_amount() {
		return valid_amount;
	}

	public void setValid_amount(Double valid_amount) {
		this.valid_amount = valid_amount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getFee_amount() {
		return fee_amount;
	}

	public void setFee_amount(Double fee_amount) {
		this.fee_amount = fee_amount;
	}

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}

	public String getFinisha_at() {
		return finisha_at;
	}

	public void setFinisha_at(String finisha_at) {
		this.finisha_at = finisha_at;
	}

	public String getFinisha_time() {
		return finisha_time;
	}

	public void setFinisha_time(String finisha_time) {
		this.finisha_time = finisha_time;
	}

	public String getBet_at_utc7() {
		return bet_at_utc7;
	}

	public void setBet_at_utc7(String bet_at_utc7) {
		this.bet_at_utc7 = bet_at_utc7;
	}

}
