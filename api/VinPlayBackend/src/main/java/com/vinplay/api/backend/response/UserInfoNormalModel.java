/*
 * Decompiled with CFR 0.144.
 */
package com.vinplay.api.backend.response;

public class UserInfoNormalModel {
    private String username;
    private String nickname;
    private String mobile;
    private long vinTotal;
    private boolean is_bot;
    private int dai_ly;

    public UserInfoNormalModel(String username, String nickname, String mobile, long vinTotal, boolean is_bot, int dai_ly) {
        this.username = username;
        this.nickname = nickname;
        this.mobile = mobile;
        this.vinTotal = vinTotal;
        this.is_bot = is_bot;
        this.dai_ly = dai_ly;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean getIs_bot() {
        return is_bot;
    }

    public void setIs_bot(boolean is_bot) {
        this.is_bot = is_bot;
    }

    public int getDai_ly() {
        return dai_ly;
    }

    public void setDai_ly(int dai_ly) {
        this.dai_ly = dai_ly;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public long getVinTotal() {
        return this.vinTotal;
    }

    public void setVinTotal(long vinTotal) {
        this.vinTotal = vinTotal;
    }
}

