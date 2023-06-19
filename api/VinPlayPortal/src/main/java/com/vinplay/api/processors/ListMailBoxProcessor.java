package com.vinplay.api.processors;

import com.vinplay.usercore.service.MailBoxService;
import com.vinplay.usercore.service.impl.MailBoxServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.ListMailBoxResponse;
import com.vinplay.vbee.common.response.MailBoxResponse;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class ListMailBoxProcessor implements BaseProcessor<HttpServletRequest, String> {
	private static final Logger logger = Logger.getLogger("api");

	public String execute(Param<HttpServletRequest> param) {
		ListMailBoxResponse response = new ListMailBoxResponse(false, "1001");
		HttpServletRequest request = (HttpServletRequest) param.get();
		String nickName = request.getParameter("nn");
		int page = Integer.parseInt(request.getParameter("p"));
		int total = 0;
		int totalPages = 0;
		if (page < 0) {
			return response.toJson();
		}
		MailBoxService service = new MailBoxServiceImpl();
		try {
			List<MailBoxResponse> trans = service.listMailBox(nickName, page);
			int mailnotread = service.countMailBoxInActive(nickName);
			if (trans != null && !trans.isEmpty()) {
				total = service.countMailBox(nickName);
				totalPages = total % 5 == 0 ? total / 5 : total / 5 + 1;
				response.setMailNotRead(mailnotread);
				response.setTotalPages(totalPages);
				response.setSuccess(true);
				response.setTransactions(trans);
				response.setErrorCode("0");
			} else {
				response.setTotalPages(0);
				response.setMailNotRead(0);
				response.setSuccess(true);
				response.setErrorCode("10001");
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return response.toJson();
	}
}
