package com.vinplay.item;

public class QuestionItem implements java.io.Serializable{
	private static final long serialVersionUID = -9014203642046770308L;
	private String id;
	private String title;
	private String content;
	private String addtime;
	private String addby;
	private String addip;
	private int status;
	private int readtimes;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getReadtimes() {
		return readtimes;
	}
	public void setReadtimes(int readtimes) {
		this.readtimes = readtimes;
	}
}
