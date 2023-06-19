package game.modules.description.XocDiaDescription;

import com.google.gson.Gson;
import game.modules.description.BauCuaDescription.BauCuaBetDescription;
import game.modules.description.BauCuaDescription.BauCuaTraCuocDescription;
import game.modules.description.BauCuaDescription.BauCuaWinDescription;

public class XocDiaDescriptionUtils {
    public static Gson gson = new Gson();

    public static String getXocDiaBetDescription(String gameID, long referenceId){
        return gson.toJson(new XocDiaBetDescription(gameID, referenceId));
    }


    public static String getXocDiaWinDescription(String gameID, long referenceId){
        return gson.toJson(new XocDiaWinDescription(gameID, referenceId));
    }
}
