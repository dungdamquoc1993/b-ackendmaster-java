package com.vinplay.service.sexygame;

import com.vinplay.item.SGUserItem;
import org.apache.log4j.Logger;

import java.util.List;
/**
 * @author Archie
 *
 */
public class QuerySGService {

	private static final Logger logger = Logger.getLogger(QuerySGService.class);
		public static SGUserItem getSGId(String loginName){
			return null;
//			if(loginName == null || loginName.length()<3){
//				logger.info("Tên người dùng trống hoặc không hợp lệ " + loginName);
//				return null;
//			}
//			DbProc dp = null;
//			SGUserItem user = new SGUserItem();
//			try{
//				dp = new DbProc();
//				dp.setSql("call PG_GAME_ACCESS.p_AG_GetSGId(?,?,?,?,?)");
//				dp.setOutParam(1, OracleTypes.CURSOR);
//				dp.setOutParam(2, OracleTypes.NUMBER);
//				dp.setOutParam(3, OracleTypes.VARCHAR);
//				dp.setOutParam(4, OracleTypes.NUMBER);
//				dp.setString(5, loginName);
//				dp.execute();
//				int res = Integer.parseInt(dp.getObject(2).toString());
//				if(res==0){
//					List<SGUserItem> list = dp.getResult(SGUserItem.class,1);
//					user = list.get(0);
//				}else if(res==1){
//					logger.info("Thất bại:  " + dp.getObject(3).toString());
//				}
//			}catch(Exception ex){
//				//
//				logger.error("p_AG_GetSGId", ex);
//			}finally{
//				dp.Close();
//			}
//			return user;
		}
	


}
