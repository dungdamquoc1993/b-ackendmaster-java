package com.vinplay.service.wm;

import com.vinplay.dto.ResultFormat;

public interface WmService {
	ResultFormat checkBalance(String nickName)  throws Exception;

	ResultFormat fundTransfer(String nickName, int direction, Double amount, String ip) throws Exception;

	ResultFormat login(String nickName) throws Exception;
	
}
