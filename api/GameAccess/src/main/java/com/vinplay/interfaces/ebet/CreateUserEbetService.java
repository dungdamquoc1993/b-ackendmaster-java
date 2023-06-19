package com.vinplay.interfaces.ebet;

import org.apache.log4j.Logger;

import com.vinplay.dto.ebet.CreateUserReqDto;
import com.vinplay.dto.ebet.CreateUserRespDto;

public class CreateUserEbetService extends BaseEbetService {
	private static final Logger logger = Logger.getLogger(CreateUserEbetService.class);

	public CreateUserRespDto CreateUser(CreateUserReqDto req) {
		logger.info(gson.toJson(req));
		String data = getData("syncuser", req);
		CreateUserRespDto resp = gson.fromJson(data, CreateUserRespDto.class);
		logger.info(gson.toJson(resp));
		return resp;
	}
}
