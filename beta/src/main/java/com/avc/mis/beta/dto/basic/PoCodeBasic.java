/**
 * 
 */
package com.avc.mis.beta.dto.basic;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.entities.process.PoCode;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * PoCode fields excluding supplier
 * 
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class PoCodeBasic extends BasicDTO {

	String code;
	String contractTypeCode;
	String contractTypeSuffix;
//	Currency currency;


	public PoCodeBasic(@NonNull Integer id, String code, String contractTypeCode, String contractTypeSuffix) {
		super(id);
		this.code = code;
		this.contractTypeCode = contractTypeCode;
//		this.currency = currency;
		this.contractTypeSuffix = contractTypeSuffix != null ? contractTypeSuffix : "";
	}
	
	
	public PoCodeBasic(@NonNull PoCode poCode) {
		super(poCode.getId());
		this.code = poCode.getCode();
		this.contractTypeCode = poCode.getContractType().getCode();
//		this.currency = poCode.getContractType().getCurrency();
		this.contractTypeSuffix = poCode.getContractType().getSuffix();
	}
	
	/**
	 * @return a string representing full PO code. e.g. VAT-900001
	 */
	public String getValue() {
		return String.format("%s-%s-%s", this.contractTypeCode, this.getCode(), this.contractTypeSuffix);
	}

}
