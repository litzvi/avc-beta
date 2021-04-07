/**
 * 
 */
package com.avc.mis.beta.dto.embedable;

import com.avc.mis.beta.dto.basic.PoCodeBasic;

import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
public class PoProcessInfo {

	PoCodeBasic poCode;

	public PoProcessInfo(
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, 
			Integer supplierId, Integer supplierVersion, String supplierName) {
		if(poCodeId != null) {
			this.poCode = new PoCodeBasic(poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName);
		}
		else {
			this.poCode = null;
		}
		
	}
}
