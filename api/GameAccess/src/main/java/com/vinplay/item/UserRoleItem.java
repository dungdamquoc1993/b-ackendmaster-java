package com.vinplay.item;

import java.io.Serializable;
/**
 * 用户角色对应信息
 * 
 * @author Donny
 *
 */
public class UserRoleItem implements Serializable{

	private static final long serialVersionUID = 4458705682933199617L;

	/**
	 * 用户id（对应ADMINUSER的LOGINNAME）
	 */
	private String userid;
	
	/**
	 * 角色id
	 */
	private String roleid;

	
	
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	
}
