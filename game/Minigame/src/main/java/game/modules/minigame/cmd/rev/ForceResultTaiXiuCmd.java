package game.modules.minigame.cmd.rev;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;

import java.nio.ByteBuffer;

public class ForceResultTaiXiuCmd extends BaseCmd {
    public short betSide;
    public ForceResultTaiXiuCmd(DataCmd dataCmd){
        super(dataCmd);
        this.unpackData();
    }

    public void unpackData() {
        ByteBuffer bf = this.makeBuffer();
        this.betSide = this.readShort(bf);
    }
}
