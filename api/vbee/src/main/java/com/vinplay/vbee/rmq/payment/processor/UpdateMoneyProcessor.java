package com.vinplay.vbee.rmq.payment.processor;

import com.hazelcast.core.IMap;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.usercore.utils.UserMakertingUtil;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.hazelcast.HazelcastUtils;
import com.vinplay.vbee.common.messages.BaseMessage;
import com.vinplay.vbee.common.messages.MoneyMessageInMinigame;
import com.vinplay.vbee.common.models.cache.UserActiveModel;
import com.vinplay.vbee.common.statics.Consts;
import com.vinplay.vbee.common.utils.VinPlayUtils;
import com.vinplay.vbee.dao.UserDao;
import com.vinplay.vbee.dao.impl.UserDaoImpl;
import java.util.Date;

import org.apache.log4j.Logger;

public class UpdateMoneyProcessor implements BaseProcessor<byte[], Boolean> {
	private static final Logger logger = Logger.getLogger("vbee");

	public Boolean execute(Param<byte[]> param) {
		byte[] body = param.get();
		MoneyMessageInMinigame message = (MoneyMessageInMinigame) BaseMessage.fromBytes(body);
		try {
			boolean updateTimeOut;
			String nickname = message.getNickname();
			IMap<String, UserActiveModel> userMap = HazelcastUtils.getActiveMap(nickname);
			updateTimeOut = true;
			if (userMap.containsKey(nickname)) {
				try {
					userMap.lock(nickname);
					UserActiveModel model = userMap.get(nickname);
					if (Long.parseLong(message.getId()) < model.getLastMessageId() || model.isBot()) {
						return true;
					}
					long currentTime = System.currentTimeMillis();
					long validTimeMs = currentTime - 600000L;
					if (model.getLastActive() > validTimeMs) {
						updateTimeOut = false;
					} else {
						if (message.getMoneyType().equals("vin")) {
							updateTimeOut = !model.isBot()
									|| VinPlayUtils.updateMoneyTimeout(model.getLastActiveVin(), 30);
							if (updateTimeOut) {
								model.setLastActiveVin(new Date().getTime());
							}
						} else {
							updateTimeOut = model.isBot() ? VinPlayUtils.updateMoneyTimeout(model.getLastActiveXu(), 30)
									: VinPlayUtils.updateMoneyTimeout(model.getLastActiveXu(), 10);
							if (updateTimeOut) {
								model.setLastActiveXu(new Date().getTime());
							}
						}
						model.setLastActive(new Date().getTime());
						model.setLastMessageId(Long.parseLong(message.getId()));
					}
					model.setUpdateMySQL(updateTimeOut);
					userMap.put(nickname, model);
				} catch (Exception e) {
					logger.error(e);
				} finally {
					userMap.unlock(nickname);
				}
			}

			int type = 0;
			if (message.getActionName().equals("Bot")) {
				type = 3;
			} else if (Consts.LIST_RECHARGE.contains(message.getActionName()) && message.getMoneyVP() == -1) {
				type = 1;
				UserMakertingUtil.userNapVin(message.getNickname(), message.getMoneyExchange());

			} else if (message.getMoneyVP() > 0 || message.getVp() != 0) {
				type = 2;
			}
			if (!updateTimeOut) {
				logger.info("update no timeout nickname: " + message.getNickname() + " money: " + message.getMoneyExchange()
								+ " current: " + message.getAfterMoney() + " moneyType: " + message.getMoneyType());
				return true;
			}
			//update money
			UserDao userDao = new UserDaoImpl();
			userDao.updateMoney(message, type);
			//update UserValue
			UserService userService = new UserServiceImpl();
			if (Consts.LIST_RECHARGE_REAL.contains(message.getActionName())) {
				userService.changeUserValue(message.getNickname(), message.getMoneyExchange(), false);
			}
			if (Consts.LIST_CASHOUT_REAL.contains(message.getActionName())) {
				if (Consts.REQUEST_CASHOUT.equalsIgnoreCase(message.getActionName())) {
					userService.changeUserValue(message.getNickname(), Math.abs(message.getMoneyExchange()), true);
				} else {
					// refund
					userService.changeUserValue(message.getNickname(), message.getMoneyExchange() * (-1), true);
				}
			}
			return true;
		} catch (Exception e2) {
			logger.error(e2);
			return false;
		}
	}
}
