package com.vinplay.item;

public class AdminBankJoinItem implements java.io.Serializable{
	private static final long serialVersionUID = 2453564818885734898L;
	private int groupid;
	private int cardid;
	private int ord;
	public int getGroupid() {
		return groupid;
	}
	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}
	public int getCardid() {
		return cardid;
	}
	public void setCardid(int cardid) {
		this.cardid = cardid;
	}
	public int getOrd() {
		return ord;
	}
	public void setOrd(int ord) {
		this.ord = ord;
	}
}
