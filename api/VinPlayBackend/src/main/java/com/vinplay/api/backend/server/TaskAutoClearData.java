package com.vinplay.api.backend.server;

import java.time.LocalDateTime;

import org.apache.log4j.Logger;

import com.vinplay.dal.dao.LogBauCuaDAO;
import com.vinplay.dal.dao.impl.LogBauCuaDAOImpl;
import com.vinplay.dal.service.LogTaiXiuService;
import com.vinplay.dal.service.impl.LogTaiXiuServiceImpl;

public class TaskAutoClearData extends java.util.TimerTask {
	
	private static final Logger LOGGER = Logger.getLogger("backend");

	@Override
	public void run() {
		
		LOGGER.info("Start Job TaskAutoClearData , time=" + LocalDateTime.now());
		
		LogTaiXiuService service = new LogTaiXiuServiceImpl();
		LogBauCuaDAO bc = new LogBauCuaDAOImpl();
		
		long rowBc = 0;
		int rowTx = 0;
		
		try {
			rowTx = service.deleteLogTaiXiuByDay(1);
		} catch (Exception e) {
			LOGGER.error(e);
		}
		
		try {
			rowBc = bc.deleteDataByDayLogBauCua();
		} catch (Exception e) {
			LOGGER.error(e);
		}

		LOGGER.info("Clear data taixiu , row = " + rowTx);
		LOGGER.info("Clear data baucua , row = " + rowBc);

	}

}
