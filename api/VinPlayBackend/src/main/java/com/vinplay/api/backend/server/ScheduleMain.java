package com.vinplay.api.backend.server;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleMain {
	public static void run() {

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);

		executor.scheduleAtFixedRate(new TaskAutoClearData(), 1, 1, TimeUnit.DAYS);// 1 day

		executor.scheduleAtFixedRate(new TaskCreateAttendence(), 2, 59, TimeUnit.MINUTES);// 1 day

	}
}