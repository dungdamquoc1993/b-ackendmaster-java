package com.vinplay.interfaces.ag;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.apache.log4j.Logger;
import com.vinplay.dto.ag.BaseAGResponseDto;
import com.vinplay.interfaces.ag.utils.HttpUtils;
import com.vinplay.usercore.utils.GameThirdPartyInit;

public class BaseAGService {
    private static final Logger LOGGER = Logger.getLogger(BaseAGService.class);

    public BaseAGResponseDto getData(Object reqDto) {
        String params = buildParams(reqDto);
        LOGGER.info("[BaseAGService] AG request:" + params);
        String result = HttpUtils.postData(GameThirdPartyInit.AG_API_URL, params);
        LOGGER.info("[BaseAGService] AG response:" + result);
        return covertData(result);
    }

    private BaseAGResponseDto covertData(String result) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BaseAGResponseDto.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            LOGGER.info("[BaseAGService] Convert xml AG to Object result:" + result);
            return (BaseAGResponseDto) jaxbUnmarshaller.unmarshal(new StringReader(result));
        } catch (Exception e) {
            LOGGER.error("[BaseAGService] Error : Convert xml AG to Object result:" + result);
            LOGGER.error("[BaseAGService] Error : Convert xml AG to Object Exception:" + e.getMessage());
            return null;
        }
    }

    /**
     * Build query param
     * 
     * @param reqDto 
     * @return query param
     */
    public String buildParams(Object reqDto) {
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
                    fieldValue = toISO8601UTC((Date) fieldObject);
                    fieldValue =
                            URLEncoder.encode(fieldValue, StandardCharsets.ISO_8859_1.toString())
                                    .toLowerCase();
                } else {
                    fieldValue = fieldObject.toString();
                    fieldValue =
                            URLEncoder.encode(fieldValue, StandardCharsets.ISO_8859_1.toString());
                }
                params.append("&").append(fieldName).append("=").append(fieldValue);
            }
            result = params.toString().replaceFirst("&", "");
            result = result.replaceAll("&", "/\\\\\\\\\\\\\\\\/");
        } catch (Exception e) {
            LOGGER.error("[BaseAGService] Exception occur when build param with reqDto {}" + reqDto
                    + ". Exception: " + e);
        }
        return result;
    }
    public static String toISO8601UTC(Date datetime) {
		return  DateTimeFormatter.ISO_DATE_TIME.format(convertToLocalDateTimeViaInstant(datetime));
	}
    public static LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
	    return dateToConvert.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDateTime();
	}
}
