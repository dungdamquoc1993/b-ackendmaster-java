/**
 * Archie
 */
package com.vinplay.utils;

/**
 * @author Archie
 *
 */
public interface ThirdpartyConstant {
	String HABA_TRANSFER = "HABA_TRANSFER";
	String HABA_WID_DEPOSIT = "HD";
	String HABA_WID_WITHDRAW = "HW";

	String GG_TRANSFER = "GG_TRANSFER";
	String GG_WID_DEPOSIT = "GD";
	String GG_WID_WITHDRAW = "GW";
	
	String HABA = "HABA";
	String GG = "GG";

	String PT_TRANSFER = "PT_TRANSFER";
	String PT_WID_DEPOSIT = "PD";
	String PT_WID_WITHDRAW = "PW";
	String PT = "PT";

	String TD = "TD";
	String TD_TRANSFER = "TD_TRANSFER";

	String EBET_TRANSFER = "EBET_TRANSFER";
	String EBET = "EBET";
	String EBET_WID_DEPOSIT = "ED";
	String EBET_WID_WITHDRAW = "EW";

	String AG_TRANSFER = "AG_TRANSFER";
	String AG_LIVE = "AG_LIVE";
	String AG_SPORT = "AG_SPORT";

	String DS = "DS";
	String DS_TRANSFER = "DS_TRANSFER";
	String DS_WID_DEPOSIT = "DD";
	String DS_WID_WITHDRAW = "DW";

	String SA = "SA";
	String SA_TRANSFER = "SA_TRANSFER";
	String SA_WID_DEPOSIT = "SD";
	String SA_WID_WITHDRAW = "SW";

	interface IBC2 {
		String SPORTS_IBC2 = "GetSportBetLog";
		String IBC = "IBC";
		String IBC_TRANSFER = "IBC_TRANSFER";
	}
	

	enum GAMES_ERROR {
	    WAIT_20S(1, "Xin chờ 20 giây để thực hiện giao dich tiếp theo"),
	    TRANS_FAIL(2, "Giao dịch thất bại "),
	    ORDERSIDE(3,"Can not connect Sport ID!"),
	    BASECC(4, "Transaction type is incorrect!"),
	   
	    ;

	    private String message;
	    private int code;

	    GAMES_ERROR(int code, String message) {
	        this.message = message;
	        this.code = code;
	    }

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

	}
	
	interface CMD {
		String VERSION_KEY = "SPORTS_BOOK";
		String SPORTS_BOOK_TRANSFER = "SPORTS_BOOK_TRANSFER";
		String SPORTS_BOOK_WID_WITHDRAW = "SBW";
		String SPORTS_BOOK_WID_DEPOSIT = "SBD";
		
		String CREATE_MEMBER_METHOD = "createmember";
		String GET_BALANCE_METHOD = "getbalance";
		String BALANCE_TRANSFER = "balancetransfer";
		String BET_RECORD = "betrecord";
	}
//	interface SBO {
//		String VERSION_KEY = "SPORTS_SBO";
//		String API_CREATE_PLAYER = "Player/Create";
//		String API_BET_LOG = "Bet/Record/Get";
//		String API_BET_DETAILS = "Bet/Record/Detail";
//		String API_LAUNCH="Launch";
//		String API_GET_BALANCE="GetPlayerBalance";
//		String API_DEPOSIT="TransferIn";
//		String API_WITHDRAW="TransferOut";
//		
//		
//		String DEFAULT_WALLET_CODE = "gf_sport_wallet";
//		
//		String GAME_CODE_H5 = "sbo_2";
//		String GAME_CODE_PC = "sbo_1";
//		String VENDOR_CODE = "SBO";
//		String CREATE_MEMBER_METHOD = "createmember";
//		String GET_BALANCE_METHOD = "getbalance";
//		String BALANCE_TRANSFER = "balancetransfer";
//		String BET_RECORD = "betrecord";
//		int TIMESTAMP_DIGIT = 13;
//		String LANGUAGE ="VI";
//	}
	interface SBO {
		String API_CREATE_PLAYER = "web-root/restricted/player/register-player.aspx";
		String API_BET_LOG = "web-root/restricted/report/get-customer-report-by-modify-date.aspx";
		String API_BET_DETAILS = "web-root/restricted/report/v2/get-bet-list-by-modify-date.aspx";//web-root/restricted/report/get-bet-list-by-modify-date.aspx
		String API_LAUNCH="web-root/restricted/player/login.aspx";
		String API_GET_BALANCE="web-root/restricted/player/get-player-balance.aspx";
		String API_DEPOSIT="web-root/restricted/player/deposit.aspx";
		String API_WITHDRAW="web-root/restricted/player/withdraw.aspx";
		
		
		String DEFAULT_WALLET_CODE = "gf_sport_wallet";
		
		String VENDOR_CODE = "SBO";
		int TIMESTAMP_DIGIT = 13;
		String LANGUAGE ="VI";
	}
	interface ESPORT {
		String VERSION_KEY = "SPORTS_ESPORT";
		String API_CREATE_PLAYER = "api/v2/members/";
		String API_BET_LOG = "api/v2/bet-transaction/";
		String API_GET_BALANCE="api/v2/balance/?LoginName=";
		String API_DEPOSIT="api/v2/deposit/";
		String API_WITHDRAW="api/v2/withdraw/";
		
		
		String GAME_CODE_H5 = "sbo_2";
		String GAME_CODE_PC = "sbo_1";
		String VENDOR_CODE = "SBO";
		String CREATE_MEMBER_METHOD = "createmember";
		String GET_BALANCE_METHOD = "getbalance";
		String BALANCE_TRANSFER = "balancetransfer";
		String BET_RECORD = "betrecord";
		
		String LANGUAGE ="VI";
	}
	
	interface WM {
		int RATE = 1000;
	}

	int WITHDRAW_FLAG = 0;
	int DEPOSIT_FLAG =0;
	String DG = "DG";
	
	// DEFINE THIRDPARTY ID
	int CMD_ID = 805;
	int SBO_ID = 806;
	int AG_ID = 805;
	int SA_ID = 805;
	int PT_ID = 805;
}
