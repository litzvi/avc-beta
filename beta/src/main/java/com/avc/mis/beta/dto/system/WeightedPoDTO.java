/**
 * 
 */
package com.avc.mis.beta.dto.system;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.system.WeightedPo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO for recording the composition of purchase orders a process's items originate from.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WeightedPoDTO extends SubjectDataDTO {

	private PoCodeBasic poCode;
	private BigDecimal weight;
	
	public WeightedPoDTO(Integer id, Integer version, Integer ordinal,
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, String supplierName, 
			BigDecimal weight) {
		super(id, version, ordinal);
		if(poCodeId != null)
			this.poCode = new PoCodeBasic(poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName);
		this.weight = weight;
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return WeightedPo.class;
	}

}
