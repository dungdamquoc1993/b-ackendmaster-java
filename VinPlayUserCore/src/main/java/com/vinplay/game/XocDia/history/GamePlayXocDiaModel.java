package com.vinplay.game.XocDia.history;


import java.sql.Timestamp;

public class GamePlayXocDiaModel {
    private long gameplay_id;
    private String result;
    private long[] totalBet;
    private Timestamp created_at;

    public long getGameplay_id() {
        return gameplay_id;
    }

    public void setGameplay_id(long gameplay_id) {
        this.gameplay_id = gameplay_id;
    }

    public GamePlayXocDiaModel(long gamePlayId, long[] totalBet) {
        this.gameplay_id = gamePlayId;
        this.totalBet = totalBet;
        this.created_at = new Timestamp(System.currentTimeMillis());
    }

    public void setDiceResult(byte[] diceResult) {
        this.result = diceResult[0] + "";
        for (int i = 1; i < diceResult.length; i++) {
            this.result += "-" + diceResult[i];
        }
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

}
