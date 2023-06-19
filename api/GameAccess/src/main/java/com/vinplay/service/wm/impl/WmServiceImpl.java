package com.vinplay.service.wm.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.vinplay.dao.wm.WmDao;
import com.vinplay.dao.wm.impl.WmDaoImpl;
import com.vinplay.dto.ResultFormat;
import com.vinplay.dto.wm.CheckUserBalanceRespDto;
import com.vinplay.dto.wm.LogInRespDto;
import com.vinplay.dto.wm.WmFundTransferReqDto;
import com.vinplay.dto.wm.WmFundTransferRespDto;
import com.vinplay.interfaces.wm.FundTransferWmService;
import com.vinplay.interfaces.wm.MemberWmService;
import com.vinplay.item.WMUserItem;
import com.vinplay.logic.CommonMethod;
import com.vinplay.logic.InitData;
import com.vinplay.service.wm.WmService;
import com.vinplay.usercore.service.MoneyInGameService;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.MoneyInGameServiceImpl;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.response.MoneyResponse;


public class WmServiceImpl implements WmService{

	private static final Logger logger = Logger.getLogger(WmServiceImpl.class);
	private WmDao wmDao =new WmDaoImpl();

	public static ResultFormat CheckUserBalance(String playerName) throws Exception {
		ResultFormat rf = new ResultFormat();
		MemberWmService service = new MemberWmService();
		try {
            //call WM games API
            CheckUserBalanceRespDto rsDto = service.CheckUserBalance(playerName);
            List<Object> list = new ArrayList<Object>();
			if (rsDto == null) {
				rf.setRes(1);
				rf.setMsg("Can not connect to wm!");
				return rf;
			}else {
				if (rsDto.getErrorCode() != 0) {
					rf.setRes(1);
					rf.setMsg("Đang bảo trì!");
					return rf;
				}else {
					if (rsDto.getResult() == null) {
						list.add(rsDto.getErrorCode());
						list.add(0);
					} else {
						list.add(rsDto.getErrorCode());
						list.add(rsDto.getResult());
						list.add(playerName);
					}
					
				}
			}
			
			rf.setList(list);
			rf.setRes(0);
			return rf;
		} catch (Exception ex) {
			rf.setRes(1);
			rf.setMsg(ex.getMessage());
			ex.printStackTrace();
			logger.error("CheckUserBalance WM", ex);
			return rf;
		}

	}

