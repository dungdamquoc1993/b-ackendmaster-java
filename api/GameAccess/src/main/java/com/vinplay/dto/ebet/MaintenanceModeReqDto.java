package com.vinplay.dto.ebet;

import java.io.Serializable;

public class MaintenanceModeReqDto extends BaseReqDto implements
		Serializable {
	private static final long serialVersionUID = -4633827829683041086L;
	private Boolean Enabled;

	public Boolean getEnabled() {
		return Enabled;
	}

	public void setEnabled(Boolean enabled) {
		Enabled = enabled;
	}
}
