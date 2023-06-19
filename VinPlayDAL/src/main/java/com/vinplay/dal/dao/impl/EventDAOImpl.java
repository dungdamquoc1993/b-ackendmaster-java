package com.vinplay.dal.dao.impl;

import com.vinplay.dal.dao.EventDAO;
import com.vinplay.dal.entities.event.EventModel;
import com.vinplay.dal.entities.event.MoonEventModel;
import com.vinplay.vbee.common.pools.ConnectionPool;
import com.vinplay.vbee.common.response.MoonEventResponse;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventDAOImpl implements EventDAO {
    @Override
    public long countlistEvent(String name, Long amount, int flagtime, String startTime, String endTime) {
        long count = 0;
        try(Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname")) {
            int index = 1;
            Boolean b_name = (name==null || name.trim().isEmpty());
            Boolean b_amount = (amount==null);
            Boolean b_startTime = (startTime==null || startTime.trim().isEmpty());
            Boolean b_endTime = (endTime==null || endTime.trim().isEmpty());
            String created_date = (b_startTime ? "" : (" and created_date >= ?"))
                    + (b_endTime ? "" : (" and created_date <= ?"));
            String expired_date = (b_startTime ? "" : (" and expired_date >= ?"))
                    + (b_endTime ? "" : (" and expired_date <= ?"));
            String sql = "Select count(*) as cnt from vinplay.event where 1=1 "
                    +(b_name ? "" : (" and name = ?"))
                    +(b_amount ? "" : (" and amount = ?"))
                    +(flagtime==1 ? created_date : "")
                    +(flagtime==2 ? expired_date : "");
            PreparedStatement stmt = conn.prepareStatement(sql);
            if(!b_name){
                stmt.setString(index++, name);
            }
            if(!b_amount){
                stmt.setLong(index++, amount);
            }
            if(!b_startTime){
                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(startTime);
                stmt.setDate(index++, new java.sql.Date(date.getTime()));
            }
            if(!b_endTime){
                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(endTime);
                stmt.setDate(index++, new java.sql.Date(date.getTime()));
            }
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt("cnt");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public List<EventModel> listEvent(String name, Long amount, int flagtime, String startTime, String endTime, int page, int maxItem) {
        List<EventModel> events = new ArrayList<>();
        try(Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname")) {
            page = (page - 1) < 0 ? 0 : (page - 1);
            int index = 1;
            Boolean b_name = (name==null || name.trim().isEmpty());
            Boolean b_amount = (amount==null);
            Boolean b_startTime = (startTime==null || startTime.trim().isEmpty());
            Boolean b_endTime = (endTime==null || endTime.trim().isEmpty());
            String created_date = (b_startTime ? "" : (" and created_date >= ?"))
                    + (b_endTime ? "" : (" and created_date <= ?"));
            String expired_date = (b_startTime ? "" : (" and expired_date >= ?"))
                    + (b_endTime ? "" : (" and expired_date <= ?"));
            String sql = "Select * from vinplay.event where 1=1 "
                    +(b_name ? "" : (" and name = ?"))
                    +(b_amount ? "" : (" and amount = ?"))
                    +(flagtime==1 ? created_date : "")
                    +(flagtime==2 ? expired_date : "")
                    +((maxItem!=-1) ? (" order by id desc limit ?,?") :"");
            PreparedStatement stmt = conn.prepareStatement(sql);
            if(!b_name){
                stmt.setString(index++, name);
            }
            if(!b_amount){
                stmt.setLong(index++, amount);
            }
            if(!b_startTime){
                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(startTime);
                stmt.setDate(index++, new java.sql.Date(date.getTime()));
            }
            if(!b_endTime){
                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(endTime);
                stmt.setDate(index++, new java.sql.Date(date.getTime()));
            }
            if(maxItem!=-1) {
                stmt.setInt(index++, page * maxItem);
                stmt.setInt(index, maxItem);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                EventModel eventModel = new EventModel();
                eventModel.setId(rs.getInt("id"));
                eventModel.setName(rs.getString("name"));
                eventModel.setAmount(rs.getLong("amount"));
                eventModel.setCreated_date(rs.getDate("created_date"));
                eventModel.setExpired_date(rs.getDate("expired_date"));
                events.add(eventModel);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return events;
    }

    @Override
    public Boolean addNewEvent(String name, String created_date, Long amount, String expired_date) {
        String sql = "INSERT INTO vinplay.event (name, created_date, amount, expired_date) VALUES(?,?,?,?)";
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");){
            PreparedStatement stm = conn.prepareStatement(sql);
            Date date1,date2;
            if(created_date==null || created_date.trim().isEmpty()){
                date1 = new Date();
            } else{
                date1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(created_date);
            }
            if(expired_date==null || expired_date.trim().isEmpty()){
                date2 = new Date();
            } else{
                date2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(expired_date);
            }
            stm.setString(1, name);
            stm.setDate(2, new java.sql.Date(date1.getTime()));
            stm.setLong(3, amount);
            stm.setDate(4, new java.sql.Date(date2.getTime()));
            stm.executeUpdate();
            stm.close();
            if (conn != null) {
                conn.close();
            }
        }
        catch (SQLException e) {
            return false;
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    @Override
    public EventModel eventDetail(Integer id) {
        EventModel eventModel = new EventModel();
        try(Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname")) {
            Boolean b_id = (id==null);
            String sql = "Select * from vinplay.event where id = ? ";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                eventModel.setId(rs.getInt("id"));
                eventModel.setName(rs.getString("name"));
                eventModel.setAmount(rs.getLong("amount"));
                eventModel.setCreated_date(rs.getDate("created_date"));
                eventModel.setExpired_date(rs.getDate("expired_date"));
                break;
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventModel;
    }

    @Override
    public Boolean updateEventById(Integer id, String name, String created_date, Long amount, String expired_date) {
        String sql = "UPDATE event SET " +
                ((amount == null ) ? "" : "amount = ?") +
                ((created_date != null && !created_date.trim().isEmpty()) ? ", " : " ") +
                ((created_date == null || created_date.trim().isEmpty()) ? "" : "created_date = ?") +
                ((expired_date != null && !expired_date.trim().isEmpty()) ? ", " : " ") +
                ((expired_date == null || expired_date.trim().isEmpty()) ? "" : "expired_date = ?") +
                ((name != null && !name.trim().isEmpty()) ? ", " : " ") +
                ((name == null || name.trim().isEmpty()) ? "" : "name = ?") +
                "WHERE id = ?";
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname")) {
            PreparedStatement stm = conn.prepareStatement(sql);
            int index = 1;
            if(amount != null) {
                stm.setLong(index++, amount);
            }
            if(created_date != null && !created_date.trim().isEmpty()) {
                stm.setString(index++, created_date);
            }
            if(expired_date != null && !expired_date.trim().isEmpty()) {
                stm.setString(index++, expired_date);
            }
            if(name != null && !name.trim().isEmpty()) {
                stm.setString(index++, name);
            }
            stm.setInt(index++, id);
            stm.executeUpdate();
        }catch (SQLException e){
            return false;
        }
        return true;
    }

    @Override
    public Boolean updateEventByName(String name, String created_date, Long amount, String expired_date) {
        String sql = "UPDATE event SET " +
                ((amount == null ) ? "" : "amount = ?") +
                ((created_date != null && !created_date.trim().isEmpty()) ? ", " : " ") +
                ((created_date == null || created_date.trim().isEmpty()) ? "" : "created_date = ?") +
                ((expired_date != null && !expired_date.trim().isEmpty()) ? ", " : " ") +
                ((expired_date == null || expired_date.trim().isEmpty()) ? "" : "expired_date = ?") +
                "WHERE name = ?";
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname")) {
            PreparedStatement stm = conn.prepareStatement(sql);
            int index = 1;
            if(amount != null) {
                stm.setLong(index++, amount);
            }
            if(created_date != null && !created_date.trim().isEmpty()) {
                stm.setString(index++, created_date);
            }
            if(expired_date != null && !expired_date.trim().isEmpty()) {
                stm.setString(index++, expired_date);
            }
            stm.setString(index++, name);
            stm.executeUpdate();
        }catch (SQLException e){
            return false;
        }
        return true;
    }

    @Override
    public Boolean deleteEvent(Integer id) {
        String sql = "DELETE FROM vinplay.event where id = ?";
        try(Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");) {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, id);
            stm.executeUpdate();
            stm.close();
            if (conn != null) {
                conn.close();
            }
        }
        catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean deleteEvent(String name) {
        String sql = "DELETE FROM vinplay.event where name = ?";
        try(Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");) {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, name);
            stm.executeUpdate();
            stm.close();
            if (conn != null) {
                conn.close();
            }
        }
        catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public List<MoonEventResponse> getListEventsMoon() {
        List<MoonEventResponse> events = new ArrayList<>();

        try(Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname")) {
            String sql = "SELECT id, name FROM vinplay.event WHERE name like 'moon-night%';";

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                MoonEventResponse eventModel = new MoonEventResponse();
                eventModel.setIdEvent(rs.getInt("id"));
                eventModel.setNameEvent(rs.getString("name"));
                events.add(eventModel);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return events;
    }

    @Override
    public MoonEventModel buyPackMoon(String nickname, int eventId) throws SQLException {
        MoonEventModel eModel = new MoonEventModel();
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
        CallableStatement call = null;

        try {
            call = conn.prepareCall("CALL SP_BuyPackMoon(?, ?, ?, ?)");
            int param = 1;
            call.setString(param++, nickname);
            call.setInt(param++, eventId);
//            call.registerOutParameter(param++, 4);
            call.registerOutParameter(param++, Types.BIGINT);
            call.registerOutParameter(param++, Types.INTEGER);
            call.execute();
            eModel.setAmount(call.getLong(3));
            eModel.setErrorCode(call.getInt(4));
        }
        catch (SQLException e) {
            throw e;
        }
        finally {
            if (call != null) {
                call.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return eModel;
    }
}
