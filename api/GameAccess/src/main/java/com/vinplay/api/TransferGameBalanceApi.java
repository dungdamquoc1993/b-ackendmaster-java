//package com.vinplay.api;
//
//import java.io.IOException;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.log4j.Logger;
//
//import com.vinplay.dto.BaseResponseDto;
//import com.vinplay.dto.ResultFormat;
//import com.vinplay.dto.ag.BaseAGResponseDto;
//import com.vinplay.dto.ibc2.common.CheckBalanceDataRespDto;
//import com.vinplay.dto.sportsbook.SportsbookFundTransferRespDto;
//import com.vinplay.dto.sportsbook.SportsbookUserBalanceRespDto;
//import com.vinplay.interfaces.sportsbook.SportsbookMemberServices;
//import com.vinplay.logic.CommonMethod;
//import com.vinplay.logic.InitData;
//import com.vinplay.payment.utils.Constant;
//import com.vinplay.service.ag.AgService;
//import com.vinplay.service.ag.impl.AgServiceImpl;
//import com.vinplay.service.ibc2.Ibc2Dao;
//import com.vinplay.service.ibc2.impl.Ibc2DaoImpl;
//import com.vinplay.service.wm.WmService;
//import com.vinplay.service.wm.impl.WmServiceImpl;
//import com.vinplay.usercore.service.UserService;
//import com.vinplay.usercore.service.impl.UserServiceImpl;
//import com.vinplay.utils.BaseResponse;
//import com.vinplay.utils.HttpUtils;
//import com.vinplay.vbee.common.models.UserModel;
//
///**
// * @author Archie
// *
// */
//public class TransferGameBalanceApi extends HttpServlet {
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -7651078352307234600L;
//	private static final Logger logger = Logger.getLogger(GameBalanceApi.class);
//	private WmService serviceWM = new WmServiceImpl();
//	private SportsbookMemberServices serviceCmd = SportsbookMemberServices.getInstance();
//	private UserService userService = new UserServiceImpl();
//	private Ibc2Dao ibcService = new Ibc2DaoImpl();
//	private AgService agService = new AgServiceImpl();
//
//	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		doPost(request, response);
//	}
//	public void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, java.io.IOException {
//		response.setContentType("application/json");
//		response.setCharacterEncoding("UTF-8");
//		response.setStatus(200);
//		// check IP
//		String ipAddress = HttpUtils.getIpAddressCF(request);
////		if (!CommonMethod.isValidIpAddress(ipAddress)) {
////			logger.info("ipaddress = "+ipAddress);
////			return;
////		}
//		String type = request.getParameter("t");
//		String nickName = request.getParameter("nn");
//
////		if (StringUtils.isBlank(type) || StringUtils.isBlank(nickName)) {
////			response.getWriter().println(BaseResponse.error(Constant.ERROR_PARAM, "Check param again"));
////			return;
////		}
//		BaseResponse rf = new BaseResponse<>(1,"");
//		try {
//			// check user exist
////			UserModel userModel = userService.getUserByNickName(nickName);
////			if (userModel == null)
////				return;
////			CheckBalanceDataRespDto da = new CheckBalanceDataRespDto();
//			switch (type) {
//			case "AG":
//				if (InitData.isAGDown()) {
//					logger.info("AG maintained");
//					rf = new BaseResponse(Constant.ERROR_MAINTAIN, "AG maintained");
//					break;
//				}
//				//pending version sau
//				break;
//			case "IBC2":
//				BaseResponse<Double> re = ibcService.withdrawAll();
//				logger.info(re.toJson());
//				break;
//			case "WM":
//				if (InitData.isWMDown()) {
//					logger.info("WM maintained");
//					rf = new BaseResponse(Constant.ERROR_MAINTAIN, "WM maintained");
//					break;
//				}
//				//pending version sau
//				break;
//			case "CMD":
////				if (InitData.isCMDDown()) {
////					logger.info("CMD maintained");
////					rf = new BaseResponse(Constant.ERROR_MAINTAIN, "CMD maintained");
////					break;
////				}
//				BaseResponseDto<SportsbookFundTransferRespDto> rfs =serviceCmd.transferUserBalance(nickName);
//				logger.info("CMD_TRANSFER_GAME_BALANCE response: " + rfs.toJson());
//				if(rfs.getCode()==0) {
//					//SUCCESS
//					rfs.setCode(0);
//					break;
//				}else {
//					//FAIL
//					rfs.setCode(-1);
//				}
//				break;
//			default:
//				break;
//			}
//			response.getWriter().println(rf.toJson());
//		} catch (Exception ex) {
//			logger.error("Wm exception ,type=" + type, ex);
//		}
//	}
//}