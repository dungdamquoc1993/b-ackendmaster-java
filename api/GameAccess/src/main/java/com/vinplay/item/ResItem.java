package com.vinplay.item;

public class ResItem implements java.io.Serializable{
	private static final long serialVersionUID = -2195640114082777726L;
	private int flag;
	private String msg;
	public ResItem(int f,String m){
		flag = f;
		msg = m;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
