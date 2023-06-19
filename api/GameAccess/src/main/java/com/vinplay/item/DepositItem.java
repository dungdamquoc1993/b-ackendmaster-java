package com.vinplay.item;

import java.util.ArrayList;
import java.util.List;

public class DepositItem implements java.io.Serializable{
	private static final long serialVersionUID = 1612633508580587949L;
	private String id;
	private Integer fatherid;
	private String fatherflag;
	private String loginname;
	private Double amount;
	private String savetime;
	private Integer status;
	private String remark;
	private String sign;
	private String dptime;
	private String dpname;
	private String dpmethod;
	private String lastnumber;
	private Integer cardid;
	private Integer type;
	private String updatetime;
	private String recname;
	private String recbank;
	private String recbankcard;
	private String flgmark;
	private String fadmin;
	
	private String adminuser1=null;
	private String adminuser2=null;
	
	private List<String> listChuY = new ArrayList<String>();
	
	private String bankstmid;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getSavetime() {
		return savetime;
	}
	public void setSavetime(String savetime) {
		this.savetime = savetime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getDptime() {
		return dptime;
	}
	public void setDptime(String dptime) {
		this.dptime = dptime;
	}
	public String getDpname() {
		return dpname;
	}
	public void setDpname(String dpname) {
		this.dpname = dpname;
	}
	public String getDpmethod() {
		return dpmethod;
	}
	public void setDpmethod(String dpmethod) {
		this.dpmethod = dpmethod;
	}
	public String getLastnumber() {
		return lastnumber;
	}
	public void setLastnumber(String lastnumber) {
		this.lastnumber = lastnumber;
	}
	public Integer getCardid() {
		return cardid;
	}
	public void setCardid(Integer cardid) {
		this.cardid = cardid;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getRecname() {
		return recname;
	}
	public void setRecname(String recname) {
		this.recname = recname;
	}
	public String getRecbank() {
		return recbank;
	}
	public void setRecbank(String recbank) {
		this.recbank = recbank;
	}
	public String getRecbankcard() {
		return recbankcard;
	}
	public void setRecbankcard(String recbankcard) {
		this.recbankcard = recbankcard;
	}
	public String getAdminuser1() {
		return adminuser1;
	}
	public void setAdminuser1(String adminuser1) {
		this.adminuser1 = adminuser1;
	}
	public String getAdminuser2() {
		return adminuser2;
	}
	public void setAdminuser2(String adminuser2) {
		this.adminuser2 = adminuser2;
	}
	public String getBankstmid() {
		return bankstmid;
	}
	public void setBankstmid(String bankstmid) {
		this.bankstmid = bankstmid;
	}
	public String getFlgmark() {
		return flgmark;
	}
	public void setFlgmark(String flgmark) {
		this.flgmark = flgmark;
	}
	public String getFadmin() {
		return fadmin;
	}
	public void setFadmin(String fadmin) {
		this.fadmin = fadmin;
	}
	public List<String> getListChuY() {
		return listChuY;
	}
	public void setListChuY(List<String> listChuY) {
		this.listChuY = listChuY;
	}

}
