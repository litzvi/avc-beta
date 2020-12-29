/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.OffsetDateTime;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.RawDamage;
import com.avc.mis.beta.entities.embeddable.RawDefects;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
public class QcReportLine {

	String checkedBy;
	BasicValueEntity<Item> item;
	OffsetDateTime checkDate;

	@JsonIgnore
	Boolean precentage;

	BigDecimal humidity;
	BigDecimal breakage;
	BigDecimal totalDefects;
	BigDecimal totalDamage;


	public QcReportLine(String checkedBy,
			Integer itemId, String itemValue, 
			OffsetDateTime checkDate, 
			BigDecimal sampleWeight, boolean precentage,
			BigDecimal humidity, BigDecimal breakage,
			BigDecimal scorched, BigDecimal deepCut, BigDecimal offColour, 
			BigDecimal shrivel, BigDecimal desert, BigDecimal deepSpot, 
			BigDecimal mold, BigDecimal dirty, BigDecimal lightDirty, 
			BigDecimal decay, BigDecimal insectDamage, BigDecimal testa) {
		this.checkedBy = checkedBy;
		this.item = new BasicValueEntity<Item>(itemId, itemValue);
		this.checkDate = checkDate;
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
	
	public BigDecimal getTotalDefectsAndDamage() {
		return getTotalDamage().add(getTotalDefects());
	}

}
