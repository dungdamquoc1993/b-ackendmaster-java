package game.GameConfig;

import bitzero.util.common.business.Debug;
import com.google.gson.Gson;
import game.GameConfig.ConfigGame.BotJackpotConfig.GalaxyBotConfig;
import game.GameConfig.ConfigGame.BotJackpotConfig.MiniPokerBotConfig;
import game.GameConfig.ConfigGame.BotJackpotConfig.Slot3x3BotConfig;
import com.vinplay.dailyQuest.DailyQuestConfig;
import game.GameConfig.ConfigGame.MinigameConfig.MinipokerGameConfig;
import game.GameConfig.ConfigGame.MinigameConfig.Slot3x3GameConfig;
import game.GameConfig.ConfigGame.SlotMultiJackpotConfig;

import java.io.*;

public class GameConfig {
    private static GameConfig instance = null;

    public static Gson gson = new Gson();

    public MiniPokerBotConfig miniPokerBotConfig = new MiniPokerBotConfig();
    public MinipokerGameConfig minipokerGameConfig = new MinipokerGameConfig();

    public Slot3x3BotConfig slot3x3BotConfig = new Slot3x3BotConfig();
    public Slot3x3GameConfig slot3x3GameConfig = new Slot3x3GameConfig();

    public GalaxyBotConfig galaxyBotConfig = new GalaxyBotConfig();

    public SlotMultiJackpotConfig slotMultiJackpotConfig = new SlotMultiJackpotConfig();

    public DailyQuestConfig dailyQuestConfig = new DailyQuestConfig();

    public static GameConfig getInstance() {
        if (instance == null) {
            instance = new GameConfig();
        }
        return instance;
    }

    public void init() {
        String json = "";
        json = loadConfig("MinipokerBotConfig.json");
        this.miniPokerBotConfig = gson.fromJson(json, MiniPokerBotConfig.class);

        json = loadConfig("MinipokerGameConfig.json");
        this.minipokerGameConfig = gson.fromJson(json, MinipokerGameConfig.class);

        json = loadConfig("Slot3x3BotConfig.json");
        this.slot3x3BotConfig = gson.fromJson(json, Slot3x3BotConfig.class);

        json = loadConfig("Slot3x3GameConfig.json");
        this.slot3x3GameConfig = gson.fromJson(json, Slot3x3GameConfig.class);

        json = loadConfig("GalaxyBotConfig.json");
        this.galaxyBotConfig = gson.fromJson(json, GalaxyBotConfig.class);

        json = loadConfig("SlotMultiJackpotConfig.json");
        this.slotMultiJackpotConfig = gson.fromJson(json, SlotMultiJackpotConfig.class);
    }


    public static String loadConfig(String fileName) {
        String path = System.getProperty("user.dir");
        File file = new File(path + "/game/Minigame/config/game/" + fileName);
        StringBuffer contents = new StringBuffer();
        BufferedReader bufferedReader = null;

        try {
            Reader r = new InputStreamReader(new FileInputStream(file), "UTF-8");
            bufferedReader = new BufferedReader(r);
            String text = null;
            while ((text = bufferedReader.readLine()) != null) {
                contents.append(text).append(System.getProperty("line.separator"));
            }
        } catch (UnsupportedEncodingException e) {
            Debug.trace(e);
        } catch (FileNotFoundException e) {
            Debug.trace(e);
        } catch (IOException e) {
            Debug.trace(e);
        }
        return contents.toString();
    }

    public synchronized void setFileConfig(String fileName, Object instance) {
        String path = System.getProperty("user.dir");
        File file = new File(path + "/game/Minigame/config/game/" + fileName);
        String json = gson.toJson(instance);
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), "utf-8"));
            writer.write(json);
        } catch (Exception e) {
            Debug.trace(e);
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {

            }
        }
    }
}
