package com.vinplay.item;

import java.io.Serializable;

public class WdAutoTransferItem implements Serializable{

	private static final long serialVersionUID = -3284659195794177048L;
	
	private String id;
	private String loginname;      
	private String bankname;       
	private Double amount;
	private String bankaccountname; 
	private String bankaccountno;   
	private String bankmobileno;    
	private String bankprovince;    
	private String bankcity; 
	private String bankbranchname;  
	private String optip;           
	private String optuser;        
	private Integer status;        
	private String remark;          
	private String paymenttype;     
	private String opttime;         
	private String finishtime;
	
	private String tid;
	
	
	  
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getBankaccountname() {
		return bankaccountname;
	}
	public void setBankaccountname(String bankaccountname) {
		this.bankaccountname = bankaccountname;
	}
	public String getBankaccountno() {
		return bankaccountno;
	}
	public void setBankaccountno(String bankaccountno) {
		this.bankaccountno = bankaccountno;
	}
	public String getBankmobileno() {
		return bankmobileno;
	}
	public void setBankmobileno(String bankmobileno) {
		this.bankmobileno = bankmobileno;
	}
	public String getBankprovince() {
		return bankprovince;
	}
	public void setBankprovince(String bankprovince) {
		this.bankprovince = bankprovince;
	}
	public String getBankcity() {
		return bankcity;
	}
	public void setBankcity(String bankcity) {
		this.bankcity = bankcity;
	}
	public String getBankbranchname() {
		return bankbranchname;
	}
	public void setBankbranchname(String bankbranchname) {
		this.bankbranchname = bankbranchname;
	}
	public String getOptip() {
		return optip;
	}
	public void setOptip(String optip) {
		this.optip = optip;
	}
	public String getOptuser() {
		return optuser;
	}
	public void setOptuser(String optuser) {
		this.optuser = optuser;
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
	public String getPaymenttype() {
		return paymenttype;
	}
	public void setPaymenttype(String paymenttype) {
		this.paymenttype = paymenttype;
	}
	public String getOpttime() {
		return opttime;
	}
	public void setOpttime(String opttime) {
		this.opttime = opttime;
	}
	public String getFinishtime() {
		return finishtime;
	}
	public void setFinishtime(String finishtime) {
		this.finishtime = finishtime;
	}
}
