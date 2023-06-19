package com.vinplay.run;

import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.log4j.Logger;

import com.vinplay.dao.ag.AgDao;
import com.vinplay.dao.impl.ag.AgDaoImpl;
import com.vinplay.dto.ag.AGGamesReportsDetailData;
import com.vinplay.dto.ag.GetAGGamesReportsRequest;
import com.vinplay.dto.ag.GetAGGamesReportsResponseDTO;
import com.vinplay.interfaces.ag.ReportAGService;
import com.vinplay.logic.CommonMethod;
import com.vinplay.logic.InitData;
import com.vinplay.service.GameDaoService;
import com.vinplay.service.impl.GameDaoServiceImpl;
import com.vinplay.usercore.utils.GameThirdPartyInit;

public final class TaskCollectAGLog extends java.util.TimerTask {
	private static final Logger logger = Logger.getLogger(TaskCollectAGLog.class);
	private AgDao agDao = new AgDaoImpl();

	private GameDaoService gameCommonDao = new GameDaoServiceImpl();
	private static final String GAME_NAME = "ag";

	public void run() {
		boolean isNotMaintain = !InitData.isAGDown();
		if (isNotMaintain) {
			try {
				boolean isLoop = true;
				while (isLoop) {
					String dtStartUTC = gameCommonDao.getLastUpdateTime(GAME_NAME);
					dtStartUTC = CommonMethod.getTimeSpace(dtStartUTC, "yyyy-MM-dd HH:mm:ss", 1, "ss");
					if ("".equals(dtStartUTC)) {
						logger.error("Error occured while trying to collect AG games log- getDTEndUTC!");
						return;
					}
					String mark = CommonMethod.getTimeSpace(dtStartUTC, "yyyy-MM-dd HH:mm:ss", 10, "mm");
					isLoop = CommonMethod.GetCurDate("yyyy-MM-dd HH:mm:ss").compareToIgnoreCase(mark) > 0;

					String dtEndUTC = this.getEndTime(dtStartUTC);
					String startDateGMTMinus4 = CommonMethod.convertUTCToThirdPartiesTime(
							CommonMethod.convertSystemTimeToUTC(dtStartUTC, "yyyy-MM-dd HH:mm:ss"),
							GameThirdPartyInit.AG_TIMEZONE, "yyyy-MM-dd HH:mm:ss");
					String endDateGMTMinus4 = CommonMethod.convertUTCToThirdPartiesTime(
							CommonMethod.convertSystemTimeToUTC(dtEndUTC, "yyyy-MM-dd HH:mm:ss"),
							GameThirdPartyInit.AG_TIMEZONE, "yyyy-MM-dd HH:mm:ss");
					boolean pass = this.getAGGamesReportsService(startDateGMTMinus4, endDateGMTMinus4, dtEndUTC);
					isLoop = isLoop && pass;
					Thread.sleep(200);
				}

			} catch (Exception ex) {
				logger.error("Error occured while trying to collect AG games log!", ex);
			}
		}
	}

	private boolean getAGGamesReportsService(String dtStart, String dtEnd, String noLogTimeSave) throws Exception {
		logger.info("[TaskCollectAGGamesLog] get log From " + dtStart + " to " + dtEnd);
		boolean flag = false;
		int page = 1;
		int perpage = 1000;

		GetAGGamesReportsRequest request = new GetAGGamesReportsRequest();
		request.setCagent(GameThirdPartyInit.AG_REPORT_CAGENT);
		request.setStartdate(dtStart);
		request.setEnddate(dtEnd);
		request.setBy("ASC");
		request.setPage(page + "");
		request.setPerpage(perpage + "");

		ReportAGService reportAGService = new ReportAGService();
		GetAGGamesReportsResponseDTO response = reportAGService.getLiveGamesReportsData(request);
		if (response == null) {
			logger.info("[TaskCollectAGGamesLog] AG Get Log Error: response = null!");
			return false;
		}

		if ("0".equals(response.getInfo())) {
			if ("0".equals(response.getAddition().getTotal())) {
				gameCommonDao.updateLastTime(GAME_NAME, noLogTimeSave);
				return true;
			}
			List<AGGamesReportsDetailData> detailDataList = response.getRow();
			if (detailDataList != null && detailDataList.size() > 0) {
				for (AGGamesReportsDetailData detailData : detailDataList) {
					try {
						boolean resp = agDao.saveRecord(detailData);
						if (resp) {
							logger.info("[TaskCollectAGGamesLog] Processing AG success .");
							flag = true;
						} else {
							logger.info("[TaskCollectAGGamesLog] Save AG Log Error." + detailData.getBillNo());
							flag = false;
							break;
						}
					} catch (Exception ex) {
						logger.error("Error occured while trying to collect AG live games log!", ex);
					}

				}

			} else {
				logger.info("[TaskCollectAGGamesLog] AGGamesReportsDetailData is null.");
			}
		} else if ("44000".equals(response.getInfo())) {
			logger.info(
					"[TaskCollectAGGamesLog] Key value had error! Please check again plain code and encrypt order.");
		} else if ("61003".equals(response.getInfo())) {
			logger.info("[TaskCollectAGGamesLog] Product ID or CAGENT does not exist!");
		} else if ("60001".equals(response.getInfo())) {
			logger.info("[TaskCollectAGGamesLog] Username does not exist!");
		} else if ("61004".equals(response.getInfo())) {
			logger.info("[TaskCollectAGGamesLog] Command execute error!");
		} else if ("61001".equals(response.getInfo())) {
			logger.info("[TaskCollectAGGamesLog] Limited check request time!");
		} else if ("61002".equals(response.getInfo())) {
			logger.info("[TaskCollectAGGamesLog] Missing parameter! Please check if it is import correctly.");
		} else if ("61005".equals(response.getInfo())) {
			logger.info("[TaskCollectAGGamesLog] Operator does not exist!");
		} else {
			logger.info("[TaskCollectAGGamesLog] Request AG live game's log had error!");
		}

		return flag;
	}

	public static void main(String[] args) {
		String saveBetTime = CommonMethod.convertToCurrentSysTime(
				CommonMethod.convertFromThirdPartiesTimeZoneToUTC("2020-02-15 06:00:00", "PRT", "yyyy-MM-dd HH:mm:ss"),
				"yyyy-MM-dd HH:mm:ss");
		System.out.println(saveBetTime);
	}

	private String getEndTime(String startTime) throws ParseException {
		// Check if differ between start date and end date more than 10 minutes
		String endTime = CommonMethod.GetCurDate("yyyy-MM-dd HH:mm:ss");
		LocalDateTime gamesStartTime = CommonMethod.convertToLocalDateTime(startTime);
		LocalDateTime gamesEndTime = CommonMethod.convertToLocalDateTime(endTime);
		Duration duration = Duration.between(gamesStartTime, gamesEndTime);
		long diffMins = duration.toMinutes();
		if (diffMins > 10) {
			// Check if new end date has passed current system date time or not
			LocalDateTime localDtEnd = gamesStartTime.plusMinutes(5);
			LocalDateTime currentSysTime = LocalDateTime.now();
			Duration timeDuration = Duration.between(localDtEnd, currentSysTime);
			long endTimeDiffMins = timeDuration.toMinutes();
			if (endTimeDiffMins >= 5) {
				endTime = localDtEnd.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			}
		}
		return endTime;
	}
}
