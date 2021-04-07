/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.BasicSubjectDataDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.values.Warehouse;
import com.avc.mis.beta.utilities.CollectionItemWithGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * DTO used as the list of storages for ProcessItemInventoryRow.
 * Contains id (for reference) of process item and storage amount, location and amounts used.
 * 
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class StorageInventoryRow extends BasicSubjectDataDTO implements CollectionItemWithGroup<StorageInventoryRow, ProcessItemInventory> {

	Integer processItemId;
	BigDecimal unitAmount;
	BigDecimal numberUnits;	
//	BigDecimal accessWeight;
	BasicValueEntity<Warehouse> warehouseLocation;
	BigDecimal numberUsedUnits;
	AmountWithUnit totalBalance;
	
	/**
	 * All database fields (the fields in the form they are fetched from the db) arguments constructor.
	 */
	public StorageInventoryRow(Integer id, Integer version, Integer ordinal,
			Integer processItemId,
			BigDecimal unitAmount, BigDecimal numberUnits, //BigDecimal accessWeight,
			Integer warehouseLocationId,  String warehouseLocationValue,
			BigDecimal numberUsedUnits, 
			BigDecimal totalBalance, MeasureUnit totalBalanceMU) {
		super(id, version, ordinal);
		this.processItemId = processItemId;
		this.unitAmount = unitAmount;
		this.numberUnits = numberUnits;
//		this.accessWeight = accessWeight;
		if(warehouseLocationId != null && warehouseLocationValue != null)
			this.warehouseLocation = new BasicValueEntity<Warehouse>(warehouseLocationId,  warehouseLocationValue);
		else
			this.warehouseLocation = null;
		this.numberUsedUnits = numberUsedUnits;
		this.totalBalance = new AmountWithUnit(totalBalance, totalBalanceMU);
	}
	
	/**
	 * All class arguments constructor
	 */
	public StorageInventoryRow(Integer id, Integer version, Integer ordinal,
			Integer processItemId, BigDecimal unitAmount, //BigDecimal accessWeight,
			BigDecimal numberUnits, BasicValueEntity<Warehouse> warehouseLocation, 
			BigDecimal numberUsedUnits, AmountWithUnit totalBalance) {
		super(id, version, ordinal);
		this.processItemId = processItemId;
		this.unitAmount = unitAmount;
		this.numberUnits = numberUnits;
//		this.accessWeight = accessWeight;
		this.warehouseLocation = warehouseLocation;
		this.numberUsedUnits = numberUsedUnits;
		this.totalBalance = totalBalance;
	}
	
	public BigDecimal getNumberAvailableUnits() {
		return this.numberUnits.subtract(this.numberUsedUnits);
	}
	
	@JsonIgnore
	@Override
	public StorageInventoryRow getItem() {
		return this;
	}

	@JsonIgnore
	@Override
	public ProcessItemInventory getGroup() {
		ProcessItemInventory group = new ProcessItemInventory();
		group.setId(getProcessItemId());
		return group;
	}

}
