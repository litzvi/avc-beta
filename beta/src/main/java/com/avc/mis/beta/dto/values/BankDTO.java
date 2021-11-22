/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.values.Bank;

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
public class BankDTO extends ValueDTO {
	
	private String value;
	
	public BankDTO(Integer id, String value) {
		super(id);
		this.value = value;
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return Bank.class;
	}
	
	@Override
	public Bank fillEntity(Object entity) {
		Bank bankEntity;
		if(entity instanceof Bank) {
			bankEntity = (Bank) entity;
		}
		else {
			throw new IllegalStateException("Param has to be Bank class");
		}
		super.fillEntity(bankEntity);
		bankEntity.setValue(getValue());
		return bankEntity;
	}

}
