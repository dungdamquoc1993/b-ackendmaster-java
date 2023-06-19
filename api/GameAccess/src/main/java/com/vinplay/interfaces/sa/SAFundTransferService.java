package com.vinplay.interfaces.sa;

import org.apache.log4j.Logger;

import com.vinplay.dto.sa.SADepositResp;
import com.vinplay.dto.sa.SAWithdrawalResp;
import com.vinplay.logic.CommonMethod;
import com.vinplay.logic.InitData;
import com.vinplay.utils.Constants;

public class SAFundTransferService {
	private static final Logger logger = Logger.getLogger(SAFundTransferService.class);

	public SADepositResp DepositTransfer(Double amount_trans, String userName, String playerCode)throws Exception {
		try {
			double amount = amount_trans;
			 logger.info("DepositTransfer loginname : "+userName+" start  FundTransfer");
			
			String wid = "PD" + CommonMethod.GetCurDate("yyMMddHHmmss") + InitData.getids();
			boolean res= SAFundTransferServiceDB.updateBalance(userName, amount * Constants.SA_RATE, 1,"",wid,0);
			if(res) {
				SADepositResp resDto = SAUtils.deposit(amount_trans, userName, playerCode);
				if (resDto == null) {
					 logger.error("DepositTransfer playerName : "+userName+" : "+amount+" resDto : is null");
					return null;
				}else if(resDto.getErrorMsgId().equals("0")){
					 logger.info("DepositTransfer playerName : "+userName+" : "+amount+" cập nhật tài khoản thành công ");
					return resDto;
				}else {
					return null;
				}
			}else{
				 logger.info("DepositTransfer playerName : "+userName+" : "+amount+" cập nhật tài khoản thất bại ");
				 return null;
			}
		} catch (Exception ex) {
			logger.error("ex", ex);
			logger.error("DepositTransfer playerName :"+userName, ex);
			return null;
		}
	}
	
	public SAWithdrawalResp WithdrawTransfer(Double amount_trans, String userName, String playerCode)throws Exception {
		try {
			 logger.info("DepositTransfer loginname : "+userName+" start  FundTransfer");
			Double amount = amount_trans;
			
			SAWithdrawalResp resDto = SAUtils.withdrawal(amount_trans, userName, playerCode);
			if (resDto == null) {
				 logger.error("WithdrawalTransfer playerName : "+userName+" : "+amount+" resDto : is null");
				return null;
			}else if(resDto.getErrorMsgId().equals("0")){
				String wid = "PW" + CommonMethod.GetCurDate("yyMMddHHmmss") + InitData.getids();
				boolean res = SAFundTransferServiceDB.updateBalance(userName, amount * Constants.SA_RATE, 0, "", wid, 0);
				if(res) {
					 logger.info("WithdrawalTransfer playerName : "+userName+" : "+amount * Constants.SA_RATE+" cập nhật tài khoản thành công ");
					return resDto;
				}else{
					 logger.info("WithdrawalTransfer playerName : "+userName+" : "+amount * Constants.SA_RATE+" cập nhật tài khoản thất bại ");
					return null;
				}
			}else if(resDto.getErrorMsgId().equals("121")){
				return resDto;
			}
		} catch (Exception ex) {
			 logger.error("DepositTransfer playerName :"+userName, ex);
			return null;
		}
		return null;
	}

}
