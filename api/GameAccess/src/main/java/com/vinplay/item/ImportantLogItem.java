package com.vinplay.item;

public class ImportantLogItem implements java.io.Serializable{
	private static final long serialVersionUID = 2842031669473930572L;
	private String title;
	private String msg;
	private String addtime;
	private String addby;
	private String addip;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getAddby() {
		return addby;
	}
	public void setAddby(String addby) {
		this.addby = addby;
	}
	public String getAddip() {
		return addip;
	}
	public void setAddip(String addip) {
		this.addip = addip;
	}
}
