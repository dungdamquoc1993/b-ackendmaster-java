package com.vinplay.api.backend.processors.agent;

import com.vinplay.api.backend.response.BaseResponse;
import com.vinplay.dal.dao.AgentDAO;
import com.vinplay.dal.dao.impl.AgentDAOImpl;
import com.vinplay.dal.entities.agent.VinPlayAgentModel;
import com.vinplay.payment.utils.Constant;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListAgentProcessor implements BaseProcessor<HttpServletRequest, String> {
    @Override
    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();
//        String serPath = request.getServletPath();
//        if(serPath == null || serPath.trim().isEmpty() || serPath != "/api_agent"){
//            return com.vinplay.vbee.common.response.BaseResponse.error(Constant.ERROR_PARAM, "Not allow access this api");
//        }

        String nick_name = request.getParameter("nn");
        String refcode = request.getParameter("rc");
        String fromTime = request.getParameter("ft");
        String endTime = request.getParameter("et");
        int page = 1, maxItem = 10;
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
        Map<String, Object> map = new HashMap<>();
        try {
            map = dao.listAgent1(nick_name, refcode, fromTime, endTime, page, maxItem);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        BaseResponse res = new BaseResponse(false, "1001");
        try {
            res.setData(map.get("listData"));
            res.setTotalData((Long) map.get("totalData"));
            res.setErrorCode("0");
            res.setSuccess(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return res.toJson();
    }
}
