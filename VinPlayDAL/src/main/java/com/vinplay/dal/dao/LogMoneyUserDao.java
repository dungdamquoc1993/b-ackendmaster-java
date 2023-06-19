/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.vinplay.vbee.common.messages.gamebai.LogNoHuGameBaiMessage
 *  com.vinplay.vbee.common.models.UserModel
 *  com.vinplay.vbee.common.response.LogMoneyUserResponse
 *  com.vinplay.vbee.common.response.LogUserMoneyResponse
 */
package com.vinplay.dal.dao;

import com.vinplay.dal.entities.gamebai.TopGameBaiModel;
import com.vinplay.vbee.common.messages.gamebai.LogNoHuGameBaiMessage;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.response.LogMoneyUserResponse;
import com.vinplay.vbee.common.response.LogUserMoneyResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface LogMoneyUserDao {
    public List<LogUserMoneyResponse> searchLogMoneyUser(String var1, String var2, String var3, String var4, String var5, String var6, String var7, int var8, int var9, int var10);
    public List<LogUserMoneyResponse> searchLogMoneyUser(String nickName, String referral_code, String serviceName, String actionName, String timeStart,
                                                         String timeEnd, int page,  int totalRecord);

    public List<LogUserMoneyResponse> searchAllLogMoneyUser(String nick_name,String type, boolean seven_days);
    
    public long getTotalBetWin(String nick_name, String type,String action_name);
    
    public LogUserMoneyResponse searchLastLogMoneyUser(String nick_name, String type, ArrayList<String> agents);

    public int countsearchLogMoneyUser(String var1, String var2, String var3, String var4, String var5, String var6, int var7);

    public List<LogMoneyUserResponse> getHistoryTransactionLogMoney(String var1, int var2, int var3);

    public List<LogMoneyUserResponse> getTransactionList(String var1, int var2, int var3, int var4);

    public int countHistoryTransactionLogMoney(String var1, int var2);

    public Map<String, TopGameBaiModel> getTopGameBai(String var1);

    public List<LogNoHuGameBaiMessage> getNoHuGameBaiHistory(int var1, String var2);

    public int countNoHuGameBaiHistory();

    public UserModel getUserByNickName(String var1) throws SQLException;

//    public List<LogUserMoneyResponse> searchLogMoneyTranferUser(String var1, String var2, String var3, String var4, int var5);

//    public boolean UpdateProcessLogChuyenTienDaiLy(String var1, String var2, String var3, String var4);
//
//    public boolean UpdateProcessLogChuyenTienDaiLyMySQL(String var1, String var2, String var3, String var4) throws SQLException;

    public Map<String, Object> getLogMoneyUserNapVin(Long trans_id, Integer user_id, String nick_name, String service_name, Long current_money,
    Long money_exchange, String description, String fromTime, String endTime, String action_name, Long fee, Date create_time, int page, int maxItem);

    public Map<String, Object> getLogMoneyUserTieuVin(Long trans_id, Integer user_id, String nick_name, String service_name, Long current_money,
    Long money_exchange, String description, String fromTime, String endTime, String action_name, Long fee, Date create_time, int page, int maxItem);

    public Map<String, Object> getLogMoneyUserVin(Long trans_id, Integer user_id, String nick_name, String service_name, Long current_money,
    Long money_exchange, String description, String fromTime, String endTime, String action_name, Long fee, Date create_time, Boolean is_bot, Boolean play_game, int page, int maxItem);

    public List<LogMoneyUserResponse> getHisTLMN(String nickName, int queryType, int start, int end);

    public List<LogMoneyUserResponse> getHisXD(String nickName, int queryType, int start, int end);
    
    public List<Map<String, Object>> searchLogMoneyUser4Report(String nickName, String userName, String moneyType, String serviceName, String actionName, String timeStart, String timeEnd, int page, int totalRecord);
    
    public List<Map<String, Object>> getLogMoneyUserVin4Report(Long trans_id, Integer user_id, String nick_name, String service_name, Long current_money, Long money_exchange, String description, String fromTime, String endTime, String action_name, Long fee, Date create_time, Boolean is_bot, Boolean play_game, int page, int maxItem);
}

