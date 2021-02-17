/**
 * 
 */
package com.avc.mis.beta.dto.doc;

import java.math.BigDecimal;
import java.util.stream.Stream;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.BulkItem;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.PackedItem;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class ContainerPoItemRow extends BasicDTO {

	BasicValueEntity<Item> item;
	PoCodeBasic poCode;//should be removed
	String[] poCodes;

	AmountWithUnit[] totalRow;

	public ContainerPoItemRow(@NonNull Integer id, 
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			BigDecimal itemUnitAmount, MeasureUnit itemMeasureUnit, Class<? extends Item> itemClazz, 
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, String supplierName, String display,
			String poCodes,
			BigDecimal total, MeasureUnit measureUnit) {
		super(id);
		this.item = new BasicValueEntity<Item>(itemId, itemValue);
		this.poCode = new PoCodeBasic(poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName, display);
		if(poCodes != null)
			this.poCodes = Stream.of(poCodes.split(",")).distinct().toArray(String[]::new);
		else
			this.poCodes = null;
		AmountWithUnit totalRow;
		if(itemClazz == BulkItem.class) {
			totalRow = new AmountWithUnit(total, measureUnit);
		}
		else if(itemClazz == PackedItem.class){
			totalRow = new AmountWithUnit(total.multiply(itemUnitAmount), itemMeasureUnit);
		}
		else 
		{
			throw new IllegalStateException("The class can only apply to weight items");
		}
		
		
		this.totalRow = new AmountWithUnit[] {
				totalRow.convert(MeasureUnit.LBS).setScale(MeasureUnit.SCALE),
				totalRow.convert(MeasureUnit.KG).setScale(MeasureUnit.SCALE)
		};
	}
	
	

}
