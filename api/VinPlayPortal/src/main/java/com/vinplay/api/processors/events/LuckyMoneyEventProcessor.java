package com.vinplay.api.processors.events;

import com.vinplay.payment.utils.Constant;
import com.vinplay.usercore.service.UserBonusService;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserBonusServiceImpl;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.enums.Games;
import com.vinplay.vbee.common.models.UserBonusModel;
import com.vinplay.vbee.common.response.BaseResponse;
import com.vinplay.vbee.common.response.MoneyResponse;
import com.vinplay.vbee.common.statics.TransType;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import java.text.ParseException;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SplittableRandom;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;

public class LuckyMoneyEventProcessor implements BaseProcessor<HttpServletRequest, String> {
	private Logger logger = Logger.getLogger((String) "api");

	private static final SplittableRandom rdom = new SplittableRandom();

	private long getAmount() {
		int r = rdom.nextInt(100 - 1 + 1) + 1;
		if (r == 1) {
			return rdom.nextBoolean() ? 40000 : 50000;
		} else if (r == 2 || r == 3) {
			return rdom.nextBoolean() ? 30000 : 40000;
		} else if (r == 4 || r == 5 || r == 6) {
			return rdom.nextBoolean() ? 30000 : 20000;
		} else if (r >= 7 && r <= 10) {
			return rdom.nextBoolean() ? 10000 : 20000;
		} else if (r > 10 && r <= 25) {
			return rdom.nextBoolean() ? 10000 : 5000;
		} else {
			return rdom.nextInt(5001);
		}
	}

	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = (HttpServletRequest) param.get();

