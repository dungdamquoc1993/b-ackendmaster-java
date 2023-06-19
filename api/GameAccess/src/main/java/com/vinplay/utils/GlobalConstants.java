/**
 * Archie
 */
package com.vinplay.utils;

import java.util.Arrays;
import java.util.List;

/**
 * @author Archie
 *
 */
public interface GlobalConstants {
	public interface ServerInfo {
		String SAGGING_SERVER = "116.93.111.247";
		String WEB_SERVER = "104.199.204.75";
		String WAP_SERVER = "34.80.67.151";
		String PRO_SERVER = "47.75.110.67";
		String LOCAL_SERVER = "127.0.0.1";
	}
	
	List<String> IP_EBET = Arrays.asList(new String[] { 
					"59.148.22.125", 
					"58.64.207.221", 
					"118.143.134.93", 
					"61.244.211.20", 
					"61.238.107.22",
					"61.64.54.130", 
					"119.28.56.169", 
					"47.91.165.178", 
					"203.69.30.85", 
					"203.69.30.86"});
	
	List<String> IP_CMD_STAGING = Arrays.asList(new String[] { "59.124.16.130", "211.23.76.7", "211.23.39.181", "211.23.39.182","52.114.14.71" });
	List<String> IP_CMD_PRO = Arrays.asList(new String[] { "218.189.20.66", "203.192.143.130", "43.240.93.22" });
	
	List<String> IP_ESPORT_STAGING = Arrays.asList(new String[] { "13.251.252.244", "13.251.28.37", "18.136.114.15" });
	List<String> IP_ESPORT_PRO = Arrays.asList(new String[] { "13.251.252.244", "13.251.28.37", "18.136.114.15" });
	
	List<String> IP_ALLOWS_OFFICE = Arrays.asList(new String[] { "127.0.0.1" });
}
