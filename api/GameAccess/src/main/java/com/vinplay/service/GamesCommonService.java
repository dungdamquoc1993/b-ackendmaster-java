/**
 * Archie
 */
package com.vinplay.service;

import org.apache.log4j.Logger;

import com.vinplay.dto.FundTransferHistoryDto;
import com.vinplay.dto.ResultFormat;
import com.vinplay.utils.BaseResponse;


/**
 * @author Archie
 *
 */
public class GamesCommonService {
	private static final Logger logger = Logger.getLogger(GamesCommonService.class);

	public static BaseResponse<String> checkPlayerExist(String loginName, String gameType) {
		return null;
	}
	
	private static String showName(int thirdPartyId) {
		switch (thirdPartyId) {
		case 2:
			return "Slot HABA";
		case 3:
			return "Casino EBET";
		case 20:
			return "Tài xỉu";
		case 21:
			return "Xổ số nhanh";
		case 22:
			return "Bầu cua";
		case 23:
			return "Xóc đĩa";
		case 24:
			return "Bắn Cá GG";
		case 25:
			return "Sport IBC2";
		case 26:
			return "Đá gà";
		case 27:
			return "12 con giáp";
		case 28:
			return "Play Tech";
		case 29:
			return "Sky Wind";
		case 30:
			return "Casino SA";
		case 31:
			return "Gaming Asia";
		default:
			return "";
		}
	}
	
	public static ResultFormat updateBalance(String loginname, Double amount, int direction, String ip, String wid,
			int thirdpartyid, Double gameBalance) {
	
		return null;
	}
	
	
	public static boolean saveTransferHistory(FundTransferHistoryDto funHis) {
		String loginname = funHis.getLoginname();
		double amount = funHis.getAmount();
		int direction = funHis.getDirection();
		String wid = funHis.getWid();
		int thirdparty_id = funHis.getThirdparty_id();
		double thirdparty_amount = funHis.getThirdparty_amount();
		String message = funHis.getMessage();

		// validation
		if (loginname == null || "".equals(loginname)) {
			logger.error("loginname is null or empty");
			return false;
		}
//		DbProc dp = null;
//		try {
//			dp = new DbProc();
//			dp.setSql("call PG_GAME_ACCESS.p_fundtransfer_history(?,?,?,?,?,?,?,?,?)");
//			dp.setString(1, loginname);
//			dp.setDouble(2, amount);
//			dp.setInt(3, direction);
//			dp.setString(4, wid);
//			dp.setInt(5, thirdparty_id);
//			dp.setDouble(6, thirdparty_amount);
//			dp.setString(7, message);
//			dp.exec();
//			Integer p_flag = Integer.parseInt(dp.getObject(1).toString());
//			if (p_flag == 0) {
//				return true;
//			} else {
//				return false;
//			}
//		} catch (Exception ex) {
//			logger.error("p_fundtransfer_history  :", ex);
//			return false;
//		} finally {
//			if (dp != null)
//				dp.close();
//		}
		return false;
	}
	
	public static void saveTransHistory(Double amount , int direction , Double gameBalance , String loginname ,
			String message ,String gameName ,Double userBalance ,String wid) {
//		DbProc dp = null;
//		try {
//			dp = new DbProc();
//			dp.setSql("call PG_GAME_ACCESS.p_fundtransfer_history(?,?,?,?,?,?,?,?,?)");
//			dp.setDouble(1, amount);
//			dp.setInt(2, direction);
//			dp.setDouble(3, gameBalance);
//			dp.setString(4, loginname);
//			dp.setString(5, message);
//			dp.setString(6, gameName);
//			dp.setDouble(7, userBalance);
//			dp.setString(8, wid);
//			dp.exec();
//		} catch (Exception ex) {
//			logger.error("p_fundtransfer_history  :", ex);
//		} finally {
//			if (dp != null)
//				dp.close();
//		}
	}
}
