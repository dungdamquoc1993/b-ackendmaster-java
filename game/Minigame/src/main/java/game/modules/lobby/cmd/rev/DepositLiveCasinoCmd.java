package game.modules.lobby.cmd.rev;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;

import java.nio.ByteBuffer;

public class DepositLiveCasinoCmd extends BaseCmd {
    public long Amount;
    //public String PhoneNumberReceived;
    public DepositLiveCasinoCmd(DataCmd data) {
        super(data);
        this.unpackData();
    }
    public void unpackData(){
        ByteBuffer bf = this.makeBuffer();
        this.Amount = bf.getLong();
        //this.PhoneNumberReceived = this.readString(bf);

    }
}
