/**
 * 
 */
package com.avc.mis.beta.dto.basic;

import com.avc.mis.beta.dto.BasicValueDTO;
import com.avc.mis.beta.entities.codes.BasePoCode;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO for PoCode containing id and the fields needed 
 * for presenting the po code/id with it's initial and suffix.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class PoCodeBasic extends BasicValueDTO {

	private String code;
	private String contractTypeCode;
	private String contractTypeSuffix;
	private String supplierName;	
	
	public PoCodeBasic(Integer id, String code,
			String contractTypeCode, String contractTypeSuffix, String supplierName
			) {
		super(id);
		this.code = code;
		this.contractTypeCode = contractTypeCode;
		this.supplierName = supplierName;
		this.contractTypeSuffix = contractTypeSuffix != null ? contractTypeSuffix : "";
	}	
	
	public PoCodeBasic(BasePoCode poCode) {
		super(poCode.getId());
		this.code = poCode.getCode();
		this.contractTypeCode = poCode.getContractType() != null ? poCode.getContractType().getCode(): null;
		this.supplierName = poCode.getSupplier() != null ? poCode.getSupplier().getName(): null;
//		this.currency = poCode.getContractType() != null ? poCode.getContractType().getCurrency(): null;
		this.contractTypeSuffix = poCode.getContractType() != null ? poCode.getContractType().getSuffix(): "";
//		this.display = poCode.getDisplay();
	}
	
	/**
	 * @return a string representing full PO code. e.g. VAT-900001, PO-900001V
	 */
	@Override
	public String getValue() {	
		return String.format("%s-%s%s", this.contractTypeCode, this.getCode(), this.contractTypeSuffix);
	}
	
	@Override
	public BasePoCode fillEntity(Object entity) {
		BasePoCode basePoCode;
		if(entity instanceof BasePoCode) {
			basePoCode = (BasePoCode) entity;
		}
		else {
			throw new IllegalStateException("Param has to be BasePoCode class");
		}
		super.fillEntity(basePoCode);
		
		return basePoCode;
	}
		
}
