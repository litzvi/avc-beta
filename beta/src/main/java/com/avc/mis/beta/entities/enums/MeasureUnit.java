/**
 * 
 */
package com.avc.mis.beta.entities.enums;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.settings.UOM;

import lombok.NonNull;

/**
 * Enum representing physical weight measure units in different denominations and measure systems.
 * 
 * @author Zvi
 *
 */
public enum MeasureUnit {
	
	KG,
	LBS,
	LOT,
	OZ,
	GRAM, 
	UNIT,
	BOX;
	
	/**
	 * Nested map with full Cartesian product for converting from every unit to any other unit.
	 * Outer map key is the origin unit and inner map key for destination unit.
	 * For every ordered pair of origin and destination units the inner map value has the UOM for the convention.
	 * 
	 * Should be used for inserting all UOM to database on deployment, so conversions could be done in queries.
	 */
	public static final Map<MeasureUnit, Map<MeasureUnit, UOM>> CONVERTION_MAP;
	
	public static final Set<MeasureUnit> WEIGHT_UNITS = EnumSet.of(KG, LBS, LOT, OZ, GRAM);
	public static final Set<MeasureUnit> DISCRETE_UNITS = EnumSet.of(UNIT, BOX);

	
	/**
	 * Default scale when returning result from conversion
	 */
	public static final int SCALE = 3;
	public static final int CALCULATION_SCALE = 16;
	
	static {
		CONVERTION_MAP = new HashMap<MeasureUnit, Map<MeasureUnit, UOM>>();
		
		Map<MeasureUnit, UOM> kgMap = new HashMap<MeasureUnit, UOM>();
		kgMap.put(KG, new UOM(KG, KG, BigDecimal.ONE, BigDecimal.ONE));
		kgMap.put(GRAM, new UOM(KG, GRAM, new BigDecimal("1000"), BigDecimal.ONE));
		kgMap.put(LBS, new UOM(KG, LBS, BigDecimal.ONE, new BigDecimal("0.4536")));
		kgMap.put(LOT, new UOM(KG, LOT, BigDecimal.ONE, new BigDecimal("15876")));
		kgMap.put(OZ, new UOM(KG, OZ, new BigDecimal("16"), new BigDecimal("0.4536")));
		CONVERTION_MAP.put(KG, kgMap);
		
		Map<MeasureUnit, UOM> gramMap = new HashMap<MeasureUnit, UOM>();
		gramMap.put(KG, new UOM(GRAM, KG, new BigDecimal("0.001"), BigDecimal.ONE));
		gramMap.put(GRAM, new UOM(GRAM, GRAM, BigDecimal.ONE, BigDecimal.ONE));
		gramMap.put(LBS, new UOM(GRAM, LBS, BigDecimal.ONE, new BigDecimal("453.6")));
		gramMap.put(LOT, new UOM(GRAM, LOT, new BigDecimal("0.001"), new BigDecimal("15876")));
		gramMap.put(OZ, new UOM(GRAM, OZ, new BigDecimal("16"), new BigDecimal("453.6")));
		CONVERTION_MAP.put(GRAM, gramMap);
		
		Map<MeasureUnit, UOM> lbsMap = new HashMap<MeasureUnit, UOM>();
		lbsMap.put(KG, new UOM(LBS, KG, new BigDecimal("0.4536"), BigDecimal.ONE));
		lbsMap.put(GRAM, new UOM(LBS, GRAM, new BigDecimal("453.6"), BigDecimal.ONE));
		lbsMap.put(LBS, new UOM(LBS, LBS, BigDecimal.ONE, BigDecimal.ONE));
		lbsMap.put(LOT, new UOM(LBS, LOT, BigDecimal.ONE, new BigDecimal("35000")));
		lbsMap.put(OZ, new UOM(LBS, OZ, new BigDecimal("16"), BigDecimal.ONE));
		CONVERTION_MAP.put(LBS, lbsMap);
		
		Map<MeasureUnit, UOM> lotMap = new HashMap<MeasureUnit, UOM>();
		lotMap.put(KG, new UOM(LOT, KG, new BigDecimal("15876"), BigDecimal.ONE));
		lotMap.put(GRAM, new UOM(LOT, GRAM, new BigDecimal("15876000"), BigDecimal.ONE));
		lotMap.put(LBS, new UOM(LOT, LBS, new BigDecimal("35000"), BigDecimal.ONE));
		lotMap.put(LOT, new UOM(LOT, LOT, BigDecimal.ONE, BigDecimal.ONE));
		lotMap.put(OZ, new UOM(LOT, OZ, new BigDecimal("560000"), BigDecimal.ONE));
		CONVERTION_MAP.put(LOT, lotMap);
		
		Map<MeasureUnit, UOM> ozMap = new HashMap<MeasureUnit, UOM>();
		ozMap.put(KG, new UOM(OZ, KG, new BigDecimal("0.4536"), new BigDecimal("16")));
		ozMap.put(GRAM, new UOM(OZ, GRAM, new BigDecimal("453.6"), new BigDecimal("16")));
		ozMap.put(LBS, new UOM(OZ, LBS, BigDecimal.ONE, new BigDecimal("16")));
		ozMap.put(LOT, new UOM(OZ, LOT, BigDecimal.ONE, new BigDecimal("560000")));
		ozMap.put(OZ, new UOM(OZ, OZ, BigDecimal.ONE, BigDecimal.ONE));
		CONVERTION_MAP.put(OZ, ozMap);
		
		Map<MeasureUnit, UOM> unitMap = new HashMap<MeasureUnit, UOM>();
		unitMap.put(UNIT, new UOM(UNIT, UNIT, BigDecimal.ONE, BigDecimal.ONE));
		CONVERTION_MAP.put(UNIT, unitMap);

		Map<MeasureUnit, UOM> boxMap = new HashMap<MeasureUnit, UOM>();
		boxMap.put(BOX, new UOM(BOX, BOX, BigDecimal.ONE, BigDecimal.ONE));
		CONVERTION_MAP.put(BOX, boxMap);

	}
	
