/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.math.BigDecimal;
import java.util.stream.Stream;

import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class RelocationWithItemAmount extends ProductionProcessWithItemAmount {


	String[] newWarehouses;
	
	public RelocationWithItemAmount(@NonNull Integer id, 
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			BigDecimal unitAmount, MeasureUnit unitMeasureUnit, Class<? extends Item> clazz,
			BigDecimal amount, String warehouses, String newWarehouses) {
		super(id, itemId, itemValue, defaultMeasureUnit, unitAmount, unitMeasureUnit, clazz, amount, warehouses);
		if(newWarehouses != null) {
			this.newWarehouses = Stream.of(newWarehouses.split(",")).distinct().toArray(String[]::new);
		}
		else {
			this.newWarehouses = null;
		}
	}	
}
