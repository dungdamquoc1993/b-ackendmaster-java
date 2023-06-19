package com.vinplay.item;

public class AgentItem implements java.io.Serializable{
	private static final long serialVersionUID = -8032644911968667535L;
	private int id;
	private String name;
	private String remark;
	private String inserttime;
	private String contact;
	private String key;
	private Double rate;
	private String loginurl;
	private String uiwebip;
	private int model;
	private Double pointssc;
	private Double point115;
	private Double pointdp;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public String getLoginurl() {
		return loginurl;
	}
	public void setLoginurl(String loginurl) {
		this.loginurl = loginurl;
	}
	public int getModel() {
		return model;
	}
	public void setModel(int model) {
		this.model = model;
	}
	public Double getPointssc() {
		return pointssc;
	}
	public void setPointssc(Double pointssc) {
		this.pointssc = pointssc;
	}
	public Double getPoint115() {
		return point115;
	}
	public void setPoint115(Double point115) {
		this.point115 = point115;
	}
	public Double getPointdp() {
		return pointdp;
	}
	public void setPointdp(Double pointdp) {
		this.pointdp = pointdp;
	}
	public String getUiwebip() {
		return uiwebip;
	}
	public void setUiwebip(String uiwebip) {
		this.uiwebip = uiwebip;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getInserttime() {
		return inserttime;
	}
	public void setInserttime(String inserttime) {
		this.inserttime = inserttime;
	}
	
}
