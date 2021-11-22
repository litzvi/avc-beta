/**
 * 
 */
package com.avc.mis.beta.dto.process.info;

import com.avc.mis.beta.dto.basic.PoCodeBasic;

import lombok.Value;

/**
 * For fetching PoProcessDTO fields.
 * 
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
