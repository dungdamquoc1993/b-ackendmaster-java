package com.vinplay.dao.wm;

import java.sql.SQLException;
import java.util.List;

import com.vinplay.dto.wm.CreateMemberReqDto;
import com.vinplay.dto.wm.GetDateTimeReportResult;
import com.vinplay.item.WMUserItem;

public interface WmDao {
	//mongo
	boolean saveWMBetLog(GetDateTimeReportResult data) throws Exception;

	// mysql
	List<Integer> maxWmUser() throws Exception;

	boolean generateWmUser(CreateMemberReqDto reqDto, int wmcountid) throws Exception;
	
	WMUserItem findWmUserByNickName(String nickName)  throws SQLException;
	
	WMUserItem findWmUserByWmId(String wmId)  throws SQLException;
	
	WMUserItem mappingUserWm(String nickName)  throws SQLException;

}
