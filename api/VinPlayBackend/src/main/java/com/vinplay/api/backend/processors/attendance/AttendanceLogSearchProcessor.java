package com.vinplay.api.backend.processors.attendance;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.vinplay.usercore.entities.AttendanceConfig;
import com.vinplay.usercore.service.AttendanceService;
import com.vinplay.usercore.service.impl.AttendanceServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

public class AttendanceLogSearchProcessor implements BaseProcessor<HttpServletRequest, String> {
    @Override
    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();
        String nickname = request.getParameter("nn");

        int pageIndex = 1;
		try {
			pageIndex = Integer.parseInt(request.getParameter("pg"));
		}catch (Exception e) {
			pageIndex = 1;
		}
		
		int limitItem = 10;
		try {
			limitItem = Integer.parseInt(request.getParameter("mi"));
		}catch (Exception e) {
			limitItem = 10;
		}

        try {
        	AttendanceService attendanceService = new AttendanceServiceImpl();
			AttendanceConfig attendanceConfig = attendanceService.getConfigLastest();
			Map<String, Object> map = new HashMap<>();
			map = attendanceService.search(attendanceConfig.getId(), nickname, "", "", pageIndex, limitItem);
        	long totalRecord = Long.parseLong(map.get("totalRecord").toString());
        	map.remove("totalRecord");
            return BaseResponse.success(map, totalRecord);
        }
        catch (Exception e) {
            return BaseResponse.error("1001", e.getMessage());
        }
    }
}