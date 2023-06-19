/*
 * Decompiled with CFR 0.144.
 */
package game.modules.slot.entities.slot.vqv;

import game.modules.slot.entities.slot.vqv.VQVItem;

public enum VQVAward {
    PENTA_JACKPOT("PENTA_JACKPOT", 0, (byte)1, VQVItem.JACKPOT, (byte)5, -1.0f),
    QUADRA_JACKPOT("QUADRA_JACKPOT", 1, (byte)1, VQVItem.JACKPOT, (byte)4, 30.0f),
    TRIPLE_JACKPOT("TRIPLE_JACKPOT", 2, (byte)1, VQVItem.JACKPOT, (byte)3, 5.0f),
    PENTA_BONUS("PENTA_BONUS", 3, (byte)1, VQVItem.BONUS, (byte)5, 8000.0f),
    QUADRA_BONUS("QUADRA_BONUS", 4, (byte)1, VQVItem.BONUS, (byte)4, -2.0f),
    TRIPLE_BONUS("TRIPLE_BONUS", 5, (byte)1, VQVItem.BONUS, (byte)3, -2.0f),
    PENTA_SCATTER("PENTA_SCATTER", 6, (byte)1, VQVItem.SCATTER, (byte)5, -3.0f),
    QUADRA_SCATTER("QUADRA_SCATTER", 7, (byte)1, VQVItem.SCATTER, (byte)4, -3.0f),
    TRIPLE_SCATTER("TRIPLE_SCATTER", 8, (byte)1, VQVItem.SCATTER, (byte)3, -3.0f),
    PENTA_FISH("PENTA_FISH", 9, (byte)1, VQVItem.FISH, (byte)5, 500.0f),
    QUADRA_FISH("QUADRA_FISH", 10, (byte)1, VQVItem.FISH, (byte)4, 20.0f),
    TRIPLE_FISH("TRIPLE_FISH", 11, (byte)1, VQVItem.FISH, (byte)3, 4.0f),
    PENTA_HAT("PENTA_HAT", 12, (byte)1, VQVItem.HAT, (byte)5, 200.0f),
    QUADRA_HAT("QUADRA_HAT", 13, (byte)1, VQVItem.HAT, (byte)4, 15.0f),
    TRIPLE_HAT("TRIPLE_HAT", 14, (byte)1, VQVItem.HAT, (byte)3, 3.0f),
    PENTA_SCARF("PENTA_SCARF", 15, (byte)1, VQVItem.SCARF, (byte)5, 75.0f),
    QUADRA_SCARF("QUADRA_SCARF", 16, (byte)1, VQVItem.SCARF, (byte)4, 10.0f),
    TRIPLE_SCARF("TRIPLE_SCARF", 17, (byte)1, VQVItem.SCARF, (byte)3, 2.0f),
    PENTA_MASK("PENTA_MASK", 18, (byte)1, VQVItem.MASK, (byte)5, 30.0f),
    QUADRA_MASK("QUADRA_MASK", 19, (byte)1, VQVItem.MASK, (byte)4, 6.0f);
    
    private byte id;
    private VQVItem item;
    private byte duplicate;
    private float ratio;

    private VQVAward(String s, int n2, byte id, VQVItem item, byte duplicate, float ratio) {
        this.id = id;
        this.item = item;
        this.duplicate = duplicate;
        this.ratio = ratio;
    }

    public void setId(byte id) {
        this.id = id;
    }

    public byte getId() {
        return this.id;
    }

    public void setItem(VQVItem item) {
        this.item = item;
    }

    public VQVItem getItem() {
        return this.item;
    }

    public void setDuplicate(byte duplicate) {
        this.duplicate = duplicate;
    }

    public byte getDuplicate() {
        return this.duplicate;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public float getRatio() {
        return this.ratio;
    }
}

