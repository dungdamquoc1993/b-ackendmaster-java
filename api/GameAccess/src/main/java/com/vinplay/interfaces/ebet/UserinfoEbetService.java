package com.vinplay.interfaces.ebet;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.vinplay.dao.ebet.EbetDao;
import com.vinplay.dao.impl.ebet.EbetDaoImpl;
import com.vinplay.dto.ebet.EbetUserItem;
import com.vinplay.dto.ebet.UserInfomationReqDto;
import com.vinplay.dto.ebet.UserInfomationRespDto;
import com.vinplay.logic.CommonMethod;
import com.vinplay.usercore.utils.GameThirdPartyInit;


public class UserinfoEbetService extends BaseEbetService {
	
	private static final Logger logger = Logger.getLogger(UserinfoEbetService.class);

	public UserInfomationRespDto GetUserInfo(UserInfomationReqDto req) {
		String data = getData("userinfo", req);
		UserInfomationRespDto resp = gson.fromJson(data, UserInfomationRespDto.class);
		logger.info(gson.toJson(resp));
		return resp;
	}
	 
	public UserInfomationRespDto getUserInfo(String username , String ebetid) {
		UserInfomationReqDto req = new UserInfomationReqDto();
		req.setUsername(ebetid);
		req.setChannelId(GameThirdPartyInit.CHANNEL_ID);
		Integer ts  = (int) (System.currentTimeMillis() / 1000L);
		req.setTimestamp(ts);
		String signature = "";
		try {
			byte[] data = ebetid.concat(ts.toString()).getBytes(StandardCharsets.UTF_8);
			signature = CommonMethod.sign(data, GameThirdPartyInit.PRIVATE_KEY);
		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | SignatureException e) {
			logger.error("ex", e);
		}
		req.setSignature(signature);
		return GetUserInfo(req);
	}

	public UserInfomationRespDto GetUserInfo(String username) throws SQLException {
		UserInfomationReqDto req = new UserInfomationReqDto();
		UserInfomationRespDto res = new UserInfomationRespDto();
		EbetDao ebetService = new EbetDaoImpl();
		EbetUserItem userItem = ebetService.mappingUserEbet(username);
		if(userItem==null) return null;
		req.setUsername(userItem.getEbetid());
		req.setChannelId(GameThirdPartyInit.CHANNEL_ID);
		Integer ts  = (int) (System.currentTimeMillis() / 1000L);
		req.setTimestamp(ts);
		String signature = "";
		if(userItem.getEbetid() != null){
			try {
				byte[] data = userItem.getEbetid().concat(ts.toString()).getBytes(StandardCharsets.UTF_8);
				signature = CommonMethod.sign(data, GameThirdPartyInit.PRIVATE_KEY);
			} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | SignatureException e) {
				logger.error("ex", e);
			}
			req.setSignature(signature);
			res = GetUserInfo(req);
		}else{
			res.setStatus(4037);
		}
		return res;
	}

}
