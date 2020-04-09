/**
 * 
 */
package com.avc.mis.beta.dto.values;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.OrderStatus;
import com.avc.mis.beta.entities.process.OrderItem;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.PoCode;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class PoRow extends ValueDTO {
	
//	int poCodeId;
	String value;
	String supplierName;
	String itemName;
	BigDecimal amount;
	MeasureUnit measureUnit;
	OffsetDateTime contractDate;
	LocalDate deliveryDate;
	OrderStatus orderStatus;
	
	/**
	 * @param id
	 * @param contractTypeCode
	 * @param supplierName
	 * @param itemName
	 * @param amount
	 * @param measureUnit
	 * @param contractDate
	 * @param deliveryDate
	 * @param orderStatus
	 */
	public PoRow(@NonNull Integer id, PoCode poCode, String supplierName, String itemName, BigDecimal amount,
			MeasureUnit measureUnit, OffsetDateTime contractDate, LocalDate deliveryDate, OrderStatus orderStatus) {
		super(id);
//		this.poCodeId = poCodeId;
		this.value = poCode.getValue();
		this.supplierName = supplierName;
		this.itemName = itemName;
		this.amount = amount;
		this.measureUnit = measureUnit;
		this.contractDate = contractDate;
		this.deliveryDate = deliveryDate;
		this.orderStatus = orderStatus;
	}
	
	/**
	 * @param po
	 * @param orderItem
	 */
	public PoRow(PO po, OrderItem orderItem) {

		super(po.getId());
//		this.poCodeId = po.getPoCode().getId();
		this.value = po.getPoCode().getValue();
		this.supplierName = po.getSupplier().getName();
		this.itemName = orderItem.getItem().getValue();
		this.amount = orderItem.getNumberUnits();
		this.measureUnit = orderItem.getMeasureUnit();
		this.contractDate = po.getRecordedTime();
		this.deliveryDate = orderItem.getDeliveryDate();
		this.orderStatus = po.getOrderStatus();
		
	}
	
	public String getMeasureUnit() {
		return this.measureUnit.toString();
	}
	
	public String getOrderStatus() {
		return this.orderStatus.toString();
	}

	
}
