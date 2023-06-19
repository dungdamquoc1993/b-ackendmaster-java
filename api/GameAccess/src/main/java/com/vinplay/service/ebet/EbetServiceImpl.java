package com.vinplay.service.ebet;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.vinplay.dao.ebet.EbetDao;
import com.vinplay.dao.impl.ebet.EbetDaoImpl;
import com.vinplay.dto.ResultFormat;
import com.vinplay.dto.ebet.EbetUserItem;
import com.vinplay.dto.ebet.RechargeRespDto;
import com.vinplay.dto.ebet.UserInfomationRespDto;
import com.vinplay.interfaces.ebet.DepositEbetService;
import com.vinplay.interfaces.ebet.UserinfoEbetService;
import com.vinplay.interfaces.ebet.WithdrawEbetService;
import com.vinplay.logic.CommonMethod;

public class EbetServiceImpl implements EbetService{
	private static final Logger logger = Logger.getLogger(EbetServiceImpl.class);
	private EbetDao ebetDao = new EbetDaoImpl();
	@Override
	public ResultFormat lunchGame(String userName) throws Exception {
		ResultFormat rf = new ResultFormat();
			logger.info("Ebet_LauchingURL request: " + "Username-" + userName);
			//connect DB
			EbetUserItem ebetUserItem = ebetDao.mappingUserEbet(userName);
			if(ebetUserItem != null){
				rf.setRes(0);
				rf.setData(ebetUserItem);
			}else{
				rf.setRes(1);
				rf.setData(ebetUserItem);
				rf.setMsg("Thất bại! (Liên hệ CSKH)");
			}
		return rf;
	}

	@Override
	public ResultFormat getBalance(String userName) throws Exception {
		ResultFormat rf = new ResultFormat();
		UserinfoEbetService service = new UserinfoEbetService();
		UserInfomationRespDto userInfo = service.GetUserInfo(userName);
		if (userInfo != null) {
			rf.setRes(0);
			rf.setData(userInfo);
			rf.setMsg("");
		} else {
			rf.setRes(1);
			rf.setData(userInfo);
			rf.setMsg("Thất bại! (Liên hệ CSKH)");
		}
		return rf;
	}

	@Override
	public ResultFormat transfer(String userName, Double amount, String ip, Integer direction) throws Exception {
		if(userName==null) {
			logger.error("SboService loginname is null or empty ");
			return new ResultFormat(1, "Yêu cầu không hợp lệ");
		}
		if (!CommonMethod.ValidateRequest(userName)) {
			return new ResultFormat("Xin chờ 10 giây để thực hiện giao dịch tiếp theo ");
		}
		if (amount <= 0) {
			logger.error("Ebet_DepositPlayerMoney amount <= 0");
			return new ResultFormat("Giao dịch thất bại , amount <0");
		}
		if (direction != 0 && direction != 1) {
			logger.error("Ebet direction = " + direction + " , Giao dịch thất bại ");
			return new ResultFormat(1, "Giao dịch thất bại , không xác định phương thức transfer");
		}
		List<Object> listObj = new ArrayList<Object>();
		logger.info("Ebet_DepositPlayerMoney request: " + "Amount-" + amount + " | " + "Username-" + userName + " | Ip-"
				+ ip);
		
//		// check exist user and maintain flag on thirdparty
		UserinfoEbetService service = new UserinfoEbetService();
		UserInfomationRespDto userInfo = service.GetUserInfo(userName);
		if (userInfo==null || userInfo.getStatus() != 200) {
			logger.error("User không tồn tại hoặc Hệ thống đang bảo trì , nickname =  " + userName);
			return new ResultFormat(2, "Hệ thống đang bảo trì , vui lòng quay lại sau !");
		}
		Double currentBalance = userInfo.getMoney() != null ? userInfo.getMoney() * 1000 : 0;
		if (direction == 1) {
			// deposit
			DepositEbetService depositEbet = new DepositEbetService();
			RechargeRespDto depositDTO = depositEbet.DepositTransfer(amount, userName, ip);
			if (depositDTO != null) {
				listObj.add(depositDTO);
				return new ResultFormat(0, "SUCCESS", listObj);
			} else {
				listObj.add("");
				return new ResultFormat(1, "Thất bại! (Liên hệ CSKH)", listObj);
			}
		}else {
			// wd
			//check enough
			if(currentBalance < amount) {
				return new ResultFormat(1, "Số dư tài khoản EBET không đủ ", null);
			}
			logger.info("Ebet_WithdrawPlayerMoney request: " + "Amount-" + amount + " | " + "Username-" + userName
					+ " | Ip-" + ip);
			WithdrawEbetService withdrawEbet = new WithdrawEbetService();
			RechargeRespDto withdrawDTO = withdrawEbet.WithdrawTransfer(amount, userName, ip);
			if (withdrawDTO != null) {
				listObj.add(withdrawDTO);
				return new ResultFormat(0, "SUCCESS", listObj);
			} else {
				listObj.add("");
				logger.error("Ebet_WithdrawPlayerMoney request: " + "Amount-" + amount + " | " + "Username-" + userName
						+ " | Ip-" + ip);
				return new ResultFormat(1, "Thất bại! (Liên hệ CSKH)", listObj);
			}
		}
		
	}
	
}
