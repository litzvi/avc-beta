package com.avc.mis.beta.dto.queryRows;

import java.util.List;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.process.PoCodeDTO;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PoInventoryRow extends ValueDTO {

	PoCodeDTO poCode;

	List<ProcessItemInventoryRow> poInventoryRows;

	public PoInventoryRow(@NonNull PoCodeDTO poCode, List<ProcessItemInventoryRow> poInventoryRows) {
		super(poCode.getId());
		this.poCode = poCode;
		this.poInventoryRows = poInventoryRows;
	}

}
