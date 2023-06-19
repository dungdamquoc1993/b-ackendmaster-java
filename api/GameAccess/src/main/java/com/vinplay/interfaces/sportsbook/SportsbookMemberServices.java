package com.vinplay.interfaces.sportsbook;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.vinplay.dao.cmd.CmdDao;
import com.vinplay.dao.cmd.impl.CmdDaoImpl;
import com.vinplay.dto.BaseResponseDto;
import com.vinplay.dto.ResultFormat;
import com.vinplay.dto.sportsbook.SportsbookFundTransferReqDto;
import com.vinplay.dto.sportsbook.SportsbookFundTransferRespDto;
import com.vinplay.dto.sportsbook.SportsbookFundTransferResponse;
import com.vinplay.dto.sportsbook.SportsbookLaunchGameResponse;
import com.vinplay.dto.sportsbook.SportsbookUserBalance;
import com.vinplay.dto.sportsbook.SportsbookUserBalanceReqDto;
import com.vinplay.dto.sportsbook.SportsbookUserBalanceRespDto;
import com.vinplay.item.SportsbookItem;
import com.vinplay.logic.CommonMethod;
import com.vinplay.logic.InitData;
import com.vinplay.service.GamesCommonService;
import com.vinplay.usercore.service.MoneyInGameService;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.MoneyInGameServiceImpl;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.utils.ThirdpartyCodes;
import com.vinplay.utils.ThirdpartyConstant;
import com.vinplay.utils.ThirdpartyMessages;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.response.MoneyResponse;

public class SportsbookMemberServices {
	private static final Logger logger = Logger.getLogger(SportsbookMemberServices.class);
	private static final Gson gson = new Gson();
	private static SportsbookMemberServices INSTANCE;
	private CmdDao cmdDao = new CmdDaoImpl();
	private SportsbookMemberServices() {
	}

	public static SportsbookMemberServices getInstance() {
		if (Objects.isNull(INSTANCE)) {
			INSTANCE = new SportsbookMemberServices();
		}
		return INSTANCE;
	}

	/**
	 * Sports book check user balance
	 * @throws SQLException 
	 */
	public BaseResponseDto<SportsbookUserBalanceRespDto> getUserBalance(String loginName) throws SQLException {
		BaseResponseDto<SportsbookUserBalanceRespDto> baseResponseDto = new BaseResponseDto<>();
		// mapping user before call to Sports book platform
		SportsbookItem sportsbookItem = cmdDao.mappingUserCmd(loginName);
		if (sportsbookItem!=null) {
			SportsbookUserBalanceReqDto reqDto = new SportsbookUserBalanceReqDto();
			reqDto.setUserName(sportsbookItem.getSportsbookid());
			// call thirdparty
			Optional<SportsbookUserBalanceRespDto> userBalanceResponse = SportsbookAllServices.getInstance()
					.checkUserBalance(reqDto);
			if (userBalanceResponse.isPresent() && userBalanceResponse.get().getCode().equals("0")
					&& CollectionUtils.isNotEmpty(userBalanceResponse.get().getData())) {
				baseResponseDto.setCode(ThirdpartyCodes.SUCCESS);
				baseResponseDto.setMessage(ThirdpartyMessages.SPORTS_BOOK_QUERY_BALANCE_SUCCESS);
				// convert user balance from Sports book platform to our system
				SportsbookUserBalance sportsbookUserBalance = userBalanceResponse.get().getData().get(0);
				double betAmount = sportsbookUserBalance.getBetAmount() * GameThirdPartyInit.SPORTS_BOOK_RATE;
				userBalanceResponse.get().getData().get(0).setBetAmount(betAmount);
				baseResponseDto.setData(userBalanceResponse.get());
			} else {
				baseResponseDto.setCode(ThirdpartyCodes.FAILED);
				baseResponseDto.setMessage(ThirdpartyMessages.SPORTS_BOOK_QUERY_BALANCE_FAILED);
			}
		} else {
			baseResponseDto.setCode(ThirdpartyCodes.FAILED);
			baseResponseDto.setMessage(ThirdpartyMessages.SPORTS_BOOK_CANNOT_FIND_USER + loginName);
		}
		return baseResponseDto;
	}
	