	private boolean updateBalance(String nickName, Double amount, int direction, String ip, String wid, int ispendding) throws SQLException {
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
			moneyResponse = moneyService.updateMoneyGame3rdUser(nickName, amount.longValue() * (-1), "vin", "wm",
					"WM_DEPOSIT", "NẠP TIỀN WM", 0, false);
		} else {
			// withdraw
			moneyResponse = moneyService.updateMoneyGame3rdUser(nickName, amount.longValue(), "vin", "wm",
					"WM_WITHDRAW", "RÚT TIỀN WM", 0, false);
		}
		if(moneyResponse!=null && "0".equals(moneyResponse.getErrorCode())) {
			return true;
		}
		return false;

	}

	public static WmFundTransferReqDto wmFundTranfer(String wmId, Double amount) {
		// request
		WmFundTransferRespDto reqDto = new WmFundTransferRespDto();
		reqDto.setUser(wmId);
		reqDto.setMoney(amount);
		reqDto.setSyslang(1);
		logger.info("WmFundTransferReqDto request , wmid=" + wmId + " ,amount =" + amount);
		// call API wm
		FundTransferWmService service = new FundTransferWmService();
		WmFundTransferReqDto resDto = service.FundTransfer(reqDto);
		return resDto;
	}

	@Override
	public ResultFormat checkBalance(String nickName) throws Exception {
		ResultFormat rf = new ResultFormat();
		WMUserItem wmUser = wmDao.findWmUserByNickName(nickName);
		if (wmUser != null) {
			String wmId = wmUser.getWmid();
			rf = CheckUserBalance(wmId + "");
		} else {
			rf.setRes(1);
			rf.setMsg("Không thể kết nối tới WM . Vui lòng liên hệ CSKH !");
		}
		return rf;
	}

	@Override
	public ResultFormat fundTransfer(String nickName,int direction, Double amount, String ip) {
		ResultFormat rf = new ResultFormat();
		String playerName = "";
		try {
			if (!CommonMethod.ValidateRequest(nickName)) {
				rf.setRes(1);
				rf.setMsg("Xin chờ 10 giây để thực hiện giao dich tiếp theo ");
				logger.info("WMSerice playerName : " + nickName + "Trong 10s chỉ thực hiện request 1 lần ");
				return rf;
			}

			logger.info("WMFundTransfer nickName=" + nickName + ", amount =" + amount + " , direction=" + direction);
			if (amount > 100000000 || amount < 1) {
				rf.setRes(2);
				rf.setMsg("Giao dịch thất bại ");
				logger.error("WMSerice loginname : " + playerName + "Giao dịch thất bại ");
				return rf;
			}

			// check wm balance
			Float curAmount = 0f;
			WMUserItem wmUser = wmDao.findWmUserByNickName(nickName);
			if (wmUser != null) {
				playerName = wmUser.getWmid();
				MemberWmService service = new MemberWmService();
				CheckUserBalanceRespDto rsDto = service.CheckUserBalance(playerName);
				if (rsDto != null && rsDto.getErrorCode() == 0) {
					curAmount = rsDto.getResult();
				} else {
					rf.setRes(1);
					rf.setMsg("Giao dịch thất bại , WM game đang bảo trì . Vui lòng liên hệ bộ phận CSKH !");
					logger.error("WMSerice loginname : " + playerName + "Giao dịch thất bại ");
					return rf;
				}
			} else {
				rf.setRes(99);
				rf.setMsg("Can not connect wm ID!");
				logger.error("WMSerice playerName : " + playerName + " Can not get Sport ID : is null");
				return rf;
			}
			String wid = CommonMethod.GetCurDate("yyMMddHHmmss") + InitData.getids();
			WmFundTransferReqDto resDto = new WmFundTransferReqDto();
			if (direction == 1) {
				//check format money
				if (amount % 1000 != 0) {
					return new ResultFormat(2, "Quý khách vui lòng nhập số tiền chẵn (là hệ số của 1000 vnđ) ");
				}
				// deposit
				boolean res = updateBalance(nickName, amount, direction, ip, wid, 0);
				if (res) {
					resDto = wmFundTranfer(playerName, amount / GameThirdPartyInit.WM_RATE);
					if (resDto.getResult() != null) {
						if (resDto.getErrorCode() == 0) {
							logger.info("WMSerice playerName : " + nickName + " : " + playerName + " finish  FundTransfer ");
							List<Object> list = new ArrayList<Object>();
							list.add(resDto.getResult().getCash());
							rf.setList(list);
							rf.setMsg("SUCCESS");
							rf.setRes(0);
							return rf;
						} else {
							rf.setRes(1);
							rf.setMsg("Transaction failed, Please try again later!");
							logger.error("WMSportSerice playerName : " + playerName + " Error_code : " + resDto.getErrorCode() + " msg :" + resDto.getErrorMessage());
							return rf;
						}
					} else {
						rf.setRes(1);
						rf.setMsg("Liên hệ CSKH !");
						logger.error("WMSportSerice playerName : " + playerName + " Can not connect WM");
						return rf;
					}
				} else {
					rf.setRes(1);
					rf.setMsg("Update account unsuccessful !");
					return rf;
				}
			} else if (direction == 0) {
				// withdraw
				if (amount > curAmount * GameThirdPartyInit.WM_RATE) {
					rf.setRes(1);
					rf.setMsg("Tài khoản không đủ số dư để giao dịch ");
					logger.info("Tài khoản không đủ số dư để giao dịch ");
					return rf;
				}
				resDto = wmFundTranfer(playerName, (-1) * amount / GameThirdPartyInit.WM_RATE);
				if (resDto.getResult() != null) {
					if (resDto.getErrorCode() == 0) {
						if (resDto.getErrorCode() == 0) {
							boolean res = updateBalance(nickName, amount, direction, ip, wid, 0);
							if (res) {
								logger.info("WMSportSerice playerName : " + nickName + " : " + playerName + " finish  FundTransfer ");
								List<Object> list = new ArrayList<Object>();
								list.add(resDto.getResult().getCash());
								rf.setList(list);
								rf.setMsg("SUCCESS");
								rf.setRes(0);
								return rf;
							} else {
								logger.info("WMSportSerice playerName : " + nickName + " : " + playerName + " finish  FundTransfer but can not update ekhelo balance");
								List<Object> list = new ArrayList<Object>();
								list.add(resDto.getResult().getCash());
								rf.setList(list);
								rf.setMsg("Finish  FundTransfer but can not save your ekhelo balance, Please contact customer service!");
								rf.setRes(1);
								return rf;
							}
						} else {
							rf.setRes(1);
							rf.setMsg("Transaction failed, Please try again later!");
							logger.error("WMSportSerice playerName : " + playerName + " Error_code : " + resDto.getErrorCode() + " msg :" + resDto.getErrorMessage());
							return rf;
						}
					} else {
						rf.setRes(1);
						rf.setMsg("Transaction failed, Please try again later!");
						logger.error("WMSportSerice playerName : " + playerName + " Error_code : " + resDto.getErrorCode() + " msg :" + resDto.getErrorMessage());
						return rf;
					}
				} else {
					if (resDto != null && 10805 == resDto.getErrorCode()) {
						rf.setRes(1);
						rf.setMsg("Tài khoản WM không đủ để chuyển quỹ !");
						logger.error("WMSportSerice playerName : " + playerName + " Can not connect WM");
						return rf;
					}

					rf.setRes(1);
					rf.setMsg("Không thể connect tới WM , chuyển quỹ thất bại !");
					logger.error("WMSportSerice playerName : " + playerName + " Can not connect WM");
					return rf;
				}
			} else {
				rf.setRes(1);
				rf.setMsg("Transaction type is incorrect!");
				logger.error("WMSportSerice playerName : " + playerName + " msg : Transaction type is incorrect!");
				return rf;
			}
		} catch (Exception ex) {
			rf.setRes(1);
			rf.setMsg(ex.getMessage());
			ex.printStackTrace();
			logger.error("WMSportSerice playerName :" + playerName, ex);
			return rf;
		}
	}

	@Override
	public ResultFormat login(String nickName) throws Exception {
		ResultFormat rf = new ResultFormat();
		if (nickName == null || "".equals(nickName)) {
			rf.setRes(1);
			rf.setMsg("loginname is null or empty");
			return rf;
		}
		WMUserItem wmUserItem = wmDao.mappingUserWm(nickName);//update nickName
		if (wmUserItem == null) {
			logger.error("[wmUserItem]: Không tồn tại user này, nickName = " + nickName);
			rf.setRes(1);
			rf.setMsg("Không tồn tại user này");
			return rf;
		}
		String wmId = wmUserItem.getWmid();
		String password = wmUserItem.getPassword();
		logger.info("request Login games WM ,wmId= " + wmId + " ,password =" + password);
		MemberWmService service = new MemberWmService();
		LogInRespDto loginDto = service.login(wmId, password);
		if (loginDto == null) {
			logger.error("[UILoginWM]: Không connect được tới WM: loginDto = " + loginDto);
			rf.setRes(1);
			rf.setMsg("Liên hệ bộ phận CSKH !");
			return rf;
		}
		
		String wmSessionToken = loginDto.getResult();
		Integer error_code = loginDto.getErrorCode();
		List<Object> obj = new ArrayList<Object>();
		obj.add(error_code);
		obj.add(wmSessionToken);
		rf.setRes(0);
		rf.setList(obj);
		rf.setMsg("SUCCESS");
		return rf;
	}
	
}
