package com.vinplay.api.processors.attendance;

import com.google.gson.Gson;
import com.vinplay.payment.utils.Constant;
import com.vinplay.usercore.entities.UserAttendance;
import com.vinplay.usercore.service.AttendanceService;
import com.vinplay.usercore.service.impl.AttendanceServiceImpl;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceProcessor implements BaseProcessor<HttpServletRequest, String> {
	private static final Logger logger = Logger.getLogger("portal");
	 private String getIpAddress(HttpServletRequest request) {
	        String ip = request.getHeader("X-FORWARDED-FOR");
	        if (ip == null) {
	            ip = request.getRemoteAddr();
	        }
	        
	        if (ip != null && !"".equals(ip)) {
				String[] arrayIp = ip.split(",");
				for (int i = 0; i < (arrayIp.length > 2 ? 2 : arrayIp.length); i++) {
					if (arrayIp[i].length() <= 40) {
						ip = arrayIp[i].trim();
						break;
					}
				}
			}
			
	        
	        return ip;
	    }
	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();
		String nickname = request.getParameter("nn");
		String accessToken = request.getParameter("at");
		String action = request.getParameter("ac");// get-receive-history
		if (nickname == null || nickname.trim().isEmpty())
			return BaseResponse.error(Constant.ERROR_PARAM, "Nickname can not empty");

		if (StringUtils.isBlank(accessToken))
			return BaseResponse.error(Constant.ERROR_PARAM, "Access token can not empty");

		if (StringUtils.isBlank(action))
			return BaseResponse.error(Constant.ERROR_PARAM, "Action can not empty");

		if (!"get".equals(action) && !"receive".equals(action) && !"history".equals(action))
			return BaseResponse.error(Constant.ERROR_PARAM, "Thao tác không đúng");

		UserService userService = new UserServiceImpl();
		boolean isToken = userService.isActiveToken(nickname, accessToken);
		if (!isToken) {
			return BaseResponse.error(Constant.ERROR_SESSION,
					"Your trading session is expired, please reload page and login again.");
		}
		String ip = getIpAddress(request);
		BaseResponse<Object> res = new BaseResponse<Object>();
		try {
			AttendanceService attendanceService = new AttendanceServiceImpl();
			Map<String, Object> map = new HashMap<>();
			List<Map<String, Object>> rs = new ArrayList<Map<String,Object>>();
			switch (action) {
				case "get":
					map = attendanceService.CheckAttendance(nickname,ip);
					break;
				case "receive":
					map = attendanceService.Attendance(nickname,ip);
					break;
				case "history":
					rs = attendanceService.historyInRound(nickname);
					break;
			}
			
			if("history".equals(action)) {
				res.setData(rs);
				res.setErrorCode(rs == null ? "1001" : "0");
				res.setMessage(null);
				res.setSuccess(rs == null ? false : true);
				return res.toJson();
			}
			
			String code = "";
			code = map.get("code").toString();
			String msg = "";
			msg = map.get("msg").toString();
			int consecutive = 0;
			if ("get".equals(action)) {
				consecutive = Integer.parseInt(map.get("consecutive").toString());
			}

			switch (code) {
				case "exist":
					res.setData("get".equals(action) ? consecutive : null);
					res.setErrorCode("1002");
					res.setMessage(msg);
					res.setSuccess(false);
					return res.toJson();
				case "invalid":
					res.setData("get".equals(action) ? consecutive : null);
					res.setErrorCode("1003");
					res.setMessage(msg);
					res.setSuccess(false);
					return res.toJson();
				case "success":
					Gson g = new Gson();
					if("receive".equals(action)) {
						UserAttendance p = g.fromJson(msg, UserAttendance.class);
						res.setData(p);
					}else
						res.setData(consecutive);
						
					res.setErrorCode("0");
					res.setMessage("get".equals(action) ? msg : null);
					res.setSuccess(true);
					return res.toJson();
				case "exception":
				case "failed":
				default:
					res.setData("get".equals(action) ? consecutive : null);
					res.setErrorCode("1001");
					res.setMessage(msg);
					res.setSuccess(false);
					return res.toJson();
			}
		} catch (Exception e) {
			logger.trace(e);
			res.setData("get".equals(action) ? 0 : null);
			res.setErrorCode("1001");
			res.setMessage("Exception, please contact customer care support for help.");
			res.setSuccess(false);
			return res.toJson();
		}
	}
}