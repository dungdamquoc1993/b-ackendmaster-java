package com.vinplay.interfaces.ebet;


import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.vinplay.dao.ebet.EbetDao;
import com.vinplay.dao.impl.ebet.EbetDaoImpl;
import com.vinplay.dto.ebet.EbetUserItem;
import com.vinplay.logic.CommonMethod;
import com.vinplay.logic.InitData;


public class MemberEbetService extends BaseEbetService {
	private  static final Logger logger = Logger.getLogger(MemberEbetService.class);

	public static EbetUserItem authUsername(String ebetId, String accessToken) {
		String keySource = CommonMethod.decoding(accessToken);
		String decodeEbetId = CommonMethod.getNumber(keySource, CommonMethod.PREFIXID);
		EbetUserItem ebetUserItem = new EbetUserItem();
		
		if(accessToken == null || "".equals(accessToken)) {
			return ebetUserItem;
		}
		
		if(keySource == null || "".equals(keySource)) {
			return ebetUserItem;
		}

		if(!keySource.contains(CommonMethod.CHARACTERS) || !keySource.contains(CommonMethod.PREFIXID)) { //check access token validate
			ebetUserItem.setStatus(410);
			return ebetUserItem;
		}
		
		if("0".equals(decodeEbetId)) {
			return ebetUserItem;
		}
		
		if(!decodeEbetId.equals(ebetId)) { //ebetId from eBET server request
			ebetUserItem.setStatus(401);
			return ebetUserItem;
		}
		EbetDao ebetDao = new EbetDaoImpl();
		try {
			ebetUserItem = ebetDao.findEbetUserByEbetId(ebetId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(ebetUserItem == null || ebetUserItem.getLoginname() == null || ebetUserItem.getPassword() == null) { //check exist ebetId in ebetuser table
			ebetUserItem.setStatus(401);
			return ebetUserItem;
		}
		if(!keySource.contains(ebetUserItem.getTimestamp().toString())) { // equal timestamp
			ebetUserItem.setStatus(410);
			return ebetUserItem;
		}
		if(!keySource.contains(ebetUserItem.getPassword())) { //check validate access token with username + password
			ebetUserItem.setStatus(401);
			return ebetUserItem;
		}
		
		if(InitData.isEbetDown()) { //maintenance mode
			ebetUserItem.setStatus(505);
			return ebetUserItem;
		}
		ebetUserItem.setStatus(200);
		return ebetUserItem;
	}
	
}
