package com.vinplay.api.processors;

import com.vinplay.usercore.service.MailBoxService;
import com.vinplay.usercore.service.impl.MailBoxServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponseModel;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

public class CountMailStatusProcessor implements BaseProcessor<HttpServletRequest, String> {
	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = (HttpServletRequest) param.get();
		BaseResponseModel response = new BaseResponseModel(false, "1001");
		String nn = request.getParameter("nn");
		if (nn != null && !nn.isEmpty()) {
			MailBoxService service = new MailBoxServiceImpl();
			int mailnotread;
			try {
				mailnotread = service.countMailBoxInActive(nn);
				response.setErrorCode("0");
				response.setSuccess(true);
				response.setData(mailnotread);
				return response.toJson();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return "MISSING PARAMETTER";
	}
}
