package game.modules.BotJackPotTimer;

import game.GameConfig.GameConfig;

public class ReloadMultiJackpotTimer implements Runnable {

    @Override
    public void run() {
        GameConfig.getInstance().slotMultiJackpotConfig.checkIsJackPotAllGame();
    }
}
