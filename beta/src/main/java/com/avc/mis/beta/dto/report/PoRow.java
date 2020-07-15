package com.avc.mis.beta.dto.report;

import java.time.LocalDate;
import java.util.List;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PoRow extends ValueDTO {

//	PoCodeBasic poCode;
	LocalDate deliveryDate; //for sorting
	AmountWithUnit totalAmount;

	List<PoItemRow> poRows;

	public PoRow(@NonNull Integer id, LocalDate deliveryDate, AmountWithUnit totalAmount, List<PoItemRow> poRows) {
		super(id);
//		this.poCode = poCode;
		this.deliveryDate = deliveryDate;
		this.totalAmount = totalAmount;
		this.poRows = poRows;
	}
	
	

}
