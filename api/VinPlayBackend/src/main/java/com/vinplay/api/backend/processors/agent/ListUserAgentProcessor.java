package com.vinplay.api.backend.processors.agent;

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

public class ListUserAgentProcessor implements BaseProcessor<HttpServletRequest, String> {
	@Override
    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();
		String keyword = request.getParameter("key");
		String code = request.getParameter("code");
		AgentDAO dao = new AgentDAOImpl();
		UserAgentModel currentAgent = new UserAgentModel();
		try {
			currentAgent = dao.DetailUserAgentByCode(code);
		} catch (SQLException e) { currentAgent = null; }

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
	
//    @Override
//    public String execute(Param<HttpServletRequest> param) {
//        HttpServletRequest request = param.get();
////        String serPath = request.getServletPath();
////        if(serPath == null || serPath.trim().isEmpty() || serPath != "/api_agent"){
////            return BaseResponse.error(Constant.ERROR_PARAM, "Not allow access this api");
////        }
//
//        String username = request.getParameter("un");
//        String nickname = request.getParameter("nn");
//        String nameagent = request.getParameter("na");
//        String address = request.getParameter("adr");
//        String phone = request.getParameter("ph");
//        String email = request.getParameter("em");
//        String facebook = request.getParameter("fa");
//        String status = request.getParameter("sts");
//        String namebank = request.getParameter("nb");
//
//        int page = 1, maxItem = 10;
//        Integer show = null, active = null;
//        try {
//            show = Integer.parseInt(request.getParameter("sh"));
//        } catch (NumberFormatException e) {
//        }
//        try {
//            active = Integer.parseInt(request.getParameter("ac"));
//        } catch (NumberFormatException e) {
//        }
//        try {
//            page = Integer.parseInt(request.getParameter("pg"));
//        } catch (NumberFormatException e) {
//        }
//        try {
//            maxItem = Integer.parseInt(request.getParameter("mi"));
//        } catch (NumberFormatException e) {
//        }
//
//        AgentDAO dao = new AgentDAOImpl();
//        try {
//            List<UserAgentModel> agents = dao.listUserAgent(username, nickname, null, nameagent, address, phone, email,
//                    facebook, null, status, null, namebank, null, null,
//                    show, active, null, null, null, null, null,
//                    null, null, null, null, null, page, maxItem);
//            long totalRecord = dao.countlistUserAgent(username, nickname, null, nameagent, address, phone, email,
//                    facebook, null, status, null, namebank, null, null,
//                    show, active, null, null, null, null, null,
//                    null, null, null, null, null);
//            return BaseResponse.success(agents, totalRecord);
//
//        }
//        catch (Exception e) {
//            return BaseResponse.error("-1", e.getMessage());
//        }
//    }
}