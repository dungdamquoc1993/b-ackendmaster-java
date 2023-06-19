package com.vinplay.pay.server;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleMain {
	public static void run() {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(8);

		executor.scheduleAtFixedRate(new TaskAutoPayment(), 1, 60 * 60, TimeUnit.SECONDS);//10 minutes
	}
}