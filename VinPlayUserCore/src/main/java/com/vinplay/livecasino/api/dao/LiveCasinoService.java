package com.vinplay.livecasino.api.dao;

import com.vinplay.livecasino.api.response.LiveCasinoUserResponse;

public interface LiveCasinoService {

    public boolean insertUserCasino(String user, String pass);

    public LiveCasinoUserResponse getUserCasino(String userName);
}
