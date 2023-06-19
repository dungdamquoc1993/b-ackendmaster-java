/**
 * Archie
 */
package com.vinplay.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.vinplay.dto.ResultFormat;
import com.vinplay.payment.utils.Constant;
import com.vinplay.service.ag.AgService;
import com.vinplay.service.ag.impl.AgServiceImpl;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.utils.BaseResponse;
import com.vinplay.utils.HttpUtils;
import com.vinplay.vbee.common.models.UserModel;

/**
 * @author Archie
 *
 */
//@WebServlet(name = "AgGamesApi", description = "API for Ag games", urlPatterns = { "/api/ag" })
public class AgGamesApi extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8070341853356219669L;
	/**
	 * 
	 */
	
	private static final Logger logger = Logger.getLogger(AgGamesApi.class);
	
	private UserService userService = new UserServiceImpl();
	private AgService agService = new AgServiceImpl();

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		// check IP
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
		ResultFormat rf = new ResultFormat(1,"");
		try {
			if (!GameThirdPartyInit.AG_STATUS) {
				// verify token
				boolean isToken = userService.isActiveToken(nickName, accessToken);
				if (!isToken) {
					String baseResponse = BaseResponse.error(Constant.ERROR_SESSION, "Phiên làm việc của bạn đã hết hạn , vui lòng tải lại trang !");
					response.getWriter().println(baseResponse);
					return;
				}
				UserModel userModel = userService.getUserByNickName(nickName);
				if (userModel == null)
					return;
				if(userModel.isBanLogin() || userModel.isBot()) {
					String baseResponse = BaseResponse.error(Constant.ERROR_USER_BAN, "This user was ban");
					response.getWriter().println(baseResponse);
					return;
				}
				switch (type) {
				case "GetBalance": {// from fe
					rf = agService.queryPlayer(nickName);
					break;
				}
				case "Deposit": {// from fe
					Double amount = 0d;
					try {
						amount = Double.parseDouble(request.getParameter("a"));
					} catch (NumberFormatException e) {
						rf = new ResultFormat(5, e.getMessage());
						response.getWriter().println(rf.toJson());
						return;
					}
					if (amount.longValue() > userModel.getVin()) { // check balance main deposit
						rf = new ResultFormat(3, "Tài khoản không đủ số dư để giao dịch ");
						response.getWriter().println(rf.toJson());
						return;
					}
					rf = agService.depositPlayerMoney(nickName, amount, ipAddress);
					break;
				}
				case "Withdraw": {// from fe
					Double amount = 0d;
					try {
						amount = Double.parseDouble(request.getParameter("a"));
					} catch (NumberFormatException e) {
						rf = new ResultFormat(5, e.getMessage());
						response.getWriter().println(rf.toJson());
						return;
					}
					rf = agService.withdrawPlayerMoney(nickName, amount, ipAddress);
					break;
				}
				case "Forward": {
					rf = agService.forwardGame(nickName);
					break;
				}
				default:
					logger.error("Type is not exist , type = " + type);
					rf = new ResultFormat(Constant.ERROR_NOT_EXIST, "type is not exist");
				}
			} else {
				logger.info("AgGamesApi maintained");
				rf = new ResultFormat(Constant.ERROR_MAINTAIN, "LiveCasino AG đang bảo trì");
			}
			response.getWriter().println(rf.toJson());
		} catch (Exception ex) {
			logger.error("Ag exception ,type=" + type, ex);
		}
		
	}
	
}