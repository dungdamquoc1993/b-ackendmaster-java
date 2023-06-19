package game.xocdia.server;

import bitzero.server.BitZeroServer;
import bitzero.util.common.business.CommonHandle;
import bitzero.util.common.business.Debug;
import com.vinplay.vbee.common.config.VBeePath;
import game.xocdia.server.init.InitTimer;

import java.util.concurrent.TimeUnit;

public class XocDiaMain {
    public static void main(String[] args) {

        // init base path
        VBeePath.initBasePath(XocDiaMain.class);

        boolean clusterMode = false;
        boolean useConsole = false;
        if (args.length > 0) {
            clusterMode = args[0].equalsIgnoreCase("cluster");
            useConsole = args.length > 1 && args[1].equalsIgnoreCase("console");
        }

        BitZeroServer bzServer = BitZeroServer.getInstance();
        bzServer.setClustered(clusterMode);
        if (useConsole) {
            bzServer.startDebugConsole();
        }

        bzServer.start();

        Debug.trace("start jetty");
        BitZeroServer.getInstance().getTaskScheduler().schedule(new InitTimer(), 5, TimeUnit.SECONDS);
        Debug.trace("start Xocdia server");
        
//        GameRoomSetting setting = new GameRoomSetting(1,100,4,1);
//        GameRoom gameRoom = new GameRoom(setting);
//        XocDiaGameServer tienlenGameServer = new XocDiaGameServer();
//        tienlenGameServer.init(gameRoom);
//        tienlenGameServer.init();
//        User user= new User("brightc", null);
//        DataCmd dataCmd = new DataCmd(null);
//        dataCmd.setId((short)0);
//        BetCmd cmd = new BetCmd(dataCmd);
//        byte potId = cmd.pot;
//        long money = cmd.money;
//        tienlenGameServer.bet(user, dataCmd);
    }
}
