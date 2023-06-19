package com.vinplay.api.backend.processors.banner;

import javax.servlet.http.HttpServletRequest;

import com.vinplay.dal.dao.BannerDAO;
import com.vinplay.dal.dao.impl.BannerDAOImpl;
import com.vinplay.dal.entities.banner.BannerModel;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

public class BannerDetailProcessor implements BaseProcessor<HttpServletRequest, String> {
	@Override
	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();

		Integer id = null;
		try {
			id = Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
			
		}
		try {
			BannerDAO dao = new BannerDAOImpl();
			if (id != null) {
				BannerModel banner = dao.BannerDetail(id);
				return BaseResponse.success(null, "", banner);
			} else {
				return BaseResponse.error("-1", "id null");
			}
		} catch (Exception e) {
			return BaseResponse.error("-1", e.getMessage());
		}
	}
}