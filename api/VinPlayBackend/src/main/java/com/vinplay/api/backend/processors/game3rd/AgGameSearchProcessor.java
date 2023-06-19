package com.vinplay.api.backend.processors.game3rd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.vinplay.common.game3rd.AGGameRecordItem;
import com.vinplay.common.game3rd.ThirdPartyResponse;
import com.vinplay.payment.utils.Constant;
import com.vinplay.report.service.ThirdPartyGameReport;
import com.vinplay.report.service.impl.ThirdPartyGameReportImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class AgGameSearchProcessor implements BaseProcessor<HttpServletRequest, String> {
	private static final Logger logger = Logger.getLogger(AgGameSearchProcessor.class);

	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();

		String nickName = request.getParameter("nn");
		String billNo = request.getParameter("bn");
		String gameCode = request.getParameter("gc");
		String fromTime = request.getParameter("ft");
		String endTime = request.getParameter("et");
		String gameType = request.getParameter("gt");

		int page = 1, maxItem = 10, flagTime = 0;
		Double betAmount = null, winAmount = null;
		try {
			String ba = request.getParameter("ba");
			betAmount = Double.parseDouble(ba);
		} catch (Exception e) {
			betAmount = null;
		}

		try {
			String wa = request.getParameter("wa");
			winAmount = Double.parseDouble(wa);
		} catch (Exception e) {
			winAmount = null;
		}

		try {
			flagTime = Integer.parseInt(request.getParameter("flt"));
		} catch (NumberFormatException e) {
			// TODO: handle exception
		}
		try {
			page = Integer.parseInt(request.getParameter("pg"));
		} catch (NumberFormatException e) {
			// TODO: handle exception
		}
		try {
			maxItem = Integer.parseInt(request.getParameter("mi"));
		} catch (NumberFormatException e) {
			// TODO: handle exception
		}

		logger.info("Request AG report nickName= " + nickName + ", billNo: " + billNo);
		ThirdPartyGameReport service = new ThirdPartyGameReportImpl();
		try {
			// TODO: Check role
			ThirdPartyResponse<List<AGGameRecordItem>> res = null;
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
