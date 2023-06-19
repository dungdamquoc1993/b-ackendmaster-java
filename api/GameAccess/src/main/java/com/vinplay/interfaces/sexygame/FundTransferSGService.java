package com.vinplay.interfaces.sexygame;

import org.apache.log4j.Logger;

import com.vinplay.logic.InitData;


public class FundTransferSGService {
	private  static final Logger logger = Logger.getLogger(FundTransferSGService.class);
	public static boolean updateBalance(String playerName,Double amount,int direction,String ip,String wid,int ispendding ){
		return false;
//		// 0 false
//		// 1 true
//		DbProc dp = null;
//		try{
//			dp = new DbProc();
//			dp.setSql("call PG_GAME_ACCESS.P_GA_SGFundTransfer(?,?,?,?,?,?,?,?)");
//			dp.setOutParam(1, OracleTypes.NUMBER);
//			dp.setOutParam(2, OracleTypes.VARCHAR);
//			dp.setString(3, playerName);
//			dp.setDouble(4, amount);
//			dp.setInt(5, direction);
//			dp.setString(6, ip);
//			dp.setString(7, wid);
//			dp.setInt(8, ispendding);
//			dp.exec();
//			Integer p_flag = Integer.parseInt(dp.getObject(1).toString());
//			if(p_flag == 0){
//				return true;
//			}else{
//				logger.error("FunTransferSGService updateBalance playerName :"+playerName + " : " + dp.getObject(2));
//				return false;
//			}
//		}catch(Exception ex){
//			ex.printStackTrace();
//			 logger.error("FunTransferSGService updateBalance playerName :"+playerName, ex);
//			return false;
//		}finally{
//			if(dp!=null)
//				dp.close();
//		}
	}
}
