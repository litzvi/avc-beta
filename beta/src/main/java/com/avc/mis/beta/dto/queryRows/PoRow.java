package com.avc.mis.beta.dto.queryRows;

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
	AmountWithUnit totalAmount;

	List<PoItemRow> poRows;

	public PoRow(@NonNull Integer id, AmountWithUnit totalAmount, List<PoItemRow> poRows) {
		super(id);
//		this.poCode = poCode;
		this.totalAmount = totalAmount;
		this.poRows = poRows;
	}
	
	

}
