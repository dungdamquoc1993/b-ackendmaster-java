package com.vinplay.run;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;

import com.vinplay.dao.wm.WmDao;
import com.vinplay.dao.wm.impl.WmDaoImpl;
import com.vinplay.dto.wm.BaseResponseDto;
import com.vinplay.dto.wm.GetDateTimeReportReqDto;
import com.vinplay.dto.wm.GetDateTimeReportResult;
import com.vinplay.interfaces.wm.ReportWMService;
import com.vinplay.logic.CommonMethod;
import com.vinplay.logic.InitData;
import com.vinplay.service.GameDaoService;
import com.vinplay.service.impl.GameDaoServiceImpl;
import com.vinplay.usercore.utils.GameThirdPartyInit;


public class TaskCollectWMLog extends java.util.TimerTask {
	
	private static final Logger logger = Logger.getLogger(TaskCollectWMLog.class);
	
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private GameDaoService gameCommonDao = new GameDaoServiceImpl();
	private WmDao wmDao = new WmDaoImpl();
	
	private static final String GAME_NAME ="wm";
	
	public void run() {
		if (!InitData.isWMDown()) {
			try {
				logger.info("[TaskCollectWMLog] starting...");
				String dtStartUTC = gameCommonDao.getLastUpdateTime(GAME_NAME);
				String dtEndUTC = CommonMethod.GetCurDate(DATE_TIME_FORMAT);
				if ("".equals(dtStartUTC)) {
					dtStartUTC = "2021-05-01 16:31:26";
				}

				String mark = CommonMethod.getTimeSpace(dtStartUTC, DATE_TIME_FORMAT, 24, "hh");
				if (dtEndUTC.compareToIgnoreCase(mark) >= 0) {
					dtEndUTC = mark;
				}

				String startDateGMT8 = CommonMethod.convertUTCToThirdPartiesTime(
						CommonMethod.convertSystemTimeToUTC(dtStartUTC, DATE_TIME_FORMAT), GameThirdPartyInit.WM_TIMEZONE,
						DATE_TIME_FORMAT);
				String endDateGMT8 = CommonMethod.convertUTCToThirdPartiesTime(
						CommonMethod.convertSystemTimeToUTC(dtEndUTC, DATE_TIME_FORMAT), GameThirdPartyInit.WM_TIMEZONE,
						DATE_TIME_FORMAT);
				this.getReportWMService(startDateGMT8, endDateGMT8);
				logger.info("[TaskCollectWMLog] finishing...");
			} catch (Exception ex) {
				logger.error("Error occured while trying to collect WM games log! ", ex);
				ex.printStackTrace();
			}
		}
	}

