package com.vinplay.usercore.service.impl;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SplittableRandom;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.usercore.dao.AttendanceConfigDao;
import com.vinplay.usercore.dao.UserAttendanceDao;
import com.vinplay.usercore.dao.impl.AttendanceConfigDaoImpl;
import com.vinplay.usercore.dao.impl.UserAttendanceDaoImpl;
import com.vinplay.usercore.entities.AttendanceConfig;
import com.vinplay.usercore.entities.UserAttendance;
import com.vinplay.usercore.service.AttendanceService;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.utils.CacheConfigName;
import com.vinplay.vbee.common.enums.Games;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.messages.LogMoneyUserMessage;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.response.BaseResponseModel;
import com.vinplay.vbee.common.rmq.RMQPublishTask;
import com.vinplay.vbee.common.statics.Consts;

public class AttendanceServiceImpl implements AttendanceService {
	private static final Logger logger = Logger.getLogger("user_core");
	private UserAttendanceDao dao = new UserAttendanceDaoImpl();
	private AttendanceConfigDao daoConf = new AttendanceConfigDaoImpl();
	private String ValidateConfig(AttendanceConfig attendanceConfig) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			if (StringUtils.isBlank(attendanceConfig.getStart_date()))
				return "Ngày bắt đầu của chu kỳ không được để trắng";

			if (StringUtils.isBlank(attendanceConfig.getEnd_date()))
				return "Ngày kết thúc của chu kỳ không được để trắng";

			if (df.parse(attendanceConfig.getEnd_date()).getTime() < df.parse(attendanceConfig.getStart_date())
					.getTime())
				return "Ngày bắt đầu phải nhỏ hơn ngày kết thúc";

			if (attendanceConfig.getMoney() < 1)
				return "Số tiền phải lớn hơn 0";

			AttendanceConfig configLastest = getConfigLastestFromCached();
			configLastest = configLastest == null ? getConfigLastest() : configLastest;
			if (configLastest != null) {
				if (df.parse(configLastest.getEnd_date()).getTime() > df.parse(attendanceConfig.getStart_date())
						.getTime())
					return "Ngày bắt đầu phải nhỏ hơn ngày kết thúc của chu kỳ trước";
			}

