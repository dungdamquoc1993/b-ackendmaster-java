package game.modules.XocDia.model;

import bitzero.server.entities.Room;
import bitzero.util.common.business.Debug;
import com.vinplay.dal.service.MiniGameService;
import com.vinplay.dal.service.impl.MiniGameServiceImpl;
import com.vinplay.vbee.common.enums.Games;
import game.modules.XocDia.XocDiaRoomManager;
import game.modules.XocDia.model.bet.BetItem;
import game.modules.XocDia.model.bet.XocDiaBetDoorItem;
import game.utils.GameUtil;

public class XocDiaBetModel {

    public static XocDiaBetModel _instance = null;

    public static XocDiaBetModel getInstance() {
        if (_instance == null) {
            synchronized (XocDiaBetModel.class) {
                if (_instance == null) {
                    _instance = new XocDiaBetModel();
                }
            }
        }
        return _instance;
    }

    public MiniGameService mgService = new MiniGameServiceImpl();

    private XocDiaBetModel() {
        try {
            this.referenceIdXocDia = this.mgService.getReferenceId(Games.XOC_DIA.getId());
        } catch (Exception e) {
            Debug.trace(e);
        }
    }

    public void reset() {
        for (XocDiaBetDoorItem item : mDoor) {
            if (item != null)
                item.clear();
        }
        this.listBotBet = new long[6];
        this.totalBet = new long[6];
        this.startTime = GameUtil.getTimeStampInSeconds();
        this.status = BET_PHASE;
        this.isStart = 1;
        this.flagRunPayMoney = false;
        this.flagRunDice = false;
        this.referenceIdXocDia++;
        this.saveReferenceIdXocDia();
    }

    public void saveReferenceIdXocDia() {
        try {
            this.mgService.saveReferenceId(this.referenceIdXocDia, Games.XOC_DIA.getId());
        } catch (Exception e) {
            Debug.trace(e);
        }
    }


    //biến cờ đảm bảo cân kèo, quay xúc xắc, trả tiền chỉ thực hiên 1 lần
    public boolean flagRunPayMoney = false;
    public boolean flagRunDice = false;

    public long referenceIdXocDia = 0;
    public static final byte BET_PHASE = 0;
    public static final byte DICE_PHASE = 1;
    public static final byte PAY_PHASE = 2;

    public long[] totalBet = new long[6];

    public long startTime = GameUtil.getTimeStampInSeconds();
    public byte isStart = 1;
    public byte status = 0;

    public long[] listBotBet = new long[6];

    public XocDiaBetDoorItem[] mDoor = new XocDiaBetDoorItem[]{
            new XocDiaBetDoorItem(),
            new XocDiaBetDoorItem(),
            new XocDiaBetDoorItem(),
            new XocDiaBetDoorItem(),
            new XocDiaBetDoorItem()
    };

    public void updateStatus(byte status) {
        this.status = status;
        this.startTime = GameUtil.getTimeStampInSeconds();
    }

    public synchronized boolean bet(long uId, String userName, byte door, long money) {
        return bet(uId, userName, door, money, false);
    }

    public synchronized boolean bet(long uId, String userName, byte door, long money, boolean isBot) {
        if (this.status != BET_PHASE) {
            return false;
        }


        if (isBot) {
            this.listBotBet[door] += money;

        }

        if (door >= 0 && door < 6) {
            if (this.mDoor[door].listBet.containsKey(uId)) {
                this.mDoor[door].listBet.get(uId).money += money;
            } else {
                this.mDoor[door].listBet.put(uId, new BetItem(uId, money, userName, isBot));
            }
            this.totalBet[door] += money;
        }
        //todo send to usser
//        Room room = XocDiaRoomManager.getRoomToJoin();
//        ExtensionUtility.instance().send(new BetXocDiaMsg(uId,door,money),room.getSessionList());
        return true;
    }
}
