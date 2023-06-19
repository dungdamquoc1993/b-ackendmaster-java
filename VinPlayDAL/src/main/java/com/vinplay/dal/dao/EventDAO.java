package com.vinplay.dal.dao;

import com.vinplay.dal.entities.event.EventModel;
import com.vinplay.dal.entities.event.MoonEventModel;
import com.vinplay.vbee.common.response.MoonEventResponse;

import java.sql.SQLException;
import java.util.List;

public interface EventDAO {

    public long countlistEvent(String name, Long amount, int flagtime, String startTime, String endTime);

    public List<EventModel> listEvent(String name, Long amount, int flagtime, String startTime, String endTime, int page, int maxItem);

    public Boolean addNewEvent(String name, String created_date, Long amount, String expired_date);

    public EventModel eventDetail(Integer id);

    public Boolean updateEventById(Integer id, String name, String created_date, Long amount, String expired_date);

    public Boolean updateEventByName(String name, String created_date, Long amount, String expired_date);

    public Boolean deleteEvent(Integer id);

    public Boolean deleteEvent(String name);

    public List<MoonEventResponse> getListEventsMoon();

    public MoonEventModel buyPackMoon(String nickname, int eventId) throws SQLException;
}
