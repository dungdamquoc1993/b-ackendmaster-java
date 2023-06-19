package com.vinplay.api.backend.server;

import java.util.TimerTask;

import com.vinplay.usercore.service.AttendanceService;
import com.vinplay.usercore.service.impl.AttendanceServiceImpl;

public class TaskCreateAttendence extends TimerTask {

	private AttendanceService attendanceService = new AttendanceServiceImpl();

	@Override
	public void run() {
		try {
			attendanceService.createConfig(500);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
