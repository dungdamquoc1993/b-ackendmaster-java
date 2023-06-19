package com.vinplay.report.service;

import java.util.Date;
import java.util.List;

import com.vinplay.common.game3rd.AGGameRecordItem;
import com.vinplay.common.game3rd.CmdGameRecords;
import com.vinplay.common.game3rd.IbcGameRecordItem;
import com.vinplay.common.game3rd.SboGameRecord;
import com.vinplay.common.game3rd.ThirdPartyResponse;
import com.vinplay.common.game3rd.WMGameRecordItem;

public interface ThirdPartyGameReport {
	ThirdPartyResponse<List<AGGameRecordItem>> filterAG(String username, String nickName, String billNo, String gameCode, int flagTime,
														String fromTime, String endTime, Double betAmount, Double winAmount,String betIp, int page, int maxItem,
														String gameType);

	ThirdPartyResponse<List<WMGameRecordItem>> filterWM(String nick_name,String user,String ip,String gameName,Long betid,
														int flagTime,String fromTime,String endTime,int page,int maxItem);

	ThirdPartyResponse<List<IbcGameRecordItem>> filterIBC(Long transid, String playername, String transactiontime, Integer matchid, Integer leagueid,
		  String leaguename, Integer awayid, String awayidname, Integer homeid, String homeidname,
		  String matchdatetime, Integer bettype, Long parlayrefno, String betteam, Double hdp,
		  String sporttype, Double awayhdp, Double homehdp, Double odds, Integer awayscore,
		  Integer homescore, String islive, String islucky, String parlay_type, String combo_type,
		  Long stake, String bettag, Long winloseamount, Date winlostdatetime, Integer versionkey,
		  String lastballno, String ticketstatus, Integer oddstype, Long actual_stake, Long refund_amount, String nick_name, int flagTime,String fromTime,String endTime, int page, int maxItem);

	ThirdPartyResponse<List<CmdGameRecords>> filterCMD(String sourcename, String referenceno, Long soctransid, String isfirsthalf, Long transdate, String ishomegive, String isbethome, Double betamount,
													   Double outstanding, Double hdp, Double odds, String currency, Double winamount, Double exchangerate, String winlosestatus, String transtype,
													   String dangerstatus, Double memcommission, String betip, Integer homescore, Integer awayscore, Integer runhomescore, Integer runawayscore, String isrunning,
													   String rejectreason, String sporttype, Integer choice, Long workingdate, String oddstype, Long matchdate, Integer hometeamid, Integer awayteamid, Integer leagueid,
													   String specialid, Integer statuschange, Long stateupdatets, Double memcommissionset, String iscashout, Double cashouttotal, Double cashouttakeback,
													   Double cashoutwinloseamount, Integer betsource, String aosexcluding, Double mmrpercent, Long matchid, String matchgroupid, String betremarks,
													   String isspecial, String betdate, String settleddate, String loginname, Double stake, Double payout, Double realbet, int flagtime, String fromTime, String endTime,int page,int maxItem);

	public List<AGGameRecordItem> showDetailAG(String billNo);

	public List<WMGameRecordItem> showDetailWM(Long betid);

	public List<IbcGameRecordItem> showDetailIBC(Long transid);

	public List<CmdGameRecords> showDetailCMD(Long id);
//	//sbo
	ThirdPartyResponse<List<SboGameRecord>> filterSBO(String playerName, String nickName, String refno, String status, int flagTime,
			String fromTime, String endTime, Double betAmount, Double winAmount, int page, int maxItem,
			String sporttype);

}
