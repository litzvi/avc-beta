/**
 * 
 */
package com.avc.mis.beta.utilities;

import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converts local date to long and vice versa, for recording and fetching from database respectively.
 * 
 * @author Zvi
 *
 */
@Converter
public class LocalDateToLong implements AttributeConverter<LocalDate, Long> {

	@Override
    public Long convertToDatabaseColumn(LocalDate date) {
        if (date != null) {
            long epochDay = date.toEpochDay();
            return epochDay;
        }
        return null;
    }

    @Override
    public LocalDate convertToEntityAttribute(Long epochDay) {
        if (epochDay != null) {
            LocalDate date = LocalDate.ofEpochDay(epochDay);
            return date;
        }
        return null;
    }
}
