package com.vinplay.vbee.rmq.minigame.processor;

import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.messages.BaseMessage;
import com.vinplay.vbee.common.messages.minigame.pokego.LogPokeGoMessage;
import com.vinplay.vbee.dao.impl.GalaxyGoDaoImpl;

public class LogGalaxyProcessor implements BaseProcessor<byte[], Boolean> {
    public Boolean execute(Param<byte[]> param) {
        LogPokeGoMessage message = (LogPokeGoMessage) BaseMessage.fromBytes((byte[])((byte[])param.get()));
        GalaxyGoDaoImpl dao = new GalaxyGoDaoImpl();
        dao.log(message);
        return true;
    }
}
