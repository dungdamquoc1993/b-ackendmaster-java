package com.vinplay.dal.service.impl;

import com.vinplay.dal.dao.MiniGameDAO;
import com.vinplay.dal.dao.impl.MiniGameDAOImpl;
import com.vinplay.dal.service.MiniGameService;
import com.vinplay.vbee.common.messages.BaseMessage;
import com.vinplay.vbee.common.messages.minigame.UpdateFundMessage;
import com.vinplay.vbee.common.messages.minigame.UpdatePotMessage;
import com.vinplay.vbee.common.response.BonusFundResponse;
import com.vinplay.vbee.common.rmq.RMQApi;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class MiniGameServiceImpl implements MiniGameService {
    private MiniGameDAO dao = new MiniGameDAOImpl();

    @Override
    public long getPot(String potName) throws SQLException {
        return this.dao.getPot(potName);
    }

    @Override
    public long[] getPots(String potName) throws SQLException {
        return this.dao.getPots(potName);
    }

    @Override
    public long getReferenceId(int gameId) throws SQLException {
        long referenceId = this.dao.getReferenceId(gameId);
        return referenceId;
    }

    @Override
    public long getFund(String fundName) throws SQLException {
        return this.dao.getFund(fundName);
    }

    @Override
    public void saveFund(String fundName, long value) throws IOException, TimeoutException, InterruptedException {
        UpdateFundMessage msg = new UpdateFundMessage();
        msg.fundName = fundName;
        msg.newValue = value;
        RMQApi.publishMessage("queue_fund", (BaseMessage)msg, 110);
    }

    @Override
    public long[] getFunds(String fundName) throws SQLException {
        return this.dao.getFunds(fundName);
    }

    @Override
    public List<BonusFundResponse> getFunds() throws SQLException {
        return this.dao.getFunds();
    }

    @Override
    public boolean saveReferenceId(long newRefenceId, int gameId) throws SQLException {
        return this.dao.saveReferenceId(newRefenceId, gameId);
    }

    @Override
    public void savePot(String potName, long value, boolean x2) throws IOException, TimeoutException, InterruptedException {
        UpdatePotMessage message = new UpdatePotMessage();
        message.potName = potName;
        message.newValue = value;
        RMQApi.publishMessage((String)"queue_pot", (BaseMessage)message, (int)106);
        CacheServiceImpl cacheService = new CacheServiceImpl();
        cacheService.setValue(potName, (int)value);
        cacheService.setValue(potName + "_x2", x2 ? 1 : 0);
    }
}

