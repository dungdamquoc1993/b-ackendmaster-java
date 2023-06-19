package com.vinplay.livecasino.api.core.obj;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LaunchGameResponse extends TCGBaseResponse{

    @JsonProperty("game_url")
    String game_url;

    public String getGame_url() {
        return game_url;
    }

    public void setGame_url(String game_url) {
        this.game_url = game_url;
    }
}
