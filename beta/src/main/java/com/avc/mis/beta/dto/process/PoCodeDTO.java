/**
 * 
 */
package com.avc.mis.beta.dto.process;

import com.avc.mis.beta.dto.BaseDTO;
import com.avc.mis.beta.dto.values.SupplierBasic;
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

	ContractType contractType;
	String supplierName;
//	SupplierBasic supplier;
	
	public PoCodeDTO(Integer id, ContractType contractType, 
			/*Integer supplierId, Integer supplierVersion, */
			String supplierName) {
		super(id);
		this.contractType = contractType;
//		this.supplier = new SupplierBasic(supplierId, supplierVersion, supplierName);
		this.supplierName = supplierName;
	}
	
	
	public PoCodeDTO(PoCode poCode) {
		super(poCode.getCode());
		this.contractType = poCode.getContractType();
//		this.supplier = new SupplierBasic(poCode.getSupplier());
		this.supplierName = poCode.getSupplier().getName();
	}
	
	/**
	 * @return a string representing full PO code. e.g. VAT-900001
	 */
	public String getValue() {
		return String.format("%s-%d", this.contractType.getValue(), this.getId());
	}
		
}
