package com.vinplay.shotfish.entites;

import java.io.Serializable;

public class TeleBotConfig implements Serializable {
	public String nameBot;
	public String secretKey;

	public TeleBotConfig() {
		super();
	}

	public TeleBotConfig(String nameBot, String secretKey) {
		super();
		this.nameBot = nameBot;
		this.secretKey = secretKey;
	}
}
