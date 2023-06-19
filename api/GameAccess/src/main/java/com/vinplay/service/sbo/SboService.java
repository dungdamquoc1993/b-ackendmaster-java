/**
 * Archie
 */
package com.vinplay.service.sbo;

import com.vinplay.dto.ResultFormat;

/**
 * @author Archie
 *
 */
public interface SboService {
	public ResultFormat lunchGame(String loginname, String gameCode) throws Exception;

	public ResultFormat getBalance(String loginName) throws Exception;
	
	public ResultFormat transfer(String loginname, Double amount, String ip ,Integer direction) throws Exception;
}
