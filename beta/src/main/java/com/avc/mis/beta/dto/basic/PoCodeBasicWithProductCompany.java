/**
 * 
 */
package com.avc.mis.beta.dto.basic;

import com.avc.mis.beta.entities.codes.BasePoCode;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class PoCodeBasicWithProductCompany extends PoCodeBasic {

	String productCompanyName;

	public PoCodeBasicWithProductCompany(Integer id, String code, String contractTypeCode, String contractTypeSuffix,
			String supplierName, String productCompanyName) {
		super(id, code, contractTypeCode, contractTypeSuffix, supplierName);
		this.productCompanyName = productCompanyName;
	}
	
	public PoCodeBasicWithProductCompany(BasePoCode poCode) {
		super(poCode);
		this.productCompanyName = poCode.getProductCompany() != null ? poCode.getProductCompany().getName(): null;
	}
}
