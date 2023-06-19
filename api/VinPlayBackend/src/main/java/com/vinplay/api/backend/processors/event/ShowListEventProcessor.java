package com.vinplay.api.backend.processors.event;

import com.google.gson.Gson;
import com.vinplay.dal.dao.EventDAO;
import com.vinplay.dal.dao.impl.EventDAOImpl;
import com.vinplay.dal.entities.event.EventModel;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ShowListEventProcessor implements BaseProcessor<HttpServletRequest, String> {
    @Override
    public String execute(Param<HttpServletRequest> param) {
        Gson gson = new Gson();
        HttpServletRequest request = param.get();
        String name = request.getParameter("n");
        String startTime = request.getParameter("st");
        String endTime = request.getParameter("et");

        Long amount = null;
        int page = 1, maxItem = 10, flagtime = 1;
        try {
            amount = Long.parseLong(request.getParameter("sts"));
        } catch (NumberFormatException e){
        }
        try {
            flagtime = Integer.parseInt(request.getParameter("fg"));
        } catch (NumberFormatException e){
        }
        try {
            page = Integer.parseInt(request.getParameter("pg"));
        } catch (NumberFormatException e){
        }
        try {
            maxItem = Integer.parseInt(request.getParameter("mi"));
        } catch (NumberFormatException e){
        }

        try {
            EventDAO dao = new EventDAOImpl();
            long total = dao.countlistEvent(name,amount,flagtime,startTime,endTime);
            List<EventModel> events = dao.listEvent(name,amount,flagtime,startTime,endTime,page,maxItem);
            return BaseResponse.success(events, total);
        }
        catch (Exception e) {
            return BaseResponse.error("-1", e.getMessage());
        }
    }
}