package com.vinplay.vbee.common.statics;

import java.util.Arrays;
import java.util.List;

public interface Consts {
	String DB_CONFIG_FILE = "/var/app/config/db_pool.properties";
	String HAZELCAST_CONFIG_FILE = "/var/app/config/hazelcast.properties";
	String MONGO_CONFIG_FILE = "/var/app/config/mongo.properties";
	String RMQ_CONFIG_FILE = "/var/app/config/rmq.properties";
	String FOLDER_UPLOAD_APP = "/var/app/config/foler_upload.properties";

	String API_PORTAL_CONFIG_FILE = "config/api_portal.xml";
	String API_OTP_CONFIG_FILE = "config/api_otp.xml";
	String API_BACKEND_CONFIG_FILE = "config/api_backend.xml";
	String APP_CONFIG_FILE = "config/app_config.properties";
	int TD_KHOP_LENH_TOI_THIEU = 2000;
	int TD_1_VAN_KHOP_LENH_TOI_THIEU = 10000;
	int TCA_KHOP_LENH_TOI_THIEU = 2000;
	int TCA_1_VAN_KHOP_LENH_TOI_THIEU = 5000;
	String VBEE_LOGGER = "vbee";
	String API_PORTAL_LOGGER = "api";
	String PAY_LOGGER = "pay";
	String BACKEND_LOGGER = "backend";
	String USER_CORE_LOGGER = "user_core";
	String REPORT_LOGGER = "report";
	String RECHARGE_LOGGER = "recharge";
	String CASHOUT_LOGGER = "cashout";
	String VBEE_STATISTIC_LOGGER = "vbeeStatistic";
	String CACHE_USER = "users";
	String CACHE_USER_EXTRA_INFO = "cache_user_extra_info";
	String CACHE_HU_GAME_BAI = "huGameBai";
	String CACHE_FREEZE = "freeze";
	String CACHE_TAI_XIU = "cacheTaiXiu";
	String CACHE_WIN_THANH_DU_TX = "cacheWinThanhDuTX";
	String CACHE_LOSS_THANH_DU_TX = "cacheLossThanhDuTX";
	String CACHE_RUT_LOC_TX = "cacheRutLocTX";
	String CACHE_GOOGLE = "cacheGoogle";
	String CACHE_FACEBOOK = "cacheFacebook";
	String CACHE_VP_MINIGAME = "VPMinigame";
	String CACHE_TOI_CHON_CA = "cacheToiChonCa";
	String CACHE_CONFIG = "cacheConfig";
	String CACHE_GAME_BAI = "cacheGameBai";
	String CACHE_CAPTCHA = "cacheCaptcha";
	String CACHE_BROADCAST = "cacheBroadcast";
	String CACHE_KHO_BAU_FREE = "cacheKhoBauFree";
	String CACHE_TOP = "cacheTop";
	String CACH_TRANSACTION = "cacheTransaction";
	String CACHE_REPORT = "cacheReports";
	String CACHE_TOP_CAO_THU_VIN = "cacheTopCaoThuVin";
	String CACHE_TOP_CAO_THU_XU = "cacheTopCaoThuXu";
	String CACHE_LOG_PORTAL = "cacheLogPortal";
	String CACHE_USERS_PLAY_GAME = "cacheUsersPlayGame";
	String CACHE_EVENT_VP_BONUS = "cacheEventVpBonus";
	String CACHE_API_OTP = "cacheApiOtp";
	String CACHE_SLOT_FREE = "cacheSlotFree";
	String CACHE_DVT = "cacheDvt";
	String CACHE_TOKEN = "cacheToken";
	String CACHE_USER_MISSION_VIN = "cacheUserMissionVin";
	String CACHE_USER_MISSION_XU = "cacheUserMissionXu";
	String CACHE_AGENT_COMMISSION = "cacheAgentCommission";
	String PAYMENT_QUEUE = "queue_payment";
	String PAYMENT_QUEUE_MINIGAME = "queue_payment_minigame";
	String PAYMENT_QUEUE_GAME_BAI = "queue_payment_gamebai";
	String QUEUE_LOG_MONEY = "queue_log_money";
	String QUEUE_LOG_MONEY_EXTRA = "queue_log_money_extra";
	String QUEUE_LOG_CHUYEN_TIEN_DAI_LY = "queue_log_chuyen_tien_dai_ly";
	String QUEUE_OTP = "queue_otp";
	String QUEUE_HU_GAMEBAI = "queue_hu_gamebai";
	String QUEUE_LOG_GAMEBAI = "queue_log_gamebai";
	String QUEUE_POT = "queue_pot";
	String QUEUE_FUND = "queue_fund";
	String QUEUE_VQMM = "queue_vqmm";
	String QUEUE_TAIXIU = "queue_taixiu";
	String QUEUE_MINIPOKER = "queue_minipoker";
	String QUEUE_CAOTHAP = "queue_caothap";
	String QUEUE_BAUCUA = "queue_baucua";
	String QUEUE_POKEGO = "queue_pokego";
	String QUEUE_TOP = "queue_top";
	String QUEUE_SERVER_INFO = "queue_server_info";
	String QUEUE_KHO_BAU = "queue_kho_bau";
	String QUEUE_AVENGERS = "queue_avengers";
	String QUEUE_MY_NHAN_NGU = "queue_my_nhan_ngu";
	String QUEUE_VQV = "queue_vqv";
	String QUEUE_REPORT = "queue_report";
	String QUEUE_NU_DIEP_VIEN = "queue_nu_diep_vien";
	String QUEUE_VIPPOINT_EVENT = "queue_vippoint_event";
	String QUEUE_LOGIN_INFO = "queue_login_info";
	String QUEUE_LOG_NO_HU = "queue_log_nohu";
	String QUEUE_USER_MISSION = "queue_user_mission";
	String QUEUE_EXCHANGE_MONEY = "queue_exchange_money";
	String QUEUE_GIFT_CODE = "queue_gift_code";
	String QUEUE_COMMISSION = "queue_commission";
	String DEFAULT_FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
	String DEFAULT_FORMAT_DATE = "dd-MM-yyyy";
	String DEFAULT_FORMAT_DATE_TIME_MINUTES = "yyyyMMddHHmm";
	String DEFAULT_FORMAT_DATE_TIME_MARKETING = "yyyy-MM-dd";
	String ACTIVE = "1";
	String INACTIVE = "0";
	String SECRET_KEY = "@LOT79#6102$817";
	short SESSION_TIMEOUT_MINUTES = 180;
	short SOCIAL_ACCESS_TOKEN_TIMEOUT_MINUTES = 1440;
	short OTP_TIMEOUT_MINUTES = 5;
	short SECURITY_TIMEOUT_MINUTES = 1440;
	short EMAIL_TIMEOUT_MINUTES = 1440;
	short RECHARGE_FAIL_TIMEOUT_MINUTES = 60;

