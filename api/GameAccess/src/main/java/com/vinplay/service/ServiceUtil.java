package com.vinplay.service;

import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

/**
 * Provide helper methods.
 */
public class ServiceUtil {

	public static void dumpRequestParamListToLog(Logger logger, List<SelectItem> items) {
		if (!logger.isDebugEnabled()) {
			return;
		}
		if (items == null || items.isEmpty()) {
			logger.debug("No parameter.");
			return;
		}

		StringBuilder sb = new StringBuilder("Params: ");
		for (SelectItem item : items) {
			sb.append(item.getLabel());
			sb.append("=");
			sb.append(String.valueOf(item.getValue()));
			sb.append("; ");
		}
		logger.debug(sb.toString());
	}
}
