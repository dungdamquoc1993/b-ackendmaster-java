package com.vinplay.interfaces.sportsbook;

import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vinplay.dto.BaseResponseDto;
import com.vinplay.dto.ResultFormat;
import com.vinplay.dto.sportsbook.SportsbookFundTransferReqDto;
import com.vinplay.dto.sportsbook.SportsbookFundTransferRespDto;
import com.vinplay.dto.sportsbook.SportsbookFundTransferResponse;
import com.vinplay.dto.sportsbook.SportsbookUserBalanceRespDto;
import com.vinplay.item.SportsbookItem;
import com.vinplay.logic.CommonMethod;
import com.vinplay.logic.InitData;
import com.vinplay.logic.InitServlet;
import com.vinplay.service.GamesCommonService;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.utils.ThirdpartyCodes;
import com.vinplay.utils.ThirdpartyConstant;
import com.vinplay.utils.ThirdpartyMessages;
import com.google.gson.Gson;

/**
 * Sports book fund transfer services
 * 
 */
public class SportsbookFundTransferServices {

	private static final Logger LOGGER = LoggerFactory.getLogger(SportsbookFundTransferServices.class);

	private static SportsbookFundTransferServices INSTANCE;
	private static Gson gson;

	private SportsbookFundTransferServices() {
	}

	public static SportsbookFundTransferServices getInstance() {
		if (Objects.isNull(INSTANCE)) {
			INSTANCE = new SportsbookFundTransferServices();
			gson = new Gson();
		}
		return INSTANCE;
	}
	
	/**
	 * Sports book deposit money services
	 * 
	 * @param loginName
	 * @param amount
	 * @param ip
	 * @return
	 */
	
}
