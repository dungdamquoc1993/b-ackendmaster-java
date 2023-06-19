package com.vinplay.run;

import org.apache.log4j.Logger;

import com.vinplay.dto.ibc2.CreateMemberReqDto;
import com.vinplay.dto.ibc2.CreateMemberRespeDto;
import com.vinplay.interfaces.ibc2.MemberIbc2Service;
import com.vinplay.logic.CommonMethod;
import com.vinplay.logic.InitData;
import com.vinplay.service.ibc2.Ibc2Dao;
import com.vinplay.service.ibc2.impl.Ibc2DaoImpl;
import com.vinplay.usercore.utils.GameThirdPartyInit;

public class TaskAutoCreateUserIbc2 extends java.util.TimerTask{

	/**
	 * 1000 for production, test for 2 or 3 only
	 */
	private static final int ODDS_TYPE = 1;
	private static final double MAX_TRANSFER = 1000000;
	private static final double MIN_TRANSFER = 100; // 100 for ibc platform, 100.000 for lottery platform
	private static final long TIME_OUT = 5*60*1000; // 5 minutes time out
	private static final String PLAYER_PREFIX = GameThirdPartyInit.IBC2_PLAYER_PREFIX;
	private static final String OPERATORID = GameThirdPartyInit.IBC2_OPERATORID;
	private static final int CURRENCYID = Integer.parseInt(GameThirdPartyInit.IBC2_CURRENTCY_ID);
	private static final int START_IBCID = Integer.parseInt(GameThirdPartyInit.IBC2_START_IBCID);
	private Integer temp_maxUserId = 0;

	private static final Logger LOGGER = Logger.getLogger(TaskAutoCreateUserIbc2.class);
	private Ibc2Dao ibcDao = new Ibc2DaoImpl();
	@Override
	public void run() {
		boolean isNotMaintain = !InitData.isIbc2Down();
		String h = CommonMethod.GetCurDate("HH");
		if (GameThirdPartyInit.CREATE_USER_TIME.equals(h)) {
			if(isNotMaintain) {
				try {
					//Get Max ibcId
					int maxIbcId = 0;
					int maxIbcUserMapping = 0;
					String suffix = GameThirdPartyInit.IBC2_SUFFIX;
					try{
						maxIbcId = ibcDao.getMaxFieldValue("ibccountid"); 
						maxIbcUserMapping = ibcDao.countUserRemain(null);
						if(maxIbcId == 0){
							maxIbcId = START_IBCID;
							LOGGER.info("[CreateUserIbcService]" + " init ibc id from: " + maxIbcId);
						} 
					}catch(Exception ex){
						LOGGER.error("CountUserIbc2", ex);
						return;
					}
					LOGGER.info("[TaskAutoCreateUserIbc2]" + " max ibcid: " + maxIbcId);
					LOGGER.info("[TaskAutoCreateUserIbc2]" + " number of remain ibcid: " + maxIbcUserMapping);
					if(maxIbcUserMapping < GameThirdPartyInit.NUM_ACCOUNT_REG) {
						int count = 1;
						int increaseId = 1;
						String ibcId = "0";
						int ibccountid = 0;
						if(maxIbcId < temp_maxUserId) {
							maxIbcId = temp_maxUserId;
							LOGGER.info("[TaskAutoCreateUserIbc2]" + " max ibcid: " + maxIbcId);
							LOGGER.info("[TaskAutoCreateUserIbc2]" + " temp_maxUserId: " + temp_maxUserId);
						}
						//catch exception exit loop in case ibc platfom maintance!
						long startTime = System.currentTimeMillis(); //fetch starting time
						while(count <= (GameThirdPartyInit.NUM_ACCOUNT_REG - maxIbcUserMapping) && (System.currentTimeMillis()-startTime) < TIME_OUT) {
							//call reg new member from ibc service
							MemberIbc2Service createMember = new MemberIbc2Service();
							CreateMemberReqDto reqDto = new CreateMemberReqDto();
							reqDto.setMaxtransfer(MAX_TRANSFER);
							reqDto.setMintransfer(MIN_TRANSFER);
							reqDto.setOperatorId(OPERATORID);
							reqDto.setCurrency(CURRENCYID);
							reqDto.setOddstype(ODDS_TYPE);
							ibcId = PLAYER_PREFIX + Integer.toString(maxIbcId + increaseId) + suffix;
							reqDto.setVendor_member_id(ibcId);  // player name : ekhelo_1, ekhelo_2, ekhelo_3, ekhelo_4, ekhelo_5....
							reqDto.setUsername(PLAYER_PREFIX + Integer.toString(maxIbcId + increaseId) + suffix);
							CreateMemberRespeDto respone = createMember.createMember(reqDto);
							if(respone == null) { //error occur!
								LOGGER.info("Register error!");
								LOGGER.error("[TaskAutoCreateUserIbc2]" + " create failed: " + ibcId + " (reponse null)");
							} else if(respone.getError_code() != 0){
								LOGGER.info("Register error!");
								LOGGER.error("[TaskAutoCreateUserIbc2]" + " create failed: " + ibcId + " (createMember false)");
							} else {
								//Insert account information in to VN Lottery DB
								//INSERT INTO IBC2USER(ibcid,loginname,max_transfer,min_transfer,oddtype,IBCCOUNTID) VALUES(p_ibc_id,null,p_max_transfer,p_min_transfer,p_odds_type,p_ibc_count_id);
								
								try{
									boolean isInsert = ibcDao.insertIbc2User(ibcId+"", MAX_TRANSFER, MIN_TRANSFER, ODDS_TYPE, maxIbcId + increaseId ,null);
									if(isInsert){
										count ++;
									}else{
										LOGGER.error("[TaskAutoCreateUserIbc2] Insert ibc id : " + ibcId + " to ibcuser tbl fail!");
									}
								}catch(Exception ex){
									LOGGER.error("[TaskAutoCreateUserIbc2] p_IBC_GeneralIbcUser", ex);
									return;
								}
							}
							increaseId ++;
						}
						LOGGER.info("[TaskAutoCreateUserIbc2] exec in loop while : " + (System.currentTimeMillis() - startTime)/1000 + " minutes");
						temp_maxUserId = ibccountid;
						LOGGER.info("[TaskAutoCreateUserIbc2]" + " temp_maxUserId: " + temp_maxUserId);
						LOGGER.info("[TaskAutoCreateUserIbc2]" + " number registed : " + (count - 1));
					}
				} catch(Exception ex){
					LOGGER.error("[TaskAutoCreateUserIbc2] TaskAutoCreateUserIBC2", ex);
				}
			}
		}
	}
	public Integer getTemp_maxUserId() {
		return temp_maxUserId;
	}

	public void setTemp_maxUserId(Integer temp_maxUserId) {
		this.temp_maxUserId = temp_maxUserId;
	}
}