	private void getReportWMService(String dtStart, String dtEnd) throws Exception {
		logger.info("[TaskCollectWMLog] get log from " + dtStart + " to " + dtEnd);
		
		LocalDateTime dateStart = LocalDateTime.parse(dtStart, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
		LocalDateTime dateEnd = LocalDateTime.parse(dtEnd, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));

		String startMonth = dateStart.getMonthValue() < 10 ? "0" + dateStart.getMonthValue()
				: dateStart.getMonthValue() + "";
		String startDay = dateStart.getDayOfMonth() < 10 ? "0" + dateStart.getDayOfMonth()
				: dateStart.getDayOfMonth() + "";
		String startHour = dateStart.getHour() < 10 ? "0" + dateStart.getHour() : dateStart.getHour() + "";
		String startMinute = dateStart.getMinute() < 10 ? "0" + dateStart.getMinute() : dateStart.getMinute() + "";
		String startSecond = dateStart.getSecond() < 10 ? "0" + dateStart.getSecond() : dateStart.getSecond() + "";
		String requestStartDate = dateStart.getYear() + "" + startMonth + startDay + startHour + startMinute
				+ startSecond;

		String endMonth = dateEnd.getMonthValue() < 10 ? "0" + dateEnd.getMonthValue() : dateEnd.getMonthValue() + "";
		String endDay = dateEnd.getDayOfMonth() < 10 ? "0" + dateEnd.getDayOfMonth() : dateEnd.getDayOfMonth() + "";
		String endHour = dateEnd.getHour() < 10 ? "0" + dateEnd.getHour() : dateEnd.getHour() + "";
		String endMinute = dateEnd.getMinute() < 10 ? "0" + dateEnd.getMinute() : dateEnd.getMinute() + "";
		String endSecond = dateEnd.getSecond() < 10 ? "0" + dateEnd.getSecond() : dateEnd.getSecond() + "";
		String requestEndDate = dateEnd.getYear() + "" + endMonth + endDay + endHour + endMinute + endSecond;
		logger.info("[TaskCollectWMLog] Request start date: " + requestStartDate + " | Request end date: "
				+ requestEndDate);

		GetDateTimeReportReqDto request = new GetDateTimeReportReqDto();
		request.setStartTime(Long.valueOf(requestStartDate));
		request.setEndTime(Long.valueOf(requestEndDate));
		request.setSyslang(1); // 0: China, 1: English
		request.setTimetype(1); // 0: bet time, 1: update time
		request.setDatatype(0); // 0: winloss report, 1: tip report, 2: all

		ReportWMService reportWMService = new ReportWMService();
		BaseResponseDto<GetDateTimeReportResult[]> response = reportWMService.getDateTimeReport(request);
		if (response == null) {
			logger.info("[TaskCollectWMLog] No log found: response = null!");
			return;
		}

		GetDateTimeReportResult[] resultList = response.getResult();
		List<GetDateTimeReportResult> dataList = Objects.isNull(resultList) ? new ArrayList<GetDateTimeReportResult>()
				: Arrays.asList(resultList);
		if (107 == response.getErrorCode() && (dataList == null || dataList.isEmpty())) {
			gameCommonDao.updateLastTime(GAME_NAME,CommonMethod.convertToCurrentSysTime(
					CommonMethod.convertFromThirdPartiesTimeZoneToUTC(dtEnd, GameThirdPartyInit.WM_TIMEZONE, DATE_TIME_FORMAT),
					DATE_TIME_FORMAT));
			logger.info("[TaskCollectWMLog] No data found!");
			return;
		}

		if (0 == response.getErrorCode()) {
			if (dtStart.equals(dataList.get(0).getSettime()) || dtStart.equals(dataList.get(0).getBetTime())) {
				gameCommonDao.updateLastTime(GAME_NAME ,CommonMethod.convertToCurrentSysTime(CommonMethod.convertFromThirdPartiesTimeZoneToUTC(
						CommonMethod.getTimeSpace(dtStart, DATE_TIME_FORMAT, 1, "ss"), GameThirdPartyInit.WM_TIMEZONE,
						DATE_TIME_FORMAT), DATE_TIME_FORMAT));
				return;
			}
			
			dataList.forEach(data -> {
				try {
					wmDao.saveWMBetLog(data);
					//if currendate >enddate -> update lasttime= endtdae 
					if(LocalDateTime.now().plusHours(1).isAfter(dateEnd)) {
						logger.info("[TaskCollectWMLog] update WM game's log to"+dateEnd);
						gameCommonDao.updateLastTime(GAME_NAME,CommonMethod.convertToCurrentSysTime(
								CommonMethod.convertFromThirdPartiesTimeZoneToUTC(dtEnd, GameThirdPartyInit.WM_TIMEZONE, DATE_TIME_FORMAT),
								DATE_TIME_FORMAT));
					}
					
				} catch (Exception e) {
					logger.error("[TaskCollectWMLog] Processing WM game's log had error! ", e);
				}
			});
		} else {
			logger.info("[TaskCollectWMLog] Request WM game's log unsucceed! " + response.getErrorMessage());
		}
	}
	public static void main(String[] args) {
		try {
			System.out.println(LocalDateTime.now().plusHours(1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
