package com.vinplay.dal.service;

import com.vinplay.dal.entities.event.MoonEventModel;
import com.vinplay.vbee.common.response.MoonEventResponse;

import java.sql.SQLException;
import java.util.List;

public interface EventsService {
    public List<MoonEventResponse> listEventsMoon() throws SQLException;

    public MoonEventModel buyPackEventMoon(String nickname, int eventId) throws SQLException;
}
