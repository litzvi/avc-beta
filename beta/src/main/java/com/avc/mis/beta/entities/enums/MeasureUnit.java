/**
 * 
 */
package com.avc.mis.beta.entities.enums;

import java.math.BigDecimal;

/**
 * @author Zvi
 *
 */
public enum MeasureUnit {
	
	KG,
	LBS,
	OZ,
	GRAM;

	private static final BigDecimal LBS_IN_KG = new BigDecimal("0.4536");
	private static final BigDecimal GRAM_IN_KG = new BigDecimal("0.001");
	private static final BigDecimal LBS_IN_OZ = new BigDecimal("16");
	
	public static BigDecimal convert(BigDecimal amount, MeasureUnit fromUnit, MeasureUnit toUnit) {
		switch(toUnit) {
		case LBS:
			return convertToLBS(amount, fromUnit);
		case KG:
			return convertToKG(amount, fromUnit);
		default:
			throw new IllegalArgumentException("Can't convert " + fromUnit + " to " + toUnit);
		}
	}
		
	private static BigDecimal convertToKG(BigDecimal amount, MeasureUnit fromUnit) {
		switch(fromUnit) {
		case OZ:
			amount = amount.divide(LBS_IN_OZ);
		case LBS:
			return amount.multiply(LBS_IN_KG);
		case KG:
			return amount;
		case GRAM:
			return amount.multiply(GRAM_IN_KG);
		default:
			throw new IllegalArgumentException("Can't convert" + fromUnit + "to KG");
		}
	}

	private static BigDecimal convertToLBS(BigDecimal amount, MeasureUnit fromUnit) {
		switch(fromUnit) {
		case GRAM:
			amount = amount.multiply(GRAM_IN_KG);
		case KG:
			return amount.divide(LBS_IN_KG);
		case LBS:
			return amount;
		case OZ:
			return amount.divide(LBS_IN_OZ);
		default:
			throw new IllegalArgumentException("Can't convert" + fromUnit + "to LBS");
		}
	}
	
}
