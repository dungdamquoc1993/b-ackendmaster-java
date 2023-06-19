package com.vinplay.usercore.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.vinplay.usercore.dao.UserLevelDao;
import com.vinplay.usercore.dao.impl.UserLevelDaoImpl;
import com.vinplay.usercore.entities.UserLevel;
import com.vinplay.usercore.service.UserLevelService;
import com.vinplay.usercore.service.UserService;
import com.vinplay.vbee.common.models.UserModel;

public class UserLevelServiceImpl implements UserLevelService {
	private static final Logger logger = Logger.getLogger(UserLevelServiceImpl.class);
	private UserLevelDao dao = new UserLevelDaoImpl();

	private String Validate(UserLevel userLevel) {
		try {
			if (StringUtils.isBlank(userLevel.getNick_name()))
				return "Nickname người được giới thiệu không được để trống";

			if (StringUtils.isBlank(userLevel.getParent_user()))
				return "Nickname người giới thiệu không được để trống";

			return "success";
		} catch (Exception e) {
			logger.error("Error Validate: user_level" + e.getMessage());
			return e.getMessage();
		}
	}
	
	@Override
	public String create(String nickname, String nickname_parent) {
		UserLevel userLevel = new UserLevel();
		userLevel.setNick_name(nickname);
		userLevel.setParent_user(nickname_parent);
		return create(userLevel);
	}
	
	@Override
	public String create(UserLevel userLevel) {
		try {
			String result = "";
			result = Validate(userLevel);
			if (!"success".equals(result))
				return result;
			
			UserService userService = new UserServiceImpl();
			UserModel userModel= userService.getUserByNickName(userLevel.getNick_name());
			if(userModel == null)
				return "Nickname người được giới thiệu không tồn tại";
			
			userLevel.setCode(userModel.getReferralCode());
			userModel= userService.getUserByNickName(userLevel.getParent_user());
			if(userModel == null)
				return "Nickname người giới thiệu không tồn tại";
			
			UserLevel userLevelExist = dao.getByNickName(userLevel.getNick_name());
			if(userLevelExist != null)
				return "Người được giới thiệu đã tham gia chương trình rồi";
			
			UserLevel userLevelParent = dao.getByNickName(userLevel.getParent_user());
			if(userLevelParent == null) {
				userLevel.setAncestor(userLevel.getParent_user());
			}else {
				String ancestorParent = userLevelParent.getAncestor();
				if(StringUtils.isBlank(ancestorParent))
					return "Nickname người được giới thiệu không đúng";
				
				String[] arrAncestorParent = ancestorParent.split(",");
				if(arrAncestorParent.length > 1) {
					userLevel.setAncestor(userLevel.getNick_name());
				}
				
				if(arrAncestorParent.length == 1) {
					userLevel.setAncestor(ancestorParent + "," + userLevel.getParent_user());
				}
			}
			
			return dao.insert(userLevel);
		} catch (Exception e) {
			logger.error("Error create user_level: " + e.getMessage());
			return e.getMessage();
		}
	}
	
	@Override
	public String update(String oldNickname, String newNickname) {
		UserLevel userLevel = new UserLevel();
		try {
			userLevel = dao.getByNickName(oldNickname);
		} catch (SQLException e) {
			userLevel = null;
		}
		
		if(userLevel == null)
			return "Nickname nhân vật không tồn tại";
		
		String newAncestor = userLevel.getAncestor();
		newAncestor.replace(oldNickname, newNickname);
		userLevel.setAncestor(newAncestor);
		userLevel.setNick_name(newNickname);
		try {
			return dao.insert(userLevel);
		} catch (SQLException e) {
			logger.error("Error update user_level: " + e.getMessage() + " | data: " + userLevel.toJson());
			return e.getMessage();
		}
	}
	
	@Override
	public UserLevel getByNickName(String nickname, String parent_user) {
		try {
			return dao.getByNickName(nickname, parent_user);
		} catch (SQLException e) {
			return null;
		}
	}
	
	@Override
	public UserLevel getByNickName(String nickname) {
		try {
			return dao.getByNickName(nickname);
		} catch (SQLException e) {
			return null;
		}
	}
	@Override
	public Map<String, Object> findChilds(String nickname, String statDate, String endDate, int pageIndex, int limit) {
		try {
			return dao.findChilds(nickname, statDate, endDate, pageIndex, limit);
		} catch (Exception e) {
			logger.error("Error findChilds user_level: " + e.getMessage());
			return new HashMap<>();
		}
	}
}
