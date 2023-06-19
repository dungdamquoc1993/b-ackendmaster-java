package com.vinplay.report.service;

import java.util.List;

import com.vinplay.dal.entities.report.LogCountUserPlay;
import com.vinplay.vbee.common.response.BaseResponse;

public interface CountReportService {
	BaseResponse<List<LogCountUserPlay>> getLogReportModelSQL(String nickName, String fromTime ,String endtime, int page , int totalrecord);

}
