package com.vinplay.item;

import java.io.Serializable;
import java.util.List;

/**
 * 角色明细
 * 
 * @author Donny
 *
 */
public class RoleItem implements Serializable{

	private static final long serialVersionUID = 8189345026736471054L;

	/**
	 * 角色id
	 */
	private String roleid;
	
	/**
	 * 角色名
	 */
	private String rolename;
	
	/**
	 * 角色描述
	 */
	private String roledesc;
	
	/**
	 * 角色对应的菜单
	 */
	private List<MenuItem> menuItems;

	
	public List<MenuItem> getMenuItems() {
		return menuItems;
	}

	public void setMenuItems(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getRoledesc() {
		return roledesc;
	}

	public void setRoledesc(String roledesc) {
		this.roledesc = roledesc;
	}


}
