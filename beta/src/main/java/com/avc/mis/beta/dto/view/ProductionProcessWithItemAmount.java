/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.values.Item;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ProductionProcessWithItemAmount extends ValueDTO {

	BasicValueEntity<Item> item;
	AmountWithUnit amountWithUnit;
	
	public ProductionProcessWithItemAmount(@NonNull Integer id, 
			Integer itemId, String itemValue, 
			BigDecimal amount, MeasureUnit measureUnit) {
		super(id);
		this.item = new BasicValueEntity<Item>(itemId, itemValue);
		this.amountWithUnit = new AmountWithUnit(amount, measureUnit);
	}
	
	public ProductionProcessWithItemAmount(@NonNull Integer id, 
			BasicValueEntity<Item> item, AmountWithUnit amountWithUnit) {
		super(id);
		this.item = item;
		this.amountWithUnit = amountWithUnit;
	}
	
	
	
}
