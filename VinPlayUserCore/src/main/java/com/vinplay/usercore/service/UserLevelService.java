package com.vinplay.usercore.service;

import java.util.Map;
import com.vinplay.usercore.entities.UserLevel;

public interface UserLevelService {
	public String create(String nickname, String nickname_parent);
	
	public String create(UserLevel userLevel);
	
	public String update(String oldNickname, String newNickname);
	
	public UserLevel getByNickName(String nickname, String parent_user);
	
	public UserLevel getByNickName(String nickname);
	
	public Map<String, Object> findChilds(String nickname, String statDate, String endDate, int pageIndex, int limit);
}

