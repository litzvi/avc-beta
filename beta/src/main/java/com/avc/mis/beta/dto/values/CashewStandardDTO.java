package com.avc.mis.beta.dto.values;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.ValueDTO;
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
	
	BigDecimal totalDefects;
	BigDecimal totalDamage;
	
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
	BigDecimal defectsAfterRoasting;
	BigDecimal weightLoss;
	
	public CashewStandardDTO(@NonNull Integer id, String standardOrganization, 
			Integer itemId, String itemValue, 
			BigDecimal totalDefects, BigDecimal totalDamage, BigDecimal breakage, BigDecimal foreignMaterial,
			BigDecimal humidity, BigDecimal testa, BigDecimal scorched, BigDecimal deepCut, BigDecimal offColour,
			BigDecimal shrivel, BigDecimal desert, BigDecimal deepSpot, BigDecimal mold, BigDecimal dirty,
			BigDecimal decay, BigDecimal insectDamage, BigDecimal defectsAfterRoasting, BigDecimal weightLoss) {
		super(id);
		this.standardOrganization = standardOrganization;
		this.item = new BasicValueEntity<Item>(itemId, itemValue);
		this.totalDefects = totalDefects;
		this.totalDamage = totalDamage;
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
		this.defectsAfterRoasting = defectsAfterRoasting;
		this.weightLoss = weightLoss;
	}
	
	public CashewStandardDTO(CashewStandard standard) {
		super(standard.getId());
		this.standardOrganization = standard.getStandardOrganization();
		Item item = standard.getItem();
		if(item != null)
			this.item = new BasicValueEntity<Item>(item.getId(), item.getValue());
		else
			this.item = null;
		this.totalDefects = standard.getTotalDefects();
		this.totalDamage = standard.getTotalDamage();
		this.breakage = standard.getBreakage();
		this.foreignMaterial = standard.getForeignMaterial();
		this.humidity = standard.getHumidity();
		this.testa = standard.getTesta();
		this.scorched = standard.getScorched();
		this.deepCut = standard.getDeepCut();
		this.offColour = standard.getOffColour();
		this.shrivel = standard.getShrivel();
		this.desert = standard.getDesert();
		this.deepSpot = standard.getDeepSpot();
		this.mold = standard.getMold();
		this.dirty = standard.getDirty();
		this.decay = standard.getDecay();
		this.insectDamage = standard.getInsectDamage();
		this.defectsAfterRoasting = standard.getDefectsAfterRoasting();
		this.weightLoss = standard.getWeightLoss();
	}

	public String getValue() {
		return String.format("%s-%s", this.item.getValue(), this.standardOrganization);
	}
}
