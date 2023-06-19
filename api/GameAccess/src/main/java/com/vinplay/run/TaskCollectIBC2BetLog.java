package com.vinplay.run;

import com.vinplay.dto.ibc2.BetDetail;
import com.vinplay.dto.ibc2.BetDetailResult;
import com.vinplay.dto.ibc2.GetSportBetLogReqDto;
import com.vinplay.interfaces.ibc2.BettingIbc2Service;
import com.vinplay.logic.InitData;
import com.vinplay.service.GameDaoService;
import com.vinplay.service.ibc2.Ibc2Dao;
import com.vinplay.service.ibc2.impl.Ibc2DaoImpl;
import com.vinplay.service.impl.GameDaoServiceImpl;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class TaskCollectIBC2BetLog extends java.util.TimerTask{
		private static final Logger LOGGER = Logger.getLogger(TaskCollectIBC2BetLog.class);
		private Ibc2Dao dao = new Ibc2DaoImpl();
		
		private GameDaoService gameCommonDao = new GameDaoServiceImpl();
		
		@Override
		public void run() {
			boolean isNotMaintain = !InitData.isIbc2Down();
			if(isNotMaintain) {
				LOGGER.info("[TaskCollectIBC2BetLog]" + " Start... " );
				try {
					//get data from ibc
					BettingIbc2Service betIbcService = new BettingIbc2Service();
	
					GetSportBetLogReqDto reqDto = new GetSportBetLogReqDto();
					reqDto.setLang("");
					//get last version key from database
					String lastVersionKeyStr = gameCommonDao.getLastUpdateTime("ibc2");//gameCommonDao.getLastUpdateTime("ibc2")
					long lastVersionKey = 0l;
					if (lastVersionKeyStr != null && !"".equals(lastVersionKeyStr)) {
						lastVersionKey = Long.parseLong(lastVersionKeyStr);
					}
					reqDto.setLastVersionKey(lastVersionKey);
					
					BetDetailResult result = betIbcService.GetSportBetLog(reqDto);
					if (result != null) {
						if(result.getError_code() == 0) {
							
							int newLastVersionKey = result.getData().getLast_version_key();
							
							//storage in lottery db
							List<BetDetail> recordTotal = new ArrayList<BetDetail>();
							List<BetDetail> record = result.getData().getBetDetails();
							List<BetDetail> record1 = result.getData().getBetNumberDetails();
							List<BetDetail> record2 = result.getData().getBetVirtualSportDetails();
							List<BetDetail> record3 = result.getData().getBetCasinoDetails();
							
							if (record != null && record.size() > 0) {
								recordTotal.addAll(record);
							}
							if (record1 != null && record1.size() > 0) {
								recordTotal.addAll(record1);
							}
							if (record2 != null && record2.size() > 0) {
								recordTotal.addAll(record2);
							}
							if (record3 != null && record3.size() > 0) {
								recordTotal.addAll(record3);
							}
							
							if (recordTotal != null && recordTotal.size() > 0) {
								LOGGER.info("TaskCollectIBC2BetLog logs version_key =" + newLastVersionKey);
								for (BetDetail reg : recordTotal) {
									dao.saveLogs(reg);
								}
							} else {
								LOGGER.info("GetIBC2BetLogResp recordToDto null");
							}
							
							if (result != null && result.getData() != null && newLastVersionKey > reqDto.getLastVersionKey().intValue()) {
								//dao.updateLastVersionKey(newLastVersionKey);
								gameCommonDao.updateLastTime("ibc2", newLastVersionKey+"");
							}
						} else {
							LOGGER.info("GetIBC2BetLogRespDto Error Code: " + result.getError_code());
						}
					} else {
						LOGGER.info("GetIBC2BetLogRespDto Result: null");
					}
					LOGGER.info("[TaskCollectIBC2BetLog]" + " END." );
				} catch(Exception ex){
					LOGGER.error("TaskCollectSportBetLog", ex);
				}
			}
		}
		public static void main(String[] args) {
			System.out.println(43712523>43712523);
		}
		
	}

