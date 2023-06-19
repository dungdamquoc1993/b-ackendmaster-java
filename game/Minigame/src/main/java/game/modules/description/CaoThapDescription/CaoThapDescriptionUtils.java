package game.modules.description.CaoThapDescription;

import com.google.gson.Gson;
import game.modules.description.BauCuaDescription.BauCuaBetDescription;
import game.modules.description.BauCuaDescription.BauCuaTraCuocDescription;
import game.modules.description.BauCuaDescription.BauCuaWinDescription;

public class CaoThapDescriptionUtils {
    public static Gson gson = new Gson();

    public static String getCaoThapBetDesciption(String gameID, long referenceId){
        return gson.toJson(new CaoThapBetDescription(gameID, referenceId));
    }

    public static String getCaoThapJackPotDesciption(String gameID, long referenceId, short step){
        return gson.toJson(new CaoThapJackpotDescription(gameID, referenceId, step));
    }

    public static String getCaoThapWinDesciption(String gameID, long referenceId, short step){
        return gson.toJson(new CaoThapWinDescription(gameID, referenceId, step));
    }
}
