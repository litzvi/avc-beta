/**
 * 
 */
package com.avc.mis.beta.dto.process;

import com.avc.mis.beta.dto.BaseDTO;
import com.avc.mis.beta.entities.enums.ContractTypeCode;
import com.avc.mis.beta.entities.process.PoCode;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class PoCodeDTO extends BaseDTO {

	ContractTypeCode contractTypeCode;
	String supplierName;
	
	public PoCodeDTO(Integer id, ContractTypeCode contractTypeCode, 
			String supplierName) {
		super(id);
		this.contractTypeCode = contractTypeCode;
		this.supplierName = supplierName;
	}
	
	
	public PoCodeDTO(PoCode poCode) {
		super(poCode.getCode());
		this.contractTypeCode = poCode.getContractType() != null ? poCode.getContractType().getCode(): null;
		this.supplierName = poCode.getSupplier() != null ? poCode.getSupplier().getName(): null;
	}
	
	public Integer getCode() {
		return getId();
	}
	
	/**
	 * @return a string representing full PO code. e.g. VAT-900001
	 */
	public String getValue() {
		return String.format("%s-%d", this.contractTypeCode, this.getId());
	}
		
}