	// Cashout
	String CASH_OUT_BY_BANK = "CashOutByBank";
	String CASH_OUT_BY_PRINCEPAY = "CashOutByPrincePay";
	String REQUEST_CASHOUT = "REQUEST_CASHOUT";
	String CASH_OUT_BY_CLICKPAY = "CashOutByClickPay";
	String REFUND_RECHARGE_ERROR = "RefundRechargeError";
	String REFUND_RECHARGE = "RefundRecharge";
	List<String> LIST_CASH_OUT = Arrays.asList(new String[] { REQUEST_CASHOUT, CASH_OUT_BY_BANK, CASH_OUT_BY_CLICKPAY,
			CASH_OUT_BY_PRINCEPAY, REFUND_RECHARGE });

	// Deposit rechard
	String RECHARGE_BY_BANK = "RechargeByBank";
	String RECHARGE_BY_PRINCEPAY = "RechargeByPrincePay";
	String RECHARGE_BY_CLICKPAY = "RechargeByClickPay";
	String RECHARGE_BY_PAYWELL = "RechargeByPaywell";
	String RECHARGE_MANUAL = "RechargeByPaywell";
	String RECHARGE_SC = "RechargeBySC";
	String KHUYEN_MAI_58K = "VERIFY_PHONE";

	String RECHARGE_SAFE_BOX = "RechargeBySafeBox";
	List<String> LIST_RECHARGE = Arrays.asList(
			new String[] { RECHARGE_BY_BANK, RECHARGE_BY_PRINCEPAY, RECHARGE_BY_CLICKPAY, RECHARGE_BY_PAYWELL,RECHARGE_MANUAL,RECHARGE_SC });

