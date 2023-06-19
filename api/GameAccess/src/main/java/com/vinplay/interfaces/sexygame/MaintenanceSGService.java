package com.vinplay.interfaces.sexygame;

import org.apache.log4j.Logger;

import com.vinplay.logic.InitData;

public class MaintenanceSGService extends BaseSGService{
	private static final Logger logger = Logger.getLogger(MaintenanceSGService.class);

	/**
	 * Set maintenance mode
	 * 
	 * @param mode
	 * @return 'AG maintenance mode off.' if mode is true 
	 *         OR 'AG maintenance mode on.' if mode is false
	 */
//	public String setMaintenanceMode(Boolean mode) {
//		String msg = "";
//		if (!mode) {
//			InitData.isSGDown = false;
//			msg = "SG maintenance mode off.";
//		} else {
//			InitData.isSGDown = true;
//			msg = "SG maintenance mode on.";
//		}
//		logger.info(msg);
//		System.out.print("SG maintenance mode: " + InitData.isAGDown);
//		return msg;
//	}
}
