package com.vinplay.vbee.dao;

import com.vinplay.vbee.common.messages.LogMoneyUserMessage;
import com.vinplay.vbee.common.messages.gamebai.LogNoHuGameBaiMessage;
import com.vinplay.vbee.common.messages.pay.ExchangeMessage;
import com.vinplay.vbee.common.models.TopUserPlayGame;
import java.sql.SQLException;

public interface LogMoneyUserDao {
    public boolean saveLogMoneyUser(LogMoneyUserMessage var1, long var2, boolean var4, boolean var5);

    public boolean saveLogMoneyUserVinOther(LogMoneyUserMessage var1, long var2, int var4);

    public void saveLogMoneySystem(String var1, long var2, long var4, String var6);

    public long getLastReferenceId(String var1);

    public boolean logTopUserPlayGame(TopUserPlayGame var1);

    public boolean logNoHuGameBai(LogNoHuGameBaiMessage var1);

    public boolean checkBot(String var1) throws SQLException;

    public boolean logExchangeMoney(ExchangeMessage var1);
}

