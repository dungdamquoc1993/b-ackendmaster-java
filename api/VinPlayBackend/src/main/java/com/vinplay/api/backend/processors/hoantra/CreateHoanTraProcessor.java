package com.vinplay.api.backend.processors.hoantra;

import com.vinplay.hoantra.service.HoanTraService;
import com.vinplay.hoantra.service.impl.HoanTraServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;
import com.vinplay.vbee.common.utils.DateTimeUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

public class CreateHoanTraProcessor implements BaseProcessor<HttpServletRequest, String> {
	private static final Logger logger = Logger.getLogger("backend");
	
	private HoanTraService hoantraService = new HoanTraServiceImpl();
	
	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();
		String date = request.getParameter("date");
		String createBy = request.getParameter("nn");
		try {
			Float rate = Float.parseFloat(request.getParameter("rate"));
			// validation data
			if (StringUtils.isBlank(date)) {
				return BaseResponse.error("99", "date is null or empty");
			}
			if (StringUtils.isBlank(createBy)) {
				return BaseResponse.error("99", "createBy is null or empty");
			}
			if (rate > 10f) {
				return BaseResponse.error("90", "Tỉ lệ quá lớn");
			}
			//check date >= currentdate -> fail
			Date inputDate = DateTimeUtils.string2Date(date, "yyyy-MM-dd");
			int compare = DateTimeUtils.compareDate(new Date(), inputDate);
			if (compare != 1) {
				return BaseResponse.error("1", "Chỉ được chọn hoàn trả cho các ngày cũ ");
			}
			//delete old data
			java.sql.Date sqlDate = new java.sql.Date(inputDate.getTime());
			//generation data
			hoantraService.generateAllHoanTra(sqlDate);
			
			return BaseResponse.success(null, 0);
		} catch (Exception e) {
			logger.error(e);
			return BaseResponse.error("-1", e.getMessage());
		}
	}

}
