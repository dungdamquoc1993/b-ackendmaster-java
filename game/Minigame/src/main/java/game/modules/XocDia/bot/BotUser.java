package game.modules.XocDia.bot;

import game.utils.GameUtil;

public class BotUser {
    public String display_name;
    public long id;
    public long money;
    public byte avatar;
    public byte level;
    public int numberPlayerCount = 0;
    public int currentPlayerCount = 0;

    public BotUser() {

    }

    public BotUser(int id, String display_name) {
        this.id = id;
        this.display_name = display_name;
        this.initData();
    }
    public void randomMoneyInGame(long currentMoneyBet){
        this.money = GameUtil.randomMax((int)currentMoneyBet/2) + currentMoneyBet;
    }
    public void initData(){
        this.avatar = (byte) GameUtil.randomMax(20);
        this.level = (byte) GameUtil.randomMax(5);
    }
}
