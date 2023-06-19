package com.vinplay.service.sa;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.vinplay.dto.ResultFormat;
import com.vinplay.dto.sa.SALoginDto;
import com.vinplay.dto.sa.SAUserInfoResp;
import com.vinplay.interfaces.sa.MemberSAService;
import com.vinplay.interfaces.sa.SAUtils;
import com.vinplay.utils.Constants;
/**
 * @author Archie
 *
 */
public class SaGameAccessService {
	private static final Logger logger = Logger.getLogger(SaGameAccessService.class);
	
	
	public static ResultFormat LoginOrCreatePlayer(ObjectInputStream objInStream) throws Exception {
		String userName = (String) objInStream.readObject();
		String ip = ((String) objInStream.readObject());
		List<Object> listObj = new ArrayList<Object>();
		logger.info("SA_LoginOrCreatePlayer request: " + "Username-" + userName + " | " + "PlayerHostAddress-" + ip);
		SALoginDto user = MemberSAService.loginMapping(userName, ip);
		if (user != null) {
			listObj.add(user);
			return new ResultFormat(Constants.SUCCESS, Constants.SUCCESS_MESSAGE, listObj);
		} else {
			listObj.add("");
			return new ResultFormat(Constants.FAIL, Constants.ERROR_MESSAGE, listObj);
		}
	}

	public static ResultFormat SA_GetPlayerInfo(ObjectInputStream objInStream)throws Exception{
		ResultFormat rf = new ResultFormat();
		String userName = (String) objInStream.readObject();
		String playerCode = ((String) objInStream.readObject());
		
		List<Object> listObj = new ArrayList<Object>();
		//validation
		if(playerCode == null || "".equals(playerCode)) {
			logger.error("SA_GetPlayerInfo playerCode  is null or empty");
			return new ResultFormat("SA_GetPlayerInfo playerCode is null or empty");
		}
		
		if (userName == null || "".equals(userName)) {
			logger.error("SA_GetPlayerInfo userName  is null or empty");
			return new ResultFormat("SA_GetPlayerInfo loginName is null or empty");
		}
		logger.info("SA_GetPlayerInfo request: " + "Username-" + userName + "playercode-" + playerCode);
		//CALL API 
		SAUserInfoResp data = SAUtils.getSAInfo(playerCode);
		if (data != null) {
			listObj.add(data);
			rf.setRes(0);
			rf.setList(listObj);
		} else {
			rf.setRes(1);
			listObj.add("");
			rf.setList(listObj);
			rf.setMsg("Thất bại! Liên hệ CSKH");
		}
		return rf;
	}
	
}
