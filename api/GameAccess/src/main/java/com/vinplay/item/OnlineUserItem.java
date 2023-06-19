package com.vinplay.item;

import java.io.Serializable;
import java.util.Date;

/**
 * 在线用户item
 *
 */
public class OnlineUserItem implements Comparable<OnlineUserItem>,Serializable{

	private static final long serialVersionUID = 8173836945446666446L;

	/**
	 * 用户信息
	 */
	private UserItem userItem;
	
	/**
	 * 最好活跃时间
	 */
	private Date lastTime;

	
	public UserItem getUserItem() {
		return userItem;
	}

	public void setUserItem(UserItem userItem) {
		this.userItem = userItem;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	/**
	 * 排序用，按最后活跃时间从近到远排序
	 */
	public int compareTo(OnlineUserItem o) {
		
		if (this.lastTime.getTime() < o.getLastTime().getTime()) {
			return -1;
		}
		
		if (this.lastTime.getTime() > o.getLastTime().getTime()) {
			return 1;
		}
		return 0;
	}
	
}
