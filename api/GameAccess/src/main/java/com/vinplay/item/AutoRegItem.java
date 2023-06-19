package com.vinplay.item;

public class AutoRegItem implements java.io.Serializable{
	private static final long serialVersionUID = -3127166146352171458L;
	private String loginname;
	private Integer model;
	private Double pointssc;
	private Double point115;
	private Double pointdp;
	private String savetime;
	private Integer fatherid;
	private String fatherflag;
	private String saveip;
	private String sign;
	
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public Integer getModel() {
		return model;
	}
	public void setModel(Integer model) {
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
	public String getSavetime() {
		return savetime;
	}
	public void setSavetime(String savetime) {
		this.savetime = savetime;
	}
	public Integer getFatherid() {
		return fatherid;
	}
	public void setFatherid(Integer fatherid) {
		this.fatherid = fatherid;
	}
	public String getFatherflag() {
		return fatherflag;
	}
	public void setFatherflag(String fatherflag) {
		this.fatherflag = fatherflag;
	}
	public String getSaveip() {
		return saveip;
	}
	public void setSaveip(String saveip) {
		this.saveip = saveip;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
}
