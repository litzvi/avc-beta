/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.math.BigDecimal;

import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
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
