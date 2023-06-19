package com.vinplay.usercore.service.impl;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.vinplay.usercore.dao.UserWagesDao;
import com.vinplay.usercore.dao.impl.UserWagesDaoImpl;
import com.vinplay.usercore.entities.UserWages;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.UserWagesService;
import com.vinplay.vbee.common.enums.Games;
import com.vinplay.vbee.common.response.MoneyResponse;
import com.vinplay.vbee.common.statics.TransType;

public class UserWagesServiceImpl implements UserWagesService {
	private static final Logger logger = Logger.getLogger(UserWagesServiceImpl.class);
	private UserWagesDao dao = new UserWagesDaoImpl();

//	private String Validate(UserWages userWages) {
//		try {
//			if (StringUtils.isBlank(userWages.getNick_name()))
//				return "Nickname không được để trắng";
//			
//			UserService userService = new UserServiceImpl();
//			UserModel userModel= userService.getUserByNickName(userWages.getNick_name());
//			if(userModel == null)
//				return "Nickname không đúng";
//			
//			if (StringUtils.isBlank(String.valueOf(userWages.getBonus())))
//				return "Số tiền không được để trắng";
//
//			if (userWages.getBonus() < 0)
//				return "Số tiền không được nhỏ hơn 0";
	
//			return "success";
//		} catch (Exception e) {
//			logger.error("Error user_wages: " + e.getMessage());
//			return e.getMessage();
//		}
//	}
//
//	@Override
//	public String create(UserWages userWages) {
//		try {
//			String result = "";
//			result = Validate(userWages);
//			if (!"success".equals(result))
//				return result;
//			
//			return dao.insert(userWages);
//		} catch (Exception e) {
//			logger.error("Error create user_wages: " + e.getMessage());
//			return e.getMessage();
//		}
//	}
	
	/**
	 * Method for job calculator money from log_report_user
	 */
	@Override
	public boolean insertByJob(String date) {
		try {
			return dao.insertByJob(date);
		} catch (Exception e) {
			logger.error("Error create user_wages: " + e.getMessage());
			return false;
		}
	}
	
	@Override
	public String receivedMoney(long id) {
		try {
			UserWages userWages = dao.getById(id);
			if(userWages == null)
				return "Dữ liệu không tồn tại";
			
			if(userWages.getStatus() > 0)
				return "Bạn đã nhận tiền ngày này rồi";
			
			String result = "";
			result = dao.updateStatus(id, 1);
			if(!"success".equalsIgnoreCase(result))
				return "Cập nhật trạng thái không thành công";
			
			UserService userService = new UserServiceImpl();
			MoneyResponse moneyResponse = userService.updateMoney(userWages.getNick_name(), userWages.getBonus(), "vin",
					Games.USER_WAGES.getName(), Games.USER_WAGES.getId() + "", "USER_WAGES", 0L, null, TransType.NO_VIPPOINT);
			if (moneyResponse.getErrorCode().equals("0")) {
				return "success";
			} else {
				userWages = dao.getById(id);
				dao.updateStatus(id, 0);
				return "Cộng tiền thưởng không thành công. Vui lòng liên hệ bộ phận chăm sóc khách hàng để được giúp đỡ";
			}
		} catch (Exception e) {
			logger.error("Error updateStatus user_wages: " + e.getMessage());
			return e.getMessage();
		}
	}
	
	public String receivedAllMoney(String nickname) {
		try {
			long totalBonusNotReceived = dao.getSumBonusByStatus(nickname, 0);
			UserService userService = new UserServiceImpl();
			MoneyResponse moneyResponse = userService.updateMoney(nickname, totalBonusNotReceived, "vin",
					Games.USER_WAGES.getName(), Games.USER_WAGES.getId() + "", "USER_WAGES", 0L, null, TransType.NO_VIPPOINT);
			if (moneyResponse.getErrorCode().equals("0")) {
				String result = "";
				result = dao.updateAllStatusToReceivedBonus(nickname);
				if(!"success".equalsIgnoreCase(result)) {
					moneyResponse = userService.updateMoney(nickname, (totalBonusNotReceived * (-1)), "vin",
							Games.USER_WAGES.getName(), Games.USER_WAGES.getId() + "", "USER_WAGES : rollback because update all status fail", 0L, null, TransType.NO_VIPPOINT);
					return "Cộng tiền thưởng không thành công. Vui lòng liên hệ bộ phận chăm sóc khách hàng để được giúp đỡ";
				}
				
				return "success";
			} else 
				return "Cộng tiền thưởng không thành công. Vui lòng liên hệ bộ phận chăm sóc khách hàng để được giúp đỡ";
		} catch (Exception e) {
			logger.error("Error updateStatus user_wages: " + e.getMessage());
			return e.getMessage();
		}
	}
	
	@Override
	public Map<String, Object> history(String nickname, String statDate, String endDate, int status, int pageIndex, int limit) {
		try {
			return dao.history(nickname, statDate, endDate, status, pageIndex, limit);
		} catch (Exception e) {
			logger.error("Error history user_wages: " + e.getMessage());
			return new HashMap<>();
		}
	}
}
