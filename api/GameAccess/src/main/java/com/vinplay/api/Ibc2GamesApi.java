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

import com.vinplay.dto.ibc2.common.CheckBalanceDataRespDto;
import com.vinplay.payment.utils.Constant;
import com.vinplay.service.ibc2.Ibc2Dao;
import com.vinplay.service.ibc2.impl.Ibc2DaoImpl;
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
public class Ibc2GamesApi extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4639613355142118336L;
	/**
	 * 
	 */
	private static final Logger logger = Logger.getLogger(Ibc2GamesApi.class);
	private UserService userService = new UserServiceImpl();
	private Ibc2Dao dao = new Ibc2DaoImpl();

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

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
		BaseResponse jsonRes = new BaseResponse(1,"");

		if (GameThirdPartyInit.IBC2_STATUS) {
			jsonRes = new BaseResponse<>(Constant.ERROR_MAINTAIN, "Thể thao IBC2 đang bảo trì");
			logger.info("IBC2 maintained");
			response.getWriter().println(jsonRes.toJson());
			return;
		}
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
			switch (type) {
			case "Login": {
				jsonRes = dao.loginIbc(nickName);
				break;
			}
			case "CheckBalance": {
				BaseResponse<CheckBalanceDataRespDto> br = dao.getBalance(nickName);
				if (br.getCode() == 0) {
					CheckBalanceDataRespDto da = br.getData();
					da.setVendor_member_id("");
					jsonRes = new BaseResponse<>(da);
				} else {
					jsonRes = br;
				}
				break;
			}
			case "Transfer": {
				// ResultFormat rf = Ibc2CommonService.FundTransfer(objInStream);
				Double amount = 0d;
				try {
					amount = Double.parseDouble(request.getParameter("a"));
				} catch (NumberFormatException e) {
					jsonRes = new BaseResponse(5, e.getMessage());
					response.getWriter().println(jsonRes.toJson());
					return;
				}
				Integer direction = 0;
				try {
					direction = Integer.parseInt(request.getParameter("d"));
				} catch (NumberFormatException e) {
					jsonRes = new BaseResponse(5, e.getMessage());
					response.getWriter().println(jsonRes.toJson());
					return;
				}
				if (direction == 1 && amount.longValue() > userModel.getVin()) { // check balance main deposit
					jsonRes = new BaseResponse(3, "Tài khoản không đủ số dư để giao dịch ");
					response.getWriter().println(jsonRes.toJson());
					return;
				}
				jsonRes = dao.transfer(nickName, direction, amount, ipAddress);
				
				break;
			}
			default:
				jsonRes = new BaseResponse<>(5, "Type is not exist , type = " + type);
				logger.error("Type is not exist , type = " + type);
			}
			response.getWriter().println(jsonRes.toJson());
		} catch (Exception e) {
			logger.error("Ibc2 exception ,type=" + type, e);
		}
		
	}
}