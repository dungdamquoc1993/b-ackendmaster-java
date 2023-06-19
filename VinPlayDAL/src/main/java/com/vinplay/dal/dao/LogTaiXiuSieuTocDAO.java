package com.vinplay.dal.dao;

import com.vinplay.dal.entities.taixiu.LogTaiXiuSieuToc;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface LogTaiXiuSieuTocDAO {
    public List<LogTaiXiuSieuToc> search(String fromTime, String endTime, int status, int page, int maxItem) throws SQLException;
    public Map<String, Object> getDetailByLogId(long logId, String fromTime, String endTime, int status,
                                                         int type, int userType, String nickname, int page, int maxItem) throws SQLException;
}

