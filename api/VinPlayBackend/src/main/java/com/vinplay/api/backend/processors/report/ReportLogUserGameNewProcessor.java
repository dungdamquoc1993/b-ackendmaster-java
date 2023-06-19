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

public class ReportLogUserGameNewProcessor
implements BaseProcessor<HttpServletRequest, String> {
    public String execute(Param<HttpServletRequest> param) {
        ReportLogUserResponse res = new ReportLogUserResponse(false, "1001");
        HttpServletRequest request = (HttpServletRequest) param.get();
        String serPath = request.getServletPath();
        if(serPath == null || serPath.trim().isEmpty() || serPath != "/api_agent"){
            return BaseResponse.error(Constant.ERROR_PARAM, "Not allow access this api");
        }
        
        String fromTime = request.getParameter("ft");
        String endTime = request.getParameter("et");
        String nickName = request.getParameter("nn");
        String referral_code = request.getParameter("code");
        //Type: NOHU | SPORT | LIVECASINO | POKER | MINIGAME
        String type = request.getParameter("type");
        if(type == null || type.trim().isEmpty())
            return BaseResponse.error(Constant.ERROR_PARAM, "type only in list: NOHU | SPORT | LIVECASINO | POKER | MINIGAME");

        if(referral_code == null || referral_code.trim().isEmpty()){
            return BaseResponse.error(Constant.ERROR_PARAM, "Code of agency not empty");
        }

        String pageNumberStr= request.getParameter("pg");
        String limitStr= request.getParameter("mi");
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

        List<String> fields = new ArrayList<>();
        List<Map<String, Object>> data = new ArrayList<>();
        try {
            AgentDAO dao = new AgentDAOImpl();
            switch (type){
                case "NOHU":
                    fields = Arrays.asList("CONCAT(IFNULL(slot_bitcoin,0),'/',IFNULL(slot_bitcoin_win,0)) bitcoint", 
                    		"CONCAT(IFNULL(slot_taydu,0),'/',IFNULL(slot_taydu_win,0)) taydu",
                            "CONCAT(IFNULL(slot_angrybird,0),'/',IFNULL(slot_angrybird_win,0)) angrybird",
                            "CONCAT(IFNULL(slot_thantai,0),'/',IFNULL(slot_thantai_win,0)) thantai",
                            "CONCAT(IFNULL(slot_thethao,0),'/',IFNULL(slot_thethao_win,0)) thethao", 
                            "CONCAT(IFNULL(slot_chiemtinh,0),'/',IFNULL(slot_chiemtinh_win,0)) chiemtinh",
                            "CONCAT(IFNULL(fish,0),'/',IFNULL(fish_win,0)) fish");
                    data = new ArrayList<>();
                    data = dao.getUserDetailAgent(fields, nickName, fromTime, endTime, referral_code, pageNumber, limit);
                    break;
                case "SPORT":
                    fields = Arrays.asList("CONCAT(IFNULL(ibc,0),'/',IFNULL(ibc_win,0)) ibc", 
                    		"CONCAT(IFNULL(cmd,0),'/',IFNULL(cmd_win,0)) cmd");
                    data = new ArrayList<>();
                    data = dao.getUserDetailAgent(fields,nickName, fromTime, endTime, referral_code, pageNumber, limit);
                    break;
                case "LIVECASINO":
                    fields = Arrays.asList("CONCAT(IFNULL(wm,0),'/',IFNULL(wm_win,0)) wm",
                    		"CONCAT(IFNULL(ag,0),'/',IFNULL(ag_win,0)) ag");
                    data = new ArrayList<>();
                    data = dao.getUserDetailAgent(fields,nickName, fromTime, endTime, referral_code, pageNumber, limit);
                    break;
                case "POKER":
                    fields = Arrays.asList("CONCAT(IFNULL(bacay,0),'/',IFNULL(bacay_win,0)) bacay", 
                    		"CONCAT(IFNULL(tlmn,0),'/',IFNULL(tlmn_win,0)) tlmn",
                    		"CONCAT(IFNULL(xocdia,0),'/',IFNULL(xocdia_win,0)) xocdia");
                    data = new ArrayList<>();
                    data = dao.getUserDetailAgent(fields,nickName, fromTime, endTime, referral_code, pageNumber, limit);
                    break;
                case "MINIGAME":
                    fields = Arrays.asList("CONCAT(IFNULL(taixiu,0),'/',IFNULL(taixiu_win,0)) taixiu",
                    		"CONCAT(IFNULL(baucua,0),'/',IFNULL(baucua_win,0)) baucua",
                    		"CONCAT(IFNULL(slot_pokemon,0),'/',IFNULL(slot_pokemon_win,0)) slot_pokemon",
                            "CONCAT(IFNULL(caothap,0),'/',IFNULL(caothap_win,0)) caothap", 
                            "CONCAT(IFNULL(minipoker,0),'/',IFNULL(minipoker_win,0)) minipoker",
                            "CONCAT(IFNULL(taixiu_st,0),'/',IFNULL(taixiu_st_win,0)) taixiu_st");
                    data = new ArrayList<>();
                    data = dao.getUserDetailAgent(fields,nickName, fromTime, endTime, referral_code, pageNumber, limit);
                    break;
            }

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

