package com.vinplay.api.backend.processors.game3rd;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.vinplay.dal.dao.LogEbetDao;
import com.vinplay.dal.dao.impl.LogEbetDaoImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

public class EbetLogSearchProcessor implements BaseProcessor<HttpServletRequest, String> {
	@Override
	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();
		String nickname = request.getParameter("nn");
		String fromTime = request.getParameter("ft");
		String endTime = request.getParameter("et");
		String ebetId = request.getParameter("eid");

		int page;
		try {
			page = Integer.parseInt(request.getParameter("pg"));
		} catch (NumberFormatException e) {
			page = 1;
		}

		int limitItem;
		try {
			limitItem = Integer.parseInt(request.getParameter("mi"));
		} catch (NumberFormatException e) {
			limitItem = 50;
		}

		int flagTime = -1;
		try {
			flagTime = Integer.parseInt(request.getParameter("flt"));
		} catch (NumberFormatException e) { }

		LogEbetDao dao = new LogEbetDaoImpl();
		try {
			String id = request.getParameter("id");
			if (null == id) {
				Map<String, Object> data = dao.search(nickname, fromTime, endTime, flagTime, ebetId, page, limitItem);
				if (data == null || data.isEmpty())
					return BaseResponse.success(null, 0);

				int totalRecord = (int) data.get("totalRecord");
				data.remove("totalRecord");
				return BaseResponse.success(data, totalRecord);
			}else {
				Object data = dao.detail(id);
				if (data == null)
					return BaseResponse.success(null, 0);
				
				return BaseResponse.success(data, 1);
			}
		} catch (Exception e) {
			return BaseResponse.error("-1", e.getMessage());
		}
	}
}