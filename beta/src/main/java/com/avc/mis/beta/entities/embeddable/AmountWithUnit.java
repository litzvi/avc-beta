/**
 * 
 */
package com.avc.mis.beta.entities.embeddable;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.validation.groups.PositiveAmount;
import com.avc.mis.beta.validation.groups.UserInputGroup;

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
	
	public static final int SCALE = 3;
	
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###.###");

	@NotNull(message = "Amount is required")
	@Positive(message = "Amount has to be positive", groups = PositiveAmount.class)
	private BigDecimal amount;
//	private BigDecimal amount = BigDecimal.ZERO;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull(message = "Measure unit required")
	private MeasureUnit measureUnit;
	
	public AmountWithUnit(BigDecimal amount) {
		super();
		this.amount = amount;
	}

	public AmountWithUnit(BigDecimal amount, String measureUnit) {
		super();
		this.amount = amount;
		this.measureUnit = MeasureUnit.valueOf(measureUnit);
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
	
	public AmountWithUnit multiply(BigDecimal multiplicand) {
		return new AmountWithUnit(this.amount.multiply(multiplicand), this.measureUnit);
	}
		
	@Override
	public AmountWithUnit clone() {
		return new AmountWithUnit(amount, measureUnit);
	}
	
	public void setMeasureUnit(String measureUnit) {
//		if(measureUnit != null)
		this.measureUnit = MeasureUnit.valueOf(measureUnit);
	}
	
	public void setMeasureUnit(MeasureUnit measureUnit) {
		this.measureUnit = measureUnit;
	}
	
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
