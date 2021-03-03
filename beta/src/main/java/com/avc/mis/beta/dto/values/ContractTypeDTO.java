/**
 * 
 */
package com.avc.mis.beta.dto.values;

import java.util.Currency;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.SupplyGroup;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.values.ContractType;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ContractTypeDTO extends ValueDTO {

	String value;
	String code;
	Currency currency; 
	String suffix;
	SupplyGroup supplyGroup;
	
	public ContractTypeDTO(@NonNull Integer id, String value, String code, 
			Currency currency, String suffix, SupplyGroup supplyGroup) {
		super(id);
		this.value = value;
		this.code = code;
		this.currency = currency;
		this.suffix = suffix;
		this.supplyGroup = supplyGroup;
	}
	
	public ContractTypeDTO(@NonNull ContractType contractType) {
		super(contractType.getId());
		this.value = contractType.getValue();
		this.code = contractType.getCode();
		this.currency = contractType.getCurrency();
		this.suffix = contractType.getSuffix();
		this.supplyGroup = contractType.getSupplyGroup();
	}

	public String getSuffix() {
		return suffix != null ? suffix : "";
	}
	
}
