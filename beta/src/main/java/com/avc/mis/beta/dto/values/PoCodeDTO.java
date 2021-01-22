/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.entities.codes.BasePoCode;
import com.avc.mis.beta.entities.codes.PoCode;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * DTO for PoCode containing id and the fields needed 
 * for presenting the po code/id with it's initial and suffix.
 * 
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class PoCodeDTO extends ValueDTO {

	String code;
	String contractTypeCode;
//	Currency currency;
	String contractTypeSuffix;
	String supplierName;	
	
	/**
	 * @param id
	 * @param contractTypeCode
	 * @param contractTypeSuffix
	 * @param supplierName
	 */
	public PoCodeDTO(Integer id, String code,
			String contractTypeCode, String contractTypeSuffix, String supplierName) {
		super(id);
		this.code = code;
		this.contractTypeCode = contractTypeCode;
		this.supplierName = supplierName;
//		this.currency = currency;
		this.contractTypeSuffix = contractTypeSuffix != null ? contractTypeSuffix : "";
	}	
	
	/**
	 * @param poCode
	 */
	public PoCodeDTO(BasePoCode poCode) {
		super(poCode.getId());
		this.code = poCode.getCode();
		this.contractTypeCode = poCode.getContractType() != null ? poCode.getContractType().getCode(): null;
		this.supplierName = poCode.getSupplier() != null ? poCode.getSupplier().getName(): null;
//		this.currency = poCode.getContractType() != null ? poCode.getContractType().getCurrency(): null;
		this.contractTypeSuffix = poCode.getContractType() != null ? poCode.getContractType().getSuffix(): "";
	}
	
//	/**
//	 * Used as a synonymous for getting id
//	 * @return the code/id
//	 */
//	public Integer getCode() {
//		return getId();
//	}
	
	/**
	 * @return a string representing full PO code. e.g. VAT-900001, PO-900001V
	 */
	public String getValue() {		
		return String.format("%s-%s%s", this.contractTypeCode, this.getCode(), this.contractTypeSuffix);
	}
	
	@JsonIgnore
	public PoCodeBasic getPoCodeBasic() {
		return new PoCodeBasic(this.getId(), this.getCode(), this.getContractTypeCode(), this.contractTypeSuffix);
	}
		
}
