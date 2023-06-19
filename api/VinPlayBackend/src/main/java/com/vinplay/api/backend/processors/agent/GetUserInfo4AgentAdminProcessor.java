package com.vinplay.api.backend.processors.agent;

import com.vinplay.dal.dao.AgentDAO;
import com.vinplay.dal.dao.impl.AgentDAOImpl;
import com.vinplay.dal.entities.agent.UserAgentModel;
import com.vinplay.payment.utils.Constant;
import com.vinplay.usercore.service.impl.UserForAdminServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.models.UserAdminInfo;
import com.vinplay.vbee.common.response.BaseResponse;
import com.vinplay.vbee.common.response.ResultUserReponse;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class GetUserInfo4AgentAdminProcessor
implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger((String)"backend");

    public String execute(Param<HttpServletRequest> param) {
    	HttpServletRequest request = (HttpServletRequest)param.get();
    	String serPath = request.getServletPath();

        if(serPath == null || serPath.trim().isEmpty() || serPath != "/api_agent"){
            return BaseResponse.error(Constant.ERROR_PARAM, "Not allow access this api");
        }
        
        ResultUserReponse response = new ResultUserReponse(false, "1001");
        String agentCode = request.getParameter("rc");
        if(StringUtils.isBlank(agentCode))
			return BaseResponse.error(Constant.ERROR_PARAM, "Code of agent can not empty");
        
        AgentDAO agentDao = new AgentDAOImpl();
		UserAgentModel userAgentModel = new UserAgentModel();
		try {
			userAgentModel = agentDao.DetailUserAgentByCode(agentCode);
		} catch (SQLException e1) {
			userAgentModel = null;
			e1.printStackTrace();
		}
		if (userAgentModel == null) {
			return BaseResponse.error(Constant.ERROR_PARAM, "Code of agent is invalid");
		}
        
        String userName = userAgentModel.getUsername();
        String nickName = userAgentModel.getNickname();
        int page = Integer.parseInt(StringUtils.isBlank(request.getParameter("p")) ? "1" : request.getParameter("p"));
        int total = Integer.parseInt(StringUtils.isBlank(request.getParameter("tr")) ? "10" : request.getParameter("tr"));
        if (page < 0) {
            return response.toJson();
        }
        
        if (total < 0) {
            return response.toJson();
        }
        
        UserForAdminServiceImpl service = new UserForAdminServiceImpl();
        try {
            List<UserAdminInfo> trans = service.searchUserAdmin(userName, nickName, null, null, null, "1", null, null, page, total, null, "-1", null, agentCode);
            int totalRecord = service.countSearchUserAdmin(userName, nickName, null, null, null, "1", null, null, null, agentCode);
            double totalPages = Math.ceil( (double)totalRecord/(double)total);
            response.setTotal((long)totalPages);
            response.setTotalRecord((long)totalRecord);
            response.setTransactions(trans);
            response.setSuccess(true);
            response.setErrorCode("0");
        }
        catch (SQLException e) {
            logger.debug((Object)e);
        }
        return response.toJson();
    }
}