	// third party
	interface GAME3RD {
		String AG = "ag";
		String IBC2 = "ibc2";
		String WM = "wm";
		String CMD = "cmd";
		List<String> LIST_GAME = Arrays.asList(new String[] { AG, IBC2, WM, CMD });

		String IBC2_WITHDRAW = "IBC2_WITHDRAW";
		String WM_WITHDRAW = "WM_WITHDRAW";
		String AG_WITHDRAW = "AG_WITHDRAW";
		String CMD_WITHDRAW = "CMD_WITHDRAW";
		List<String> LIST_WITHDRAW = Arrays
				.asList(new String[] { IBC2_WITHDRAW, WM_WITHDRAW, AG_WITHDRAW, CMD_WITHDRAW });

		String AG_DEPOSIT = "AG_DEPOSIT";
		String IBC2_DEPOSIT = "IBC2_DEPOSIT";
		String WM_DEPOSIT = "WM_DEPOSIT";
		String CMD_DEPOSIT = "CMD_DEPOSIT";
		List<String> LIST_DEPOSIT_3RD = Arrays
				.asList(new String[] { AG_DEPOSIT, IBC2_DEPOSIT, WM_DEPOSIT, CMD_DEPOSIT });
	}

	// recharge by payment portal
	List<String> LIST_RECHARGE_REAL = Arrays
			.asList(new String[] { RECHARGE_BY_PRINCEPAY, RECHARGE_BY_CLICKPAY, RECHARGE_BY_PAYWELL,RECHARGE_MANUAL,RECHARGE_SC });
	List<String> LIST_CASHOUT_REAL = Arrays
			.asList(new String[] { REQUEST_CASHOUT, CASH_OUT_BY_CLICKPAY, CASH_OUT_BY_PRINCEPAY, REFUND_RECHARGE });

	String SAFE_MONEY = "SafeMoney";
	String TRANSFER_MONEY = "TransferMoney";
	String VIN2XU = "NapXu";
	String ADMIN = "Admin";
	String GIFT_CODE = "GiftCode";
	String GIFT_CODE_VH = "GiftCodeVH";
	String GIFT_CODE_MKT = "GiftCodeMKT";
	String CASH_OUT_BY_VIPPOINT = "CashoutByVP";
	String BOT = "Bot";
	String REFUND_FEE = "RefundFee";
	String CHARGE_SMS = "ChargeSMS";
	String EVENT_VP = "EventVP";
	String EVENT_VP_BONUS = "EventVPBonus";
	String GC_AGENT = "GcAgent";
	String GC_AGENT_EXPORT = "GcAgentExport";
	String GC_AGENT_IMPORT = "GcAgentImport";
	String BONUS_TOP_DS = "BonusTopDS";
	String PKT_TICKET = "PktTicket";
	String ACTION_NAME_NHIEM_VU = "NhiemVu";
	// String SERVICE_NAME_TOPUP_VTCPAY = "N\u00e1\u00ba\u00a1p vin qua VTCPay";
	String VQMM = "VQMM";
	String VQVIP = "VQVIP";
	String TAI_XIU = "TaiXiu";
	String BAU_CUA = "BauCua";
	String MINI_POKER = "MiniPoker";
	String CAO_THAP = "CaoThap";
	String POKE_GO = "PokeGo";
	String CANDY = "CANDY";

	String KHO_BAU = "KhoBau";
	String NU_DIEP_VIEN = "NuDiepVien";
	String SIEU_ANH_HUNG = "SieuAnhHung";
	String VUONG_QUOC_VIN = "VuongQuocVin";
	String KHO_BAU_VQ_FREE = "KhoBauVqFree";
	String NU_DIEP_VIEN_VQ_FREE = "NuDiepVienVqFree";
	String SIEU_ANH_HUNG_VQ_FREE = "SieuAnhHungVqFree";
	String VUONG_QUOC_VIN_VQ_FREE = "VuongQuocVinVqFree";
	// new game
	String MAYBACH = "MAYBACH";
	String AUDITION = "Audition";
	String TAMHUNG = "TAMHUNG";
	String RANGE_ROVER = "RANGE_ROVER";
	String BENLEY = "BENLEY";
	String CHIEMTINH = "CHIEMTINH";
	String TAI_XIU_ST = "TAI_XIU_ST";
	String FISH = "FISH";
	String ROLL_ROYE = "ROLL_ROYE";
	String Spartan = "Spartan";
	String BIKINI = "BIKINI";
	String GALAXY = "GALAXY";

