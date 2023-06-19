package com.vinplay.interfaces.sa;


import org.apache.log4j.Logger;


public class SAFundTransferServiceDB {
	private  static final Logger logger = Logger.getLogger(SAFundTransferServiceDB.class);
	public static void main(String[] args) throws Exception {
	}
	
	public static boolean updateBalance(String playerName,Double amount,int direction,String ip,String wid,int ispendding ){ /// wid truyen cai extID vao
		// 0 false
		// 1 true
//		DbProc dp = null;
//		try{
//			dp = new DbProc();
//			dp.setSql("call PG_GAME_ACCESS.P_GA_SAFundTransfer(?,?,?,?,?,?,?,?)");
//			dp.setOutParam(1, OracleTypes.INTEGER);
//			dp.setOutParam(2, OracleTypes.VARCHAR);
//			dp.setString(3, playerName);
//			dp.setDouble(4, amount);
//			dp.setInt(5, direction);
//			dp.setString(6, ip);
//			dp.setString(7, wid);
//			dp.setInt(8, ispendding);
//			dp.exec();
//			String flag = dp.getObject(1).toString();
//			if ("0".equals(flag)) {
//				return true;
//			} else {
//				String message = dp.getObject(2).toString();
//				logger.error("FunTransferSAService updateBalance playerName :" + message);
//				return false;
//			}
//				
//		}catch(Exception ex){
//			logger.error("FunTransferSAService updateBalance playerName :"+playerName, ex);
//			return false;
//		}finally{
//			if(dp!=null)
//				dp.close();
//		}
		return true;
	}
	
}
