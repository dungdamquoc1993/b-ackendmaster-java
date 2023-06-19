package com.vinplay.run;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.vinplay.dto.sa.SABetDetails;
import com.vinplay.dto.sa.SALogs;
import com.vinplay.interfaces.sa.SAUtils;
import com.vinplay.logic.CommonMethod;
import com.vinplay.logic.InitData;
import com.google.gson.Gson;

/**
 * @author Archie
 *
 */
public class TaskAutoCollectSABetLog extends java.util.TimerTask{
	
	private static final Logger LOGGER = Logger.getLogger(TaskAutoCollectSABetLog.class);
	
	private static String dtStartUTC = "";
	
	private static boolean isRunning = false;

	private static String getDtStartUTC() {
		if (dtStartUTC == null || "".equals(dtStartUTC)) {
			dtStartUTC = getDTEndUTC();
		}
		return dtStartUTC;
	}

	private static void setDtStartUTC(String dtStartUTCNew) {
		if (dtStartUTCNew != null && !"".equals(dtStartUTCNew)) {
			dtStartUTC = dtStartUTCNew;
		}
	}
	
	public TaskAutoCollectSABetLog() {
		getDtStartUTC();
	}
	@Override
	public void run() {
		if(isRunning) return;
		
		isRunning = true;
		boolean isNotMaintain = !InitData.isSADown();
		if (isNotMaintain) {
			String strdate = "";
			try {
				Date currentTime = new Date();
				SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				strdate = format0.format(currentTime);
				//add 1 hours for api timezone +8
				String nextOneHour = CommonMethod.getTimeSpace(strdate, "yyyy-MM-dd HH:mm:ss", 55, "mm");// dtStart +1 hour
				
				reportSAService(getDtStartUTC(), nextOneHour);
			} catch (Exception ex) {
				LOGGER.error("Error occured while trying to collect SA games log!", ex);
			}finally {
				isRunning = false;
			}
		}
		isRunning = false;
	}

	private void reportSAService(String dtStart, String dtEndUTC) throws Exception {
		String finalTime = dtEndUTC;
		
		while (true) {
			
			String nextOneDays = CommonMethod.getTimeSpace(dtStart, "yyyy-MM-dd HH:mm:ss", 23, "HH");// dtStart +day
			if (dtStart.compareTo(finalTime) < 0) {
				if (nextOneDays.compareTo(finalTime) > 0) {
					dtEndUTC = finalTime;
				} else {
					dtEndUTC = nextOneDays;
				}
			} else {
				break;
			}

			LOGGER.info("[TaskCollectSABetLog] dtStart =" + dtStart + " ,dtEndUTC=" + dtEndUTC);
			// get data from sa
			SALogs result = SAUtils.getBetLogs(dtStart, dtEndUTC);
			if (result !=null && result.getErrorMsgId().equals("0")) {
				List<SABetDetails> details = result.getBetDetailList();
				if (details != null) {
					for (SABetDetails gri : details) {
						// storage in lottery db
						if (storageBetLog(gri)) {
							LOGGER.info("TaskCollectSABetLog Storage success");
							// update dtStartUTC
						} else {
							Gson gson = new Gson();
							LOGGER.error("[TaskCollectSABetLog0] TaskCollectSABetLog unsuccessful , record: "
									+ gson.toJson(gri));
						}
					}
					// save startDate
					setDtStartUTC(dtEndUTC.compareTo(finalTime) > 0 ? finalTime : dtEndUTC);// 2019-12-13 14:36:42
				}

			} else {
				if (result.getErrorMsgId().equals("112")) {
					//API recently called
					TimeUnit.SECONDS.sleep(5);
				}
			}
			// end function
			dtStart = nextOneDays;
		}
	}
	
	private static String getDTEndUTC() {
		String dtEndUTC = "";
		
//		try {
//			dp = new DbProc();
//			dp.setSql("call PG_GAME_ACCESS.P_AG_GetSAEndUTC(?)");
//			dp.setOutParam(1, OracleTypes.VARCHAR);
//			dp.execute();
//			if(dp.getObject(1)!=null) {
//				dtEndUTC = dp.getObject(1).toString();
//			}
//			
//		} catch(Exception ex) {
//			LOGGER.error("ex", ex);
//		} finally {
//			if (dp != null)
//				dp.close();
//		}
		return dtEndUTC;
	}
	/**
	 * Storage in to SAgamerecord table
	 * @param formatter
	 * @param input
	 * @return
	 * @throws Exception
	 */
	
	private String convertPHtoHCM(DateTimeFormatter formatter, String input) {
		String resulttime = CommonMethod.convertPHtoUTC(input, "yyyy-MM-dd'T'HH:mm:ss.SSS");
		resulttime = CommonMethod.convertUTCToHCM(resulttime, "yyyy-MM-dd'T'HH:mm:ss.SSS");
		LocalDateTime formatDateTimebet = LocalDateTime.parse(resulttime, formatter);
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(formatDateTimebet);
	}
	
	private boolean storageBetLog(SABetDetails resDto) throws Exception {
		String betTimeParam = resDto.getBetTime();
		String payTimeParam = resDto.getPayoutTime();

		betTimeParam = betTimeParam.length() == 19 ? betTimeParam + ".0" : betTimeParam;
		payTimeParam = payTimeParam.length() == 19 ? payTimeParam + ".0" : payTimeParam;

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
		String bettime = convertPHtoHCM(formatter, betTimeParam);
		String payoutTime = convertPHtoHCM(formatter, payTimeParam);

//		DbProc dp = null;
//		try {
//			dp = new DbProc();
//			dp.setSql("call PG_GAME_ACCESS.P_AG_SABetLog(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//			dp.setOutParam(1, OracleTypes.NUMBER);
//			dp.setOutParam(2, OracleTypes.VARCHAR);
//			dp.setString(3, bettime);
//			dp.setString(4, payoutTime);
//			dp.setString(5, resDto.getUserName());
//			dp.setInt(6, resDto.getHostID());
//			dp.setString(7, resDto.getGameID());
//			dp.setInt(8, resDto.getRound());
//			dp.setInt(9, resDto.getSet());
//			dp.setLong(10, resDto.getBetID());
//			dp.setDouble(11, resDto.getBetAmount());
//			dp.setDouble(12, resDto.getRolling());
//			dp.setDouble(13, resDto.getResultAmount());
//			dp.setDouble(14, resDto.getBalance());
//			dp.setString(15, resDto.getGameType());
//			dp.setInt(16, resDto.getBetType());
//			dp.setInt(17, resDto.getBetSource());
//			dp.setString(18, resDto.getState());
//			dp.setLong(19, resDto.getTransactionID());
//			dp.setString(20, resDto.getDetail());
//			dp.setString(21, bettime); // endUTC
//			dp.setString(22, resDto.getGameResult());
//
//			dp.execute();
//			int res = Integer.parseInt(dp.getObject(1).toString());
//			dp.getObject(2).toString();
//			if (res == 0) {
//				return true;
//			} else if (res == 1) {
//				LOGGER.error("[TaskCollectTDBetLog] P_AG_SABetLog" + dp.getObject(2).toString());
//				return false;
//			}
//
//		} catch (Exception ex) {
//			LOGGER.error("ex", ex);
//			return false;
//		} finally {
//			if (dp != null)
//				dp.close();
//		}
		return false;
	}
}
