/**
 * Archie
 */
package com.vinplay.run;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.vinplay.dao.cmd.CmdDao;
import com.vinplay.dao.cmd.impl.CmdDaoImpl;
import com.vinplay.dto.sportsbook.SportsbookMemberBetTicketInformationDetail;
import com.vinplay.dto.sportsbook.SportsbookMemberBetTicketInformationRespDto;
import com.vinplay.interfaces.sportsbook.SportsbookAllServices;
import com.vinplay.logic.InitData;
import com.vinplay.service.GameDaoService;
import com.vinplay.service.impl.GameDaoServiceImpl;


/**
 * @author Archie
 *
 */
public class TaskAutoCollectCmdLogs extends TimerTask {
	
	private static final Logger LOGGER = Logger.getLogger(TaskAutoCollectCmdLogs.class);
	
	private GameDaoService gameDaoService = new GameDaoServiceImpl();
	private CmdDao dao = new CmdDaoImpl();
	
	@Override
	public void run() {
		if (InitData.isCMDDown()) {
			return;
		}
		try {
			// get last version key from database
			String lastVersionKeyStr = gameDaoService.getLastUpdateTime("cmd");
			long lastVersionKey = 0l;
			if (lastVersionKeyStr != null && !"".equals(lastVersionKeyStr)) {
				lastVersionKey = Long.parseLong(lastVersionKeyStr);
			}
			
			LOGGER.info("[TaskAutoCollectSportsbookBetLogs] get last version key success:" + lastVersionKey);
			
			Optional<SportsbookMemberBetTicketInformationRespDto> betRecords = SportsbookAllServices.getInstance()
					.getBetRecords(lastVersionKey);
			if (betRecords.isPresent() && betRecords.get().getCode().equals("0")) {
				List<SportsbookMemberBetTicketInformationDetail> betDetails = betRecords.get().getData();
				if (betDetails != null && !betDetails.isEmpty()) {
					int count = 0;
					// sort by getId Comparator.nullsLast(Comparator.naturalOrder())
					betDetails.sort(Comparator.comparing(SportsbookMemberBetTicketInformationDetail::getId));

					for (SportsbookMemberBetTicketInformationDetail betDetail : betDetails) {
						lastVersionKey = betDetail.getId();
						if (dao.saveCmdBetLog(betDetail)) {
							count++;
						}
					}
					if (count == betDetails.size() && count > 0) {
						LOGGER.info("[TaskAutoCollectSportsbookBetLogs] update last version key: " + lastVersionKey);
						gameDaoService.updateLastTime("cmd", lastVersionKey + "");
					} else {
						LOGGER.info(
								"[TaskAutoCollectSportsbookBetLogs] there was an exception while saving record number: "
										+ count + ", of " + betDetails.size());
					}
				} else {
					LOGGER.info("[TaskAutoCollectSportsbookBetLogs] there's no records found for versionKey: "
							+ lastVersionKey);
				}
			} else {
				LOGGER.info("[TaskAutoCollectSportsbookBetLogs] call Sports book to get bet logs failed, versionKey: "
						+ lastVersionKey);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("[TaskAutoCollectSportsbookBetLogs] exception: " + e);
		}
	}

	
}
