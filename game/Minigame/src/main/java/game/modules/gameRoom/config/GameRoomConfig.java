package game.modules.gameRoom.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.vinplay.vbee.common.config.VBeePath;
import org.json.JSONObject;

public class GameRoomConfig {
    public JSONObject config;
    private static GameRoomConfig gameRoom = null;

    private GameRoomConfig() {
        this.initconfig();
    }

    public static GameRoomConfig instance() {
        if (gameRoom == null) {
            gameRoom = new GameRoomConfig();
        }
        return gameRoom;
    }

    public void initconfig() {
        String path = VBeePath.basePath;
        File file = new File(path + "conf/gameroom.json");
        StringBuffer contents = new StringBuffer();
        BufferedReader reader = null;
        InputStreamReader r=null;
        try {
            r = new InputStreamReader(new FileInputStream(file), "UTF-8");
            reader = new BufferedReader(r);
            String text = null;
            while ((text = reader.readLine()) != null) {
                contents.append(text).append(System.getProperty("line.separator"));
            }
            this.config = new JSONObject(contents.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (r != null) {
				try {
					r.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
    }
}

