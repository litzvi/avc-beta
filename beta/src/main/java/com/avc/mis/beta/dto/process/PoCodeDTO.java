/**
 * 
 */
package com.avc.mis.beta.dto.process;

import com.avc.mis.beta.dto.BaseDTO;
import com.avc.mis.beta.dto.values.SupplierBasic;
import com.avc.mis.beta.entities.enums.ContractTypeCode;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.values.ContractType;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
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
		this.contractTypeCode = poCode.getContractType().getCode();
		this.supplierName = poCode.getSupplier().getName();
	}
	
	/**
	 * @return a string representing full PO code. e.g. VAT-900001
	 */
	public String getValue() {
		return String.format("%s-%d", this.contractTypeCode, this.getId());
	}
		
}
