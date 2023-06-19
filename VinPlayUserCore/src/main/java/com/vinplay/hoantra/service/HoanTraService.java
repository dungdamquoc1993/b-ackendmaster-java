package com.vinplay.hoantra.service;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import com.vinplay.vbee.common.models.HoanTraModel;

public interface HoanTraService {

	public List<HoanTraModel> getMoneyHoanTra(Date date) throws SQLException;

	public List<HoanTraModel> getListHoanTra(java.sql.Date date, String nick_name) throws SQLException;

	public List<HoanTraModel> getListHoanTraHistories(java.sql.Date date, String nick_name, int page, int limit)
			throws SQLException;

	public int updateHoanTra(HoanTraModel hoanTraModel, Boolean isSuccess, String message) throws SQLException;

	public int deleteHoanTra(Date date, Boolean send_success) throws SQLException;

	public int insertHoanTraList(List<HoanTraModel> listHoanTraModel) throws SQLException;

	public int[] generateAllHoanTra(Date date) throws SQLException;

	public long countListHoanTraHistories(java.sql.Date date, String nick_name) throws SQLException;

	public long countListHoanTra(java.sql.Date date, String nick_name) throws SQLException;

	public int insertHoanTraHistory(HoanTraModel hoanTraModel, Boolean isSuccess, String responseAddHoantra)
			throws SQLException;
}
