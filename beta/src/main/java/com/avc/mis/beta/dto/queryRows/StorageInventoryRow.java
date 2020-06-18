/**
 * 
 */
package com.avc.mis.beta.dto.queryRows;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StorageInventoryRow  extends ValueDTO {

	Integer processItemId;
	AmountWithUnit unitAmount;
	BigDecimal numberUnits;	
	BasicValueEntity<Warehouse> warehouseLocation;
	
	public StorageInventoryRow(Integer id, Integer processItemId,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, 
			Integer warehouseLocationId,  String warehouseLocationValue) {
		super(id);
		this.processItemId = processItemId;
		this.unitAmount = new AmountWithUnit(unitAmount, measureUnit);;
		this.numberUnits = numberUnits;
		if(warehouseLocationId != null && warehouseLocationValue != null)
			this.warehouseLocation = new BasicValueEntity<Warehouse>(warehouseLocationId,  warehouseLocationValue);
		else
			this.warehouseLocation = null;
	}
	
	public StorageInventoryRow(Integer id, Integer processItemId, AmountWithUnit unitAmount,
			BigDecimal numberUnits, BasicValueEntity<Warehouse> warehouseLocation) {
		super(id);
		this.processItemId = processItemId;
		this.unitAmount = unitAmount;
		this.numberUnits = numberUnits;
		this.warehouseLocation = warehouseLocation;
	}
}
