/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.vinplay.vbee.common.messages.LogGameMessage
 */
package com.vinplay.dal.dao;

import com.vinplay.vbee.common.messages.LogGameMessage;
import java.util.List;

public interface LogGameDAO {
    public List<LogGameMessage> searchLogGameByNickName(String var1, String var2, String var3, String var4, String var5, String var6, int var7);

    public int countSearchLogGameByNickName(String var1, String var2, String var3, String var4, String var5, String var6);

    public int countPlayerLogGameByNickName(String var1, String var2, String var3, String var4, String var5, String var6);

    public LogGameMessage getLogGameDetailBySessionID(String var1, String var2, String var3);
}

