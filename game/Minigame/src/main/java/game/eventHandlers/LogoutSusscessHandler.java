package game.eventHandlers;

import bitzero.server.core.BZEventParam;
import bitzero.server.core.IBZEvent;
import bitzero.server.core.IBZEventParam;
import bitzero.server.entities.User;
import bitzero.server.exceptions.BZException;
import bitzero.server.extensions.BaseServerEventHandler;
import bitzero.util.ExtensionUtility;
import bitzero.util.common.business.Debug;
import com.vinplay.dailyQuest.DailyQuestUtils;

public class LogoutSusscessHandler extends BaseServerEventHandler {
    public void handleServerEvent(IBZEvent ibzevent) throws BZException {
        this.onLogoutSuccess((User)ibzevent.getParameter((IBZEventParam) BZEventParam.USER));
    }

    private void onLogoutSuccess(User user) {
        ExtensionUtility.instance().sendLogoutOK(user);
        Debug.trace("logout sucess  ",user.getName());
//        DailyQuestUtils.playerLogin(user.getName());
    }
}
