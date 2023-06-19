package com.vinplay.dal.service.impl;

import java.util.List;
import java.util.Map;

import com.vinplay.dal.dao.LogBauCuaDAO;
import com.vinplay.dal.dao.impl.LogBauCuaDAOImpl;
import com.vinplay.dal.service.LogBauCuaService;
import com.vinplay.vbee.common.response.BauCuaReponseDetail;
import com.vinplay.vbee.common.response.BauCuaResponse;
import com.vinplay.vbee.common.response.BauCuaResultResponse;

public class LogBauCuaServiceImpl implements LogBauCuaService {
	private LogBauCuaDAO dao = new LogBauCuaDAOImpl();
    @Override
    public List<BauCuaResponse> listLogBauCua(String referent_id, String nickName, String room, String timeStart, String timeEnd, String moneyType, int page) {
        return dao.listLogBauCua(referent_id, nickName, room, timeStart, timeEnd, moneyType, page);
    }

    @Override
    public int countLogBauCua(String referent_id, String nickName, String room, String timeStart, String timeEnd, String moneyType) {
        return dao.countLogBauCua(referent_id, nickName, room, timeStart, timeEnd, moneyType);
    }

    @Override
    public Map countMapLogBauCua(String referent_id, String nickName, String room, String timeStart, String timeEnd, String moneyType) {
        return dao.countMapLogBauCua(referent_id, nickName, room, timeStart, timeEnd, moneyType);
    }

    @Override
    public List<BauCuaReponseDetail> getLogBauCuaDetail(String referent_id, int page) {
        return dao.getLogBauCuaDetail(referent_id, page);
    }

    @Override
    public int countLogBauCuaDetail(String referent_id) {
        return dao.countLogBauCuaDetail(referent_id);
    }

    @Override
    public List<BauCuaResultResponse> listLogBauCauResult(String referent_id, String room, String timeStart, String timeEnd, int page) {
        return dao.listLogBauCauResult(referent_id, room, timeStart, timeEnd, page);
    }

    @Override
    public long countLogBauCauResult(String referent_id, String room, String timeStart, String timeEnd) {
        return dao.countLogBauCauResult(referent_id, room, timeStart, timeEnd);
    }
}

