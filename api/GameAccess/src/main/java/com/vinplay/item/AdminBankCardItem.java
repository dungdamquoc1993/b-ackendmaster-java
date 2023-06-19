package com.vinplay.item;

public class AdminBankCardItem implements java.io.Serializable{
	private static final long serialVersionUID = 772491993761544990L;
	private int id;
	private int type;
	private String icon;
	private String bankcard;
	private String name;
	private String showbankcard;
	private int status;
	private String remark;
	private int minamount;
	private int maxamount;
	private String tab;
	private Integer active;
	private String accid;
	private Integer fee = 0;
	
	private String address;
	private int ord=0;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getBankcard() {
		return bankcard;
	}
	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShowbankcard() {
		return showbankcard;
	}
	public void setShowbankcard(String showbankcard) {
		this.showbankcard = showbankcard;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTab() {
		return tab;
	}
	public void setTab(String tab) {
		this.tab = tab;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getMinamount() {
		return minamount;
	}
	public void setMinamount(int minamount) {
		this.minamount = minamount;
	}
	public int getMaxamount() {
		return maxamount;
	}
	public void setMaxamount(int maxamount) {
		this.maxamount = maxamount;
	}
	public int getOrd() {
		return ord;
	}
	public void setOrd(int ord) {
		this.ord = ord;
	}
	public Integer getActive() {
		return active;
	}
	public void setActive(Integer active) {
		this.active = active;
	}
	public String getAccid() {
		return accid;
	}
	public void setAccid(String accid) {
		this.accid = accid;
	}
	public Integer getFee() {
		return fee;
	}
	public void setFee(Integer fee) {
		this.fee = fee;
	}
}
