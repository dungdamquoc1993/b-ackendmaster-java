package com.vinplay.service.ibc2;

import com.vinplay.dto.ibc2.BetDetail;
import com.vinplay.dto.ibc2.common.CheckBalanceDataRespDto;
import com.vinplay.utils.BaseResponse;

public interface Ibc2Dao {
	public BaseResponse<Double> withdrawAll();
	
	Integer getMaxFieldValue(String fieldName);// IBCCOUNTID

	String findIbcIdUserByNickName(String nickName);

	Integer countUserRemain(String nickName);

	boolean insertIbc2User(String ibcid, Double max_transfer, Double min_transfer, Integer oddtype, Integer IBCCOUNTID,
			String nickName);

	boolean updateUser(String ibcid, String nickName);

	boolean saveLogs(BetDetail reg);

	BaseResponse loginIbc(String nickname);

	BaseResponse<CheckBalanceDataRespDto> getBalance(String nickName);
	
	BaseResponse<Double> transfer(String nickName, Integer direction, Double amount, String ip);

}
