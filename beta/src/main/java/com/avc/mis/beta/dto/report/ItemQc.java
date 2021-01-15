/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;

import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.RawDamage;
import com.avc.mis.beta.entities.embeddable.RawDefects;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.QcCompany;
import com.avc.mis.beta.entities.item.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
public class ItemQc {

	@JsonIgnore
	Integer processId;
	BasicValueEntity<Item> item;

	@JsonIgnore
	Boolean precentage;

	BigDecimal humidity;
	BigDecimal breakage;
	BigDecimal totalDefects;
	BigDecimal totalDamage;



	public ItemQc(
			Integer processId,
			Integer itemId, String itemValue, 
			BigDecimal sampleWeight, boolean precentage,
			BigDecimal humidity, BigDecimal breakage,
			BigDecimal scorched, BigDecimal deepCut, BigDecimal offColour, 
			BigDecimal shrivel, BigDecimal desert, BigDecimal deepSpot, 
			BigDecimal mold, BigDecimal dirty, BigDecimal lightDirty, 
			BigDecimal decay, BigDecimal insectDamage, BigDecimal testa) {
		this.processId = processId;
		this.item = new BasicValueEntity<Item>(itemId, itemValue);
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
