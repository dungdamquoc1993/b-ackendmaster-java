package com.vinplay.dao.ag;

import com.vinplay.dto.ag.AGGamesReportsDetailData;
import com.vinplay.item.AGUserItem;

public interface AgDao {
	boolean createUserNoMapping(String agId, String agPassword, int agCountId);

	AGUserItem mappingUser(String nickName);
	
	boolean saveRecord(AGGamesReportsDetailData detailData);
	
}
