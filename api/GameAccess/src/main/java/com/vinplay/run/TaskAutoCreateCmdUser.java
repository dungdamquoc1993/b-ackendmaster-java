package com.vinplay.run;

import java.util.List;
import java.util.Optional;
import java.util.TimerTask;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.vinplay.dao.cmd.CmdDao;
import com.vinplay.dao.cmd.impl.CmdDaoImpl;
import com.vinplay.dto.sportsbook.SportsbookCreateMemberRespDto;
import com.vinplay.interfaces.sportsbook.SportsbookAllServices;
import com.vinplay.logic.CommonMethod;
import com.vinplay.logic.InitData;
import com.vinplay.usercore.utils.GameThirdPartyInit;

public class TaskAutoCreateCmdUser extends TimerTask {
	private Integer temp_maxUserId = 0;

	private static final Logger LOGGER = Logger.getLogger(TaskAutoCreateCmdUser.class);
	private CmdDao cmdDao = new CmdDaoImpl();
	int START_CMDID = 1;

	@Override
	public void run() {
		String h = CommonMethod.GetCurDate("HH");
		if (InitData.isCMDDown() || !h.equals("19")) {
			LOGGER.info("CMD is maintained ,time="+h);
			return;
		}

		try {
			// Get Max Id
			// Get Max wmID
			int maxId = 0;
			int maxUserMapping = 0;
			int start_wm_id = START_CMDID;

			try {
				List<Integer> lstWmResult = cmdDao.maxCmdUser();

				if (lstWmResult != null) {
					maxId = lstWmResult.get(0);
					maxUserMapping = lstWmResult.get(1);
				} else {
					maxId = start_wm_id;
				}
			} catch (Exception e) {
				LOGGER.error(e);
				return;
			}
			LOGGER.info("[TaskAutoCreateUserCMD]" + " max cmdID: " + maxId);
			LOGGER.info("[TaskAutoCreateUserCMD]" + " number of remain cmdID: " + maxUserMapping);

			if (maxUserMapping < GameThirdPartyInit.NUM_ACCOUNT_REG) {
				int countWhileLoop = 0;
				int count = 1;
				int increaseId = 1;
				int sportsBookCountId = 0;
				if (maxId < temp_maxUserId) {
					maxId = temp_maxUserId;
					LOGGER.info("[TaskAutoCreateSportsbookUser] temp_maxUserId: " + temp_maxUserId);
				}

				while (count <= (GameThirdPartyInit.NUM_ACCOUNT_REG - maxUserMapping) && countWhileLoop < 100) {
					// call register new member from sports book service
					sportsBookCountId = maxId + increaseId;
					String sportsBookId = GameThirdPartyInit.SPORTS_BOOK_USER_NAME_PREFIX
							+ StringUtils.leftPad(sportsBookCountId + "", 6, "0");

					Optional<SportsbookCreateMemberRespDto> sportsBookOpt = SportsbookAllServices.getInstance()
							.createSportsbookMember(sportsBookId);
					if (sportsBookOpt.isPresent()) {
						if(sportsBookOpt.get().getCode().equals("0") || sportsBookOpt.get().getCode().equals("-98")) {
							List<String> data = sportsBookOpt.get().getData();
							if (CollectionUtils.isNotEmpty(data) && data.size() == 2) {
								// Insert account information into SportsBookUser table
								sportsBookId = data.get(0);
								String sportsBookUserName = data.get(1);
								boolean isInsertWm = cmdDao.generateCmdUser(sportsBookId, sportsBookCountId,
										sportsBookUserName);
								try {
									if (isInsertWm) {
										count++;
										LOGGER.info("[TaskAutoCreateSportsbookUser] Insert sports book id: " + sportsBookId
												+ " to SportsBookUser table success!");
									} else {
										LOGGER.error("[TaskAutoCreateSportsbookUser] Insert sports book id: " + sportsBookId
												+ " to SportsBookUser table failed!");
									}
								} catch (Exception ex) {
									ex.printStackTrace();
									LOGGER.error("[TaskAutoCreateSportsbookUser] p_GA_InsertSportsBookUser", ex);
									return;
								}
								increaseId++;
							} else {
								countWhileLoop++;
								LOGGER.info(
										"[TaskAutoCreateSportsbookUser] call SportsBookAllServices to create user return response not correct");
							}
						}else if (sportsBookOpt.get().getCode().equals("-98")) {
							//exist
							LOGGER.info(sportsBookOpt.get().getMessage());
						}
						
					} else {
						countWhileLoop++;
						LOGGER.info("[TaskAutoCreateSportsbookUser] call SportsBookAllServices to create user failed");
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error("[TaskAutoCreateSportsbookUser] has an exception: ", ex);
		}
	}
}