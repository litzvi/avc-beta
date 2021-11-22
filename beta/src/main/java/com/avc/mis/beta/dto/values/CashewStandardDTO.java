package com.avc.mis.beta.dto.values;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.basic.BasicValueEntity;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.embeddable.RawDamage;
import com.avc.mis.beta.entities.embeddable.RawDefects;
import com.avc.mis.beta.entities.values.CashewStandard;
import com.avc.mis.beta.entities.values.Item;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * DTO for cashew standard.
 * 
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CashewStandardDTO extends ValueDTO {

	private String value;
	private Set<BasicValueEntity<Item>> items;

	private String standardOrganization;	
	 
	private RawDefects defects;
	private RawDamage damage;
	private BigDecimal totalDefects;
	private BigDecimal totalDamage;
	private BigDecimal totalDefectsAndDamage;
	 
	private BigDecimal wholeCountPerLb;
	private BigDecimal smallSize;
	private BigDecimal ws;
	private BigDecimal lp;
	private BigDecimal breakage;
	private BigDecimal foreignMaterial;
	private BigDecimal humidity;
	private BigDecimal roastingWeightLoss;
	
	public CashewStandardDTO(@NonNull Integer id, String value, String standardOrganization, 
			BigDecimal totalDefects, BigDecimal totalDamage, BigDecimal totalDefectsAndDamage, 
			BigDecimal wholeCountPerLb, BigDecimal smallSize, BigDecimal ws, BigDecimal lp, BigDecimal breakage, 
			BigDecimal foreignMaterial, BigDecimal humidity, 
			BigDecimal scorched, BigDecimal deepCut, BigDecimal offColour,
			BigDecimal shrivel, BigDecimal desert, BigDecimal deepSpot, 
			BigDecimal mold, BigDecimal dirty, BigDecimal lightDirty,
			BigDecimal decay, BigDecimal insectDamage, BigDecimal testa, BigDecimal roastingWeightLoss) {
		super(id);
		this.value = value;
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
		this.roastingWeightLoss = roastingWeightLoss;
	}

//	public String getValue() {
//		return String.format("%s", this.standardOrganization);
//	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return CashewStandard.class;
	}
	
	@Override
	public CashewStandard fillEntity(Object entity) {
		CashewStandard stdQuality;
		if(entity instanceof CashewStandard) {
			stdQuality = (CashewStandard) entity;
		}
		else {
			throw new IllegalStateException("Param has to be CashewStandard class");
		}
		super.fillEntity(stdQuality);
		stdQuality.setValue(getValue());
		stdQuality.setStandardOrganization(getStandardOrganization());
		if(getItems() == null || getItems().isEmpty()) {
			throw new IllegalArgumentException("Cashew standard has to reference at least one item");
		}
		else {
			stdQuality.setItems(getItems().stream().map(i -> (Item)i.fillEntity(new Item())).collect(Collectors.toSet()));
		}
		stdQuality.setDefects(getDefects());
		stdQuality.setDamage(getDamage());
		stdQuality.setTotalDefects(getTotalDefects());
		stdQuality.setTotalDamage(getTotalDamage());
		stdQuality.setTotalDefectsAndDamage(getTotalDefectsAndDamage());
		stdQuality.setWholeCountPerLb(getWholeCountPerLb());
		stdQuality.setSmallSize(getSmallSize());
		stdQuality.setWs(getWs());
		stdQuality.setLp(getLp());
		stdQuality.setBreakage(getBreakage());
		stdQuality.setForeignMaterial(getForeignMaterial());
		stdQuality.setHumidity(getHumidity());
		stdQuality.setRoastingWeightLoss(getRoastingWeightLoss());
		
				
		return stdQuality;
	}

}
