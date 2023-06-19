package com.vinplay.run;

import java.util.List;

import org.apache.log4j.Logger;

import com.vinplay.dao.wm.WmDao;
import com.vinplay.dao.wm.impl.WmDaoImpl;
import com.vinplay.dto.wm.CreateMemberReqDto;
import com.vinplay.dto.wm.CreateMemberRespeDto;
import com.vinplay.interfaces.wm.MemberWmService;
import com.vinplay.logic.CommonMethod;
import com.vinplay.logic.InitData;
import com.vinplay.usercore.utils.GameThirdPartyInit;

public class TaskAutoCreateUserWm extends java.util.TimerTask {

	private static final Logger LOGGER = Logger.getLogger(TaskAutoCreateUserWm.class);

	private static final long TIME_OUT = 5 * 60 * 1000; // 5 minutes time out
	private Integer temp_maxUserId = 0;
	private static final int START_WMID = GameThirdPartyInit.WM_START_IBCID;

	private WmDao wmDao = new WmDaoImpl();

	@Override
	public void run() {
		String h = CommonMethod.GetCurDate("HH");
		if (GameThirdPartyInit.CREATE_USER_TIME.equals(h) && !InitData.isWMDown()) {
			LOGGER.info("[TaskAutoCreateUserWm] start ..");
			// Get Max wmID
			int maxWmID = 0;
			int maxWmUserMapping = 0;
			int start_wm_id = START_WMID;
			try {
				try {
					List<Integer> lstWmResult = wmDao.maxWmUser();

					if (lstWmResult != null) {
						maxWmID = lstWmResult.get(0);
						maxWmUserMapping = lstWmResult.get(1);
					} else {
						maxWmID = start_wm_id;
					}
				} catch (Exception e) {
					LOGGER.error(e);
					return;
				}
				LOGGER.info("[TaskAutoCreateUserWm]" + " max wmID: " + maxWmID);
				LOGGER.info("[TaskAutoCreateUserWm]" + " number of remain wmID: " + maxWmUserMapping);
				if (maxWmUserMapping < GameThirdPartyInit.NUM_ACCOUNT_REG) {
					int count = 1;
					int increaseId = 1;
					String wmID = "0";
					int ibccountid = 0;
					if (maxWmID < temp_maxUserId) {
						maxWmID = temp_maxUserId;
						LOGGER.info("[TaskAutoCreateUserWm]" + " max wmID: " + maxWmID);
						LOGGER.info("[TaskAutoCreateUserWm]" + " temp_maxUserId: " + temp_maxUserId);
					}
					long startTime = System.currentTimeMillis(); // fetch starting time
					while (count <= (GameThirdPartyInit.NUM_ACCOUNT_REG - maxWmUserMapping)
							&& (System.currentTimeMillis() - startTime) < TIME_OUT) {
						MemberWmService createMember = new MemberWmService();
						CreateMemberReqDto reqDto = new CreateMemberReqDto();
						String wmPassword = CommonMethod.randomString(16);
						String wmId = GameThirdPartyInit.WM_PLAYER_PREFIX + Integer.toString(maxWmID + increaseId)
								+ GameThirdPartyInit.WM_SUFFIX;
						reqDto.setUsername(wmId);
						reqDto.setUser(GameThirdPartyInit.WM_PLAYER_PREFIX + Integer.toString(maxWmID + increaseId));
						reqDto.setPassword(wmPassword);
						reqDto.setSyslang(1);

						CreateMemberRespeDto respone = createMember.createMember(reqDto);
						if (respone == null) {
							LOGGER.error("[TaskAutoCreateUserWm]" + " create failed: " + wmID + " (reponse null)");
						} else if (respone.getErrorCode() != 0) {
							System.out.println("Register error!");
							LOGGER.error(
									"[TaskAutoCreateUserWm]" + " create failed: " + wmID + " (createMember false)");
						} else {
							ibccountid = maxWmID + increaseId;
							boolean isInsertWm = wmDao.generateWmUser(reqDto, ibccountid);
							if (isInsertWm) {
								count++;
							}
						}
						increaseId++;
					}
					LOGGER.info("[TaskAutoCreateUserWm] exec in loop while : "
							+ (System.currentTimeMillis() - startTime) / 1000 + " minutes");
					temp_maxUserId = ibccountid;
					LOGGER.info("[TaskAutoCreateUserWm]" + " number registed : " + (count - 1));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				LOGGER.error("[TaskAutoCreateUserWm] TaskAutoCreateUserWm", ex);
			}
		}
	}

}
