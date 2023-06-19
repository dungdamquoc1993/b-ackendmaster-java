package com.vinplay.item;

import java.io.Serializable;

public class VipUserItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5737441426311858716L;
	
	private String loginname;
	
	private String period;
	
	private String agent;
	
	private String id_vip_term;
	
	private Double total_bet;
	
	private Double surplus_amount_daily;
	
	private Double total_point;
	
	private Double bonus_weekly;
	
	private Integer status_payout_weekly;
	
	private Double bonus_month;
	
	private Integer status_payout_month;
	
	private Integer type;

	private String lastupdatetime;
	
	private Double total_bet_all;
	
	private Double total_point_all;
	
	private Integer currentlevelvip;
	
	private Integer monthlevelvip;
	
	private String auditvip;
	
	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getId_vip_term() {
		return id_vip_term;
	}

	public void setId_vip_term(String id_vip_term) {
		this.id_vip_term = id_vip_term;
	}

	public Double getTotal_bet() {
		return total_bet;
	}

	public void setTotal_bet(Double total_bet) {
		this.total_bet = total_bet;
	}

	public Double getSurplus_amount_daily() {
		return surplus_amount_daily;
	}

	public void setSurplus_amount_daily(Double surplus_amount_daily) {
		this.surplus_amount_daily = surplus_amount_daily;
	}


	public Double getBonus_weekly() {
		return bonus_weekly;
	}

	public void setBonus_weekly(Double bonus_weekly) {
		this.bonus_weekly = bonus_weekly;
	}

	public Integer getStatus_payout_weekly() {
		return status_payout_weekly;
	}

	public void setStatus_payout_weekly(Integer status_payout_weekly) {
		this.status_payout_weekly = status_payout_weekly;
	}

	public Double getBonus_month() {
		return bonus_month;
	}

	public void setBonus_month(Double bonus_month) {
		this.bonus_month = bonus_month;
	}

	public Integer getStatus_payout_month() {
		return status_payout_month;
	}

	public void setStatus_payout_month(Integer status_payout_month) {
		this.status_payout_month = status_payout_month;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getLastupdatetime() {
		return lastupdatetime;
	}

	public void setLastupdatetime(String lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}

	public Double getTotal_bet_all() {
		return total_bet_all;
	}

	public void setTotal_bet_all(Double total_bet_all) {
		this.total_bet_all = total_bet_all;
	}


	public Integer getCurrentlevelvip() {
		return currentlevelvip;
	}

	public void setCurrentlevelvip(Integer currentlevelvip) {
		this.currentlevelvip = currentlevelvip;
	}

	public Integer getMonthlevelvip() {
		return monthlevelvip;
	}

	public void setMonthlevelvip(Integer monthlevelvip) {
		this.monthlevelvip = monthlevelvip;
	}

	public Double getTotal_point() {
		return total_point;
	}

	public void setTotal_point(Double total_point) {
		this.total_point = total_point;
	}

	public Double getTotal_point_all() {
		return total_point_all;
	}

	public void setTotal_point_all(Double total_point_all) {
		this.total_point_all = total_point_all;
	}

	public String getAuditvip() {
		return auditvip;
	}

	public void setAuditvip(String auditvip) {
		this.auditvip = auditvip;
	}

}
