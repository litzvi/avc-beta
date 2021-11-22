/**
 * 
 */
package com.avc.mis.beta.dto.process.group;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.RankedAuditedDTO;
import com.avc.mis.beta.dto.basic.ItemWithUse;
import com.avc.mis.beta.dto.process.collectionItems.CountAmountDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProductionUse;
import com.avc.mis.beta.entities.process.collectionItems.CountAmount;
import com.avc.mis.beta.entities.process.group.ItemCount;
import com.avc.mis.beta.entities.values.Item;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO Class representing a count/weight check for inventory check,
 * but dosen't actually influence inventory balance.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ItemCountDTO extends RankedAuditedDTO {

	private ItemWithUse item;
	private MeasureUnit measureUnit;
	private BigDecimal containerWeight;
	private BigDecimal accessWeight;
	
	private List<CountAmountDTO> amounts;

	public ItemCountDTO(Integer id, Integer version, Integer ordinal,
			Integer itemId, String itemValue, ProductionUse productionUse, Class<? extends Item> clazz,
			MeasureUnit measureUnit, BigDecimal containerWeight, BigDecimal accessWeight) {
		super(id, version, ordinal);
		this.item = new ItemWithUse(itemId, itemValue, productionUse, clazz);
		this.measureUnit = measureUnit;
		this.containerWeight = containerWeight;
		this.accessWeight = accessWeight;
	}

	public List<AmountWithUnit> getTotalAmount() {
		if(this.amounts == null) {
			return null;
		}
		BigDecimal total = this.amounts.stream()
				.map(i -> i.getAmount())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		if(this.containerWeight != null) {
			total = total.subtract(this.containerWeight.multiply(new BigDecimal(this.amounts.size()), MathContext.DECIMAL64));
		}
		if(this.accessWeight != null) {
			total = total.subtract(this.accessWeight);
		}
		AmountWithUnit totalAmount = new AmountWithUnit(total, this.measureUnit);
		return Arrays.asList(totalAmount.setScale(MeasureUnit.SUM_DISPLAY_SCALE));
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return ItemCount.class;
	}
	
	@Override
	public ItemCount fillEntity(Object entity) {
		ItemCount itemCount;
		if(entity instanceof ItemCount) {
			itemCount = (ItemCount) entity;
		}
		else {
			throw new IllegalStateException("Param has to be ItemCount class");
		}
		super.fillEntity(itemCount);
		
		if(getItem() == null)
			throw new IllegalArgumentException("Item is mandatory");
		else
			itemCount.setItem(getItem().fillEntity(new Item()));
		
		itemCount.setMeasureUnit(getMeasureUnit());		
		itemCount.setContainerWeight(getContainerWeight());
		itemCount.setAccessWeight(getAccessWeight());
		
		if(getAmounts() == null || getAmounts().isEmpty()) {
			throw new IllegalArgumentException("Sample has to contain at least one sampled amount");	
		}
		else {
			Ordinal.setOrdinals(getAmounts());
			itemCount.setAmounts(getAmounts().stream().map(i -> i.fillEntity(new CountAmount())).collect(Collectors.toSet()));
		}
		
		return itemCount;
	}
	
	
}
