package com.vinplay.interfaces.sexygame;

import java.util.List;

import com.vinplay.dto.sg.BaseRespDto;
import com.vinplay.dto.sg.CreateMemberDto;
import com.vinplay.dto.sg.GetBalanceReqDto;
import com.vinplay.dto.sg.GetBalanceRespDto;
import com.vinplay.dto.sg.LoginAndLaunchGame;
import com.vinplay.dto.sg.LoginDto;
import com.vinplay.dto.sg.LogoutDto;
import com.vinplay.dto.sg.LogoutRespDto;
import com.vinplay.dto.sg.LonginRespDto;
import com.vinplay.dto.sg.SGLoginResp;
import com.vinplay.item.SGUserItem;
import com.vinplay.logic.InitData;
import com.vinplay.logic.InitServlet;


public class MemberSGService extends BaseSGService {

	public SGLoginResp login(String loginName,String isMobileLogin) {
		SGUserItem sguser = mappingUserSG(loginName);
		LonginRespDto resDto = new LonginRespDto();
		SGLoginResp resp = new SGLoginResp();
		if (sguser == null) {
			resDto.setStatus("1");
			resDto.setDesc("User not found!");
		} else {
			LoginDto reqDto = new LoginDto();
			reqDto.setUserId(sguser.getSgid());
			reqDto.setIsMobileLogin(isMobileLogin);
			reqDto.setExternalURL("https://mig8vn.com");
			String data = callAPI("login", reqDto);
			resDto = gson.fromJson(data, LonginRespDto.class);
			resp.setLoginname(sguser.getLoginname());
			resp.setSgid(sguser.getSgid());
			resp.setGameUrl(resDto.getUrl());
		}
		return resp;
	}

	public BaseRespDto createMember(CreateMemberDto reqDto) {
		String data = callAPI("createMember", reqDto);
		BaseRespDto resDto = gson.fromJson(data, BaseRespDto.class);
		return resDto;
	}

	
	public LogoutRespDto logout(String loginName) {
		LogoutDto reqDto = new LogoutDto();
		SGUserItem sguser = mappingUserSG(loginName);
		LogoutRespDto res = new LogoutRespDto();
		if (sguser == null) {
			res.setStatus("1");
			res.setDesc("User not found!");
		} else {
			reqDto.setUserIds(sguser.getSgid());
			String data = callAPI("logout", reqDto);
			res = gson.fromJson(data, LogoutRespDto.class);
		}

		return res;
	}

	public GetBalanceRespDto getBalance(String loginName) {
		GetBalanceReqDto reqDto = new GetBalanceReqDto();
		GetBalanceRespDto resDto = new GetBalanceRespDto();
		SGUserItem sguser = mappingUserSG(loginName);
		if(sguser == null) {
			resDto.setStatus("1");
			resDto.setDesc("User not found!");
		}else {
			reqDto.setUserIds(sguser.getSgid());
			String data = callAPI("getBalance", reqDto);
			resDto = gson.fromJson(data, GetBalanceRespDto.class);
		}
		return resDto;
	}

	public SGUserItem mappingUserSG(String loginName) {
		if(loginName == null || loginName.length()<3){
			logger.info("mappingUserSG Wrong loginname  " + loginName);
			return null;
		}
//		DbProc dp = null;
//		SGUserItem sgUserItem = null;
//		try {
//			dp = new DbProc();
//			dp.setSql("call PG_GAME_ACCESS.p_AG_MappingSGUser(?,?,?,?)");
//			dp.setOutParam(1, OracleTypes.CURSOR);
//			dp.setOutParam(2, OracleTypes.NUMBER);
//			dp.setOutParam(3, OracleTypes.VARCHAR);
//			dp.setString(4, loginName);
//			dp.execute();
//			int res = Integer.parseInt(dp.getObject(2).toString());
//			if (res == 0) {
//				List<SGUserItem> list = dp.getResult(SGUserItem.class, 1);
//				sgUserItem = list.get(0);
//			} else if (res == 1) {
//					logger.info("Thất bại:  " + dp.getObject(3).toString());
//			}
//		} catch (Exception ex) {
//				ex.printStackTrace();
//				logger.error("p_AG_MappingSGUser", ex);
//		} finally {
//			dp.Close();
//		}
//		return sgUserItem;
		return null;
	}

	public static void main(String[] args) {
		MemberSGService service = new MemberSGService();
//		CreateMemberDto reqDto1 = new CreateMemberDto();
//		reqDto1.setUserId("testplayer");
//		reqDto1.setCurrency("THB");
//		reqDto1.setBetLimit("{\"SEXYBCRT\":{\"LIVE\":{\"limitId\":[260901, 260902, 260903, 260904, 260905]}}}");
//		service.createMember(reqDto1);

//		LonginRespDto rest = service.login("testplayer");

//		System.out.println(rest.toString());
	}
}