	// new game free spin
	String MAYBACH_FREE = "MAYBACH_FREE";
	String AUDITION_FREE = "Audition_FREE";
	String TAMHUNG_FREE = "TAMHUNG_FREE";
	String RANGE_ROVER_FREE = "RANGE_ROVER_FREE";
	String BENLEY_FREE = "BENLEY_FREE";
	String ROLL_ROYE_FREE = "ROLL_ROYE_FREE";
	String Spartan_FREE = "Spartan_FREE";
	String CHIEMTINH_FREE = "CHIEMTINH_FREE";
	String BIKINI_FREE = "BIKINI_FREE";

	String SAM = "Sam";
	String BA_CAY = "BaCay";
	String BINH = "Binh";
	String TLMN = "Tlmn";
	String TALA = "TaLa";
	String LIENG = "Lieng";
	String XI_TO = "XiTo";
	String BAI_CAO = "BaiCao";
	String POKER = "Poker";
	String POKER_TOUR = "PokerTour";
	String XOC_DIA = "XocDia";
	String XI_DZACH = "XiDzach";
	String CARO = "Caro";
	String CO_TUONG = "CoTuong";
	String CO_VUA = "CoVua";
	String CO_UP = "CoUp";
	String HAM_CA_MAP = "HamCaMap";
	String HOAN_TRA = "HOAN_TRA";
	List<String> TIEU_VIN = Arrays.asList(REFUND_RECHARGE, REQUEST_CASHOUT, CASH_OUT_BY_BANK, REFUND_RECHARGE_ERROR,
			CASH_OUT_BY_CLICKPAY, CASH_OUT_BY_PRINCEPAY, "Admin", "TransferMoney", "NapXu", "GcAgent", "GcAgentExport");
	List<String> NAP_VIN = Arrays.asList(RECHARGE_BY_CLICKPAY, RECHARGE_BY_PAYWELL, RECHARGE_BY_PRINCEPAY,RECHARGE_MANUAL,
			"RechargeByBank", "RechargeByIAP", "GiftCode", "CashoutByVP", "VQMM", "Admin", "VQVIP", "RefundFee",
			"EventVPBonus", "BonusTopDS", "GiftCodeMKT", "GiftCodeVH", "KhoBauVqFree", "NuDiepVienVqFree",
			"SieuAnhHungVqFree", "VuongQuocVinVqFree", "EventVP");
	List<String> NAP_XU = Arrays.asList("NapXu", "VQMM", "Admin", "GiftCode", "GiftCodeVH", "GiftCodeMKT");
	List<String> GAMES = Arrays.asList("TaiXiu", "BauCua", "MiniPoker", "CaoThap", "PokeGo", "KhoBau", "NuDiepVien",
			"SieuAnhHung", "VuongQuocVin", "Sam", "BaCay", "Binh", "Tlmn", "TaLa", "Lieng", "XiTo", "BaiCao", "Poker",
			"PokerTour", "XocDia", "XiDzach", "Caro", "CoTuong", "CoVua", "CoUp", "HamCaMap", CANDY, AUDITION, MAYBACH,
			BENLEY, RANGE_ROVER, Spartan, TAMHUNG, ROLL_ROYE, AUDITION_FREE, MAYBACH_FREE, BENLEY_FREE,CHIEMTINH,CHIEMTINH_FREE,
			RANGE_ROVER_FREE, Spartan_FREE, TAMHUNG_FREE, TAMHUNG_FREE, ROLL_ROYE_FREE, BIKINI, BIKINI_FREE, GALAXY);

	List<String> REAL_DEPOSIT_VIN = Arrays.asList(RECHARGE_BY_PRINCEPAY, RECHARGE_BY_CLICKPAY, RECHARGE_BY_PAYWELL ,RECHARGE_MANUAL);
	List<String> REAL_WITHDRAW_VIN = Arrays.asList(CASH_OUT_BY_CLICKPAY, CASH_OUT_BY_PRINCEPAY, REFUND_RECHARGE,
			REQUEST_CASHOUT);

