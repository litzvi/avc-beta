/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.values.CompanyPosition;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for bank
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CompanyPositionDTO extends ValueDTO {
	
	private String value;
	
	public CompanyPositionDTO(Integer id, String value) {
		super(id);
		this.value = value;
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return CompanyPosition.class;
	}
	
	@Override
	public CompanyPosition fillEntity(Object entity) {
		CompanyPosition cpEntity;
		if(entity instanceof CompanyPosition) {
			cpEntity = (CompanyPosition) entity;
		}
		else {
			throw new IllegalStateException("Param has to be CompanyPosition class");
		}
		super.fillEntity(cpEntity);
		cpEntity.setValue(getValue());
		return cpEntity;
	}

}
