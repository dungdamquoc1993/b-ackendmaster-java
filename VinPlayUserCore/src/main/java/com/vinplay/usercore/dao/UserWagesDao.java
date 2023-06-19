package com.vinplay.usercore.dao;

import java.sql.SQLException;
import java.util.Map;

import com.vinplay.usercore.entities.UserWages;

public interface UserWagesDao {
	public String insert(UserWages userWages) throws SQLException;
	
	public boolean insertByJob(String date) throws SQLException;

	public String update(UserWages userWages) throws SQLException;

	public String updateStatus(long id, int status) throws SQLException;
	
	public String updateAllStatusToReceivedBonus(String nickname) throws SQLException ;
	
	public long getSumBonusByStatus(String nickname, int status) throws SQLException;

	public UserWages getById(long id) throws SQLException;
	
	public UserWages getByDate(String date) throws SQLException;

	public Map<String, Object> history(String nickname, String startDate, String endDate, int status, int pageIndex,
			int limit) throws SQLException;
}
