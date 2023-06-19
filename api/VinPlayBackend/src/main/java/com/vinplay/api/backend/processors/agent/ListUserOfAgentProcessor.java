package com.vinplay.api.backend.processors.agent;

import com.vinplay.dal.dao.AgentDAO;
import com.vinplay.dal.dao.impl.AgentDAOImpl;
import com.vinplay.dal.entities.agent.UserOfAgentModel;
import com.vinplay.payment.utils.Constant;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ListUserOfAgentProcessor implements BaseProcessor<HttpServletRequest, String> {
    @Override
    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();
//        String serPath = request.getServletPath();
//        if(serPath == null || serPath.trim().isEmpty() || serPath != "/api_agent"){
//            return BaseResponse.error(Constant.ERROR_PARAM, "Not allow access this api");
//        }

        String referral_code = request.getParameter("rc");
        String nick_name = request.getParameter("nn");
        String fromTime = request.getParameter("ft");
        String endTime = request.getParameter("et");
        if((fromTime == null || fromTime.trim().isEmpty()) || (endTime == null || endTime.trim().isEmpty())){
            return BaseResponse.error("-1", "Phải có đủ cả fromTime và endTime!");
        }

        int page, maxItem;
        Long doanhThu;
        try {
            doanhThu = Long.parseLong(request.getParameter("dt"));
        } catch (NumberFormatException e) {
            doanhThu = null;
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
            Long total = dao.countUserOfAgent(referral_code, nick_name, fromTime, endTime, doanhThu);
            Long[] analystics = dao.analyticsDepositWithdrawOfAllUserOfAgent(referral_code, nick_name, fromTime, endTime, doanhThu);
            List<UserOfAgentModel> users = dao.listUserOfAgent(referral_code, nick_name, fromTime, endTime, doanhThu, page, maxItem);
            Map<String, Object> map = new HashMap<>();
            map.put("total_nap", analystics[0]);
            map.put("total_rut", analystics[1]);
            map.put("total_doanhthu", analystics[0] - analystics[1]);
            map.put("total_km", analystics[2]);
            map.put("listData", users);
//            List<Map<String, Object>> data = new ArrayList<>();
//            data = dao.reportUserPlay4Agent(referral_code, nick_name, fromTime, endTime, page, maxItem);
//            int totalRecord = Integer.parseInt(data.get(data.size() - 1).get("total_nap").toString());
//            data.remove(data.get(data.size() - 1));
//            long totalNap = Long.parseLong(data.get(data.size() - 1).get("total_nap").toString());
//            long totalRut = Long.parseLong(data.get(data.size() - 1).get("total_rut").toString());
//            long totalKM = Long.parseLong(data.get(data.size() - 1).get("total_km").toString());
//            Map<String, Object> map = new HashMap<>();
//            map.put("total_nap", totalNap);
//            map.put("total_rut", totalRut);
//            map.put("total_doanhthu", totalNap - totalRut);
//            map.put("total_km", totalKM);
//            data.remove(data.get(data.size() - 1));
//            map.put("listData", data);
            return BaseResponse.success(map, total);
        }
        catch (Exception e) {
            return BaseResponse.error("-1", e.getMessage());
        }
    }
}