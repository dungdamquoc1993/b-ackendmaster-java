package com.vinplay.vbee.main;

import com.vinplay.vbee.common.config.VBeePath;
import com.vinplay.vbee.common.hazelcast.HazelcastLoader;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.dao.impl.LogMoneyUserDaoImpl;
import com.vinplay.vbee.dao.impl.LuckyDaoImpl;
import com.vinplay.vbee.rmq.RMQ;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class VBeeMain {
    private static final String LOG_PROPERTIES_FILE = "config/log4j.properties";
    private static final Logger logger = Logger.getLogger((String)"vbee");
    public static long luckyReferenceId;
    public static long moneyVinReferenceId;
    public static long moneyXuReferenceId;
    private static String basePath;

    private static void initializeLogger() {
        Properties logProperties = new Properties();
        try {
            File file = new File(basePath.concat(LOG_PROPERTIES_FILE));
            logProperties.load(new FileInputStream(file));
            PropertyConfigurator.configure((Properties)logProperties);
            logger.info("Logging initialized.");
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to load logging property config/log4j.properties");
        }
    }

    public static void main(String[] args) {
        basePath = VBeePath.initBasePath(VBeeMain.class);
        VBeeMain.initializeLogger();
        logger.info("VBEE INIT");
        try {
            HazelcastLoader.start();
            MongoDBConnectionFactory.init();
            LuckyDaoImpl lkDao = new LuckyDaoImpl();
            luckyReferenceId = lkDao.getLastReferenceId();
            LogMoneyUserDaoImpl mnDao = new LogMoneyUserDaoImpl();
            moneyVinReferenceId = mnDao.getLastReferenceId("vin");
            moneyXuReferenceId = mnDao.getLastReferenceId("xu");
            logger.info("luckyReferenceId: " + luckyReferenceId);
            logger.info("moneyVinReferenceId: " + moneyVinReferenceId);
            logger.info("moneyXuReferenceId: " + moneyXuReferenceId);
            RMQ.start();
        }
        catch (IOException e) {
            e.printStackTrace();
            logger.debug(e);
        }
    }
}

