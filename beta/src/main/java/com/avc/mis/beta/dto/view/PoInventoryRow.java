package com.avc.mis.beta.dto.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Setter;
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
	
	@Setter(value = AccessLevel.NONE)
	private List<ProcessItemInventoryRow> poInventoryRows;

	public PoInventoryRow(@NonNull PoCodeBasic poCode) {
		super(poCode.getId());
		this.poCode = poCode;
		this.supplierName = poCode.getSupplierName();
	}
	
	public void setProductPoInventoryRows(List<ProcessItemInventoryRow> poInventoryRows) {
		this.poInventoryRows = poInventoryRows;
		this.totalWeight = ProcessItemInventoryRow.getTotalWeight(poInventoryRows);
	}
	
	public void setGeneralPoInventoryRows(List<ProcessItemInventoryRow> poInventoryRows) {
		this.poInventoryRows = poInventoryRows;
		this.totalWeight = null;
	}
	
	public List<AmountWithUnit> getTotalStock() {
		if(this.totalWeight != null) {
			return AmountWithUnit.weightDisplay(this.totalWeight, Arrays.asList(MeasureUnit.LBS, MeasureUnit.LOT));
		}
		return null;
	}

}
