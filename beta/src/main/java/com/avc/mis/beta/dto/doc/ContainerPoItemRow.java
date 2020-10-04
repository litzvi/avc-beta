/**
 * 
 */
package com.avc.mis.beta.dto.doc;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Data;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
public class ContainerPoItemRow {

	String item;
	PoCodeBasic poCode;

	AmountWithUnit[] totalRow;

	public ContainerPoItemRow(String item, 
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, 
			BigDecimal total, MeasureUnit measureUnit) {
		super();
		this.item = item;
		this.poCode = new PoCodeBasic(poCodeId, contractTypeCode, contractTypeSuffix);
		AmountWithUnit totalRow = new AmountWithUnit(total, measureUnit);
		this.totalRow = new AmountWithUnit[] {
				totalRow.convert(MeasureUnit.LBS).setScale(MeasureUnit.SCALE),
				totalRow.convert(MeasureUnit.KG).setScale(MeasureUnit.SCALE)
		};
	}
	
	

}
