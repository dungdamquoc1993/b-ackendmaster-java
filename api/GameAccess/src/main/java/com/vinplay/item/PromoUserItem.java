package com.vinplay.item;

public class PromoUserItem implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4630364896007702881L;
	
	private Integer promoid;
	private String loginname;
	private String applytime;
	private String category;
	private String title;
	private String extra;
	
	private Integer viplevel;
	private String regtime;
	
	
	public Integer getPromoid() {
		return promoid;
	}
	public void setPromoid(Integer promoid) {
		this.promoid = promoid;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getApplytime() {
		return applytime;
	}
	public void setApplytime(String applytime) {
		this.applytime = applytime;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}
	public Integer getViplevel() {
		return viplevel;
	}
	public void setViplevel(Integer viplevel) {
		this.viplevel = viplevel;
	}
	public String getRegtime() {
		return regtime;
	}
	public void setRegtime(String regtime) {
		this.regtime = regtime;
	}
	

}
