package com.vinplay.vbee.common.pools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.vinplay.vbee.common.config.VBeePath;
import com.vinplay.vbee.common.statics.Consts;

import snaq.db.ConnectionPoolManager;

public class ConnectionPool {
    public static final String USER_POOL = "mysqlpoolname";
    public static final String MINIGAME_POOL = "mysqlpool_minigame";
    public static final String ADMIN_POOL = "mysqlpool_admin";
    public static final String GAMEBAI_POOL = "mysqlpool_gamebai";
    public static final String XC_GIFTCODE_POOL = "mysql_xc_giftcode";
    private static String CONFIG_FILE = Consts.DB_CONFIG_FILE;
    private static final int DEFAULT_TIMEOUT = 5000;
    private ConnectionPoolManager managerVinPlay = null;
    private static ConnectionPool _instance;
    private static final Logger logger = Logger.getLogger("wspay");
    
    public static ConnectionPool getInstance() {
        if (null == _instance) {
            _instance = new ConnectionPool();
        }
        return _instance;
    }

    public static void start(String configFile) {
    	CONFIG_FILE = configFile;
    }

    private ConnectionPool() {
        try {
            this.init(CONFIG_FILE);
        } catch (IOException e) {
        	logger.error(e);
        }
    }
//
//    public void init2(String configFile) throws IOException {
//        Properties prop = new Properties();
//        FileInputStream input = new FileInputStream(configFile);
//        prop.load(input);
//        if (input != null) {
//            input.close();
//        }
//        ConnectionPoolManager.createInstance((Properties)prop);
//        this.managerVinPlay = ConnectionPoolManager.getInstance();
//    }

	public void init(String configFile) throws IOException {
		try (BufferedReader br = Files.newBufferedReader(Paths.get(configFile))) {
			Properties prop = new Properties();
			prop.load(br);
			ConnectionPoolManager.createInstance((Properties) prop);
			this.managerVinPlay = ConnectionPoolManager.getInstance();
		} catch (IOException e) {
			logger.error("connection_pool error" + e);
		}
	}

    public Connection getConnection(String poolName) {
        Connection conn = null;
        try {
            conn = this.managerVinPlay.getConnection(poolName, DEFAULT_TIMEOUT);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }        
        return conn;
    }

    public static void releaseConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

