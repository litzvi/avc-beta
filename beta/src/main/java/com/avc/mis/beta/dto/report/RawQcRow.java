/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.OffsetDateTime;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.values.PoCodeBasic;

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
public class RawQcRow extends ValueDTO {

	PoCodeBasic poCode;
	String supplierName;
	String itemName;
	OffsetDateTime checkDate;
	BigInteger numberOfSamples;
	BigDecimal totalDefects;
	BigDecimal totalDamage;


	public RawQcRow(@NonNull Integer id, 
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName, 
			String itemName, OffsetDateTime checkDate, 
			BigInteger numberOfSamples, BigDecimal totalDefects, BigDecimal totalDamage) {
		super(id);
		this.poCode = new PoCodeBasic(poCodeId, contractTypeCode, contractTypeSuffix);
		this.supplierName = supplierName;
		this.itemName = itemName;
		this.checkDate = checkDate;
		this.numberOfSamples = numberOfSamples;
		this.totalDefects = totalDefects;
		this.totalDamage = totalDamage;
	}
	
	

}
