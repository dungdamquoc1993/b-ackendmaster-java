package com.vinplay.api.backend.processors.agent;

import com.vinplay.dal.dao.AgentDAO;
import com.vinplay.dal.dao.impl.AgentDAOImpl;
import com.vinplay.dal.entities.agent.DetailUserModel;
import com.vinplay.dal.entities.agent.UserOfAgentModel;
import com.vinplay.payment.utils.Constant;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListUserOfUserAgentProcessor implements BaseProcessor<HttpServletRequest, String> {
    @Override
    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();
        String serPath = request.getServletPath();
        if(serPath == null || serPath.trim().isEmpty() || serPath != "/api_agent"){
            return BaseResponse.error(Constant.ERROR_PARAM, "Not allow access this api");
        }

        String nickname = request.getParameter("nn");
        String fromTime = request.getParameter("ft");
        String endTime = request.getParameter("et");
        String code = request.getParameter("cd");
        int page, maxItem;
        if((code==null || code.isEmpty())){
            return BaseResponse.error("-1", "nhập thiếu mã code");
        }

        if(!(fromTime==null || fromTime.isEmpty())){
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(fromTime);
            } catch (ParseException e) {
                return BaseResponse.error("-1", "ngày bắt đầu sai định dạng yyyy-MM-dd hh:mm:ss");
            }
        }

        if(!(endTime==null || endTime.isEmpty())){
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(endTime);
            } catch (ParseException e) {
                return BaseResponse.error("-1", "ngày kết thúc sai định dạng yyyy-MM-dd hh:mm:ss");
            }
        }

        try {
            page = Integer.parseInt(request.getParameter("pg"));
        } catch (NumberFormatException e) {
            page = 1;
        }

        try {
            maxItem = Integer.parseInt(request.getParameter("mi"));
        } catch (NumberFormatException e) {
            maxItem = 10;
        }

        AgentDAO dao = new AgentDAOImpl();
        try {
//            Long totalRecord = dao.countUserOfUserAgent(code, nickname, fromTime, endTime);
//            List<DetailUserModel> users = dao.listUserOfUserAgent(code, nickname, fromTime, endTime, page, maxItem);
//        	  return BaseResponse.success(users, totalRecord);

            List<Map<String, Object>> data = new ArrayList<>();
            if(code.equals("referral_code_default")) {
            	code = "";
            }
            
            data = dao.reportUserPlay4Agent(code, nickname, fromTime, endTime, page, maxItem);
            int totalRecord = Integer.parseInt(data.get(data.size() - 1).get("total").toString());
            data.remove(data.get(data.size() - 1));
            long totalNap = Long.parseLong(data.get(data.size() - 1).get("total_nap").toString());
            long totalRut = Long.parseLong(data.get(data.size() - 1).get("total_rut").toString());
            long totalKM = Long.parseLong(data.get(data.size() - 1).get("total_km").toString());
            Map<String, Object> map = new HashMap<>();
            map.put("total_nap", totalNap);
            map.put("total_rut", totalRut);
            map.put("total_doanhthu", totalNap - totalRut -totalKM);
            map.put("total_km", totalKM);
            data.remove(data.get(data.size() - 1));
            map.put("listData", data);
            return BaseResponse.success(map, totalRecord);
        }
        catch (Exception e) {
            return BaseResponse.error("-1", e.getMessage());
        }
    }
}