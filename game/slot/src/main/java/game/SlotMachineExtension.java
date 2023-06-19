/*
 * Decompiled with CFR 0.144.
 *
 * Could not load the following classes:
 *  bitzero.engine.sessions.ISession
 *  bitzero.server.BitZeroServer
 *  bitzero.server.core.BZEventType
 *  bitzero.server.core.IBZEventType
 *  bitzero.server.entities.User
 *  bitzero.server.entities.data.ISFSObject
 *  bitzero.server.exceptions.BZException
 *  bitzero.server.extensions.BZExtension
 *  bitzero.server.extensions.data.DataCmd
 *  bitzero.server.util.TaskScheduler
 *  bitzero.util.ExtensionUtility
 *  bitzero.util.common.business.Debug
 *  bitzero.util.socialcontroller.bean.UserInfo
 *  com.vinplay.usercore.utils.GameCommon
 *  com.vinplay.vbee.common.hazelcast.HazelcastLoader
 *  com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory
 *  com.vinplay.vbee.common.pools.ConnectionPool
 *  com.vinplay.vbee.common.rmq.RMQApi
 */
package game;

import bitzero.engine.sessions.ISession;
import bitzero.server.BitZeroServer;
import bitzero.server.core.BZEventType;
import bitzero.server.entities.data.ISFSObject;
import bitzero.server.exceptions.BZException;
import bitzero.server.extensions.BZExtension;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.ExtensionUtility;
import bitzero.util.common.business.Debug;
import bitzero.util.socialcontroller.bean.UserInfo;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.vbee.common.hazelcast.HazelcastLoader;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.common.pools.ConnectionPool;
import com.vinplay.vbee.common.rmq.RMQApi;
import com.vinplay.vbee.common.statics.Consts;

import game.BotJackpotTimer.ReloadMultiJackpotTimer;
import game.GameConfig.GameConfig;
import game.Jetty.JettyUtils;
import game.eventHandlers.LoginSuccessHandler;
import game.init.InitTimer;
import game.modules.slot.*;
import game.modules.slot.cmd.rev.LoginCmd;
import game.modules.slot.entities.BotMinigame;
import game.util.ConfigGame;
import game.util.GameUtils;

import java.util.concurrent.TimeUnit;

public class SlotMachineExtension
extends BZExtension {
    private int countReloadConfig = 0;
    private final Runnable gameLoopTask = new GameLoopTask();

    public void init() {
		try {
			RMQApi.start(Consts.RMQ_CONFIG_FILE);
			HazelcastLoader.start();
			MongoDBConnectionFactory.init();
			ConnectionPool.start(Consts.DB_CONFIG_FILE);
			ConfigGame.reload();
			BotMinigame.loadData();
			GameCommon.init();
			GameConfig.getInstance().init();
			GameThirdPartyInit.init();
		} catch (Exception e) {
			Debug.trace("INIT MINIGAME ERROR " + e.getMessage());
		}
        this.addRequestHandler(SlotMachineServerHandlerConfig.HallSlotModule, HallSlotModule.class);

        // 11 icon wild
        this.addRequestHandler(SlotMachineServerHandlerConfig.Slot11IconWild_0, BenleyModule.class);
        this.addRequestHandler(SlotMachineServerHandlerConfig.Slot11IconWild_1, SpartanModule.class);
        this.addRequestHandler(SlotMachineServerHandlerConfig.Slot11IconWild_2, ChiemTinhModule.class);
        this.addRequestHandler(SlotMachineServerHandlerConfig.Slot11IconWild_3, BikiniModule.class);

        // 7icon wild
        this.addRequestHandler(SlotMachineServerHandlerConfig.Slot7IconWildModule_0, RangeRoverModule.class);
        this.addRequestHandler(SlotMachineServerHandlerConfig.Slot7IconWildModule_1, AuditionModule.class); // audition

        //9 Icon
        this.addRequestHandler(SlotMachineServerHandlerConfig.Slot9IconModule_0, MayBachModule.class); // avenger

        // 7icon thuong
        this.addRequestHandler(SlotMachineServerHandlerConfig.Slot7IconModule_0, RollRoyModule.class); // gai nhay
        this.addRequestHandler(SlotMachineServerHandlerConfig.Slot7IconModule_1, TamHungModule.class); // tam hung


        this.addEventHandler(BZEventType.USER_LOGIN, LoginSuccessHandler.class);
        this.addEventHandler(BZEventType.USER_DISCONNECT, LoginSuccessHandler.class);
        BitZeroServer.getInstance().getTaskScheduler().scheduleAtFixedRate(this.gameLoopTask, 10, 1, TimeUnit.SECONDS);

        BitZeroServer.getInstance().getTaskScheduler().scheduleAtFixedRate(new ReloadMultiJackpotTimer(),
                10, 300, TimeUnit.SECONDS);

        this.bz.getTaskScheduler().schedule(new InitTimer(), 5, TimeUnit.SECONDS);
    }

    public void doLogin(short s, ISession iSession, DataCmd dataCmd) throws BZException {
        if (s != 1) {
            return;
        }
        LoginCmd cmd = new LoginCmd(dataCmd);
        UserInfo info = GameUtils.getUserInfo(cmd.nickname, cmd.sessionKey);
        if (info != null) {
            ExtensionUtility.instance().canLogin(info, "", iSession);
            Debug.trace("Slot canlogin 1 init");
        }
    }

    public void doLogin(ISession iSession, ISFSObject iSFSObject) throws Exception {
    }

    private void gameLoop() {
        ++this.countReloadConfig;
        if (this.countReloadConfig == 300) {
            Debug.trace((Object)"reload config");
            ConfigGame.reload();
            this.countReloadConfig = 0;
        }
    }

    private final class GameLoopTask
    implements Runnable {
        private GameLoopTask() {
        }

        @Override
        public void run() {
            SlotMachineExtension.this.gameLoop();
        }
    }

}

