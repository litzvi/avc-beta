/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.avc.mis.beta.dto.basic.WeightedObject;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.values.ContractType;

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
public class ProductWeightedPo extends ProcessInfoEntity {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "poCodeId", nullable = false)
	private PoCode poCode;
	
	@Column(nullable = false)
	private BigDecimal weight = BigDecimal.ONE;

}
