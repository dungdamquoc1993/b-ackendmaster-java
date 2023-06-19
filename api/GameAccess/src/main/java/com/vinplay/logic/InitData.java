package com.vinplay.logic;

import org.apache.log4j.Logger;

import com.vinplay.usercore.utils.GameThirdPartyInit;

public class InitData {
	private static Logger logger = Logger.getLogger(InitData.class);
	public static int ids = 100000;

	public static void init() {
		initGame3rdConfig();
	}

	public static void initGame3rdConfig() {
		logger.info("isIbc2Down=" + isIbc2Down());
		logger.info("isAGDown=" + isAGDown());
		logger.info("isWMDown=" + isWMDown());
		logger.info("isCMDDown=" + isCMDDown());
		logger.info("isEbetDown=" + isEbetDown());
		logger.info("isSBODown=" + isSBODown());
		
		logger.info("finish rewrite maintain flag");
	}

	public static boolean isSBODown() {
		return GameThirdPartyInit.SBO_STATUS;
	}

	public static boolean isSGDown() {
		return GameThirdPartyInit.IBC2_STATUS;
	}

	public static boolean isEsportDown() {
		return GameThirdPartyInit.IBC2_STATUS;
	}

	public static boolean isEbetDown() {
		return GameThirdPartyInit.EBET_STATUS;
	}

	public static boolean isSADown() {
		return GameThirdPartyInit.IBC2_STATUS;
	}

	//active
	public static boolean isIbc2Down() {
		return GameThirdPartyInit.IBC2_STATUS;
	}

	public static boolean isAGDown() {
		return GameThirdPartyInit.AG_STATUS;
	}

	public static boolean isWMDown() {
		return GameThirdPartyInit.WM_STATUS;
	}
	
	public static boolean isCMDDown() {
		return GameThirdPartyInit.SPORTS_BOOK_MAINTENANCE_FLAG;
	}
	public synchronized static int getids() {
		ids++;
		if (ids > 999999) {
			ids = 100000;
		}
		return ids;
	}

}
