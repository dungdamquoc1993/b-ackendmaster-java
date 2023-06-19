package com.vinplay.interfaces.ebet;

import java.io.UnsupportedEncodingException;
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
import com.vinplay.logic.CommonMethod;
import com.vinplay.logic.InitData;
import com.vinplay.usercore.service.MoneyInGameService;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.MoneyInGameServiceImpl;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.response.MoneyResponse;

public class WithdrawEbetService extends BaseEbetService {
	
	private static final Logger logger = Logger.getLogger(WithdrawEbetService.class);

	private EbetDao ebetDao = new EbetDaoImpl();
	
	public RechargeRespDto WithdrawPlayerMoney(RechargeReqDto req) {
		logger.info(gson.toJson(req));
		String data = getData("recharge", req);
		RechargeRespDto resp = gson.fromJson(data, RechargeRespDto.class);
		logger.info(gson.toJson(resp));
		return resp;
	}

	public RechargeRespDto WithdrawTransfer(Double amount_trans, String userName, String ip)throws Exception {
		try {
			logger.info("EbetWithdrawTransfer loginname : "+userName+" start  FundTransfer");
			Double amount = amount_trans;
			RechargeReqDto req = new RechargeReqDto();
			EbetUserItem userItem = ebetDao.mappingUserEbet(userName);
			if (userItem == null) {
				return null;
			}
			req.setUsername(userItem.getEbetid());
			req.setChannelId(GameThirdPartyInit.CHANNEL_ID);
			req.setMoney(amount/1000 * -1);
			String wid = "EW" + CommonMethod.GetCurDate("yyMMddHHmmss") + InitData.getids();
			req.setRechargeReqId(wid);
			Integer ts  = (int) (System.currentTimeMillis() / 1000L);
			req.setTimestamp(ts);
			String signature = "";
			try {
				byte[] data = userItem.getEbetid().concat(ts.toString()).getBytes("UTF-8");
				signature = CommonMethod.sign(data, GameThirdPartyInit.PRIVATE_KEY);
				logger.info(signature);
			} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | SignatureException | UnsupportedEncodingException e) {
				logger.error("ex", e);
			}
			req.setSignature(signature);
			req.setTimestamp(ts);
			req.setTypeId(0);
			//call api minus money
			RechargeRespDto resDto = WithdrawPlayerMoney(req);
			
			if (resDto == null) {
				logger.error("EbetWithdrawTransfer playerName : "+userName+" Ebet response is null");
				return null;
			}else{
				RechargestatusEbetService service = new RechargestatusEbetService();
				//call api check status
				RechargeStatusRespDto rsDto = service.CheckRechargeStatus(wid);
				if(rsDto != null){
					resDto.setStatus(rsDto.getStatus());
					if(rsDto.getStatus() == 200){
						boolean res = updateBalance(userName, amount.longValue(), 0);
						if(res){
							logger.info("EbetWithdrawTransfer playerName : "+userName+" : "+amount+" cập nhật tài khoản thành công");
						}else{
							logger.info("EbetWithdrawTransfer playerName : "+userName+" : "+amount+" cập nhật tài thất bại");
						}
					}
				}else{
					logger.error("EbetDepositTransfer playerName : "+userName+" : "+amount+" trans id " + wid + " response null, can't check status" );
					resDto.setStatus(-2);
				}
			}
			return resDto;
		} catch (Exception ex) {
			
			logger.error("EbetWithdrawTransfer playerName :"+userName, ex);
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
