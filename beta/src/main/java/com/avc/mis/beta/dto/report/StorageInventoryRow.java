/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.ProcessDTO;
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
public class StorageInventoryRow  extends ProcessDTO {

	Integer processItemId;
	AmountWithUnit unitAmount;
	BigDecimal numberUnits;	
	BasicValueEntity<Warehouse> warehouseLocation;
	BigDecimal usedUnits;
	AmountWithUnit totalBalance;
	
	public StorageInventoryRow(Integer id, Integer version, Integer processItemId,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, 
			Integer warehouseLocationId,  String warehouseLocationValue,
			BigDecimal usedUnits, 
			BigDecimal totalBalance, MeasureUnit totalBalanceMU) {
		super(id, version);
		this.processItemId = processItemId;
		this.unitAmount = new AmountWithUnit(unitAmount, measureUnit);;
		this.numberUnits = numberUnits;
		if(warehouseLocationId != null && warehouseLocationValue != null)
			this.warehouseLocation = new BasicValueEntity<Warehouse>(warehouseLocationId,  warehouseLocationValue);
		else
			this.warehouseLocation = null;
		this.usedUnits = usedUnits;
		this.totalBalance = new AmountWithUnit(totalBalance, totalBalanceMU);
	}
	
	public StorageInventoryRow(Integer id, Integer version, Integer processItemId, AmountWithUnit unitAmount,
			BigDecimal numberUnits, BasicValueEntity<Warehouse> warehouseLocation, 
			BigDecimal usedUnits, AmountWithUnit totalBalance) {
		super(id, version);
		this.processItemId = processItemId;
		this.unitAmount = unitAmount;
		this.numberUnits = numberUnits;
		this.warehouseLocation = warehouseLocation;
		this.usedUnits = usedUnits;
		this.totalBalance = totalBalance;
	}
}
