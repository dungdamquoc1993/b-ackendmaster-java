package com.vinplay.usercore.service;

import java.util.Map;

public interface UserWagesService {
//	public String create(UserWages userWages);
	
	public boolean insertByJob(String date);
	
	public String receivedMoney(long id);
	
	public String receivedAllMoney(String nickname);
	
	public Map<String, Object> history(String nickname, String statDate, String endDate, int status, int pageIndex, int limit);
}

