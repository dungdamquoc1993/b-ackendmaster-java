package com.vinplay.item;

import java.io.Serializable;
/**
 * 菜单角色关联信息
 * 
 * @author Donny
 *
 */
public class RoleMenuItem implements Serializable{

	private static final long serialVersionUID = 4597715161624330910L;

	/**
	 * 角色id
	 */
	private String roleid;
	
	/**
	 * 菜单id
	 */
	private String menuid;

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}
}
