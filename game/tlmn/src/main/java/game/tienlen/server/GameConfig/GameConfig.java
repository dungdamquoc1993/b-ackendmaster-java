package game.tienlen.server.GameConfig;

import bitzero.util.common.business.Debug;
import com.google.gson.Gson;
import game.tienlen.server.GameConfig.Config.BetLevelConfig;
import game.tienlen.server.GameConfig.Config.TLMNBotConfig;
import game.tienlen.server.GameConfig.Config.TLMNautoWinConfig;

import java.io.*;

public class GameConfig {

    private static GameConfig instance = null;

    public static Gson gson = new Gson();
    public TLMNBotConfig tlmnBotConfig = new TLMNBotConfig();
    public BetLevelConfig betLevelConfig = new BetLevelConfig();
    public TLMNautoWinConfig tlmNautoWinConfig = new TLMNautoWinConfig();

    public static GameConfig getInstance() {
        if (instance == null) {
            instance = new GameConfig();
        }
        return instance;
    }

    private GameConfig(){

    }

    public void init() {
        String json = "";
        json = loadConfig("TLMNBotConfig.json");
        this.tlmnBotConfig = gson.fromJson(json, TLMNBotConfig.class);

        json = loadConfig("BetLevelConfig.json");
        this.betLevelConfig = gson.fromJson(json, BetLevelConfig.class);

        json = loadConfig("TLMNautoWinConfig.json");
        this.tlmNautoWinConfig = gson.fromJson(json, TLMNautoWinConfig.class);

       // System.out.println("test");

    }

    public static String loadConfig(String fileName) {
        String path = System.getProperty("user.dir");
        File file = new File(path + "/game/tlmn/config/game/" + fileName);
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
        }finally {
			if(bufferedReader!=null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
        return contents.toString();
    }

    public synchronized void setFileConfig(String fileName, Object instance) {
        String path = System.getProperty("user.dir");
        File file = new File(path + "/game/tlmn/config/game/" + fileName);
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
