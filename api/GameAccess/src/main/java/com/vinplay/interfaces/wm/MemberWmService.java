package com.vinplay.interfaces.wm;

import org.apache.log4j.Logger;

import com.vinplay.dto.wm.CheckUserBalanceReqDto;
import com.vinplay.dto.wm.CheckUserBalanceRespDto;
import com.vinplay.dto.wm.CreateMemberReqDto;
import com.vinplay.dto.wm.CreateMemberRespeDto;
import com.vinplay.dto.wm.LogInReqDto;
import com.vinplay.dto.wm.LogInRespDto;

public class MemberWmService extends BaseWmService {
	private static final Logger LOGGER = Logger.getLogger(MemberWmService.class);

	public CreateMemberRespeDto createMember(CreateMemberReqDto reqDto) {
		System.out.println(gson.toJson(reqDto));
		String data = getData("MemberRegister", reqDto);
		CreateMemberRespeDto resDto = gson.fromJson(data, CreateMemberRespeDto.class);
		System.out.println(gson.toJson(resDto));
		return resDto;
	}

	public LogInRespDto login(String user , String password) throws Exception{
		LogInReqDto reqDto = new LogInReqDto();
		reqDto.setUser(user);
		reqDto.setPassword(password);
		reqDto.setLang(3);// Vietnamese
		String data = getData("SigninGame", reqDto);
		LOGGER.info("response LoginWM data = " + data);
		
		LogInRespDto resDto = gson.fromJson(data, LogInRespDto.class);
		return resDto;
	}


	public CheckUserBalanceRespDto CheckUserBalance(String loginName) {
		CheckUserBalanceReqDto reqDto = new CheckUserBalanceReqDto();
		reqDto.setSyslang(1);
		reqDto.setUser(loginName);
		
		String data = getData("GetBalance", reqDto);
		
		LOGGER.info("GetBalance WM response: " + data);
		
		CheckUserBalanceRespDto resDto = gson.fromJson(data, CheckUserBalanceRespDto.class);
		//System.out.println(gson.toJson(resDto));
		return resDto;
	}

	// public UpdateMemberRespDto updateMember(UpdateMemberReqDto reqDto) {
	// System.out.println(gson.toJson(reqDto));
	// String data = callAPI("UpdateMember", reqDto);
	// UpdateMemberRespDto resDto = gson.fromJson(data,
	// UpdateMemberRespDto.class);
	// System.out.println(gson.toJson(resDto));
	// return resDto;
	// }
	//
	// public KickUserRespDto KickUser(KickUserReqDto reqDto) {
	// System.out.println(gson.toJson(reqDto));
	// String data = callAPI("KickUser", reqDto);
	// KickUserRespDto resDto = gson.fromJson(data,
	// KickUserRespDto.class);
	// System.out.println(gson.toJson(resDto));
	// return resDto;
	// }
	//
	// public CheckIsOnlineRespDto CheckIsOnline(CheckIsOnlineReqDto reqDto) {
	// System.out.println(gson.toJson(reqDto));
	// String data = callAPI("CheckIsOnline", reqDto);
	// CheckIsOnlineRespDto resDto = gson.fromJson(data,
	// CheckIsOnlineRespDto.class);
	// System.out.println(gson.toJson(resDto));
	// return resDto;
	// }
	//

	//
	// public LockMemberRespDto LockMember(LockMemberReqDto reqDto) {
	// System.out.println(gson.toJson(reqDto));
	// String data = callAPI("LockMember", reqDto);
	// LockMemberRespDto resDto = gson.fromJson(data,
	// LockMemberRespDto.class);
	// System.out.println(gson.toJson(resDto));
	// return resDto;
	// }
	//
	// public UnLockMemberRespDto UnLockMember(UnLockMemberReqDto reqDto) {
	// System.out.println(gson.toJson(reqDto));
	// String data = callAPI("UnLockMember", reqDto);
	// UnLockMemberRespDto resDto = gson.fromJson(data,
	// UnLockMemberRespDto.class);
	// System.out.println(gson.toJson(resDto));
	// return resDto;
	// }
	//
	// public GetOnlineUserCountRespDto GetOnlineUserCount(GetOnlineUserCountReqDto reqDto) {
	// System.out.println(gson.toJson(reqDto));
	// String data = callAPI("GetOnlineUserCount", reqDto);
	// GetOnlineUserCountRespDto resDto = gson.fromJson(data, GetOnlineUserCountRespDto.class);
	// System.out.println(gson.toJson(resDto));
	// return resDto;
	// }
}
