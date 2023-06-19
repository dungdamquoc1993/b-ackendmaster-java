package com.vinplay.interfaces.wm.utils;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class DateDeserializer implements JsonDeserializer<Date> {

	public Date deserialize(JsonElement element, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		String date = element.getAsString();

		try {
			if ("".equals(date) == false) {
				return DateTimeUtil.fromISO8601UTC(date);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}