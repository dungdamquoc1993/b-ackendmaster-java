package game.GameConfig.ConfigGame.BotJackpotConfig;

import game.utils.GameUtil;

public class Slot3x3BotConfig {
    //    public int[][] jackPotPer5s = {{600, 900}, {2000, 2500}, {4500, 8000}};
//    public int[][] timeBotEat = {{3000, 4200}, {7200, 14400}, {36000, 48000}};

    public int[][] jackPotPer5s;
    public int[][] timeBotEat;

    public int randomJackPotPer5s(int betLevel) {
        return GameUtil.randomBetween(this.jackPotPer5s[betLevel][0], this.jackPotPer5s[betLevel][1]);
    }

    public int randomTimeBotEat(int betLevel) {
        return GameUtil.randomBetween(this.timeBotEat[betLevel][0], this.timeBotEat[betLevel][1]);
    }
}
