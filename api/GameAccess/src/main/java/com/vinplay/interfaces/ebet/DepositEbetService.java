package com.vinplay.interfaces.ebet;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.vinplay.dao.ebet.EbetDao;
import com.vinplay.dao.impl.ebet.EbetDaoImpl;
import com.vinplay.dto.ebet.EbetUserItem;
import com.vinplay.dto.ebet.RechargeReqDto;
import com.vinplay.dto.ebet.RechargeRespDto;
import com.vinplay.dto.ebet.RechargeStatusRespDto;
import com.vinplay.dto.ebet.UserInfomationRespDto;
import com.vinplay.logic.CommonMethod;
import com.vinplay.logic.InitData;
import com.vinplay.usercore.service.MoneyInGameService;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.MoneyInGameServiceImpl;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.response.MoneyResponse;

public class DepositEbetService extends BaseEbetService {
	
	private static final Logger logger = Logger.getLogger(DepositEbetService.class);
	
	private EbetDao ebetDao =new EbetDaoImpl();
	
	public RechargeRespDto DepositPlayerMoney(RechargeReqDto req) {
		logger.info(gson.toJson(req));
		String data = getData("recharge", req);
		RechargeRespDto resp = gson.fromJson(data, RechargeRespDto.class);
		logger.info(gson.toJson(resp));
		return resp;
	}
	
//	private boolean returnMoney(String userName, long amount, String ip) throws SQLException {
//		boolean res = updateBalance(userName, amount, 0);
//		if (res) {
//			logger.info("return money playerName : " + userName + " : " + amount + " cập nhật tài khoản thành công");
//		} else {
//			logger.error("return money playerName : " + userName + " : " + amount + " cập nhật tài thất bại");
//		}
//		return res;
//	}
	
	public RechargeRespDto DepositTransfer(Double amount_trans, String userName, String ip) throws Exception {
		logger.info("Ebet loginname : " + userName + " start  deposit transfer");
		final Double amount = amount_trans;
		RechargeReqDto req = new RechargeReqDto();
		EbetUserItem userItem = ebetDao.mappingUserEbet(userName);
		if(userItem ==null|| userItem.getEbetid() == null || "".equals(userItem.getEbetid())) {
			logger.error("DepositTransfer ebetid is null or empty");
			return null;
		}
		String ebetId = userItem.getEbetid();
		UserinfoEbetService infsv = new UserinfoEbetService();
		try {
			UserInfomationRespDto checkExitst = infsv.getUserInfo(userName, ebetId);
			if (checkExitst.getStatus() != 200) {
				logger.info("EbetDepositTransfer playerName : " + userName + " : " + amount + " code: " + checkExitst.getStatus());
				return null;
			}
			req.setUsername(ebetId);
			req.setChannelId(GameThirdPartyInit.CHANNEL_ID);
			req.setMoney(amount / 1000);
			String wid = "ED" + CommonMethod.GetCurDate("yyMMddHHmmss") + InitData.getids();
			req.setRechargeReqId(wid);
			Integer ts = (int) (System.currentTimeMillis() / 1000L);
			req.setTimestamp(ts);
			String signature = "";
			try {
				byte[] data = ebetId.concat(ts.toString()).getBytes(StandardCharsets.UTF_8);
				signature = CommonMethod.sign(data, GameThirdPartyInit.PRIVATE_KEY);
				logger.info(signature);
			} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | SignatureException e) {
				logger.error("ex", e);
			}
			req.setSignature(signature);
			req.setTimestamp(ts);
			req.setTypeId(0);

			boolean res = updateBalance(userName, amount.longValue(), 1);
			if (res) {
				// Set DepositPlayerMoneyResponse info
				logger.info("EbetDepositTransfer playerName : " + userName + " : " + amount + " cập nhật tài khoản thành công ");
				RechargeRespDto resDto = null;
				try {
					resDto = DepositPlayerMoney(req);
				} catch (Exception e) {
//					returnMoney(userName, amount, ip, wid);
					logger.error(e);
					return null;
				}
				if (resDto == null) {
					// rollback
					logger.info("EbetDepositTransfer playerName : " + userName + " : " + amount + " Ebet response is null");
//					returnMoney(userName, amount, ip, wid);
					return null;
				} else {
					RechargestatusEbetService service = new RechargestatusEbetService();
					RechargeStatusRespDto rsDto = service.CheckRechargeStatus(wid);
					if (rsDto != null) {
						resDto.setStatus(rsDto.getStatus());
						logger.info("EbetDepositTransfer playerName : " + userName + " : " + amount + " trans id " + wid + " status code " + rsDto.getStatus());
					}
//					else {
//						// roll back
//						returnMoney(userName, amount.longValue(), ip);
//						logger.error("EbetDepositTransfer playerName : " + userName + " : " + amount + " trans id " + wid + " response null, can't check status");
//						resDto.setStatus(-2);
//					}
				}
				logger.info("EbetDepositTransfer playerName : " + userName + " : " + amount + " code: " + resDto.getStatus());
				return resDto;
			} else {
				logger.info("EbetDepositTransfer playerName : " + userName + " : " + amount + " cập nhật tài khoản thất bại ");
				return null;
			}
		} catch (Exception ex) {
			logger.error("EbetDepositTransfer playerName :" + userName, ex);
			return null;
		}
	}

	private boolean updateBalance(String nickName, long amount, int direction) throws SQLException {
		MoneyInGameService moneyService = new MoneyInGameServiceImpl();
		MoneyResponse moneyResponse = null;
		if (direction == 1) {
			//check current balance
			UserService userService = new UserServiceImpl();
			UserModel u = userService.getUserByNickName(nickName);
			if (u.getVin() < amount) {
				return false;
			}
			// deposit
			moneyResponse = moneyService.updateMoneyGame3rdUser(nickName, amount * (-1), "vin", "ebet",
					"EBET_DEPOSIT", "NẠP TIỀN LIVECASINO EBET", 0, false);
		} else {
			// withdraw
			moneyResponse = moneyService.updateMoneyGame3rdUser(nickName, amount, "vin", "ebet",
					"EBET_WITHDRAW", "RÚT TIỀN LIVECASINO EBET", 0, false);
		}
		if(moneyResponse!=null && "0".equals(moneyResponse.getErrorCode())) {
			return true;
		}
		return false;
	}
	
}
