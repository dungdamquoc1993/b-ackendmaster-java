package game.GameConfig.RollLoseConfig;

import game.GameConfig.GameConfig;
import game.modules.GameUtil;

public class Slot11IconWildLienTucRollLoseConfig {
    public byte[][] rollLose0;

    public byte[] getTableRollLose(){
        return this.rollLose0[GameUtil.randomMax(this.rollLose0.length)];
    }
}
