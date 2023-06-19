package com.vinplay.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.time.DateUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.json.JSONObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.vinplay.payment.utils.Constant;
import com.vinplay.shotfish.entites.ShotfishConfig;
import com.vinplay.usercore.service.MoneyInGameService;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.MoneyInGameServiceImpl;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.usercore.utils.CacheConfigName;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.vbee.common.enums.Games;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.messages.LogMoneyUserMessage;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.common.response.BaseResponse;
import com.vinplay.vbee.common.response.MoneyResponse;
import com.vinplay.vbee.common.rmq.RMQPublishTask;
import com.vinplay.vbee.common.statics.Consts;
import com.vinplay.dal.dao.LogFishDao;
import com.vinplay.dal.dao.LogFishTransactionDao;
import com.vinplay.dal.dao.impl.LogFishDaoImpl;
import com.vinplay.dal.dao.impl.LogFishTransactionDaoImpl;
import com.vinplay.dal.entities.fish.FishGameRecord;
import com.vinplay.dal.entities.fish.FishTransaction;

public class ShotFishUtils {
	private static final Logger logger = Logger.getLogger("api portal");

	// -----------------------------Utilities-------------------------//
	public enum ORDERSTATUS {
		NOT_EXIT(2, "Not exist"), SUCCESS(0, "Success"), ERROR(-1, "Error");

		private int code;
		private String description;

		private ORDERSTATUS(int code, String description) {
			this.code = code;
			this.description = description;
		}

		public String getDescription() {
			return description;
		}

		public int getCode() {
			return code;
		}
	}