	List<String> NO_GAME = Arrays.asList("NhiemVu", "CashOutByCard", "CashOutByTopUp", "RechargeByCard",
			"RechargeByVinCard", "RechargeByMegaCard", "RechargeByIAP", "RechargeByBank", "RechargeBySMS",
			"TransferMoney", "NapXu", "Admin", "GiftCode", "GiftCodeVH", "GiftCodeMKT", "CashoutByVP", "Bot",
			"RefundFee", "ChargeSMS", "EventVPBonus", "GcAgent", "GcAgentExport", "GcAgentImport", "BonusTopDS",
			"KhoBauVqFree", "NuDiepVienVqFree", "SieuAnhHungVqFree", "VuongQuocVinVqFree", "SafeMoney", "EventVP",
			"TopupVTCPay");
	
	List<String> VIN_IN_USER = Arrays.asList(RECHARGE_BY_CLICKPAY, RECHARGE_BY_PAYWELL, RECHARGE_BY_PRINCEPAY,RECHARGE_MANUAL,RECHARGE_SC);
	List<String> VIN_IN_EVENT = Arrays.asList(HOAN_TRA, KHUYEN_MAI_58K, "NhiemVu", "GiftCode", "GiftCodeMKT",
			"GiftCodeVH", "GcAgentImport", "RefundFee", "BonusTopDS", "CashoutByVP", "EventVPBonus", "EventVP", "VQMM",
			"VQVIP", "KhoBauVqFree", "NuDiepVienVqFree", "SieuAnhHungVqFree", "VuongQuocVinVqFree", "DIEM_DANH");
	
	List<String> VIN_OUT_USER = Arrays.asList(REQUEST_CASHOUT, CASH_OUT_BY_BANK, REFUND_RECHARGE_ERROR,
			CASH_OUT_BY_CLICKPAY, CASH_OUT_BY_PRINCEPAY, REFUND_RECHARGE);
	List<String> VIN_OTHER = Arrays.asList("NapXu", "ChargeSMS", "Admin", "GcAgent", "GcAgentExport", "PktTicket");
	// list game slot
	List<String> GAMES_SLOT = Arrays.asList(TAI_XIU, BAU_CUA, MINI_POKER, CAO_THAP, CANDY, AUDITION, MAYBACH, BENLEY,CHIEMTINH,CHIEMTINH_FREE,
			RANGE_ROVER, Spartan, TAMHUNG, ROLL_ROYE, AUDITION_FREE, MAYBACH_FREE, BENLEY_FREE, RANGE_ROVER_FREE,
			Spartan_FREE, TAMHUNG_FREE, TAMHUNG_FREE, ROLL_ROYE_FREE, TAI_XIU_ST, FISH, BIKINI, BIKINI_FREE, GALAXY);
//	//List game sport
//	List<String> GAMES_PORT = Arrays.asList(GAME3RD.CMD, GAME3RD.IBC2);
//	//List game casino
//	List<String> GAMES_CASINO = Arrays.asList(GAME3RD.WM, GAME3RD.AG);
//	//List game Slot(Nổ hũ)
//	List<String> GAMES_SLOTNEW = Arrays.asList(Spartan, BENLEY, MAYBACH, AUDITION, TAMHUNG, CHIEMTINH);
//	//List game Mini(Minigame)
//	List<String> GAMES_MININEW = Arrays.asList(TAI_XIU, TAI_XIU_ST, BAU_CUA, CAO_THAP, MINI_POKER, POKE_GO, XOC_DIA);
//	//List game bài(Game bài)
//	List<String> GAMES_BAI = Arrays.asList(BA_CAY, TLMN, BAU_CUA, CAO_THAP, MINI_POKER, POKE_GO, XOC_DIA);

	List<String> GAMES_BAI = Arrays.asList(SAM, BA_CAY, BINH, TLMN, TALA, LIENG, XI_TO, BAI_CAO, POKER, POKER_TOUR,
			XOC_DIA, XI_DZACH);

	List<String> GAMES_KHAC = Arrays.asList(CARO, CO_TUONG, CO_VUA, CO_UP, HAM_CA_MAP);

	// list game slot

