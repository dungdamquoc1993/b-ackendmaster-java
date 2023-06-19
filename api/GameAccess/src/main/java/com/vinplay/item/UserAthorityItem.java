package com.vinplay.item;

import java.io.Serializable;

/**
 * 用户权限关联信息
 * 
 * @author Donny
 *
 */
public class UserAthorityItem implements Serializable{

	private static final long serialVersionUID = 1518042551627278622L;

	/**
	 * 用户id
	 */
	private String userid;
	
	/**
	 *  权限id
	 */
	private String athorityid;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getAthorityid() {
		return athorityid;
	}

	public void setAthorityid(String athorityid) {
		this.athorityid = athorityid;
	}

	
}
