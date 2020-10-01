/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.List;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.entities.embeddable.RawDamage;
import com.avc.mis.beta.entities.embeddable.RawDefects;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CashewQcRow extends ValueDTO {

	private PoCodeBasic poCode;
	private String supplierName;
	private Integer itemId;
	private String itemName;
	private OffsetDateTime checkDate;
	private BigInteger numberOfSamples;
	private BigDecimal sampleWeight;
	private BigDecimal totalDefects;
	private BigDecimal totalDamage;


	public CashewQcRow(@NonNull Integer id, 
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName, 
			String itemName, OffsetDateTime checkDate, 
			BigInteger numberOfSamples, BigDecimal sampleWeight, boolean precentage,
			BigDecimal scorched, BigDecimal deepCut, BigDecimal offColour, 
			BigDecimal shrivel, BigDecimal desert, BigDecimal deepSpot, 
			BigDecimal mold, BigDecimal dirty, BigDecimal lightDirty, 
			BigDecimal decay, BigDecimal insectDamage, BigDecimal testa) {
		super(id);
		this.poCode = new PoCodeBasic(poCodeId, contractTypeCode, contractTypeSuffix);
		this.supplierName = supplierName;
		this.itemName = itemName;
		this.checkDate = checkDate;
		this.numberOfSamples = numberOfSamples;
		this.sampleWeight = sampleWeight;
		
		RawDefects rawDefects = new RawDefects(scorched, deepCut, offColour, shrivel, desert, deepSpot);		
		RawDamage rawDamage = new RawDamage(mold, dirty, lightDirty, decay, insectDamage, testa);
		
		BigDecimal divisor;
		if(precentage) {
			divisor = BigDecimal.valueOf(100L);
		}
		else{		
			divisor = sampleWeight;
		}
		this.totalDefects = rawDefects.getTotal().divide(divisor, MeasureUnit.SCALE, RoundingMode.HALF_DOWN);
		this.totalDamage = rawDamage.getTotal().divide(divisor, MeasureUnit.SCALE, RoundingMode.HALF_DOWN);
		
		
	}
	
	public BigDecimal getTotalDefectsAndDamage() {
		return getTotalDamage().add(getTotalDefects());
	}

}
