package com.vinplay.run;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vinplay.dao.sbo.SboDao;
import com.vinplay.dao.sbo.impl.SboDaoImpl;
import com.vinplay.dto.sbo.SboRecordDetail;
import com.vinplay.logic.InitData;
import com.vinplay.service.GameDaoService;
import com.vinplay.service.impl.GameDaoServiceImpl;
import com.vinplay.service.sbo.SboTaskService;
import com.vinplay.service.sbo.impl.SboTaskServiceImpl;


/**
 * @author Adminstrator
 *
 */
public class TaskAutoCollectSboBetLogs extends TimerTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskAutoCollectSboBetLogs.class);
	
	private final SboTaskService sboService = new SboTaskServiceImpl();
	private final GameDaoService gameCommonDao = new GameDaoServiceImpl();
	private final SboDao sboDao = new SboDaoImpl();

	private volatile static boolean isRunning = false;
	
	public static void main(String[] args) {
		LocalDateTime currentTime = LocalDateTime.now();
		LocalDateTime currentTimex2 = LocalDateTime.now().minusHours(11);
		long elapsedHours = ChronoUnit.HOURS.between(currentTime, currentTimex2);
		System.out.println(elapsedHours);
	}
	@Override
	public void run() {
		if (InitData.isSBODown() || isRunning)
			return;
		try {
			isRunning = true;
			// get last version key from database
			String lastTime = gameCommonDao.getLastUpdateTime("sbo");//gameCommonDao.getLastUpdateTime("ibc2")
			LocalDateTime startTime = LocalDateTime.parse(lastTime);
			LocalDateTime currentTime = LocalDateTime.now().minusHours(11);
			LocalDateTime next29Minus = startTime.plusMinutes(29);
			LocalDateTime timeMax = startTime;
			// get log from 3party
			int k=0;
			while(true) {
				if(next29Minus.isAfter(currentTime)) {//>
					next29Minus = currentTime;
					k++;
				}
				if(k>=2) {
					break;
				}
				try {
					List<SboRecordDetail> betDetails = sboService.getBetLogDetail(startTime.toString(), next29Minus.toString());
					if (betDetails != null && !betDetails.isEmpty()) {
						// save log data
						timeMax = startTime;
						for (SboRecordDetail record : betDetails) {
							sboDao.saveSboBetLog(record);
							// get last version key from
							if (record.getModifyDate() != null && !"".equals(record.getModifyDate())) {
								LocalDateTime lastModifytime = LocalDateTime.parse(record.getModifyDate());
								if (lastModifytime.isAfter(timeMax)) {
									timeMax = lastModifytime;
								}
							}
						}
						//update last modify
						gameCommonDao.updateLastTime("sbo", timeMax.toString());//2021-08-25T16:52:30.573
					}
				} catch (Exception e) {
					LOGGER.error(e + "");
				} finally {
					startTime = next29Minus;
					next29Minus = next29Minus.plusMinutes(29);
				}
			}
			//neu qua 12h k co log -> update last logtime = ngay cuoi cùng do
			long elapsedHours = ChronoUnit.HOURS.between(timeMax, currentTime);
			if (elapsedHours > 12) {
				gameCommonDao.updateLastTime("sbo", currentTime.minusHours(12).toString());// 2021-08-25T16:52:30.573
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("[TaskAutoCollectSboBetLogs] exception: " + e);
		} finally {
			isRunning = false;
		}
	}

}