	public BaseResponseDto<SportsbookFundTransferRespDto> transferUserBalance(String loginName) throws SQLException {
		BaseResponseDto<SportsbookFundTransferRespDto> baseResponseDto = new BaseResponseDto<>();
		// mapping user before call to Sports book platform
		BaseResponseDto<SportsbookUserBalanceRespDto> sportsbookItem = this.getUserBalance(loginName);
		if (sportsbookItem != null && ThirdpartyCodes.SUCCESS == sportsbookItem.getCode()) {
			String ticketNo = ThirdpartyConstant.CMD.SPORTS_BOOK_WID_WITHDRAW + CommonMethod.GetCurDate("yyMMddHHmmss")
					+ InitData.getids();
			SportsbookFundTransferReqDto reqDto = new SportsbookFundTransferReqDto();
			SportsbookUserBalanceRespDto sports = sportsbookItem.getData();
			String sportBookId = sports.getData().get(0).getUserName();
			double gameBalance =sports.getData().get(0).getBetAmount();//đã x1000
			
			reqDto.setUserName(sportBookId);
			reqDto.setPaymentType(0);
			reqDto.setMoney(gameBalance / GameThirdPartyInit.SPORTS_BOOK_RATE);
			reqDto.setTicketNo(ticketNo);
			Optional<SportsbookFundTransferRespDto> wdResponse = SportsbookAllServices
					.getInstance().fundTransfer(reqDto);
			if (wdResponse.isPresent()) {
				if (wdResponse.get().getCode().equals("0")) {
					logger.info("Sportsbook withdrawal update balance in Sportsbook platform success");
					//update database
					boolean isBalance = updateBalance(loginName, gameBalance, 0);
					if (!isBalance) {
						baseResponseDto.setCode(ThirdpartyCodes.FAILED);
						baseResponseDto.setMessage(ThirdpartyMessages.SPORTS_BOOK_WITHDRAWAL_FAILED);
						return baseResponseDto;
					} else {
						baseResponseDto.setCode(ThirdpartyCodes.SUCCESS);
						baseResponseDto.setMessage(ThirdpartyMessages.SPORTS_BOOK_WITHDRAWAL_SUCCESS);
						SportsbookFundTransferResponse res = wdResponse.get().getData();
						double betAmount = res.getBetAmount() * GameThirdPartyInit.SPORTS_BOOK_RATE;
						res.setBetAmount(betAmount);
						wdResponse.get().setData(res);
						baseResponseDto.setData(wdResponse.get());
						logger.info("Sportsbook withdrawal update balance success: "
								+ gson.toJson(wdResponse.get()));
					}
				} else {
					logger.info("Sportsbook withdrawal update balance failed: "
							+ gson.toJson(wdResponse.get()));
					baseResponseDto.setCode(ThirdpartyCodes.FAILED);
					String errorMessage = StringUtils.isNotBlank(wdResponse.get().getMessage())
							? wdResponse.get().getMessage()
							: ThirdpartyMessages.SPORTS_BOOK_WITHDRAWAL_FAILED;
					baseResponseDto.setMessage(errorMessage);
				}
			} else {
				logger.info("Sportsbook withdrawal failed, response is null");
				baseResponseDto.setCode(ThirdpartyCodes.FAILED);
				baseResponseDto.setMessage(ThirdpartyMessages.SPORTS_BOOK_WITHDRAWAL_FAILED);
			}
		} else {
			baseResponseDto.setCode(ThirdpartyCodes.FAILED);
			baseResponseDto.setMessage(ThirdpartyMessages.SPORTS_BOOK_CANNOT_FIND_USER);
		}
		return baseResponseDto;
	}

