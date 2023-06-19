package com.vinplay.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.vinplay.dto.esport.EsportLoginBase64;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.utils.GlobalConstants;
import com.vinplay.utils.MD5Utils;
import com.vinplay.vbee.common.statics.Consts;
import com.google.gson.Gson;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class CommonMethod {
	
	
	public static String deftime = null;
	private static final Logger logger = Logger.getLogger(CommonMethod.class);
	public static final DecimalFormat DEC_FORMATTER = new DecimalFormat("#0.00");
	public static String CHARACTERS = "0ABghlC1DEnoFmuG5HijkIJKLpqt9MNO2QfvRS8Trs4UVwxWXyY36Zabcd7Pez";
	public static String PREFIXID = "elBeoTo";
	public static Map<String, Long> mapCache = new ConcurrentHashMap<String, Long>();

	public static boolean ValidateRequest(String loginname) {
		if (mapCache.isEmpty()) {
			long t1 = new java.util.Date().getTime();
			mapCache.put(loginname, t1);
		} else {
			if (mapCache.containsKey(loginname)) {

				long t1 = mapCache.get(loginname);
				long t2 =  new java.util.Date().getTime();
				if ((t2 - t1) > 1000 * 10) {
					mapCache.put(loginname, t2);
					return true;
				} else {
					return false;
				}

			} else {
				long t1 =  new java.util.Date().getTime();
				mapCache.put(loginname, t1);
			}
		}
		return true;
	}
//	public static boolean ValidateRequest(String loginname) {
//
//		if (loginname == null) {
//			return false;
//		}
//		int i = 0;
//		InitData.requestLoginnameList.add(new FrequencyRequestItem(loginname));
//		long now = new java.util.Date().getTime();
//		for (FrequencyRequestItem item : InitData.requestLoginnameList) {
//			if (loginname.equals(item.getLoginname())) {
//				if ((now - item.getTime()) < 1000 * 20) {
//					i++;
//				}
//			}
//		}
//		for (int j = (InitData.requestLoginnameList.size() - 1); j >= 0; j--) {
//			FrequencyRequestItem item = InitData.requestLoginnameList.get(j);
//			if ((now - item.getTime()) > 1000 * 20) {
//				InitData.requestLoginnameList.remove(item);
//			}
//		}
//
//		return i < 2;
//	}

	public static String inputStream2String(InputStream in) throws IOException {
		StringBuilder out = new StringBuilder();
		long startTime = System.nanoTime();
		long endTime;
		byte[] b = new byte[4096];
		try {
			for (int n; (n = in.read(b)) != -1;) {
				out.append(new String(b, 0, n));
			}
		} catch (IOException ex) {
			endTime = System.nanoTime();
			String message = String.format("Can't read all data from user within time frame: %s seconds", DEC_FORMATTER.format((endTime - startTime) / 1e9));
			logger.error(message, ex);
		}
		return out.toString();
	}
	
	public static String convInputStream2String(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		String str;
		BufferedReader br = null;
		long startTime = System.nanoTime();
		try {
			br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			while ((str = br.readLine()) != null) {
				out.append(str);
			}
		} catch (Exception ex) {
			long endTime = System.nanoTime();
			String message = String.format("Can't read all data from user within time frame: %s seconds",
					DEC_FORMATTER.format((endTime - startTime) / 1e9));
			logger.error(message, ex);
			return "";
		} finally {
			if (br != null)
				br.close();
			if (in != null)
				in.close();
		}
		return out.toString();
	}
	
	public static List<SelectItem> parseParam(String reqStr) throws Exception {
		if (reqStr == null || reqStr.length() < 1) {
			return null;
		}
		reqStr = reqStr.replaceAll("(\\r|\\n|\\{|\\}|\\s|\\\")", "");
		String[] args = reqStr.split(",");
		List<SelectItem> list = new ArrayList<>();
		for (int i = 0; i < args.length; i++) {
			String s = args[i];
			String[] a = s.split(":");
			if (a.length == 2) {
				a[1] = CommonMethod.Decode(a[1]);
				a[0] = CommonMethod.Decode(a[0]);
				list.add(new SelectItem(a[1], a[0]));
			}
			if (a.length == 1) {
				a[0] = CommonMethod.Decode(a[0]);
				list.add(new SelectItem("", a[0]));
			}
			// key:a[0] value:a[1]
		}
		return list;
	}
	
	public static String Encode(String str) throws Exception {
		if (CommonMethod.isEmpty(str)) {
			return "";
		} else {
			return java.net.URLEncoder.encode(str, "utf8");
		}
	}

	public static String Decode(String str) throws Exception {
		if (CommonMethod.isEmpty(str)) {
			return "";
		} else {
			return java.net.URLDecoder.decode(str, "utf8");
		}
	}
	
	public static boolean isEmpty(String str) {
		return str == null || "".equals(str.trim());
	}

	/**
	 * Object转换成JSON字符串 cf数组是不转的属性
	 */
	public static String beanToJson(Object bean, String[] cf) {
		JSONObject jsonObject;
		if (cf != null && cf.length > 0) {
			JsonConfig config = new JsonConfig();
			config.setExcludes(cf);
			jsonObject = JSONObject.fromObject(bean, config);
		} else {
			jsonObject = JSONObject.fromObject(bean);
		}
		return jsonObject.toString();
	}

	/**
	 * Object转换成JSON数据 cf数组是不转的属性
	 */
	public static JSONObject beanToJSON(Object bean, String[] cf) {
		JSONObject jsonObject;

		if (cf != null && cf.length > 0) {
			JsonConfig config = new JsonConfig();
			config.setExcludes(cf);
			jsonObject = JSONObject.fromObject(bean, config);
		} else {
			jsonObject = JSONObject.fromObject(bean);
		}
		return jsonObject;
	}

	/**
	 * json字符串转换成JSON数据
	 */
	public static JSONObject objToJSON(String str) {
		JSONObject jsonObject = JSONObject.fromObject(str);
		return jsonObject;
	}

	/**
	 * 输出json数据
	 *
	 * @throws IOException
	 */
	public static void printData(HttpServletResponse response, String jsonStr) throws IOException {
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(jsonStr);
		out.flush();
		out.close();
	}
	/**
	 * 计算时间差，时间间隔（输出时间格式和输入时间格式�?���?
	 * 
	 * @param inTime
	 *            输入时间�?010-06-10 11:22:44�?
	 * @param inTimeType
	 *            输入时间格式(yyyy-MM-dd HH:mm:ss)
	 * @param space
	 *            间隔(-2)
	 * @param type
	 *            时间差时间间隔类�?dd)
	 * @return
	 * @throws Exception
	 */
	public static String getTimeSpace(String inTime, String inTimeType, int space, String type) throws Exception {
		return getTimeSpace(inTime, inTimeType, space, type, inTimeType);
	}

	/**
	 * 计算时间差，时间间隔
	 * 
	 * @param inTime
	 *            输入时间�?010-06-10 11:22:44�?
	 * @param inTimeType
	 *            输入时间格式(yyyy-MM-dd HH:mm:ss)
	 * @param space
	 *            间隔(-2)
	 * @param type
	 *            时间差时间间隔类�?dd)
	 * @param outTimeType
	 *            输出时间格式(MM-dd)
	 * @return (06-08)
	 * @throws Exception
	 */
	public static String getTimeSpace(String inTime, String inTimeType, int space, String type, String outTimeType) throws Exception {
		Date curDate = null;
		DateFormat format = new SimpleDateFormat(inTimeType);
		curDate = format.parse(inTime);
		Calendar CurCalendar = Calendar.getInstance();
		CurCalendar.setTime(curDate);
		if (type.toLowerCase().equals("yyyy"))
			CurCalendar.add(Calendar.YEAR, space);
		else if (type.equals("MM"))
			CurCalendar.add(Calendar.MONTH, space);
		else if (type.toLowerCase().equals("dd"))
			CurCalendar.add(Calendar.DATE, space);
		else if (type.toUpperCase().equals("HH"))
			CurCalendar.add(Calendar.HOUR, space);
		else if (type.equals("mm"))
			CurCalendar.add(Calendar.MINUTE, space);
		else if (type.toLowerCase().equals("mi"))
			CurCalendar.add(Calendar.MINUTE, space);
		else if (type.toLowerCase().equals("ss"))
			CurCalendar.add(Calendar.SECOND, space);
		else
			// 默认�?�?
			CurCalendar.add(Calendar.DATE, space);
		CurCalendar.getTime();

		Format formatter = new SimpleDateFormat(outTimeType);
		return formatter.format(CurCalendar.getTime());
	}

	/**
	 * get current date
	 * 
	 * @param type
	 * @return
	 */
	public static String GetCurDate(String type) {
		try {
			String strdate = "";
			Date currentTime = new Date();
			SimpleDateFormat format0 = new SimpleDateFormat(type);
			strdate = format0.format(currentTime);
			return strdate;
		} catch (Exception ex) {
			logger.error("ex", ex);
			return null;
		}
	}
	
	/**
	 * get current date base on timezone
	 * 
	 * @param type
	 * @return
	 */
	public static String GetCurDate(String type, TimeZone zone) {
		try {
			String strdate = "";
			Date currentTime = new Date();
			SimpleDateFormat format0 = new SimpleDateFormat(type);
			format0.setTimeZone(zone);
			strdate = format0.format(currentTime);
			return strdate;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static String getRandomNubmer(int num) {
		char[] codeSequence = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		Random random = new Random();
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < num; i++) {
			String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
			str.append(strRand);
		}
		return str.toString();
	}
	
	/**
	 * random character
	 */
	public static String randomString(int len) {
		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		SecureRandom rnd = new SecureRandom();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}

	/**
	 * convert UTC time To HoChiMinh
	 * 
	 * @param num
	 * @return
	 */
	public static String convertUTCToHCM(String utcTime, String dateFormat) {
		try {
			DateFormat formatterUTC = new SimpleDateFormat(dateFormat);
			formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC")); // UTC timezone
			Date date = formatterUTC.parse(utcTime);

			DateFormat formatterIST = new SimpleDateFormat(dateFormat);
			formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh")); // better than using IST : plus 7 hours
			return formatterIST.format(date);
		} catch (ParseException e) {
			logger.error("ex", e);
			return null;
		}
	}
	
	public static String convertUTCToPH(String utcTime, String dateFormat) {
		try {
			DateFormat formatterUTC = new SimpleDateFormat(dateFormat);
			formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC")); // UTC timezone
			Date date = formatterUTC.parse(utcTime);

			DateFormat formatterIST = new SimpleDateFormat(dateFormat);
			formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Manila")); // better than using IST : plus 8 hours
			return formatterIST.format(date);
		} catch (ParseException e) {
			logger.error("ex", e);
			return null;
		}
	}
	public static String convertPHtoUTC(String phTime, String dateFormat) {
		try {
			DateFormat formatterIST = new SimpleDateFormat(dateFormat);
			formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Manila")); // UTC timezone
			Date date = formatterIST.parse(phTime);

			DateFormat formatterUTC = new SimpleDateFormat(dateFormat);
			formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC")); // better than using IST : plus 7 hours
			return formatterUTC.format(date);
		} catch (ParseException e) {
			logger.error("ex", e);
			return null;
		}
	}
	/**
	 * convert HoChiMinh time To UTC
	 * 
	 * @param num
	 * @return
	 */
	public static String convertHCMToUTC(String hcmTime, String dateFormat) {
		try {
			DateFormat formatterIST = new SimpleDateFormat(dateFormat);
			Calendar calendar = new GregorianCalendar();
			TimeZone timeZone = calendar.getTimeZone();
			formatterIST.setTimeZone(TimeZone.getTimeZone(timeZone.getID())); // UTC timezone
			Date date = formatterIST.parse(hcmTime);

			DateFormat formatterUTC = new SimpleDateFormat(dateFormat);
			formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC")); // better than using IST : plus 7 hours
			return formatterUTC.format(date);
		} catch (ParseException e) {
			logger.error("ex", e);
			return null;
		}
	}
	
	public static String convertHCMToPH() {
		String dtEndUTC = CommonMethod.convertHCMToUTC(CommonMethod.GetCurDate("yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
		return CommonMethod.convertUTCToPH(dtEndUTC, "yyyy-MM-dd HH:mm:ss");
	}

	public static boolean putToSession(String str, Object obj) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(str, obj);
		return true;
	}

	public static boolean putToSession(HttpServletRequest request, String key, Object obj) {
		request.getSession(true).setAttribute(key, obj);
		return true;
	}

	public static boolean existSession(String key) {
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey(key);
	}

	public static void sessionRemove(String key) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(key);
	}

	/**
	 * 从session中获�?
	 * 
	 * @param key
	 * @return
	 */
	public static Object getFromSession(String key) {
		if (FacesContext.getCurrentInstance() == null) {
			return null;
		}
		/**
		 * if(FacesContext.getCurrentInstance().getExternalContext()==null){ return
		 * null; }
		 */
		if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey(key)) {
			return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(key);
		} else {
			return null;
		}
	}

	public static Object getSession(String key) {
		return getFromSession(key);
	}

	public static boolean SessionExist(String key) {
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey(key);
	}

	/**
	 * 获得IP地址
	 * 
	 * @return
	 */
	public static String getIpAddress() {
		String ipAddress = "";
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			ipAddress = ip.getHostAddress();
		} catch (UnknownHostException e) {
			logger.error("ex", e);
		}
		return ipAddress;
	}

	// private Key encrypt
	public static String sign(byte[] data, String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
		//byte[] keyBytes = Base64.getDecoder().decode(key);
		byte[] keyBytes = Base64.getMimeDecoder().decode(privateKey.getBytes());
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey priKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		Signature signature = Signature.getInstance("MD5withRSA");
		signature.initSign(priKey);
		signature.update(data);
		return new String(Base64.getEncoder().encode(signature.sign()));
	}

	public static void main(String[] args) {
		byte[] data= {49};
		String privateKey="MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEAsT31yytJMkQL6cc9aGCOQCRR3WyprztlDN3/d9ddC6mbM5keLK4/034mOJNkpP48vz9mYDfx3Ym31PYFgQOTlwIDAQABAkBfw8yUc9TetemB7Mb/KHxzp6wb6WRda8gThbdMty5s59M5jSo5T/LaHqZnjv9rahu+TWx1sLSsN1mOuHa6gWwxAiEA/p6iauSvhwl4J8/vWFvvhIDwsSjqIMctAeUQTZBLvDMCIQCyM/COiXcJHAZsfC3z48Y2gzYmkWtw5ex3MrpCF0PnDQIhAIofxDv4kr/Og0AVrOFh/i0DRY7Vgy0E34WHnbB19p/BAiEAl1frRnLS6Kangf0Y3dglX+ih1bGNKQ3sfPNVIoo1vgUCIQCcKnGzMDVPHTzWzEbNi7vsL8AZDjCfV22URhDBTtazgg==";
		try {
			byte [] res = privateKey.getBytes();
			sign(data, privateKey);
		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Instant instant = Instant.now(); // get The current time in instant object
//	    Timestamp t=java.sql.Timestamp.from(instant); // Convert instant to Timestamp
//	    Instant anotherInstant=t.toInstant();         // Convert Timestamp to Instant
//	    
//	    System.out.println(t);
//		System.out.println(anotherInstant);
	}
	// public Key verify
	public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
		byte[] keyBytes = Base64.getMimeDecoder().decode(publicKey.getBytes());
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey2 = keyFactory.generatePublic(x509EncodedKeySpec);
		Signature signature = Signature.getInstance("MD5withRSA");
		signature.initVerify(publicKey2);
		signature.update(data);
		return signature.verify(Base64.getDecoder().decode(sign));
	}
	// get ebet id
	public static String getNumber(String str, String prefix) {
		str = str.replaceAll("[`~!@#$%^&*()_+-[\\\\]\\\\\\\\;\\',./{}|:\\\"<>?\\s]", "");// removed any special character
		prefix = prefix.toLowerCase();
		String regex = prefix + "[0-9]+"; // get all code by folow: elbeoto2433
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str.toLowerCase());
		String id = "0";
		while (matcher.find()) {
			id = str.toLowerCase().substring(matcher.start(), matcher.end());
		}
		return id.replace(prefix, "");
	}

	// Encoding
	public static String encoding(long timeStamp, String ebetId, String password) {
		String keySource = timeStamp + CHARACTERS + password + PREFIXID + ebetId;
		String accessToken = new String(Base64.getEncoder().encode(keySource.getBytes()));
		return accessToken;
	}

	// Encoding
	public static String decoding(String encodedString) {
		String decodedString = new String(Base64.getDecoder().decode(encodedString.getBytes()));
		return decodedString;
	}
	// Convert array to string
	public static String convertArrayToString(Integer[] arr){

		String str = Arrays.toString(arr).replaceAll("\\s+", "");   // [1,2,3,4,5]

		String strArray[] = str.substring(1, str.length() - 1).split(",");

		return Arrays.toString(strArray);
	}

	/**
	 * date conversion string
	 * 
	 * @param date
	 * @param type
	 * @return
	 */
	public static String dateToStr(Date date, String type) {
		try {
			Format formatter = new SimpleDateFormat(type);
			return formatter.format(date);
		} catch (Exception ex) {
			logger.error("ex", ex);
			return "";
		}
	}
	public static Calendar strToDate(String strDate, String type) {
		SimpleDateFormat sdf = new SimpleDateFormat(type);
		Calendar cal = null;
		try { 
			Date date = sdf.parse(strDate);
			cal = Calendar.getInstance();
			cal.setTime(date);
		}catch (Exception e) 
		{ 
			logger.error("ex", e);
			return null;
		} 
		return cal;
	}
	
	/**
	 * convert System time To UTC
	 * 
	 * @param num
	 * @return
	 */
	public static String convertSystemTimeToUTC(String systemTime, String dateFormat) {
		try {
			DateFormat formatterIST = new SimpleDateFormat(dateFormat);
			Calendar calendar = new GregorianCalendar();
			TimeZone timeZone = calendar.getTimeZone();
			formatterIST.setTimeZone(TimeZone.getTimeZone(timeZone.getID())); // System timezone
			Date date = formatterIST.parse(systemTime);

			DateFormat formatterUTC = new SimpleDateFormat(dateFormat);
			formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC")); // UTC timezone
			return formatterUTC.format(date);
		} catch (ParseException e) {
			logger.error("ex", e);
			return null;
		}
	}
	/**
	 * convert UTC to Third-parties time
	 * 
	 * @param utcTime, dateFormatPatterns
	 * @return
	 */
	public static String convertUTCToThirdPartiesTime(String utcTime, String thirdPartiesTimezone, String dateFormat) {
		try {
			DateFormat formatterUTC = new SimpleDateFormat(dateFormat);
			formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC")); // UTC timezone
			Date date = formatterUTC.parse(utcTime);
			logger.info("UTC time: " + date);

			DateFormat formatterIST = new SimpleDateFormat(dateFormat);
			formatterIST.setTimeZone(TimeZone.getTimeZone(thirdPartiesTimezone)); // Third-parties timezone
			logger.info("3rd-parties time: " + formatterIST.format(date));
			return formatterIST.format(date);
		} catch (ParseException e) {
			logger.error("ex", e);
			return null;
		}
	}
	
	public static String convertFromThirdPartiesTimeZoneToUTC(String thirdPartyTime, String timezone,
			String dateFormat) {
		try {
			DateFormat formatterIST = new SimpleDateFormat(dateFormat);
			formatterIST.setTimeZone(TimeZone.getTimeZone(timezone)); // Third-parties timezone
			Date date = formatterIST.parse(thirdPartyTime);

			DateFormat formatterUTC = new SimpleDateFormat(dateFormat);
			formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC")); // UTC timezone
			return formatterUTC.format(date);
		} catch (ParseException ex) {
			logger.error("ex", ex);
			return "";
		}
	}
	
	public static String convertToCurrentSysTime(String curTime, String dateFormat) {
		try {
			DateFormat formatterUTC = new SimpleDateFormat(dateFormat);
			formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC")); // UTC timezone
			Date date = formatterUTC.parse(curTime);

			DateFormat formatterIST = new SimpleDateFormat(dateFormat);
			Calendar calendar = new GregorianCalendar();
			TimeZone timeZone = calendar.getTimeZone();
			formatterIST.setTimeZone(TimeZone.getTimeZone(timeZone.getID())); // System timezone
			return formatterIST.format(date);
		} catch (ParseException ex) {
			logger.error("ex", ex);
			return "";
		}
	}
	
	public static LocalDateTime convertToLocalDateTime(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date oldDate = sdf.parse(date);
			return oldDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		} catch (ParseException e) {
			logger.error("ex", e);
			return null;
		}
	}
	
	public static String convertTime(String fromTime, String inputTimeZone, String dateFormat) {
		try {
			DateFormat fromTimeFormat = new SimpleDateFormat(dateFormat);
			fromTimeFormat.setTimeZone(TimeZone.getTimeZone(inputTimeZone)); // UTC, GMT,... timezone
			Date date = fromTimeFormat.parse(fromTime);

			DateFormat expectTimeFormat = new SimpleDateFormat(dateFormat);
			expectTimeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
			return expectTimeFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static Optional<EsportLoginBase64> esportDecode(String token) {
		logger.info("esportLoginDecode token: " + token);
		byte[] actualByte = Base64.getDecoder().decode(token.getBytes());
		String tokenDecode = new String(actualByte);
		Gson gson = new Gson();
		EsportLoginBase64 esportLoginBase64 = gson.fromJson(tokenDecode, EsportLoginBase64.class);
		logger.info("esportLoginDecode decode success: " + tokenDecode);
		return Optional.ofNullable(esportLoginBase64);
	}

	public static Optional<String> esportEncode(String loginName, String esportUserName) {
		logger.info("esportLoginEncode encode loginName: " + loginName);
		String token = null;
		EsportLoginBase64 esports = new EsportLoginBase64();
		esports.setLoginName(loginName);
		esports.setTimeStamp(System.currentTimeMillis());
		String md5EsportUserName = MD5Utils.generateKey(esportUserName);
		String md5OurSecretKey = MD5Utils.generateKey(GameThirdPartyInit.ESPORT_SECRET_TOKEN);
		esports.setEsport_userName(md5EsportUserName);
		esports.setSecret_key(md5OurSecretKey);
		Gson gson = new Gson();
		String tokenJson = gson.toJson(esports);
		byte[] actualByte = Base64.getEncoder().encode(tokenJson.getBytes());
		token = new String(actualByte);
		logger.info("esportLoginEncode token is: " + token);
		return Optional.ofNullable(token);
	}

	public static String Instant2String(Instant date, String format) {
		if (format == null)
			format = "yyyy-MM-dd HH:mm:ss";
		try {
			DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(format)
					.withZone(ZoneId.systemDefault());
			return DATE_TIME_FORMATTER.format(date);
		} catch (Exception e) {
			logger.error(e);
			return "";
		}
	}
	
	
	public static boolean isValidIpAddress(String ipAddress) {
		if("dev".equals(GameThirdPartyInit.enviroment)) {
			return Consts.IP_OFFCIE.contains(ipAddress);
		}else {
			return Consts.IP_SERVER.contains(ipAddress);
		}
		
	}
	
	/**
	 * convert UTC to Third-parties time
	 * 
	 * @param utcTime, dateFormatPatterns
	 * @return
	 */
	public static String convertUTCToThirdPartiesTimeBBIN(String utcTime, String thirdPartiesTimezone, String dateFormat) {
		try {
			DateFormat formatterUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa");
			formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC")); // UTC timezone
			Date date = formatterUTC.parse(utcTime);

			DateFormat formatterIST = new SimpleDateFormat(dateFormat);
			formatterIST.setTimeZone(TimeZone.getTimeZone(thirdPartiesTimezone)); // Third-parties timezone
			return formatterIST.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean inSpace5Minutes(String endTime , long minuteLimit) throws ParseException {
		DateFormat fromTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date sdate = new Date();
		Date edate = fromTimeFormat.parse(endTime);
		long diff = edate.getTime() - sdate.getTime();
		long diffMinutes = diff / (60 * 1000) % 60;
		if (diffMinutes > 0 && diffMinutes < 4) {
			return false;
		} else {
			return true;
		}
	}
	
	public static Long convertTimeToMilli(String date, String fomatDate) {
		SimpleDateFormat f = new SimpleDateFormat(fomatDate);
		try {
			Date d = f.parse(date);
			return d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0L;
	}

	public static Date convertStringTodate(String type, String dateInString) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(type, Locale.ENGLISH);
			return formatter.parse(dateInString);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date GetCurDateByZone(String type, TimeZone zone) {
		try {
			String strdate = "";
			Date currentTime = new Date();
			SimpleDateFormat format0 = new SimpleDateFormat(type);
			format0.setTimeZone(zone);
			strdate = format0.format(currentTime);
			return format0.parse(strdate);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static String convertDateToString(String type, Date date) {
		DateFormat formatter = new SimpleDateFormat(type, Locale.ENGLISH);
		return formatter.format(date);
	}

	public static String convertPHtoHCM(String phTime, String dateFormat) {
		try {
			DateFormat formatterIST = new SimpleDateFormat(dateFormat);
			Calendar calendar = new GregorianCalendar();
			formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Manila")); // UTC timezone
			Date date = formatterIST.parse(phTime);

			DateFormat formatterUTC = new SimpleDateFormat(dateFormat);
			formatterUTC.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh")); // better than using IST : plus 7 hours
			return formatterUTC.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
