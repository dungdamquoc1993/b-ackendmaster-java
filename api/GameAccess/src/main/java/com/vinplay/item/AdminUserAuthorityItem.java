package com.vinplay.item;

import java.util.List;
/**
 * 用户角色权限等详细信息
 * 
 * @author Donny
 *
 */
public class AdminUserAuthorityItem extends AdminUserItem {

	private static final long serialVersionUID = -860701857281206846L;

	
	/**
	 * 角色信息
	 */
	private RoleItem roleItem;
	
	/**
	 * 菜单
	 */
	private List<MenuItem> menuItems;
	
	/**
	 * 权限信息
	 */
	private List<AuthorityItem> athorityItmes;



	public RoleItem getRoleItem() {
		return roleItem;
	}


	public void setRoleItem(RoleItem roleItem) {
		this.roleItem = roleItem;
	}


	public List<MenuItem> getMenuItems() {
		return menuItems;
	}


	public void setMenuItems(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}


	public List<AuthorityItem> getAthorityItmes() {
		return athorityItmes;
	}


	public void setAthorityItmes(List<AuthorityItem> athorityItmes) {
		this.athorityItmes = athorityItmes;
	}
	
	
	
}
