package com.avc.mis.beta.dto.view;

import java.util.ArrayList;
import java.util.List;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Data;
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
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class PoInventoryRow extends BasicDTO {

	private PoCodeBasic poCode;
	//already in poCode
	private String supplierName;
	
	private AmountWithUnit totalWeight;
	private List<ProcessItemInventoryRow> poInventoryRows;

	public PoInventoryRow(@NonNull PoCodeBasic poCode) {
		super(poCode.getId());
		this.poCode = poCode;
		this.supplierName = poCode.getSupplierName();
	}
	
	public void setPoInventoryRows(List<ProcessItemInventoryRow> poInventoryRows) {
		this.poInventoryRows = poInventoryRows;
		this.totalWeight = ProcessItemInventoryRow.getTotalWeight(poInventoryRows);
	}
	
	public List<AmountWithUnit> getTotalStock() {
		List<AmountWithUnit> totalStock = new ArrayList<>();
		if(this.totalWeight != null) {
			totalStock.add(this.totalWeight.convert(MeasureUnit.LBS).setScale(MeasureUnit.SCALE));
			totalStock.add(this.totalWeight.convert(MeasureUnit.LOT).setScale(MeasureUnit.SCALE));
		}
		return totalStock;
	}

}