	public static ORDERSTATUS valueOf(int code) throws IllegalArgumentException {
		return Arrays.stream(ORDERSTATUS.values()).filter(x -> x.code == code).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("unknown code: " + code));
	}

	public static <T extends Enum<T>> T valueOfIgnoreCase(Class<T> enumeration, String code) {

		for (T enumValue : enumeration.getEnumConstants()) {
			if (enumValue.name().equalsIgnoreCase(code)) {
				return enumValue;
			}
		}

		throw new IllegalArgumentException(
				String.format("There is no value with code '%s' in Enum %s", code, enumeration.getName()));
	}

	public static <T extends Enum<T>> T valueOfIgnoreCase(Class<T> enumeration, int code) {

		for (T enumValue : enumeration.getEnumConstants()) {
			if (enumValue.name().equalsIgnoreCase(String.valueOf(code))) {
				return enumValue;
			}
		}

		throw new IllegalArgumentException(
				String.format("There is no value with code '%s' in Enum %s", code, enumeration.getName()));
	}

	public static IvParameterSpec generateIv() {
		byte[] iv = new byte[16];
		new SecureRandom().nextBytes(iv);
		return new IvParameterSpec(iv);
	}

	public static SecretKeySpec createKey(String secret) {
		byte[] data = null;
		if (secret == null) {
			secret = "";
		}
		StringBuffer sb = new StringBuffer(16);
		sb.append(secret);
		while (sb.length() < 16) {
			sb.append("0");
		}
		if (sb.length() > 16) {
			sb.setLength(16);
		}
		try {
			data = sb.toString().getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new SecretKeySpec(data, "AES");
	}

	public static String encrypt(String strToEncrypt, String secret) {
		try {
			SecretKeySpec secretKey = createKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes()));
		} catch (Exception e) {
			logger.debug((Object) e);
			System.out.println("Error while encrypting: " + e.toString());
		}

		return null;
	}

	public static String decrypt(String strToDecrypt, String secret) {
		try {
			SecretKeySpec secretKey = createKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (Exception e) {
			logger.debug((Object) e);
			System.out.println("Error while decrypting: " + e.toString());
		}

		return null;
	}

	/**
	 * Hash string using MD5
	 * 
	 * @param input
	 * @return String hased
	 */
	public static String getMd5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(input.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * Hash string using HmacSHA1
	 * 
	 * @param value
	 * @param key
	 * @return String hased
	 */
	public static String getHMACSHA1(String value, String key) throws Exception {
		try {
			byte[] keyBytes = key.getBytes();
			SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(value.getBytes());
			byte[] hexBytes = new Hex().encode(rawHmac);
			return new String(hexBytes, "UTF-8");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * Hash string using HmacSHA256
	 * 
	 * @param key
	 * @param data
	 * @return String hased
	 * @throws Exception
	 */
	public static String getHMACSHA256(String key, String data) throws Exception {
		try {
			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("ASCII"), "HmacSHA256");
			sha256_HMAC.init(secret_key);
			return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("ASCII")));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static long getCurrentTimeStamp() {
		ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
		return utc.toEpochSecond();
	}

	public static String getCurrentTime(String pattern) {
		if (pattern == null || pattern.trim().isEmpty())
			pattern = "yyyyMMddHHmmssSSS";

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(new Date());
	}

	public static String getRequest(String url) throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		StringBuffer result = new StringBuffer();
		String line = "";
		try {
			HttpResponse response = client.execute(get);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			return "";
		}

		return result.toString();
	}

	public static boolean updateBalance(String nickName, Double amount, int direction) throws SQLException {
		MoneyInGameService moneyService = new MoneyInGameServiceImpl();
		MoneyResponse moneyResponse = null;
		if (direction == 1) {
			// check current balance
			UserService userService = new UserServiceImpl();
			UserModel u = userService.getUserByNickName(nickName);
			if (u.getVin() < amount.longValue()) {
				return false;
			}
			// deposit
			moneyResponse = moneyService.updateMoneyGame3rdUser(nickName, amount.longValue() * (-1), "vin", "fish",
					"FISH_DEPOSIT", "NẠP TIỀN FISH", 0, false);
		} else {
			// withdraw
			moneyResponse = moneyService.updateMoneyGame3rdUser(nickName, amount.longValue(), "vin", "fish",
					"FISH_WITHDRAW", "RÚT TIỀN FISH", 0, false);
		}
		if (moneyResponse != null && "0".equals(moneyResponse.getErrorCode())) {
			return true;
		}

		return false;
	}

	public static BaseResponse<Object> CheckUserInfo(String nickname, String accessToken, Long money,
			boolean ischeckBalance) {
		BaseResponse<Object> res = new BaseResponse<Object>();
		res.setData(null);
		res.setTotalRecords(0);
		res.setErrorCode("1001");
		res.setMessage("error");
		res.setSuccess(false);
		try {
			UserService userService = new UserServiceImpl();
			boolean isToken = userService.isActiveToken(nickname, accessToken);
			if (!isToken) {
				res.setData("Phiên làm việc của bạn đã hết hạn , vui lòng tải lại trang !");
				res.setErrorCode(Constant.ERROR_SESSION);
				return res;
			}

			UserModel userModel = null;
			try {
				userModel = userService.getUserByNickName(nickname);
			} catch (SQLException e1) {
			}

			if (userModel == null) {
				res.setData("Tài khoản không đúng.");
				res.setErrorCode(String.valueOf(Constant.ERROR_NOT_EXIST));
				return res;
			}

			if (ischeckBalance) {
				if (money > 0) {
					if (userModel.isBanLogin() || userModel.isBot()) {
						res.setData("Tài khoản bị khoá.");
						res.setErrorCode(Constant.ERROR_USER_BAN);
						return res;
					}

					long balance = userModel.getVin();
					if (balance < money) {
						res.setData("Số dư không đủ.");
						res.setErrorCode(Constant.MIN_MONEY);
						return res;
					}
				}
			}
		} catch (Exception e) {
			logger.error("[CHEKUSERINFO FISH] Exception: " + e.getMessage());
			return res;
		}

		res.setData(null);
		res.setTotalRecords(0);
		res.setErrorCode("0");
		res.setMessage("success");
		res.setSuccess(true);
		return res;
	}

	// -----------------------------For logic business
	// game-------------------------//
	/**
	 * Get shot fish configuration
	 * 
	 * @return ShotfishConfig
	 */
	public static ShotfishConfig getConfig() {
		try {
			HazelcastInstance instance = HazelcastClientFactory.getInstance();
			IMap<String, String> configCache = instance.getMap(Consts.CACHE_CONFIG);
			String value = configCache.get(CacheConfigName.SHOTFISHCONFIGCACHE).toString();
			Type type = new TypeToken<ShotfishConfig>() {
			}.getType();
			ShotfishConfig shotfishConfig = new Gson().fromJson(value, type);
			return shotfishConfig;
		} catch (Exception e) {
			logger.error("[GETCONFIG FISH] Exception: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Login game
	 * 
	 * @param nickname
	 * @param accessToken
	 * @param clientIP
	 * @param money
	 * @return
	 */
	public static BaseResponse<Object> LoginGame(String nickname, String accessToken, String clientIP, Long money) {
		BaseResponse<Object> valid = CheckUserInfo(nickname, accessToken, money, true);
		if (!valid.isSuccess())
			return valid;

		BaseResponse<Object> res = new BaseResponse<Object>();
		res.setData(null);
		res.setTotalRecords(0);
		res.setErrorCode("1001");
		res.setMessage("error");
		res.setSuccess(false);
		ShotfishConfig config = getConfig();
		LogFishTransactionDao logFishTransactionDao = new LogFishTransactionDaoImpl();
		FishTransaction fishTransaction = new FishTransaction();
		fishTransaction.setPrefix(config.prefix);
		fishTransaction.setNickname(nickname);
		fishTransaction.setAction("LOGIN");
		fishTransaction.setMoney(money);
		String orderId = config.agentId + getCurrentTime(null) + config.prefix + nickname;
		fishTransaction.setOrderId(orderId);
		String[] params = { "s=0", "account=" + config.prefix + nickname, "money=" + money, "orderid=" + orderId,
				"ip=" + clientIP, "lineCode=" + config.envCode, "KindID=" + config.kindId, "showReturn=0" };
		String param = "";
		param = String.join("&", params);
		fishTransaction.setParam(param);
		param = encrypt(param, config.secretKey);
		try {
			param = URLEncoder.encode(param, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			logger.error("[LOGIN FISH] Error encrypt param: " + e1.getMessage());
			e1.printStackTrace();
			return res;
		}

		Long timeStamp = getCurrentTimeStamp();
		String pKey = getMd5(config.agentId + String.valueOf(timeStamp) + config.secretKey);
		fishTransaction.setTimeStamp(timeStamp);
		fishTransaction.setKey(pKey);
		String url = config.urlApi + "agent=" + config.agentId + "&timestamp=" + timeStamp + "&param=" + param + "&key="
				+ pKey;
		try {
			fishTransaction.setUrlApi(url);
			String data = getRequest(url);
			JSONObject jsonObj = new JSONObject(data);
			int code = jsonObj.getJSONObject("d").getInt("code");
			if (code != 0) {
				fishTransaction.setStatus(ORDERSTATUS.ERROR.name());
				logFishTransactionDao.Save(fishTransaction);
				logger.error("[LOGIN FISH] Error response data: " + data);
				return res;
			}

			if (money > 0) {
				boolean result = updateBalance(nickname, money.doubleValue(), 1);
				// rollback money if update balance false
				if (!result) {
					fishTransaction.setStatus(ORDERSTATUS.ERROR.name());
					logFishTransactionDao.Save(fishTransaction);
					WithDraw(nickname, accessToken, money);
					res.setMessage(
							"Lỗi chuyển quỹ (deposit when login). Quý khách vui lòng thử lại lần nữa hoặc liên hệ với bộ phận CSKH để được hỗ trợ.");
					return res;
				}
			}

			fishTransaction.setStatus(ORDERSTATUS.SUCCESS.name());
			logFishTransactionDao.Save(fishTransaction);
			res.setData(jsonObj.getJSONObject("d").getString("url"));
			res.setTotalRecords(0);
			res.setErrorCode("0");
			res.setMessage("success");
			res.setSuccess(true);
			return res;
		} catch (Exception e) {
			logger.error("[LOGIN FISH] Exception: " + e.getMessage());
			return res;
		}
	}

	/**
	 * Check balance in game
	 * 
	 * @param nickname
	 * @return String data json
	 */
	public static BaseResponse<Object> CheckUserBalance(String nickname) {
		BaseResponse<Object> res = new BaseResponse<Object>();
		res.setData(null);
		res.setTotalRecords(0);
		res.setErrorCode("1001");
		res.setMessage("error");
		res.setSuccess(false);
		ShotfishConfig config = getConfig();
		String[] params = { "s=1", "account=" + config.prefix + nickname };
		String param = "";
		param = String.join("&", params);
		param = ShotFishUtils.encrypt(param, config.secretKey);
		try {
			param = URLEncoder.encode(param, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			logger.error("[CHECKUSERBALANCE FISH] Error encrypt param: " + e1.getMessage());
			e1.printStackTrace();
			return res;
		}

		Long timeStamp = getCurrentTimeStamp();
		String pKey = getMd5(config.agentId + String.valueOf(timeStamp) + config.secretKey);
		String url = config.urlApi + "agent=" + config.agentId + "&timestamp=" + timeStamp + "&param=" + param + "&key="
				+ pKey;
		try {
			String data = getRequest(url);
			JSONObject jsonObj = new JSONObject(data);
			int code = jsonObj.getJSONObject("d").getInt("code");
			if (code != 0) {
				logger.error("[CHECKUSERBALANCE FISH] Error response data: " + data);
				return res;
			}

			res.setData(((Double) jsonObj.getJSONObject("d").getDouble("money")).longValue());
			res.setTotalRecords(0);
			res.setErrorCode("0");
			res.setMessage("success");
			res.setSuccess(true);
			return res;
		} catch (Exception e) {
			logger.error("[CHECKUSERBALANCE FISH] Exception: " + e.getMessage());
			return res;
		}
	}

	/**
	 * Check order status
	 * 
	 * @param orderId
	 * @return String data json
	 */
	public static BaseResponse<Object> CheckOrderStatus(String orderId) {
		BaseResponse<Object> res = new BaseResponse<Object>();
		res.setData(null);
		res.setTotalRecords(0);
		res.setErrorCode("1001");
		res.setMessage("error");
		res.setSuccess(false);
		ShotfishConfig config = getConfig();
		String[] params = { "s=4", "orderid=" + orderId };
		String param = "";
		param = String.join("&", params);
		param = ShotFishUtils.encrypt(param, config.secretKey);
		try {
			param = URLEncoder.encode(param, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			logger.error("[CHECKORDERSTATUS FISH] Error encrypt param: " + e1.getMessage());
			e1.printStackTrace();
			return res;
		}

		Long timeStamp = getCurrentTimeStamp();
		String pKey = getMd5(config.agentId + String.valueOf(timeStamp) + config.secretKey);
		String url = config.urlApi + "agent=" + config.agentId + "&timestamp=" + timeStamp + "&param=" + param + "&key="
				+ pKey;
		try {
			String data = getRequest(url);
			JSONObject jsonObj = new JSONObject(data);
			int code = jsonObj.getJSONObject("d").getInt("code");
			if (code != 0) {
				logger.error("[CHECKORDERSTATUS FISH] Error response data: " + data);
				return res;
			}

			ORDERSTATUS status = valueOf(jsonObj.getJSONObject("d").getInt("status"));
			res.setData(jsonObj.getJSONObject("d").toString());
			if (status == ORDERSTATUS.SUCCESS) {
				res.setErrorCode(String.valueOf(status.getCode()));
				res.setMessage("success");
				res.setSuccess(true);
			} else {
				res.setErrorCode(String.valueOf(status.getCode()));
				res.setMessage("error");
				res.setSuccess(false);
			}

			return res;
		} catch (Exception e) {
			logger.error("[CHECKORDERSTATUS FISH] Exception: " + e.getMessage());
			return res;
		}
	}

	/**
	 * Check user in game
	 * 
	 * @param nickname
	 * @return String data json
	 */
	public static BaseResponse<Object> CheckUserInGame(String nickname) {
		BaseResponse<Object> res = new BaseResponse<Object>();
		res.setData(null);
		res.setTotalRecords(0);
		res.setErrorCode("1001");
		res.setMessage("error");
		res.setSuccess(false);
		ShotfishConfig config = getConfig();
		String[] params = { "s=10", "account=" + config.prefix + nickname };
		String param = "";
		param = String.join("&", params);
		param = encrypt(param, config.secretKey);
		try {
			param = URLEncoder.encode(param, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			logger.error("[CHECKUSERINGAME FISH] Error encrypt param: " + e1.getMessage());
			e1.printStackTrace();
			return res;
		}

		Long timeStamp = getCurrentTimeStamp();
		String pKey = getMd5(config.agentId + String.valueOf(timeStamp) + config.secretKey);
		String url = config.urlApi + "agent=" + config.agentId + "&timestamp=" + timeStamp + "&param=" + param + "&key="
				+ pKey;
		try {
			String data = ShotFishUtils.getRequest(url);
			JSONObject jsonObj = new JSONObject(data);
			int code = jsonObj.getJSONObject("d").getInt("code");
			if (code != 0) {
				logger.error("[CHECKUSERINGAME FISH] Error response data: " + data);
				return res;
			}

			res.setData(jsonObj.getJSONObject("d").getBoolean("status"));
			res.setTotalRecords(0);
			res.setErrorCode("0");
			res.setMessage("success");
			res.setSuccess(true);
			return res;
		} catch (Exception e) {
			logger.error("[CHECKUSERINGAME FISH] Exception: " + e.getMessage());
			return res;
		}
	}

	/**
	 * Deposit money game
	 * 
	 * @param nickname
	 * @param accessToken
	 * @param money
	 * @return String data json
	 */
	public static BaseResponse<Object> Deposit(String nickname, String accessToken, Long money) {
		BaseResponse<Object> valid = CheckUserInfo(nickname, accessToken, money, true);
		if (!valid.isSuccess())
			return valid;

		valid = CheckUserBalance(nickname);
		if (!valid.isSuccess())
			return valid;

		BaseResponse<Object> res = new BaseResponse<Object>();
		res.setData(null);
		res.setTotalRecords(0);
		res.setErrorCode("1001");
		res.setMessage("error");
		res.setSuccess(false);
		if (money < 0) {
			res.setMessage("Số tiền nạp không đúng");
			return res;
		}

		ShotfishConfig config = getConfig();
		LogFishTransactionDao logFishTransactionDao = new LogFishTransactionDaoImpl();
		FishTransaction fishTransaction = new FishTransaction();
		fishTransaction.setPrefix(config.prefix);
		fishTransaction.setNickname(nickname);
		fishTransaction.setAction("DEPOSIT");
		fishTransaction.setMoney(money);
		String orderId = config.agentId + getCurrentTime(null) + config.prefix + nickname;
		fishTransaction.setOrderId(orderId);
		String[] params = { "s=2", "account=" + config.prefix + nickname, "money=" + money, "orderid=" + orderId };
		String param = "";
		param = String.join("&", params);
		fishTransaction.setParam(param);
		param = encrypt(param, config.secretKey);
		try {
			param = URLEncoder.encode(param, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			logger.error("[DEPOSIT FISH] Error encrypt param: " + e1.getMessage());
			e1.printStackTrace();
			return res;
		}

		Long timeStamp = getCurrentTimeStamp();
		fishTransaction.setTimeStamp(timeStamp);
		String pKey = getMd5(config.agentId + String.valueOf(timeStamp) + config.secretKey);
		fishTransaction.setKey(pKey);
		String url = config.urlApi + "agent=" + config.agentId + "&timestamp=" + timeStamp + "&param=" + param + "&key="
				+ pKey;
		try {
			fishTransaction.setUrlApi(url);
			String data = getRequest(url);
			JSONObject jsonObj = new JSONObject(data);
			int code = jsonObj.getJSONObject("d").getInt("code");
			if (code != 0) {
				fishTransaction.setStatus(ORDERSTATUS.ERROR.name());
				logFishTransactionDao.Save(fishTransaction);
				logger.error("[DEPOSIT FISH] Error response data: " + data);
				return res;
			}

			boolean result = updateBalance(nickname, money.doubleValue(), 1);
			// rollback money if update balance false
			if (!result) {
				fishTransaction.setStatus(ORDERSTATUS.ERROR.name());
				logFishTransactionDao.Save(fishTransaction);
				WithDraw(nickname, accessToken, money);
				res.setMessage(
						"Lỗi chuyển quỹ (deposit). Quý khách vui lòng thử lại lần nữa hoặc liên hệ với bộ phận CSKH để được hỗ trợ.");
				return res;
			}

			fishTransaction.setStatus(ORDERSTATUS.SUCCESS.name());
			logFishTransactionDao.Save(fishTransaction);
			res.setData(((Double) jsonObj.getJSONObject("d").getDouble("money")).longValue());
			res.setTotalRecords(0);
			res.setErrorCode("0");
			res.setMessage("success");
			res.setSuccess(true);
			return res;
		} catch (Exception e) {
			logger.error("[DEPOSIT FISH] Exception: " + e.getMessage());
			return res;
		}
	}

	/**
	 * Withdraw money from balance of game
	 * 
	 * @param nickname
	 * @param accessToken
	 * @param money
	 * @return String data json
	 */
	public static BaseResponse<Object> WithDraw(String nickname, String accessToken, Long money) {
		BaseResponse<Object> res = new BaseResponse<Object>();
		res.setData(null);
		res.setTotalRecords(0);
		res.setErrorCode("1001");
		res.setMessage("error");
		res.setSuccess(false);
		if (money < 0) {
			res.setMessage("Số tiền nạp không đúng");
			return res;
		}

		BaseResponse<Object> valid = CheckUserInfo(nickname, accessToken, money, false);
		if (!valid.isSuccess())
			return valid;

		valid = CheckUserBalance(nickname);
		if (!valid.isSuccess())
			return valid;

		Long moneyReal = 0l;
		try {
			moneyReal = Long.parseLong(valid.getData().toString());
		} catch (Exception e) {
			res.setMessage(
					"Lỗi chuyển quỹ (deposit). Quý khách vui lòng thử lại lần nữa hoặc liên hệ với bộ phận CSKH để được hỗ trợ.");
			return res;
		}

		if (moneyReal < money) {
			res.setMessage(
					"Lỗi chuyển quỹ (withdraw). Vui lòng kiểm tra lại quỹ và đảm bảo số dư luôn lớn hơn số tiền muốn chuyển");
			return res;
		}

		ShotfishConfig config = getConfig();
		LogFishTransactionDao logFishTransactionDao = new LogFishTransactionDaoImpl();
		FishTransaction fishTransaction = new FishTransaction();
		fishTransaction.setPrefix(config.prefix);
		fishTransaction.setNickname(nickname);
		fishTransaction.setAction("DEPOSIT");
		fishTransaction.setMoney(money);
		String orderId = config.agentId + ShotFishUtils.getCurrentTime(null) + config.prefix + nickname;
		fishTransaction.setOrderId(orderId);
		String[] params = { "s=3", "account=" + config.prefix + nickname, "money=" + money, "orderid=" + orderId };
		String param = "";
		param = String.join("&", params);
		fishTransaction.setParam(param);
		param = encrypt(param, config.secretKey);
		try {
			param = URLEncoder.encode(param, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			logger.error("[WITHDRAW FISH] Error encrypt param: " + e1.getMessage());
			e1.printStackTrace();
			return res;
		}

		Long timeStamp = getCurrentTimeStamp();
		fishTransaction.setTimeStamp(timeStamp);
		String pKey = getMd5(config.agentId + String.valueOf(timeStamp) + config.secretKey);
		fishTransaction.setKey(pKey);
		String url = config.urlApi + "agent=" + config.agentId + "&timestamp=" + timeStamp + "&param=" + param + "&key="
				+ pKey;
		try {
			fishTransaction.setUrlApi(url);
			String data = ShotFishUtils.getRequest(url);
			JSONObject jsonObj = new JSONObject(data);
			int code = jsonObj.getJSONObject("d").getInt("code");
			if (code != 0) {
				fishTransaction.setStatus(ORDERSTATUS.ERROR.name());
				logFishTransactionDao.Save(fishTransaction);
				logger.error("[WITHDRAW FISH] Error response data: " + data);
				res.setMessage(
						"Lỗi chuyển quỹ (withdraw). Vui lòng kiểm tra lại quỹ và đảm bảo số dư luôn lớn hơn số tiền muốn chuyển");
				return res;
			}

			Double balance = 0d;
			try {
				balance = jsonObj.getJSONObject("d").getDouble("money");
			} catch (Exception e) {
				fishTransaction.setStatus(ORDERSTATUS.ERROR.name());
				logFishTransactionDao.Save(fishTransaction);
				logger.error("[WITHDRAW FISH] Error response data: " + data);
				res.setMessage(
						"Lỗi chuyển quỹ (withdraw). Vui lòng kiểm tra lại quỹ và đảm bảo số dư luôn lớn hơn số tiền muốn chuyển");
				return res;
			}
			boolean result = updateBalance(nickname, money.doubleValue(), 0);
			// rollback money if update balance false
			if (!result) {
				fishTransaction.setStatus(ORDERSTATUS.ERROR.name());
				logFishTransactionDao.Save(fishTransaction);
				Deposit(nickname, accessToken, money);
				res.setMessage(
						"Lỗi chuyển quỹ (withdraw). Quý khách vui lòng thử lại lần nữa hoặc liên hệ với bộ phận CSKH để được hỗ trợ.");
				return res;
			}

			fishTransaction.setStatus(ORDERSTATUS.SUCCESS.name());
			logFishTransactionDao.Save(fishTransaction);
			Map<String, Object> map = new HashMap<>();
			map.put("Transfer", money);
			map.put("Balance", balance.longValue());
			res.setData(map);
			res.setTotalRecords(0);
			res.setErrorCode("0");
			res.setMessage("success");
			res.setSuccess(true);
			return res;
		} catch (Exception e) {
			logger.error("[WITHDRAW FISH] Exception: " + e.getMessage());
			return res;
		}
	}

	/**
	 * Query history bet
	 * 
	 * @param startTime (Time stamp in milliseconds)
	 * @param endTime   (Time stamp in milliseconds)
	 * @return String json data
	 */
	public static BaseResponse<Object> History(String startTime, String endTime) {
		BaseResponse<Object> res = new BaseResponse<Object>();
		res.setData(null);
		res.setTotalRecords(0);
		res.setErrorCode("1001");
		res.setMessage("error");
		res.setSuccess(false);
		ShotfishConfig config = getConfig();
		String[] params = { "s=6", "startTime=" + startTime, "endTime=" + endTime };
		String param = "";
		param = String.join("&", params);
		param = encrypt(param, config.secretKey);
		try {
			param = URLEncoder.encode(param, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			logger.error("[HISTORY FISH] Error encrypt param: " + e1.getMessage());
			e1.printStackTrace();
		}

		Long timeStamp = getCurrentTimeStamp();
		String pKey = getMd5(config.agentId + String.valueOf(timeStamp) + config.secretKey);
		String url = config.urlApi + "agent=" + config.agentId + "&timestamp=" + timeStamp + "&param=" + param + "&key="
				+ pKey;
		try {
			String data = getRequest(url);
			JSONObject jsonObj = new JSONObject(data);
			int code = jsonObj.getJSONObject("d").getInt("code");
			if (code != 0) {
				logger.error("[HISTORY FISH] Error response data: " + data);
				return res;
			}

			res.setData(jsonObj.getJSONObject("d").getString("list"));
			res.setTotalRecords(jsonObj.getJSONObject("d").getLong("count"));
			res.setErrorCode("0");
			res.setMessage("success");
			res.setSuccess(true);
			return res;
		} catch (Exception e) {
			logger.error("[HISTORY FISH] Exception: " + e.getMessage());
			return res;
		}
	}

	private static List<FishGameRecord> convertJsontoListObject(String jsonString) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<FishGameRecord> result = mapper.readValue(jsonString, new TypeReference<List<FishGameRecord>>() {
			});
			return result;
		} catch (JsonParseException e) {
			e.printStackTrace();
			logger.error("[CONVERTJSONTOLIST FISH] Error response data: " + jsonString);
			return null;
		} catch (JsonMappingException e) {
			e.printStackTrace();
			logger.error("[CONVERTJSONTOLIST FISH] Error response data: " + jsonString);
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("[CONVERTJSONTOLIST FISH] Error response data: " + jsonString);
			return null;
		}
	}

	private static FishGameRecord convertJsontoObject(String jsonString) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			FishGameRecord result = mapper.readValue(jsonString, new TypeReference<FishGameRecord>() {
			});
			return result;
		} catch (JsonParseException e) {
			e.printStackTrace();
			logger.error("[CONVERTJSONTOLIST FISH] Error response data: " + jsonString);
			return null;
		} catch (JsonMappingException e) {
			e.printStackTrace();
			logger.error("[CONVERTJSONTOLIST FISH] Error response data: " + jsonString);
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("[CONVERTJSONTOLIST FISH] Error response data: " + jsonString);
			return null;
		}
	}

	/**
	 * Synchronize bet log
	 * 
	 * @param startTime (Time stamp in milliseconds)
	 * @param endTime   (Time stamp in milliseconds)
	 * @return BaseResponse<Object>
	 */
	public static BaseResponse<Object> synchronizeHistory(String startTime, String endTime) {
		BaseResponse<Object> res = new BaseResponse<Object>();
		res.setData(null);
		res.setTotalRecords(0);
		res.setErrorCode("1001");
		res.setMessage("error");
		res.setSuccess(false);
		ShotfishConfig config = getConfig();
		String[] params = { "s=6", "startTime=" + startTime, "endTime=" + endTime };
		String param = "";
		param = String.join("&", params);
		param = encrypt(param, config.secretKey);
		try {
			param = URLEncoder.encode(param, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			logger.error("[HISTORY FISH] Error encrypt param: " + e1.getMessage());
			e1.printStackTrace();
			return res;
		}

		Long timeStamp = getCurrentTimeStamp();
		String pKey = getMd5(config.agentId + String.valueOf(timeStamp) + config.secretKey);
		String url = config.urlApi + "agent=" + config.agentId + "&timestamp=" + timeStamp + "&param=" + param + "&key="
				+ pKey;
		try {
			String data = getRequest(url);
			JSONObject jsonObj = new JSONObject(data);
			int code = jsonObj.getJSONObject("d").getInt("code");
			if (code != 0) {
				logger.error("[HISTORY FISH] Error response data: " + data);
				return res;
			}

			String json = "";
			json = jsonObj.getJSONObject("d").getString("list");
			List<FishGameRecord> fishGameRecords = new ArrayList<>();
			fishGameRecords = convertJsontoListObject(json);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			int countSuccess = 0;
			if (fishGameRecords != null) {
				LogFishDao logFishDao = new LogFishDaoImpl();
				for (FishGameRecord fishGameRecord : fishGameRecords) {
					//TODO: Chuyen mui gio
					Date endTimeChange = simpleDateFormat.parse(fishGameRecord.getEndtime());
					endTimeChange = DateUtils.addHours(new Date(), -1);
					fishGameRecord.setEndtime(simpleDateFormat.format(endTimeChange));
					// TODO: If exist -> insert into queue
					if (logFishDao.findItem(fishGameRecord.getId(), fishGameRecord.getRoomid(), fishGameRecord.getGid(),
							fishGameRecord.getMuid(), fishGameRecord.getEndtime()) == null) {
						logFishDao.insert(fishGameRecord);
						countSuccess += 1;
						
						LogMoneyUserMessage message = new LogMoneyUserMessage(0,
								fishGameRecord.getMuid().replace(config.prefix, ""), "FISH",
								Games.SHOT_FISH.getId() + "", 0, - fishGameRecord.getBetcoin().longValue(), "vin", "",
								0, false, false);
						RMQPublishTask taskReportUser = new RMQPublishTask(message, "queue_log_report_user_balance",
								602);
						taskReportUser.start();

						if (fishGameRecord.getCoin().longValue() > 0) {
							LogMoneyUserMessage message2 = new LogMoneyUserMessage(0,
									fishGameRecord.getMuid().replace(config.prefix, ""), "FISH",
									Games.SHOT_FISH.getId() + "", 0, Math.abs(fishGameRecord.getCoin().longValue()),
									"vin", "", 0, false, false);
							RMQPublishTask taskReportUser2 = new RMQPublishTask(message2, "queue_log_report_user_balance",
									602);
							taskReportUser2.start();
						}
					} else {
						logFishDao.update(fishGameRecord);
						countSuccess += 1;
					}
				}
			}

			int totalRecord = 0;
			totalRecord = jsonObj.getJSONObject("d").getInt("count");
			Map<String, Object> map = new HashMap<>();
			map.put("countSuccess", countSuccess);
			map.put("countError", totalRecord - countSuccess);
			res.setData(map);
			res.setTotalRecords(totalRecord);
			res.setErrorCode("0");
			res.setMessage("success");
			res.setSuccess(true);
			return res;
		} catch (Exception e) {
			logger.error("[HISTORY FISH] Exception: " + e.getMessage());
			return res;
		}
	}

	/**
	 * Synchronize bet log automatic each 1 minutes
	 * 
	 * @return BaseResponse<Object>
	 */
	public static BaseResponse<Object> synchronizeHistory() {
		LogFishDao logFishDao = new LogFishDaoImpl();
		String startTime = String.valueOf(logFishDao.getLastUpdateTime());
		String endTime = String.valueOf(DateUtils.addHours(new Date(), 1).getTime());
		return synchronizeHistory(startTime, endTime);
	}
}
