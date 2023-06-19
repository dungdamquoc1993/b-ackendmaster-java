package com.vinplay.dal.service.impl;

import com.vinplay.dal.dao.EventDAO;
import com.vinplay.dal.dao.impl.EventDAOImpl;
import com.vinplay.dal.entities.event.MoonEventModel;
import com.vinplay.dal.service.EventsService;
import com.vinplay.vbee.common.response.MoonEventResponse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventsServiceImpl implements EventsService {
    @Override
    public List<MoonEventResponse> listEventsMoon() throws SQLException {
        List<MoonEventResponse> events = new ArrayList<>();
        EventDAO dao = new EventDAOImpl();
        events = dao.getListEventsMoon();

        return events;
    }

    @Override
    public MoonEventModel buyPackEventMoon(String nickname, int eventId) throws SQLException {
        MoonEventModel eModel = new MoonEventModel();
        EventDAO dao = new EventDAOImpl();
        eModel = dao.buyPackMoon(nickname, eventId);

        return eModel;
    }
}
