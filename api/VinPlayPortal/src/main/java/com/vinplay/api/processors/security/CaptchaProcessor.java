package com.vinplay.api.processors.security;

import java.awt.Font;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;

import com.github.cage.Cage;
import com.github.cage.IGenerator;
import com.github.cage.image.EffectConfig;
import com.github.cage.image.Painter;
import com.github.cage.image.ScaleConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.api.processors.response.CaptchaResponse;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.utils.StringUtils;
import com.vinplay.vbee.common.utils.VinPlayUtils;

public class CaptchaProcessor implements BaseProcessor<HttpServletRequest, String> {

	private static final Logger logger = Logger.getLogger("api");

	public String execute(Param<HttpServletRequest> param) {
		try {
			IGenerator<Font> fonts = new IGenerator<Font>() {
				public Font next() {
					return new Font("Monospaced", 0, 8);
				}
			};
			Painter painter = new Painter(109, 38, null, Painter.Quality.MAX,
					new EffectConfig(false, false, false, false, new ScaleConfig(1.0f, 1.0f)), null);
			Cage cage = new Cage(painter, fonts, null, null, null, null, null);
			HazelcastInstance client = HazelcastClientFactory.getInstance();
			IMap<String, String> captchaCache = client.getMap("cacheCaptcha");
			String captcha = StringUtils.randomString(3);
			String idCaptcha = VinPlayUtils.getMD5Hash(captcha).substring(0, 3) + System.currentTimeMillis();
			captchaCache.put(idCaptcha, captcha, 3L, TimeUnit.MINUTES);
			String img = DatatypeConverter.printBase64Binary(cage.draw(captcha));
			CaptchaResponse res = new CaptchaResponse(idCaptcha, img);
			return res.toJson();
		} catch (Exception e) {
			logger.error(e);
			return "";
		}
	}

}
