/**
 * 
 */
package com.avc.mis.beta.entities.embeddable;

import java.math.BigDecimal;

import javax.persistence.Column;
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

//	@Column(precision = 19, scale = 3)
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
		return String.format("%s%s", this.amount, this.measureUnit);
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
