package com.vinplay.dao.ebet;

import java.sql.SQLException;
import java.util.List;

import com.vinplay.dto.ebet.BetHistoriesDto;
import com.vinplay.dto.ebet.EbetUserItem;


public interface EbetDao {
	// mongo
	public boolean saveEbetBetLog(List<BetHistoriesDto> betHistory) throws Exception;

	// mysql
	List<Integer> maxEbetUser() throws Exception;

	public boolean generateEbetUser(String ebetid, int ebetcountid,String password) throws Exception;

	EbetUserItem findEbetUserByNickName(String nickName) throws SQLException;

	EbetUserItem findEbetUserByEbetId(String ebetId) throws SQLException;

	EbetUserItem mappingUserEbet(String nickName) throws SQLException;

}
