/**
 * 
 */
package com.avc.mis.beta.entities.values;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.avc.mis.beta.entities.LinkEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "UOM", 
	uniqueConstraints = { @UniqueConstraint(columnNames = { "fromUnit", "toUnit" }) })
public class UOM extends LinkEntity {
	
	@Enumerated(EnumType.STRING)
	@Column(updatable = false, nullable = false)
	private MeasureUnit fromUnit;

	@Enumerated(EnumType.STRING)
	@Column(updatable = false, nullable = false)
	private MeasureUnit toUnit;
	
	@Column(nullable = false, updatable = false, precision = 19, scale = 4)
	private BigDecimal multiply = BigDecimal.ONE;
	
	@Column(nullable = false, updatable = false, precision = 19, scale = 4)
	private BigDecimal divide = BigDecimal.ONE;

	@Override
	public boolean isLegal() {
		return fromUnit != null && toUnit != null && multiply != null && divide != null;
	}

	@Override
	public String getIllegalMessage() {
		return "All fields have to have values and can't be null";
	}

}
