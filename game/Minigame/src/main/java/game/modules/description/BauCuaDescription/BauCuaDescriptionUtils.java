package game.modules.description.BauCuaDescription;

import com.google.gson.Gson;

public class BauCuaDescriptionUtils {
    public static Gson gson = new Gson();

    public static String getBauCuaBetDescription(String gameID, long referenceId){
        return gson.toJson(new BauCuaBetDescription(gameID, referenceId));
    }

    public static String getBauCuaTraCuocDescription(String gameID, long referenceId){
        return gson.toJson(new BauCuaTraCuocDescription(gameID, referenceId));
    }

    public static String getBauCuaWinDescription(String gameID, long referenceId){
        return gson.toJson(new BauCuaWinDescription(gameID, referenceId));
    }
}
