/**
 * 
 */
package com.avc.mis.beta.entities.embeddable;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.avc.mis.beta.entities.enums.MeasureUnit;

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

	private BigDecimal amount = BigDecimal.ZERO;
	
	@Enumerated(EnumType.STRING)
//	@Column(nullable = false)
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
		return new AmountWithUnit(augendAmount, this.measureUnit);
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
		return String.format("%s %s", this.amount.setScale(SCALE, RoundingMode.HALF_DOWN), this.measureUnit);
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
