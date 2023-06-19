package com.vinplay.run;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import com.vinplay.shotfish.entites.ShotfishConfig;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.utils.ShotFishUtils;
import com.vinplay.vbee.common.response.BaseResponse;


public class TaskCollecFishLog extends java.util.TimerTask {
	
	private static final Logger logger = Logger.getLogger(TaskCollecFishLog.class);
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public void run() {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
			logger.info("[TaskCollectFishLog] starting " + simpleDateFormat.format(new Date()) + " ...");
			BaseResponse<Object> result = ShotFishUtils.synchronizeHistory();
			logger.info(result);
			logger.info("[TaskCollectWMLog] finishing " + simpleDateFormat.format(new Date()) + " ...");
		} catch (Exception ex) {
			logger.error("Error occured while trying to collect Fish games log! ", ex);
			ex.printStackTrace();
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
