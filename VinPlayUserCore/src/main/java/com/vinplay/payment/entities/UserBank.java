package com.vinplay.payment.entities;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserBank implements Serializable{
	
	private Long id;
	private Integer userId;
	private String nickName;
	private String bankName;
	private String customerName;
	private String bankNumber;
	private Integer status;
	private Timestamp createDate;
	private String branch;
	private Timestamp updateDate;
	private String lastEditor;
	

	public String getLastEditor() {
		return lastEditor;
	}

	public void setLastEditor(String lastEditor) {
		this.lastEditor = lastEditor;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getBankNumber() {
		return bankNumber;
	}

	public void setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public UserBank(Long id, Integer userId, String nickName, String bankName, String customerName, String bankNumber,
			Integer status, Timestamp createDate, String branch, Timestamp updateDate ,String lastEditor) {
		super();
		this.id = id;
		this.userId = userId;
		this.nickName = nickName;
		this.bankName = bankName;
		this.customerName = customerName;
		this.bankNumber = bankNumber;
		this.status = status;
		this.createDate = createDate;
		this.branch = branch;
		this.updateDate = updateDate;
		this.lastEditor = lastEditor;
	}

	public UserBank() {
		super();
	}

}
