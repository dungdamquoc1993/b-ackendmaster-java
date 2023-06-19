package com.vinplay.item;

import java.io.Serializable;

import com.vinplay.item.UserItem;

public class UserCreationVo implements Serializable{

	private static final long serialVersionUID = 7763157101588211912L;

	private String gameType;
	
	private UserItem userItem;

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public UserItem getUserItem() {
		return userItem;
	}

	public void setUserItem(UserItem userItem) {
		this.userItem = userItem;
	}

}