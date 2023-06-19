package com.vinplay.api.backend.processors.agent;

import com.vinplay.dal.dao.AgentDAO;
import com.vinplay.dal.dao.impl.AgentDAOImpl;
import com.vinplay.dal.entities.agent.UserAgentModel;
import com.vinplay.dal.utils.AgentUtils;
import com.vinplay.payment.utils.Constant;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetChilds4AgentProcessor implements BaseProcessor<HttpServletRequest, String> {
	@Override
	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();
		String serPath = request.getServletPath();
		if (serPath == null || serPath.trim().isEmpty() || serPath != "/api_agent") {
			return BaseResponse.error(Constant.ERROR_PARAM, "Not allow access this api");
		}

		String keyword = request.getParameter("key");
		String code = request.getParameter("code");
		if (StringUtils.isBlank(code))
			return BaseResponse.error(Constant.ERROR_PARAM, "Code of agent can not empty");

		AgentDAO dao = new AgentDAOImpl();
		UserAgentModel currentAgent = new UserAgentModel();
		try {
			currentAgent = dao.DetailUserAgentByCode(code);
		} catch (SQLException e) {
		}

		if (currentAgent == null)
			return BaseResponse.error("1002", "Code of agent parent is invalid");

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
		int level = currentAgent.getLevel() + 1;
		try {
			level = Integer.parseInt(levelStr);
		} catch (Exception e) { }

		try {
			return AgentUtils.searchChilds(currentAgent.getId(), keyword, level, page, maxItem).toJson();
		} catch (Exception e) {
			return BaseResponse.success(new ArrayList<>(), 0);
		}
	}
}
