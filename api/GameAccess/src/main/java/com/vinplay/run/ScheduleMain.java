package com.vinplay.run;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.vinplay.usercore.utils.GameThirdPartyInit;

public class ScheduleMain {
	public static void run() {
		if ("pro".equals(GameThirdPartyInit.enviroment)) {
			ScheduledExecutorService executor = Executors.newScheduledThreadPool(50);

			// 1. IBC2
			executor.scheduleAtFixedRate(new TaskAutoCreateUserIbc2(), 13, 30 * 60, TimeUnit.SECONDS);
			executor.scheduleAtFixedRate(new TaskCollectIBC2BetLog(), 14, 5 * 60, TimeUnit.SECONDS);

			// 2. WM
			executor.scheduleAtFixedRate(new TaskAutoCreateUserWm(), 19, 20 * 60, TimeUnit.SECONDS);
			executor.scheduleAtFixedRate(new TaskCollectWMLog(), 20, 5 * 60, TimeUnit.SECONDS);

			// 3 .AG
			executor.scheduleAtFixedRate(new TaskAutoCreateUserAG(), 15, 30 * 60, TimeUnit.SECONDS);
			executor.scheduleAtFixedRate(new TaskCollectAGLog(), 16, 5 * 60, TimeUnit.SECONDS);
			
			// 3 .CMD
			//executor.scheduleAtFixedRate(new TaskAutoCollectCmdLogs(), 15, 5 * 60, TimeUnit.SECONDS);
			//executor.scheduleAtFixedRate(new TaskAutoCreateCmdUser(), 16, 20 * 60, TimeUnit.SECONDS);
			
			//SBO
			executor.scheduleAtFixedRate(new TaskAutoCollectSboBetLogs(), 15, 5 * 60, TimeUnit.SECONDS);
			executor.scheduleAtFixedRate(new TaskAutoCreateSboUser(), 16, 30 * 60, TimeUnit.SECONDS);
			
			//FISH
			executor.scheduleAtFixedRate(new TaskCollecFishLog(), 15, 5 * 60, TimeUnit.SECONDS);
			
			//Ebet
			executor.scheduleAtFixedRate(new TaskAutoCreateUserEbet(), 15, 30 * 60, TimeUnit.SECONDS);
			executor.scheduleAtFixedRate(new TaskCollectEBetLog(), 16, 5 * 60, TimeUnit.SECONDS);
			
		} else if ("dev".equals(GameThirdPartyInit.enviroment)) {
			ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);

			// ibc2
//			executor.scheduleAtFixedRate(new TaskAutoCreateUserIbc2(), 4, 5 * 60, TimeUnit.SECONDS);
//			executor.scheduleAtFixedRate(new TaskCollectIBC2BetLog(), 5, 5 * 60, TimeUnit.SECONDS);
			//executor.scheduleAtFixedRate(new TaskAutoCreateUserAG(), 15, 30 * 60, TimeUnit.SECONDS);
//
//////			// WM
//			executor.scheduleAtFixedRate(new TaskAutoCreateUserWm(), 17, 5 * 60, TimeUnit.SECONDS);
//			executor.scheduleAtFixedRate(new TaskCollectWMLog(), 18, 5 * 60, TimeUnit.SECONDS);
//////
////			// 8 .AG
////			executor.scheduleAtFixedRate(new TaskAutoCreateUserAG(), 15, 30 * 60, TimeUnit.SECONDS);
////			executor.scheduleAtFixedRate(new TaskCollectAGLog(), 16, 5 * 60, TimeUnit.SECONDS);
//			
//
////			executor.scheduleAtFixedRate(new TaskAutoCreateUserAG(), 15, 30 * 60, TimeUnit.SECONDS);
////			executor.scheduleAtFixedRate(new TaskCollectAGLog(), 16, 5 * 60, TimeUnit.SECONDS);
//			
////			// CMD
//			executor.scheduleAtFixedRate(new TaskAutoCollectCmdLogs(), 15, 5 * 60, TimeUnit.SECONDS);
//			executor.scheduleAtFixedRate(new TaskAutoCreateCmdUser(), 16, 20 * 60, TimeUnit.SECONDS);
			
			//EBET
//			executor.scheduleAtFixedRate(new TaskAutoCreateUserEbet(), 15, 30 * 60, TimeUnit.SECONDS);
			executor.scheduleAtFixedRate(new TaskCollectEBetLog(), 16, 5*30 , TimeUnit.SECONDS);
		
		// SBO
//		executor.scheduleAtFixedRate(new TaskAutoCollectSboBetLogs(), 15, 5 * 60, TimeUnit.SECONDS);
//		executor.scheduleAtFixedRate(new TaskAutoCreateSboUser(), 16, 1, TimeUnit.SECONDS);
		
		//FISH
//		executor.scheduleAtFixedRate(new TaskCollecFishLog(), 15, 5 * 60, TimeUnit.SECONDS);
		}
	}
}