		try {
			String ip = getIpAddress(request);
			String nickName = request.getParameter("nn");
			String accessToken = request.getParameter("at");
			String action = request.getParameter("ac");// get-receive-time
			if (nickName == null || nickName.trim().isEmpty())
				return BaseResponse.error(Constant.ERROR_PARAM, "Nickname không được để trắng");

			if (StringUtils.isBlank(accessToken))
				return BaseResponse.error(Constant.ERROR_PARAM, "Mã phiên làm việc không được để trắng");

			if (StringUtils.isBlank(action))
				return BaseResponse.error(Constant.ERROR_PARAM, "Thao tác không được để trắng");

			if (!"get".equals(action) && !"receive".equals(action) && !"time".equals(action))
				return BaseResponse.error(Constant.ERROR_PARAM, "Thao tác không đúng");

			UserService userService = new UserServiceImpl();
			boolean isToken = userService.isActiveToken(nickName, accessToken);
			if (!isToken) {
				return BaseResponse.error(Constant.ERROR_SESSION,
						"Phiên làm việc của bạn đã hết hạn , vui lòng tải lại trang !");
			}

			BaseResponse<Object> res = new BaseResponse<Object>();
			int startHour = 16;
			int endHour = 17;
			res = checkCondition(nickName, startHour, endHour);
			switch (action) {
			case "get":
				return res.toJson();
			case "time":
				return getCountDown(nickName, startHour, endHour).toJson();
			case "receive":
				if (!res.getErrorCode().equals("0")) {
					return res.toJson();
				}
				
				long bonus = getAmount();
				UserBonusService userBonusService = new UserBonusServiceImpl();
				UserBonusModel model = new UserBonusModel(nickName, Games.LUCKY_MONEY.getId(), (double) bonus, null, ip,
						"BONUS LUCKY MONEY " + Games.LUCKY_MONEY.getName());
				userBonusService.insertBonus(model);
				MoneyResponse moneyResponse = userService.updateMoney(nickName, bonus, "vin",
						Games.LUCKY_MONEY.getName(), Games.LUCKY_MONEY.getId() + "", "LUCKY_MONEY", 0L, null,
						TransType.NO_VIPPOINT);
				if (moneyResponse.getErrorCode().equals("0")) {
					res.setData(bonus);
					return res.toJson();
				} else {
					res.setData("Add money fail");
					res.setErrorCode("1005");
					res.setMessage(
							"Cộng tiền thưởng không thành công. Vui lòng liên hệ bộ phận chăm sóc khách hàng để được giúp đỡ");
					res.setSuccess(false);
					return res.toJson();
				}
			default:
				return BaseResponse.error(Constant.ERROR_PARAM, "Thao tác không đúng");
			}
		} catch (Exception e) {
			this.logger.error((Object) "Lucky money error: ", (Throwable) e);
			BaseResponse<Object> res = new BaseResponse<Object>();
			res.setData("Exception");
			res.setErrorCode("1001");
			res.setMessage(e.getMessage());
			res.setSuccess(false);
			return res.toJson();
		}
	}

	private String getIpAddress(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		return ipAddress;
	}

	private BaseResponse<Object> checkCondition(String nickname, int startHour, int endHour) {
		BaseResponse<Object> res = new BaseResponse<Object>();
		UserBonusService userBonusService = new UserBonusServiceImpl();
		boolean rs = false;
		rs = userBonusService.checkExit(nickname, Games.LUCKY_MONEY.getId());
		if (rs) {
			res.setData("Received bonus");
			res.setErrorCode("1003");
			res.setMessage("Bạn đã nhận phần thưởng ngày hôm nay rồi.");
			res.setSuccess(false);
			return res;
		}

		rs = userBonusService.checkConditionsByCurrentTime(nickname);
		if (!rs) {
			res.setData("Not passed conditions");
			res.setErrorCode("1004");
			res.setMessage(
					"Bạn không đủ điều kiện để nhận phần thưởng. Vui lòng đọc thể lệ và hoàn thành các yêu cầu để được nhận thưởng");
			res.setSuccess(false);
			return res;
		}

		if ((java.time.LocalDateTime.now().getHour() >= endHour && java.time.LocalDateTime.now().getSecond() > 0) ||
				java.time.LocalDateTime.now().getHour() > endHour) {
			res.setData("Over time");
			res.setErrorCode("1005");
			res.setMessage("Khung giờ nhận thưởng đã đóng, vui lòng chờ tới khung giờ tiếp theo để nhận thưởng.");
			res.setSuccess(false);
			return res;
		}

		if (java.time.LocalDateTime.now().getHour() < startHour) {
			// TODO: Calculator second to start event
			java.time.LocalDateTime dateTime = java.time.LocalDateTime.parse(
					new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) + "T" + startHour + ":00:00");
			res.setData(dateTime.toEpochSecond(ZoneOffset.UTC)
					- java.time.LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
			res.setErrorCode("1002");
			res.setMessage("Khung giờ nhận thưởng chưa được mở, vui lòng chờ tới khung giờ nhận thưởng.");
			res.setSuccess(false);
			return res;
		}
		
		res.setErrorCode("0");
		res.setMessage("success");
		res.setData("success");
		res.setSuccess(true);
		return res;
	}

	public static BaseResponse<Object> getCountDown(String nickname, int startHour, int endHour) {
		BaseResponse<Object> res = new BaseResponse<Object>();
		try {
			java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			Map<String, Object> data = new HashMap<>();
			java.time.LocalDateTime currentTime = java.time.LocalDateTime.now().plusSeconds(1);
			if (currentTime.getHour() < startHour) {
				java.time.LocalDateTime targetTime = java.time.LocalDateTime.parse(
						new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) + "T" + startHour + ":00:00");
				currentTime = java.time.LocalDateTime.now().plusSeconds(1);
				data.put("currentTime", formatter.format(currentTime));
				data.put("countTime",
						(targetTime.toEpochSecond(ZoneOffset.UTC) - currentTime.toEpochSecond(ZoneOffset.UTC)));
				res.setData(data);
				res.setErrorCode("1002");
				res.setMessage(null);
				res.setSuccess(false);
				return res;
			}

			if ((currentTime.getHour() >= endHour && currentTime.getSecond() > 0) || (currentTime.getHour() > endHour)) {
				java.time.LocalDateTime targetTime = java.time.LocalDateTime.parse(java.time.format.DateTimeFormatter
						.ofPattern("yyyy-MM-dd").format(java.time.LocalDateTime.now().plusDays(1)) + "T" + startHour +  ":00:00");
				currentTime = java.time.LocalDateTime.now().plusSeconds(1);
				data.put("currentTime", formatter.format(currentTime));
				data.put("countTime",
						(targetTime.toEpochSecond(ZoneOffset.UTC) - currentTime.toEpochSecond(ZoneOffset.UTC)));
				res.setData(data);
				res.setErrorCode("1005");
				res.setMessage(null);
				res.setSuccess(false);
				return res;
			}
			
			UserBonusService userBonusService = new UserBonusServiceImpl();
			boolean rs = false;
			rs = userBonusService.checkExit(nickname, Games.LUCKY_MONEY.getId());
			if (rs) {
				res.setData("Received bonus");
				res.setErrorCode("1003");
				res.setMessage("Bạn đã nhận phần thưởng ngày hôm nay rồi.");
				res.setSuccess(false);
				return res;
			}
			
			res.setData(null);
			res.setErrorCode("0");
			res.setMessage(null);
			res.setSuccess(true);
			return res;
		} catch (Exception e) {
			res.setData(null);
			res.setErrorCode("1001");
			res.setMessage(null);
			res.setSuccess(false);
			return res;
		}
	}

	public static void main(String[] args) {
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
				.ofPattern("yyyy-MM-dd HH:mm:ss");
		getCountDown("", 14, 15);
		java.time.LocalDateTime dateTime = java.time.LocalDateTime.now().plusHours(7);
		if (dateTime.getHour() < 14) {
			java.time.LocalDateTime targetTime = java.time.LocalDateTime.parse(
					new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) + " 14:00:00",
					formatter);
			System.out.println("targetTime: " + formatter.format(targetTime));
			java.time.LocalDateTime currentTime = java.time.LocalDateTime.now().plusSeconds(1);
			System.out.println("currentTime: " + formatter.format(currentTime));
			System.out.println("rs: " + (targetTime.toEpochSecond(ZoneOffset.UTC) - currentTime.toEpochSecond(ZoneOffset.UTC)));
		}

		if (dateTime.getHour() >= 15 && dateTime.getSecond() > 0) {
			java.time.LocalDateTime targetTime = java.time.LocalDateTime.parse(java.time.format.DateTimeFormatter
					.ofPattern("yyyy-MM-dd").format(java.time.LocalDateTime.now().plusDays(1)) + " 14:00:00",
					formatter);
			System.out.println("targetTime: " + formatter.format(targetTime));
			java.time.LocalDateTime currentTime = java.time.LocalDateTime.now().plusSeconds(1);
			System.out.println("currentTime: " + formatter.format(currentTime));
			System.out.println("rs: " + (targetTime.toEpochSecond(ZoneOffset.UTC) - dateTime.toEpochSecond(ZoneOffset.UTC)));
		}
		
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
		try {
			long diff = Math.abs(TimeUnit.DAYS.convert(format.parse(format.format(new Date())).getTime() - format.parse("2021-10-30 00:00:00").getTime(),
					TimeUnit.MILLISECONDS));
			System.out.println("offset: " + diff);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
