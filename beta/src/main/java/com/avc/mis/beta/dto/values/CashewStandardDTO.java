package com.avc.mis.beta.dto.values;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.embeddable.RawDamage;
import com.avc.mis.beta.entities.embeddable.RawDefects;
import com.avc.mis.beta.entities.values.CashewStandard;
import com.avc.mis.beta.entities.values.Item;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class CashewStandardDTO extends ValueDTO {

	String standardOrganization;	

	BasicValueEntity<Item> item;
	
	RawDefects defects;
	RawDamage damage;
	BigDecimal totalDefects;
	BigDecimal totalDamage;
	BigDecimal totalDefectsAndDamage;
	
	BigDecimal wholeCountPerLb;
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
//	BigDecimal decay;
//	BigDecimal insectDamage;
//	BigDecimal defectsAfterRoasting;
	BigDecimal roastingWeightLoss;
	
	public CashewStandardDTO(@NonNull Integer id, String standardOrganization, 
			Integer itemId, String itemValue, 
			BigDecimal totalDefects, BigDecimal totalDamage, BigDecimal totalDefectsAndDamage, 
			BigDecimal foreignMaterial,
			BigDecimal wholeCountPerLb, BigDecimal smallSize, BigDecimal ws, BigDecimal lp, BigDecimal breakage, 
			BigDecimal humidity, 
			BigDecimal scorched, BigDecimal deepCut, BigDecimal offColour,
			BigDecimal shrivel, BigDecimal desert, BigDecimal deepSpot, 
			BigDecimal mold, BigDecimal dirty, BigDecimal lightDirty,
			BigDecimal decay, BigDecimal insectDamage, BigDecimal testa, BigDecimal roastingWeightLoss) {
		super(id);
		this.standardOrganization = standardOrganization;
		this.item = new BasicValueEntity<Item>(itemId, itemValue);
		this.damage = new RawDamage(mold, dirty, lightDirty, decay, insectDamage, testa);
		this.defects = new RawDefects(scorched, deepCut, offColour, shrivel, desert, deepSpot);
		this.totalDefects = totalDefects;
		this.totalDamage = totalDamage;
		this.totalDefectsAndDamage = totalDefectsAndDamage;
		this.wholeCountPerLb = wholeCountPerLb;
		this.smallSize = smallSize;
		this.ws = ws;
		this.lp = lp;
		this.breakage = breakage;
		this.foreignMaterial = foreignMaterial;
		this.humidity = humidity;
//		this.testa = testa;
//		this.scorched = scorched;
//		this.deepCut = deepCut;
//		this.offColour = offColour;
//		this.shrivel = shrivel;
//		this.desert = desert;
//		this.deepSpot = deepSpot;
//		this.mold = mold;
//		this.dirty = dirty;
//		this.decay = decay;
//		this.insectDamage = insectDamage;
//		this.defectsAfterRoasting = defectsAfterRoasting;
		this.roastingWeightLoss = roastingWeightLoss;
	}
	
	public CashewStandardDTO(CashewStandard standard) {
		super(standard.getId());
		this.standardOrganization = standard.getStandardOrganization();
		Item item = standard.getItem();
		if(item != null)
			this.item = new BasicValueEntity<Item>(item.getId(), item.getValue());
		else
			this.item = null;
		this.wholeCountPerLb = standard.getWholeCountPerLb();
		this.smallSize = standard.getSmallSize();
		this.ws = standard.getWs();
		this.lp = standard.getLp();
		this.damage = standard.getDamage();
		this.defects = standard.getDefects();
		this.totalDefects = standard.getTotalDefects();
		this.totalDamage = standard.getTotalDamage();
		this.totalDefectsAndDamage = standard.getTotalDefectsAndDamage();
		this.breakage = standard.getBreakage();
		this.foreignMaterial = standard.getForeignMaterial();
		this.humidity = standard.getHumidity();
//		this.testa = standard.getTesta();
//		this.scorched = standard.getScorched();
//		this.deepCut = standard.getDeepCut();
//		this.offColour = standard.getOffColour();
//		this.shrivel = standard.getShrivel();
//		this.desert = standard.getDesert();
//		this.deepSpot = standard.getDeepSpot();
//		this.mold = standard.getMold();
//		this.dirty = standard.getDirty();
//		this.decay = standard.getDecay();
//		this.insectDamage = standard.getInsectDamage();
//		this.defectsAfterRoasting = standard.getDefectsAfterRoasting();
		this.roastingWeightLoss = standard.getRoastingWeightLoss();
	}

	public String getValue() {
		return String.format("%s-%s", this.item.getValue(), this.standardOrganization);
	}
}
