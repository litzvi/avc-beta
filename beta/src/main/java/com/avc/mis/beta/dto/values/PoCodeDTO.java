/**
 * 
 */
package com.avc.mis.beta.dto.values;

import java.util.Currency;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.data.DataObjectWithName;
import com.avc.mis.beta.entities.codes.BasePoCode;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.enums.SupplyGroup;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PoCodeDTO extends ValueDTO {

	private String code;
	private DataObjectWithName<Supplier> supplier; 
	private ContractTypeDTO contractType;
	private DataObjectWithName<Supplier> productCompany; 

	
	public PoCodeDTO(@NonNull Integer id, String code, 
			Integer supplierId, Integer supplierVersion, String supplierName,
			Integer contractTypeId, String contractTypeValue, String contractTypeCode, 
			Currency contractTypeCurrency, String contractTypeSuffix, SupplyGroup supplyGroup, 
			Integer productCompanyId, Integer productCompanyVersion, String productCompanyName) {
		super(id);
		this.code = code;
		this.supplier = new DataObjectWithName<Supplier>(supplierId, supplierVersion, supplierName);
		this.contractType = new ContractTypeDTO(contractTypeId, contractTypeValue, contractTypeCode, 
				contractTypeCurrency, contractTypeSuffix, supplyGroup);
		if(productCompanyId != null)
			this.productCompany = new DataObjectWithName<Supplier>(productCompanyId, productCompanyVersion, productCompanyName);
	}
	
	public PoCodeDTO(@NonNull BasePoCode poCode) {
		super(poCode.getId());
		this.code = poCode.getCode();
		this.supplier = new DataObjectWithName<Supplier>(poCode.getSupplier());
		this.contractType = new ContractTypeDTO(poCode.getContractType());
		if(poCode.getProductCompany() != null)
			this.productCompany = new DataObjectWithName<Supplier>(poCode.getProductCompany());
	}
	
	/**
	 * @return a string representing full PO code. e.g. VAT-900001, PO-900001V
	 */
	public String getValue() {
//		if(this.display != null) {
//			return this.display;
//		}
		return String.format("%s-%s%s", this.contractType.getCode(), this.code, this.contractType.getSuffix());
	}
}
