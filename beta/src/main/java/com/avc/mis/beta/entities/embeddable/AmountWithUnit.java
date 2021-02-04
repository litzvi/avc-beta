/**
 * 
 */
package com.avc.mis.beta.entities.embeddable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.validation.groups.PositiveAmount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AmountWithUnit implements Cloneable {
		
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###.######");

	public static final AmountWithUnit ZERO_KG = new AmountWithUnit(BigDecimal.ZERO, MeasureUnit.KG);
	public static final AmountWithUnit ZERO_LOT = new AmountWithUnit(BigDecimal.ZERO, MeasureUnit.LOT);
	public static final AmountWithUnit ZERO_LBS = new AmountWithUnit(BigDecimal.ZERO, MeasureUnit.LBS);
	public static final AmountWithUnit ONE_UNIT = new AmountWithUnit(BigDecimal.ONE, MeasureUnit.UNIT);
	public static final AmountWithUnit NEUTRAL = new AmountWithUnit(BigDecimal.ONE, MeasureUnit.NONE);;


	@NotNull(message = "Amount is required")
	@Positive(message = "Amount has to be positive", groups = PositiveAmount.class)
	private BigDecimal amount = BigDecimal.ONE;
	
	@Enumerated(EnumType.STRING)
	@NotNull(message = "Measure unit required")
	private MeasureUnit measureUnit;

	public AmountWithUnit(BigDecimal amount, String measureUnit) {
		super();
		this.amount = amount;
		this.measureUnit = MeasureUnit.valueOf(measureUnit);
	}
	
	public AmountWithUnit(BigInteger amount, MeasureUnit measureUnit) {
		super();
		this.amount = new BigDecimal(amount);
		this.measureUnit = measureUnit;
	}
	
	public AmountWithUnit(String amount, String measureUnit) {
		super();
		this.amount = (new BigDecimal(amount));
		this.measureUnit = MeasureUnit.valueOf(measureUnit);
	}
	
	public AmountWithUnit(MeasureUnit measureUnit) {
		super();
		this.measureUnit = measureUnit;
	}
	
	public AmountWithUnit add(AmountWithUnit augend) {
		return add(augend.getAmount(), augend.getMeasureUnit());
//		BigDecimal augendAmount = MeasureUnit.convert(augend.getAmount(), augend.getMeasureUnit(), this.measureUnit);
//		if(augendAmount == null)
//			throw new UnsupportedOperationException(
//					"Convertion from " + augend.getMeasureUnit() + " to " + this.measureUnit + " not supported");
//		return new AmountWithUnit(this.amount.add(augendAmount), this.measureUnit);
	}
	
	public AmountWithUnit add(BigDecimal augendAmount, MeasureUnit augendMeasureUnit) {
		BigDecimal augendConvertedAmount = MeasureUnit.convert(augendAmount, augendMeasureUnit, this.measureUnit);
		if(augendConvertedAmount == null)
			throw new UnsupportedOperationException(
					"Convertion from " + augendMeasureUnit + " to " + this.measureUnit + " not supported");
		return new AmountWithUnit(this.amount.add(augendConvertedAmount), this.measureUnit);
	}
	
	private AmountWithUnit negate() {
		return new AmountWithUnit(this.amount.negate(), measureUnit);
	}
	
	public AmountWithUnit subtract(AmountWithUnit subtrahend) {
		BigDecimal subtrahendAmount = MeasureUnit.convert(subtrahend.getAmount(), subtrahend.getMeasureUnit(), this.measureUnit);
		if(subtrahendAmount == null)
			throw new UnsupportedOperationException(
					"Convertion from " + subtrahend.getMeasureUnit() + " to " + this.measureUnit + " not supported");
		return new AmountWithUnit(this.amount.subtract(subtrahendAmount), this.measureUnit);
	}
	
	public AmountWithUnit subtract(BigDecimal subtrahend) {
		return new AmountWithUnit(this.amount.subtract(subtrahend), this.measureUnit);
	}
	
	public AmountWithUnit convert(MeasureUnit measureUnit) {
		BigDecimal convertedAmount = MeasureUnit.convert(this.amount, this.measureUnit, measureUnit);
		if(convertedAmount == null)
			throw new UnsupportedOperationException(
					"Convertion from " + this.measureUnit + " to " + measureUnit + " not supported");
		return new AmountWithUnit(convertedAmount, measureUnit);
	}
	
	public AmountWithUnit multiply(BigDecimal multiplicand) {
		return new AmountWithUnit(this.amount.multiply(multiplicand), this.measureUnit);
	}
			
	@Override
	public AmountWithUnit clone() {
		return new AmountWithUnit(this.amount, this.measureUnit);
	}
	
//	public void setMeasureUnit(String measureUnit) {
//		this.measureUnit = MeasureUnit.valueOf(measureUnit);
//	}
//	
//	public void setMeasureUnit(MeasureUnit measureUnit) {
//		this.measureUnit = measureUnit;
//	}
	
	public AmountWithUnit setScale(int newScale) {
		this.amount = amount.setScale(newScale, RoundingMode.HALF_DOWN);
		return this;
	}
	
	public String getValue() {
		if(!isFilled()) {
			return null;
		}
		return String.format("%s %s", 
				DECIMAL_FORMAT.format(this.amount), 
				this.measureUnit);
	}
	
	public boolean isFilled() {
		return this.amount != null && this.measureUnit != null;
	}
	
	public int signum() {
		if(amount == null) {
			throw new NullPointerException();
		}
		return amount.signum();
	}

	/**
	 * @param amountsWithUnit
	 * @param scale
	 */
	public static void setScales(AmountWithUnit[] amountsWithUnit, int newScale) {
		for(int i=0; i< amountsWithUnit.length; i++) {
			amountsWithUnit[i].setScale(newScale);
		}
		
	}

	public static AmountWithUnit addNullable(AmountWithUnit base, AmountWithUnit augend) {
		if(base != null && augend != null) {
			return base.add(augend);
		}
		if(base != null) {
			return base;
		}
		if(augend != null) {
			return augend;
		}
		return null;
	}
	
	public static AmountWithUnit subtractNullable(AmountWithUnit base, AmountWithUnit subtrahend) {
		if(base != null && subtrahend != null) {
			return base.subtract(subtrahend);
		}
		if(base != null) {
			return base;
		}
		if(subtrahend != null) {
			return subtrahend.negate();
		}
		return null;
	}

	public static BigDecimal divide(AmountWithUnit numerator, AmountWithUnit denominator) {
		if(numerator == null || denominator == null || denominator.amount.equals(BigDecimal.ZERO)) {
			return null;
		}
		BigDecimal denominatorAmount = MeasureUnit.convert(denominator, numerator.getMeasureUnit());
		if(denominatorAmount == null)
			throw new UnsupportedOperationException(
					"Convertion from " + denominator.getMeasureUnit() + " to " + numerator.getMeasureUnit() + " not supported");
		return numerator.getAmount().divide(denominatorAmount, MathContext.DECIMAL64);
	}

	public static BigDecimal percentageLoss(AmountWithUnit out, AmountWithUnit in) {
		BigDecimal ratio = AmountWithUnit.divide(out, in);
		if(ratio == null)
			return null;
		return ratio
				.setScale(MeasureUnit.DIVISION_SCALE, RoundingMode.HALF_DOWN)
				.subtract(BigDecimal.ONE)
				.multiply(new BigDecimal("100"));
	}
	

	


}
