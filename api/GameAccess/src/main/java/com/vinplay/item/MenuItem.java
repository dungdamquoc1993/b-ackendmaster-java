package com.vinplay.item;

import java.io.Serializable;
/**
 * 菜单明细
 * 
 * @author Donny
 *
 */
public class MenuItem implements Serializable{

	private static final long serialVersionUID = 4483605647329642574L;

	/**
	 * 菜单id
	 */
	private String menuid;
	
	/**
	 * 菜单名 
	 */
	private String menuname;
	
	/**
	 * 父级菜单id
	 */
	private String fathermenu;
	
	/**
	 * 图标链接
	 */
	private String iconurl;
	
	/**
	 * 菜单url
	 */
	private String url;
	
	/**
	 * open标示
	 */
	private Integer open;

	
	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}



	public String getMenuname() {
		return menuname;
	}

	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}

	public String getFathermenu() {
		return fathermenu;
	}

	public void setFathermenu(String fathermenu) {
		this.fathermenu = fathermenu;
	}

	public String getIconurl() {
		return iconurl;
	}

	public void setIconurl(String iconurl) {
		this.iconurl = iconurl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getOpen() {
		return open;
	}

	public void setOpen(Integer open) {
		this.open = open;
	}
}
