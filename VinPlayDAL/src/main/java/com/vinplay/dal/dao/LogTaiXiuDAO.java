/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.vinplay.vbee.common.response.TaiXiuDetailReponse
 *  com.vinplay.vbee.common.response.TaiXiuResponse
 *  com.vinplay.vbee.common.response.TaiXiuResultResponse
 */
package com.vinplay.dal.dao;

import com.vinplay.vbee.common.response.TaiXiuDetailReponse;
import com.vinplay.vbee.common.response.TaiXiuResponse;
import com.vinplay.vbee.common.response.TaiXiuResultResponse;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface LogTaiXiuDAO {
	public int deleteLogTaiXiuByDay(int soNgay) throws SQLException;
	
    public List<TaiXiuResponse> listLogTaiXiu(String var1, String var2, String var3, String var4, String var5, String var6,String isBot, int var7) throws SQLException;

    public int countLogTaiXiu(String var1, String var2, String var3, String var4, String var5, String var6, String isBot) throws SQLException;

    public Map<String, Integer> countPlayerLogTaiXiu(String var1, String var2, String var3, String var4, String var5, String var6, String isBot) throws SQLException;

    public List<TaiXiuDetailReponse> getLogTaiXiuDetail(String var1, String var2, String var3, String var4, int botType, int var5) throws SQLException;

    public int countLogTaiXiuDetail(String var1, String var2, String var3, String var4, int botType) throws SQLException;

    public List<TaiXiuResultResponse> listLogTaiXiuResult(String var1, String var2, String var3, String var4, int var5) throws SQLException;

    public int countLogTaiXiuResult(String var1, String var2, String var3, String var4) throws SQLException;
}

