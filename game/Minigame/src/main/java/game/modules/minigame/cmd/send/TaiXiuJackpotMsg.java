package game.modules.minigame.cmd.send;

import game.BaseMsgEx;
import game.modules.minigame.cmd.TaiXiuCMD;

import java.nio.ByteBuffer;

public class TaiXiuJackpotMsg extends BaseMsgEx {
	public long id;
	public long amount;

	public TaiXiuJackpotMsg() {
		super(TaiXiuCMD.TAI_XIU_JACKPOT);
	}

	public byte[] createData() {
		ByteBuffer buffer = this.makeBuffer();
		buffer.putLong(this.id);
		buffer.putLong(this.amount);
		return this.packBuffer(buffer);
	}
}
