package game.modules.XocDia;

import bitzero.server.core.BZEventParam;
import bitzero.server.core.BZEventType;
import bitzero.server.core.IBZEvent;
import bitzero.server.core.IBZEventParam;
import bitzero.server.entities.User;
import bitzero.server.exceptions.BZException;
import bitzero.server.extensions.BaseClientRequestHandler;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.common.business.Debug;
import com.vinplay.game.XocDia.XocDiaHistoryModel;
import com.vinplay.game.XocDia.XocDiaSoiCauUtil;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import game.modules.XocDia.model.XocDiaFundModel;
import game.modules.XocDia.msg.in.BetXocDia;

public class GameXocDiaController extends BaseClientRequestHandler {
    private UserService userService = new UserServiceImpl();

    @Override
    public void handleClientRequest(User u, DataCmd data) {
        try{
            switch (data.getId()){
                case GameXocDiaCmdDefine.BET:{
                    this.bet(u,data);
                    break;
                }
                case GameXocDiaCmdDefine.HISTORY:{
                    this.getHistory(u);
                    break;
                }
            }
        }catch (Exception e){
            Debug.trace(e);
        }
    }

    public void init() {
        XocDiaRoomManager.createRoomXocDia();
        XocDiaFundModel.getInstance();
        this.getParentExtension().addEventListener(BZEventType.USER_DISCONNECT, this);
    }

    public void handleJoinRoom(User u){

    }

    public void handleLeaveRoom(User u){

    }

    private void getHistory(User u) { // lich su soi cau
        XocDiaSoiCauUtil.getListSoiCau();
    }

    private void bet(User u, DataCmd data) {
        BetXocDia cmd = new BetXocDia(data);
        if (cmd.money < 1) {
            return;
        }

    }

    public void handleServerEvent(IBZEvent ibzevent) throws BZException {
        if (ibzevent.getType() == BZEventType.USER_DISCONNECT) {
            User user = (User)ibzevent.getParameter((IBZEventParam) BZEventParam.USER);
            this.userDis(user);
        }
    }

    public void userDis(User u){
        this.handleLeaveRoom(u);
    }
}
