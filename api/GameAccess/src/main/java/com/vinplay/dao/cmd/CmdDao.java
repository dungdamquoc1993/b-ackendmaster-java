package com.vinplay.dao.cmd;

import java.sql.SQLException;
import java.util.List;

import com.vinplay.dto.sportsbook.SportsbookMemberBetTicketInformationDetail;
import com.vinplay.dto.wm.GetDateTimeReportResult;
import com.vinplay.item.SportsbookItem;

public interface CmdDao {
	// mongo
	boolean saveCmdBetLog(SportsbookMemberBetTicketInformationDetail betDetail) throws Exception;

	// mysql
	List<Integer> maxCmdUser() throws Exception;

	public boolean generateCmdUser(String sportsBookId, int sportsBookCountId, String sportsBookUserName) throws SQLException;

	SportsbookItem findUserByNickName(String nickName) throws SQLException;

	public SportsbookItem findUserByCmdId(String cmdid) throws SQLException;

	SportsbookItem mappingUserCmd(String nickName) throws SQLException;
}
