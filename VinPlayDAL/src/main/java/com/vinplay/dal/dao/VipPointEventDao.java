/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.vinplay.vbee.common.response.LogVipPointEventResponse
 *  com.vinplay.vbee.common.response.UserVipPointEventResponse
 */
package com.vinplay.dal.dao;

import com.vinplay.dal.entities.agent.TopVippointResponse;
import com.vinplay.vbee.common.response.LogVipPointEventResponse;
import com.vinplay.vbee.common.response.UserVipPointEventResponse;
import java.sql.SQLException;
import java.util.List;

public interface VipPointEventDao {
    public List<LogVipPointEventResponse> listLogVipPointEvent(String var1, String var2, String var3, String var4, String var5, int var6, String var7);

    public long countLogVipPointEvent(String var1, String var2, String var3, String var4, String var5, String var6);

    public long totalVipPointEvent(String var1, String var2, String var3, String var4, String var5, String var6);

    public List<UserVipPointEventResponse> listuserVipPoint(String var1, String var2, String var3, String var4, String var5, int var6, String var7) throws SQLException;

    public long countUserVipPoint(String var1, String var2, String var3, String var4, String var5, String var6) throws SQLException;
    
    public List<TopVippointResponse> GetTopVippointAgency(int skip, int pageSize, String client) throws SQLException;

    public List<TopVippointResponse> GetTopVippointAgencyByNN(int skip, int pageSize, List<String> nicknames, String client) throws SQLException;

    public List<String> GetAgentByParent(int parentId) throws SQLException;
}

