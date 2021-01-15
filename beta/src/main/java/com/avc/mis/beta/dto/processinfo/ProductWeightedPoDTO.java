/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.dto.values.PoCodeDTO;
import com.avc.mis.beta.entities.processinfo.ProductWeightedPo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductWeightedPoDTO extends SubjectDataDTO {

	private PoCodeDTO poCode;
	private BigDecimal weight;
	
	public ProductWeightedPoDTO(Integer id, Integer version, Integer ordinal,
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, String supplierName,
			BigDecimal weight) {
		super(id, version, ordinal);
		if(poCodeId != null)
			this.poCode = new PoCodeDTO(poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName);
		this.weight = weight;
	}
	
	public ProductWeightedPoDTO(ProductWeightedPo weightedPo) {
		super(weightedPo.getId(), weightedPo.getVersion(), weightedPo.getOrdinal());
		this.poCode = new PoCodeDTO(weightedPo.getPoCode());
		this.weight = weightedPo.getWeight();
	}

}
