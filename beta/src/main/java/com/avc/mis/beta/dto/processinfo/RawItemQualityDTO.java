/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.RawDamage;
import com.avc.mis.beta.entities.embeddable.RawDefects;
import com.avc.mis.beta.entities.enums.CheckStatus;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.processinfo.RawItemQuality;
import com.avc.mis.beta.entities.values.Item;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RawItemQualityDTO extends ProcessDTO {
	
	BasicValueEntity<Item> item;
	MeasureUnit measureUnit;
	BigDecimal sampleWeight;
	BigInteger numberOfSamples;
	
	RawDefects defects;
	RawDamage damage;
	
	BigDecimal totalDefects;
	BigDecimal totalDamage;
	
	BigInteger wholeCountPerLb;
	BigDecimal smallSize;
	BigDecimal ws;
	BigDecimal lp;
	BigDecimal breakage;
	BigDecimal foreignMaterial;
	BigDecimal humidity;
	
//	BigDecimal testa;
//	BigDecimal scorched;
//	BigDecimal deepCut;
//	BigDecimal offColour;
//	BigDecimal shrivel;
//	BigDecimal desert;
//	BigDecimal deepSpot;
//	BigDecimal mold;
//	BigDecimal dirty;
//	BigDecimal lightDirty;
//	BigDecimal decay;
//	BigDecimal insectDamage;
//	BigDecimal nutCount;
//	BigDecimal smallKernels;
//	BigDecimal defectsAfterRoasting;
	BigDecimal roastingWeightLoss;
	CheckStatus colour; 
	CheckStatus flavour; 
	

	public RawItemQualityDTO(Integer id, Integer version, 
			Integer itemId, String itemValue,
			MeasureUnit measureUnit, BigDecimal sampleWeight, BigInteger numberOfSamples,
			/* Integer poCodeId, ContractTypeCode contractTypeCode, String supplierName, */
			/* String description, String remarks, */ 
			BigInteger wholeCountPerLb, BigDecimal smallSize, BigDecimal ws, BigDecimal lp, BigDecimal breakage, 
			BigDecimal foreignMaterial, BigDecimal humidity, 
			BigDecimal scorched, BigDecimal deepCut, BigDecimal offColour, BigDecimal shrivel, BigDecimal desert,
			BigDecimal deepSpot, BigDecimal mold, BigDecimal dirty, BigDecimal lightDirty, 
			BigDecimal decay, BigDecimal insectDamage, BigDecimal testa,
			BigDecimal roastingWeightLoss,
			CheckStatus colour, CheckStatus flavour) {
		super(id, version);
//		super(id, version, itemId, itemValue, /* poCodeId, contractTypeCode, supplierName, */ description, remarks);
		this.item = new BasicValueEntity<Item>(itemId, itemValue);
		this.measureUnit = measureUnit;
		this.sampleWeight = sampleWeight;
		this.numberOfSamples = numberOfSamples;

		this.wholeCountPerLb = wholeCountPerLb;
		this.smallSize = smallSize;
		this.ws = ws;
		this.lp = lp;
		this.breakage = breakage;
		this.foreignMaterial = foreignMaterial;
		this.humidity = humidity;
		
		this.damage = new RawDamage(mold, dirty, lightDirty, decay, insectDamage, testa);
		this.defects = new RawDefects(scorched, deepCut, offColour, shrivel, desert, deepSpot);
		
		this.totalDamage = this.damage.getTotal();
		this.totalDefects = this.defects.getTotal();
		
//		this.scorched = scorched;
//		this.deepCut = deepCut;
//		this.offColour = offColour;
//		this.shrivel = shrivel;
//		this.desert = desert;
//		this.deepSpot = deepSpot;
//		this.testa = testa;
//		this.mold = mold;
//		this.dirty = dirty;
//		this.lightDirty = lightDirty;
//		this.decay = decay;
//		this.insectDamage = insectDamage;
//		this.nutCount = nutCount;
//		this.smallKernels = smallKernels;
//		this.defectsAfterRoasting = defectsAfterRoasting;
		this.roastingWeightLoss = roastingWeightLoss;
		this.colour = colour;
		this.flavour = flavour;
	}
	
	public RawItemQualityDTO(RawItemQuality itemQuality) {
		super(itemQuality.getId(), itemQuality.getVersion());
		this.item = new BasicValueEntity<Item>(itemQuality.getItem());
		this.measureUnit = itemQuality.getMeasureUnit();
		this.sampleWeight = itemQuality.getSampleWeight();
		this.numberOfSamples = itemQuality.getNumberOfSamples();

		this.wholeCountPerLb = itemQuality.getWholeCountPerLb();
		this.smallSize = itemQuality.getSmallSize();
		this.ws = itemQuality.getWs();
		this.lp = itemQuality.getLp();
		this.breakage = itemQuality.getBreakage();            
		this.foreignMaterial = itemQuality.getForeignMaterial();
		this.humidity = itemQuality.getHumidity();
		
		this.defects = itemQuality.getDefects();
//		this.scorched = rawDefects.getScorched();
//		this.deepCut = rawDefects.getDeepCut();
//		this.offColour = rawDefects.getOffColour();
//		this.shrivel = rawDefects.getShrivel();
//		this.desert = rawDefects.getDesert();
//		this.deepSpot = rawDefects.getDeepSpot();
		this.damage = itemQuality.getDamage();
//		this.testa = rawDamage.getTesta();
//		this.mold = rawDamage.getMold();
//		this.dirty = rawDamage.getDirty();
//		this.lightDirty = rawDamage.getLightDirty();
//		this.decay = rawDamage.getDecay();
//		this.insectDamage = rawDamage.getInsectDamage();
//		this.nutCount = itemQuality.getNutCount();
//		this.smallKernels = getSmallKernels();        
//		this.defectsAfterRoasting = itemQuality.getDefectsAfterRoasting();
		
		this.totalDamage = this.damage.getTotal();
		this.totalDefects = this.defects.getTotal();
		
		this.roastingWeightLoss = itemQuality.getRoastingWeightLoss();
		this.colour = itemQuality.getColour();
		this.flavour = itemQuality.getFlavour();
	}

//	
//	public BigDecimal getTotalDefects() {
//		return this.rawDefects.getTotal();
//	}
//	
//	public BigDecimal getTotalDamage() {
//		return this.rawDamage.getTotal();
//	}

	public BigDecimal getTotalDefectsAndDamage() {
		return getTotalDamage().add(getTotalDefects());
	}

}
