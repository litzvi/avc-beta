/**
 * 
 */
package com.avc.mis.beta.entities.system;

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
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Records percentage of process's produced items originate from purchase order (PO).
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "PRUDUCT_WEIGHTED_POS")
public class WeightedPo extends ProcessInfoEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "poCodeId", nullable = false)
	@NotNull(message = "Po code is mandatory")
	private BasePoCode poCode;
	
	@Column(precision = 19, scale = MeasureUnit.DIVISION_SCALE)
	private BigDecimal weight;

}
