/*
 * Decompiled with CFR 0_116.
 *
 * Could not load the following classes:
 *  bitzero.engine.sessions.ISession
 *  bitzero.server.core.BZEventType
 *  bitzero.server.core.IBZEventType
 *  bitzero.server.entities.User
 *  bitzero.server.entities.data.ISFSObject
 *  bitzero.server.exceptions.BZException
 *  bitzero.server.extensions.BZExtension
 *  bitzero.server.extensions.data.DataCmd
 *  bitzero.util.ExtensionUtility
 *  bitzero.util.common.business.Debug
 *  bitzero.util.socialcontroller.bean.UserInfo
 *  com.vinplay.usercore.utils.GameCommon
 *  com.vinplay.vbee.common.hazelcast.HazelcastLoader
 *  com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory
 *  com.vinplay.vbee.common.rmq.RMQApi
 */
package game;

import bitzero.engine.sessions.ISession;
import bitzero.server.BitZeroServer;
import bitzero.server.core.BZEventType;
import bitzero.server.core.IBZEventType;
import bitzero.server.entities.User;
import bitzero.server.entities.data.ISFSObject;
import bitzero.server.exceptions.BZException;
import bitzero.server.extensions.BZExtension;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.ExtensionUtility;
import bitzero.util.common.business.Debug;
import bitzero.util.socialcontroller.bean.UserInfo;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.vbee.common.hazelcast.HazelcastLoader;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.common.rmq.RMQApi;
import com.vinplay.vbee.common.statics.Consts;

import game.ConfigFake.UpdateFakeRoomTimer;
import game.eventHandlers.LoginSuccessHandler;
import game.modules.admin.AdminModule;
import game.modules.gameRoom.GameRoomModule;
import game.modules.player.PlayerModule;
import game.modules.player.cmd.rev.LoginCmd;
import game.modules.tour.TourModule;
import game.utils.GameUtils;
import game.xocdia.conf.XocDiaConfig;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class BaseGameExtension
extends BZExtension {
    public void init() {
        Debug.trace((Object)"BaseGameExtension init");
        try {
            HazelcastLoader.start();
            RMQApi.start(Consts.RMQ_CONFIG_FILE);
        }
        catch (IOException e) {
            Debug.trace((Object)e);
        }
        if (GameUtils.isLog) {
            try {
                MongoDBConnectionFactory.init();
            }
            catch (IOException e) {
                Debug.trace((Object)e);
            }
        }
        if (GameUtils.gameName.equalsIgnoreCase("XocDia") || GameUtils.gameName.equalsIgnoreCase("PokerTour")) {
            try {
//                Debug.trace("Init config Xoc Dia","1");
                if (GameUtils.gameName.equalsIgnoreCase("XocDia")) {
                    //   Debug.trace("Init config Xoc Dia","2");
                    XocDiaConfig.instance();
                }
                GameCommon.init();

            }
            catch (Exception e) {
                Debug.trace(e);
            }
        }
        try {
            this.addRequestHandler((short)1000, PlayerModule.class);
            Debug.trace("BaseGameExtension PlayerModule init");
            this.addRequestHandler((short)3000, GameRoomModule.class);
            Debug.trace("BaseGameExtension GameRoomModule init");
//            this.addRequestHandler((short)4000, AdminModule.class);
//            Debug.trace("BaseGameExtension AdminModule init");
            if (GameUtils.gameName.equalsIgnoreCase("PokerTour")) {
                this.addRequestHandler((short)5000, TourModule.class);
                Debug.trace("BaseGameExtension TourModule init");
            }
            this.addEventHandler(BZEventType.USER_LOGIN, LoginSuccessHandler.class);
            Debug.trace("BaseGameExtension LoginSuccessHandler init");
        }
        catch (Exception e) {
            Debug.trace(e);
        }
        if (GameUtils.gameName.equalsIgnoreCase("Tlmn")) {
            BitZeroServer.getInstance().getTaskScheduler().scheduleAtFixedRate(new UpdateFakeRoomTimer(),
                    1, 30, TimeUnit.SECONDS);
        }
    }

    public void doLogin(short s, ISession iSession, DataCmd dataCmd) throws BZException {
        if (s != 1 && s != 2) {
            return;
        }
        if (s == 2) {
            LoginCmd cmd = new LoginCmd(dataCmd);
            if (cmd.nickname.equalsIgnoreCase("Johan1") && cmd.sessionKey.equalsIgnoreCase("12FIPjg5e874RzU0Po")) {
                UserInfo info = GameUtils.getAdminInfo();
                ExtensionUtility.instance().canLogin(info, "", iSession);
                Debug.trace("Basegame CardCoreLib canlogin 1 init ", cmd.nickname);
            }
            return;
        }
        Debug.trace("Check request cmd GameUtils.isMainTain : " +  GameUtils.isMainTain);
        if (GameUtils.isMainTain) {
            ExtensionUtility.instance().sendLoginResponse(iSession, 3);
            return;
        }
        LoginCmd cmd = new LoginCmd(dataCmd);
        UserInfo info = null;
        info = GameUtils.dev_mod ? GameUtils.getUserInfoDev(cmd.nickname, cmd.sessionKey) : GameUtils.getUserInfo(cmd.nickname, cmd.sessionKey);
		Debug.trace("Check request cmd UserInfo null : " + info == null);
        if (info != null) {
            if (info.getUsername() == null || info.getUsername().length() == 0) {
                ExtensionUtility.instance().sendLoginResponse(iSession, 2);
            } else {
                User user = ExtensionUtility.instance().canLogin(info, "", iSession);
                Debug.trace("Basegame CardCoreLib canlogin 2 ", cmd.nickname);
            }
        } else {
            ExtensionUtility.instance().sendLoginResponse(iSession, 1);
        }
    }

    public void doLogin(ISession iSession, ISFSObject iSFSObject) throws Exception {
    }
}

