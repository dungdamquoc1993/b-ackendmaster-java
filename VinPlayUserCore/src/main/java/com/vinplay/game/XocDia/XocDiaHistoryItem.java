package com.vinplay.game.XocDia;

import java.io.Serializable;

public class XocDiaHistoryItem implements Serializable {
    private static final long serialVersionUID = -8655119501137984381L;

    public long gamePlayId = 0;
    public byte[] data;

    public XocDiaHistoryItem(long gamePlayId, byte[] data) {
        this.gamePlayId = gamePlayId;
        this.data = data;
    }
}
