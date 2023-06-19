package com.vinplay.interfaces.ibc2;

import com.vinplay.dto.ibc2.*;
import org.apache.log4j.Logger;

public class MemberIbc2Service extends BaseIbc2Service {
	private  static final Logger logger = Logger.getLogger(MemberIbc2Service.class);


	public LogInRespDto login(String username) {
		LogInReqDto reqDto = new LogInReqDto();
		reqDto.setVendor_member_id(username);
		reqDto.setDomain(Domain);
		String data = callAPI("LogIn", reqDto);
		LogInRespDto resDto = gson.fromJson(data,LogInRespDto.class);
		logger.info(gson.toJson(resDto));
		return resDto;
	}

	public CreateMemberRespeDto createMember(CreateMemberReqDto reqDto) {
		logger.info(gson.toJson(reqDto));
		String data = callAPI("CreateMember", reqDto);
		CreateMemberRespeDto resDto = gson.fromJson(data,
				CreateMemberRespeDto.class);
		logger.info(gson.toJson(resDto));
		return resDto;
	}


	public UpdateMemberRespDto updateMember(UpdateMemberReqDto reqDto) {
		logger.info(gson.toJson(reqDto));
		String data = callAPI("UpdateMember", reqDto);
		UpdateMemberRespDto resDto = gson.fromJson(data,
				UpdateMemberRespDto.class);
		logger.info(gson.toJson(resDto));
		return resDto;
	}

	public KickUserRespDto KickUser(KickUserReqDto reqDto) {
		logger.info(gson.toJson(reqDto));
		String data = callAPI("KickUser", reqDto);
		KickUserRespDto resDto = gson.fromJson(data,
				KickUserRespDto.class);
		logger.info(gson.toJson(resDto));
		return resDto;
	}

	public CheckIsOnlineRespDto CheckIsOnline(CheckIsOnlineReqDto reqDto) {
		logger.info(gson.toJson(reqDto));
		String data = callAPI("CheckIsOnline", reqDto);
		CheckIsOnlineRespDto resDto = gson.fromJson(data,
				CheckIsOnlineRespDto.class);
		logger.info(gson.toJson(resDto));
		return resDto;
	}

	public Ibc2CheckUserBalanceRespDto CheckUserBalance(Ibc2CheckUserBalanceReqDto reqDto) {
		logger.info(gson.toJson(reqDto));
		reqDto.setWallet_id(1);
		String data = callAPI("CheckUserBalance", reqDto);
		Ibc2CheckUserBalanceRespDto resDto = gson.fromJson(data,
				Ibc2CheckUserBalanceRespDto.class);
		logger.info(gson.toJson(resDto));
		return resDto;
	}

	public LockMemberRespDto LockMember(LockMemberReqDto reqDto) {
		logger.info(gson.toJson(reqDto));
		String data = callAPI("LockMember", reqDto);
		LockMemberRespDto resDto = gson.fromJson(data,
				LockMemberRespDto.class);
		logger.info(gson.toJson(resDto));
		return resDto;
	}

	public UnLockMemberRespDto UnLockMember(UnLockMemberReqDto reqDto) {
		logger.info(gson.toJson(reqDto));
		String data = callAPI("UnLockMember", reqDto);
		UnLockMemberRespDto resDto = gson.fromJson(data,
				UnLockMemberRespDto.class);
		logger.info(gson.toJson(resDto));
		return resDto;
	}

	public GetOnlineUserCountRespDto GetOnlineUserCount(GetOnlineUserCountReqDto reqDto) {
		logger.info(gson.toJson(reqDto));
		String data = callAPI("GetOnlineUserCount", reqDto);
		GetOnlineUserCountRespDto resDto = gson.fromJson(data,
				GetOnlineUserCountRespDto.class);
		logger.info(gson.toJson(resDto));
		return resDto;
	}
}
