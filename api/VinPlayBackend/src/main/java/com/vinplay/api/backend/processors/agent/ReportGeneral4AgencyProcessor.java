package com.vinplay.api.backend.processors.agent;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.api.backend.response.ReportLogUserResponse;
import com.vinplay.dal.dao.AgentDAO;
import com.vinplay.dal.dao.impl.AgentDAOImpl;
import com.vinplay.payment.utils.Constant;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.response.BaseResponse;
import javax.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.*;

public class ReportGeneral4AgencyProcessor
implements BaseProcessor<HttpServletRequest, String> {
    public String execute(Param<HttpServletRequest> param) {
        ReportLogUserResponse res = new ReportLogUserResponse(false, "1001");
        HttpServletRequest request = (HttpServletRequest) param.get();
        String serPath = request.getServletPath();
        if(serPath == null || serPath.trim().isEmpty() || serPath != "/api_agent"){
            return BaseResponse.error(Constant.ERROR_PARAM, "Not allow access this api");
        }
        
        String currentTime = request.getParameter("t");
        String agencyCode = request.getParameter("rc");
        if(agencyCode == null || agencyCode.trim().isEmpty())
        	return BaseResponse.error(Constant.ERROR_PARAM, "Agency code not empty");
        	
        String fromTime = currentTime == null || currentTime.trim().isEmpty() ? new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(Calendar.getInstance().getTime()) : currentTime + " 00:00:00";
        String endTime = currentTime == null || currentTime.trim().isEmpty() ? new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(Calendar.getInstance().getTime()) : currentTime + " 23:59:59";
        List<Map<String, Object>> data = new ArrayList<>();
        try {
            AgentDAO dao = new AgentDAOImpl();
            Map<String, Object> obj = new HashMap<>();
            obj.put("sumDeposit", dao.getSumDeposit4Agency(agencyCode, fromTime, endTime));
            data.add(obj);
            obj.put("sumWithdraw", dao.getSumWithdraw4Agency(agencyCode, fromTime, endTime));
            data.add(obj);
            obj.put("totalMember", dao.getTotalUser4Agency(agencyCode));
            data.add(obj);
            obj.put("totalUserBet", dao.getTotalUserBet4Agency(agencyCode, fromTime, endTime));
            data.add(obj);
            obj.put("totalUserRegisterNew", dao.getTotalUserRegister4Agency(agencyCode, fromTime, endTime));
            data.add(obj);
            int totalMemberOnl = 0;
    		HazelcastInstance client = HazelcastClientFactory.getInstance();
    		if (client != null) {
    			IMap map = client.getMap("mapCheckCCU");
    			try{ totalMemberOnl = (int) map.get("mapCheckCCU");}catch (Exception e) { }
    		}
    		
            obj.put("totalUserOnline", totalMemberOnl);
            data.add(obj);
            res.total = 1;
            res.setData(obj);
            res.setErrorCode("0");
            res.setSuccess(true);
            return res.toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"success\":false,\"errorCode\":\"1001\"}";
        }
    }
}