	/**
	 * Login to sports book
	 * 
	 * @param loginName
	 * @throws SQLException 
	 */
	public BaseResponseDto<SportsbookLaunchGameResponse> login(String loginName) throws SQLException {
		BaseResponseDto<SportsbookLaunchGameResponse> baseResponseDto = new BaseResponseDto<>();
		// mapping user before call to Sports book platform
		//Optional<SportsbookItem> sportsbookItem = SportsbookAllServices.getInstance().mappingSportsbookUser(loginName);
		SportsbookItem item = cmdDao.mappingUserCmd(loginName);
		if (item != null) {
			Optional<String> token = SportsbookAllServices.getInstance().sportsbookLoginEncode(loginName,
					item.getSportsbook_username());
			if (token.isPresent()) {
				SportsbookLaunchGameResponse sportsbookLaunchGameResponse = new SportsbookLaunchGameResponse();
				sportsbookLaunchGameResponse.setToken(token.get());
				sportsbookLaunchGameResponse.setUserName(item.getSportsbookid());
				sportsbookLaunchGameResponse.setWebRoot(GameThirdPartyInit.SPORTS_BOOK_WEB_ROOT);
				sportsbookLaunchGameResponse.setMobileRoot(GameThirdPartyInit.SPORTS_BOOK_MOBILE_ROOT);
				sportsbookLaunchGameResponse.setNewMobileRoot(GameThirdPartyInit.SPORTS_BOOK_NEW_MOBILE_ROOT);
				baseResponseDto.setCode(ThirdpartyCodes.SUCCESS);
				baseResponseDto.setMessage(ThirdpartyMessages.SPORTS_BOOK_LOGIN_SUCCESS);
				baseResponseDto.setData(sportsbookLaunchGameResponse);
			} else {
				baseResponseDto.setCode(ThirdpartyCodes.FAILED);
				baseResponseDto.setMessage(ThirdpartyMessages.SPORTS_BOOK_LOGIN_FAILED);
			}
		} else {
			baseResponseDto.setCode(ThirdpartyCodes.FAILED);
			baseResponseDto.setMessage(ThirdpartyMessages.SPORTS_BOOK_CANNOT_FIND_USER + loginName);
		}
		return baseResponseDto;
	}
	
