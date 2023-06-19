package game.ConfigFake;

import game.utils.GameUtils;

import java.util.Random;

public class ConfigFakeTLMN {
    public static final int NUMBER_ROOM_TLMN = 4;
    public static final int NUMBER_ROOM_TLMN_SOLO = 2;

    public static int[][] tlmnRoomFakeConfig = {
            {25, 30},
            {20, 25},
            {10, 15},
            {5, 7},
            {1, 2},
    };

    public static int[][] tlmnSLRoomFakeConfig = {
            {30, 40},
            {20, 30},
            {10, 20},
            {10, 15},
            {2, 4},
    };

    private static ConfigFakeTLMN _instance = null;
    private static final Object lock = new Object();

    private ConfigFakeTLMN() {
        this.currentRoomTLMN = new int[tlmnRoomFakeConfig.length];
        for (int i = 0; i < this.currentRoomTLMN.length; i++) {
            this.currentRoomTLMN[i] = randomBetween(tlmnRoomFakeConfig[i][0], tlmnRoomFakeConfig[i][1]);
        }

        this.currentRoomTLMN_SL = new int[tlmnSLRoomFakeConfig.length];
        for (int i = 0; i < this.currentRoomTLMN_SL.length; i++) {
            this.currentRoomTLMN_SL[i] = randomBetween(tlmnSLRoomFakeConfig[i][0], tlmnSLRoomFakeConfig[i][1]);
        }
    }


    public static ConfigFakeTLMN getInstance() {
        if (_instance == null) {
            synchronized (lock) {
                if (_instance == null) {
                    _instance = new ConfigFakeTLMN();
                }
            }
        }
        return _instance;
    }

    public int[] currentRoomTLMN = null;
    public int[] currentRoomTLMN_SL = null;


    public int[] getCurrentFakeRoomTLMN() {
        for (int i = 0; i < this.currentRoomTLMN.length; i++) {
            int random = randomBetween(0, 3);
            if (random == 1) {
                if (this.currentRoomTLMN[i] + random < tlmnRoomFakeConfig[i][1]) {
                    this.currentRoomTLMN[i] += random;
                }
            }
            if (random == 0) {
                if (this.currentRoomTLMN[i] - random > tlmnRoomFakeConfig[i][0]) {
                    this.currentRoomTLMN[i] -= random;
                }
            }
        }

        return this.currentRoomTLMN;
    }

    public int[] getCurrentFakeRoomTLMNSL() {

        for (int i = 0; i < this.currentRoomTLMN_SL.length; i++) {
            int random = randomBetween(0, 2);
            if (random == 1) {
                if (this.currentRoomTLMN_SL[i] + random < tlmnSLRoomFakeConfig[i][1]) {
                    this.currentRoomTLMN_SL[i] += random;
                }
            }
            if (random == 0) {
                if (this.currentRoomTLMN_SL[i] - random > tlmnSLRoomFakeConfig[i][0]) {
                    this.currentRoomTLMN_SL[i] -= random;
                }
            }
        }
        return this.currentRoomTLMN;
    }


    private static Random rd = new Random();

    public static int randomBetween(int a, int b) {
        return a + rd.nextInt(b - a);
    }
}
