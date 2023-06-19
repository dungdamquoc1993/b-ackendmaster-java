package com.vinplay.run;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.vinplay.dao.ebet.EbetDao;
import com.vinplay.dao.impl.ebet.EbetDaoImpl;
import com.vinplay.dto.ebet.BetHistoriesDto;
import com.vinplay.dto.ebet.UserBetHistoriReqDto;
import com.vinplay.dto.ebet.UserBetHistoriRespDto;
import com.vinplay.interfaces.ebet.UserBetHistoryService;
import com.vinplay.logic.CommonMethod;
import com.vinplay.logic.InitData;
import com.vinplay.service.GameDaoService;
import com.vinplay.service.impl.GameDaoServiceImpl;
import com.vinplay.usercore.utils.GameThirdPartyInit;


public class TaskCollectEBetLog extends java.util.TimerTask {
	private static final Logger LOGGER = Logger.getLogger(TaskCollectEBetLog.class);
	private GameDaoService gameCommonDao = new GameDaoServiceImpl();
	
	private EbetDao ebetDao =new EbetDaoImpl();
	public static void main(String[] args) throws Exception {
		String dtEndUTC = CommonMethod.convertHCMToUTC(CommonMethod.GetCurDate("yyyy-MM-dd'T'HH:mm:ss'Z'"), "yyyy-MM-dd'T'HH:mm:ss'Z'");
		System.out.println(dtEndUTC);
	}
	
	@Override
	public void run() {
		boolean isNotMaintain = !InitData.isEbetDown();
		if (isNotMaintain) {
			try {
				String dtStartUTC = gameCommonDao.getLastUpdateTime("ebet");
				String dtEndUTC = CommonMethod.convertHCMToUTC(CommonMethod.GetCurDate("yyyy-MM-dd'T'HH:mm:ss'Z'"), "yyyy-MM-dd'T'HH:mm:ss'Z'");
				if ("".equals(dtStartUTC)) {
					dtStartUTC = dtEndUTC;
				}
				UserBetHistoriReqDto reqDto = new UserBetHistoriReqDto();
				reqDto.setStartTimeStr(dtStartUTC);
				reqDto.setEndTimeStr(dtEndUTC);
				reqDto.setChannelId(GameThirdPartyInit.CHANNEL_ID);
				reqDto.setSubChannelId(0);
				Integer timestamp = (int) (System.currentTimeMillis() / 1000L);
				reqDto.setTimestamp(timestamp);
				byte[] data = timestamp.toString().getBytes(StandardCharsets.UTF_8);
				reqDto.setSignature(CommonMethod.sign(data, GameThirdPartyInit.PRIVATE_KEY));
				reqDto.setPageSize(5000);
				LOGGER.info("[TaskCollectEBetLog]" + " start time: " + dtStartUTC + " end time: " + dtEndUTC);
				// get data from ebet
				UserBetHistoryService userBetHistoryService = new UserBetHistoryService();
				UserBetHistoriRespDto result = userBetHistoryService.getUserBetHistory(reqDto);
				if (result == null) {
					LOGGER.error("[TaskCollectEBetLog] User Bet History<userbethistory> result: null");
					return;
				}
				if(result.getStatus() == 200) {
					if(result.getCount() > 0) {
						List<BetHistoriesDto> betHistory = result.getBetHistories();
						ebetDao.saveEbetBetLog(betHistory);
						//update lasttime
						gameCommonDao.updateLastTime("ebet", dtEndUTC);
					} else {
						LOGGER.info("[TaskCollectEBetLog] User Bet History<userbethistory> betHistory: empty");
						return;
					}
				} else {
					LOGGER.error("[TaskCollectEBetLog] User Bet History<userbethistory> status: " + result.getStatus());
					return;
				}
			} catch (Exception ex) {
				
				LOGGER.error("TaskCollectEBetLog", ex);
			}
		}
	}


}
