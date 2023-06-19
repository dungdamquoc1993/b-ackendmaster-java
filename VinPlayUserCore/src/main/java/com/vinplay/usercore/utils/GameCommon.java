package com.vinplay.usercore.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.payment.entities.Bank;
import com.vinplay.usercore.dao.BankDao;
import com.vinplay.usercore.dao.GameConfigDao;
import com.vinplay.usercore.dao.impl.BankDaoImpl;
import com.vinplay.usercore.dao.impl.GameConfigDaoImpl;
import com.vinplay.usercore.entities.AttendanceConfig;
import com.vinplay.usercore.service.AttendanceService;
import com.vinplay.usercore.service.impl.AttendanceServiceImpl;
import com.vinplay.vbee.common.enums.StatusGames;
import com.vinplay.vbee.common.exceptions.KeyNotFoundException;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.statics.Consts;

public class GameCommon {
	public static final String SUCCESS = "1";
	public static final String ERROR = "-1";
	public static final String VIN = "VIN";
	public static final String MESSAGE_TYPE = "1";
	public static final String TOTAL_MESSAGE = "1";
	public static final String MESSAGE_INDEX = "1";
	public static final String IS_MORE = "0";
	public static final String CONTENT_TYPE = "0";
	public static final int OPEN = 0;
	public static final int CLOSE = 1;
	public static final int ON = 1;
	public static final int OFF = 0;

	public static List<Bank> LIST_BANK_NAME = new ArrayList<>();
	public static List<String> IP_BLACK_LIST = new ArrayList<>();
	public static String BANK_JSON = "";

	public static final int[] EXP_VIP = { 80, 800, 4500, 8600, 50000, 1000000 };
	public static final int[] HOAN_TRA_SPORT = { 50, 50, 60, 70, 80, 100 };// chia 10.000
	public static final int[] HOAN_TRA_CASINO = { 30, 30, 40, 50, 60, 80 };// chia 10.000
	public static final int[] HOAN_TRA_GAME = { 10, 10, 15, 20, 30, 50 }; // chia 10.000

	private static String webconf = "";
	private static String iosconf = "";
	private static String adconf = "";
	
	private static String readPaymentConfig() throws IOException {
		StringBuilder fullStr = new StringBuilder();
		try (BufferedReader br = Files.newBufferedReader(Paths.get(Consts.PAYMENT_CONFIG_FILE))) {
			String line;
			while ((line = br.readLine()) != null) {
				fullStr.append(line);
			}
		}
		return fullStr.toString();
	}
	private static final GameConfigDao dao = new GameConfigDaoImpl();
	
	public static void initPayment(IMap<String,String> map) throws SQLException {
		 String configpay;
			try {
				configpay = readPaymentConfig();
			} catch (IOException e1) {
				configpay = dao.getGameCommon("payment_config");
			}
	        map.put(CacheConfigName.PAYMENTCONFIGCACHE, configpay);
	}
	

	private static void initConfigVersionStatus(Map<String, String> mapConfig) throws JSONException {
		// get web
		JSONObject webConfig = new JSONObject(mapConfig.get("web"));
		webconf = webConfig.toString();
		// get ios
		JSONObject iosConfig = new JSONObject(mapConfig.get("ios"));
		iosconf = iosConfig.toString();
		// get adroid
		JSONObject androidConfig = new JSONObject(mapConfig.get("ad"));
		adconf = androidConfig.toString();
	}
	
	public static String getConfigVersionStatus(String platform) {
		switch (platform) {
		case "web":
			return webconf;
		case "ios":
			return iosconf;
		case "ad":
			return adconf;
		default:
			break;
		}
		return "";
	}
	
