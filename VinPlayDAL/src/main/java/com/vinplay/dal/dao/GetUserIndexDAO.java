/*
 * Decompiled with CFR 0.144.
 */
package com.vinplay.dal.dao;

import java.sql.SQLException;

public interface GetUserIndexDAO {
    public int getRegister(String var1, String var2, String refferal_code) throws SQLException;

    public int getRecharge(String var1, String var2, String refferal_code) throws SQLException;

    public int getSecMobile(String var1, String var2, String refferal_code) throws SQLException;

    public int getBoth(String var1, String var2, String refferal_code) throws SQLException;
}

