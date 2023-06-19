package game.tienlen.server;

import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.vbee.common.config.VBeePath;

import bitzero.server.BitZeroServer;
import bitzero.util.common.business.CommonHandle;
import game.tienlen.server.GameConfig.GameConfig;
import game.tienlen.server.init.InitTimer;

import java.util.concurrent.TimeUnit;

public class TlmnMain {
    public static void main(String[] args) {
      //  byte[][] test = DealCard.dealCardBotAutoWin(1);
        // init base path
        VBeePath.initBasePath(TlmnMain.class);

        boolean clusterMode = false;
        boolean useConsole = false;
        if (args.length > 0) {
            clusterMode = args[0].equalsIgnoreCase("cluster");
            useConsole = args.length > 1 && args[1].equalsIgnoreCase("console");
        }

        BitZeroServer bzServer = BitZeroServer.getInstance();
        bzServer.setClustered(clusterMode);

//        useConsole = true;
        if (useConsole) {
            bzServer.startDebugConsole();
        }

        bzServer.start();
        GameConfig.getInstance().init();
        BotFundController.getInstance();

        CommonHandle.writeInfoLog("start jetty");
        try {
			GameThirdPartyInit.init();
		} catch (Exception e) {
			// TODO: handle exception
		}
        BitZeroServer.getInstance().getTaskScheduler().schedule(new InitTimer(), 5, TimeUnit.SECONDS);
        CommonHandle.writeInfoLog("start TLMN");
    }
}
