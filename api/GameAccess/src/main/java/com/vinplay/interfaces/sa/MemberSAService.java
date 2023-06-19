package com.vinplay.interfaces.sa;

import java.util.List;
import org.apache.log4j.Logger;

import com.vinplay.dto.sa.SALoginDto;
import com.vinplay.dto.sa.SALoginReq;
import com.vinplay.dto.sa.SALoginResp;
import com.vinplay.dto.sa.SAUserInfoResp;
import com.vinplay.item.SAUser;
import com.vinplay.logic.InitServlet;

public class MemberSAService {
	private  static final Logger logger = Logger.getLogger(MemberSAService.class);
	
	public static SALoginDto loginMapping(String loginName, String Ip)throws Exception{
		if(loginName == null || loginName.length()<3){
			logger.error("Tên người dùng trống hoặc không hợp lệ " + loginName);
			return null;
		}
//		DbProc dp = null;
//		try{
//			dp = new DbProc();
//			dp.setSql("call PG_GAME_ACCESS.p_AG_MappingSAUser(?,?,?,?)");
//			dp.setOutParam(1, OracleTypes.CURSOR);
//			dp.setOutParam(2, OracleTypes.NUMBER);
//			dp.setOutParam(3, OracleTypes.VARCHAR);
//			dp.setString(4, loginName);
//			dp.execute();
//			int res = Integer.parseInt(dp.getObject(2).toString());
//			if (res == 0) {
//				List<SAUser> list = dp.getResult(SAUser.class,1);
//				SAUser saUser = list.get(0);
//				SALoginReq req= new SALoginReq();
//				req.setUserName(saUser.getSaid());
//				SALoginResp loginResp = SAUtils.login(req);
//				if(loginResp == null) {
//					logger.info("SALoginResp is null");
//					return null;
//				}else {
//					SALoginDto resp = new SALoginDto();
//					resp.setLoginname(saUser.getLoginname());
//					resp.setSaid(saUser.getSaid());
//					resp.setToken(loginResp.getToken());
//					resp.setUsername(loginResp.getDisplayName());
//					String url = InitServlet.SA_CLIENT + "?username="+saUser.getSaid()+
//							"&token="+loginResp.getToken()+"&lobby="+InitServlet.SA_LOBBY_CODE+"&lang=vn";
//					resp.setUrl(url);
//					return resp;
//				}
//				
//			} else if (res == 1) {
//				logger.info("Thất bại:  " + dp.getObject(3).toString());
//				return null;
//			}
//		}catch(Exception ex){
//			logger.error("p_AG_MappingSAUser", ex);
//			return null;
//		}finally{
//			if (dp != null)
//				dp.Close();
//		}
		return null;
	}
	public SAUserInfoResp getPlayerInfo(String playerName ) {
		SAUserInfoResp resp= SAUtils.getSAInfo(playerName);
		return resp;
	}
}
