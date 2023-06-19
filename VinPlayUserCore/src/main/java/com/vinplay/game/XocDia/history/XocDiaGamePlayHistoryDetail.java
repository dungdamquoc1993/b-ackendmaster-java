package com.vinplay.game.XocDia.history;



import java.sql.Timestamp;

public class XocDiaGamePlayHistoryDetail {
    private long user_id;
    private long gameplay_id;
    private String username;
    private byte door;
    private long bet;
    private long pay;
    private Timestamp created_at;

    public long getGameplay_id() {
        return gameplay_id;
    }

    public void setGameplay_id(long gameplay_id) {
        this.gameplay_id = gameplay_id;
    }

    public XocDiaGamePlayHistoryDetail(long user_id, long gameplay_id, String username, byte door, long bet, Timestamp time) {
        this.user_id = user_id;
        this.gameplay_id = gameplay_id;
        this.username = username;
        this.door = door;
        this.bet = bet;
        this.created_at = time;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getBet() {
        return bet;
    }

    public void setBet(long bet) {
        this.bet = bet;
    }

    public void setPay(long pay) {
        this.pay = pay;
    }

    public byte getDoor() {
        return door;
    }

    public long getPay() {
        return pay;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

}
