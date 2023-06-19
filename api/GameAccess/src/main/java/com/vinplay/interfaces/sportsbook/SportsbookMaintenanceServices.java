package com.vinplay.interfaces.sportsbook;

import java.util.Objects;

public class SportsbookMaintenanceServices {
	private static SportsbookMaintenanceServices INSTANCE;

	private SportsbookMaintenanceServices() {
	}

	public static SportsbookMaintenanceServices getInstance() {
		if (Objects.isNull(INSTANCE)) {
			INSTANCE = new SportsbookMaintenanceServices();
		}
		return INSTANCE;
	}

}
