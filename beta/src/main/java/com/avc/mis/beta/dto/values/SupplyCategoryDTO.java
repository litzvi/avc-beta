/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.enums.SupplyGroup;
import com.avc.mis.beta.entities.values.SupplyCategory;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for supply category.
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SupplyCategoryDTO extends ValueDTO {
	
	private String value;
	private SupplyGroup supplyGroup;

	public SupplyCategoryDTO(Integer id, String value, SupplyGroup supplyGroup) {
		super(id);
		this.value = value;
		this.supplyGroup = supplyGroup;
	}
	
	public SupplyCategoryDTO(SupplyCategory supplyCategory) {
		super(supplyCategory.getId());
		this.value = supplyCategory.getValue();
		this.supplyGroup = supplyCategory.getSupplyGroup();
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return SupplyCategory.class;
	}
	
	@Override
	public SupplyCategory fillEntity(Object entity) {
		SupplyCategory scEntity;
		if(entity instanceof SupplyCategory) {
			scEntity = (SupplyCategory) entity;
		}
		else {
			throw new IllegalStateException("Param has to be SupplyCategory class");
		}
		super.fillEntity(scEntity);
		scEntity.setValue(getValue());
		scEntity.setSupplyGroup(getSupplyGroup());
		
		return scEntity;
	}

}
