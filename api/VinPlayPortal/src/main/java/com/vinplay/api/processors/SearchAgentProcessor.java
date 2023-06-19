package com.vinplay.api.processors;

import com.vinplay.dal.dao.AgentDAO;
import com.vinplay.dal.dao.impl.AgentDAOImpl;
import com.vinplay.dal.entities.agent.UserAgentModel;
import com.vinplay.dal.utils.AgentUtils;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;

public class SearchAgentProcessor implements BaseProcessor<HttpServletRequest, String> {
	@Override
	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();
		String keyword = request.getParameter("key");
		String code = request.getParameter("code");
		AgentDAO dao = new AgentDAOImpl();
		UserAgentModel currentAgent = new UserAgentModel();
		try {
			currentAgent = dao.DetailUserAgentByCode(code);
		} catch (SQLException e) {
			currentAgent = null;
		}

		int page, maxItem;
		try {
			page = Integer.parseInt(request.getParameter("pg"));
		} catch (NumberFormatException e) {
			page = 1;
		}

		try {
			maxItem = Integer.parseInt(request.getParameter("mi"));
		} catch (NumberFormatException e) {
			maxItem = 10;
		}

		String levelStr = request.getParameter("lv");
		int level = currentAgent == null ? -1 : currentAgent.getLevel() + 1;
		try {
			level = Integer.parseInt(levelStr);
		} catch (Exception e) { }

		try {
			return AgentUtils.searchChilds(currentAgent == null ? -1 : currentAgent.getId(), keyword, level, page, maxItem).toJson();
		} catch (Exception e) {
			return BaseResponse.success(new ArrayList<>(), 0);
		}
	}
}
