/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.BaseDTO;
import com.avc.mis.beta.entities.enums.ContractTypeCode;
import com.avc.mis.beta.entities.process.PoCode;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * PoCode fields excluding supplier
 * 
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class PoCodeBasic extends BaseDTO {

	ContractTypeCode contractTypeCode;

	public PoCodeBasic(Integer id, ContractTypeCode contractTypeCode) {
		super(id);
		this.contractTypeCode = contractTypeCode;
	}
	
	
	public PoCodeBasic(PoCode poCode) {
		super(poCode.getCode());
		this.contractTypeCode = poCode.getContractType().getCode();
	}
	
	/**
	 * @return a string representing full PO code. e.g. VAT-900001
	 */
	public String getValue() {
		return String.format("%s-%d", this.contractTypeCode, this.getId());
	}

}
