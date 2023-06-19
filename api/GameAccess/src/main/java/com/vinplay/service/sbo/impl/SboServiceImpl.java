/**
 * Archie
 */
package com.vinplay.service.sbo.impl;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.JsonObject;
import com.vinplay.connection.ConnectionPoolUtil;
import com.vinplay.dao.sbo.SboDao;
import com.vinplay.dao.sbo.impl.SboDaoImpl;
import com.vinplay.dto.ResultFormat;
import com.vinplay.dto.sbo.SboBalance;
import com.vinplay.dto.sbo.SboLogin;
import com.vinplay.dto.sbo.SboTransferResponse;
import com.vinplay.dto.sbo.SboUserDto;
import com.vinplay.logic.CommonMethod;
import com.vinplay.logic.InitData;
import com.vinplay.service.sbo.SboService;
import com.vinplay.usercore.service.MoneyInGameService;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.MoneyInGameServiceImpl;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.utils.ThirdpartyConstant;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.response.MoneyResponse;


/**
 * @author Archie
 *
 */
public class SboServiceImpl extends SboConfig implements SboService {
	
	private static final Logger logger = LoggerFactory.getLogger(SboServiceImpl.class);
	
	private SboDao sboDao =new SboDaoImpl();
	
	private boolean isNullOrEmpty(String text) {
		return text == null || "".equals(text);
	}

