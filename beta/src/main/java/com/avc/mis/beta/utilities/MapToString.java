/**
 * 
 */
package com.avc.mis.beta.utilities;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author zvi
 *
 */
@Converter
public class MapToString  implements AttributeConverter<Map<Integer, BigDecimal>, String> {

    private static ObjectMapper objectMapper;

	@Override
	public String convertToDatabaseColumn(Map<Integer, BigDecimal> map) {
		if(map == null) {
			return null;
		}
		try {
			return objectMapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("Can't convert map to json string");
		}
	}

	@Override
	public Map<Integer, BigDecimal> convertToEntityAttribute(String jsonMap) {
		if(jsonMap == null) {
			return null;
		}
        try {
            return objectMapper.readValue(jsonMap, Map.class);
        } catch (IOException e) {
        	throw new IllegalStateException("Can't convert json string to map");
        }
	}

}
