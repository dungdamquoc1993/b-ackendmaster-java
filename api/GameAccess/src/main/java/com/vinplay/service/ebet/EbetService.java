package com.vinplay.service.ebet;

import com.vinplay.dto.ResultFormat;

public interface EbetService {
	public ResultFormat lunchGame(String loginname) throws Exception;

	public ResultFormat getBalance(String loginName) throws Exception;
	
	public ResultFormat transfer(String loginname, Double amount, String ip ,Integer direction) throws Exception;
}
