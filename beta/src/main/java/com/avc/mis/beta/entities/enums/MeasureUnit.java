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
	KG("1", "2.2046"),
	LBS("0.4536", "1.0"),
	OZ("0.02835", "0.0625");
	
	private BigDecimal kg;
	private BigDecimal lbs;
	
	MeasureUnit(String kg, String lbs) {
		this.kg = new BigDecimal(kg);
		this.lbs = new BigDecimal(lbs);
	}
	
	public BigDecimal kg() {
		return this.kg;
	}
	
	public BigDecimal lbs() {
		return this.lbs;
	}

	/**
	 * @param numberUnits
	 * @param from
	 * @param to
	 * @return
	 */
	public static BigDecimal convert(BigDecimal numberUnits, MeasureUnit from, MeasureUnit to) {
		switch(to) {
		case KG:
			return from.kg.multiply(numberUnits);
		case LBS:
			return from.lbs.multiply(numberUnits);			
		}
		return null;
	}
	
}
