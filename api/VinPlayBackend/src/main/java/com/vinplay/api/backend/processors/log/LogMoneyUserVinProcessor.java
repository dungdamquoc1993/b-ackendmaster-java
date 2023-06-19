package com.vinplay.api.backend.processors.log;

import com.vinplay.api.backend.response.LogMoneyUserResponse;
import com.vinplay.api.backend.response.LogMoneyUserVinResponse;
import com.vinplay.dal.dao.LogMoneyUserDao;
import com.vinplay.dal.dao.impl.LogMoneyUserDaoImpl;
import com.vinplay.dal.entities.log.LogMoneyUserVinModel;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LogMoneyUserVinProcessor implements BaseProcessor<HttpServletRequest, String> {
    @Override
    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();
        String nick_name = request.getParameter("nn");
        String action_name = request.getParameter("an");
        String service_name = request.getParameter("sn");
        String fromTime = request.getParameter("ft");
        String endTime = request.getParameter("et");

        int page = 1, maxItem = 10;
        Long trans_id = null,current_money = null,money_exchange = null,fee = null;
        Integer user_id = null;
        try {
            trans_id = Long.parseLong(request.getParameter("trid"));
        } catch (NumberFormatException e) {
            // TODO: handle exception
        }
        try {
            user_id = Integer.parseInt(request.getParameter("uid"));
        } catch (NumberFormatException e) {
            // TODO: handle exception
        }
        try {
            current_money = Long.parseLong(request.getParameter("cm"));
        } catch (NumberFormatException e) {
            // TODO: handle exception
        }
        try {
            money_exchange = Long.parseLong(request.getParameter("me"));
        } catch (NumberFormatException e) {
            // TODO: handle exception
        }
        try {
        	fee = Long.parseLong(request.getParameter("fe"));
        } catch (NumberFormatException e) {
            // TODO: handle exception
        }
        try {
            page = Integer.parseInt(request.getParameter("pg"));
        } catch (NumberFormatException e) {
            // TODO: handle exception
        }
        try {
            maxItem = Integer.parseInt(request.getParameter("mi"));
        } catch (NumberFormatException e) {
            // TODO: handle exception
        }
        LogMoneyUserDao dao = new LogMoneyUserDaoImpl();

        LogMoneyUserResponse res = new LogMoneyUserResponse(false, "1001");
        try {
//            List<LogMoneyUserVinModel> data = new ArrayList<>();
//            Map<String, Object> map = dao.getLogMoneyUserVin(trans_id, user_id, nick_name, service_name, current_money,
//                    money_exchange, null, fromTime, endTime, action_name, fee, null, null, null, page, maxItem);
//            data = (List<LogMoneyUserVinModel>) map.get("listData");
//            res.setData(data);
//            res.setErrorCode("0");
//            res.setSuccess(true);
//            res.setTotalData((Long) map.get("totalData"));
//            res.setTotalBet((Long) map.get("totalBet"));
//            res.setTotalFee((Long) map.get("totalFee"));
//            res.setTotalSoVongcuoc((Long) map.get("soVongCuoc"));
        	
        	if(action_name.equals("taixiusieutoc")) {
        		service_name = "190";
        		action_name = "TaiXiu";
        	}
        	
        	List<Map<String, Object>> data = dao.getLogMoneyUserVin4Report(trans_id, user_id, nick_name, service_name, current_money,
                    money_exchange, null, fromTime, endTime, action_name, fee, null, null, null, page, maxItem);
        	if(data.size() < 4) {
        		return res.toJson();
        	}
        	
            res.setData(data.get(0).get("data"));
            res.setErrorCode("0");
            res.setSuccess(true);
            res.setTotalData((Long) data.get(1).get("totalRecords"));
            res.setTotalBet((Long) data.get(2).get("totalBet"));
            res.setTotalFee((Long) data.get(3).get("totalFee"));
            res.setTotalSoVongcuoc((Long) data.get(4).get("soVongCuoc"));
        }
        catch (Exception e) {
            // log
        }
        return res.toJson();
    }
}
