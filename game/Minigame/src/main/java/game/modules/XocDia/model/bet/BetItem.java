package game.modules.XocDia.model.bet;

import java.sql.Timestamp;

public class BetItem {
    public long uId = 0;
    public long money = 0;
    public Timestamp time;
    public String userName;
    public boolean isBot;

    public BetItem(long uId, long money, String userName, boolean isBot) {
        this.uId = uId;
        this.money = money;
        this.time = new Timestamp(System.currentTimeMillis());
        this.isBot = isBot;
        this.userName = userName;
    }
}
