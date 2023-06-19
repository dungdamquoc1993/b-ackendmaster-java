/*
 * Decompiled with CFR 0.144.
 */
package game.modules.slot.entities.slot.khobau;

public enum KhoBauItem {
    NONE("NONE", 0, "none", (byte)-1),
    POUCH("POUCH", 1, "pouch", (byte)0),
    BAG("BAG", 2, "bag", (byte)1),
    BOOK("BOOK", 3, "book", (byte)2),
    BULLSEYE("BULLSEYE", 4, "bullseye", (byte)3),
    MAP("MAP", 5, "map", (byte)4),
    BOTTLE("BOTTLE", 6, "bottle", (byte)5),
    ANVIL("ANVIL", 7, "anvil", (byte)6);
    
    private String name;
    private byte id;

    private KhoBauItem(String s, int n2, String name, byte id) {
        this.name = name;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setId(byte id) {
        this.id = id;
    }

    public byte getId() {
        return this.id;
    }

    public static KhoBauItem findItem(byte id) {
        for (KhoBauItem entry : KhoBauItem.values()) {
            if (entry.getId() != id) continue;
            return entry;
        }
        return null;
    }
}

