package com.vinplay.item;

public class MessageItem implements java.io.Serializable{
	private static final long serialVersionUID = -495192300728312670L;
	private Integer id;
	private String recipient;
	private String sender;
	private String title="";
	private String content="";
	private String sendtime;
	private String rectime;
	private Integer viewtimes;
	private Integer status;
	private String ip;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
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
	public String getSendtime() {
		return sendtime;
	}
	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}
	public String getRectime() {
		return rectime;
	}
	public void setRectime(String rectime) {
		this.rectime = rectime;
	}
	public Integer getViewtimes() {
		return viewtimes;
	}
	public void setViewtimes(Integer viewtimes) {
		this.viewtimes = viewtimes;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
}
