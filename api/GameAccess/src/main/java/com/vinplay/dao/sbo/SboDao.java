package com.vinplay.dao.sbo;

import java.sql.SQLException;
import java.util.List;

import com.vinplay.dto.sbo.SboRecordDetail;
import com.vinplay.dto.sbo.SboUserDto;

public interface SboDao {
	// mongo
	public boolean saveSboBetLog(SboRecordDetail record) throws Exception;

	// mysql
	List<Integer> maxSboUser() throws Exception;

	public boolean generateSboUser(String sboId, int sbocountid) throws Exception;

	SboUserDto findSboUserByNickName(String nickName) throws SQLException;

	SboUserDto findSboUserBySboId(String sboId) throws SQLException;

	SboUserDto mappingUserSbo(String nickName) throws SQLException;

}
