package com.vinplay.interfaces.ebet;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import com.vinplay.dto.ebet.RechargeStatusReqDto;
import com.vinplay.dto.ebet.RechargeStatusRespDto;
import com.vinplay.logic.CommonMethod;
import com.vinplay.logic.InitServlet;
import com.vinplay.usercore.utils.GameThirdPartyInit;

import org.apache.log4j.Logger;


public class RechargestatusEbetService extends BaseEbetService {
	private static Logger logger = Logger.getLogger(RechargestatusEbetService.class);
	public RechargeStatusRespDto CheckRechargeStatus(RechargeStatusReqDto req) {
		logger.info(gson.toJson(req));
		String data = getData("rechargestatus", req);
		RechargeStatusRespDto resp = gson.fromJson(data, RechargeStatusRespDto.class);
		logger.info(gson.toJson(resp));
		return resp;
	}

	public RechargeStatusRespDto CheckRechargeStatus(String rechargeReqId) {
		RechargeStatusReqDto req = new RechargeStatusReqDto();
		req.setChannelId(GameThirdPartyInit.CHANNEL_ID);
		req.setRechargeReqId(rechargeReqId);
		String signature = "";
		try {
			byte[] data = rechargeReqId.getBytes("UTF-8");
			signature = CommonMethod.sign(data, GameThirdPartyInit.PRIVATE_KEY);
		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | SignatureException | UnsupportedEncodingException e) {
			
		}
		req.setSignature(signature);
		RechargeStatusRespDto res = CheckRechargeStatus(req);
		return res;
	}

}
