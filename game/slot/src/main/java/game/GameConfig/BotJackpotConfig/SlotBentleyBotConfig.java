package game.GameConfig.BotJackpotConfig;

import game.modules.GameUtil;

public class SlotBentleyBotConfig {
    public int[][] jackPotPer5s = {{500, 1000}, {1500, 3000}, {4000, 8500}};
    public int[][] timeBotEat = {{3000,4200},{7200,14400},{36000,48000}};

    public int randomJackPotPer5s(int betLevel){
        return GameUtil.randomBetween(this.jackPotPer5s[betLevel][0],this.jackPotPer5s[betLevel][1]);
    }

    public int randomTimeBotEat(int betLevel){
        return GameUtil.randomBetween(this.timeBotEat[betLevel][0],this.timeBotEat[betLevel][1]);
    }
}
