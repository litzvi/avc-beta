/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.BaseDTO;
import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.values.ContractType;

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

	ContractType contractType;

	public PoCodeBasic(Integer id, ContractType contractType) {
		super(id);
		this.contractType = contractType;
	}
	
	
	public PoCodeBasic(PoCode poCode) {
		super(poCode.getCode());
		this.contractType = poCode.getContractType();
	}
	
	/**
	 * @return a string representing full PO code. e.g. VAT-900001
	 */
	public String getValue() {
		return String.format("%s-%d", this.contractType.getValue(), this.getId());
	}

}
