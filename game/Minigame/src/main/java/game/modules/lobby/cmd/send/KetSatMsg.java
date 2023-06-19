/*
 * Decompiled with CFR 0.144.
 */
package game.modules.lobby.cmd.send;

import game.BaseMsgEx;
import java.nio.ByteBuffer;

public class KetSatMsg
extends BaseMsgEx {
    public long moneyUse;
    public long safe;

    public int type;

    public KetSatMsg() {
        super(20009);
    }

    public byte[] createData() {
        ByteBuffer bf = this.makeBuffer();
        bf.putLong(this.moneyUse);
        bf.putLong(this.safe);
        bf.putInt(type);
        return this.packBuffer(bf);
    }
}

