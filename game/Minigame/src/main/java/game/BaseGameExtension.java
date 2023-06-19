package game;

import bitzero.engine.sessions.ISession;
import bitzero.server.BitZeroServer;
import bitzero.server.core.BZEventType;
import bitzero.server.entities.User;
import bitzero.server.entities.data.ISFSObject;
import bitzero.server.exceptions.BZException;
import bitzero.server.extensions.BZExtension;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.ExtensionUtility;
import bitzero.util.common.business.Debug;
import bitzero.util.socialcontroller.bean.UserInfo;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.dal.service.ServerInfoService;
import com.vinplay.dal.service.impl.ServerInfoServiceImpl;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.vbee.common.enums.Platform;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.hazelcast.HazelcastLoader;
import com.vinplay.vbee.common.models.cache.UserExtraInfoModel;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.common.pools.ConnectionPool;
import com.vinplay.vbee.common.rmq.RMQApi;
import com.vinplay.vbee.common.statics.Consts;
import com.vinplay.vbee.common.utils.DateTimeUtils;
import game.GameConfig.GameConfig;
import game.eventHandlers.LoginSuccessHandler;
import game.eventHandlers.LogoutSusscessHandler;
import game.eventHandlers.UserDisconnectHandler;
import game.init.InitTimer;
import game.modules.BotJackPotTimer.ReloadMultiJackpotTimer;
import game.modules.admin.AdminModule;
import game.modules.chat.ChatMD5Module;

import game.modules.chat.ChatModule;
import game.modules.gameRoom.GameRoomModule;
import game.modules.lobby.LobbyModule;
import game.modules.minigame.*;
import game.modules.minigame.entities.BotMinigame;
import game.modules.quest.DailyQuestModule;
import game.modules.player.PlayerModule;
import game.modules.player.cmd.rev.LoginCmd;
import game.utils.ConfigGame;
import game.utils.GameUtils;

import java.util.concurrent.TimeUnit;

