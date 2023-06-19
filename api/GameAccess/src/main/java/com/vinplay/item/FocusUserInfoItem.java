package com.vinplay.item;

import java.io.Serializable;

/**
 * 后台消息提示item
 * 
 * @author Donny
 *
 */
public class FocusUserInfoItem implements Serializable{

	private static final long serialVersionUID = 8129486307678072706L;

	/**
	 * 关注用户名
	 */
	private String loginName;
	
	/**
	 * 登录次数
	 */
	private Integer loginTimes;
	
	/**
	 * 充值次数
	 */
	private Integer chargeTimes;
	
	/**
	 * 投注次数
	 */
	private Integer betTimes;
	
	/**
	 * 是否清除标志
	 */
	private Boolean flag;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Integer getLoginTimes() {
		return loginTimes;
	}

	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}

	public Integer getChargeTimes() {
		return chargeTimes;
	}

	public void setChargeTimes(Integer chargeTimes) {
		this.chargeTimes = chargeTimes;
	}

	public Integer getBetTimes() {
		return betTimes;
	}

	public void setBetTimes(Integer betTimes) {
		this.betTimes = betTimes;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}
	
	
}
