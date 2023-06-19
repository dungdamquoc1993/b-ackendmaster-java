/*
 * Decompiled with CFR 0.144.
 *
 * Could not load the following classes:
 *  bitzero.server.entities.User
 *  bitzero.server.extensions.BaseClientRequestHandler
 *  bitzero.server.extensions.data.BaseMsg
 *  bitzero.server.extensions.data.DataCmd
 *  bitzero.util.common.business.Debug
 *  com.vinplay.usercore.service.UserMissionService
 *  com.vinplay.usercore.service.impl.UserMissionServiceImpl
 *  com.vinplay.vbee.common.models.userMission.CompleteMissionObj
 *  com.vinplay.vbee.common.models.userMission.UserMissionResponse
 */
package game.modules.quest;

import bitzero.server.entities.User;
import bitzero.server.extensions.BaseClientRequestHandler;
import bitzero.server.extensions.data.BaseMsg;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.common.business.Debug;

import com.vinplay.dailyQuest.DailyQuestConfig;
import com.vinplay.dailyQuest.DailyQuestUtils;
import com.vinplay.dailyQuest.model.DailyQuestData;
import com.vinplay.dailyQuest.model.DailyQuestModel;
import game.GameConfig.GameConfig;
import game.modules.quest.cmd.DailyQuestCMDID;
import game.modules.quest.cmd.rev.ReceiveGiftCmd;
import game.modules.quest.cmd.send.GetListQuestMsg;
import game.modules.quest.cmd.send.ReceiveGiftMsg;

import java.util.ArrayList;
import java.util.List;

public class DailyQuestModule
        extends BaseClientRequestHandler {
//    private UserMissionService missionService = new UserMissionServiceImpl();

    public void init() {
        super.init();
    }

    public void handleClientRequest(User user, DataCmd dataCmd) {
        switch (dataCmd.getId()) {
            case DailyQuestCMDID
                    .GET_LIST_QUEST: {
                this.getListQuest(user);
                break;
            }
            case DailyQuestCMDID
                    .RECEIVE_LIST_QUEST: {
                this.receiveQuest(user, dataCmd);
                break;
            }
        }
    }

    private void getListQuest(User user) {
        DailyQuestModel dailyQuestModel = DailyQuestUtils.getDailyQuestModel(user.getName());
        Debug.trace("dailyQuestModel cached="+ GameConfig.gson.toJson(dailyQuestModel));
        //{"lastTimeChange":18818,"dailyGiftData":[{"isSuccess":false,"isReceive":false,"currentValue":0},{"isSuccess":false,"isReceive":false,"currentValue":0},{"isSuccess":false,"isReceive":false,"currentValue":0},{"isSuccess":false,"isReceive":false,"currentValue":0},{"isSuccess":false,"isReceive":false,"currentValue":0},{"isSuccess":false,"isReceive":false,"currentValue":0},{"isSuccess":false,"isReceive":false,"currentValue":0},{"isSuccess":false,"isReceive":false,"currentValue":0},{"isSuccess":false,"isReceive":false,"currentValue":0},{"isSuccess":false,"isReceive":false,"currentValue":0}],"userName":"brightc"}
        List<DailyQuestSendData> dailyQuestSendData = new ArrayList<>();
        //TODO dag die o day
        for (int i = 0; i < DailyQuestConfig.questActive.length; i++) {
            if (DailyQuestConfig.questActive[i]) {
                dailyQuestSendData.add(new DailyQuestSendData(DailyQuestConfig.allQuest[i],
                        dailyQuestModel.dailyGiftData.get(i), i));
            }
        }
        Debug.trace("dailyQuestSendData size="+ dailyQuestSendData.size());
        GetListQuestMsg getListQuestMsg = new GetListQuestMsg(GameConfig.gson.toJson(dailyQuestSendData));
        Debug.trace("getListQuestMsg ="+ GameConfig.gson.toJson(getListQuestMsg));
        this.send(getListQuestMsg, user);

    }

    private synchronized void receiveQuest(User user, DataCmd dataCmd) {
        ReceiveGiftCmd receiveGiftCmd = new ReceiveGiftCmd(dataCmd);
        boolean isSuccess = DailyQuestUtils.playerReceiveGift(user.getName(), receiveGiftCmd.indexQuest);
        ReceiveGiftMsg receiveGiftMsg = new ReceiveGiftMsg(user.getName(), isSuccess);
        this.send(receiveGiftMsg, user);
    }


}

