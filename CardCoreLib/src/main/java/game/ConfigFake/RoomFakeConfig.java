package game.ConfigFake;

import bitzero.util.common.business.CommonHandle;
import bitzero.util.common.business.Debug;
import com.vinplay.vbee.common.models.ConfigGame;

public class RoomFakeConfig {
    public int[] fakeRoomTLMN = new int[5];
    public int[] fakeRoomTLMNSolo = new int[5];
    public int[] fakeRoomSam = new int[5];
    public int[] fakeRoomSamSL = new int[5];

    private static Object lock = new Object();
    public static RoomFakeConfig _instance = null;

    private RoomFakeConfig(){};

    public static RoomFakeConfig getInstance(){
        if(_instance == null){
            synchronized (lock){
                if(_instance == null){
                    _instance = new RoomFakeConfig();
                }
            }
        }
        return _instance;
    }

    public void changeConfigFakeRoom(){ // change sau 1 phut
        this.fakeRoomTLMN = ConfigFakeTLMN.getInstance().getCurrentFakeRoomTLMN();
        this.fakeRoomTLMNSolo = ConfigFakeTLMN.getInstance().getCurrentFakeRoomTLMNSL();
    }

    public static int getIndexWithBet(long bet){
        if(bet == 100) return 0;
        if(bet == 500) return 1;
        if(bet == 1000) return 2;
        if(bet == 2000) return 3;
        if(bet == 3000) return 4;
        return -1;
    }

    public int getBonusNumberPlayerTLMN(long bet){
        int index = getIndexWithBet(bet);
        if(index<0) return  0;
//        CommonHandle.writeErrLog("test TLMN   "+ bet + "   " + index + "   " + this.fakeRoomTLMN[index]);
        return this.fakeRoomTLMN[index]*ConfigFakeTLMN.NUMBER_ROOM_TLMN;
    }

    public int getBonusNumberPlayerTLMNSL(long bet){
        int index = getIndexWithBet(bet);
        if(index<0) return 0;
        return this.fakeRoomTLMNSolo[index]*ConfigFakeTLMN.NUMBER_ROOM_TLMN_SOLO;
    }
}
