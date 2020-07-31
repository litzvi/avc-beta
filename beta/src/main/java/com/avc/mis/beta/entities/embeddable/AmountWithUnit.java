/**
 * 
 */
package com.avc.mis.beta.entities.embeddable;

import java.math.BigDecimal;
import java.math.BigInteger;
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

	public static final AmountWithUnit ZERO = new AmountWithUnit(BigDecimal.ZERO, MeasureUnit.KG);

	@NotNull(message = "Amount is required")
	@Positive(message = "Amount has to be positive", groups = PositiveAmount.class)
	private BigDecimal amount;
	
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
	
	public AmountWithUnit add(AmountWithUnit augend) {
		BigDecimal augendAmount = MeasureUnit.convert(augend.getAmount(), augend.getMeasureUnit(), this.measureUnit);
		if(augendAmount == null)
			throw new UnsupportedOperationException(
					"Convertion from " + augend.getMeasureUnit() + " to " + this.measureUnit + " not supported");
		return new AmountWithUnit(this.amount.add(augendAmount), this.measureUnit);
	}
	
	public AmountWithUnit substract(AmountWithUnit subtrahend) {
		BigDecimal subtrahendAmount = MeasureUnit.convert(subtrahend.getAmount(), subtrahend.getMeasureUnit(), this.measureUnit);
		if(subtrahendAmount == null)
			throw new UnsupportedOperationException(
					"Convertion from " + subtrahend.getMeasureUnit() + " to " + this.measureUnit + " not supported");
		return new AmountWithUnit(this.amount.subtract(subtrahendAmount), this.measureUnit);
	}
	
	public AmountWithUnit multiply(BigDecimal multiplicand) {
		return new AmountWithUnit(this.amount.multiply(multiplicand), this.measureUnit);
	}
			
	@Override
	public AmountWithUnit clone() {
		return new AmountWithUnit(amount, measureUnit);
	}
	
//	public void setMeasureUnit(String measureUnit) {
//		this.measureUnit = MeasureUnit.valueOf(measureUnit);
//	}
//	
//	public void setMeasureUnit(MeasureUnit measureUnit) {
//		this.measureUnit = measureUnit;
//	}
	
	public AmountWithUnit setScale(int newScale) {
		this.amount = amount.setScale(newScale);
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

	

	

	


}
