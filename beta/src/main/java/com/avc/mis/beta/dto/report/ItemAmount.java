/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.ItemWithUnitDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.ToString;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
public class ItemAmount {

	BasicValueEntity<Item> item;

	@ToString.Exclude @JsonIgnore
	ItemWithUnitDTO itemWithUnit;
	BigDecimal amount;
	
	public ItemAmount(Integer id, String value, MeasureUnit defaultMeasureUnit, 
			ItemGroup group, ProductionUse productionUse, 
			BigDecimal unitAmount, MeasureUnit unitMeasureUnit, Class<? extends Item> clazz, 
			BigDecimal amount) {
		super();
		this.item = new BasicValueEntity<Item>(id, value);
		this.itemWithUnit = new ItemWithUnitDTO(id, value, defaultMeasureUnit, group, productionUse, unitAmount, unitMeasureUnit, clazz);
		this.amount = amount;
	}
	
	public AmountWithUnit[] getWeight() {
		return null;
	}
	
	@JsonIgnore
	public ItemGroup getItemGroup() {
		return itemWithUnit.getGroup();
	}

}
