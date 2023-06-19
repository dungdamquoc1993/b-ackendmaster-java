package game.modules.description.XocDiaDescription;

import game.modules.description.BauCuaDescription.BauCuaBaseDescription;

public class XocDiaWinDescription extends XocDiaBaseDescription {

    public XocDiaWinDescription(String gameID, long referenceId){
        super(gameID,referenceId, (byte)2);
    }
}
