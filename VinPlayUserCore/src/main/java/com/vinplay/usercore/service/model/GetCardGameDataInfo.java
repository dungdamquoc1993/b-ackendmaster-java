package com.vinplay.usercore.service.model;

import com.google.gson.Gson;

public class GetCardGameDataInfo {
    private static Gson gson = new Gson();

    public static String getCardGameDataInfo(String gameID, String roomID, String matchID) {
        return gson.toJson(new CardGameServiceData(gameID, roomID, matchID));
    }
}
