package game.modules.description.TaiXiuDescription;

import com.google.gson.Gson;

public class TaiXiuDescriptionUtils {
    public static Gson gson = new Gson();

    public static String getTaiXiuBetDescription(String gameID, long referenceId, String time, short betSide){
        return gson.toJson(new TaiXiuBetDescription(gameID, referenceId, time, betSide));
    }

    public static String getTaiXiuTraCuocDescription(String gameID, long referenceId){
        return gson.toJson(new TaiXiuTraCuocDescription(gameID, referenceId));
    }

    public static String getTaiXiuRefundDescription(String gameID, long referenceId){
        return gson.toJson(new TaiXiuRefundDescription(gameID, referenceId));
    }

    public static String getTaiXiuWinDescription(String gameID, long referenceId,byte action){
        return gson.toJson(new TaiXiuWinDescription(gameID, referenceId ,action));
    }
}
