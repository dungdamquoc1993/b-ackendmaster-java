/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.vinplay.vbee.common.models.UserAdminInfo
 *  com.vinplay.vbee.common.models.UserModel
 */
package com.vinplay.usercore.service;

import com.vinplay.dal.entities.agent.DetailUserModel;
import com.vinplay.vbee.common.models.UserAdminInfo;
import com.vinplay.vbee.common.models.UserModel;

import java.sql.SQLException;
import java.util.List;

public interface UserForAdminService {
    public UserModel getUserNormalByNickName(String var1) throws SQLException;

    public boolean updateStatusDailyByNickName(String var1, int var2) throws SQLException;

    public List<UserAdminInfo> searchUserAdmin(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, int var9, int var10, String var11, String var12, String var13) throws SQLException;

    public int countSearchUserAdmin(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9) throws SQLException;

    public List<UserAdminInfo> searchUserAdmin(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, int var9, int var10, String var11, String var12, String var13, String var14) throws SQLException;

    public int countSearchUserAdmin(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, String var14) throws SQLException;

    public DetailUserModel searchDetailUser(String user_name, String nick_name) throws SQLException;

    public String changePasswordUser(String nick_name, String old_password, String new_password, String is_bot, String dai_ly, Boolean is_reset) throws SQLException;
}

