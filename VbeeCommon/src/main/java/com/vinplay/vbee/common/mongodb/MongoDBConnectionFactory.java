package com.vinplay.vbee.common.mongodb;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.vinplay.vbee.common.statics.Consts;

public class MongoDBConnectionFactory {
	private static String MONGODB_HOST = "";
	private static String MONGODB_SLAVE1 = "";
	private static String MONGODB_SLAVE2 = "";
	private static String MONGODB_DATABASE = "win123club";
	private static String MONGODB_AUTH_DATABASE = "admin";
	private static String MONGODB_USERNAME = "";
	private static String MONGODB_PASSWORD = "";
	private static int MONGODB_PORT = 27017;

	private static MongoClient mongoClientMain;
	private static MongoClient mongoClientSlave1;
	private static MongoClient mongoClientSlave2;
	private volatile static boolean COUNT = false;

	public static void init() throws IOException {
		try (BufferedReader br = Files.newBufferedReader(Paths.get(Consts.MONGO_CONFIG_FILE))) {
			Properties prop = new Properties();
			prop.load(br);
			MONGODB_HOST = prop.getProperty("host");
			MONGODB_SLAVE1 = prop.getProperty("hostslave1");
			MONGODB_SLAVE2 = prop.getProperty("hostslave2");
			MONGODB_DATABASE = prop.getProperty("database");
			MONGODB_AUTH_DATABASE = prop.getProperty("auth_database");
			MONGODB_PORT = Integer.parseInt(prop.getProperty("port"));
			MONGODB_USERNAME = prop.getProperty("username");
			MONGODB_PASSWORD = prop.getProperty("password");
			
			MongoDBConnectionFactory.newConnection();
			MongoDBConnectionFactory.newConnectionS1();
			MongoDBConnectionFactory.newConnectionS2();
		}
	}

    public static void newConnection() {
        MongoCredential credential = MongoCredential.createCredential(MONGODB_USERNAME, MONGODB_AUTH_DATABASE, (char[])MONGODB_PASSWORD.toCharArray());
        mongoClientMain = new MongoClient(new ServerAddress(MONGODB_HOST, MONGODB_PORT), Arrays.asList(new MongoCredential[]{credential}));
    }
    
    public static void newConnectionS1() {
        MongoCredential credential = MongoCredential.createCredential(MONGODB_USERNAME, MONGODB_AUTH_DATABASE, (char[])MONGODB_PASSWORD.toCharArray());
        mongoClientSlave1 = new MongoClient(new ServerAddress(MONGODB_SLAVE1, MONGODB_PORT), Arrays.asList(new MongoCredential[]{credential}));
    }

	public static void newConnectionS2() {
		MongoCredential credential = MongoCredential.createCredential(MONGODB_USERNAME, MONGODB_AUTH_DATABASE, (char[]) MONGODB_PASSWORD.toCharArray());
		mongoClientSlave2 = new MongoClient(new ServerAddress(MONGODB_SLAVE2, MONGODB_PORT),Arrays.asList(new MongoCredential[] { credential }));
	}

    public static MongoDatabase getDB() {
        if (mongoClientMain == null) {
            MongoDBConnectionFactory.newConnection();
        }
        return mongoClientMain.getDatabase(MONGODB_DATABASE);
    }
    
	public static MongoDatabase getDBSlave() {
		if (COUNT) {
			COUNT = false;
			return getDBSlave1();
		} else {
			COUNT = true;
			return getDBSlave2();
		}
	}
    
    public static MongoDatabase getDBSlave1() {
        if (mongoClientSlave1 == null) {
            MongoDBConnectionFactory.newConnectionS1();
        }
        return mongoClientSlave1.getDatabase(MONGODB_DATABASE);
    }
    
    public static MongoDatabase getDBSlave2() {
        if (mongoClientSlave2 == null) {
            MongoDBConnectionFactory.newConnectionS2();
        }
        return mongoClientSlave2.getDatabase(MONGODB_DATABASE);
    }
}