	int TRANSACTION_LOG_MONEY_IN_GAME = 1;
	int TRANSACTION_LOG_MONEY_RECEIPT = 2;
	int TRANSACTION_LOG_MONEY_SEND = 3;
	long MONEY_REGISTER = 100000000L;
	String SAM_LOG = "sam_log";
	String TIEN_LEN_LOG = "tien_len_log";
	int PAGE_SIZE = 10;
	String FACEBOOK = "fb";
	String GOOGLE = "gg";
	int VIPPOINT_INDEX = 5000000;
	String OTP_SMS = "0";
	String OTP_APP = "1";
	String POT_SYSTEM = "Vinplay";
	long SLOT_FREE_WIN_MAX = 50000L;
	int MAX_TRANSACTION_CACHE = 65;
	long TRANSACTION_CACHE_TTL_HOURS = 72L;
	String C = null;
	String CACHE_BAN_CHAT = "cacheBanChat";
	String COUNT_REQUEST_PORTAL_LOGGER = "count_request_portal_logger";
	String TONG = "Tong";
	String VINPLAY = "vinplay";

	String PAYMENT_CONFIG_FILE = "/var/app/config/payment.json";
	String GAME_CONFIG_FILE = "/var/app/config/gameaccess.json";
	String GAME_CONFIG_FILE_DEV = "/var/app/config/gameaccessdev.json";
	String WEB = "web";
	String ANDROID = "ad";
	String IOS = "ios";
	String WINPHONE = "wp";
	String FACEBOOK_APP = "fb";
	String DESKTOP = "dt";
	String OTHER = "ot";

	String SYSTEM = "system";
	String DAI_LY_VIN = "DaiLyVin";
	String MARKETING_VIN = "MarketingVin";
	String VAN_HANH_VIN = "VanHanhVin";
	String DAI_LY_XU = "DaiLyXu";
	String MARKETING_XU = "MarketingXu";
	String VAN_HANH_XU = "VanHanhXu";
	int FREEZE_MONEY = 0;
	int NOT_FREEZE_MONEY = 1;
	int UNLOCKED_FREEZE_MONEY = 2;
	String SUCCESS_NGAN_LUONG_TRANS = "00";
	int TOP_DS_FREEZE_MONEY = 0;
	int TOP_DS_UNLOCKED_FREEZE_MONEY = 1;
	int SCANNED = 0;
	int NOT_SCANNED = 1;
	int LOCKED = 1;
	int NOT_LOCK = 0;
	String TRANFER_MONEY_AGENT = "FreezeMoneyTranferAgent";
	String MONEY_VIN = "vin";
	String ALL = "all";
	String PRE_PAID = "1";
	String POST_PAID = "2";
	int CARD_NEW = 0;
	int CARD_USED = 1;
	String CARD_NEW_MESSAGE = "Th\u00e1\u00ba\u00bb ch\u00c6\u00b0a s\u00e1\u00bb\u00ad d\u00e1\u00bb\u00a5ng";
	String CARD_USED_MESSAGE = "Th\u00e1\u00ba\u00bb \u00c4\u2018\u00c3\u00a3 s\u00e1\u00bb\u00ad d\u00e1\u00bb\u00a5ng";
	String AUTHORIZATION = "Authorization";
	String KEY_BASE_AUTHEN = "fU3z7wP0IeFOPntKXcRifUDTGbV8AXyI";
	String SERVICE_NAME_NHIEM_VU = "Th\u00c6\u00b0\u00e1\u00bb\u0178ng Nhi\u00e1\u00bb\u2021m V\u00e1\u00bb\u00a5";
	int RATIO_FEE_VIN = 2;
	int RATIO_FEE_XU = 7;
	String TOP_RUT_LOC = "TopRutLoc";
	String TOP_TAN_LOC = "TopTanLoc";
	String TOP_DS_AGENTS_1 = "TopDSAgents1";
	int PASS_CONDITION_REWARD = 0;
	int NOT_PASS_CONDITION_REWARD = 1;
	int COMPLETE_ALL_LEVEL = 0;
	int NOT_COMPLETE_ALL_LEVEL = 1;
	int RATIO_NAP_VINPLAY_CARD = 10500;
	int TIMEOUT_ALERT = 1;

	String GIFTCODE_ACTIVE = "A";
	String GIFTCODE_INACTIVE = "I";
	String GIFTCODE_LOCK = "L";
	String GIFTCODE_USED = "U";
	int DONE = 1;
	int INIT = 0;

	String SUCCESS = "0";
	int IN_COMPLETE = 3;
	int BONUS_58K = 1;
	int BONUS_GIFTCODE_EVENT1 = 2;

