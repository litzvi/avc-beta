/**
 * 
 */
package com.avc.mis.beta.dto.values;

import java.math.BigDecimal;
import java.util.Set;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.process.StorageDTO;
import com.avc.mis.beta.entities.enums.ContractTypeCode;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PoInventoryRow extends ValueDTO {
	
	PoCodeBasic poCode;
	String supplierName;
	String itemName;
	BigDecimal amount;
	MeasureUnit measureUnit;
	private Set<StorageDTO> storageForms;
	
	
}