public class BaseGameExtension
extends BZExtension {
    private int countReloadConfig = 0;
    private int countLogCCU = 0;
    private final Runnable gameLoopTask = new GameLoopTask();
    private ServerInfoService serverInfoSrv = new ServerInfoServiceImpl();
    private int lastCCU = 0;
    public static int ccuWeb = 0;
    public static int ccuAD = 0;
    public static int ccuIOS = 0;
    public static int ccuWP = 0;
    public static int ccuFB = 0;
    public static int ccuDT = 0;

    public void init() {
        try {
            Debug.trace(("INIT MINIGAME"));
            RMQApi.start(Consts.RMQ_CONFIG_FILE);
            HazelcastLoader.start();
            MongoDBConnectionFactory.init();
            ConnectionPool.start(Consts.DB_CONFIG_FILE);
            ConfigGame.reload();
            BotMinigame.loadData();
            GameCommon.init();
            GameConfig.getInstance().init();
            
            GameThirdPartyInit.init();
        }
        catch (Exception e) {
            Debug.trace("INIT MINIGAME ERROR " + e.getMessage());
        }
        this.addRequestHandler(MinigameServerHandleConfig.PlayerModule, PlayerModule.class);
        if (GameUtils.gameName.equalsIgnoreCase("Minigame")) {
            this.addRequestHandler(MinigameServerHandleConfig.TaiXiuModule, TaiXiuModule.class);
			this.addRequestHandler(MinigameServerHandleConfig.TaiXiuMD5Module, TaiXiuMD5Module.class);
            this.addRequestHandler(MinigameServerHandleConfig.ChatMD5Module, ChatMD5Module.class);
            this.addRequestHandler(MinigameServerHandleConfig.MiniPokerModule, MiniPokerModule.class);
            this.addRequestHandler(MinigameServerHandleConfig.BauCuaModule, BauCuaModule.class);
            this.addRequestHandler(MinigameServerHandleConfig.CaoThapModule, CaoThapModule.class);
            this.addRequestHandler(MinigameServerHandleConfig.CandyModule, CandyModule.class);
            this.addRequestHandler(MinigameServerHandleConfig.GalaxyModule, GalaxyModule.class);
            this.addRequestHandler(MinigameServerHandleConfig.ChatModule, ChatModule.class);
            //this.addRequestHandler(MinigameServerHandleConfig.AdminModule, AdminModule.class);
            this.addRequestHandler(MinigameServerHandleConfig.LobbyModule, LobbyModule.class);
            this.addRequestHandler(MinigameServerHandleConfig.MissionModule, DailyQuestModule.class);
            //this.addRequestHandler((short)8000, Slot3x3ExtendModule.class);
        } else {
            this.addRequestHandler(MinigameServerHandleConfig.GameRoomModule, GameRoomModule.class);
        }
        this.addEventHandler(BZEventType.USER_LOGIN, LoginSuccessHandler.class);
        this.addEventHandler(BZEventType.USER_LOGOUT, LogoutSusscessHandler.class);
        this.addEventHandler(BZEventType.USER_DISCONNECT, UserDisconnectHandler.class);
        BitZeroServer.getInstance().getTaskScheduler().scheduleAtFixedRate(this.gameLoopTask, 10, 1, TimeUnit.SECONDS);
        BitZeroServer.getInstance().getTaskScheduler().scheduleAtFixedRate(new ReloadMultiJackpotTimer(),
                10, 300, TimeUnit.SECONDS);
//        BitZeroServer.getInstance().getTaskScheduler().scheduleAtFixedRate(new CheckCCUTimer(),
//                10, 300, TimeUnit.SECONDS);
        this.bz.getTaskScheduler().schedule(new InitTimer(), 5, TimeUnit.SECONDS);
    }

    public void doLogin(short s, ISession iSession, DataCmd dataCmd) throws BZException {
        User user;
        if (s != 1) {
            return;
        }
        LoginCmd cmd = new LoginCmd(dataCmd);
        UserInfo info = GameUtils.getUserInfo(cmd.nickname, cmd.sessionKey);
        if (info != null && (user = ExtensionUtility.instance().canLogin(info, "", iSession)) != null) {
            user.setProperty("dai_ly", info.getStatus());
            this.saveCCUPlatform(user);
        }
        Debug.trace("Basegame Minigame canlogin init  ", cmd.nickname);
        Debug.trace("Check CCU  ", BitZeroServer.getInstance().getUserManager().getUserCount());
    }

    public void doLogin(ISession iSession, ISFSObject iSFSObject) throws Exception {
    }

    public void saveCCUPlatform(User user) {
        HazelcastInstance instance = HazelcastClientFactory.getInstance();
        IMap userExtraModel = instance.getMap("cache_user_extra_info");
        if (userExtraModel.containsKey(user.getName())) {
            UserExtraInfoModel model = (UserExtraInfoModel)userExtraModel.get(user.getName());
            if (model != null && model.getPlatfrom() != null) {
                user.setProperty("pf", model.getPlatfrom());
                Platform platform = Platform.find(model.getPlatfrom());
                switch (platform) {
                    case WEB: {
                        ++ccuWeb;
                        break;
                    }
                    case ANDROID: {
                        ++ccuAD;
                        break;
                    }
                    case IOS: {
                        ++ccuIOS;
                        break;
                    }
                    case WINPHONE: {
                        ++ccuWP;
                        break;
                    }
                    case FACEBOOK_APP: {
                        ++ccuFB;
                        break;
                    }
                    case DESKTOP: {
                        ++ccuDT;
                    }
				default:
					break;
                }
            }
        } else {
            Debug.trace(("Cannot find user's extra info " + user.getName()));
        }
    }

    private void gameLoop() {
        ++this.countReloadConfig;
        if (this.countReloadConfig == 300) {
            Debug.trace("reload config");
            ConfigGame.reload();
            this.countReloadConfig = 0;
        }
        ++this.countLogCCU;
        if (this.countLogCCU == ConfigGame.getIntValue("update_log_ccu")) {
            int ccu = ExtensionUtility.globalUserManager.getUserCount();//  .getUserCountByName();
            long ccuGiam = this.lastCCU - ccu;
            if (ccuGiam >= (long)ConfigGame.getIntValue("min_so_ccu_giam", 50)) {
                GameUtils.sendAlertAndCall("CCU giam " + ccuGiam + " trong " + ConfigGame.getIntValue("update_log_ccu") + " (s), time= " + DateTimeUtils.getCurrentTime());
            }
            this.lastCCU = ccu;
            this.countLogCCU = 0;
            int ccuOT = ccu - (ccuWeb + ccuAD + ccuIOS + ccuWP + ccuFB + ccuDT);
            this.serverInfoSrv.logCCU(ccu, ccuWeb, ccuAD, ccuIOS, ccuWP, ccuFB, ccuDT, ccuOT);
        }
    }

    private final class GameLoopTask
    implements Runnable {
        private GameLoopTask() {
        }

        @Override
        public void run() {
            BaseGameExtension.this.gameLoop();
        }
    }

}

