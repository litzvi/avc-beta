/**
 * 
 */
package com.avc.mis.beta.dto.doc;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.DTOWithId;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
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
@EqualsAndHashCode(callSuper = true)
public class ContainerPoItemRow extends DTOWithId {

	BasicValueEntity<Item> item;
	PoCodeBasic poCode;

	AmountWithUnit[] totalRow;

	public ContainerPoItemRow(@NonNull Integer id, 
			Integer itemId, String itemValue,
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, 
			BigDecimal total, MeasureUnit measureUnit) {
		super(id);
		this.item = new BasicValueEntity<Item>(itemId, itemValue);
		this.poCode = new PoCodeBasic(poCodeId, contractTypeCode, contractTypeSuffix);
		AmountWithUnit totalRow = new AmountWithUnit(total, measureUnit);
		this.totalRow = new AmountWithUnit[] {
				totalRow.convert(MeasureUnit.LBS).setScale(MeasureUnit.SCALE),
				totalRow.convert(MeasureUnit.KG).setScale(MeasureUnit.SCALE)
		};
	}
	
	

}
