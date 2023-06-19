package com.vinplay.service.ag;

import com.vinplay.dto.ResultFormat;

public interface AgService {
	ResultFormat forwardGame(String nickName) throws Exception;

	ResultFormat queryPlayer(String userName) throws Exception;

	ResultFormat depositPlayerMoney(String userName, Double amount, String ip) throws Exception;

	ResultFormat withdrawPlayerMoney(String userName, Double amount, String ip) throws Exception;

	//ResultFormat getUserInfo(String userName) throws Exception;
}
