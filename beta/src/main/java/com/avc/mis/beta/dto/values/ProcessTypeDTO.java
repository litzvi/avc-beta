/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.values.ProcessType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * DTO for contract type.
 * 
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ProcessTypeDTO extends ValueDTO {

	private String value;
	private ProcessName processName;
	
	public ProcessTypeDTO(@NonNull Integer id, String value, ProcessName processName) {
		super(id);
		this.value = value;
		this.processName = processName;
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return ProcessType.class;
	}
	
	@Override
	public ProcessType fillEntity(Object entity) {
		ProcessType typeEntity;
		if(entity instanceof ProcessType) {
			typeEntity = (ProcessType) entity;
		}
		else {
			throw new IllegalStateException("Param has to be ProcessType class");
		}
		super.fillEntity(typeEntity);
		typeEntity.setValue(getValue());
		typeEntity.setProcessName(getProcessName());
		return typeEntity;
	}

	
}
