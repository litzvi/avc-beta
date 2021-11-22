/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.math.BigDecimal;

import com.avc.mis.beta.entities.enums.ItemGroup;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProductionUse;
import com.avc.mis.beta.entities.values.Item;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * Item with amount, weight and po code.
 * Used in multiple reports to show an item balance per po.
 * 
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ItemAmountWithPo extends ItemAmount {

	Integer poCodeId;

	public ItemAmountWithPo(Integer poCodeId, Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, ItemGroup itemGroup,
			ProductionUse productionUse, BigDecimal unitAmount, MeasureUnit unitMeasureUnit,
			Class<? extends Item> clazz, BigDecimal amount) {
		super(itemId, itemValue, defaultMeasureUnit, itemGroup, productionUse, unitAmount, unitMeasureUnit, clazz, amount);
		this.poCodeId = poCodeId;
	}	
	
}
