/*
 * Decompiled with CFR 0.144.
 */
package game.modules.slot.entities.slot.vqv;

import game.modules.slot.entities.slot.vqv.VQVAward;

public class AwardsOnLine {
    private VQVAward award;
    private long money;
    private String lineName;

    public AwardsOnLine(VQVAward award, long money, String lineName) {
        this.award = award;
        this.money = money;
        this.lineName = lineName;
    }

    public VQVAward getAward() {
        return this.award;
    }

    public void setAward(VQVAward award) {
        this.award = award;
    }

    public long getMoney() {
        return this.money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getLineName() {
        return this.lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getLineId() {
        return this.lineName.substring(4, this.lineName.length());
    }
}

