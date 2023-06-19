package com.vinplay.run;

import java.util.List;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.vinplay.dao.sbo.SboDao;
import com.vinplay.dao.sbo.impl.SboDaoImpl;
import com.vinplay.dto.sbo.AbsSboBaseResponse;
import com.vinplay.logic.CommonMethod;
import com.vinplay.logic.InitData;
import com.vinplay.service.sbo.SboTaskService;
import com.vinplay.service.sbo.impl.SboTaskServiceImpl;
import com.vinplay.usercore.utils.GameThirdPartyInit;


/**
 * @author Adminstrator
 *
 */
public class TaskAutoCreateSboUser extends TimerTask {
	private Integer temp_maxUserId = 0;

	private static final Logger LOGGER = Logger.getLogger(TaskAutoCreateSboUser.class);
	private SboDao sboDao = new SboDaoImpl();
	@Override
	public void run() {
		String h = CommonMethod.GetCurDate("HH");// 
		if (GameThirdPartyInit.CREATE_USER_TIME.equals(h) && !InitData.isSBODown()) {
			try {
				// Get Max Id
				int maxId = 0;
				int maxUserMapping = 0;
				try {
					List<Integer> lstSboResult = sboDao.maxSboUser();

					if (lstSboResult != null) {
						maxId = lstSboResult.get(0);
						maxUserMapping = lstSboResult.get(1);
					} else {
						maxId = GameThirdPartyInit.SBO_START_USER_ID;
					}
				} catch (Exception e) {
					LOGGER.error(e);
					return;
				}

				LOGGER.info("[TaskAutoCreateSboUser] max sbo userId: " + maxId);
				LOGGER.info("[TaskAutoCreateSboUser] number of remain sbo userId: " + maxUserMapping);
				if (maxUserMapping < GameThirdPartyInit.NUM_ACCOUNT_REG) {
					int countWhileLoop = 0;
					int count = 1;
					int increaseId = 1;
					int sboCountId = 0;
					if (maxId < temp_maxUserId) {
						maxId = temp_maxUserId;
						LOGGER.info("[TaskAutoCreateSboUser] temp_maxUserId: " + temp_maxUserId);
					}
					// catch exception exit loop in case sbo platform is under maintenance!
					while (count <= (GameThirdPartyInit.NUM_ACCOUNT_REG - maxUserMapping) && countWhileLoop < 100) {
						// call register new member from sbo service
						sboCountId = maxId + increaseId;
						String sboId = GameThirdPartyInit.SBO_USER_NAME_PREFIX
								+ StringUtils.leftPad(sboCountId + "", 6, "0");
						SboTaskService sboService = new SboTaskServiceImpl();
						boolean isSbo = sboService.createUser(sboId);
						if (isSbo) {
							boolean isInsert = sboDao.generateSboUser(sboId, sboCountId);
							if (isInsert) {
								count++;
							} else {
								LOGGER.error("[TaskAutoCreateSboUser] Insert sbo id: " + sboId + " to SboUser table failed!");
							}
							increaseId++;
						} else {
							countWhileLoop++;
							LOGGER.info("[TaskAutoCreateSboUser] call SboServices to create user failed");
						}
					}
				}
			} catch (Exception ex) {
				LOGGER.error("[TaskAutoCreateSboUser] has an exception: ", ex);
			}
		}
	}
}