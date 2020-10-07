package com.avc.mis.beta.dto.view;

import java.util.List;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * DTO of inventory for one po code. 
 * Contains total stock in inventory for this po code, it's storages
 * and information of the originating process, process item and used amounts.
 * 
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PoInventoryRow extends ValueDTO {

	PoCodeDTO poCode;
	String supplierName;
	AmountWithUnit[] totalStock;

	List<ProcessItemInventoryRow> poInventoryRows;

	public PoInventoryRow(@NonNull PoCodeDTO poCode, AmountWithUnit totalStock, 
			List<ProcessItemInventoryRow> poInventoryRows) {
		super(poCode.getId());
		this.poCode = poCode;
		this.supplierName = poCode.getSupplierName();
		this.totalStock = new AmountWithUnit[2];
		this.totalStock[0] = totalStock.setScale(MeasureUnit.SCALE);
		this.totalStock[1] = totalStock.convert(MeasureUnit.LOT).setScale(MeasureUnit.SCALE);
		this.poInventoryRows = poInventoryRows;
	}

}
