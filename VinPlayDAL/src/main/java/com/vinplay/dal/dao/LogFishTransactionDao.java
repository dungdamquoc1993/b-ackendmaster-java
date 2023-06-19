package com.vinplay.dal.dao;

import java.util.List;
import java.util.Map;

import com.vinplay.dal.entities.fish.FishGameRecord;
import com.vinplay.dal.entities.fish.FishTransaction;

public interface LogFishTransactionDao {
	public List<Map<String, Object>> search(String nickName, String orderId, String timeStart, String timeEnd, int page);
	
	public FishGameRecord findItem(String orderId) throws Exception;
	
	public boolean Save(FishTransaction entity) throws Exception;
}
