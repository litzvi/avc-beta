/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
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

	BigInteger wholeCountPerLb;
	BigDecimal smallSize;
	BigDecimal ws;
	BigDecimal lp;
	BigDecimal breakage;
	BigDecimal foreignMaterial;
	BigDecimal humidity;
	BigDecimal testa;
	BigDecimal scorched;
	BigDecimal deepCut;
	BigDecimal offColour;
	BigDecimal shrivel;
	BigDecimal desert;
	BigDecimal deepSpot;
	BigDecimal mold;
	BigDecimal dirty;
	BigDecimal decay;
	BigDecimal insectDamage;
//	BigDecimal nutCount;
//	BigDecimal smallKernels;
//	BigDecimal defectsAfterRoasting;
	BigDecimal roastingWeightLoss;
	CheckStatus colour; 
	CheckStatus flavour; 
	

	public RawItemQualityDTO(Integer id, Integer version, 
			Integer itemId, String itemValue,
			MeasureUnit measureUnit, BigDecimal sampleWeight, 
			/* Integer poCodeId, ContractTypeCode contractTypeCode, String supplierName, */
			/* String description, String remarks, */ 
			BigInteger wholeCountPerLb, BigDecimal smallSize, BigDecimal ws, BigDecimal lp, BigDecimal breakage, 
			BigDecimal foreignMaterial, BigDecimal humidity, BigDecimal testa,
			BigDecimal scorched, BigDecimal deepCut, BigDecimal offColour, BigDecimal shrivel, BigDecimal desert,
			BigDecimal deepSpot, BigDecimal mold, BigDecimal dirty, BigDecimal decay, BigDecimal insectDamage,
			BigDecimal roastingWeightLoss,
			CheckStatus colour, CheckStatus flavour) {
		super(id, version);
//		super(id, version, itemId, itemValue, /* poCodeId, contractTypeCode, supplierName, */ description, remarks);
		this.item = new BasicValueEntity<Item>(itemId, itemValue);
		this.measureUnit = measureUnit;
		this.sampleWeight = sampleWeight;

		this.wholeCountPerLb = wholeCountPerLb;
		this.smallSize = smallSize;
		this.ws = ws;
		this.lp = lp;
		this.breakage = breakage;
		this.foreignMaterial = foreignMaterial;
		this.humidity = humidity;
		this.testa = testa;
		this.scorched = scorched;
		this.deepCut = deepCut;
		this.offColour = offColour;
		this.shrivel = shrivel;
		this.desert = desert;
		this.deepSpot = deepSpot;
		this.mold = mold;
		this.dirty = dirty;
		this.decay = decay;
		this.insectDamage = insectDamage;
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

		this.wholeCountPerLb = itemQuality.getWholeCountPerLb();
		this.smallSize = itemQuality.getSmallSize();
		this.ws = itemQuality.getWs();
		this.lp = itemQuality.getLp();
		this.breakage = itemQuality.getBreakage();            
		this.foreignMaterial = itemQuality.getForeignMaterial();
		this.humidity = itemQuality.getHumidity();
		this.testa = itemQuality.getTesta();
		this.scorched = itemQuality.getScorched();
		this.deepCut = itemQuality.getDeepCut();
		this.offColour = itemQuality.getOffColour();
		this.shrivel = itemQuality.getShrivel();
		this.desert = itemQuality.getDesert();
		this.deepSpot = itemQuality.getDeepSpot();
		this.mold = itemQuality.getMold();
		this.dirty = itemQuality.getDirty();
		this.decay = itemQuality.getDecay();
		this.insectDamage = itemQuality.getInsectDamage();
//		this.nutCount = itemQuality.getNutCount();
//		this.smallKernels = getSmallKernels();        
//		this.defectsAfterRoasting = itemQuality.getDefectsAfterRoasting();
		this.roastingWeightLoss = itemQuality.getRoastingWeightLoss();
		this.colour = itemQuality.getColour();
		this.flavour = itemQuality.getFlavour();
	}

	
	public BigDecimal getTotalDefects() {
		List<BigDecimal> list = Arrays.asList(this.testa, this.scorched, this.deepCut, 
				this.offColour, this.shrivel, this.desert, this.deepSpot);
		BigDecimal sum = BigDecimal.ZERO;
		for(BigDecimal augend: list) {
			if(augend != null) {
				sum = sum.add(augend);
			}
		}
		return sum;
	}
	
	public BigDecimal getTotalDamage() {
		List<BigDecimal> list = Arrays.asList(this.mold, this.dirty, this.decay, this.insectDamage);
		BigDecimal sum = BigDecimal.ZERO;
		for(BigDecimal augend: list) {
			if(augend != null) {
				sum = sum.add(augend);
			}
		}
		return sum;
	}

}
