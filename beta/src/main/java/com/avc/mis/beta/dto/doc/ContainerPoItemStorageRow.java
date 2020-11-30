/**
 * 
 */
package com.avc.mis.beta.dto.doc;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
public class ContainerPoItemStorageRow {

	String item;
	PoCodeBasic poCode;
	AmountWithUnit unitAmount;
//	BigDecimal containerWeight;
	BigDecimal numberUnits;	

	public ContainerPoItemStorageRow(String item, 
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits) {
		super();
		this.item = item;
		this.poCode = new PoCodeBasic(poCodeId, contractTypeCode, contractTypeSuffix);
		this.unitAmount = new AmountWithUnit(unitAmount, measureUnit);
//		this.containerWeight = containerWeight;
		this.numberUnits = numberUnits;
		
	}
}
