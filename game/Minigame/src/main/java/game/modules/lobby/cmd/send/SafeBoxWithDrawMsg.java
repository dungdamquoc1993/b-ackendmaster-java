package game.modules.lobby.cmd.send;

import game.BaseMsgEx;

import java.nio.ByteBuffer;


public class SafeBoxWithDrawMsg extends BaseMsgEx {

    public String message;
    public SafeBoxWithDrawMsg() {
        super(20312);
    }

    public byte[] createData() {
        ByteBuffer bf = this.makeBuffer();
        this.putStr(bf, this.message);
        return this.packBuffer(bf);
    }
}
