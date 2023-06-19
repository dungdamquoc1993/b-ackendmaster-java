/*
 * Decompiled with CFR 0.144.
 */
package com.vinplay.vbee.common.hazelcast;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import com.vinplay.vbee.common.statics.Consts;

public class HazelcastLoader {
    public static void start() throws IOException {
    	
        try (BufferedReader br = Files.newBufferedReader(Paths.get(Consts.HAZELCAST_CONFIG_FILE))) {
			Properties prop = new Properties();
			prop.load(br);
	        HazelcastClientFactory.ADDRESS = prop.getProperty("address");
	        HazelcastClientFactory.GROUP_NAME = prop.getProperty("group_name");
	        HazelcastClientFactory.GROUP_PASS = prop.getProperty("group_pass");
	        HazelcastClientFactory.initDefault();
		}
       
    }
}

