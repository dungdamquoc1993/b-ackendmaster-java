package com.vinplay.api.processors.hoantra;

import com.google.gson.Gson;
import com.vinplay.usercore.service.MailBoxService;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.MailBoxServiceImpl;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.enums.Games;
import com.vinplay.vbee.common.models.HoanTraModel;
import com.vinplay.vbee.common.models.LogReportModel;
import com.vinplay.vbee.common.pools.ConnectionPool;
import com.vinplay.vbee.common.response.BaseResponseModel;
import com.vinplay.vbee.common.utils.VinPlayUtils;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReturnMoney {
    private Gson gson = new Gson();
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private Connection conn = null;

    private Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
        }

        return conn;
    }

    public List<HoanTraModel> getMoneyHoanTra(Date date) throws SQLException {
        /***
         * Note: addMoneyHoanTra just true on dai_ly = 0
         */
        List<HoanTraModel> listHoanTraModel = new ArrayList<>();

        try (Connection conn = getConnection()) {
            String sql = "SELECT u.vip_point, u.vip_point_save, u.money_vp, l.* " +
                    " FROM vinplay.log_report_user l JOIN vinplay.users u ON l.nick_name = u.nick_name " +
                    " WHERE time_report = ? and dai_ly = 0 and u.is_bot = 0";

            PreparedStatement stm = conn.prepareStatement(sql);
            int param = 1;
            stm.setDate(param++, date);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                LogReportModel logReportModel = new LogReportModel();
                logReportModel.id = rs.getInt("id");
                logReportModel.nick_name = rs.getString("nick_name");
                logReportModel.time = df.format(rs.getDate("time_report"));
                logReportModel.wm = rs.getLong("wm");
                logReportModel.wm_win = rs.getLong("wm_win");
                logReportModel.ibc = rs.getLong("ibc");
                logReportModel.ibc_win = rs.getLong("ibc_win");
                logReportModel.cmd = rs.getLong("cmd");
                logReportModel.cmd_win = rs.getLong("cmd_win");
                logReportModel.ag = rs.getLong("ag");
                logReportModel.ag_win = rs.getLong("ag_win");
                logReportModel.tlmn = rs.getLong("tlmn");
                logReportModel.tlmn_win = rs.getLong("tlmn_win");
                logReportModel.bacay = rs.getLong("bacay");
                logReportModel.bacay_win = rs.getLong("bacay_win");
                logReportModel.xocdia = rs.getLong("xocdia");
                logReportModel.xocdia_win = rs.getLong("xocdia_win");
                logReportModel.minipoker = rs.getLong("minipoker");
                logReportModel.minipoker_win = rs.getLong("minipoker_win");
                logReportModel.slot_pokemon = rs.getLong("slot_pokemon");
                logReportModel.slot_pokemon_win = rs.getLong("slot_pokemon_win");
                logReportModel.baucua = rs.getLong("baucua");
                logReportModel.baucua_win = rs.getLong("baucua_win");
                logReportModel.taixiu = rs.getLong("taixiu");
                logReportModel.taixiu_win = rs.getLong("taixiu_win");
                logReportModel.caothap = rs.getLong("caothap");
                logReportModel.caothap_win = rs.getLong("caothap_win");
                logReportModel.slot_bitcoin = rs.getLong("slot_bitcoin");
                logReportModel.slot_bitcoin_win = rs.getLong("slot_bitcoin_win");
                logReportModel.slot_taydu = rs.getLong("slot_taydu");
                logReportModel.slot_taydu_win = rs.getLong("slot_taydu_win");
                logReportModel.slot_angrybird = rs.getLong("slot_angrybird");
                logReportModel.slot_angrybird_win = rs.getLong("slot_angrybird_win");
                logReportModel.slot_thantai = rs.getLong("slot_thantai");
                logReportModel.slot_thantai_win = rs.getLong("slot_thantai_win");
                logReportModel.slot_thethao = rs.getLong("slot_thethao");
                logReportModel.slot_thethao_win = rs.getLong("slot_thethao_win");
                logReportModel.slot_chiemtinh = rs.getLong("slot_chiemtinh");
                logReportModel.slot_chiemtinh_win = rs.getLong("slot_chiemtinh_win");
                logReportModel.taixiu_st = rs.getLong("taixiu_st");
                logReportModel.taixiu_st_win = rs.getLong("taixiu_st_win");
                logReportModel.deposit = rs.getLong("deposit");
                logReportModel.withdraw = rs.getLong("withdraw");
                logReportModel.slot_bikini = rs.getLong("slot_bikini");
                logReportModel.slot_bikini_win = rs.getLong("slot_bikini_win");
                logReportModel.slot_galaxy = rs.getLong("slot_galaxy");
                logReportModel.slot_galaxy_win = rs.getLong("slot_galaxy_win");
                logReportModel.ebet = rs.getLong("ebet");
                logReportModel.ebet_win = rs.getLong("ebet_win");
                int vippoint = rs.getInt("vip_point");
				HoanTraModel hoanTraModel = new HoanTraModel(logReportModel, vippoint);
				if (hoanTraModel.isHoanTra()) {
					listHoanTraModel.add(hoanTraModel);
				}
            }

            return listHoanTraModel;
        }
    }
    
    public HoanTraModel getMoneyHoanTra(Date date, String nickname) throws SQLException {
    	HoanTraModel hoanTraModel = new HoanTraModel();
        try (Connection conn = getConnection()) {
            String sql = "SELECT u.vip_point, u.vip_point_save, u.money_vp, l.* " +
                    " FROM vinplay.log_report_user l JOIN vinplay.users u ON l.nick_name = u.nick_name " +
                    " WHERE time_report = ? and nick_name=? and dai_ly = 0 and u.is_bot = 0";
            try {
	            PreparedStatement stm = conn.prepareStatement(sql);
	            int param = 1;
	            stm.setDate(param++, date);
	            stm.setString(param++, nickname);
	            ResultSet rs = stm.executeQuery();
	            while (rs.next()) {
	                LogReportModel logReportModel = new LogReportModel();
	                logReportModel.id = rs.getInt("id");
	                logReportModel.nick_name = rs.getString("nick_name");
	                logReportModel.time = df.format(rs.getDate("time_report"));
	                logReportModel.wm = rs.getLong("wm");
	                logReportModel.wm_win = rs.getLong("wm_win");
	                logReportModel.ibc = rs.getLong("ibc");
	                logReportModel.ibc_win = rs.getLong("ibc_win");
	                logReportModel.cmd = rs.getLong("cmd");
	                logReportModel.cmd_win = rs.getLong("cmd_win");
	                logReportModel.ag = rs.getLong("ag");
	                logReportModel.ag_win = rs.getLong("ag_win");
	                logReportModel.tlmn = rs.getLong("tlmn");
	                logReportModel.tlmn_win = rs.getLong("tlmn_win");
	                logReportModel.bacay = rs.getLong("bacay");
	                logReportModel.bacay_win = rs.getLong("bacay_win");
	                logReportModel.xocdia = rs.getLong("xocdia");
	                logReportModel.xocdia_win = rs.getLong("xocdia_win");
	                logReportModel.minipoker = rs.getLong("minipoker");
	                logReportModel.minipoker_win = rs.getLong("minipoker_win");
	                logReportModel.slot_pokemon = rs.getLong("slot_pokemon");
	                logReportModel.slot_pokemon_win = rs.getLong("slot_pokemon_win");
	                logReportModel.baucua = rs.getLong("baucua");
	                logReportModel.baucua_win = rs.getLong("baucua_win");
	                logReportModel.taixiu = rs.getLong("taixiu");
	                logReportModel.taixiu_win = rs.getLong("taixiu_win");
	                logReportModel.caothap = rs.getLong("caothap");
	                logReportModel.caothap_win = rs.getLong("caothap_win");
	                logReportModel.slot_bitcoin = rs.getLong("slot_bitcoin");
	                logReportModel.slot_bitcoin_win = rs.getLong("slot_bitcoin_win");
	                logReportModel.slot_taydu = rs.getLong("slot_taydu");
	                logReportModel.slot_taydu_win = rs.getLong("slot_taydu_win");
	                logReportModel.slot_angrybird = rs.getLong("slot_angrybird");
	                logReportModel.slot_angrybird_win = rs.getLong("slot_angrybird_win");
	                logReportModel.slot_thantai = rs.getLong("slot_thantai");
	                logReportModel.slot_thantai_win = rs.getLong("slot_thantai_win");
	                logReportModel.slot_thethao = rs.getLong("slot_thethao");
	                logReportModel.slot_thethao_win = rs.getLong("slot_thethao_win");
	                logReportModel.slot_chiemtinh = rs.getLong("slot_chiemtinh");
	                logReportModel.slot_chiemtinh_win = rs.getLong("slot_chiemtinh_win");
	                logReportModel.taixiu_st = rs.getLong("taixiu_st");
	                logReportModel.taixiu_st_win = rs.getLong("taixiu_st_win");
	                logReportModel.deposit = rs.getLong("deposit");
	                logReportModel.withdraw = rs.getLong("withdraw");
	                logReportModel.slot_bikini = rs.getLong("slot_bikini");
	                logReportModel.slot_bikini_win = rs.getLong("slot_bikini_win");
	                logReportModel.slot_galaxy = rs.getLong("slot_galaxy");
	                logReportModel.slot_galaxy_win = rs.getLong("slot_galaxy_win");
	                int vippoint = rs.getInt("vip_point");
					hoanTraModel = new HoanTraModel(logReportModel, vippoint);
	            }
	            
	            rs.close();
            } catch (Exception e) {
				throw e;
			} finally {
				conn.close();
			}
        } catch (Exception e) {
			throw e;
		} finally {
			conn.close();
		}
        
        return hoanTraModel;
    }

    public int deleteHoanTra(Date date, Boolean send_success) throws SQLException {
        try (Connection conn = getConnection()) {
            String sql = "DELETE FROM vinplay.log_hoan_tra WHERE time = ? "
                    + (send_success == null ? "" : " and send_success = ?");

            PreparedStatement stm = conn.prepareStatement(sql);

            int param = 1;
            stm.setDate(param++, date);
            if (send_success != null) {
                stm.setBoolean(param++, send_success);
            }

            int result = stm.executeUpdate();

            return result;
        }
    }
    
    public int deleteHoanTra(Date date, Boolean send_success, String nickname) throws SQLException {
    	int result = 0;
        try (Connection conn = getConnection()) {
            String sql = "DELETE FROM vinplay.log_hoan_tra WHERE time = ? and nick_name = ?"
                    + (send_success == null ? "" : " and send_success = ?");

            PreparedStatement stm = conn.prepareStatement(sql);
            int param = 1;
            stm.setDate(param++, date);
            stm.setString(param++, nickname);
            if (send_success != null) {
                stm.setBoolean(param++, send_success);
            }

            result = stm.executeUpdate();
        }catch (Exception e) {
			throw e;
		} finally {
			conn.close();
		}
        
        return result;
    }

    public int deleteHoanTra(Date date) throws SQLException {
        return deleteHoanTra(date, null);
    }

    public int updateHoanTra(HoanTraModel hoanTraModel, Boolean isSuccess, String message) throws SQLException {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE vinplay.log_hoan_tra SET send_success = ?, message = ? where `time`=? and nick_name = ?";
            PreparedStatement stm2 = conn.prepareStatement(sql);
            int param = 1;
            stm2.setBoolean(param++, isSuccess);
            stm2.setString(param++, message);
            stm2.setString(param++, hoanTraModel.time);
            stm2.setString(param, hoanTraModel.nick_name);
            return stm2.executeUpdate();
        }
    }

    public int insertHoanTraList(List<HoanTraModel> listHoanTraModel) throws SQLException {
        int countInsert = 0;

        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO vinplay.log_hoan_tra (nick_name,time,vip_point,total_money_sport,hoan_tra_sport,total_money_casino," +
                    " hoan_tra_casino, total_money_game, hoan_tra_game, vip_index) VALUE (?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement stm = conn.prepareStatement(sql);

            for (int i = 0; i < listHoanTraModel.size(); i++) {
                HoanTraModel hoanTraModel = listHoanTraModel.get(i);

                int param = 1;
                stm.setString(param++, hoanTraModel.nick_name);
                stm.setDate(param++, java.sql.Date.valueOf(hoanTraModel.time));
                stm.setInt(param++, hoanTraModel.vip_point);
                stm.setLong(param++, hoanTraModel.total_money_sport);
                stm.setLong(param++, hoanTraModel.hoan_tra_sport);
                stm.setLong(param++, hoanTraModel.total_money_casino);
                stm.setLong(param++, hoanTraModel.hoan_tra_casino);
                stm.setLong(param++, hoanTraModel.total_money_game);
                stm.setLong(param++, hoanTraModel.hoan_tra_game);
                stm.setInt(param++, hoanTraModel.vip_index);
                stm.addBatch();

                if (i % 50 == 0) {
                    int[] result = stm.executeBatch();
                    countInsert += result.length;
                }

            }

            int[] result = stm.executeBatch();
            countInsert += result.length;

            return countInsert;
        }
    }
    
    public boolean insertHoanTraList(HoanTraModel hoanTraModel) throws SQLException {
    	boolean result = false;
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO vinplay.log_hoan_tra (nick_name,time,vip_point,total_money_sport,hoan_tra_sport,total_money_casino," +
                    " hoan_tra_casino, total_money_game, hoan_tra_game, vip_index) VALUE (?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement stm = conn.prepareStatement(sql);
            int param = 1;
            stm.setString(param++, hoanTraModel.nick_name);
            stm.setDate(param++, java.sql.Date.valueOf(hoanTraModel.time));
            stm.setInt(param++, hoanTraModel.vip_point);
            stm.setLong(param++, hoanTraModel.total_money_sport);
            stm.setLong(param++, hoanTraModel.hoan_tra_sport);
            stm.setLong(param++, hoanTraModel.total_money_casino);
            stm.setLong(param++, hoanTraModel.hoan_tra_casino);
            stm.setLong(param++, hoanTraModel.total_money_game);
            stm.setLong(param++, hoanTraModel.hoan_tra_game);
            stm.setInt(param++, hoanTraModel.vip_index);
            stm.executeUpdate();
            result = true;
        } finally {
			try {
				conn.close();
			} catch (Exception e) { }
		}
        
        return result;
    }

    public int insertHoanTraHistory(HoanTraModel hoanTraModel, Boolean isSuccess, String responseAddHoantra) throws SQLException {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO vinplay.log_hoan_tra_histories (nick_name,time,vip_point,total_money_sport,hoan_tra_sport,total_money_casino," +
                    " hoan_tra_casino, total_money_game, hoan_tra_game, vip_index, send_success, message) VALUE (?,?,?,?,?,?,?,?,?,?,?, ?)";

            PreparedStatement stm = conn.prepareStatement(sql);

            int param = 1;
            stm.setString(param++, hoanTraModel.nick_name);
            stm.setDate(param++, java.sql.Date.valueOf(hoanTraModel.time));
            stm.setInt(param++, hoanTraModel.vip_point);
            stm.setLong(param++, hoanTraModel.total_money_sport);
            stm.setLong(param++, hoanTraModel.hoan_tra_sport);
            stm.setLong(param++, hoanTraModel.total_money_casino);
            stm.setLong(param++, hoanTraModel.hoan_tra_casino);
            stm.setLong(param++, hoanTraModel.total_money_game);
            stm.setLong(param++, hoanTraModel.hoan_tra_game);
            stm.setInt(param++, hoanTraModel.vip_index);
            stm.setBoolean(param++, isSuccess);
            stm.setString(param, responseAddHoantra);

            int result = stm.executeUpdate();

            return result;
        }
    }

    public List<HoanTraModel> getListHoanTra(java.util.Date date, String nick_name) throws SQLException {
    	List<HoanTraModel> listHoanTraModel = new ArrayList<>();
    	
    	String sql = "SELECT * FROM vinplay.log_hoan_tra where 1 =1 "
                + (date == null ? "" : " and time = ?")
                + (nick_name == null || nick_name.isEmpty() ? "" : " and nick_name = ?");

        try (Connection conn = getConnection()) {
            PreparedStatement stm = conn.prepareStatement(sql);

            int param = 1;
            if (date != null) {
                stm.setDate(param++, new Date(date.getTime()));
            }

            if (nick_name != null && !nick_name.isEmpty()) {
                stm.setString(param, nick_name);
            }

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                HoanTraModel hoanTraModel = new HoanTraModel();
                hoanTraModel.id = rs.getInt("id");
                hoanTraModel.nick_name = rs.getString("nick_name");
                hoanTraModel.time = df.format(rs.getDate("time"));
                hoanTraModel.vip_point = rs.getInt("vip_point");
                hoanTraModel.total_money_sport = rs.getLong("total_money_sport");
                hoanTraModel.hoan_tra_sport = rs.getLong("hoan_tra_sport");
                hoanTraModel.total_money_casino = rs.getLong("total_money_casino");
                hoanTraModel.hoan_tra_casino = rs.getLong("hoan_tra_casino");
                hoanTraModel.total_money_game = rs.getLong("total_money_game");
                hoanTraModel.hoan_tra_game = rs.getLong("hoan_tra_game");
                hoanTraModel.vip_index = rs.getInt("vip_index");
                hoanTraModel.send_success =  rs.getBoolean("send_success");
                hoanTraModel.created_at =  rs.getTimestamp("created_at");
                hoanTraModel.updated_at =  rs.getTimestamp("updated_at");
                hoanTraModel.message =  rs.getString("message");
                if(hoanTraModel.isHoanTra()) {
                	listHoanTraModel.add(hoanTraModel);
                }
            }

            return listHoanTraModel;
        }
    }

    public List<HoanTraModel> getListHoanTra(java.util.Date date, String nick_name, int page, int maxItem) throws SQLException {
        List<HoanTraModel> listHoanTraModel = new ArrayList<>();
        page = (page-1) <0 ? 0 : (page - 1);
        String sql = "SELECT * FROM vinplay.log_hoan_tra where 1 =1 "
                + (date == null ? "" : " and time = ?")
                + (nick_name == null || nick_name.isEmpty() ? "" : " and nick_name = ?")
                +" limit ?,?";

        try (Connection conn = getConnection()) {
            PreparedStatement stm = conn.prepareStatement(sql);

            int param = 1;
            if (date != null) {
                stm.setDate(param++, new Date(date.getTime()));
            }

            if (nick_name != null && !nick_name.isEmpty()) {
                stm.setString(param++, nick_name);
            }
            stm.setInt(param++, page*maxItem);
            stm.setInt(param, maxItem);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                HoanTraModel hoanTraModel = new HoanTraModel();
                hoanTraModel.id = rs.getInt("id");
                hoanTraModel.nick_name = rs.getString("nick_name");
                hoanTraModel.time = df.format(rs.getDate("time"));
                hoanTraModel.vip_point = rs.getInt("vip_point");
                hoanTraModel.total_money_sport = rs.getLong("total_money_sport");
                hoanTraModel.hoan_tra_sport = rs.getLong("hoan_tra_sport");
                hoanTraModel.total_money_casino = rs.getLong("total_money_casino");
                hoanTraModel.hoan_tra_casino = rs.getLong("hoan_tra_casino");
                hoanTraModel.total_money_game = rs.getLong("total_money_game");
                hoanTraModel.hoan_tra_game = rs.getLong("hoan_tra_game");
                hoanTraModel.vip_index = rs.getInt("vip_index");

                hoanTraModel.send_success =  rs.getBoolean("send_success");
                hoanTraModel.created_at =  rs.getTimestamp("created_at");
                hoanTraModel.updated_at =  rs.getTimestamp("updated_at");
                hoanTraModel.message =  rs.getString("message");

                listHoanTraModel.add(hoanTraModel);
            }

            return listHoanTraModel;
        }
    }

    public long countListHoanTra(java.util.Date date, String nick_name) throws SQLException {
        long count = 0;
        try (Connection conn = getConnection()) {
            String sql = "SELECT count(*) as cnt FROM vinplay.log_hoan_tra where 1 =1 "
                    + (date == null ? "" : " and time = ?")
                    + (nick_name == null || nick_name.isEmpty() ? "" : " and nick_name = ?");

            PreparedStatement stm = conn.prepareStatement(sql);

            int param = 1;
            if (date != null) {
                stm.setDate(param++, new Date(date.getTime()));
            }

            if (nick_name != null && !nick_name.isEmpty()) {
                stm.setString(param, nick_name);
            }

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                count = rs.getLong("cnt");
            }

            return count;
        }catch (Exception e) {
			return 1;
		}
        finally {
        	try{
        		conn.close();
        	}catch (Exception e) { }
		}
    }

    public List<HoanTraModel> getListHoanTraHistories(java.sql.Date date, String nick_name, int page, int limit) throws SQLException {
        List<HoanTraModel> listHoanTraModel = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM vinplay.log_hoan_tra_histories where 1 =1 "
                    + (date == null ? "" : " and time = ?")
                    + (nick_name == null || nick_name.isEmpty() ? "" : " and nick_name = ?")
                    + " order by id desc limit ?,?";
            PreparedStatement stm = conn.prepareStatement(sql);
            int param = 1;
            if (date != null) {
                stm.setDate(param++, date);
            }

            if (nick_name != null && !nick_name.isEmpty()) {
                stm.setString(param++, nick_name);
            }

            stm.setInt(param++, (page-1)*limit);
            stm.setInt(param, limit);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                HoanTraModel hoanTraModel = new HoanTraModel();
                hoanTraModel.id = rs.getInt("id");
                hoanTraModel.nick_name = rs.getString("nick_name");
                hoanTraModel.time = df.format(rs.getDate("time"));
                hoanTraModel.vip_point = rs.getInt("vip_point");
                hoanTraModel.total_money_sport = rs.getLong("total_money_sport");
                hoanTraModel.hoan_tra_sport = rs.getLong("hoan_tra_sport");
                hoanTraModel.total_money_casino = rs.getLong("total_money_casino");
                hoanTraModel.hoan_tra_casino = rs.getLong("hoan_tra_casino");
                hoanTraModel.total_money_game = rs.getLong("total_money_game");
                hoanTraModel.hoan_tra_game = rs.getLong("hoan_tra_game");
                hoanTraModel.vip_index = rs.getInt("vip_index");
                hoanTraModel.send_success = rs.getBoolean("send_success");
                hoanTraModel.created_at = rs.getTimestamp("created_at");
                hoanTraModel.updated_at = rs.getTimestamp("updated_at");
                hoanTraModel.message = rs.getString("message");
                listHoanTraModel.add(hoanTraModel);
            }

            return listHoanTraModel;
        }
    }
    
    public Map<String, Object> searchListHoanTraHistories(java.sql.Date date, String nick_name, int page, int limit) throws SQLException {
        List<HoanTraModel> listHoanTraModel = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM vinplay.log_hoan_tra_histories where 1 =1 "
                    + (date == null ? "" : " and time >= ? and time <= ?")
                    + (nick_name == null || nick_name.isEmpty() ? "" : " and nick_name = ?")
                    + " order by id desc limit ?,?";
            String sqlTotalRecords = "SELECT count(*) as totalrecords FROM vinplay.log_hoan_tra_histories where 1 =1 "
                    + (date == null ? "" : " and time >= ? and time <= ?")
                    + (nick_name == null || nick_name.isEmpty() ? "" : " and nick_name = ?");
            String sqlSum = "SELECT ifnull(sum(ifnull(total_money_sport,0)),0) sporttotalsum,"
            		+ " ifnull(sum(ifnull(total_money_casino,0)),0) casinototalsum,"
            		+ " ifnull(sum(ifnull(total_money_game,0)),0) egametotalsum,"
            		+ " ifnull(sum(ifnull(hoan_tra_sport,0)),0) sportfundsum,"
            		+ " ifnull(sum(ifnull(hoan_tra_casino,0)),0) casinofundsum,"
            		+ " ifnull(sum(ifnull(hoan_tra_game,0)),0) egamefundsum"
            		+ " FROM vinplay.log_hoan_tra_histories where 1 =1 "
                    + (date == null ? "" : " and time >= ? and time <= ?")
                    + (nick_name == null || nick_name.isEmpty() ? "" : " and nick_name = ?");
            PreparedStatement stm = conn.prepareStatement(sql);
            PreparedStatement stmTotalRecords = conn.prepareStatement(sqlTotalRecords);
            PreparedStatement stmSum = conn.prepareStatement(sqlSum);
            int param = 1;
            if (date != null) {
                stm.setString(param, date.toString() + " 00:00:00");
                stmTotalRecords.setString(param, date.toString() + " 00:00:00");
                stmSum.setString(param, date.toString() + " 00:00:00");
                param++;
                stm.setString(param, date.toString() + " 23:59:59");
                stmTotalRecords.setString(param, date.toString() + " 23:59:59");
                stmSum.setString(param, date.toString() + " 23:59:59");
                param++;
            }

            if (nick_name != null && !nick_name.isEmpty()) {
                stm.setString(param, nick_name);
                stmTotalRecords.setString(param, nick_name);
                stmSum.setString(param, nick_name);
                param++;
            }

            stm.setInt(param++, (page-1)*limit);
            stm.setInt(param++, limit);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                HoanTraModel hoanTraModel = new HoanTraModel();
                hoanTraModel.id = rs.getInt("id");
                hoanTraModel.nick_name = rs.getString("nick_name");
                hoanTraModel.time = df.format(rs.getDate("time"));
                hoanTraModel.vip_point = rs.getInt("vip_point");
                hoanTraModel.total_money_sport = rs.getLong("total_money_sport");
                hoanTraModel.hoan_tra_sport = rs.getLong("hoan_tra_sport");
                hoanTraModel.total_money_casino = rs.getLong("total_money_casino");
                hoanTraModel.hoan_tra_casino = rs.getLong("hoan_tra_casino");
                hoanTraModel.total_money_game = rs.getLong("total_money_game");
                hoanTraModel.hoan_tra_game = rs.getLong("hoan_tra_game");
                hoanTraModel.vip_index = rs.getInt("vip_index");
                hoanTraModel.send_success = rs.getBoolean("send_success");
                hoanTraModel.created_at = rs.getTimestamp("created_at");
                hoanTraModel.updated_at = rs.getTimestamp("updated_at");
                hoanTraModel.message = rs.getString("message");
                listHoanTraModel.add(hoanTraModel);
            }
            
            result.put("records", listHoanTraModel);
            ResultSet rsSum = stmSum.executeQuery();
            while (rsSum.next()) {
            	result.put("sportTotalSum", rsSum.getLong("sporttotalsum"));
            	result.put("casinoTotalSum", rsSum.getLong("casinototalsum"));
            	result.put("egameTotalSum", rsSum.getLong("egametotalsum"));
            	result.put("sportFundSum", rsSum.getLong("sportfundsum"));
            	result.put("casinoFundSum", rsSum.getLong("casinofundsum"));
            	result.put("egameFundSum", rsSum.getLong("egamefundsum"));
            }
            
            ResultSet rsTotalRecords = stmTotalRecords.executeQuery();
            while (rsTotalRecords.next()) {
            	result.put("totalrecords", rsTotalRecords.getLong("totalrecords"));
            }
        }catch (Exception e) {
        	result = new HashMap<>();
        	result.put("records", new ArrayList<>());
        	result.put("sportTotalSum", 0);
        	result.put("casinoTotalSum", 0);
        	result.put("egameTotalSum", 0);
        	result.put("sportFundSum", 0);
        	result.put("casinoFundSum", 0);
        	result.put("egameFundSum", 0);
        	result.put("totalrecords", 0);
		}
        
        return result;
    }

    public long countListHoanTraHistories(java.sql.Date date, String nick_name) throws SQLException {
        long count = 0;
        try (Connection conn = getConnection()) {
            String sql = "SELECT count(*) as cnt FROM vinplay.log_hoan_tra_histories where 1 =1 "
                    + (date == null ? "" : " and time = ?")
                    + (nick_name == null || nick_name.isEmpty() ? "" : " and nick_name = ?");

            PreparedStatement stm = conn.prepareStatement(sql);

            int param = 1;
            if (date != null) {
                stm.setDate(param++, date);
            }

            if (nick_name != null && !nick_name.isEmpty()) {
                stm.setString(param, nick_name);
            }

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                count = rs.getLong("cnt");
            }

            return count;
        }
    }

	public int[] addMoneyHoanTraToUser(List<HoanTraModel> listHoanTraModel) throws SQLException {
		int countSuccess = 0;
		int countFail = 0;
		
		Map<String,Long> lstNickName = new HashMap<>();
		for (HoanTraModel hoanTraModel : listHoanTraModel) {
			long moneyReturn = hoanTraModel.hoan_tra_casino + hoanTraModel.hoan_tra_sport + hoanTraModel.hoan_tra_game;
			if (moneyReturn > 0) {
				BaseResponseModel responseModel = addMoneyHoanTra(hoanTraModel);
				if (responseModel.isSuccess()) {
					updateHoanTra(hoanTraModel, true, "");
					insertHoanTraHistory(hoanTraModel, true, "");
					countSuccess++;
					lstNickName.put(hoanTraModel.nick_name, moneyReturn);
				} else {
					updateHoanTra(hoanTraModel, false, responseModel.toJson());
					insertHoanTraHistory(hoanTraModel, false, responseModel.toJson());
					countFail++;
				}
			}
		}
		if (lstNickName != null && !lstNickName.isEmpty()) {
			MailBoxService mailService = new MailBoxServiceImpl();
			lstNickName.forEach((k, v) -> {
				mailService.sendMailBoxFromByNickNameAdmin(k, "Hoàn Trả hàng ngày "+ VinPlayUtils.getYesterday(),
						"Chúc mừng quý khách đã nhận về " + v
								+ " tiền hoàn cược vào tài khoản chính theo chương trình hoàn trả mỗi ngày. \r\n"
								+ "Chúc quý khách chơi to thắng lớn cùng Roy88");

			});
		}
		return new int[] { countSuccess, countFail };
	}

    public BaseResponseModel addMoneyHoanTra(HoanTraModel hoanTraModel) {
    		UserService userService = new UserServiceImpl();
            BaseResponseModel baseResponseModel = userService.updateMoneyFromAdmin(
                    hoanTraModel.nick_name,
                    hoanTraModel.hoan_tra_casino + hoanTraModel.hoan_tra_sport + hoanTraModel.hoan_tra_game,
                    "vin",
                    Games.HOAN_TRA.getName(),
                    Games.HOAN_TRA.getId() + "",
                    gson.toJson(new HoanTraDescription(hoanTraModel.time)));
            return baseResponseModel;
    }

    public int[] insertHoanTra(Date date) throws SQLException {
        int deleteRecords = this.deleteHoanTra(date);
        List<HoanTraModel> hoanTraModels = this.getMoneyHoanTra(date);
        int countInsert = this.insertHoanTraList(hoanTraModels);

        return new int[]{countInsert, deleteRecords};
    }
    
    public boolean insertHoanTra(Date date, String nickname) throws SQLException {
    	HoanTraModel hoanTraModel = this.getMoneyHoanTra(date, nickname);
        return insertHoanTraList(hoanTraModel);
    }

    public int[] addMoneyHoanTra(Date date) throws SQLException {
        List<HoanTraModel> hoanTraModels = this.getListHoanTra(date, null);
        int[] countSendHoanTra = this.addMoneyHoanTraToUser(hoanTraModels);
//        just delete success user for resend hoanTra
        deleteHoanTra(date, true);

        return countSendHoanTra;
    }
    
    public int[] addMoneyHoanTra(Date date, String nickname) throws SQLException {
        List<HoanTraModel> hoanTraModels = this.getListHoanTra(date, nickname);
        int[] countSendHoanTra = this.addMoneyHoanTraToUser(hoanTraModels);
//        just delete success user for resend hoanTra
        deleteHoanTra(date, true, nickname);

        return countSendHoanTra;
    }
}
