/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.BulkItem;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.PackedItem;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;

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
