package com.vinplay.api.backend.processors.log;

import com.vinplay.api.backend.response.LogMoneyUserVinResponse;
import com.vinplay.dal.dao.LogMoneyUserDao;
import com.vinplay.dal.dao.impl.LogMoneyUserDaoImpl;
import com.vinplay.dal.entities.log.LogMoneyUserNapTieuVinModel;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogMoneyUserNapVinProcessor implements BaseProcessor<HttpServletRequest, String> {
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
        try {
            Map<String, Object> map = dao.getLogMoneyUserNapVin(trans_id, user_id, nick_name, service_name, current_money,
                    money_exchange, null, fromTime, endTime, action_name, fee, null, page, maxItem);
            List<LogMoneyUserNapTieuVinModel> data = (List<LogMoneyUserNapTieuVinModel>) map.get("listData");
            Long totalData = (Long) map.get("totalData");
            Map<String, Object> mapData = new HashMap<>();
            mapData.put("listData", data);
            mapData.put("totalNap", (Long) map.get("totalNap"));
            return BaseResponse.success(mapData, totalData);
        }
        catch (Exception e) {
            return BaseResponse.error("-1", e.getMessage());
        }
    }
}