package com.vinplay.interfaces.wm;

import org.apache.log4j.Logger;

import com.vinplay.dto.wm.BaseResponseDto;
import com.vinplay.dto.wm.GetDateTimeReportReqDto;
import com.vinplay.dto.wm.GetDateTimeReportResult;

public class ReportWMService extends BaseWmService {
	private static final Logger logger = Logger.getLogger(ReportWMService.class);

	@SuppressWarnings("unchecked")
	public BaseResponseDto<GetDateTimeReportResult[]> getDateTimeReport(GetDateTimeReportReqDto req) {
		logger.info("--REQUEST-- " + gson.toJson(req));
		String data = getData("GetDateTimeReport", req);
		BaseResponseDto<GetDateTimeReportResult[]> resDto = gson.fromJson(data, BaseResponseDto.class);
		logger.info("--RESPONSE-- " + gson.toJson(resDto));
		return convertJsonToData(resDto, GetDateTimeReportResult[].class);
	}
}
