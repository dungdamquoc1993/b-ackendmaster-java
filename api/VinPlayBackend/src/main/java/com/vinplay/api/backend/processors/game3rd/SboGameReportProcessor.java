package com.vinplay.api.backend.processors.game3rd;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.vinplay.common.game3rd.IbcGameRecordItem;
import com.vinplay.common.game3rd.SboGameRecord;
import com.vinplay.common.game3rd.ThirdPartyResponse;
import com.vinplay.payment.utils.Constant;
import com.vinplay.report.service.ThirdPartyGameReport;
import com.vinplay.report.service.impl.ThirdPartyGameReportImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

public class SboGameReportProcessor implements BaseProcessor<HttpServletRequest, String> {
	private static final Logger logger = Logger.getLogger("backend");

	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();

		String nick_name = request.getParameter("nn");
		String playername = request.getParameter("pn");
		String sporttype = request.getParameter("sporttype");
		String fromTime = request.getParameter("ft");
		String endTime = request.getParameter("et");
		String refno= request.getParameter("refno");
		String status= request.getParameter("status"); 
		int flagTime= Integer.parseInt(request.getParameter("flagTime")); 
		Double betAmount = request.getParameter("betAmount") != null
				? Double.parseDouble(request.getParameter("betAmount"))
				: null;
		Double winAmount = request.getParameter("winAmount") != null
				? Double.parseDouble(request.getParameter("winAmount"))
				: null;
		Integer page = request.getParameter("pg") != null ? Integer.parseInt(request.getParameter("pg")) : 0;
		Integer maxItem = request.getParameter("mi") != null ? Integer.parseInt(request.getParameter("mi")) : 10;

		logger.info("Request SBO report nickName= " + nick_name + ", refno: " + refno);
		ThirdPartyGameReport service = new ThirdPartyGameReportImpl();
		try {
			ThirdPartyResponse<List<SboGameRecord>> res = service.filterSBO(playername, nick_name, refno, status,
					flagTime, fromTime, endTime, betAmount, winAmount, page, maxItem, sporttype);
			if (res != null) {
				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String json = ow.writeValueAsString(res);
				return new BaseResponse<String>().success(json);
			} else {
				return BaseResponse.error(Constant.ERROR_SYSTEM, "null");
			}

		} catch (Exception e) {
			logger.error(e);
			return BaseResponse.error(Constant.ERROR_SYSTEM, e.getMessage());
		}

	}
}
