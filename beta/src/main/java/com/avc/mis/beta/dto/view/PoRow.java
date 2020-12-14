package com.avc.mis.beta.dto.view;

import java.util.List;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class PoRow extends BasicDTO {

//	PoCodeBasic poCode;
//	LocalDate deliveryDate; //for sorting
//	AmountWithUnit totalAmount;

	List<PoItemRow> poRows;

	public PoRow(@NonNull Integer id, List<PoItemRow> poRows) {
		super(id);
//		this.poCode = poCode;
//		this.deliveryDate = deliveryDate;
//		this.totalAmount = totalAmount.setScale(MeasureUnit.SCALE);
//		this.totalAmount = new AmountWithUnit[] {
//				totalAmount.setScale(MeasureUnit.SCALE),
//				totalAmount.convert(MeasureUnit.LOT).setScale(MeasureUnit.SCALE)};
		this.poRows = poRows;
	}
	
	//perhaps total weight
	public AmountWithUnit[] getTotalAmount() {
		AmountWithUnit totalAmount = poRows.stream()
				.map(pi -> pi.getNumUnits())
				.filter(u -> MeasureUnit.WEIGHT_UNITS.contains(u.getMeasureUnit()))
				.reduce(AmountWithUnit::add).orElse(AmountWithUnit.ZERO_LBS);
		return new AmountWithUnit[] {
				totalAmount.setScale(MeasureUnit.SCALE),
				totalAmount.convert(MeasureUnit.LOT).setScale(MeasureUnit.SCALE)};
	}

}
