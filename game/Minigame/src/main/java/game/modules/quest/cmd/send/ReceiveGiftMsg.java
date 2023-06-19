/*
 * Decompiled with CFR 0.144.
 */
package game.modules.quest.cmd.send;

import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import game.BaseMsgEx;
import game.modules.quest.cmd.DailyQuestCMDID;

import java.nio.ByteBuffer;

public class ReceiveGiftMsg
        extends BaseMsgEx {
    public boolean isSuccess = false;
    public long currentMoney;

    public ReceiveGiftMsg(String userName, boolean isSuccess) {
        super(DailyQuestCMDID.RECEIVE_LIST_QUEST);
        UserService userService = new UserServiceImpl();
        this.isSuccess = isSuccess;
        this.currentMoney = userService.getCurrentMoneyUserCache(userName, "vin");
    }

    public byte[] createData() {
        ByteBuffer bf = this.makeBuffer();
        putBoolean(bf, this.isSuccess);
        bf.putLong(this.currentMoney);
        return this.packBuffer(bf);
    }
}

