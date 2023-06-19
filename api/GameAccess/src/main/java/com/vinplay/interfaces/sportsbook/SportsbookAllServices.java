/**
 * Archie
 */
package com.vinplay.interfaces.sportsbook;

import java.util.Base64;
import java.util.Optional;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.vinplay.dto.sportsbook.SportsbookCreateMemberRespDto;
import com.vinplay.dto.sportsbook.SportsbookFundTransferReqDto;
import com.vinplay.dto.sportsbook.SportsbookFundTransferRespDto;
import com.vinplay.dto.sportsbook.SportsbookLoginBase64;
import com.vinplay.dto.sportsbook.SportsbookMemberBetTicketInformationRespDto;
import com.vinplay.dto.sportsbook.SportsbookUserBalanceReqDto;
import com.vinplay.dto.sportsbook.SportsbookUserBalanceRespDto;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.utils.MD5Utils;
import com.vinplay.utils.ThirdpartyConstant;


/**
 * @author Archie
 *
 */
public class SportsbookAllServices {

	private static final Logger LOGGER = Logger.getLogger(SportsbookAllServices.class);

	private static final Gson GSON = new Gson();

	private static class LazySingleton {
		static final SportsbookAllServices INSTANCE = new SportsbookAllServices();
	}

	public static SportsbookAllServices getInstance() {
		return LazySingleton.INSTANCE;
	}

	/**
	 * Register new member for Sports book platform
	 * 
	 * @param reqDto
	 * @return
	 */
	public Optional<SportsbookCreateMemberRespDto> createSportsbookMember(String sportsBookId) {
		String data = "";
		try {
			data = SportsbookBaseServices.getInstance().createUser(sportsBookId);
		} catch (Exception e) {
			data = "";
		}
		LOGGER.info("Sportsbook createSportsbookMember response: " + data);
		SportsbookCreateMemberRespDto response = GSON.fromJson(data, SportsbookCreateMemberRespDto.class);
		LOGGER.info("Sportsbook createSportsbookMember response after converted: " + data);
		return Optional.ofNullable(response);
	}

	/**
	 * Check user balance
	 * 
	 * @param reqDto
	 * @return
	 */
	public Optional<SportsbookUserBalanceRespDto> checkUserBalance(SportsbookUserBalanceReqDto reqDto) {
		LOGGER.info("Sportsbook checkUserBalance request: " + GSON.toJson(reqDto));
		String data = null;
		try {
			data = SportsbookBaseServices.getInstance().postData(reqDto, ThirdpartyConstant.CMD.GET_BALANCE_METHOD);
		} catch (Exception e) {
			LOGGER.error(e);
			data ="";
		}
		LOGGER.info("Sportsbook checkUserBalance response: " + data);
		SportsbookUserBalanceRespDto response = GSON.fromJson(data, SportsbookUserBalanceRespDto.class);
		return Optional.ofNullable(response);
	}

	/**
	 * Encode token based on loginName, sportsbookUserName
	 * 
	 * @param loginName
	 * @param sportsbookUserName
	 * @return
	 */
	public Optional<String> sportsbookLoginEncode(String loginName, String sportsbookUserName) {
		LOGGER.info("sportsbookLoginEncode encode loginName: " + loginName);
		String token = null;
		SportsbookLoginBase64 sportsBook = new SportsbookLoginBase64();
		sportsBook.setLoginName(loginName);
		sportsBook.setTimeStamp(System.currentTimeMillis());
		// encode sportsBookUserName, ourSecretKey using md5
		String md5SportsBookUserName = MD5Utils.generateKey(sportsbookUserName);
		String md5OurSecretKey = MD5Utils.generateKey(GameThirdPartyInit.SPORTS_BOOK_OUR_SECRET_KEY);

		sportsBook.setSportsbook_userName(md5SportsBookUserName);
		sportsBook.setSecret_key(md5OurSecretKey);
		String tokenJson = GSON.toJson(sportsBook);
		byte[] actualByte = Base64.getEncoder().encode(tokenJson.getBytes());
		token = new String(actualByte);
		LOGGER.info("sportsbookLoginEncode token is: " + token);
		return Optional.ofNullable(token);
	}


	/**
	 * Decode token
	 * 
	 * @param token
	 * @return
	 */
	public Optional<SportsbookLoginBase64> sportsbookLoginDecode(String token) {
		LOGGER.info("sportsbookLoginDecode token: " + token);
		SportsbookLoginBase64 sportsbookLoginBase64 = null;
		byte[] actualByte = Base64.getDecoder().decode(token.getBytes());
		String tokenDecode = new String(actualByte);
		sportsbookLoginBase64 = GSON.fromJson(tokenDecode, SportsbookLoginBase64.class);
		LOGGER.info("sportsbookLoginDecode decode success: " + tokenDecode);
		return Optional.ofNullable(sportsbookLoginBase64);
	}

	/**
	 * Fund transfer money
	 * 
	 * @param reqDto
	 * @return
	 */
	public Optional<SportsbookFundTransferRespDto> fundTransfer(SportsbookFundTransferReqDto reqDto) {
		LOGGER.info("Sportsbook fund transfer request: " + GSON.toJson(reqDto));
		String data = null;
		try {
			data = SportsbookBaseServices.getInstance().postData(reqDto, ThirdpartyConstant.CMD.BALANCE_TRANSFER);
		} catch (Exception e) {
			LOGGER.error(e);
			data ="";
		}
		LOGGER.info("Sportsbook fund transfer response: " + data);
		SportsbookFundTransferRespDto response = GSON.fromJson(data, SportsbookFundTransferRespDto.class);
		LOGGER.info("Sportsbook fund transfer response after converted: " + GSON.toJson(response));
		return Optional.ofNullable(response);
	}

	/**
	 * Query bet records information
	 * 
	 * @param versionKey
	 * @return
	 */
	public Optional<SportsbookMemberBetTicketInformationRespDto> getBetRecords(long versionKey) {
		LOGGER.info("Sportsbook getBetRecords request: " + versionKey);
		String data = null;
		try {
			data = SportsbookBaseServices.getInstance().queryBetRecords(versionKey,
					ThirdpartyConstant.CMD.BET_RECORD);
		} catch (Exception e) {
			LOGGER.error(e);
			data ="";
		}
		LOGGER.info("Sportsbook getBetRecords response: " + data);
		SportsbookMemberBetTicketInformationRespDto betDetail = GSON.fromJson(data,
				SportsbookMemberBetTicketInformationRespDto.class);
		LOGGER.info("Sportsbook getBetRecords response after converted: " + GSON.toJson(betDetail));
		return Optional.ofNullable(betDetail);
	}

}
