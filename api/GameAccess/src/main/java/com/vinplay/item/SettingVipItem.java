package com.vinplay.item;

import java.io.Serializable;

public class SettingVipItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -536277344141377860L;
	
	private String id_vip_term;
	
	private String vip_name;
	
	private Integer level_vip;
	
	private String bonus_level;
	
	private String base_bonus_weekly;
	
	private String base_accumulated_points_level;
	
	private String base_accumulated_points_weekly;
	
	private String createtime;
	
	private String createby;

	private String lastupdatetime;
	
	private String lastupdateby;

	public String getId_vip_term() {
		return id_vip_term;
	}

	public void setId_vip_term(String id_vip_term) {
		this.id_vip_term = id_vip_term;
	}

	public String getVip_name() {
		return vip_name;
	}

	public void setVip_name(String vip_name) {
		this.vip_name = vip_name;
	}

	public Integer getLevel_vip() {
		return level_vip;
	}

	public void setLevel_vip(Integer level_vip) {
		this.level_vip = level_vip;
	}


	public String getBonus_level() {
		return bonus_level;
	}

	public void setBonus_level(String bonus_level) {
		this.bonus_level = bonus_level;
	}

	public String getBase_bonus_weekly() {
		return base_bonus_weekly;
	}

	public void setBase_bonus_weekly(String base_bonus_weekly) {
		this.base_bonus_weekly = base_bonus_weekly;
	}

	public String getBase_accumulated_points_level() {
		return base_accumulated_points_level;
	}

	public void setBase_accumulated_points_level(String base_accumulated_points_level) {
		this.base_accumulated_points_level = base_accumulated_points_level;
	}

	public String getBase_accumulated_points_weekly() {
		return base_accumulated_points_weekly;
	}

	public void setBase_accumulated_points_weekly(String base_accumulated_points_weekly) {
		this.base_accumulated_points_weekly = base_accumulated_points_weekly;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getCreateby() {
		return createby;
	}

	public void setCreateby(String createby) {
		this.createby = createby;
	}

	public String getLastupdatetime() {
		return lastupdatetime;
	}

	public void setLastupdatetime(String lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}

	public String getLastupdateby() {
		return lastupdateby;
	}

	public void setLastupdateby(String lastupdateby) {
		this.lastupdateby = lastupdateby;
	}

}
