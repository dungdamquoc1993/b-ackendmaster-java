package game.modules.BotJackPotTimer;

import bitzero.util.common.business.Debug;
import com.vinplay.vbee.common.enums.Games;
import game.GameConfig.GameConfig;
import game.modules.minigame.MiniPokerModule;
import game.modules.minigame.entities.BotMinigame;
import game.modules.minigame.room.MGRoomMiniPoker;
import game.utils.GameUtil;

import java.util.List;

public class BotJackPotMinipokerTimer implements Runnable {
    public boolean isInitBot = false;
    public List<String> bots;
    private long timeJackPot_100 = 0;
    private long timeJackPot_1000 = 0;
    private long timeJackPot_10000 = 0;

    public BotJackPotMinipokerTimer(long[] timeJackpot){
        this.timeJackPot_100 = timeJackpot[0];
        this.timeJackPot_1000 = timeJackpot[1];
        this.timeJackPot_10000 = timeJackpot[2];
    }

    @Override
    public void run() {
       try {
//           Debug.trace("BotJackPotMinipokerTimer");
           if (!this.isInitBot) {
               try{
                   this.bots = BotMinigame.getBotsJackPot(500, "vin");
               }catch (Exception e){
                   Debug.warn("error init bot");
               }
               this.isInitBot = true;

           }
           int moneyToJackPot_100 = GameConfig.getInstance().miniPokerBotConfig.randomJackPotPer5s(0);
           MGRoomMiniPoker room100 = (MGRoomMiniPoker) MiniPokerModule.rooms.get(Games.MINI_POKER.getName() + "_vin_100");
           room100.addMoneyToPot(moneyToJackPot_100);
           int moneyToJackPot_1000 = GameConfig.getInstance().miniPokerBotConfig.randomJackPotPer5s(1);
           MGRoomMiniPoker room1000 = (MGRoomMiniPoker) MiniPokerModule.rooms.get(Games.MINI_POKER.getName() + "_vin_1000");
           room1000.addMoneyToPot(moneyToJackPot_1000);
           int moneyToJackPot_10000 = GameConfig.getInstance().miniPokerBotConfig.randomJackPotPer5s(2);
           MGRoomMiniPoker room10000 = (MGRoomMiniPoker) MiniPokerModule.rooms.get(Games.MINI_POKER.getName() + "_vin_10000");
           room10000.addMoneyToPot(moneyToJackPot_10000);

           long currentTime = GameUtil.getTimeStampInSeconds();
           int time = 0;
           if(currentTime > timeJackPot_100){
               time = GameConfig.getInstance().miniPokerBotConfig.randomTimeBotEat(0);
               timeJackPot_100 = currentTime + time;
               room100.botEatJackpot(MiniPokerModule.keyBotJackpot + "_vin_100",timeJackPot_100,
                       this.bots.get(GameUtil.randomMax(this.bots.size())));
           }
           if(currentTime > timeJackPot_1000){
               time = GameConfig.getInstance().miniPokerBotConfig.randomTimeBotEat(1);
               timeJackPot_1000 = currentTime + time;
               room1000.botEatJackpot(MiniPokerModule.keyBotJackpot + "_vin_1000",timeJackPot_1000,
                       this.bots.get(GameUtil.randomMax(this.bots.size())));
           }
           if(currentTime > timeJackPot_10000){
               time = GameConfig.getInstance().miniPokerBotConfig.randomTimeBotEat(2);
               timeJackPot_10000 = currentTime + time;
               room10000.botEatJackpot(MiniPokerModule.keyBotJackpot + "_vin_10000",timeJackPot_10000,
                       this.bots.get(GameUtil.randomMax(this.bots.size())));
           }
          // Debug.trace("run finish");
       }catch (Exception e){
           Debug.trace(e);
       }
    }
}
