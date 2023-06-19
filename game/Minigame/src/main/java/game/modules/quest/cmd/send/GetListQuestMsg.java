/*
 * Decompiled with CFR 0.144.
 */
package game.modules.quest.cmd.send;

import game.BaseMsgEx;
import game.modules.quest.cmd.DailyQuestCMDID;

import java.nio.ByteBuffer;

public class GetListQuestMsg
extends BaseMsgEx {
    public String listMission = "";

    public GetListQuestMsg(String listMission) {
        super(DailyQuestCMDID.GET_LIST_QUEST);
        this.listMission = listMission;
    }

    public byte[] createData() {
        ByteBuffer bf = this.makeBuffer();
        this.putStr(bf, this.listMission);
        return this.packBuffer(bf);
    }
}

