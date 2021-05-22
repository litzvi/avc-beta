/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.RawDamage;
import com.avc.mis.beta.entities.embeddable.RawDefects;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.QcCompany;
import com.avc.mis.beta.entities.item.Item;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class CashewQcRow extends BasicDTO {

	PoCodeBasic poCode;
	//already in poCode
	String supplierName;
	String checkedBy;
//	Integer itemId;
	BasicValueEntity<Item> item;
//	String itemName;
	LocalDateTime checkDate;
	ProcessStatus status;
	BigInteger numberOfSamples;
	Boolean precentage;
	BigDecimal sampleWeight;
	BigDecimal humidity;
	BigDecimal breakage;
	BigDecimal totalDefects;
	BigDecimal totalDamage;


	public CashewQcRow(@NonNull Integer id, 
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, String supplierName, 
			QcCompany checkedBy,
			Integer itemId, String itemValue, 
//			String itemName, 
			LocalDateTime checkDate, ProcessStatus status,
			BigInteger numberOfSamples, BigDecimal sampleWeight, boolean precentage,
			BigDecimal humidity, BigDecimal breakage,
			BigDecimal scorched, BigDecimal deepCut, BigDecimal offColour, 
			BigDecimal shrivel, BigDecimal desert, BigDecimal deepSpot, 
			BigDecimal mold, BigDecimal dirty, BigDecimal lightDirty, 
			BigDecimal decay, BigDecimal insectDamage, BigDecimal testa) {
		super(id);
		this.poCode = new PoCodeBasic(poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName);
		this.supplierName = supplierName;
		this.checkedBy = checkedBy.toString();
		this.item = new BasicValueEntity<Item>(itemId, itemValue);
//		this.itemName = itemName;
		this.checkDate = checkDate;
		this.status = status;
		this.numberOfSamples = numberOfSamples;
		this.sampleWeight = sampleWeight;
		this.humidity = humidity;
		
		RawDefects rawDefects = new RawDefects(scorched, deepCut, offColour, shrivel, desert, deepSpot);		
		RawDamage rawDamage = new RawDamage(mold, dirty, lightDirty, decay, insectDamage, testa);
		
		this.precentage = precentage;
		BigDecimal divisor;
		if(precentage) {
			divisor = BigDecimal.valueOf(100L);
		}
		else{		
			divisor = sampleWeight;
		}
		if(breakage != null) {
			this.breakage = breakage.divide(divisor, MeasureUnit.SCALE, RoundingMode.HALF_DOWN);
		}
		else {
			this.breakage = null;
		}
		this.totalDefects = rawDefects.getTotal().divide(divisor, MeasureUnit.SCALE, RoundingMode.HALF_DOWN);
		this.totalDamage = rawDamage.getTotal().divide(divisor, MeasureUnit.SCALE, RoundingMode.HALF_DOWN);
		
		
	}
	
	public String getCheckedBy() {
		if(this.checkedBy != null)
			return this.checkedBy.toString();
		return null;
	}
	
	public BigDecimal getTotalDefectsAndDamage() {
		return getTotalDamage().add(getTotalDefects());
	}

}
