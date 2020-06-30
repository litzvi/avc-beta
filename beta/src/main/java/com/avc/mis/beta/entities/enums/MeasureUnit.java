/**
 * 
 */
package com.avc.mis.beta.entities.enums;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.settings.UOM;

/**
 * @author Zvi
 *
 */
public enum MeasureUnit {
	
	KG,
	LBS,
	OZ,
	GRAM,
	LOT;
	
	private static final Map<MeasureUnit, Map<MeasureUnit, UOM>> CONVERTION_MAP;
	
	static {
		CONVERTION_MAP = new HashMap<MeasureUnit, Map<MeasureUnit, UOM>>();
		
		Map<MeasureUnit, UOM> kgMap = new HashMap<MeasureUnit, UOM>();
		kgMap.put(KG, new UOM(KG, KG, BigDecimal.ONE, BigDecimal.ONE));
		kgMap.put(LBS, new UOM(KG, LBS, BigDecimal.ONE, new BigDecimal("0.4536")));
		kgMap.put(LOT, new UOM(KG, LOT, BigDecimal.ONE, new BigDecimal("15876")));
		CONVERTION_MAP.put(KG, kgMap);
		
		Map<MeasureUnit, UOM> lbsMap = new HashMap<MeasureUnit, UOM>();
		lbsMap.put(KG, new UOM(LBS, KG, new BigDecimal("0.4536"), BigDecimal.ONE));
		lbsMap.put(LBS, new UOM(LBS, LBS, BigDecimal.ONE, BigDecimal.ONE));
		lbsMap.put(LOT, new UOM(LBS, LOT, BigDecimal.ONE, new BigDecimal("35000")));
		CONVERTION_MAP.put(LBS, lbsMap);
		
	}
	
	public static BigDecimal convert(BigDecimal amount, MeasureUnit fromUnit, MeasureUnit toUnit) {
		UOM convertUOM = CONVERTION_MAP.get(fromUnit).get(toUnit);
		return amount
				.multiply(convertUOM.getMultiplicand())
				.divide(convertUOM.getDivisor(), AmountWithUnit.SCALE, RoundingMode.HALF_DOWN);
	}

//	public static final int SCALE = 3;
	private static final BigDecimal LBS_IN_KG = new BigDecimal("0.4536");
	private static final BigDecimal GRAM_IN_KG = new BigDecimal("0.001");
	private static final BigDecimal LBS_IN_OZ = new BigDecimal("16");
	
//	public static BigDecimal convert(BigDecimal amount, MeasureUnit fromUnit, MeasureUnit toUnit) {
//		switch(toUnit) {
//		case LBS:
//			return convertToLBS(amount, fromUnit);
//		case KG:
//			return convertToKG(amount, fromUnit);
//		default:
//			throw new IllegalArgumentException("Can't convert " + fromUnit + " to " + toUnit);
//		}
//	}
		
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
