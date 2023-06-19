package com.vinplay.interfaces.ag;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.vinplay.dto.ag.GetAGGamesReportsResponseDTO;
import com.vinplay.dto.ag.GetAGSportReportsResponseDTO;
import com.vinplay.interfaces.ag.utils.HttpUtils;
import com.vinplay.interfaces.ibc2.utils.DateTimeUtil;
import com.vinplay.usercore.utils.GameThirdPartyInit;

public final class ReportAGService {
	private static final Logger logger = Logger.getLogger(ReportAGService.class);

	public GetAGGamesReportsResponseDTO getLiveGamesReportsData(Object reqDto) {
		String params = this.buildParams(reqDto);
		logger.info("AG request GetAGGamesReportsResponseDTO :" + params);
		String result = HttpUtils.getReportData(this.buildUrl("getorders.xml", params), this.buildKey(reqDto));
		logger.info("AG response GetAGGamesReportsResponseDTO :" + result);
		return convertLiveGamesData(result);
	}

	public GetAGSportReportsResponseDTO getSportReportsData(Object reqDto) {
		String params = this.buildParams(reqDto);
		logger.info("AG request GetAGSportReportsResponseDTO :" + params);
		String result = HttpUtils.getReportData(this.buildUrl("getagsportorders_ex.xml", params),
				this.buildKey(reqDto));
		logger.info("AG response GetAGSportReportsResponseDTO:" + result);
		return convertSportData(result);
	}

	private GetAGGamesReportsResponseDTO convertLiveGamesData(String result) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(GetAGGamesReportsResponseDTO.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			logger.info("Convert Live Games report xml AG to Object result:" + result);
			return (GetAGGamesReportsResponseDTO) jaxbUnmarshaller.unmarshal(new StringReader(result));
		} catch (Exception e) {
			logger.error("Error : Convert Live Games report xml AG to Object result: " + result);
			logger.error("Error : Convert Live Games report xml AG to Object Exception: " + e.getMessage());
			return null;
		}
	}

	private GetAGSportReportsResponseDTO convertSportData(String result) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(GetAGSportReportsResponseDTO.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			logger.info("Convert Sport report xml AG to Object result: " + result);
			return (GetAGSportReportsResponseDTO) jaxbUnmarshaller.unmarshal(new StringReader(result));
		} catch (Exception e) {
			logger.error("Error : Convert Sport report xml AG to Object result:" + result);
			logger.error("Error : Convert Sport report xml AG to Object Exception:" + e.getMessage());
			return null;
		}
	}

	private String buildUrl(String method, String params) {
		StringBuffer uri = new StringBuffer();
		uri.append(method).append("?").append(params);
		String url = GameThirdPartyInit.AG_DATA_API_URL + uri.toString();
		logger.info("--REQUEST URL: " + url);
		return url;
	}

	private String buildParams(Object reqDto) {
		String result = "";
		StringBuilder params = new StringBuilder();
		try {
			Field[] fields = reqDto.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				String fieldName = field.getName();
				Object fieldObject = field.get(reqDto);
				if (fieldName == null || "serialVersionUID".equals(fieldName)) {
					continue;
				}
				if (fieldObject == null) {
					continue;
				}
				String fieldValue = "";
				if (fieldObject instanceof Date) {
					fieldValue = DateTimeUtil.toISO8601UTC((Date) fieldObject);
					fieldValue = URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()).toLowerCase();
				} else {
					fieldValue = fieldObject.toString();
//					fieldValue = URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString());
				}
				params.append("&").append(fieldName).append("=").append(fieldValue);
			}
			result = params.toString().replaceFirst("&", "");
		} catch (Exception e) {
			logger.error("ex", e);
		}
		return result;
	}

	private String buildKey(Object reqDto) {
		StringBuilder params = new StringBuilder();
		try {
			Field[] fields = reqDto.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				String fieldName = field.getName();
				Object fieldObject = field.get(reqDto);
				if (fieldName == null || "serialVersionUID".equals(fieldName)) {
					continue;
				}
				if (fieldObject == null) {
					continue;
				}
				String fieldValue = "";
				if (fieldObject instanceof Date) {
					fieldValue = DateTimeUtil.toISO8601UTC((Date) fieldObject);
					fieldValue = URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()).toLowerCase();
				} else {
					fieldValue = fieldObject.toString();
//					fieldValue = URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString());
				}
				params.append(fieldValue);
			}
			return params.toString();
		} catch (Exception ex) {
			logger.error("ex", ex);
		}

		return "";
	}
}
