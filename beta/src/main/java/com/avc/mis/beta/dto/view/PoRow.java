package com.avc.mis.beta.dto.view;

import java.util.List;

import com.avc.mis.beta.dto.DTOWithId;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PoRow extends DTOWithId {

//	PoCodeBasic poCode;
//	LocalDate deliveryDate; //for sorting
	AmountWithUnit[] totalAmount;

	List<PoItemRow> poRows;

	public PoRow(@NonNull Integer id, AmountWithUnit totalAmount, List<PoItemRow> poRows) {
		super(id);
//		this.poCode = poCode;
//		this.deliveryDate = deliveryDate;
		this.totalAmount = new AmountWithUnit[] {
				totalAmount.setScale(MeasureUnit.SCALE),
				totalAmount.convert(MeasureUnit.LOT).setScale(MeasureUnit.SCALE)};
		this.poRows = poRows;
	}
	
	

}
