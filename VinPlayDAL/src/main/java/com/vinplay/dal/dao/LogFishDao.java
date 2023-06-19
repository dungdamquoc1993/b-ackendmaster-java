package com.vinplay.dal.dao;

import java.util.List;
import java.util.Map;

import com.vinplay.dal.entities.fish.FishGameRecord;

public interface LogFishDao {
	public Map<String, Object> search(String nickName, String timeStart, String timeEnd, int page);
	
	public FishGameRecord findItem(Integer id,Integer roomId, String gId, String mUid, String endTime) throws Exception;
	
	public boolean Save(FishGameRecord entity) throws Exception;
	
	boolean insert(FishGameRecord entity) throws Exception;
	
	boolean update(FishGameRecord entity) throws Exception;
	
	public Long getLastUpdateTime();
}
