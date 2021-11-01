/**
 * 
 */
package com.avc.mis.beta.dto.process.collection;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.collection.ApprovalTask;
import com.avc.mis.beta.entities.process.collection.WeightedPo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
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
	
	public WeightedPoDTO(WeightedPo weightedPo) {
		super(weightedPo.getId(), weightedPo.getVersion(), weightedPo.getOrdinal());
		this.poCode = new PoCodeBasic(weightedPo.getPoCode());
		this.weight = weightedPo.getWeight().setScale(MeasureUnit.SCALE);
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return WeightedPo.class;
	}

}
