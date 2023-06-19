package com.vinplay.dal.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.vinplay.dal.dao.LogTaiXiuDAO;
import com.vinplay.dal.dao.impl.LogTaiXiuDAOImpl;
import com.vinplay.dal.service.LogTaiXiuService;
import com.vinplay.vbee.common.response.TaiXiuDetailReponse;
import com.vinplay.vbee.common.response.TaiXiuResponse;
import com.vinplay.vbee.common.response.TaiXiuResultResponse;

public class LogTaiXiuServiceImpl implements LogTaiXiuService {

	private LogTaiXiuDAO dao = new LogTaiXiuDAOImpl();

	@Override
	public int deleteLogTaiXiuByDay(int soNgay) throws SQLException {
		return dao.deleteLogTaiXiuByDay(soNgay);
	}

	@Override
	public List<TaiXiuResponse> listLogTaiXiu(String referentId, String userName, String betSide, String moneyType,
			String timeStart, String timeEnd, String isBot, int page) throws SQLException {
		return dao.listLogTaiXiu(referentId, userName, betSide, moneyType, timeStart, timeEnd, isBot, page);
	}

	@Override
	public int countLogTaiXiu(String referentId, String userName, String betSide, String moneyType, String timeStart,
			String timeEnd, String isBot) throws SQLException {
		return dao.countLogTaiXiu(referentId, userName, betSide, moneyType, timeStart, timeEnd, isBot);
	}

	@Override
	public Map<String, Integer> countPlayerLogTaiXiu(String referentId, String userName, String betSide,
			String moneyType, String timeStart, String timeEnd, String isBot) throws SQLException {
		return dao.countPlayerLogTaiXiu(referentId, userName, betSide, moneyType, timeStart, timeEnd, isBot);
	}

	@Override
	public List<TaiXiuDetailReponse> getLogTaiXiuDetail(String referent_id, String betSide, String moneyType,
			String nickName, int botType, int page) throws SQLException {
		return dao.getLogTaiXiuDetail(referent_id, betSide, moneyType, nickName, botType, page);
	}

	@Override
	public int countLogTaiXiuDetail(String referent_id, String betSide, String moneyType, String nickName, int botType)
			throws SQLException {
		return dao.countLogTaiXiuDetail(referent_id, betSide, moneyType, nickName, botType);
	}

	@Override
	public List<TaiXiuResultResponse> listLogTaiXiuResult(String referentId, String moneyType, String timeStart,
			String timeEnd, int page) throws SQLException {
		return dao.listLogTaiXiuResult(referentId, moneyType, timeStart, timeEnd, page);
	}

	@Override
	public int countLogTaiXiuResult(String referentId, String moneyType, String timeStart, String timeEnd)
			throws SQLException {
		return dao.countLogTaiXiuResult(referentId, moneyType, timeStart, timeEnd);
	}
}
