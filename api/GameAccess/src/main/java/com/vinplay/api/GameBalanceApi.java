package com.vinplay.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.vinplay.dto.BaseResponseDto;
import com.vinplay.dto.ResultFormat;
import com.vinplay.dto.ag.BaseAGResponseDto;
import com.vinplay.dto.ebet.UserInfomationRespDto;
import com.vinplay.dto.ibc2.common.CheckBalanceDataRespDto;
import com.vinplay.dto.sbo.SboBalance;
import com.vinplay.dto.sportsbook.SportsbookUserBalanceRespDto;
import com.vinplay.interfaces.sportsbook.SportsbookMemberServices;
import com.vinplay.logic.CommonMethod;
import com.vinplay.logic.InitData;
import com.vinplay.payment.utils.Constant;
import com.vinplay.service.ag.AgService;
import com.vinplay.service.ag.impl.AgServiceImpl;
import com.vinplay.service.ebet.EbetService;
import com.vinplay.service.ebet.EbetServiceImpl;
import com.vinplay.service.ibc2.Ibc2Dao;
import com.vinplay.service.ibc2.impl.Ibc2DaoImpl;
import com.vinplay.service.sbo.SboService;
import com.vinplay.service.sbo.impl.SboServiceImpl;
import com.vinplay.service.wm.WmService;
import com.vinplay.service.wm.impl.WmServiceImpl;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.utils.BaseResponse;
import com.vinplay.utils.HttpUtils;
import com.vinplay.vbee.common.models.UserModel;

/**
 * @author Archie
 *
 */
