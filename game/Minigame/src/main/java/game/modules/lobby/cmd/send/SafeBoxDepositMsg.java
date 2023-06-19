package game.modules.lobby.cmd.send;

import game.BaseMsgEx;

import java.nio.ByteBuffer;


public class SafeBoxDepositMsg extends BaseMsgEx {

    public String message;
    public SafeBoxDepositMsg() {
        super(20310);
    }

    public byte[] createData() {
        ByteBuffer bf = this.makeBuffer();
        this.putStr(bf, this.message);
        return this.packBuffer(bf);
    }
}
