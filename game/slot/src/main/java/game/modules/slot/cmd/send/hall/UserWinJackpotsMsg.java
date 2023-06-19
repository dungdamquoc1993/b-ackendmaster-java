/*
 * Decompiled with CFR 0.144.
 *
 * Could not load the following classes:
 *  bitzero.server.extensions.data.BaseMsg
 */
package game.modules.slot.cmd.send.hall;

import bitzero.server.extensions.data.BaseMsg;
import game.modules.slot.cmd.SlotCMD;

import java.nio.ByteBuffer;

public class UserWinJackpotsMsg
extends BaseMsg {
    String gameID = "";
    long moneyWin = 0;
    int type = 0;

    public UserWinJackpotsMsg(String gameID, long moneyWin, byte type) {
        super(SlotCMD.USER_WIN_JACKPOT);
        this.gameID = gameID;
        this.moneyWin = moneyWin;
        this.type = type;
    }

    public byte[] createData() {
        ByteBuffer bf = this.makeBuffer();
        this.putStr(bf, this.gameID);
        this.putLong(bf, this.moneyWin);
        bf.putInt(this.type);
        return this.packBuffer(bf);
    }
}

