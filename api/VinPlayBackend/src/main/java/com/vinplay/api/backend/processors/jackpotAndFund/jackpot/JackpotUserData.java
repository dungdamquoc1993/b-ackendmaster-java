package com.vinplay.api.backend.processors.jackpotAndFund.jackpot;

public class JackpotUserData {

    public String nickName;
    public int gameID;
    public int betValue;

    public JackpotUserData(String nickName, int gameID, int betValue) {
        this.nickName = nickName;
        this.gameID =gameID;
        this.betValue = betValue;
    }
}