    public static void init() throws SQLException, JSONException, ParseException {
        HazelcastInstance instance = HazelcastClientFactory.getInstance();
        IMap<String,String> map = instance.getMap(Consts.CACHE_CONFIG);
        
        //get all web wap andr ios
        Map<String, String> mapConfig = dao.getGameConfig();
        
        initConfigVersionStatus(mapConfig);
        //get: admin
        map.put("ADMIN", dao.getGameCommon("admin"));
        map.put("STATUS_GAME", StatusGames.RUN.getId()+"");
        //get: game_common
        String commons = dao.getGameCommon("game_common");
        map.put("COMMONS", commons);
        JSONObject commonObj = new JSONObject(commons);
        String hotline = commonObj.getString("hotline");
        String email = commonObj.getString("email");
        String facebook = commonObj.getString("facebook");
        String web = commonObj.getString("web");
        
        initPayment(map);
        
        String gg_firebase = dao.getGameCommon("gg_firebase");
        map.put("GGFIREBASE", gg_firebase);
        String social = dao.getGameCommon("social");
        map.put("SOCIAL", social);
        map.put("HOT_LINE", hotline);
        map.put("EMAIL", email);
        map.put("FACEBOOK", facebook);
        map.put("WEB", web);
        map.put("SMS_OTP", commonObj.getString("sms_otp"));
        map.put("BANNER", commonObj.getJSONArray("banner").toString());
        map.put("BANNER_TOUR", commonObj.getJSONArray("banner_tour").toString());
        map.put("PASSWORD_DEFAULT", commonObj.getString("password_default"));
        map.put("IAP_KEY", commonObj.getString("iap_key"));
        map.put("UPDATE_BOT_VIN", String.valueOf(commonObj.getInt("bot_vin")));
        map.put("UPDATE_BOT_XU", String.valueOf(commonObj.getInt("bot_xu")));
        map.put("UPDATE_USER_VIN", String.valueOf(commonObj.getInt("user_vin")));
        map.put("UPDATE_USER_XU", String.valueOf(commonObj.getInt("user_xu")));
        map.put("VIN_PLUS", dao.getGameCommon("vin_plus"));
        
        //ratio return money
		String gameRefundRate = dao.getGameCommon("game_refund_rate");
		JSONObject refundRateObj = new JSONObject(gameRefundRate);
		map.put("EXP_VIP", refundRateObj.getString("EXP_VIP"));
		map.put("HOAN_TRA_SPORT", refundRateObj.getString("HOAN_TRA_SPORT"));
		map.put("HOAN_TRA_CASINO", refundRateObj.getString("HOAN_TRA_CASINO"));
		map.put("HOAN_TRA_EGAME", refundRateObj.getString("HOAN_TRA_EGAME"));
		//billing
		String billing = dao.getGameCommon("billing");
        map.put("BILLING", billing);
        JSONObject blObj = new JSONObject(billing);
        map.put("IS_NAP_MEGA_CARD", blObj.getString("is_nap_mega_card"));
        map.put("RATIO_NAP_MEGA_CARD", blObj.getString("ratio_nap_mega_card"));
        map.put("IS_RECHARGE_CARD", String.valueOf(blObj.getInt("is_nap_the")));
        map.put("IS_RECHARGE_VIN_CARD", String.valueOf(blObj.getInt("is_nap_vin_card")));
        map.put("IS_RECHARGE_BANK", String.valueOf(blObj.getInt("is_nap_vin_nh")));
        map.put("IS_RECHARGE_IAP", String.valueOf(blObj.getInt("is_nap_vin_iap")));
        map.put("IS_NAP_XU", String.valueOf(blObj.getInt("is_nap_xu")));
        map.put("IS_TRANSFER_MONEY", String.valueOf(blObj.getInt("is_chuyen_vin")));
        map.put("IS_CASHOUT_CARD", String.valueOf(blObj.getInt("is_mua_the")));
        map.put("IS_CASHOUT_TOPUP", String.valueOf(blObj.getInt("is_nap_dt")));
        map.put("IS_CASHOUT_BANK", String.valueOf(blObj.getInt("is_cashout_bank")));
        map.put("IS_CASHOUT_MOMO", String.valueOf(blObj.getInt("is_cashout_momo")));
        map.put("RATIO_NAP_XU", String.valueOf(blObj.getDouble("ratio_xu")));
        map.put("RATIO_RECHARGE_CARD", String.valueOf(blObj.getDouble("ratio_nap_the")));
        map.put("RATIO_RECHARGE_VIN_CARD", String.valueOf(blObj.getDouble("ratio_nap_vin_card")));
        map.put("RATIO_RECHARGE_BANK", String.valueOf(blObj.getDouble("ratio_nap_vin_nh")));
        map.put("RATIO_RECHARGE_MOMO", String.valueOf(blObj.getDouble("ratio_nap_vin_momo")));
        map.put("RATIO_RECHARGE_SMS", String.valueOf(blObj.getDouble("ratio_nap_sms")));
        map.put("RATIO_CASHOUT_CARD", String.valueOf(blObj.getDouble("ratio_mua_the")));
        map.put("RATIO_CASHOUT_TOPUP", String.valueOf(blObj.getDouble("ratio_nap_dt")));
        map.put("RATIO_TRANSFER", String.valueOf(blObj.getDouble("ratio_chuyen")));
        map.put("RATIO_CASHOUT_BANK", String.valueOf(blObj.getDouble("ratio_cashout_bank")));
        map.put("RATIO_CASHOUT_MOMO", String.valueOf(blObj.getDouble("ratio_cashout_momo")));
        map.put("TRANSFER_MONEY_MIN", String.valueOf(blObj.getInt("chuyen_vin_min")));
        map.put("CASHOUT_BANK_MIN", String.valueOf(blObj.getLong("cashout_bank_min")));
        map.put("CASHOUT_MOMO_MIN", String.valueOf(blObj.getLong("cashout_momo_min")));
        map.put("CASHOUT_LIMIT_USER", String.valueOf(blObj.getLong("cashout_limit_user")));
        map.put("CASHOUT_LIMIT_SYSTEM", String.valueOf(blObj.getLong("cashout_limit_system")));
        map.put("NUM_RECHARGE_FAIL", String.valueOf(blObj.getInt("num_recharge_fail")));
        map.put("NUM_CASHOUT_CARD", String.valueOf(blObj.getInt("num_doi_the")));
        map.put("CASHOUT_TIME_BLOCK", String.valueOf(blObj.getInt("cashout_time_block")));
        map.put("SUPER_ADMIN", blObj.getString("super_admin"));
        map.put("SUPER_AGENT", blObj.getString("super_agent"));
        map.put("CASHOUT_BANK_MAX", String.valueOf(blObj.getInt("cashout_bank_max")));
        map.put("CASHOUT_MOMO_MAX", String.valueOf(blObj.getInt("cashout_momo_max")));
        map.put("RATIO_REFUND_FEE_1", String.valueOf(blObj.getDouble("ratio_refund_fee_1")));
        map.put("RATIO_REFUND_FEE_2", String.valueOf(blObj.getDouble("ratio_refund_fee_2")));
        map.put("RATIO_REFUND_FEE_2_MORE", String.valueOf(blObj.getDouble("ratio_refund_fee_2_more")));
        map.put("REFUND_FEE_2_MORE", String.valueOf(blObj.getLong("refund_fee_2_more")));
        map.put("RATIO_TRANSFER_DL_1", String.valueOf(blObj.getDouble("ratio_transfer_dl_1")));
        map.put("DL1_TO_SUPER_MIN", String.valueOf(blObj.getLong("dl1_to_super_min")));
        map.put("DL1_TO_SUPER_MAX", String.valueOf(blObj.getLong("dl1_to_super_max")));
        map.put("DL1_TO_SUPER_MIN_X", String.valueOf(blObj.getLong("dl1_to_super_min_x")));
        map.put("IAP_MAX", String.valueOf(blObj.getInt("iap_max")));
        map.put("SYSTEM_IAP_MAX", String.valueOf(blObj.getInt("system_iap_max")));
        map.put("RATIO_TRANSFER_01", String.valueOf(blObj.getDouble("r_tf_01")));
        map.put("RATIO_TRANSFER_02", String.valueOf(blObj.getDouble("r_tf_02")));
        map.put("RATIO_TRANSFER_20", String.valueOf(blObj.getDouble("r_tf_20")));
        map.put("RATIO_TRANSFER_21", String.valueOf(blObj.getDouble("r_tf_21")));
        map.put("RATIO_TRANSFER_22", String.valueOf(blObj.getDouble("r_tf_22")));
        map.put("RATIO_TRANSFER_11", String.valueOf(blObj.getDouble("r_tf_11")));
        map.put("RATIO_TRANSFER_12", String.valueOf(blObj.getDouble("r_tf_12")));
        map.put("SMS_PLUS_OPEN", String.valueOf(blObj.getInt("is_sms_plus")));
        map.put("SMS_OPEN", String.valueOf(blObj.getInt("is_sms")));
        map.put("API_OTP_OPEN", String.valueOf(blObj.getInt("is_api_otp")));
        
        JSONObject alertObj = new JSONObject(dao.getGameCommon("alert"));
        map.put("ALERT_URL", alertObj.getString("alert_url"));
        map.put("COUNT_FAIL", alertObj.getString("count_fail"));
        map.put("DISCONNECT_GROUP_NUMBER", alertObj.getString("disconnect_group_number"));
        map.put("PENDING_GROUP_NUMBER", alertObj.getString("pending_group_number"));
        map.put("FREEZE_MONEY_GROUP_NUMBER", alertObj.getString("freeze_money_group_number"));
        map.put("MEGA_CARD_GROUP_NUMBER", alertObj.getString("mega_card_group_number"));

        String telegramToken = alertObj.getString("Telegram_boot_token");
        String telegramChatId = alertObj.getString("Telegram_chat_id");
        map.put("Telegram_boot_token", telegramToken);
        map.put("Telegram_chat_id", telegramChatId);

		String ipBlackList = dao.getGameCommon("ip_black_list");
		if (ipBlackList != null) {
			IP_BLACK_LIST = Arrays.asList(ipBlackList.split(","));
		}
//		JSONObject listBank = new JSONObject(dao.getGameCommon("bank"));
//		BANK_JSON = listBank.toString();
//		//LIST_BANK_NAME = Arrays.asList(listBank.getString("bank").split(","));
		BankDao bankDao = new BankDaoImpl();
		try {
			LIST_BANK_NAME = bankDao.findAll();
		} catch (Exception e) {
			
		}
		StringBuilder bankStr = new StringBuilder();
		if (LIST_BANK_NAME != null && !LIST_BANK_NAME.isEmpty()) {
			LIST_BANK_NAME.forEach(bank -> {
				bankStr.append(bank.getBank_name().trim()).append(",");
			});
		}
		BANK_JSON = String.format("{\"bank\":\"%s\"}", bankStr.substring(0, bankStr.length()-1));
		
		JSONObject shotfishConfig = new JSONObject(dao.getGameCommon("shotfish"));
        map.put(CacheConfigName.SHOTFISHCONFIGCACHE, shotfishConfig.toString());
        JSONObject telegramBotConfig = new JSONObject(dao.getGameCommon("telegrambot"));
        map.put(CacheConfigName.TELEGRAMBOTCONFIGCACHE, telegramBotConfig.toString());
        
        //Set cached attendance configuration
        AttendanceService attendanceService = new AttendanceServiceImpl();
        AttendanceConfig attendanceConfig = new AttendanceConfig();
        try{
        	attendanceConfig = attendanceService.getConfigLastest();
        }catch (Exception e) {
        	attendanceService = null;
		}
        map.put(CacheConfigName.ATTENDANCE_CONFIG, attendanceConfig == null ? "" : attendanceConfig.toJson());
    }
    
