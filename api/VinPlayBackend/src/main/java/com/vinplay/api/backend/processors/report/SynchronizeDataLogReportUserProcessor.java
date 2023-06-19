package com.vinplay.api.backend.processors.report;

import com.vinplay.dal.dao.AgentDAO;
import com.vinplay.dal.dao.impl.AgentDAOImpl;
import com.vinplay.payment.utils.Constant;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SynchronizeDataLogReportUserProcessor implements BaseProcessor<HttpServletRequest, String> {
	private static final Logger logger = Logger.getLogger((String) "api_agent");

	public String execute(Param<HttpServletRequest> param) {
		try {
			com.vinplay.api.backend.response.BaseResponse response = new com.vinplay.api.backend.response.BaseResponse(
					false, "1001");
			HttpServletRequest request = (HttpServletRequest) param.get();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String ft = request.getParameter("ft");
			if (ft == null || ft.trim().isEmpty()) {
				return BaseResponse.error(Constant.ERROR_PARAM, "Start time not empty");
			}

			String et = request.getParameter("et");
			if (et == null || et.trim().isEmpty()) {
				return BaseResponse.error(Constant.ERROR_PARAM, "End time not empty");
			}

			Date fromDate = null;
			try {
				fromDate = dateFormat.parse(ft); 
			} catch (ParseException e1) {
				return BaseResponse.error(Constant.ERROR_PARAM, "Start time is invalid");
			}

			Date endDate = null;
			try {
				endDate = dateFormat.parse(et);
			} catch (ParseException e1) {
				return BaseResponse.error(Constant.ERROR_PARAM, "End time is invalid");
			}

			if (endDate.getTime() < fromDate.getTime())
				return BaseResponse.error(Constant.ERROR_PARAM, "End time must greater than start time");

			// Game name: icb | wm | ag
			String gameName = request.getParameter("gn");
			if (gameName == null || gameName.trim().isEmpty()) {
				return BaseResponse.error(Constant.ERROR_PARAM, "Game name not empty");
			}

			List<String> gameNames = Arrays.asList("ibc", "wm", "ag");
			if (!gameNames.contains(gameName)) {
				return BaseResponse.error(Constant.ERROR_PARAM, "Game name only in: " + String.join(",", gameNames));
			}

			gameName = gameName.toLowerCase();
			List<Map<String, Object>> data = new ArrayList<>();
			int total = 0;
			AgentDAO dao = new AgentDAOImpl();
			if (endDate.equals(fromDate)) {
				List<Map<String, Object>> result = new ArrayList<>();
				result = dao.SynDataLogGameToReport(dateFormat.format(fromDate), gameName);
				Map<String, Object> map = new HashMap<>();
				map.put(dateFormat.format(fromDate), result);
				data.add(map);
				total = result == null ? 0 : result.size();
				response.setData(data);
				response.setTotalData(total);
				response.setErrorCode("0");
				response.setSuccess(true);
				return response.toJson();
			}
			
			long dayCount = TimeUnit.DAYS.convert(endDate.getTime() - fromDate.getTime(), TimeUnit.MILLISECONDS);
			for (int i = 0; i <= dayCount; i++) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(fromDate);
				cal.add(Calendar.DAY_OF_YEAR, i);
				List<Map<String, Object>> result = new ArrayList<>();
				result = dao.SynDataLogGameToReport(dateFormat.format(cal.getTime()), gameName);
				Map<String, Object> map = new HashMap<>();
				map.put(dateFormat.format(cal.getTime()), result);
				data.add(map);
				total += result == null ? 0 : result.size();
			}
			
			response.setData(data);
			response.setTotalData(total);
			response.setErrorCode("0");
			response.setSuccess(true);
			return response.toJson();
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error((Object) e);
			return "{\"success\":false,\"errorCode\":\"1001\"}";
		}
	}
}