/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.avc.mis.beta.entities.codes.BasePoCode;
import com.avc.mis.beta.entities.codes.PoCode;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "PRUDUCT_WEIGHTED_POS")
public class WeightedPo extends ProcessInfoEntity {
		
//	public WeightedPo(@NotNull PoCode poCode, @NotNull BigDecimal weight) {
//		super();
//		this.poCode = poCode;
//		this.weight = weight;
//	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "poCodeId", nullable = false)
	@NotNull(message = "Po code is mandatory")
	private BasePoCode poCode;
	
//	@Column(nullable = false, precision = 19, scale = MeasureUnit.SCALE)
//	@NotNull(message = "weight for every po is mandatory") set by code when needed
	@Column(precision = 19, scale = MeasureUnit.SCALE)
	private BigDecimal weight;

}