	@Override
	public ResultFormat lunchGame(String loginName ,String portfolio) throws Exception {
		// validation
		if (isNullOrEmpty(loginName))  throw new Exception("Loginname is null or empty");
		// mapping user	
		SboUserDto sboUser = sboDao.mappingUserSbo(loginName);
		if (sboUser == null || "".equals(sboUser.getSboid()))
			throw new Exception("This user is not exit");
		//get playername
		JsonObject json =globalJsonParams.deepCopy();
		json.addProperty("Username", sboUser.getSboid());
		if (portfolio == null || "".equals(portfolio)) {
			portfolio = "SportsBook";
		}
		json.addProperty("Portfolio", portfolio);//SportsBook / Casino / Games / VirtualSports / SeamlessGame / ThirdPartySportsBook
		// url
		String url = String.format("%s%s", GameThirdPartyInit.SBO_API_URL, ThirdpartyConstant.SBO.API_LAUNCH);
		// invoke API
		String data = ConnectionPoolUtil.postData(url, json.toString(), null);
		logger.info("lunchGame SBO , response = " + data);
		if (data != null) {
			SboLogin result = MAPPER.readValue(data,
					new TypeReference<SboLogin>() {});
			if (result != null && result.getError() != null && result.getError().getId() == 0) {
				// success
				return new ResultFormat(0, "SUCCESS", result.getUrl());
			} else {
				logger.error("SBO Launch error , code ={} , message ={}", result.getError().getId(),
						result.getError().getMsg());
				return new ResultFormat(1, result.getError().getMsg());
			}
		}
		return new ResultFormat(1, "Game SBO đang bảo trì ");
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
			moneyResponse = moneyService.updateMoneyGame3rdUser(nickName, amount * (-1), "vin", "sbo",
					"SBO_DEPOSIT", "NẠP TIỀN SBO", 0, false);
		} else {
			// withdraw
			moneyResponse = moneyService.updateMoneyGame3rdUser(nickName, amount, "vin", "sbo",
					"SBO_WITHDRAW", "RÚT TIỀN SBO", 0, false);
		}
		if(moneyResponse!=null && "0".equals(moneyResponse.getErrorCode())) {
			return true;
		}
		return false;
	}

	
	private SboTransferResponse sboTranfer(String sportId, Double amount, int direction, String traceId) throws Exception{
		logger.info("SboTransferDto request , sportId=" + sportId + " ,amount =" + amount + ",direction=" + direction);
		// parameter
		String fullUrl = null;
		JsonObject json =globalJsonParams.deepCopy();
		json.addProperty("Username", sportId);
		json.addProperty("Amount", amount);
		json.addProperty("TxnId", traceId);
		
		if (direction == 1) {// deposit
			fullUrl = String.format("%s%s", GameThirdPartyInit.SBO_API_URL, ThirdpartyConstant.SBO.API_DEPOSIT);
			traceId="D"+traceId;
		} else {// withdraw
			fullUrl = String.format("%s%s", GameThirdPartyInit.SBO_API_URL, ThirdpartyConstant.SBO.API_WITHDRAW);
			traceId="W"+traceId;
			json.addProperty("IsFullAmount", false);
		}
		
		
		// invoke API
		//String data = HttpConnectionPool.postData(fullUrl, params);
		String data =  ConnectionPoolUtil.postData(fullUrl, json.toString(), null);
		logger.info("sboTranfer , response = " + data);
		if (data != null) {
			SboTransferResponse result = MAPPER.readValue(data,
					new TypeReference<SboTransferResponse>() {
					});
			if (result!=null && result.getError() != null && result.getError().getId()==0) {
				// success
				return result;
			} else {
				logger.error("SBO transfer error , code ={} , message ={}", result.getError().getId(),
						result.getError().getMsg());
			}
		}
		return null;
	}
	
	@Override
	public ResultFormat transfer(String loginname, Double amountTrans, String ip ,Integer direction) throws Exception {
		// validation
		if (isNullOrEmpty(loginname)){
			logger.error("SboService loginname is null or empty ");
			return new ResultFormat(1, "Yêu cầu không hợp lệ");
		}
		if (!CommonMethod.ValidateRequest(loginname)) {
			logger.info("SboService loginname : " + loginname + "Trong 10s chỉ thực hiện request 1 lần ");
			return new ResultFormat(1, "Xin chờ 10 giây để thực hiện giao dịch tiếp theo ");
		}
		Double amount = amountTrans;
		if (amount > 100000000 || amount < 0) {
			logger.error("SboService loginname = " + loginname + " , Giao dịch thất bại ");
			return new ResultFormat(1, "Giao dịch thất bại ");
		}
		
		if (direction != 0 && direction != 1) {
			logger.error("SboService direction = " + direction + " , Giao dịch thất bại ");
			return new ResultFormat(1, "Giao dịch thất bại , không xác định phương thức transfer");
		}
		// check exist user and maintain flag on thirdparty
		ResultFormat rf = getBalance(loginname);
		if (rf.getRes() != 0 || rf.getData() == null) {
			logger.error("User không tồn tại hoặc Hệ thống đang bảo trì , loginname =  "+loginname);
			return new ResultFormat(2, "Hệ thống đang bảo trì , vui lòng quay lại sau !");
		}
		SboBalance sbo = (SboBalance) rf.getData();
		// get transactionId
		String wid = CommonMethod.GetCurDate("yyMMddHHmmss") + InitData.getids();
		String sboId = sbo.getUsername();
		
		logger.info("SboService request transfer loginname = " + loginname + ", sboId=  " + sboId + ", direction= " + direction + ",amount=" + amount);
		SboTransferResponse resDto = new SboTransferResponse();
		//check balance and maintenance
		Double currentBalance = sbo.getBalance() != null ? sbo.getBalance() * GameThirdPartyInit.SBO_RATE : 0;
		try {
			if (direction == 1) { // deposit
				//check format money
				if (amount % 1000 != 0) {
					return new ResultFormat(2, "Quý khách vui lòng nhập số tiền chẵn (là hệ số của 1000 vnđ) ");
				}
				//update database
				boolean res = updateBalance(loginname, amount.longValue(), direction);
				if (res) {
					long t1 =System.currentTimeMillis();
					//call api sbo
					resDto = sboTranfer(sboId, amount / GameThirdPartyInit.SBO_RATE, direction, wid);
					long t2 =System.currentTimeMillis();
					System.out.println("::::::::::::::::::::::::::::::::::TIMEXSBO" + (t2 - t1));
					if (resDto != null) {
						logger.info("SboService deposit success ,loginname = " + loginname + ", sboId= " + sboId);
						return new ResultFormat(0, "SUCCESS", resDto);
					} else {
						logger.error("SboService deposit fail , loginname= " + loginname + ", sboId= " + sboId);
						return new ResultFormat(1, "Liên hệ CSKH !");
					}
				}
			} else if (direction == 0) { // withdraw
				//check enough
				if(currentBalance < amount) {
					return new ResultFormat(1, "Số dư tài khoản SBO không đủ ", null);
				}
				resDto = sboTranfer(sboId, amount / GameThirdPartyInit.SBO_RATE, direction, wid);
				if (resDto != null) {
					boolean res = updateBalance(loginname, amount.longValue(), direction);
					if (res) {
						logger.info("SboService withdraw success ,loginname=" + loginname + ", sboId= " + sboId);
						return new ResultFormat(0, "SUCCESS", resDto);
					}

				} else {
					logger.error("SboService playerName : " + sboId + " Can not connect SBO");
					return new ResultFormat(1, "Không thể connect tới SBO , chuyển quỹ thất bại !");
				}
			} else {
				logger.error("SboService playerName : " + sboId + " msg : Transaction type is incorrect!");
				return new ResultFormat(1, "Transaction type is incorrect!");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("SboService playerName :" + sboId, ex);
			return new ResultFormat(1, ex.getMessage());
		}
		return new ResultFormat(1, "Liên hệ CSKH !");
	}
	

	@Override
	public ResultFormat getBalance(String loginName) throws Exception {
		if (isNullOrEmpty(loginName)) throw new Exception("Loginname is null or empty");
		// Mapping user
		SboUserDto sboU = sboDao.mappingUserSbo(loginName);
		if (sboU == null || "".equals(sboU.getSboid())) throw new Exception("this user is not exist");
		// Params
		JsonObject json =globalJsonParams.deepCopy();
		json.addProperty("Username", sboU.getSboid());
		
		// url
		String url = String.format("%s%s", GameThirdPartyInit.SBO_API_URL, ThirdpartyConstant.SBO.API_GET_BALANCE);
		// invoke API
		String data = ConnectionPoolUtil.postData(url, json.toString(), null);
		if (data != null) {
			SboBalance  result = MAPPER.readValue(data, new TypeReference<SboBalance>() {});
			if (result!=null &&result.getError()!=null &&result.getError().getId()==0) {
				// success
				return new ResultFormat(0, "SUCCESS", result);
			} else {
				logger.error("SBO service impl error , code ={} , message ={}", result.getError().getId(),
						result.getError().getMsg());
				
			}
		}
		return new ResultFormat(1, "Thất bại! Liên hệ CSKH");
	}
	
	
}