			return "success";
		} catch (Exception e) {
			logger.error("Error ValidateConfig: " + e.getMessage());
			return e.getMessage();
		}
	}

	@Override
	public boolean createConfig(long money) {
		try {
			AttendanceConfig attendanceConfig = new AttendanceConfig();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date start = DateUtils.truncate(new Date(), java.util.Calendar.DAY_OF_MONTH);
			Date end = DateUtils.truncate(new Date(), java.util.Calendar.DAY_OF_MONTH);
			end.setTime(end.getTime() + (6 * 1000 * 60 * 60 * 24));
			attendanceConfig.setStart_date(df.format(start) + " 00:00:00");
			attendanceConfig.setEnd_date(df.format(end) + " 23:59:59");
			attendanceConfig.setMoney(money);
			attendanceConfig.setCreate_at(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
			return createConfig(attendanceConfig).equalsIgnoreCase("success") ? true : false;
		} catch (Exception e) {
			logger.error("Error createConfig: " + e.getMessage());
			return false;
		}
	}

	@Override
	public String createConfig(AttendanceConfig attendanceConfig) {
		try {
			String result = "";
			result = ValidateConfig(attendanceConfig);
			if (!"success".equals(result))
				return result;
			
//			AttendanceConfig attendanceConfigLasttest = new AttendanceConfig();
//			attendanceConfigLasttest = dao.getLastest();
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//			Date start = df.parse(attendanceConfigLasttest.getStart_date());
//			Date end = df.parse(attendanceConfigLasttest.getEnd_date());
//			Date startCurrent = df.parse(attendanceConfig.getStart_date());
//			if(startCurrent.before(start) && end.after(start)) {
//				//TODO: Set cached
//				setAttendanceConfigToCached();
//				return "success";
//			}
			
			String rs = daoConf.insert(attendanceConfig);
			//TODO: Set cached
			setAttendanceConfigToCached();
			return rs;
		} catch (Exception e) {
			logger.error("Error createConfig: " + e.getMessage());
			return e.getMessage();
		}
	}

	@Override
	public AttendanceConfig getConfigLastest() {
		try {
			return daoConf.getLastest();
		} catch (Exception e) {
			logger.error("GETCONFIG Attendance lastest from DB: " + e.getMessage());
			return null;
		}
	}
	
	private boolean setAttendanceConfigToCached() {
		HazelcastInstance instance = HazelcastClientFactory.getInstance();
		if (instance == null) {
			logger.error("[SETCACHECONFIG Attendance] Can't connect cached server");
			return false;
		}

		IMap<String, String> configCache = instance.getMap(Consts.CACHE_CONFIG);
		if (configCache.containsKey(CacheConfigName.ATTENDANCE_CONFIG)) {
			try {
				configCache.lock(CacheConfigName.ATTENDANCE_CONFIG);
				AttendanceConfig attendanceConfig = getConfigLastest();
				configCache.put(CacheConfigName.ATTENDANCE_CONFIG, attendanceConfig == null ? "" : attendanceConfig.toJson());
				return true;
			} catch (Exception e) {
				logger.error("[SETCACHECONFIG Attendance] Exception: " + e.getMessage());
				return false;
			}
			finally {
				if(configCache.isLocked(CacheConfigName.ATTENDANCE_CONFIG)) {
					try{
						configCache.unlock(CacheConfigName.ATTENDANCE_CONFIG);
					}catch (Exception e) { 
						logger.error("[SETCACHECONFIG Attendance]: " + e.getMessage());
					}
				}
			}
		} else {
			logger.error("[SETCACHECONFIG Attendance] Can't found key " + CacheConfigName.ATTENDANCE_CONFIG + " into cached server");
			return false;
		}
	}
	
	@Override
	public AttendanceConfig getConfigLastestFromCached() {
		try {
			HazelcastInstance instance = HazelcastClientFactory.getInstance();
			IMap<String, String> configCache = instance.getMap(Consts.CACHE_CONFIG);
			String value = configCache.get(CacheConfigName.ATTENDANCE_CONFIG).toString();
			Type type = new TypeToken<AttendanceConfig>() {
			}.getType();
			AttendanceConfig attendanceConfig = new Gson().fromJson(value, type);
			if(attendanceConfig == null)
				attendanceConfig = getConfigLastest();
			
			return attendanceConfig;
		} catch (Exception e) {
			logger.error("[GETCONFIG Attendance lastest from cache server] Exception: " + e.getMessage());
			AttendanceConfig attendanceConfig = new AttendanceConfig();
			attendanceConfig = getConfigLastest();
			return attendanceConfig;
		}
	}

	public BaseResponseModel addMoneyDiemDanh(UserAttendance userAttendance, long money) {
		UserService userService = new UserServiceImpl();
		BaseResponseModel baseResponseModel = userService.updateMoneyFromAdmin(userAttendance.getNick_name(), money,
				"vin", Games.DIEM_DANH.getName(), Games.DIEM_DANH.getId() + "", userAttendance.toJson());
		return baseResponseModel;
	}

	@Override
	public Map<String, Object> Attendance(String nickname,String ip) {
		Map<String, Object> result = new HashMap<>();
		HazelcastInstance client = HazelcastClientFactory.getInstance();
		if (client == null) {
			result.put("code", "failed");
			result.put("msg", "Lỗi hệ thống. Vui lòng liên hệ bộ phận CSKH để được giúp đỡ.");
			return result;
		}
		
		IMap<String, UserModel> userMap = client.getMap("users");
		try {
			AttendanceConfig config = getConfigLastestFromCached();
			config = config == null ? getConfigLastest() : config;
			if(config.getId() == 0)
				config = getConfigLastest();
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			UserAttendance userAttendance = new UserAttendance();
			UserAttendance userAttendanceLastest = getAttendLastest(nickname);
			Date today = DateUtils.truncate(new Date(), java.util.Calendar.DAY_OF_MONTH);
			// Date today = df.parse(df.format(new Date()));
			if (today.getTime() < df.parse(config.getStart_date()).getTime()
					|| today.getTime() > df.parse(config.getEnd_date()).getTime()) {
				result.put("code", "invalid");
				result.put("msg", "Ngày điểm danh không nằm trong chu kỳ điểm danh");
				return result;
			}
			
			if (userMap.containsKey(nickname)) {
				try {
					if(userMap.isLocked(nickname)) {
						result.put("code", "failed");
						result.put("msg", "Bạn chỉ được quay nhận thưởng một lần trong ngày.");
						return result;
					}
						
					userMap.lock(nickname);
				}catch (Exception e) { }
			}
			
			if (userAttendanceLastest == null) {
				userAttendance.setAttend_id(config.getId());
				userAttendance.setConsecutive(1);
				userAttendance.setNick_name(nickname);
				userAttendance.setDate_attend(df.format(new Date()));
				short[] diceResult = getDice(nickname,ip);
				long[] moneys = CalculatorMoney(diceResult, config, userAttendance.getConsecutive());
				userAttendance.setBonus_basic(moneys[0]);
				userAttendance.setBonus_consecutive(moneys[1]);
				userAttendance.setBonus_vip(moneys[2]);
				userAttendance.setSpin(StringUtils.join(ArrayUtils.toObject(diceResult), "-"));
				userAttendance.setResult(userAttendance.getSpin());
				userAttendance.setIp(ip);
			} else {
				Date lastDate = DateUtils.truncate(df.parse(userAttendanceLastest.getDate_attend()),
						java.util.Calendar.DAY_OF_MONTH);
				if (DateUtils.isSameDay(lastDate, today)) {
					result.put("code", "exist");
					result.put("msg", "Bạn đã nhận quà điểm danh vào lúc: " + userAttendanceLastest.getDate_attend());
					return result;
				}

				if (DateUtils.isSameDay(lastDate, DateUtils.addDays(today, -1)))
					if(userAttendanceLastest.getAttend_id() == config.getId()) {
						userAttendance.setConsecutive(userAttendanceLastest.getConsecutive() + 1);
					}else {
						userAttendance.setConsecutive(1);
					}
				else
					userAttendance.setConsecutive(1);

				userAttendance.setNick_name(nickname);
				userAttendance.setDate_attend(df.format(new Date()));
				short[] diceResult = getDice(nickname,ip);
				long[] moneys = CalculatorMoney(diceResult, config, userAttendance.getConsecutive());
				userAttendance.setBonus_basic(moneys[0]);
				userAttendance.setBonus_consecutive(moneys[1]);
				userAttendance.setBonus_vip(moneys[2]);
				userAttendance.setSpin(StringUtils.join(ArrayUtils.toObject(diceResult), "-"));
				userAttendance.setResult(userAttendance.getSpin());
				userAttendance.setAttend_id(config.getId());
				userAttendance.setIp(ip);
			}

			String rs = dao.insert(userAttendance);
			// TODO: Update money for player
			if ("success".equals(rs)) {
				long money = userAttendance.getBonus_basic() + userAttendance.getBonus_consecutive() + userAttendance.getBonus_vip();
				BaseResponseModel baseResponseModel = addMoneyDiemDanh(userAttendance, money);
				if (baseResponseModel.isSuccess()) {
					LogMoneyUserMessage message = new LogMoneyUserMessage(0, userAttendance.getNick_name(), "DIEM_DANH",
							Games.DIEM_DANH.getId() + "", 0, money, "vin", "", 0, false, false);
					RMQPublishTask taskReportUser = new RMQPublishTask(message, "queue_log_report_user_balance", 602);
					taskReportUser.start();
					result.put("code", "success");
					result.put("msg", userAttendance.toJson());
					return result;
				} else {
					// TODO: Delete if failed
					userAttendance = getAttendLastest(nickname);
					dao.delete(userAttendance.getAttend_id());
					result.put("code", "failed");
					result.put("msg", "Lỗi hệ thống. Vui lòng liên hệ bộ phận CSKH để được giúp đỡ.");
					return result;
				}
			}

			result.put("code", "failed");
			result.put("msg", "Lỗi hệ thống. Vui lòng liên hệ bộ phận CSKH để được giúp đỡ.");
			return result;
		} catch (Exception e) {
			logger.error("Error insertUserAttend: " + e.getMessage());
			result.put("code", "exception");
			result.put("msg", "Lỗi hệ thống. Vui lòng liên hệ bộ phận CSKH để được giúp đỡ.");
			return result;
		}
		finally {
			if(userMap.isLocked(nickname)) {
				try{
					userMap.unlock(nickname);
				}catch (Exception e) { 
					logger.error("Error insertUserAttend: " + e.getMessage());
				}
			}
		}
	}
	
	@Override
	public Map<String, Object> CheckAttendance(String nickname,String ip) {
		Map<String, Object> result = new HashMap<>();
		try {
			AttendanceConfig config = getConfigLastestFromCached();
			config = config == null ? getConfigLastest() : config;
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			UserAttendance userAttendanceLastest = getAttendLastest(nickname);
			Date today = DateUtils.truncate(new Date(), java.util.Calendar.DAY_OF_MONTH);
			// Date today = df.parse(df.format(new Date()));
			
			if (today.getTime() < df.parse(config.getStart_date()).getTime()
					|| today.getTime() > df.parse(config.getEnd_date()).getTime()) {
				result.put("code", "invalid");
				result.put("consecutive", -1);
				result.put("msg", "Ngày điểm danh không nằm trong chu kỳ điểm danh");
				return result;
			}

			if (userAttendanceLastest == null) {
				result.put("code", "success");
				result.put("consecutive", 0);
				result.put("msg", "Bạn chưa điểm danh ngày hôm nay");
				return result;
			} else {
				Date lastDate = DateUtils.truncate(df.parse(userAttendanceLastest.getDate_attend()),
						java.util.Calendar.DAY_OF_MONTH);
				if (DateUtils.isSameDay(lastDate, today)) {
					result.put("code", "exist");
					result.put("consecutive", userAttendanceLastest.getConsecutive());
					result.put("msg", "Bạn đã nhận quà điểm danh vào lúc: " + userAttendanceLastest.getDate_attend());
					return result;
				}

				result.put("code", "success");
				result.put("consecutive", userAttendanceLastest.getConsecutive());
				result.put("msg", "Bạn chưa điểm danh ngày hôm nay");
				return result;
			}
			
		} catch (Exception e) {
			logger.error("Error insertUserAttend: " + e.getMessage());
			result.put("code", "exception");
			result.put("consecutive", -1);
			result.put("msg", "Lỗi hệ thống. Vui lòng liên hệ bộ phận CSKH để được giúp đỡ.");
			return result;
		}
	}

	private static final SplittableRandom rdom = new SplittableRandom();

	private static short[] genarateRandomNum(int totalNum) {

		// int totalNum = rdom.nextInt(6 - 3 + 1) + 3;
		int t1 = totalNum / 3;
		int t2 = 0;
		if (totalNum - t1 <= 6) {
			t2 = rdom.nextInt(totalNum - t1 - 1 - 1 + 1) + 1;
		} else {
			t2 = rdom.nextInt(6 - 1 - 1 + 1) + 1;
		}
		int t3 = totalNum - t1 - t2;
		return new short[] { (short) t1, (short) t2, (short) t3 };
	}

	private static final short[] SO_BIG_VALUE = { 4, 5, 7, 8, 10, 13, 14, 16, 17, 11, 3, 6, 9, 12, 15, 18 };
	private static final short[] BIG_VALUE = { 4, 5, 7, 8, 10, 13, 14, 16, 17, 11, 3, 6, 9, 12, 5, 7, 8, 10, 13 };
	private static final short[] BIG_BIG_VALUE = { 4, 5, 7, 8, 10, 13, 14, 16, 17, 11, 15, 18, 3, 6, 9, 12, 5, 7, 8, 10, 13 };
	private static final short[] MIN_VALUE = { 4, 5, 7, 8, 10, 4, 5, 7 ,8};
	private static final short[] MEDIUM_VALUE = { 4, 5, 7, 8, 10, 13, 14, 16, 17, 11, 8, 10, 13 };// 11

	private short[] getDice(String nickName,String ip) {
		// check ip address
		if (checkIp(nickName, ip)) {
			return new short[] { 0, 0, 0 };
		}
		UserService userService = new UserServiceImpl();
		long value = userService.getUserValue(nickName);// dp -wd
		if (value <= 10000000) {
			// user winin
			int index = rdom.nextInt(MIN_VALUE.length);
			return genarateRandomNum(MIN_VALUE[index]);
		} else if (value <= 100000000) {
			// user lose <100m
			int index = rdom.nextInt(MEDIUM_VALUE.length);
			return genarateRandomNum(MEDIUM_VALUE[index]);
		}else if (value <= 500000000) {
			// user lose <500m
			int index = rdom.nextInt(BIG_VALUE.length);
			return genarateRandomNum(BIG_VALUE[index]);
		}else if (value <= 1000000000) {
				// user lose <1000m
				int index = rdom.nextInt(BIG_BIG_VALUE.length);
				return genarateRandomNum(BIG_BIG_VALUE[index]);
		} else {
			// user lose >1000m
			int index = rdom.nextInt(SO_BIG_VALUE.length);
			return genarateRandomNum(SO_BIG_VALUE[index]);
		}
	}

	public static void main(String[] args) {
		int index = rdom.nextInt(MIN_VALUE.length);
		System.out.println(Arrays.toString(genarateRandomNum(MIN_VALUE[index])));
	}

	
	@Override
	public UserAttendance getAttendLastest(String nickname) {
		try {
			return dao.getLastest(nickname);
		} catch (Exception e) {
			logger.error("Error getUserAttendLastest: " + e.getMessage());
			return null;
		}
	}

	@Override
	public Map<String, Object> search(Integer attendId, String nickname, String fromTime, String endTime, int pageIndex,
			int limit) {
		try {
			return dao.search4BO(attendId, nickname, fromTime, endTime, pageIndex, limit);
		} catch (Exception e) {
			logger.error("Error getUserAttendLastest: " + e.getMessage());
			Map<String, Object> data = new HashMap<>();
			data.put("userAttendances", new ArrayList<UserAttendance>());
			data.put("totalRecord", 0);
			data.put("totalMoney", 0);
			data.put("totalPlayer", 0);
			return data;
		}
	}
	
	public List<Map<String, Object>> historyInRound(String nickname) {
		try {
			List<Map<String, Object>> rs = new ArrayList<Map<String,Object>>();
			java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
			AttendanceConfig attendanceConfig =  getConfigLastestFromCached();
			Date createDate = format.parse(attendanceConfig.getCreate_at());
			Date endDate = format.parse(attendanceConfig.getEnd_date());
			int offset = 1;
			while (createDate.getTime() <= endDate.getTime()) {
				UserAttendance userAttendance = new UserAttendance();
				userAttendance = dao.getDetail(nickname, attendanceConfig.getId(), format.format(createDate));
				Map<String, Object> detail = new HashMap<String, Object>();
				detail.put("date", format.format(createDate));
				if(userAttendance == null) {
					detail.put("ratioBonus", 0);
					detail.put("consecutive", 0);
				}else {
					detail.put("ratioBonus", (userAttendance.getConsecutive() - 1)*10);
					detail.put("consecutive", userAttendance.getConsecutive());
				}
				
				detail.put("offsetDay", String.valueOf(offset));
				offset++;
				createDate.setTime(createDate.getTime() + (1 * 1000 * 60 * 60 * 24));
				rs.add(detail);
			}
			
			return rs;
		} catch (Exception e) {
			logger.error("Error getUserAttendLastest: " + e.getMessage());
			return null;
		}
	}

	private long[] CalculatorMoney(short[] diceResult, AttendanceConfig attendanceConfig, int consecutive) {
		int index = 0;
		for (int i = 1; i < 7; i++) {
			short[] diceSpec = new short[] { (short) i, (short) i, (short) i };
			if (Arrays.equals(diceResult, diceSpec)) {
				index = i;
				break;
			}
		}

		switch (index) {
		case 1:
			return new long[] { 40000, 0, 0 };
		case 2:
			return new long[] { 70000, 0, 0 };
		case 3:
			return new long[] { 100000, 0, 0 };
		case 4:
			return new long[] { 130000, 0, 0 };
		case 5:
			return new long[] { 160000, 0, 0 };
		case 6:
			return new long[] { 200000, 0, 0 };
		default:
			long bonus_basic, bonus_consecutive = 0;
			int totalDice = 0;
			for (short s : diceResult) {
				totalDice += Integer.valueOf(s);
			}

			bonus_basic = attendanceConfig.getMoney() * totalDice;
			bonus_consecutive = (long) (bonus_basic * ((consecutive - 1) * 0.1));
			return new long[] { bonus_basic, bonus_consecutive, 0 };
		}
	}

	@Override
	public boolean checkIp(String nickName, String ip) {
		// TODO Auto-generated method stub
		return daoConf.isCheckSameIP();
	}
}