	List<String> IP_OFFCIE = Arrays.asList(new String[] { "127.0.0.1", "0:0:0:0:0:0:0:1" });

	List<String> IP_SERVER = Arrays.asList(new String[] { "127.0.0.1", "0:0:0:0:0:0:0:1"});//, "116.97.240.231"
	
	List<String> IP_DAILY = Arrays.asList(new String[] { "127.0.0.1", "0:0:0:0:0:0:0:1"});//, "116.97.240.231"
	
	interface SERVICE_PAYMENT {
		String CASHOUT = "Cashout";
		String REJECT_CASHOUT = "REJECT_CASHOUT";
		List<String> LIST = Arrays.asList(new String[] { CASHOUT, REJECT_CASHOUT });
	}

	interface USER_TYPE {
		int BOT = 1;
		int REAL = 0;
		int TEST_SPORT = 2;
		int TEST_XD = 3;
	}
	interface MAIL{
		String REGEX ="<on click='OpenURL' param = '%s'><i><b><u><color=#57FF08>TẠI ĐÂY</color></u></b></i></on>";
		
		String TITLE_WELCOME="Chào Mừng Tân Thủ Sun88";
		String CONTENT_WELCOME = "Chúc mừng quý khách đã tạo thành công tài khoản tại Sun88! Chúng tôi hân hạnh gửi đến Quý Khách chương trình Khuyến Mãi chào mừng Tân Thủ. "
				+ "Để nhận giftcode 79k free Quý Khách xin vui lòng xem chi tiết ";
		String LINK_WELCOME = "https://news.sun88.link/Sun88-dang-ky-tai-khoan-nhan-ngay-79k/";
		
		String TITLE_NAPDAU="Cơn Mưa Tiền Thưởng NẠP ĐẦU Trên Sun88";
		String CONTENT_NAPDAU = "Bất cứ giao dịch gửi tiền lần đầu nào khi tham gia chơi tại các sản phẩm: mini game, slot game và game bài trên Sun88 đều sẽ nhận được tiền thưởng tương ứng với giá trị tiền gửi nhất định. Để tham gia khuyến mãi xem chi tiết  ";
		String LINK_NAPDAU = "https://news.sun88.link/con-mua-tien-thuong-khi-thuc-hien-giao-dich-gui-tien-tren-Sun88/";
		
		String TITLE_BONUS_DAU="Sun88 Thưởng 170% Gửi Tiền Lần Đầu";
		String CONTENT_BONUS_DAU = "Chương trình chào mừng cổng game Sun88 cập bến Việt Nam, tặng ngay 100% tổng giá trị nạp tiền cho lần nạp tiền đâu tiên, 50% tổng giá trị nạp cho lần nạp tiền thứ 2 và 20% tổng giá trị nạp cho lần nạp tiền thứ 3, khi tham gia chơi tại các sản phẩm thể thao IBC, Cmd và Live Casino AG, Wm. Để tham gia khuyến mãi xem chi tiết ";
		String LINK_BONUS_DAU = "https://news.sun88.link/Sun88-thuong-hon-12-trieu-dong-khuyen-mai-nap-tien/";
		
		String TITLE_GIOVANG="Giờ Vàng Gửi Tiền, Nhận Ngàn Thưởng Lớn";
		String CONTENT_GIOVANG = "Đồng hành cùng các giải đấu bóng đá lớn như Euro 2020, Copa 2021, Sun88 tặng thưởng gửi tiền trong khung giờ vàng 20h tối đến 2h sáng lên đến 100% lên đến 2 triệu đồng, tham gia chơi bóng đá ủng hộ đội bóng ưa thích của mình ngay nhé. Để tham gia khuyến mãi xem chi tiết ";
		String LINK_GIOVANG = "https://news.sun88.link/the-thao-gio-vang/";
		
		String TITLE_TAIXIU="Giải đấu Tài Xỉu Sun88- Rộn ràng hơn 200 Triệu Tiền Thưởng";
		String CONTENT_TAIXIU = "Cơ hội nhận hơn 200 triệu tiền thưởng khi tham gia tại trò chơi tài xỉu tại Sun88. Để tham gia khuyến mãi xem chi tiết ";
		String LINK_TAIXIU = "https://news.sun88.link/giai-dau-tai-xiu-Sun88/";
	}
}
