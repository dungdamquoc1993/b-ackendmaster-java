package game.modules.slot;

import bitzero.server.BitZeroServer;
import bitzero.server.entities.User;
import bitzero.server.extensions.BaseClientRequestHandler;
import bitzero.server.extensions.data.BaseMsg;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.common.business.Debug;
import com.vinplay.dal.service.impl.CacheServiceImpl;
import com.vinplay.usercore.utils.CacheConfigName;
import com.vinplay.vbee.common.enums.Games;

import game.modules.slot.cmd.send.hall.ListAutoPlayInfoMsg;
import game.modules.slot.cmd.send.hall.UpdateJackpotsMsg;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.json.simple.JSONObject;

public class HallSlotModule
extends BaseClientRequestHandler {
    private Set<User> usersSub = new HashSet<User>();
    private Runnable updateJackpotsTask = new UpdateJackpotsTask();

    public HallSlotModule() {
        BitZeroServer.getInstance().getTaskScheduler().scheduleAtFixedRate(this.updateJackpotsTask, 10, 4, TimeUnit.SECONDS);
    }

    public void handleClientRequest(User user, DataCmd dataCmd) {
        switch (dataCmd.getId()) {
            case 10001: {
                this.subScribe(user, dataCmd);
                break;
            }
            case 10002: {
                this.unSubscribe(user, dataCmd);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void subScribe(User user, DataCmd dataCmd) {
        boolean auto;
        Set<User> set = this.usersSub;
        synchronized (set) {
            this.usersSub.add(user);
        }
        UpdateJackpotsMsg msg = new UpdateJackpotsMsg();
        msg.json = this.buildJsonJackpots();
        this.send(msg, user);
        ListAutoPlayInfoMsg listAutoMsg = new ListAutoPlayInfoMsg();
        if (user.getProperty(("auto_" + Games.KHO_BAU.getName())) != null) {
            listAutoMsg.autoKhoBau = auto = ((Boolean)user.getProperty(("auto_" + Games.KHO_BAU.getName()))).booleanValue();
        }
        if (user.getProperty(("auto_" + Games.NU_DIEP_VIEN.getName())) != null) {
            listAutoMsg.autoNDV = auto = ((Boolean)user.getProperty(("auto_" + Games.NU_DIEP_VIEN.getName()))).booleanValue();
        }
        if (user.getProperty(("auto_" + Games.AVENGERS.getName())) != null) {
            listAutoMsg.autoAvenger = auto = ((Boolean)user.getProperty(("auto_" + Games.AVENGERS.getName()))).booleanValue();
        }
        if (user.getProperty(("auto_" + Games.VUONG_QUOC_VIN.getName())) != null) {
            listAutoMsg.autoVQV = auto = ((Boolean)user.getProperty(("auto_" + Games.VUONG_QUOC_VIN.getName()))).booleanValue();
        }
        this.send((BaseMsg)listAutoMsg, user);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void unSubscribe(User user, DataCmd dataCmd) {
        Set<User> set = this.usersSub;
        synchronized (set) {
            this.usersSub.remove(user);
        }
    }

    private String buildJsonJackpots() {
        JSONObject json = new JSONObject();
        JSONObject jsonAudition = this.buildGameSlotInfo(Games.AUDITION.getName());
        json.put("audition", jsonAudition);

        JSONObject jsonMaybach = this.buildGameSlotInfo(Games.MAYBACH.getName());
        json.put("maybach", jsonMaybach);

        JSONObject jsonTamhung = this.buildGameSlotInfo(Games.TAMHUNG.getName());
        json.put("tamhung", jsonTamhung);

        JSONObject jsonRangeRover = this.buildGameSlotInfo(Games.RANGE_ROVER.getName());
        json.put("rangeRover", jsonRangeRover);

        JSONObject jsonBenley = this.buildGameSlotInfo(Games.BENLEY.getName());
        json.put("benley", jsonBenley);

        JSONObject jsonRollRoye = this.buildGameSlotInfo(Games.ROLL_ROYE.getName());
        json.put("rollRoye", jsonRollRoye);

        JSONObject jsonSpartan = this.buildGameSlotInfo(Games.SPARTAN.getName());
        json.put("spartan", jsonSpartan);

        JSONObject jsonChiemtinh = this.buildGameSlotInfo(Games.CHIEM_TINH.getName());
        json.put("chiemtinh", jsonChiemtinh);

        JSONObject jsonBikini = this.buildGameSlotInfo(Games.BIKINI.getName());
        json.put("bikini", jsonBikini);
        
		// minigame
        JSONObject jsonPokemon = this.buildGameSlotInfo(Games.CANDY.getName());
        json.put("pokemon", jsonPokemon);
        
        JSONObject jsonGalaxy = this.buildGameSlotInfo(Games.GALAXY.getName());
        json.put("galaxy", jsonGalaxy);
        
        JSONObject jsonMiniPoker = this.buildGameSlotInfo(Games.MINI_POKER.getName());
        json.put("minipoker", jsonMiniPoker);
        
        JSONObject jsonCaoThap = this.buildGameSlotInfo("cao_thap");
        json.put("caothap", jsonCaoThap);

        JSONObject jsonTx = this.buildGameSlotInfo(Games.TAI_XIU.name());
        json.put(Games.TAI_XIU.name(), jsonTx);
        
        return json.toJSONString();
    }

    private JSONObject buildGameSlotInfo(String gameName) {
        JSONObject jsonGame = new JSONObject();
        try {
        	if(gameName.equals(Games.TAI_XIU.name())) {
				JSONObject roomTai = this.buildRoomMiniInfo(gameName, 1);
				JSONObject roomXiu = this.buildRoomMiniInfo(gameName, 0);
				jsonGame.put("1", roomTai);
				jsonGame.put("0", roomXiu);
        	}else if ("cao_thap".equals(gameName)) {
        		JSONObject room101 = this.buildRoomSlotInfo(gameName, 1000);
                jsonGame.put("1000", room101);
                JSONObject room102 = this.buildRoomSlotInfo(gameName, 10000);
                jsonGame.put("10000", room102);
                JSONObject room103 = this.buildRoomSlotInfo(gameName, 50000);
                jsonGame.put("50000", room103);
                JSONObject room104 = this.buildRoomSlotInfo(gameName, 100000);
                jsonGame.put("100000", room104);
                JSONObject room105 = this.buildRoomSlotInfo(gameName, 500000);
                jsonGame.put("500000", room105);
			}else {
				JSONObject room100 = this.buildRoomSlotInfo(gameName, 100);
                jsonGame.put("100", room100);
                JSONObject room101 = this.buildRoomSlotInfo(gameName, 1000);
                jsonGame.put("1000", room101);
                JSONObject room102 = this.buildRoomSlotInfo(gameName, 10000);
                jsonGame.put("10000", room102);
			}
        	
        }
        catch (Exception e) {
            Debug.trace(("Hall Slot get jackpots " + gameName + " error: " + e.getMessage()));
        }
        return jsonGame;
    }

    private JSONObject buildRoomSlotInfo(String gameName, int room) {
        CacheServiceImpl cacheService = new CacheServiceImpl();
        JSONObject jsonValue = new JSONObject();
        try {
            int pot = cacheService.getValueInt(String.valueOf(gameName) + "_vin_" + room);
            jsonValue.put("p", pot);
            int x2 = cacheService.getValueInt(String.valueOf(gameName) + "_vin_" + room + "_x2");
            jsonValue.put("x2", x2);
        }
        catch (Exception e) {
            Debug.trace(("Hall Slot get jackpots " + gameName + " - " + room + " error: " + e.getMessage()));
        }
        return jsonValue;
    }
    private JSONObject buildRoomMiniInfo(String gameName ,int type) {
    	//type =1 tai , type=0 xiu
        CacheServiceImpl cacheService = new CacheServiceImpl();
        JSONObject jsonValue = new JSONObject();
        try {
        	if(type==1) {
        		long potT = cacheService.getValueJP(CacheConfigName.TAIXIU_FUND_JP_FAKETAI);
                jsonValue.put("pt", potT);
        	}else {
        		long potX = cacheService.getValueJP(CacheConfigName.TAIXIU_FUND_JP_FAKEXIU);
                jsonValue.put("px", potX);
			}
        }
        catch (Exception e) {
        }
        return jsonValue;
    }
    static void access$2(HallSlotModule hallSlotModule, BaseMsg baseMsg, User user) {
        hallSlotModule.send(baseMsg, user);
    }

	private class UpdateJackpotsTask implements Runnable {
        private UpdateJackpotsTask() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            try {
                String result = HallSlotModule.this.buildJsonJackpots();
                UpdateJackpotsMsg msg = new UpdateJackpotsMsg();
                msg.json = result;
                Set set = HallSlotModule.this.usersSub;
                synchronized (set) {
                    for (User user : HallSlotModule.this.usersSub) {
                        if (user == null) continue;
                        HallSlotModule.access$2(HallSlotModule.this, msg, user);
                    }
                }
            }
            catch (Exception e) {
                Debug.trace(("Update slot exception: " + e.getMessage()));
            }
        }
    }

}

