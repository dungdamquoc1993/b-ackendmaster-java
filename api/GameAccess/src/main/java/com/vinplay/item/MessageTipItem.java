package com.vinplay.item;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class MessageTipItem implements Serializable{
	
	private static final long serialVersionUID = 4646421772271829368L;
	
	private String title;
	private String content ;
	private String sender ;
	private String reciever ;
	private String type ;
	
	/**
	 * 失效时间
	 */
	private String expireTime;
	
	/**
	 * 已经推送过的用户
	 */
	private Set<String> sendedUser = new HashSet<>();
	
	
	public Set<String> getSendedUser() {
		return sendedUser;
	}
	public void setSendedUser(Set<String> sendedUser) {
		this.sendedUser = sendedUser;
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
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReciever() {
		return reciever;
	}
	public void setReciever(String reciever) {
		this.reciever = reciever;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}
	
	

}
