package com.vinplay.run;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import org.apache.log4j.Logger;

import com.vinplay.dao.ebet.EbetDao;
import com.vinplay.dao.impl.ebet.EbetDaoImpl;
import com.vinplay.dto.ebet.CreateUserReqDto;
import com.vinplay.dto.ebet.CreateUserRespDto;
import com.vinplay.interfaces.ebet.CreateUserEbetService;
import com.vinplay.logic.CommonMethod;
import com.vinplay.logic.InitData;
import com.vinplay.usercore.utils.GameThirdPartyInit;

 
public class TaskAutoCreateUserEbet extends java.util.TimerTask{
	private static final long TIME_OUT = 5*60*1000; // 5 minutes time out
	private static final Integer START_EBET_PRO  = 1;//from 100 for test account and from 1000 for real account
	private Integer temp_maxUserId = 0;
	
	private static final Logger LOGGER = Logger.getLogger(TaskAutoCreateUserEbet.class);
	private EbetDao ebetDao =new EbetDaoImpl();

	
	@Override
	public void run() {
		boolean isNotMaintain = !InitData.isEbetDown();
		//LOGGER.info("[TaskAutoCreateUserEbet] isNotMaintain=" + isNotMaintain);
		String h = CommonMethod.GetCurDate("HH");
		if ("00".equals(h) && isNotMaintain) {//
			try {
				LOGGER.info("CREATEUSEREBET Start job create user EBET");
				//Get Max Id
				int maxId = 0;
				int maxUserMapping = 0;
				try {
					List<Integer> lstEbetResult = ebetDao.maxEbetUser();

					if (lstEbetResult != null) {
						maxId = lstEbetResult.get(0);
						maxUserMapping = lstEbetResult.get(1);
					} else {
						maxId = START_EBET_PRO;
					}
				} catch (Exception e) {
					LOGGER.error(e);
					return;
				}
				LOGGER.info("[CreateUserEbetService]" + " max ebetid: " + maxId);
				LOGGER.info("[CreateUserEbetService]" + " number of remain ebetid: " + maxUserMapping);
				if(maxUserMapping < GameThirdPartyInit.NUM_ACCOUNT_REG) {
					int count = 1;
					int increaseId = 1;
					int ebetCountId = 0;
					String ebetId = "0";
					if(maxId < temp_maxUserId) {
						maxId = temp_maxUserId;
						LOGGER.info("[CreateUserEbetService]" + " max ebetid: " + maxId);
						LOGGER.info("[CreateUserEbetService]" + " temp_maxUserId: " + temp_maxUserId);
					}
					//catch exception exit loop in case ibc platfom maintance!
					long startTime = System.currentTimeMillis(); //fetch starting time
					while(count <= (GameThirdPartyInit.NUM_ACCOUNT_REG-maxUserMapping) && (System.currentTimeMillis()-startTime) < TIME_OUT) {
						//call reg new member from ibc service
						ebetCountId = maxId + increaseId;
						CreateUserEbetService createMember = new CreateUserEbetService();
						CreateUserReqDto req = new CreateUserReqDto();
						ebetId = Integer.toString(maxId + increaseId);
						String g_pass = CommonMethod.randomString(16);
						req.setUsername(ebetId);
						req.setChannelId(GameThirdPartyInit.CHANNEL_ID);
						req.setLang(6);
						String signature = "";
						try {
							byte[] data = ebetId.getBytes("UTF-8");
							signature = CommonMethod.sign(data, GameThirdPartyInit.PRIVATE_KEY);
							LOGGER.info(signature);
						} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | SignatureException | UnsupportedEncodingException e) {
							LOGGER.error("ex", e);
						}
						req.setSignature(signature);
						req.setSubChannelId(0);
						CreateUserRespDto respone = createMember.CreateUser(req);
						if(respone == null) { //error occur!
							LOGGER.error("[CreateUserEbetService]" + " create failed: " + ebetId + " (reponse null)");
						} else if(respone.getStatus() != 200){
							LOGGER.error("[CreateUserEbetService]" + " create failed: " + ebetId + "  error code: " + respone.getStatus());
						} else {
							boolean isInsert = ebetDao.generateEbetUser(ebetId, ebetCountId, g_pass);
							if (isInsert) {
								count++;
							} else {
								LOGGER.error("[TaskAutoCreateEbetUser] Insert ebetId : " + ebetId + " to EbetUser table failed!");
							}
						}
						increaseId++;
					}
					LOGGER.info("[CreateUserEbetService] exec in loop while : " + (System.currentTimeMillis() - startTime)/1000 + " minutes");
					temp_maxUserId = Integer.parseInt(ebetId);
					LOGGER.info("[CreateUserEbetService]" + " temp_maxUserId: " + temp_maxUserId);
					LOGGER.info("[CreateUserEbetService]" + " number registed : " + (count - 1));
				}
			} catch(Exception ex){
				LOGGER.error("[CreateUserEbetService] TaskAutoCreateUserEbet", ex);
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
