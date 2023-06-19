package game.tienlen.server.GameConfig.Config;

import game.tienlen.server.GameUtil;

public class TLMNBotConfig {
    public int[] timeAttack;
    public int[] timeJoinRoom;
    public boolean enableBot;

    public int randomTimeAttack(){
        return GameUtil.randomBetween(this.timeAttack[0],this.timeAttack[1]);
    }
}