	private boolean updateBalance(String nickName, Double amount, int direction) throws SQLException {
		MoneyInGameService moneyService = new MoneyInGameServiceImpl();
		MoneyResponse moneyResponse = null;
		if (direction == 1) {
			//check current balance
			UserService userService = new UserServiceImpl();
			UserModel u = userService.getUserByNickName(nickName);
			if (u.getVin() < amount.longValue()) {
				return false;
			}
			// deposit
			moneyResponse = moneyService.updateMoneyGame3rdUser(nickName, amount.longValue() * (-1), "vin", "cmd",
					"CMD_DEPOSIT", "NẠP TIỀN CMD", 0, false);
		} else {
			// withdraw
			moneyResponse = moneyService.updateMoneyGame3rdUser(nickName, amount.longValue(), "vin", "cmd",
					"CMD_WITHDRAW", "RÚT TIỀN CMD", 0, false);
		}
		if(moneyResponse!=null && "0".equals(moneyResponse.getErrorCode())) {
			return true;
		}
		return false;

	}

	
	public BaseResponseDto<SportsbookFundTransferRespDto> deposit(String loginName, Double amount, String ip) throws SQLException {
		logger.info("Sportsbook start deposit with loginName: " + loginName + ", amount: " + amount);
		BaseResponseDto<SportsbookFundTransferRespDto> baseResponseDto = new BaseResponseDto<>();
		
		//validation
		if (amount == null || amount < 50000) {
			logger.error("CMD deposit amount <50000");
			return new BaseResponseDto<>("Quý khách vui lòng chuyển quỹ tối thiểu từ 50.000 VNĐ ");
		}

		if (!CommonMethod.ValidateRequest(loginName)) {
			logger.info("[CMD deposit] Trong 10s chỉ thực hiện request 1 lần , playerName ="+ loginName);
			return new BaseResponseDto<>("Xin chờ 10 giây để thực hiện giao dịch tiếp theo ");
		}
		// check exist on system and get balance
		BaseResponseDto<SportsbookUserBalanceRespDto> sportsbookItem = this.getUserBalance(loginName);
		if (sportsbookItem != null && ThirdpartyCodes.SUCCESS == sportsbookItem.getCode()) {
			
			SportsbookUserBalanceRespDto sports = sportsbookItem.getData();
			String sportBookId = sports.getData().get(0).getUserName();
			String ticketNo = ThirdpartyConstant.CMD.SPORTS_BOOK_WID_DEPOSIT + CommonMethod.GetCurDate("yyMMddHHmmss") + InitData.getids();
			//update db
			boolean isBalance = updateBalance(loginName, amount, 1);
			if (!isBalance) {
				baseResponseDto.setCode(ThirdpartyCodes.FAILED);
				baseResponseDto.setMessage("Chuyển quỹ không thành công , hệ thống đang bảo trì !");
				return baseResponseDto;
			}
			SportsbookFundTransferReqDto reqDto = new SportsbookFundTransferReqDto();
			reqDto.setUserName(sportBookId);
			reqDto.setPaymentType(1);
			reqDto.setMoney(amount /  GameThirdPartyInit.SPORTS_BOOK_RATE);
			reqDto.setTicketNo(ticketNo);
			//call 3rd
			Optional<SportsbookFundTransferRespDto> sportsbookFundTransferResponseDto = SportsbookAllServices
					.getInstance().fundTransfer(reqDto);
			if (sportsbookFundTransferResponseDto.isPresent()) {
				if (sportsbookFundTransferResponseDto.get().getCode().equals("0")) {
					baseResponseDto.setCode(ThirdpartyCodes.SUCCESS);
					baseResponseDto.setMessage(ThirdpartyMessages.SPORTS_BOOK_DEPOSIT_SUCCESS);
					SportsbookFundTransferResponse res = sportsbookFundTransferResponseDto.get().getData();
					double betAmount = res.getBetAmount() * GameThirdPartyInit.SPORTS_BOOK_RATE;
					res.setBetAmount(betAmount);
					sportsbookFundTransferResponseDto.get().setData(res);
					baseResponseDto.setData(sportsbookFundTransferResponseDto.get());
					logger.info("Sportsbook deposit success, response: "
							+ gson.toJson(sportsbookFundTransferResponseDto.get()));
				} else {
					logger.info("Sportsbook deposit failed, response: "
							+ gson.toJson(sportsbookFundTransferResponseDto.get()));
					baseResponseDto.setCode(ThirdpartyCodes.FAILED);
					String errorMessage = StringUtils.isNotBlank(sportsbookFundTransferResponseDto.get().getMessage())
							? sportsbookFundTransferResponseDto.get().getMessage()
							: ThirdpartyMessages.SPORTS_BOOK_DEPOSIT_FAILED;
					baseResponseDto.setMessage(errorMessage);
				}
			} else {
				logger.info("Sportsbook deposit failed response is null");
				baseResponseDto.setCode(ThirdpartyCodes.FAILED);
				baseResponseDto.setMessage(ThirdpartyMessages.SPORTS_BOOK_DEPOSIT_FAILED);
			}
		} else {
			baseResponseDto.setCode(ThirdpartyCodes.FAILED);
			baseResponseDto.setMessage(ThirdpartyMessages.SPORTS_BOOK_CANNOT_FIND_USER);
		}
		return baseResponseDto;
	}

