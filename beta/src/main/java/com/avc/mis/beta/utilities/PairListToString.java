/**
 * 
 */
package com.avc.mis.beta.utilities;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author zvi
 *
 */
@Converter
public class PairListToString  implements AttributeConverter<List<Pair<Integer, BigDecimal>>, String> {

    private static ObjectMapper objectMapper;

	@Override
	public String convertToDatabaseColumn(List<Pair<Integer, BigDecimal>> list) {
		if(list == null) {
			return null;
		}
		try {
			return objectMapper.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("Can't convert list to json string");
		}
	}

	@Override
	public List<Pair<Integer, BigDecimal>> convertToEntityAttribute(String jsonMap) {
		if(jsonMap == null) {
			return null;
		}
        try {
            return objectMapper.readValue(jsonMap, new TypeReference<List<Pair<Integer, BigDecimal>>>() { });
        } catch (IOException e) {
        	throw new IllegalStateException("Can't convert json string to map");
        }
	}

}
