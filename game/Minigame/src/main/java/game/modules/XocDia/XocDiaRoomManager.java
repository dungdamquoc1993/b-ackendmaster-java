package game.modules.XocDia;

import bitzero.server.BitZeroServer;
import bitzero.server.api.CreateRoomSettings;
import bitzero.server.entities.Room;
import bitzero.server.entities.Zone;
import bitzero.server.exceptions.BZCreateRoomException;
import bitzero.util.common.business.Debug;
import game.utils.GameUtil;

public class XocDiaRoomManager {
    public static final int ZONE = 0;
    public static final String SEPARATOR = "_";
    public static final String XOC_DIA_NAME = "_";

    public static Room createRoomXocDia() {
        Zone zone = BitZeroServer.getInstance().getZoneManager().getZoneById(ZONE);
        CreateRoomSettings roomSettings = new CreateRoomSettings();
        String name = new StringBuilder().append(ZONE).append(SEPARATOR).
                append(SEPARATOR).append(GameUtil.getTimeStampInSeconds()).toString();
        roomSettings.setName(name);
        roomSettings.setMaxUsers(100000);
        roomSettings.setGroupId(XOC_DIA_NAME + "");
        Room room;
        try {
            room = zone.createRoom(roomSettings);
        } catch (BZCreateRoomException e) {
            Debug.trace(e);
            return null;
        }

        room.setDynamic(false);
        Debug.trace("createRoom");

        return room;
    }

    public static synchronized Room getRoomToJoin() {
        Zone zone = BitZeroServer.getInstance().getZoneManager().getZoneById(ZONE);
        Room room = (Room) zone.getRoomListFromGroup(XOC_DIA_NAME + "").get(0);
        return room;
    }
}
