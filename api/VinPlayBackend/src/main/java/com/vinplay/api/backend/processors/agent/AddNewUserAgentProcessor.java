package com.vinplay.api.backend.processors.agent;

import com.vinplay.api.backend.utils.CommonUtils;
import com.vinplay.dal.dao.AgentDAO;
import com.vinplay.dal.dao.impl.AgentDAOImpl;
import com.vinplay.dal.entities.agent.UserAgentModel;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.response.BaseResponse;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddNewUserAgentProcessor implements BaseProcessor<HttpServletRequest, String> {
	@Override
	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();
//        String serPath = request.getServletPath();
//        if(serPath == null || serPath.trim().isEmpty() || serPath != "/api_agent"){
//            return BaseResponse.error(Constant.ERROR_PARAM, "Not allow access this api");
//        }

		String username = request.getParameter("un");
		String nickname = request.getParameter("nn");
		String password = request.getParameter("ps");
		String nameagent = request.getParameter("na");
		String address = request.getParameter("adr");
		String phone = request.getParameter("ph");
		String email = request.getParameter("em");
		String facebook = request.getParameter("fa");
		String status = request.getParameter("sts");
		String namebank = request.getParameter("nb");
		Integer show = null, active = null;
		try {
			show = Integer.parseInt(request.getParameter("sh"));
		} catch (NumberFormatException e) { }
		
		try {
			active = Integer.parseInt(request.getParameter("ac"));
		} catch (NumberFormatException e) { }
		
		int codeLength = 6;
		try {
			codeLength = Integer.parseInt(request.getParameter("cl"));
		} catch (NumberFormatException e) { codeLength = 6; }
		
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
		
		String code = null;
		AgentDAO dao = new AgentDAOImpl();
		List<String> codes = new ArrayList<>();
		try {
			codes = dao.getListCode();
		} catch (SQLException e1) { }

		try {
			while (true) {
				code = CommonUtils.GenerateRandomNumber(codeLength);
				if(codes.size() == 0) break; 
				
				if (!codes.contains(code))
					break;
			}

			Boolean check = dao.AddNewUserAgent(username, nickname, password, nameagent, address, phone, email,
					facebook, null, status, -1, namebank, null, null, show, active, null, null, null, null, null,
					null, null, null, null, code);
			if (check) {
				UserAgentModel userAgentModelNew = dao.DetailUserAgentByCode(code);
				if(userAgentModelNew == null)
					return BaseResponse.error("-1", "Thêm mới không thành công !");
				
				userAgentModelNew.setPassword(password);
				boolean result = dao.AddNewUser(userAgentModelNew);
				if(!result) {
					dao.deleteUserAgent(userAgentModelNew.getId());
					return BaseResponse.error("-1", "Thêm mới không thành công !");
				}
				
				return BaseResponse.success("", "Thêm mới thành công", null);
			} else {
				return BaseResponse.error("-1", "Thêm mới không thành công !");
			}
		} catch (Exception e) {
			return BaseResponse.error("-1", e.getMessage());
		}
	}
}