	public static BigDecimal convert(@NonNull BigDecimal amount, MeasureUnit fromUnit, MeasureUnit toUnit) {
		UOM convertUOM = CONVERTION_MAP.get(fromUnit).get(toUnit);
		if(convertUOM == null)
			return null;
		return amount
				.multiply(convertUOM.getMultiplicand())
				.divide(convertUOM.getDivisor(), MathContext.DECIMAL64);
	}
	
	public static BigDecimal convert(@NonNull AmountWithUnit amount, MeasureUnit toUnit) {
		return convert(amount.getAmount(), amount.getMeasureUnit(), toUnit);
	}
	
	public static List<UOM> getAllUOM() {
		return CONVERTION_MAP.values().stream()
				.flatMap(map -> map.values().stream())
				.collect(Collectors.toList());
	}

//	private static final BigDecimal LBS_IN_KG = new BigDecimal("0.4536");
//	private static final BigDecimal GRAM_IN_KG = new BigDecimal("0.001");
//	private static final BigDecimal LBS_IN_OZ = new BigDecimal("16");
	
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
		
//	private static BigDecimal convertToKG(BigDecimal amount, MeasureUnit fromUnit) {
//		switch(fromUnit) {
//		case OZ:
//			amount = amount.divide(LBS_IN_OZ);
//		case LBS:
//			return amount.multiply(LBS_IN_KG);
//		case KG:
//			return amount;
//		case GRAM:
//			return amount.multiply(GRAM_IN_KG);
//		default:
//			throw new IllegalArgumentException("Can't convert" + fromUnit + "to KG");
//		}
//	}
//
//	private static BigDecimal convertToLBS(BigDecimal amount, MeasureUnit fromUnit) {
//		switch(fromUnit) {
//		case GRAM:
//			amount = amount.multiply(GRAM_IN_KG);
//		case KG:
//			return amount.divide(LBS_IN_KG);
//		case LBS:
//			return amount;
//		case OZ:
//			return amount.divide(LBS_IN_OZ);
//		default:
//			throw new IllegalArgumentException("Can't convert" + fromUnit + "to LBS");
//		}
//	}

	
	
}
