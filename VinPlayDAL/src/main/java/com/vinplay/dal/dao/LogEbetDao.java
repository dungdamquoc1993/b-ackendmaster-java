package com.vinplay.dal.dao;

import java.util.Map;

public interface LogEbetDao {
	public Map<String, Object> search(String nickName, String timeStart, String timeEnd, int flagTime, String ebetId, int page, int limitItem);
	public Object detail(String id);
}
