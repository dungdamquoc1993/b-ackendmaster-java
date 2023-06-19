package com.vinplay.api.backend.processors.agent;

import com.vinplay.dal.dao.AgentDAO;
import com.vinplay.dal.dao.impl.AgentDAOImpl;
import com.vinplay.dal.entities.agent.UserAgentModel;
import com.vinplay.dal.utils.AgentUtils;
import com.vinplay.payment.utils.Constant;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.response.BaseResponse;
import org.apache.commons.lang.StringUtils;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Date;

public class AddNewUserAgentChildProcessor implements BaseProcessor<HttpServletRequest, String> {
	@Override
	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();
        String serPath = request.getServletPath();
        if(serPath == null || serPath.trim().isEmpty() || serPath != "/api_agent"){
            return BaseResponse.error(Constant.ERROR_PARAM, "Not allow access this api");
        }

        String parentCode = request.getParameter("code");
        String username = request.getParameter("un");
		String nickname = request.getParameter("nn");
		String password = request.getParameter("ps");
		String nameagent = request.getParameter("na");
		String address = request.getParameter("adr");
		String phone = request.getParameter("ph");
		String email = request.getParameter("em");
		String facebook = request.getParameter("fa");
		String namebank = request.getParameter("nb");
		
		if(StringUtils.isBlank(username))
			return BaseResponse.error(Constant.ERROR_PARAM, "Username can not empty");
		
		if(StringUtils.isBlank(nickname))
			return BaseResponse.error(Constant.ERROR_PARAM, "Nickname can not empty");
		
		if(StringUtils.isBlank(password))
			return BaseResponse.error(Constant.ERROR_PARAM, "Password can not empty");
		
		if(StringUtils.isBlank(nameagent))
			return BaseResponse.error(Constant.ERROR_PARAM, "Name agent can not empty");
		
		if(StringUtils.isBlank(parentCode))
			return BaseResponse.error(Constant.ERROR_PARAM, "Code of agent parent can not empty");
		
		AgentDAO dao = new AgentDAOImpl();
		UserAgentModel agentParent = new UserAgentModel();
		try {
			agentParent = dao.DetailUserAgentByCode(parentCode);
		} catch (SQLException e) { }
		
		if(agentParent == null)
			return BaseResponse.error("1002", "Code of agent parent is invalid");
		
//		Integer show = 1, active = 1;
//		try {
//			show = Integer.parseInt(request.getParameter("sh"));
//		} catch (NumberFormatException e) { show = 1;}
//		
//		try {
//			active = Integer.parseInt(request.getParameter("ac"));
//		} catch (NumberFormatException e) { active = 1;}
		
		UserService userService = new UserServiceImpl();
		UserModel userModel = new UserModel();
		try {
			userModel = userService.getUserByUserName(username);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(userModel != null)
			return BaseResponse.error("-1", "Username đã tồn tại !");
		
		try {
			userModel = userService.getUserByNickName(nickname);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(userModel != null)
			return BaseResponse.error("-1", "Nickname đã tồn tại !");
		
		int codeLength = 6;
		try {
			codeLength = Integer.parseInt(request.getParameter("cl"));
		} catch (NumberFormatException e) { codeLength = 6; }
		
		UserAgentModel userAgentModel = new UserAgentModel();
		userAgentModel.setActive(0);
		userAgentModel.setAddress(address);
		userAgentModel.setCreatetime(new Date());
		userAgentModel.setEmail(email);
		userAgentModel.setFacebook(facebook);
		userAgentModel.setNameagent(nameagent);
		userAgentModel.setNamebank(namebank);
		userAgentModel.setNickname(nickname);
		userAgentModel.setParentid(agentParent.getId());
		userAgentModel.setPassword(password);
		userAgentModel.setPhone(phone);
		userAgentModel.setShow(1);
		userAgentModel.setUpdatetime(new Date());
		userAgentModel.setUsername(username);
		return AgentUtils.createAgent(userAgentModel, codeLength).toJson();
	}
}
