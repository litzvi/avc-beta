/**
 * 
 */
package com.avc.mis.beta.dto.basic;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * Shows partial information of a po code, including product company.
 * Used for reference and display.
 * 
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
	
}