    public static String getValueStr(String key) throws KeyNotFoundException {
        HazelcastInstance instance = HazelcastClientFactory.getInstance();
        IMap map = instance.getMap("cacheConfig");
        if (map.containsKey(key)) {
            return (String)map.get(key);
        }
        throw new KeyNotFoundException();
    }

    public static int getValueInt(String key) throws KeyNotFoundException, NumberFormatException {
        HazelcastInstance instance = HazelcastClientFactory.getInstance();
        IMap map = instance.getMap("cacheConfig");
        if (map.containsKey(key)) {
            return Integer.parseInt((String)map.get(key));
        }
        throw new KeyNotFoundException();
    }

    public static double getValueDouble(String key) throws KeyNotFoundException, NumberFormatException {
        HazelcastInstance instance = HazelcastClientFactory.getInstance();
        IMap map = instance.getMap("cacheConfig");
        if (map.containsKey(key)) {
            return Double.parseDouble((String)map.get(key));
        }
        throw new KeyNotFoundException();
    }

    public static long getValueLong(String key) throws KeyNotFoundException, NumberFormatException {
        HazelcastInstance instance = HazelcastClientFactory.getInstance();
        IMap map = instance.getMap("cacheConfig");
        if (map.containsKey(key)) {
            return Long.parseLong((String)map.get(key));
        }
        throw new KeyNotFoundException();
    }

    public static String getHuVangGameBai() throws KeyNotFoundException {
        HazelcastInstance instance = HazelcastClientFactory.getInstance();
        IMap map = instance.getMap("cacheConfig");
        if (map.containsKey("HU_GAME_BAI")) {
            return (String)map.get("HU_GAME_BAI");
        }
        throw new KeyNotFoundException();
    }

    public static List<String> getPhoneAlert() throws KeyNotFoundException {
        HazelcastInstance instance = HazelcastClientFactory.getInstance();
        IMap map = instance.getMap("cacheConfig");
        if (map.containsKey("LIST_PHONE_ALERT")) {
            String[] split;
            ArrayList<String> res = new ArrayList<String>();
            String[] arr = split = ((String)map.get("LIST_PHONE_ALERT")).split(",");
            for (String m : split) {
                if (m.isEmpty()) continue;
                res.add(m);
            }
            return res;
        }
        throw new KeyNotFoundException();
    }

}

