package com.vinplay.api.backend.processors.event;

import com.google.gson.Gson;
import com.vinplay.dal.dao.EventDAO;
import com.vinplay.dal.dao.impl.EventDAOImpl;
import com.vinplay.dal.entities.event.EventModel;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

import javax.servlet.http.HttpServletRequest;

public class AddNewEventProcessor implements BaseProcessor<HttpServletRequest, String> {
    @Override
    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();
        String name = request.getParameter("n");
        String created_date = request.getParameter("cd");
        String expired_date = request.getParameter("ed");


        Long amount = null;
        try {
            amount = Long.parseLong(request.getParameter("am"));
        } catch (NumberFormatException e){
        }

        try {
            EventDAO dao =  new EventDAOImpl();
            if(dao.addNewEvent(name, created_date, amount, expired_date)){
                return BaseResponse.success("", "Thêm mới thành công", null);
            } else{
                return BaseResponse.error("-1", "Thêm mới không thành công !");
            }
        }
        catch (Exception e) {
            return BaseResponse.error("-1", e.getMessage());
        }
    }
}