	/**
	 * Sports book withdrawal money services
	 * 
	 * @param loginName
	 * @param amount
	 * @param ip
	 * @return
	 * @throws SQLException 
	 */
	public BaseResponseDto<SportsbookFundTransferRespDto> withdrawal(String loginName, Double amount, String ip) throws SQLException {
		// validation
		if (amount == null || amount <= 0) {
			logger.error("CMD withdraw amount <=0");
			return new BaseResponseDto<>("Không thể withdraw số tiền <=0 ");
		}

		if (!CommonMethod.ValidateRequest(loginName)) {
			logger.info("[CMD withdraw] Trong 10s chỉ thực hiện request 1 lần , playerName = {} "+ loginName);
			return new BaseResponseDto<>("Xin chờ 10 giây để thực hiện giao dịch tiếp theo ");
		}
		logger.info("Sportsbook withdrawal request loginName: " + loginName + ", amount: " + amount);
		
		BaseResponseDto<SportsbookFundTransferRespDto> baseResponseDto = new BaseResponseDto<>();
		// mapping user before call to Sports book platform
		BaseResponseDto<SportsbookUserBalanceRespDto> sportsbookItem = this.getUserBalance(loginName);
		if (sportsbookItem != null && ThirdpartyCodes.SUCCESS == sportsbookItem.getCode()) {
			String ticketNo = ThirdpartyConstant.CMD.SPORTS_BOOK_WID_WITHDRAW + CommonMethod.GetCurDate("yyMMddHHmmss")
					+ InitData.getids();
			SportsbookFundTransferReqDto reqDto = new SportsbookFundTransferReqDto();
			SportsbookUserBalanceRespDto sports = sportsbookItem.getData();
			String sportBookId = sports.getData().get(0).getUserName();
			double gameBalance =sports.getData().get(0).getBetAmount();
			if (gameBalance  < amount) {//đã x1000 trong hàm getbalance
				return new BaseResponseDto<>("Số dư của quý khách không đủ !");
			}
			reqDto.setUserName(sportBookId);
			reqDto.setPaymentType(0);
			reqDto.setMoney(amount / GameThirdPartyInit.SPORTS_BOOK_RATE);
			reqDto.setTicketNo(ticketNo);
			Optional<SportsbookFundTransferRespDto> wdResponse = SportsbookAllServices
					.getInstance().fundTransfer(reqDto);
			if (wdResponse.isPresent()) {
				if (wdResponse.get().getCode().equals("0")) {
					logger.info("Sportsbook withdrawal update balance in Sportsbook platform success");
					//update database
					boolean isBalance = updateBalance(loginName, amount, 0);
					if (!isBalance) {
						baseResponseDto.setCode(ThirdpartyCodes.FAILED);
						baseResponseDto.setMessage(ThirdpartyMessages.SPORTS_BOOK_WITHDRAWAL_FAILED);
						return baseResponseDto;
					} else {
						baseResponseDto.setCode(ThirdpartyCodes.SUCCESS);
						baseResponseDto.setMessage(ThirdpartyMessages.SPORTS_BOOK_WITHDRAWAL_SUCCESS);
						SportsbookFundTransferResponse res = wdResponse.get().getData();
						double betAmount = res.getBetAmount() * GameThirdPartyInit.SPORTS_BOOK_RATE;
						res.setBetAmount(betAmount);
						wdResponse.get().setData(res);
						baseResponseDto.setData(wdResponse.get());
						logger.info("Sportsbook withdrawal update balance success: "
								+ gson.toJson(wdResponse.get()));
					}
				} else {
					logger.info("Sportsbook withdrawal update balance failed: "
							+ gson.toJson(wdResponse.get()));
					baseResponseDto.setCode(ThirdpartyCodes.FAILED);
					String errorMessage = StringUtils.isNotBlank(wdResponse.get().getMessage())
							? wdResponse.get().getMessage()
							: ThirdpartyMessages.SPORTS_BOOK_WITHDRAWAL_FAILED;
					baseResponseDto.setMessage(errorMessage);
				}
			} else {
				logger.info("Sportsbook withdrawal failed, response is null");
				baseResponseDto.setCode(ThirdpartyCodes.FAILED);
				baseResponseDto.setMessage(ThirdpartyMessages.SPORTS_BOOK_WITHDRAWAL_FAILED);
			}
		} else {
			baseResponseDto.setCode(ThirdpartyCodes.FAILED);
			baseResponseDto.setMessage(ThirdpartyMessages.SPORTS_BOOK_CANNOT_FIND_USER);
		}
		return baseResponseDto;
	}

}
