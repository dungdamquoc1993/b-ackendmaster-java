package com.vinplay.interfaces.ibc2.utils;

import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtil {
	
	private static Logger logger = Logger.getLogger(DateTimeUtil.class);
	public static String toISO8601UTC(Date datetime) {
		return  DateTimeFormatter.ISO_DATE_TIME.format(convertToLocalDateTimeViaInstant(datetime));
	}
	public static LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
	    return dateToConvert.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDateTime();
	}
	public static Date fromISO8601UTC(String dateStr) {
		LocalDateTime ldt = LocalDateTime.parse(dateStr);
		Date out = Date.from(ldt.atZone(ZoneId.of("GMT-4")).toInstant());
		return out;
	}
	/**
	 * Transform ISO 8601 string to Date.
	 */
	public static Date toDate(final String iso8601string) throws ParseException {
	    String s = iso8601string.replace("Z", "+00:00");
	    try {
	        s = s.substring(0, 22) + s.substring(23);  // to get rid of the ":"
	    } catch (IndexOutOfBoundsException e) {
	    	logger.error("e", e);
	        throw new ParseException("Invalid length", 0);
	    }
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	    Date date = df.parse(s);
	    return date;
	}
}
