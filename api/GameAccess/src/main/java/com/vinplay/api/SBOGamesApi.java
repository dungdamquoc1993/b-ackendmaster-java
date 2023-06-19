package com.vinplay.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.vinplay.dto.ResultFormat;
import com.vinplay.logic.InitData;
import com.vinplay.payment.utils.Constant;
import com.vinplay.service.sbo.SboService;
import com.vinplay.service.sbo.impl.SboServiceImpl;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.utils.BaseResponse;
import com.vinplay.utils.HttpUtils;
import com.vinplay.vbee.common.models.UserModel;

/**
 * @author Archie
 *
 */
public class SBOGamesApi extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7651078352307234600L;
	private static final Logger logger = Logger.getLogger(SBOGamesApi.class);
	private UserService userService = new UserServiceImpl();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	SboService service =new SboServiceImpl();
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(200);
		// check IP
		String ipAddress = HttpUtils.getIpAddress(request);
//		if (!CommonMethod.isValidIpAddress(ipAddress)) {
//			return;
//		}
		String type = request.getParameter("t");
		String nickName = request.getParameter("nn");
		String accessToken = request.getParameter("at");
		
		if (StringUtils.isBlank(type) || StringUtils.isBlank(nickName) || StringUtils.isBlank(accessToken)) {
			response.getWriter().println(BaseResponse.error(Constant.ERROR_PARAM, "Check param again"));
			return;
		}
		
		ResultFormat rf = new ResultFormat(1, "");;
		if (InitData.isSBODown()) {
			logger.info("isSBODown maintained");
			rf = new ResultFormat(Constant.ERROR_MAINTAIN, "Sport SBO đang bảo trì");
			response.getWriter().println(rf.toJson());
			return;
		}
		logger.info("SBO_param ipAddress=" + ipAddress + " , type=" + type + " ,nickName=" + nickName);
		try {

			// verify token
			boolean isToken = userService.isActiveToken(nickName, accessToken);
			if (!isToken) {
				String baseResponse = BaseResponse.error(Constant.ERROR_SESSION,
						"Phiên làm việc của bạn đã hết hạn , vui lòng tải lại trang !");
				response.getWriter().println(baseResponse);
				return;
			}
			UserModel userModel = userService.getUserByNickName(nickName);
			if (userModel == null)
				return;
			if (userModel.isBanLogin() || userModel.isBot()) {
				String baseResponse = BaseResponse.error(Constant.ERROR_USER_BAN, "This user was banned");
				response.getWriter().println(baseResponse);
				return;
			}
			long balance = userModel.getVin();
			switch (type) {
			case "Transfer": {
				Double amount = 0d;
				try {
					amount = Double.parseDouble(request.getParameter("a"));
				} catch (NumberFormatException e) {
					rf = new ResultFormat(5, e.getMessage());
					response.getWriter().println(rf.toJson());
					return;
				}
				Integer direction = 0;
				try {
					direction = Integer.parseInt(request.getParameter("d"));
				} catch (NumberFormatException e) {
					rf = new ResultFormat(5, e.getMessage());
					response.getWriter().println(rf.toJson());
					return;
				}
				if (direction == 1 && amount > balance) { // check balance main deposit
					rf.setRes(3);
					rf.setMsg("Tài khoản không đủ số dư để giao dịch ");
					logger.info("Tài khoản không đủ số dư để giao dịch ");
					response.getWriter().println(rf.toJson());
					return;
				}
				rf= service.transfer(nickName, amount, ipAddress, direction);
				break;
			}
			case "CheckBalance": {
				rf = service.getBalance(nickName);
				break;
			}
			case "Login": {
				String gameCode = request.getParameter("gc");
				rf = service.lunchGame(nickName, gameCode);
				break;
			}
			default:
				logger.error("Type is not exist , type = " + type);
				rf = new ResultFormat(-1, "type is not exist");
			}
			response.getWriter().println(rf.toJson());
		} catch (Exception ex) {
			logger.error("SBO exception ,type=" + type, ex);
		}
	}
}