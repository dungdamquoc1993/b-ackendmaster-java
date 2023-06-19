package com.vinplay.api.backend.processors.event;

import com.vinplay.dal.dao.BannerDAO;
import com.vinplay.dal.dao.EventDAO;
import com.vinplay.dal.dao.impl.BannerDAOImpl;
import com.vinplay.dal.dao.impl.EventDAOImpl;
import com.vinplay.dal.entities.banner.BannerModel;
import com.vinplay.dal.entities.event.EventModel;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

import javax.servlet.http.HttpServletRequest;

public class EventDetailProcessor implements BaseProcessor<HttpServletRequest, String> {
    @Override
    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();

        Integer id = null;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e){

        }
        try {
            EventDAO dao = new EventDAOImpl();
            if (id != null) {
                EventModel event = dao.eventDetail(id);
                return BaseResponse.success(null, "", event);
            } else {
                return BaseResponse.error("-1", "id null");
            }
        }
        catch (Exception e) {
            return BaseResponse.error("-1", e.getMessage());
        }
    }
}