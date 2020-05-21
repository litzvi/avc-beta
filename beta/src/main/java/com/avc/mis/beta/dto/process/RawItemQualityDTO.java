/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.avc.mis.beta.entities.enums.CheckStatus;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.process.RawItemQuality;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class RawItemQualityDTO extends ProcessItemDTO {

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
	BigDecimal count;
	BigDecimal smallKernels;
	BigDecimal defectsAfterRoasting;
	BigDecimal weightLoss;
	CheckStatus colour; 
	CheckStatus flavour; 
	

	public RawItemQualityDTO(Integer id, Integer version, Item item, PoCode itemPo, String description,
			String remarks, 
			BigDecimal breakage, BigDecimal foreignMaterial, BigDecimal humidity, BigDecimal testa,
			BigDecimal scorched, BigDecimal deepCut, BigDecimal offColour, BigDecimal shrivel, BigDecimal desert,
			BigDecimal deepSpot, BigDecimal mold, BigDecimal dirty, BigDecimal decay, BigDecimal insectDamage,
			BigDecimal count, BigDecimal smallKernels, BigDecimal defectsAfterRoasting, BigDecimal weightLoss,
			CheckStatus colour, CheckStatus flavour) {
		super(id, version, item, itemPo, description, remarks);
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
		this.count = count;
		this.smallKernels = smallKernels;
		this.defectsAfterRoasting = defectsAfterRoasting;
		this.weightLoss = weightLoss;
		this.colour = colour;
		this.flavour = flavour;
	}
	
	public RawItemQualityDTO(RawItemQuality itemQuality) {
		super(itemQuality);
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
		this.count = itemQuality.getCount();
		this.smallKernels = getSmallKernels();        
		this.defectsAfterRoasting = itemQuality.getDefectsAfterRoasting();
		this.weightLoss = itemQuality.getWeightLoss();
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