public class GameBalanceApi extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7651078352307234600L;
	private static final Logger logger = Logger.getLogger(GameBalanceApi.class);
	private WmService serviceWM = new WmServiceImpl();
	private SportsbookMemberServices serviceCmd = SportsbookMemberServices.getInstance();
	private UserService userService = new UserServiceImpl();
	private Ibc2Dao ibcService = new Ibc2DaoImpl();
	private AgService agService = new AgServiceImpl();

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	public static void main(String[] args) {
		Float a= 0.944765f;
		a = a*1000;
		System.out.println(a);
		System.out.println(a.longValue());
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(200);
		// check IP
		String ipAddress = HttpUtils.getIpAddressCF(request);
		if (!CommonMethod.isValidIpAddress(ipAddress)) {
			logger.info("ipaddress = "+ipAddress);
			return;
		}
		String type = request.getParameter("t");
		String nickName = request.getParameter("nn");

		if (StringUtils.isBlank(type) || StringUtils.isBlank(nickName)) {
			response.getWriter().println(BaseResponse.error(Constant.ERROR_PARAM, "Check param again"));
			return;
		}
		BaseResponse rf = new BaseResponse(1,"");
		try {
			// check user exist
			UserModel userModel = userService.getUserByNickName(nickName);
			if (userModel == null)
				return;
			CheckBalanceDataRespDto da = new CheckBalanceDataRespDto();
			switch (type) {
			case "AG":
				if (InitData.isAGDown()) {
					logger.info("AG maintained");
					rf = new BaseResponse(Constant.ERROR_MAINTAIN, "AG maintained");
					break;
				}
				ResultFormat rfag = agService.queryPlayer(nickName);
				if (rfag.getRes() == 0) {
					BaseAGResponseDto ba = (BaseAGResponseDto) rfag.getList().get(0);
					Double amount = Double.parseDouble(ba.getInfo() != null ? ba.getInfo() : "0");
					rf.setCode(0);
					da.setBalance(amount * 1000);
					da.setVendor_member_id(ba.getMsg());
					rf.setData(da);
				}else {
					rf.setCode(rfag.getRes());
					rf.setMessage(rfag.getMsg());
					rf.setData(da);
				}
				break;
			case "IBC2":
				if (InitData.isIbc2Down()) {
					logger.info("IBC2 maintained");
					rf = new BaseResponse(Constant.ERROR_MAINTAIN, "IBC2 maintained");
					break;
				}
				rf = ibcService.getBalance(nickName);
				logger.info("IBC2_GetBalance response: " + rf.toJson());
				if (rf.getCode() == 0) {
					da = (CheckBalanceDataRespDto) rf.getData();
//					da.setVendor_member_id("");
					da.setBalance(da.getBalance() != null ? da.getBalance() * 1000 : 0);
					rf.setData(da);
				}
				break;
			case "WM":
				if (InitData.isWMDown()) {
					logger.info("WM maintained");
					rf = new BaseResponse(Constant.ERROR_MAINTAIN, "WM maintained");
					break;
				}
				ResultFormat rfwm = serviceWM.checkBalance(nickName);
				logger.info("WM_GetBalance response: " + rfwm.toJson());
				if (rfwm.getRes() == 0) {
					rf.setCode(0);
					rf.setMessage(rfwm.getMsg());
					Float amount = (Float) rfwm.getList().get(1);
					String venderU ="";
					try {
						venderU = (String) rfwm.getList().get(2);
					} catch (Exception e) {
						// TODO: handle exception
					}
					Float realBalance = amount != null ? amount * 1000 : 0;
					long realAmountRound = realBalance.longValue();
					da.setBalance((double) realAmountRound);
					da.setVendor_member_id(venderU);
					rf.setData(da);
				} else {
					rf.setCode(rfwm.getRes());
					rf.setMessage(rfwm.getMsg());
					rf.setData(da);
				}
				break;
			case "CMD":
				if (InitData.isCMDDown()) {
					logger.info("CMD maintained");
					rf = new BaseResponse(Constant.ERROR_MAINTAIN, "CMD maintained");
					break;
				}
				BaseResponseDto<SportsbookUserBalanceRespDto> rfc = serviceCmd.getUserBalance(nickName);
				logger.info("CMD_GetBalance response: " + rfc.toJson());
				if(rfc.getCode()==0) {
					rf.setCode(0);
					rf.setMessage(rfc.getMessage());
					SportsbookUserBalanceRespDto object = rfc.getData();
					Double amount = null;
					try {
						amount = object.getData().get(0).getBetAmount();
					} catch (Exception e) {
						// TODO: handle exception
					}
					da.setBalance(amount != null ? amount.doubleValue()  : 0d);
					rf.setData(da);
				}else {
					rf.setCode(rfc.getCode());
					rf.setMessage(rfc.getMessage());
					rf.setData(da);
				}
				break;
			case "EBET":
				EbetService ebetService = new EbetServiceImpl();
				ResultFormat ef = ebetService.getBalance(nickName);
				logger.info("CMD_GetBalance response: " + ef.toJson());
				if(ef.getRes()==0) {
					rf.setCode(0);
					rf.setMessage(ef.getMsg());
					UserInfomationRespDto object = (UserInfomationRespDto) ef.getData();
					Double amount = null;
					try {
						amount = object.getMoney() != null ? object.getMoney() * 1000 : 0;
					} catch (Exception e) {
						// TODO: handle exception
					}
					da.setBalance(amount != null ? amount.doubleValue()  : 0d);
					da.setVendor_member_id(object.getUsername());
					rf.setData(da);
				}else {
					rf.setCode(ef.getRes());
					rf.setMessage(ef.getMsg());
					rf.setData(da);
				}
				break;
			case "SBO":
				SboService sboService =new SboServiceImpl();
				ResultFormat eff = sboService.getBalance(nickName);
				SboBalance bal = (SboBalance) eff.getData();
				logger.info("CMD_GetBalance response: " + eff.toJson());
				if(eff.getRes()==0) {
					rf.setCode(0);
					rf.setMessage(eff.getMsg());
					
					Double amount = null;
					try {
						amount = bal.getBalance();
					} catch (Exception e) {
						// TODO: handle exception
					}
					da.setBalance(amount != null ? amount.doubleValue()*1000  : 0d);
					da.setVendor_member_id(bal.getUsername());
					rf.setData(da);
				}else {
					rf.setCode(eff.getRes());
					rf.setMessage(eff.getMsg());
					rf.setData(da);
				}
				break;
			default:
				break;
			}
			response.getWriter().println(rf.toJson());
		} catch (Exception ex) {
			logger.error("Wm exception ,type=" + type, ex);
		}
	}
}