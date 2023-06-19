/*
 * Decompiled with CFR 0.144.
 */
package game.modules.lobby.cmd.send;

import game.BaseMsgEx;
import java.nio.ByteBuffer;

public class ResultKetSatMsg
extends BaseMsgEx {
    public long moneyUse;
    public long safe;
    public long currentMoney;
    public int type;

    public ResultKetSatMsg() {
        super(20009);
    }

    public byte[] createData() {
        ByteBuffer bf = this.makeBuffer();
        bf.putLong(this.moneyUse);
        bf.putLong(this.safe);
        bf.putLong(this.currentMoney);
        bf.putInt(type);
        return this.packBuffer(bf);
    }
}

