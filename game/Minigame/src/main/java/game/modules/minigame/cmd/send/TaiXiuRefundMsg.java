package game.modules.minigame.cmd.send;

import game.BaseMsgEx;
import game.modules.minigame.cmd.TaiXiuCMD;

import java.nio.ByteBuffer;

public class TaiXiuRefundMsg
extends BaseMsgEx {
    public long moneyRefund;

    public TaiXiuRefundMsg(long moneyRefund) {
        super(TaiXiuCMD.TAI_XIU_REFUND_MONEY);
        this.moneyRefund = moneyRefund;
    }

    public byte[] createData() {
        ByteBuffer bf = this.makeBuffer();
        bf.putLong(this.moneyRefund);
        return this.packBuffer(bf);
    }
}

