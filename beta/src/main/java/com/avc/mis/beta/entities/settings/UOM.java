/**
 * 
 */
package com.avc.mis.beta.entities.settings;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.LinkEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.validation.groups.OnPersist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Formula for converting between units of measure.
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "UOM", 
	uniqueConstraints = { @UniqueConstraint(columnNames = { "fromUnit", "toUnit" }) })
public class UOM extends LinkEntity {
	
	@Enumerated(EnumType.STRING)
	@Column(updatable = false, nullable = false)
	@NotNull(message = "From unit is mandatory", groups = OnPersist.class)
	private MeasureUnit fromUnit;

	@Enumerated(EnumType.STRING)
	@Column(updatable = false, nullable = false)
	@NotNull(message = "To unit is mandatory", groups = OnPersist.class)
	private MeasureUnit toUnit;
	
	@Column(nullable = false, precision = 19, scale = 4)
	@NotNull(message = "Multiplicand value is mandatory")
	private BigDecimal multiplicand;
	
	@Column(nullable = false, precision = 19, scale = 4)
	@NotNull(message = "Divisor value is mandatory")
	private BigDecimal divisor;

}
