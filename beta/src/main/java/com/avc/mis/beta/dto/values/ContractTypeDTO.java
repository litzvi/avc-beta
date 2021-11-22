/**
 * 
 */
package com.avc.mis.beta.dto.values;

import java.util.Currency;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.enums.SupplyGroup;
import com.avc.mis.beta.entities.values.ContractType;

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
public class ContractTypeDTO extends ValueDTO {

	private String value;
	private String code;
	private Currency currency; 
	private String suffix;
	private SupplyGroup supplyGroup;
	
	public ContractTypeDTO(@NonNull Integer id, String value, String code, 
			Currency currency, String suffix, SupplyGroup supplyGroup) {
		super(id);
		this.value = value;
		this.code = code;
		this.currency = currency;
		this.suffix = suffix;
		this.supplyGroup = supplyGroup;
	}

	public String getSuffix() {
		return suffix != null ? suffix : "";
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return ContractType.class;
	}
	
	@Override
	public ContractType fillEntity(Object entity) {
		ContractType typeEntity;
		if(entity instanceof ContractType) {
			typeEntity = (ContractType) entity;
		}
		else {
			throw new IllegalStateException("Param has to be ContractType class");
		}
		super.fillEntity(typeEntity);
		typeEntity.setValue(getValue());
		typeEntity.setCode(getCode());
		typeEntity.setCurrency(getCurrency());
		typeEntity.setSuffix(getSuffix());
		typeEntity.setSupplyGroup(getSupplyGroup());
		return typeEntity;
	}

	
}
