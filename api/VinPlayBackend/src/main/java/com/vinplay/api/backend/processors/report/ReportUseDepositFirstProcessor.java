package com.vinplay.api.backend.processors.report;

import com.vinplay.api.backend.response.ReportLogUserResponse;
import com.vinplay.dal.dao.AgentDAO;
import com.vinplay.dal.dao.impl.AgentDAOImpl;
import com.vinplay.dal.entities.report.MoneyInOut;
import com.vinplay.dal.entities.report.ReportMoneySystemModelNew;
import com.vinplay.dal.service.impl.AgentServiceImpl;
import com.vinplay.payment.utils.Constant;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.AgentResponse;
import com.vinplay.vbee.common.response.BaseResponse;
import com.vinplay.vbee.common.response.LogUserMoneyResponse;
import com.vinplay.vbee.common.statics.Consts;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ReportUseDepositFirstProcessor
implements BaseProcessor<HttpServletRequest, String> {
    public String execute(Param<HttpServletRequest> param) {
        ReportLogUserResponse res = new ReportLogUserResponse(false, "1001");
        HttpServletRequest request = (HttpServletRequest) param.get();
        String serPath = request.getServletPath();
        if(serPath == null || serPath.trim().isEmpty() || serPath != "/api_agent"){
            return BaseResponse.error(Constant.ERROR_PARAM, "Not allow access this api");
        }
        
        String currentTime = request.getParameter("t");
        String referral_code = request.getParameter("code");
        String nickname = request.getParameter("nn");
        String pageNumberStr= request.getParameter("pg");
        String limitStr= request.getParameter("mi");
        if(referral_code == null || referral_code.trim().isEmpty()){
            return BaseResponse.error(Constant.ERROR_PARAM, "Code of agency not empty");
        }
        
        if(referral_code.equals("referral_code_default")) {
        	referral_code = "";
        }
        
        int pageNumber =0;
        int limit = 0;
        try {
            pageNumber = Integer.parseInt(pageNumberStr);
            limit = Integer.parseInt(limitStr);
        } catch (NumberFormatException e) {
            return BaseResponse.error(Constant.ERROR_PARAM, "pageNumber or limit format");
        }

        if (pageNumber <= 0)
            pageNumber = 1;

        if (limit < 0)
            limit = 15;

        try {
        	List<Map<String, Object>> data = new ArrayList<>();
            AgentDAO dao = new AgentDAOImpl();
            data = dao.getUsersDepositFirstInDay(currentTime, nickname, referral_code, pageNumber, limit);
            if (data == null || data.size() == 0)
                return res.toJson();

            res.total = Integer.parseInt(data.get(data.size() - 1).get("total").toString());
            data.remove(data.size() - 1);
            res.setData(data);
            res.setErrorCode("0");
            res.setSuccess(true);
            return res.toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"success\":false,\"errorCode\":\"1001\"}";
        }
    }
}

