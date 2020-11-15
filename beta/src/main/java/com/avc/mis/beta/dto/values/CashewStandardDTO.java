package com.avc.mis.beta.dto.values;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.embeddable.RawDamage;
import com.avc.mis.beta.entities.embeddable.RawDefects;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.values.CashewStandard;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class CashewStandardDTO extends ValueDTO {

	private Set<BasicValueEntity<Item>> items;

	String standardOrganization;	
	
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
		this.items = new HashSet<BasicValueEntity<Item>>();
		for(Item i: standard.getItems()) {
			this.items.add(new BasicValueEntity<Item>(i.getId(), i.getValue()));
		}
		this.standardOrganization = standard.getStandardOrganization();
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
		return String.format("%s", this.standardOrganization);
	}
}
